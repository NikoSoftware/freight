package net.xiaomotou.freight;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@MapperScan("net.xiaomotou.freight")
public class FreightApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreightApplication.class, args);
    }

}
