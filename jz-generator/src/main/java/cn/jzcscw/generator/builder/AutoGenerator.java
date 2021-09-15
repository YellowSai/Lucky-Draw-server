package cn.jzcscw.generator.builder;

import cn.jzcscw.generator.builder.impl.db.EntityBuilder;
import cn.jzcscw.generator.builder.impl.db.ModelDaoBuilder;
import cn.jzcscw.generator.builder.impl.db.ModelMaperBuilder;
import cn.jzcscw.generator.builder.impl.vue.VueApiBuilder;
import cn.jzcscw.generator.builder.impl.vue.VueEditBuilder;
import cn.jzcscw.generator.builder.impl.vue.VueIndexBuilder;
import cn.jzcscw.generator.builder.impl.vue.VueListBuilder;
import cn.jzcscw.generator.builder.impl.web.ModelAddDTOBuilder;
import cn.jzcscw.generator.builder.impl.web.ModelAdminControllerBuilder;
import cn.jzcscw.generator.builder.impl.web.ModelServiceBuilder;
import cn.jzcscw.generator.builder.impl.web.ModelServiceImplBuilder;
import cn.jzcscw.generator.builder.impl.web.ModelUpdateDTOBuilder;
import cn.jzcscw.generator.builder.impl.web.ModelVOBuilder;
import cn.jzcscw.generator.config.BuilderConfig;
import cn.jzcscw.generator.config.VueBuilderConfig;

import javax.sql.DataSource;

public class AutoGenerator {
    private DataSource dataSource;

    public AutoGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generateNoController(BuilderConfig config, String tableName, String entityName) {
        generateEntity(config, tableName, entityName);
        generateDao(config, tableName, entityName);
        generateMapper(config, tableName, entityName);
        generateService(config, tableName, entityName);
    }

    public void generate(BuilderConfig config, String tableName, String entityName) {
        generateEntity(config, tableName, entityName);
        generateDao(config, tableName, entityName);
        generateMapper(config, tableName, entityName);
        generateService(config, tableName, entityName);

        generateController(config, tableName, entityName);
    }

    public void generateEntity(BuilderConfig config, String tableName, String entityName) {
        EntityBuilder entityBuilder = new EntityBuilder(dataSource);
        entityBuilder.build(config, tableName, entityName);
    }

    public void generateDao(BuilderConfig config, String tableName, String entityName) {
        ModelDaoBuilder daoBuilder = new ModelDaoBuilder(dataSource);
        daoBuilder.build(config, tableName, entityName);
    }

    public void generateMapper(BuilderConfig config, String tableName, String entityName) {
        ModelMaperBuilder modelMaperBuilder = new ModelMaperBuilder(dataSource);
        modelMaperBuilder.build(config, tableName, entityName);
    }

    public void generateService(BuilderConfig config, String tableName, String entityName) {
        ModelServiceBuilder modelServiceBuilder = new ModelServiceBuilder(dataSource);
        modelServiceBuilder.build(config, tableName, entityName);

        ModelServiceImplBuilder modelServiceImplBuilder = new ModelServiceImplBuilder(dataSource);
        modelServiceImplBuilder.build(config, tableName, entityName);
    }

    public void generateController(BuilderConfig config, String tableName, String entityName) {
        this.generateDTO(config, tableName, entityName);
        this.generateVO(config, tableName, entityName);

        ModelAdminControllerBuilder modelAdminControllerBuilder = new ModelAdminControllerBuilder(dataSource);
        modelAdminControllerBuilder.build(config, tableName, entityName);
    }

    public void generateDTO(BuilderConfig config, String tableName, String entityName) {
        ModelAddDTOBuilder modelAddDTOBuilder = new ModelAddDTOBuilder(dataSource);
        modelAddDTOBuilder.build(config, tableName, entityName);

        ModelUpdateDTOBuilder modelUpdateDTOBuilder = new ModelUpdateDTOBuilder(dataSource);
        modelUpdateDTOBuilder.build(config, tableName, entityName);
    }

    public void generateVO(BuilderConfig config, String tableName, String entityName) {
        ModelVOBuilder modelVOBuilder = new ModelVOBuilder(dataSource);
        modelVOBuilder.build(config, tableName, entityName);
    }

    public void generateAdmin(VueBuilderConfig config, String tableName, String entityName) {
        this.generateAdminIndex(config, tableName, entityName);

        this.generateAdminApi(config, tableName, entityName);
        this.generateAdminList(config, tableName, entityName);
        this.generateAdminEdit(config, tableName, entityName);
    }

    public void generateAdminIndex(VueBuilderConfig config, String tableName, String entityName) {
        VueIndexBuilder vueIndexBuilder = new VueIndexBuilder(dataSource);
        vueIndexBuilder.build(config, tableName, entityName);
    }

    public void generateAdminApi(VueBuilderConfig config, String tableName, String entityName) {
        VueApiBuilder vueApiBuilder = new VueApiBuilder(dataSource);
        vueApiBuilder.build(config, tableName, entityName);
    }

    public void generateAdminList(VueBuilderConfig config, String tableName, String entityName) {
        VueListBuilder vueListBuilder = new VueListBuilder(dataSource);
        vueListBuilder.build(config, tableName, entityName);
    }

    public void generateAdminEdit(VueBuilderConfig config, String tableName, String entityName) {
        VueEditBuilder vueEditBuilder = new VueEditBuilder(dataSource);
        vueEditBuilder.build(config, tableName, entityName);
    }
}
