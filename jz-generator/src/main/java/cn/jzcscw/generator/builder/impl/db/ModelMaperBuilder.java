package cn.jzcscw.generator.builder.impl.db;

import cn.jzcscw.generator.config.BuilderConfig;
import cn.jzcscw.generator.builder.IBuilder;

import javax.sql.DataSource;

public class ModelMaperBuilder extends IBuilder {
    {
        absoluteTemplatePath = "resources/mybatis/modelMapper.ftl";
        isResources = true;
    }

    public ModelMaperBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, null, model + "Mapper.xml");
    }
}
