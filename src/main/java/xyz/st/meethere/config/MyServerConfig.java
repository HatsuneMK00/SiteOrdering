package xyz.st.meethere.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class MyServerConfig {
    public static String server;
    public static String port;

    Logger logger = LoggerFactory.getLogger(getClass());

    /*
    * 使用构造函数的自动注入
    * 可以自己控制自动注入和构造函数的执行顺序
    * 让自动注入先于构造函数中的调用
    * */
    @Autowired
    public MyServerConfig(Environment environment) {
        port = environment.getProperty("local.server.port");

        try {
            server = InetAddress.getLocalHost().getHostAddress();
            if (server.startsWith("192.168"))
                server = "localhost";
            logger.info("server ip address: " + server);
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(),e);
            server = "localhost";
        }
    }

}
