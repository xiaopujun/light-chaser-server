package com.dagu.lightchaser.config;

import com.dagu.lightchaser.config.interceptor.BasicValidRequestInterceptor;
import com.dagu.lightchaser.factory.WebParamEnumFactory;
import com.dagu.lightchaser.global.GlobalVariables;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class WebMvcConfig  extends WebMvcConfigurationSupport {

    private final String baseUrl;
    public WebMvcConfig(
            @Value("${springfox.documentation.swagger-ui.base-url:}") String baseUrl) {
        this.baseUrl = baseUrl;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String projectResourcePath = "file:" + GlobalVariables.PROJECT_RESOURCE_PATH;

        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.
                addResourceHandler(baseUrl + "/swagger-ui/**")

                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")

                .resourceChain(false);

        registry.addResourceHandler("/images/**").addResourceLocations(projectResourcePath + GlobalVariables.SOURCE_IMAGE_PATH);
        registry.addResourceHandler("/covers/**").addResourceLocations(projectResourcePath + GlobalVariables.COVER_PATH);

        super.addResourceHandlers(registry);

    }



    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new WebParamEnumFactory());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/pet")
                .allowedOrigins("http://editor.swagger.io");
        registry
                .addMapping("/v2/api-docs.*")
                .allowedOrigins("http://editor.swagger.io");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicValidRequestInterceptor()).addPathPatterns("/**");
    }
}
