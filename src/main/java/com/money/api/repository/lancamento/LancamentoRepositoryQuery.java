package com.money.api.repository.lancamento;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.money.api.model.Lancamento;
import com.money.api.repository.filter.LancamentoFilter;
import com.money.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	//criada para que possamos implementar um novo metodo
	//para que possamos implementar no nosso repository
	
	public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter filter, Pageable pageable);
}
