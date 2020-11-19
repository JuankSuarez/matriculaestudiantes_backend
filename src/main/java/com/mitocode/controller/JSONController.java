package com.mitocode.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitocode.model.Curso;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/json")
public class JSONController {

	@GetMapping(path = "/noStream", produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Curso> noStream() {
		return Flux.interval(Duration.ofMillis(100))
				.map(tick -> new Curso("1", "MATEMATICAS", "MAT", true));
		//[{},{},{},{},{}]
	}
	
	@GetMapping(path = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Curso> stream() {
		return Flux.interval(Duration.ofMillis(100))
				.map(tick -> new Curso("1", "MATEMATICAS", "MAT", true));
		//{}{}{}{}{}{}{}{}{}{}{}{}{}
	}
	
   @GetMapping(path = "/noStreamFinito", produces = MediaType.APPLICATION_JSON_VALUE)
   public Flux<Curso> fluxFinitonoStream() {
       return Flux.range(0, 5000)
               .map(tick -> new Curso("1", "MATEMATICAS", "MAT", true));
    }
	
	@GetMapping(path = "/streamFinito", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Curso> fluxFinitoStream() {
        return Flux.range(0, 5000)
                .map(tick -> new Curso("1", "MATEMATICAS", "MAT", true));
    }
}
