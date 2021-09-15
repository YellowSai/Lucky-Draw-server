package cn.jzcscw.generator.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ModelBean {
    /**
     * 数据库名
     */
    private String database;
    /**
     * 表名
     */
    private String table;
    /**
     * 实体类名
     */
    private String model;
    /**
     * 小驼峰变量名
     */
    private String camelCaseField;
    /**
     * 功能描述
     */
    private String comment;
    /**
     * 属性列表
     */
    private List<Field> fieldList;

    private boolean hasBigDecimal;

    private boolean hasDate;

    @Data
    @Accessors(chain = true)
    public static class Field {
        private String field;
        private String camelCaseField;
        private String comment;
        private String type;
        private boolean primaryKey;
        private boolean autoIncrement;
    }
}
