package com.dcms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@MapperScan("com.dcms.repository")
public class Dcms004Application {

    public static void main(String[] args) {
        SpringApplication.run(Dcms004Application.class, args);
    }

}
