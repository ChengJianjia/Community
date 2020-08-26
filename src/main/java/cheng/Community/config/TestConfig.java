package cheng.Community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;


@Configuration  //表示是配置类不是普通类
public class TestConfig {

    @Bean
    public SimpleDateFormat SimpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
