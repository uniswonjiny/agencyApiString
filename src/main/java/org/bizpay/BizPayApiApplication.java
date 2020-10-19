package org.bizpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@MapperScan(basePackages = {"org.bizpay.mapper"})
@SpringBootApplication(scanBasePackages = {"org.bizpay.common.util" , "org.bizpay.controller" ,"org.bizpay.service" })
public class BizPayApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BizPayApiApplication.class, args);
	}

}
