package xyz.st.meethere;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.st.meethere.mapper")
public class MeetHereApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetHereApplication.class, args);
    }

}
