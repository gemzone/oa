package io.nzo.social;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SocialConfig implements SocialConfigurer
{

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env)
	{
		cfConfig.addConnectionFactory(new FacebookConnectionFactory(env.getProperty("facebook.clientId"),
				env.getProperty("facebook.clientSecret")));
	}

	@Override
	public UserIdSource getUserIdSource()
	{
		return null;
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator arg0)
	{
		return null;
	}

}