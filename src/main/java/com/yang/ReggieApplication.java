package com.yang;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@SpringBootApplication
@MapperScan("com.yang.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {

    public static void main(String[] args) {
        log.info("{}", "程序启动成功。。。。。。");
        SpringApplication.run(ReggieApplication.class, args);
    }

}
