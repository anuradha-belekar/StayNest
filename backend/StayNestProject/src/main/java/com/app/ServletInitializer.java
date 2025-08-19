package com.app;


//utility class to configure and build springboot app
import org.springframework.boot.builder.SpringApplicationBuilder;
//spring boot class that enables to run in servlet container 
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(StayNestApplication.class);
	}

}
