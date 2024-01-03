package com.dagu.lightchaser.config;

import com.dagu.lightchaser.global.GlobalVariables;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ImageUrlInterceptor extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/image/**")
                .addResourceLocations("file:/" + GlobalVariables.PROJECT_PATH + GlobalVariables.IMAGE_PATH);
        super.addResourceHandlers(registry);
    }
}
