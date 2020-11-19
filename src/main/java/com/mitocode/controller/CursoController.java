package com.mitocode.controller;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.mitocode.dto.CursoEstudianteDTO;
import com.mitocode.model.Estudiante;
import com.mitocode.model.Curso;
import com.mitocode.pagination.PageSupport;
import com.mitocode.service.IEstudianteService;
import com.mitocode.service.ICursoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/cursos")
public class CursoController {
	
	private static final Logger log = LoggerFactory.getLogger(CursoController.class);

	@Autowired
	private ICursoService service;
	
	@Autowired
	private IEstudianteService estudianteService;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Curso>>> listar(){

		service.listar().repeat(3).parallel().runOn(Schedulers.parallel()).subscribe(i -> log.info(i.toString()));
		Flux<Curso> fxCursos = service.listar();
		
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fxCursos)
				);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Curso>> listarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
					.map(p -> ResponseEntity.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(p)
					)
					.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Curso>> registrar(@Valid @RequestBody Curso curso, final ServerHttpRequest req){
		return service.registrar(curso)
				.map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				);
	}
	
	@PutMapping
	public Mono<ResponseEntity<Curso>> modificar(@Valid @RequestBody Curso curso) {
		return service.modificar(curso)
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
	
	private Curso cursoHateoas;
	
	@GetMapping("/hateoas/{id}")
	public Mono<EntityModel<Curso>> listarHateoasPorId(@PathVariable("id") String id) {
		Mono<Link> link1 = linkTo(methodOn(CursoController.class).listarPorId(id)).withSelfRel().toMono();
		Mono<Link> link2 = linkTo(methodOn(CursoController.class).listarPorId(id)).withSelfRel().toMono();
		return link1.zipWith(link2)
				.map(function((left, right) -> Links.of(left, right)))				
				.zipWith(service.listarPorId(id), (links, p) -> EntityModel.of(p, links));
	}
	
	
	@GetMapping("/pageable")
	public Mono<ResponseEntity<PageSupport<Curso>>> listarPagebale(
			@RequestParam(name = "page", defaultValue = "0") int page,
		    @RequestParam(name = "size", defaultValue = "10") int size
			){
		
		Pageable pageRequest = PageRequest.of(page, size);
		
		return service.listarPage(pageRequest)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)	
						)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/curso1")
	public Flux<Curso> listarCurso1(){
		Flux<Curso> fx = WebClient.create("http://localhost:8080/cursos")
							.get()							
							.retrieve()
							.bodyToFlux(Curso.class);
		return fx;
	}
	
	
	@GetMapping("/curso2")
	public Mono<ResponseEntity<CursoEstudianteDTO>> listarClient2() {
		Mono<Curso> curso = service.listarPorId("5f4ae6574baf876d32f85da2").subscribeOn(Schedulers.single()).defaultIfEmpty(new Curso());
	    Mono<Estudiante> estudiante = estudianteService.listarPorId("5f419c64a6750848fdf77197").subscribeOn(Schedulers.single()).defaultIfEmpty(new Estudiante());
	    
	    return Mono.zip(estudiante, curso, CursoEstudianteDTO::new)
	    		.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
				).defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
