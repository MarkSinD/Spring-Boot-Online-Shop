package com.shopme.shopmebackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/"+ dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");
    }
}
