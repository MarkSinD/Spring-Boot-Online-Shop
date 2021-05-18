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

        //*********************************************************************
        String dirName2 = "category-images";
        Path userPhotosDir2 = Paths.get(dirName2);

        String userPhotosPath2 = userPhotosDir2.toFile().getAbsolutePath();

        registry.addResourceHandler("/"+ dirName2 + "/**")
                .addResourceLocations("file:/" + userPhotosPath2 + "/");

        //*********************************************************************
        String dirName3 = "brand-logo";
        Path userPhotosDir3 = Paths.get(dirName3);

        String userPhotosPath3 = userPhotosDir3.toFile().getAbsolutePath();

        registry.addResourceHandler("/"+ dirName3 + "/**")
                .addResourceLocations("file:/" + userPhotosPath3 + "/");

        //*********************************************************************
        String dirName4 = "product-images";
        Path userPhotosDir4 = Paths.get(dirName4);

        String userPhotosPath4 = userPhotosDir4.toFile().getAbsolutePath();

        registry.addResourceHandler("/"+ dirName4 + "/**")
                .addResourceLocations("file:/" + userPhotosPath4 + "/");

        //*********************************************************************
        String dirName5 = "site-logo";
        Path userPhotosDir5 = Paths.get(dirName5);

        String userPhotosPath5 = userPhotosDir5.toFile().getAbsolutePath();

        registry.addResourceHandler("/"+ dirName5 + "/**")
                .addResourceLocations("file:/" + userPhotosPath5 + "/");
    }
}
