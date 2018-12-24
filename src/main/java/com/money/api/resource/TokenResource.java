package com.money.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.config.property.MoneyApiProperty;

//Classe para retirar o refresh token do servidor e do cookie
@Profile("oauth-security")
@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	//passando configurações de producao
	@Autowired
	private MoneyApiProperty moneyApiProperty;
	
	//Métodos para revogar e pegar de volta o refresh token
	@DeleteMapping("/revoke") // quando chamar o delete na url "revoke"
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		//Removendo o refreshToken do cookie 
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
//		cookie.setSecure(false); //TODO: EM PRODUCAO SERÀ TRUE // abaixo resolucao
		cookie.setSecure(moneyApiProperty.getSeguranca().isEnableHttps());// passando true para producao
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);//Expira agora
		
		resp.addCookie(cookie);
		resp.setStatus(HttpStatus.NO_CONTENT.value());//apos adicionar o cookie na resposta, adiciono o status no value
	}
}
