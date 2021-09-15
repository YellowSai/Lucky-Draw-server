package cn.jzcscw.generator.builder.impl.web;

import cn.jzcscw.generator.config.BuilderConfig;
import cn.jzcscw.generator.builder.IBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * 服务实现类构建器
 */

@Slf4j
public class ModelServiceImplBuilder extends IBuilder {
    {
        absoluteTemplatePath = "java/service/modelServiceImpl.ftl";
        isClass = true;
    }

    public ModelServiceImplBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "service.impl", model + "ServiceImpl.java");
    }
}
