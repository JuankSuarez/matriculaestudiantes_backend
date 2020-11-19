package com.mitocode.dto;

import com.mitocode.model.Estudiante;
import com.mitocode.model.Curso;

public class CursoEstudianteDTO {

	private Estudiante estudiante;
	private Curso curso;
	
	public CursoEstudianteDTO(Estudiante estudiante, Curso curso) {
		this.estudiante = estudiante;
		this.curso = curso;
	}
	public Estudiante getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	
}
