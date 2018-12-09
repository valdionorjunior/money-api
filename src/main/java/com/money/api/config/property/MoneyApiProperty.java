package com.money.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

//configuracao de propeiedade
@ConfigurationProperties("money") //nome da configuracao
public class MoneyApiProperty {
	
	//default de configuração  de origen permitida para outros servidores
	private String originPermitida = "http://localhost:8000";
	
	private final Seguranca seguranca = new Seguranca();
	
	public Seguranca getSeguranca() {
		return seguranca;
	}
	

	public String getOriginPermitida() {
		return originPermitida;
	}


	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}


	//agrupamos por temas crindo uma classe do tema 
	public static class Seguranca{
		
		private boolean enableHttps; //o default do boolean é false, não ha necessidade de inicializacao

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
	
		
	}

}
