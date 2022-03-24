package org.sid.reactiveprograminwebflux.web;

import org.sid.reactiveprograminwebflux.entities.Societe;
import org.sid.reactiveprograminwebflux.repo.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SocieteReactiveController {

    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping("/societes")
    public Flux<Societe> findAll(){
        return societeRepository.findAll();
    }

    @GetMapping("/societes/{id}")
    public Mono<Societe> getOne(@PathVariable String id){
        return societeRepository.findById(id);
    }

    @PostMapping("/societes")
    public Mono<Societe> save(@RequestBody Societe societe){
        return societeRepository.save(societe);
    }

    @DeleteMapping("/societes/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return societeRepository.deleteById(id);
    }
    @PutMapping("/societes/{id}")
    public Mono<Societe> update(@PathVariable String id,@RequestBody Societe societe){
        societe.setId(id);
        return societeRepository.save(societe);
    }

}
