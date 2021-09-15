package cn.jzcscw.generator.builder.impl.web;

import cn.jzcscw.generator.builder.IBuilder;
import cn.jzcscw.generator.config.BuilderConfig;

import javax.sql.DataSource;

/**
 * 控制层构造器
 */
public class ModelAdminControllerBuilder extends IBuilder {
    {
        absoluteTemplatePath = "java/controller/admapi/modelAdminController.ftl";
        isClass = true;
    }

    public ModelAdminControllerBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "controller.admapi", model + "AdminController.java");
    }
}
