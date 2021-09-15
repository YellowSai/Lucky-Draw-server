package cn.jzcscw.generator.builder.impl.vue;

import cn.jzcscw.generator.builder.IVueBuilder;
import cn.jzcscw.generator.config.VueBuilderConfig;

import javax.sql.DataSource;

public class VueIndexBuilder extends IVueBuilder {
    {
        absoluteTemplatePath = "admin/views/model/index.ftl";
    }

    public VueIndexBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(VueBuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "views/"+model, "Index.vue");
    }
}
