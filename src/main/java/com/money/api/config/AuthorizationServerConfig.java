package com.money.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()//tipo do cliente, poderia se no jdbc
			.withClient("angular")//nome do cliente
			.secret("@ngul@r0")//senha do cliente
			.scopes("read", "write")//qual scopo do cliente, pode-se definir scopos diferentes para clientes diferentes
			.authorizedGrantTypes("password")//define qual o GranType utilizado - nesse caso o password flow
			.accessTokenValiditySeconds(1800);//tempo em segundo de validade do token
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore()) // armazena o token recebido, para que a Angular venha buscar o token e devolva para poder acessar a api /lancamento
			.authenticationManager(authenticationManager);//valida se ta tudo certo
	}
	
	@Bean
	public TokenStore tokenStore() { //armazena o toquen
		return new InMemoryTokenStore(); // poderia ser armazenado no banco
	}
}
