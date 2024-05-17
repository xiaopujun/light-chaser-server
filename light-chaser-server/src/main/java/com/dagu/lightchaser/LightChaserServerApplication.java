package com.dagu.lightchaser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = {"com.dagu.lightchaser", "lightchaser.core","lightchaser.data.provider"})
@MapperScan("com.dagu.lightchaser.mapper")
public class LightChaserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightChaserServerApplication.class, args);
    }

}
