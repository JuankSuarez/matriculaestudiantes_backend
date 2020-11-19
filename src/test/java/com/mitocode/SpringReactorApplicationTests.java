package com.mitocode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mitocode.model.Curso;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringReactorApplicationTests {
	
	@Autowired
	private WebTestClient clienteWeb;

	@Test
	void listarTest() {
		clienteWeb.get()
		.uri("/cursos")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Curso.class)
		.hasSize(5);
	}
	
	@Test
	void registrarTest() {
		
		Curso curso = new Curso();
		curso.setNombre("MATEMATICAS");
		curso.setEstado(true);
		
		clienteWeb.post()
		.uri("/cursos")
		.body(Mono.just(curso), Curso.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.nombre").isNotEmpty();
	}
	
	@Test
	void modificarTest() {
		Curso curso = new Curso();
		curso.setId("5f4ae59c594d0e4b1cf3408a");
		curso.setNombre("MATEMATICAS");
		curso.setEstado(false);
		
		clienteWeb.put()
		.uri("/cursos")
		.body(Mono.just(curso), Curso.class)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.nombre").isNotEmpty();
	}
	
	@Test
	void eliminarTest() {
		Curso curso = new Curso();
		curso.setId("5f4ae5ffe66cc14f351e780a");
		
		clienteWeb.delete()
		.uri("/cursos/" + curso.getId())
		.exchange()
		.expectStatus().isNoContent();
	}

}
