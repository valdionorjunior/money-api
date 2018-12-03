package com.money.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component // É um componente do Spring
@Order(Ordered.HIGHEST_PRECEDENCE) // É um componente com prioridade bem alta
public class CorsFilter implements Filter{//será um filtro
	
	private String origemPermitida = "http://localhost:8000"; //TODO: Configurar pra diferentes ambientes

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//convertemos a requisição para uma HttpServletRequest e fazemos o mesmo com o response
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		//setandos os headers de origem permitida e 
		resp.setHeader("Access-Control-Allow-Origin", origemPermitida); // header pra qual origem será permitida
		resp.setHeader("Access-Control-Allow-Credentials", "true"); // header de credenciais para que o cookie seja enviado com o refreshToken
		
		
		//Verificamos se os os metodos da requisicao sao um OPTIONS OU NAO
		//verifico também se o header da minha requisicao é do tipo Origin
		//Permitindo o pre-flytRequest**
		if("OPTIONS".equals(req.getMethod())
				&& origemPermitida.equals(req.getHeader("Origin"))){
			//setandos os headers do CrossOrigin
			resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");// seto o header e pra quais metodos ele vai permitir , get, post, put...
			resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");// quais Header vai permitir
			resp.setHeader("Access-Control-Allow-Max-Age", "3600");//quanto tempo irar duracao para proxima requisição // no caso 1 hora
			resp.setStatus(HttpServletResponse.SC_OK); // dou uma resposta de OK
		}else { // se não for sigo o fluxo normal de requisição
			chain.doFilter(request, response);
		}
		
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}


	@Override
	public void destroy() {
		
	} 

}
