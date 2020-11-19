package com.mitocode.model;

public class MatriculaItem {

	private Curso curso;

	public MatriculaItem() {
	}

	public MatriculaItem(Curso curso) {
		this.curso = curso;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
}
