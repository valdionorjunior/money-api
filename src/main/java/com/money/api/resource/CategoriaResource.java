package com.money.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.money.api.model.Categoria;
import com.money.api.repository.CategoriaRepository;

//recurso de Categoria -- REST - Class que expoe tudo de esta relacionado ao categoria
// é um controlador

@RestController //diz que a clase é um controlador Rest - retorno ja converte pra JASON
@RequestMapping("/categorias") //mapeamento  da requisição
public class CategoriaResource {
	
	@Autowired // faz com que o spring ache um implementação de categoria repository e injete dentro dela pra mim
	//procure uma implementação de categoria repository e injete
	private CategoriaRepository categoriaRepository; //vamos injetar o nosso repositorio criado 

	@GetMapping // metodo sera chamado via get em Categorias
	public List<Categoria> listar(){
		//injetando
		return categoriaRepository.findAll(); // ja faz um select * from Categoria,
												//buscando de forma direta no banco, se tem ele retorna as entidades
												//caso contrario retorna uma lista vazia
	}
	
	/*
	@GetMapping // metodo sera chamado via get em Categorias
	public ResponseEntity<?> listar(){ //mudamos o medoto para ser um resposta de entidade,
									   // o '?' significa que tem retorno indefinido, pode escolher o que retornar
		//injetando
		List<Categoria> categorias = categoriaRepository.findAll();// ja faz um select * from Categoria, buscando de forma direta no banco
		return !categorias.isEmpty()? ResponseEntity.ok(categorias): ResponseEntity.noContent().build();
				// se a entidade categorias n estiver vazia retorna as categorias,
				//caso contrario retorna que esta OK mas n tem nada pra mostrar 
				//um not found (404) se usar o metodo ".notFound()", o build() precisa colocar pra gerar o response entity
	}
	*/
	/*
	@PostMapping //anotação para salvar no banco - salvar uma nova categoria
	@ResponseStatus(HttpStatus.CREATED) //created é status 201 -@ResponseStatus ao executas o metodo abaixo ira retornar o status created 201
	public void criar(@RequestBody Categoria categoria) {//ResquestBody ira pegar o JASON e interpreta-lo pra salvar com oobjeto categoria decifrendo atributos
		 categoriaRepository.save(categoria);
	} 
	*/
	@PostMapping //anotação para salvar no banco - salvar uma nova categoria
		//retorno a categoria ja criada no Body @Valid valida com been validation a entidade no banco
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {//o HttpServletResponse para trabalahr com o Header
		 Categoria categoriaSalva = categoriaRepository.save(categoria); //Pego o codigo do id na hora de salvar
		 
		 URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")//uso o builder do Spring pegando a URI apartir da requisição atual
				 .buildAndExpand(categoriaSalva.getCodigo()).toUri();	//(o fromCurrentRequestUri), colocando um path(caminho), com builAndExpand passando o codigo
		 																//e salvando a URI numa variavel 		
		 response.setHeader("Location", uri.toASCIIString()); //retorno o Header da response passando o Header Location passando o uri.toASCIIString();       	                  
	
		 return ResponseEntity.created(uri).body(categoria); // retorno a categoria ja criada, ja repassando também o status 201 created
		 													// sem a necessidade de anotação do Spring @ResponseStatus(HttpStatus.CREATED)
	} 
	/*A biblioteca Jackson do spring (spring.jackson) é responsavel por serializar e deserializar os dados para o banco
	 * transforma JASON em OBJECT JAVA e o inverso!
	 * uma boa validação é configurar par aque o jason enviado n seja enviado com propriedas a mais 
	 * como por exemplo, na Entidade Categoria temos somente codigo e nome,
	 * caso o jason enviado poasua mais coisa, a bilbioteca irá recusar estourando o erro para o usuaario
	 * Configura-se na Aplication.Properties com a bilbioteca "spring.jackson.deserialization.fail-on-unknown-properties=true"
	 * o defaut da biblioteca é false, sendo necessario passar para true*/
	
	
	
	//retonar a categoria criada
	@GetMapping("/{codigo}") // buscando pelo codigo de forma variavel
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) { //codigo de gatMapping é um parametro do caminha(Path) 
		
		Categoria recCatecogia = categoriaRepository.findOne(codigo);
		
		return recCatecogia != null ? ResponseEntity.ok(recCatecogia) : ResponseEntity.notFound().build();
		
	}
}
