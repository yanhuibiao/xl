package com.xl.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * （3）Swagger常用注解
 * 在Java类中添加Swagger的注解即可生成Swagger接口文档，常用Swagger注解如下：
 * @Api：修饰整个类，描述Controller的作用
 * @ApiOperation：描述一个类的一个方法，或者说一个接口
 * @ApiParam：单个参数的描述信息
 * @ApiModel：用对象来接收参数
 * @ApiModelProperty：用对象接收参数时，描述对象的一个字段
 * @ApiResponse：HTTP响应其中1个描述
 * @ApiResponses：HTTP响应整体描述
 * @ApiIgnore：使用该注解忽略这个API
 * @ApiError ：发生错误返回的信息
 * @ApiImplicitParam：一个请求参数
 * @ApiImplicitParams：多个请求参数的描述信息
 *  @ApiImplicitParam属性：
 *
 * | 属性         | 取值   | 作用                                          |
 * | ------------ | ------ | --------------------------------------------- |
 * | paramType    |        | 查询参数类型                                  |
 * |              | path   | 以地址的形式提交数据                          |
 * |              | query  | 直接跟参数完成自动映射赋值                    |
 * |              | body   | 以流的形式提交 仅支持POST                     |
 * |              | header | 参数在request headers 里边提交                |
 * |              | form   | 以form表单的形式提交 仅支持POST               |
 * | dataType     |        | 参数的数据类型 只作为标志说明，并没有实际验证 |
 * |              | Long   |                                               |
 * |              | String |                                               |
 * | name         |        | 接收参数名                                    |
 * | value        |        | 接收参数的意义描述                            |
 * | required     |        | 参数是否必填                                  |
 * |              | true   | 必填                                          |
 * |              | false  | 非必填                                        |
 * | defaultValue |        | 默认值                                        |
 *
 *
 * @Api → @Tag
 * @ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
 * @ApiImplicitParam → @Parameter
 * @ApiImplicitParams → @Parameters
 * @ApiModel → @Schema
 * @ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
 * @ApiModelProperty → @Schema
 * @ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
 * @ApiParam → @Parameter
 * @ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")
 *
 *
 */



@Configuration
//@EnableSwagger2
public class OpenApiConfiguration {

    @Bean
    public OpenAPI springDocOpenAPI() {
        return new OpenAPI().info(new Info().title("Xl API")
                .description("Xl接口文档说明").version("v1.0.0-SNAPSHOT")
                .license(new License().name("xl").url("https://gitee.com/yanhuibiao/xl")))
                .externalDocs(new ExternalDocumentation().description("xl")
                .url("https://gitee.com/yanhuibiao/xl/wikis"));
    }
// 分组
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public")
//                .pathsToMatch("/public/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi privateApi() {
//        return GroupedOpenApi.builder()
//                .group("private")
//                .pathsToMatch("/private/**")
//                .build();
//    }

//    @Bean
//    public Docket buildDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(buildApiInfo())
//                .select()
//                // 要扫描的API(Controller)基础包
//                .apis(RequestHandlerSelectors.basePackage("com.yanhuiby"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo buildApiInfo() {
//        Contact contact = new Contact("yan","","");
//        return new ApiInfoBuilder()
//                .title("Fintech-API Overview")
//                .description("Fintech background api")
//                .contact(contact)
//                .version("1.0.0").build();
//    }
}
