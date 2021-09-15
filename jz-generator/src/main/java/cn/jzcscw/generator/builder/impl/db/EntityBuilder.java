package cn.jzcscw.generator.builder.impl.db;

import cn.jzcscw.generator.config.BuilderConfig;
import cn.jzcscw.generator.builder.IBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * 实体类构建器
 */

@Slf4j
public class EntityBuilder extends IBuilder {
    {
        absoluteTemplatePath = "java/po/model.ftl";
        isClass = true;
    }

    public EntityBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(BuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "po", model+".java");
    }
}
