package com.mitocode.repo;

import com.mitocode.model.Curso;

public interface IPlatoRepo extends IGenericRepo<Curso, String>{

	/*@Query("find().skip(:page).limit(:size)")
	Flux<Plato> getPagina(@Param("page") int page, @Param("size") int size);*/
}
