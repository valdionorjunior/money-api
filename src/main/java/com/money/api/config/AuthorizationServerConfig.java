package com.money.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
				.authorizedGrantTypes("password", "refresh_token")//define qual o GranType utilizado - nesse caso o password flow // atualizado para ter um novo GranType refresh_token,
	//			para que o proprio browser posssa atualizar o token sem que redirecionet o usuario para tela de login.
				.accessTokenValiditySeconds(1800)//tempo em segundo de validade do token // 1800 sec para 30 min // atualizado par a20 sec
				.refreshTokenValiditySeconds(3600 * 24) //validade to refresh_token -> neste caso 3600 é 1 h que * 24 da um dia inteiro de refresh. pode dar refresh um dia inteiro
			.and()
				.withClient("mobile")//Adicionado outro usuario client somente para leitura
				.secret("m0b1l30")
				.scopes("read")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800)
				.refreshTokenValiditySeconds(3600 * 24);
	
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore()) // armazena o token recebido, para que a Angular venha buscar o token e devolva para poder acessar a api /lancamento
			.accessTokenConverter(accessTokenConverter())//conversor de token para jwt
			.reuseRefreshTokens(false)//habilita o reuso to refresh token se o usuario estiver logado, o refresh tem validade igual a validade do token
//			que esta cetado no accessTokenValiditySeconds(20), ou seja cada refresh , gera outro token de 20 sec.
//			onde os fresh poden ser utilizado de acordo com refreshTokenValiditySeconds(3600 * 24); qu neste caso pode dar refresh um dia todo.
			.authenticationManager(authenticationManager);//valida se ta tudo certo
	}
		//UTILIZANDO JWT PARA ENVIO E GERAÇÂ ODE TOKEN SEM PRECISAR GRAVAR EM MEMORIA O TOKEN
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() { //metodo  que converte o token de sessao para jwt
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("Bwi280281*"); //senha que valida a ultima parate do JWT (JASON Web Token - https://jwt.io/)
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() { //gera token
//		return new InMemoryTokenStore(); // poderia ser armazenado no banco
		return new JwtTokenStore(accessTokenConverter()); //gera to JWT
	}
}
