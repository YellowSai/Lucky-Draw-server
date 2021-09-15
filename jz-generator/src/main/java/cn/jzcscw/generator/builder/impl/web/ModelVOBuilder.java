package cn.jzcscw.generator.builder.impl.web;

import cn.jzcscw.generator.builder.IBuilder;
import cn.jzcscw.generator.config.BuilderConfig;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class ModelVOBuilder extends IBuilder {
    {
        absoluteTemplatePath = "java/vo/modelVO.ftl";
        isClass = true;
    }

    public ModelVOBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "vo", model + "VO.java");
    }
}
