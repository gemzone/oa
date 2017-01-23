package io.nzo.oauth;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter
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
		
		clients.inMemory()
            .withClient("my-trusted-client")
	            .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
	            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
	            .scopes("read", "write", "trust")
	            .secret("lkjhpoiu0987")
	            .accessTokenValiditySeconds(120)		//Access token is only valid for 2 minutes.
	            .refreshTokenValiditySeconds(600)		//Refresh token is only valid for 10 minutes.
		.and()
			.withClient("my-client-with-registered-redirect")
				.authorizedGrantTypes("authorization_code")
				.authorities("ROLE_CLIENT")
				.scopes("read", "write", "trust")
				.resourceIds(OaApplication.RESOURCE_ID)
				.redirectUris("https://test.nzo.io/oa/api/hello");
	}
	
	@Bean
	public TokenStore tokenStore()
	{
		return new JdbcTokenStore(dataSource);
	}
}
