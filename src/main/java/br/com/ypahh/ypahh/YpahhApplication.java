package br.com.ypahh.ypahh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = "br.com.ypahh.ypahh") 
@SpringBootApplication
public class YpahhApplication {

	public static void main(String[] args) {
		SpringApplication.run(YpahhApplication.class, args);
	}
}
