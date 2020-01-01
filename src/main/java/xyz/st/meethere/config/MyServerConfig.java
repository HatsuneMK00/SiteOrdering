package xyz.st.meethere.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class MyServerConfig implements ApplicationListener<WebServerInitializedEvent> {
    public static String server;
    public static String port;

    Logger logger = LoggerFactory.getLogger(getClass());

    /*
    * 使用构造函数的自动注入
    * 可以自己控制自动注入和构造函数的执行顺序
    * 让自动注入先于构造函数中的调用
    * */
//    @Autowired
    public MyServerConfig() {
        try {
            server = InetAddress.getLocalHost().getHostAddress();
            if (server.startsWith("192.168"))
                server = "localhost";
            logger.info(String.format("--------------------------------------server ip address: %s", server));
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(),e);
            server = "localhost";
        }
//        server="47.101.217.16";
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        port = String.valueOf(webServerInitializedEvent.getWebServer().getPort());
        logger.info(String.format("------------------------------------------port: %s", MyServerConfig.port));
    }
}
