package com.wolf.eshop.inventory;

import com.wolf.eshop.inventory.listener.InitListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.wolf.eshop.inventory.mapper")
public class EshopInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EshopInventoryApplication.class, args);
	}


	@Bean
	public ServletListenerRegistrationBean servletListenerRegistrationBean(){
		ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
		servletListenerRegistrationBean.setListener(new InitListener());
		return servletListenerRegistrationBean;
	}
}

