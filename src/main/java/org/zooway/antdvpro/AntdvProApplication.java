package org.zooway.antdvpro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "org.zooway.antdvpro.mapper")
@SpringBootApplication(scanBasePackages = {"org.zooway.antdvpro"})
public class AntdvProApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntdvProApplication.class, args);
    }

}
