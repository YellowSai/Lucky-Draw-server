package cn.jzcscw.generator.builder;

import cn.hutool.core.io.FileUtil;
import cn.jzcscw.commons.util.StringUtil;
import cn.jzcscw.generator.bean.ModelBean;
import cn.jzcscw.generator.bean.TableBean;
import cn.jzcscw.generator.config.VueBuilderConfig;
import cn.jzcscw.generator.util.JdbcUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class IVueBuilder {

    private static final String[] intType = {"int", "tinyint", "smallint"};

    @Setter
    private DataSource dataSource;

    protected Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);

    protected String absoluteTemplatePath = null;

    public IVueBuilder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract void build(VueBuilderConfig config, String table, String model);

    protected void generateFile(VueBuilderConfig config, String table, String model, String directory, String fileName) {
        try {
            configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));
            Template template = configuration.getTemplate(absoluteTemplatePath);

            String baseDir = config.getProjectDir();

            baseDir = String.join(File.separator, baseDir, directory);

            File dir = FileUtil.file(baseDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = FileUtil.file(dir, fileName);
            FileWriter fileWriter = new FileWriter(file);
            TableBean tableBean = this.getTableInfo(table);

            Map<String, Object> dataModel = new HashMap<>();
            ModelBean modelBean = new ModelBean()
                    .setTable(table)
                    .setModel(model)
                    .setCamelCaseField(StringUtil.lowerFirst(model))
                    .setComment(tableBean.getComment().replaceAll("表$",""));
            List<ModelBean.Field> fieldList = this.getFieldList(table);
            modelBean.setFieldList(this.getFieldList(table));
            modelBean.setHasDate(fieldList.stream().anyMatch(s -> "Date".equals(s.getType())));
            modelBean.setHasBigDecimal(fieldList.stream().anyMatch(s -> "BigDecimal".equals(s.getType())));

            log.debug("======> {}", modelBean);
            dataModel.put("model", modelBean);
            dataModel.put("config", config);
            template.process(dataModel, fileWriter);
            fileWriter.flush();
            fileWriter.close();
            log.info("生成完毕=> {}", file);
        } catch (IOException e) {
            log.error("文件读写异常=>IOException {}", e);
        } catch (TemplateException e) {
            log.error("模板生成异常=>TemplateException {}", e);
        }
    }

    protected String getDbName() {
        if (dataSource == null) {
            throw new RuntimeException("没有设置数据源");
        }
        String sql = "select database()";
        JdbcUtil jdbc = null;
        try {
            jdbc = new JdbcUtil(dataSource);

            jdbc.prepareStatement(sql);
            Map map = jdbc.executeMap();
            String dbName = (String) map.get("database()");
            return dbName;
        } catch (SQLException e) {
            log.error("getDbName =>");
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.close();
            }
        }
        return null;
    }

    protected TableBean getTableInfo(String table) {
        if (dataSource == null) {
            throw new RuntimeException("没有设置数据源");
        }
        String dbName = this.getDbName();
        String sql = "select * from information_schema.tables where table_schema = '" + dbName + "' and table_name='" + table + "'";
        log.debug("sql->{}", sql);
        JdbcUtil jdbc = null;
        try {
            jdbc = new JdbcUtil(dataSource);

            jdbc.prepareStatement(sql);
            Map map = jdbc.executeMap();
            String comment = (String) map.get("TABLE_COMMENT");
            TableBean tableBean = new TableBean();
            tableBean.setComment(comment);
            return tableBean;
        } catch (SQLException e) {
            log.error("getTableInfo =>");
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.close();
            }
        }
        return null;
    }

    protected List<ModelBean.Field> getFieldList(String table) {
        if (dataSource == null) {
            throw new RuntimeException("没有设置数据源");
        }
        String sql = "show full fields from `" + table + "`";
        JdbcUtil jdbc = null;
        List<ModelBean.Field> fieldList = new ArrayList<>();
        try {
            jdbc = new JdbcUtil(dataSource);

            jdbc.prepareStatement(sql);
            List<Map<String, Object>> list = jdbc.executeList();
            if (list != null) {
                for (Map<String, Object> map : list) {
                    ModelBean.Field field = new ModelBean.Field();
                    String fieldName = (String) map.get("Field");
                    if (fieldName == null) {
                        fieldName = (String) map.get("COLUMN_NAME");
                    }
                    String key = (String) map.get("Key");
                    if (key == null) {
                        key = (String) map.get("COLUMN_KEY");
                    }
                    String extra = (String) map.get("Extra");
                    if (extra == null) {
                        extra = (String) map.get("EXTRA");
                    }

                    String comment = (String) map.get("Comment");
                    if (comment == null) {
                        comment = (String) map.get("COLUMN_COMMENT");
                    }

                    String type = (String) map.get("Type");
                    if (type == null) {
                        type = (String) map.get("COLUMN_TYPE");
                    }

                    field.setField(fieldName);
                    field.setCamelCaseField(StringUtil.toCamelCase(fieldName));
                    field.setComment(comment);
                    field.setType(getJavaType(type));
                    if (key.contains("PRI")) {
                        field.setPrimaryKey(true);
                    }
                    if (extra.contains("auto_increment")) {
                        field.setAutoIncrement(true);
                    }
                    fieldList.add(field);
                }
            }
        } catch (SQLException e) {
            log.error("getFieldList =>");
            e.printStackTrace();
        } finally {
            if (jdbc != null) {
                jdbc.close();
            }
        }
        return fieldList;
    }

    private static String getJavaType(String str) {
        if (StringUtil.isEmpty(str)) {
            return "";
        }
        str = str.toLowerCase();
        for (String type : intType) {
            if (str.startsWith(type)) {
                return "int";
            }
        }
        if (str.startsWith("bigint")) {
            return "Long";
        } else if (str.startsWith("float") || str.startsWith("decimal")) {
            return "BigDecimal";
        } else if (str.startsWith("double")) {
            return "Double";
        } else if (str.startsWith("date") || str.startsWith("time")) {
            return "Date";
        } else {
            return "String";
        }
    }
}
