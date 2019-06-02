package org.redrock.gobang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("org.redrock.gobang.mapper")
@ServletComponentScan
@SpringBootApplication
public class GobangApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GobangApplication.class, args);
    }
    //为了打包springboot项目
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
