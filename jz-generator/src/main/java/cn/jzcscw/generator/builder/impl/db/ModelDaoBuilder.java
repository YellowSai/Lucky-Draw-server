package cn.jzcscw.generator.builder.impl.db;

import cn.jzcscw.generator.config.BuilderConfig;
import cn.jzcscw.generator.builder.IBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class ModelDaoBuilder extends IBuilder {
    {
        absoluteTemplatePath = "java/dao/modelDao.ftl";
        isClass = true;
    }

    public ModelDaoBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "dao", model + "Dao.java");
    }
}
