package xyz.st.meethere.config;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.st.meethere.service.FileService;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        System.out.println(new ApplicationHome(FileService.class).getSource().getParentFile().getPath() + "/images/");
        registry.addResourceHandler("/images/**")
        .addResourceLocations("file:D:/JavaProject/meethere/target/images/")
        .addResourceLocations("file:/root/meethere/images/");
    }
}
