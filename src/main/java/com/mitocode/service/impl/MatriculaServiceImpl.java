package com.mitocode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Matricula;
import com.mitocode.repo.IFacturaRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.IMatriculaService;

@Service
public class MatriculaServiceImpl extends CRUDImpl<Matricula, String> implements IMatriculaService {

	@Autowired
	private IFacturaRepo repo;
	
	@Override
	protected IGenericRepo<Matricula, String> getRepo() {
		return repo; 
	}

}
