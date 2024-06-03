package com.dagu.lightchaser.config;

import com.dagu.lightchaser.factory.WebParamEnumFactory;
import com.dagu.lightchaser.global.GlobalVariables;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class StaticSourceInterceptor extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectResourcePath = "file:" + GlobalVariables.PROJECT_RESOURCE_PATH;
        registry.addResourceHandler("/static/images/**").addResourceLocations(projectResourcePath + GlobalVariables.SOURCE_IMAGE_PATH);
        registry.addResourceHandler("/static/covers/**").addResourceLocations(projectResourcePath + GlobalVariables.COVER_PATH);
        super.addResourceHandlers(registry);
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new WebParamEnumFactory());
    }
}
