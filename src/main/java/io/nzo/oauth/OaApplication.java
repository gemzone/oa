package io.nzo.oauth;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController

@SpringBootApplication
public class OaApplication
{
	
	public static final String RESOURCE_ID = "bb";
	
	public static void main(String[] args)
	{
		SpringApplication.run(OaApplication.class, args);
	}
	
	@RequestMapping("/hello")
	public String home()
	{
		return "Hello World";
	}
	
	@RequestMapping("/api/hello")
	public String home3()
	{
		return "Api Hello World";
	}
	
	@RequestMapping("/")
	public String home2()
	{
		return "root";
	}
	
	
	@Bean
	public ServletRegistrationBean h2servletRegistration() 
	{
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/h2console/*");
	    return registration;
	}
	
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
	public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration
	{
		//@Autowired
		//private WebSecurityConfig securityConfig;

		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler()
		{
			return new OAuth2MethodSecurityExpressionHandler();
		}
	}

}
