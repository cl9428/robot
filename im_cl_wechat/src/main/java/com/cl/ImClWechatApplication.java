package com.cl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;


@SpringBootApplication
@MapperScan(value = "com.cl.mapper")
@ComponentScan(basePackages = {"com.cl","com.n3r.idworker"})
public class ImClWechatApplication extends SpringBootServletInitializer {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ImClWechatApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ImClWechatApplication.class, args);
    }

}
