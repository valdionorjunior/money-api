package com.money.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)//Deixa a classe com prioridade muito alta, quero que essa classe seja um filtro com prioridade muito alta
//pois preciso analizar a requisição antes de tudo pois se for uma requisição que tenha o grantype fresh token, preciso adicionala na requisição.

public class RefreshTokenPostProcessorFilter implements Filter{//implementa um filtro comum, ou seja o do pacote javax.servlet
	
	@Override //utilizaremos apenas o doFilter
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request; // pego o http servlet request
		
		//verificando se a requisição é para "oauth/token" e se esta requisição possui como grant_type -> refresh_token, pois entao tenho a possibilidade 
		//de a requisição ter o cookie com refresh, por isso verifico tbm se a req possui cookie
		if("oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equals(req.getParameter("grant_type"))
					&& req.getCookies() != null) {
			//verifico todos os cookies da requisição para ver se tem algum com o nome que demos no processor "refresh_token"
			for(Cookie cookie : req.getCookies()) {
				if(cookie.getName().equals("refresh_token")){
					String refreshToken = cookie.getValue();
					//agora preciso adicionar novamente no Mapa de parametros da requisião, contudo eu n consigo alterar mais a requisição neste momento
					// que seria req.getParameterMap().put("", value); contudo sem a solução abaixo -> "SOLUCAO"
					//SOLUCAO *
					req = new MyHttpServletResquestWrapper(req, refreshToken); // sob escrevo a requisição repassando agora um ParameterMap no meu metodo MyHttpServletResquestWrapper
				}
			}
			
		}
		
		//continuo a cadeia do filtro
		chain.doFilter(req, response);
		
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}


	@Override
	public void destroy() {
		
	} 
	
	//SOLUCAO *
	//crio um nova classe que estende da HttpServletRequestWrapper
	static class MyHttpServletResquestWrapper extends HttpServletRequestWrapper{
		
		private String refreshToken;

		public MyHttpServletResquestWrapper(HttpServletRequest request, String refreshToken) { // passo no construtor n so o request mas o refresh token
			super(request);
			this.refreshToken = refreshToken;
		}
		
		//sobrescrev o ParameterMap para passar o refresh nele
		@Override
		public Map<String, String[]> getParameterMap() {
			// inicio o novo ParameterMap ja com os valores da requisição antiga getRequest().getParameterMap()
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap()); 
			//aqui altero o ParameterMap passando o nome e o valor que recoperei do cookie
			map.put("refresh_Token", new String [] { refreshToken });
			map.setLocked(true); //travo o ParameterMap para retornalo
			return map;
		}
		
	} 

}
