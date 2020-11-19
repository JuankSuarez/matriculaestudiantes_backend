package com.mitocode.controller;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitocode.model.Estudiante;
import com.mitocode.service.IEstudianteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

	@Autowired
	private IEstudianteService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Estudiante>>> listar(){
		Flux<Estudiante> fxEstudiantes = service.listar();
		
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fxEstudiantes)
				);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Estudiante>> listarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
					.map(p -> ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(p)
					)
					.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Estudiante>> registrar(@Valid @RequestBody Estudiante estudiante, final ServerHttpRequest req){
		return service.registrar(estudiante)
				.map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				);
	}
	
	@PutMapping
	public Mono<ResponseEntity<Estudiante>> modificar(@Valid @RequestBody Estudiante estudiante) {
		return service.modificar(estudiante)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				);
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable("id") String id){
		return service.listarPorId(id)
				.flatMap(p -> {
					return service.eliminar(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));						
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
					
	}
	
	private Estudiante platoHateoas;
	
	@GetMapping("/hateoas/{id}")
	public Mono<EntityModel<Estudiante>> listarHateoasPorId(@PathVariable("id") String id) {
		Mono<Link> link1 = linkTo(methodOn(EstudianteController.class).listarPorId(id)).withSelfRel().toMono();

		return service.listarPorId(id)
				.zipWith(link1, (p, links) -> EntityModel.of(p, links));

	}
}
