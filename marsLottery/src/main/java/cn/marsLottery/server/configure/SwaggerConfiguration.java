package cn.marsLottery.server.configure;

import cn.hutool.core.collection.CollectionUtil;
import cn.marsLottery.server.config.AppConfig;
import cn.marsLottery.server.consts.AuthConsts;
import cn.jzcscw.commons.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;

/**
 * swagger基本配置<br>
 * 利用Profile的机制，只在开发环境下开启<br>
 * 另外还可以通过property变量+enable()方法来禁用<br>
 * 详情可见https://stackoverflow.com/questions/27442300/disabling-swagger-with-spring-mvc
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
@Profile(value = {"dev", "test"})
public class SwaggerConfiguration {

    @Resource
    private AppConfig appConfig;

    /**
     * 对外接口Swagger2创建Docket的Bean
     *
     * @return
     */
    @Bean
    public Docket apiDocket() {
        return newGroupDocket("对外接口", "cn.marsLottery.server.controller.api", AuthConsts.FRONT_AUTHORIZATION_HEADER);
    }

    /**
     * 管理后台接口Swagger2创建Docket的Bean
     *
     * @return
     */
    @Bean
    public Docket admApiDocket() {
        return newGroupDocket("管理后台接口", "cn.marsLottery.server.controller.admapi", AuthConsts.AUTHORIZATION_HEADER);
    }

    /**
     * Swagger2创建该Api的基本信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("系统API接口RESTful APIs")
                .description("spring Boot 中构建RESTful API")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }

    /**
     * 创建一个分组文档
     *
     * @param groupName
     * @param packages
     * @param tokenName
     * @return
     */
    private Docket newGroupDocket(String groupName, String packages, String tokenName) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        if (StringUtil.isNotBlank(appConfig.getSwaggerHost())) {
            docket = docket.host(appConfig.getSwaggerHost());
        }
        //header或参数,可添加多个
        ParameterBuilder headerParameterBuilder = new ParameterBuilder();
        headerParameterBuilder.parameterType("header")
                .name(tokenName)
                .modelRef(new ModelRef("string"))
                .required(false).build();
        return docket.groupName(groupName).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(packages))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(CollectionUtil.newArrayList(headerParameterBuilder.build()));
    }

}
