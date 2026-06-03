package kr.co.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@MapperScan("kr.co.shop.repository.mapper")
@EnableRetry
@EnableCaching
@SpringBootApplication
public class EcommerceSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceSpringbootApplication.class, args);
    }

}
