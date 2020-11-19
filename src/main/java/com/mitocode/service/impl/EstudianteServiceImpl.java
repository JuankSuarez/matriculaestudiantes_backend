package com.mitocode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Estudiante;
import com.mitocode.repo.IEstudianteRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.IEstudianteService;

@Service
public class EstudianteServiceImpl extends CRUDImpl<Estudiante, String> implements IEstudianteService {

	@Autowired
	private IEstudianteRepo repo;
	
	@Override
	protected IGenericRepo<Estudiante, String> getRepo() {
		return repo; 
	}

}
