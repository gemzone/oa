package io.nzo.oauth;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController

@SpringBootApplication
public class OaApplication
{
	
	public static final String RESOURCE_ID = "data";
	
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
	public ServletRegistrationBean h2servletRegistration() {
		
		
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/h2console/*");
	    return registration;
	}
	

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter
	{
		@Override
		public void configure(HttpSecurity http) throws Exception
		{
			//http.headers().frameOptions().disable();
			
			http
	        .anonymous().disable()
	        .requestMatchers().antMatchers("/api/**")
	        .and().authorizeRequests()
	        .antMatchers("/api/**").access("hasRole('ADMIN')")
	        .and()
	        .exceptionHandling()
	        .accessDeniedHandler(new OAuth2AccessDeniedHandler());
			
//			.requestMatchers()
//			.antMatchers("/", "/admin/beans")
//			.and()
			
//			http.authorizeRequests()
//				.antMatchers("/hello").permitAll()
//				.antMatchers("/**").authenticated();
			
//			requestMatchers()
//				.antMatchers("/**")
//				.and()
//				.authorizeRequests()
//				.anyRequest()
//				.access("#oauth2.hasScope('read')");
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception
		{
			// resources.resourceId(RESOURCE_ID);			
			resources.resourceId(RESOURCE_ID).stateless(false);
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter
	{   
		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		private DataSource dataSource;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
		{
			endpoints.tokenStore(new JdbcTokenStore(dataSource)).authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception
		{
			clients.jdbc(dataSource);
		}

		@Bean
		public TokenStore tokenStore()
		{
			return new JdbcTokenStore(dataSource); // access and refresh tokens
													// will be maintain in
													// database
		}
	    
	    
/*
		private static String REALM = "MY_OAUTH_REALM";
		
	    @Autowired
	    private TokenStore tokenStore;
	    @Override
	    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
	        endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
	                .authenticationManager(authenticationManager);
	    }
	    
	    @Autowired
	    private UserApprovalHandler userApprovalHandler;
	    
	    @Autowired
	    @Qualifier("authenticationManagerBean")
	    private AuthenticationManager authenticationManager;
		
	    @Override
	    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	        oauthServer.realm(REALM + "/client");
	    }

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception
		{
			// clients.jdbc(dataSource);
			
			clients.inMemory()
	            .withClient("my-trusted-client")
	            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
	            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
	            .scopes("read", "write", "trust")
	            .secret("secret")
	            .accessTokenValiditySeconds(120)		//Access token is only valid for 2 minutes.
	            .refreshTokenValiditySeconds(600);		//Refresh token is only valid for 10 minutes.
		}
*/		
	}

	
	
	@Configuration
	@EnableWebSecurity
	public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter 
	{
	    @Autowired
	    private ClientDetailsService clientDetailsService;
	    
	    @Autowired
	    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
	        auth.inMemoryAuthentication()
	        .withUser("bill").password("abc123").roles("ADMIN").and()
	        .withUser("bob").password("abc123").roles("USER");
	    }
	    
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	
	    	
	    	http.headers().frameOptions().disable();
	    	
	        http
	        .csrf().disable()
	        .anonymous().disable()
	        .authorizeRequests()
	        .antMatchers("/oauth/token").permitAll();
	    }
	 
	    @Override
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
	 
	 
	    @Bean
	    public TokenStore tokenStore() {
	        return new InMemoryTokenStore();
	    }
	 
	    @Bean
	    @Autowired
	    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
	        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
	        handler.setTokenStore(tokenStore);
	        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
	        handler.setClientDetailsService(clientDetailsService);
	        return handler;
	    }
	     
	    @Bean
	    @Autowired
	    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
	        TokenApprovalStore store = new TokenApprovalStore();
	        store.setTokenStore(tokenStore);
	        return store;
	    }
	     
	}
	
	
	
	@Configuration
	@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
	public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	    @Autowired
	    private OAuth2SecurityConfiguration securityConfig;
	 
	    @Override
	    protected MethodSecurityExpressionHandler createExpressionHandler() {
	        return new OAuth2MethodSecurityExpressionHandler();
	    }
	}
	
}
