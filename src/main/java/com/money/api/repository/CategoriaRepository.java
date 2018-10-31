package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Categoria;

public interface CategoriaRepository  extends JpaRepository<Categoria, Long>{
	
//	Ao extender com JpaRepository - a interface adquece os comandos de Crud (delete, save, update)
//	Quem irá implementar essa interface será o Spring data JPA
//	É necessario configurar a categoria e o tipo de chave primaria, pois como é generico preciso falar pra qual
//	qual entidade e sua chave primaria a qual ele irá atuar

	
}
