package com.money.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.money.api.model.Lancamento;
import com.money.api.model.Lancamento_;
import com.money.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{
	//implementação dos métodos LancamentoRepositoryQuery
	
	//para trabalhar com a consulta
	
	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
	
		CriteriaBuilder builder = manager.getCriteriaBuilder(); //cria a criteria
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		//filtros que seria o where de um sql
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
			//Filtrando
		Predicate[] predicates = criarRestriçoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		//
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		return query.getResultList(); 
	}

	private Predicate[] criarRestriçoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		//where descricao like '%asdasdadq%'
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(
					builder.like(builder.lower(root.get(Lancamento_.descricao)),"%"+lancamentoFilter.getDescricao().toLowerCase()+"%"));			
		}
		
		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}
		
		if(lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	

}
