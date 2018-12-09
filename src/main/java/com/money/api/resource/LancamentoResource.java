package com.money.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.event.RecursoCriadoEvent;
import com.money.api.exceptionhandler.MoneyExceptionHandler.Erro;
import com.money.api.model.Lancamento;
import com.money.api.repository.LancamentoRepository;
import com.money.api.repository.filter.LancamentoFilter;
import com.money.api.repository.projection.ResumoLancamento;
import com.money.api.service.LancamentoService;
import com.money.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired 
	private MessageSource messageSource;
	
//	@GetMapping
//	public List<Lancamento>pesquisar(LancamentoFilter lancamentoFilter){
//		return lancamentoRepository.filtrar(lancamentoFilter);
//	}
	
	//pesquisa por paginação
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')") //dando permissão do OAuth 2 aos metodo // adicionado o  and #oauth2.hasScope('read') para somente leitura
	//do usuario cliente mobile // esse escopo é o scopo do cliente, ja o ROLE é para o Usuario
	public Page<Lancamento>pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable){
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}
	
	@GetMapping(params = "resumo")// pesquisa resumida, sem passar tudos os detalhes
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')") //dando permissão do OAuth 2 aos metodo // adicionado o  and #oauth2.hasScope('read') para somente leitura
	//do usuario cliente mobile // esse escopo é o scopo do cliente, ja o ROLE é para o Usuario
	public Page<ResumoLancamento>resumir(LancamentoFilter lancamentoFilter, Pageable pageable){
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')") //dando permissão do OAuth 2 aos metodo // adicionado o  and #oauth2.hasScope('read') para somente leitura
	//do usuario cliente mobile // esse escopo é o scopo do cliente, ja o ROLE é para o Usuario
	public ResponseEntity<Lancamento> buscaPeloCodigo(@PathVariable Long codigo){
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();	
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')") //dando permissão do OAuth 2 aos mpetodo //  and #oauth2.hasScope('read') para escrita por causa do novo cliet mobile que possue
	//permissão somente para leitura
	public ResponseEntity<Lancamento> criaLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
		Lancamento lancamentoSalva = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')") //dando permissão do OAuth 2 aos mpetodo //  and #oauth2.hasScope('read') para escrita por causa do novo cliet mobile que possue
	//permissão somente para leitura
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.delete(codigo);
	}
	
	//Excpetion não geral, então podemos tratala aqui mesmo
	@ExceptionHandler({PessoaInexistenteOuInativaException.class})
	public ResponseEntity<Object>handlerPessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString(); 
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
		
	}

}
