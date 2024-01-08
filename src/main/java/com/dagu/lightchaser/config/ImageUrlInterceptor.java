package com.dagu.lightchaser.config;

import com.dagu.lightchaser.global.GlobalVariables;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ImageUrlInterceptor extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectResourcePath = "file:" + GlobalVariables.PROJECT_RESOURCE_PATH;
        registry.addResourceHandler("/images/**").addResourceLocations(projectResourcePath + GlobalVariables.SOURCE_IMAGE_PATH);
        registry.addResourceHandler("/covers/**").addResourceLocations(projectResourcePath + GlobalVariables.COVER_PATH);
        super.addResourceHandlers(registry);
    }
}
