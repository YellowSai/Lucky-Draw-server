package cn.jzcscw.generator.builder.impl.web;

import cn.jzcscw.generator.builder.IBuilder;
import cn.jzcscw.generator.config.BuilderConfig;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class ModelAddDTOBuilder extends IBuilder {
    {
        absoluteTemplatePath = "java/dto/modelAddDTO.ftl";
        isClass = true;
    }

    public ModelAddDTOBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "dto", model + "AddDTO.java");
    }
}
