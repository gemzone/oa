package io.nzo.oauth;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.jdbcAuthentication()                
		    .dataSource(dataSource)
		    .usersByUsernameQuery("SELECT username, [password], [enabled] FROM [dbo].[user] WHERE username = ? ")             
		    .authoritiesByUsernameQuery("SELECT principal, [role] FROM [dbo].[auth] WHERE principal = ? ")	// principal = username           
		    .rolePrefix("ROLE_");	// ADMIN, USER, MANAGER
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		// frameset 허용
		// http.headers().frameOptions().disable();

		http.csrf().disable()
		.anonymous()
		.disable()
		.authorizeRequests()
		.antMatchers("/oauth/token", "/oauth/authorize").permitAll();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Bean
	public TokenStore tokenStore()
	{
		return new InMemoryTokenStore();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore)
	{
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}

	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception
	{
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}
}
