package cn.jzcscw.generator.builder.impl.vue;

import cn.jzcscw.generator.builder.IVueBuilder;
import cn.jzcscw.generator.config.VueBuilderConfig;

import javax.sql.DataSource;

public class VueApiBuilder extends IVueBuilder {
    {
        absoluteTemplatePath = "admin/api/modelApi.ftl";
    }

    public VueApiBuilder(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void build(VueBuilderConfig config, String table, String model) {
        super.generateFile(config, table, model, "api", model + "Api.js");
    }
}
