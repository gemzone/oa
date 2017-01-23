package io.nzo.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter
{
	
	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
			.antMatchers("/api/**").access("hasAnyRole('USER','MEMBER','MANAGER','ADMIN')")
			.anyRequest().authenticated()
			.and()
	    	.exceptionHandling()
	    	.accessDeniedHandler(new OAuth2AccessDeniedHandler());
			

//    	.anonymous().disable()
//    		.requestMatchers().antMatchers("/api/**")
//    	.and()
		
		
//		.requestMatchers()
//		.antMatchers("/", "/admin/beans")
//		.and()
		
//		http.authorizeRequests()
//			.antMatchers("/hello").permitAll()
//			.antMatchers("/**").authenticated();
		
//		requestMatchers()
//			.antMatchers("/**")
//			.and()
//			.authorizeRequests()
//			.anyRequest()
//			.access("#oauth2.hasScope('read')");
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception
	{
		resources.resourceId(OaApplication.RESOURCE_ID).stateless(false);
	}
}