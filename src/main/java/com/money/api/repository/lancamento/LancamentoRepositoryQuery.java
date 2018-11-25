package com.money.api.repository.lancamento;

import java.util.List;

import com.money.api.model.Lancamento;
import com.money.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	//criada para que possamos implementar um novo metodo
	//para que possamos implementar no nosso repository
	
	public List<Lancamento>filtrar(LancamentoFilter filter);
}
