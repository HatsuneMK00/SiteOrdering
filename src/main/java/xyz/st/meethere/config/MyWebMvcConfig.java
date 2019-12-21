package xyz.st.meethere.config;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.st.meethere.service.FileService;

@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    public static String server="localhost";
    public static String port="8080";
    public static String imageToStorage = new ApplicationHome(FileService.class).getSource().getParentFile().getPath() + "/images/";


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
        // local directory
        .addResourceLocations("file:D:/JavaProject/meethere/target/images/")
        // local dynamic directory
        .addResourceLocations("file:"+imageToStorage);
        // remote directory
        //.addResourceLocations("file:/root/meethere/images/");

        System.out.println(imageToStorage);
        // TOOD: after deploying on server, check [local dynamic directory] == [remote directory]
        // 因为我们需要从 ip:8080/images/xxx.jpg访问的图片与从FileService中存储的绝对路径
    }
}