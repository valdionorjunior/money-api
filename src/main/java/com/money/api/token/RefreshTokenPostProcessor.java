package com.money.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// Classe para que possamos retirar o refresh token to corpo de resposta e faze-lo trafegar em coocke HTTP onde é mais seguro e n pode se pego no JS

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{// o ResponseBodyAdvice pega a resposta passada no seu 
																						//parametro generico (objeto OAuth2 vindo no body), ou seja irá pegar so as resposta do meu OAuth2

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken"); // como pode vir objetos OAuth2 em outras repostas , como na de lançamento ou categoria por exemplo,
//		o suports consegue fazer esse trabalho nas outras respostas.
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		// Esse metodo recupera o corpo do objeto Oauth2 nas requisiçoes - sendo assim aqui podemos implementar nossas regras para que consguigamos colocar o token em coocke
		
		
		
		//Para adicionar o token preciso do request e do response do HttpServletRequest e do HttpServletResponse
		//Já recebo eles no corpo do método porem preciso convertelos
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		String refreshToken = body.getRefreshToken().getValue();// aqui pegamos o nosso refresh token passado
		
		//para remover temos de passar o body mas fazendo um cast 
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		//adicionando o refresh em coocke Http
		adicionarRefreshTokenNoCooKie(refreshToken, req, resp);
		
		//removendo o refresh token do body
		removerRefreshTokenDoBody(token);
		
		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null); // aqui retiramos token  setando ele no body como null 
		
	}

	private void adicionarRefreshTokenNoCooKie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		// Criando Cookie Http
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken); // damos um nome do token e passamos o token
		//Propriedades do Cookie
		refreshTokenCookie.setHttpOnly(true);// so será acessivel via Http
		refreshTokenCookie.setSecure(false);// ele sera acessivel apenas https ou não // funciona nos http e https por enquanto TODO: Mudar para true em producao
		refreshTokenCookie.setPath(req.getContextPath()+ "/oauth/token"); // para qual caminho o cookie será enviado pelo browser/ aqui usamos o request // se tiver contextPath ele busca e concatena
		refreshTokenCookie.setMaxAge(2592000); // em quanto tempo o cookie irá expirar em dias - 2592000 = a 30 dias
		resp.addCookie(refreshTokenCookie); // adicionamos o cookie na reposta
		
	}


}
