package org.sid.reactiveprograminwebflux.web;

import lombok.Data;
import org.sid.reactiveprograminwebflux.entities.Societe;
import org.sid.reactiveprograminwebflux.entities.Transaction;
import org.sid.reactiveprograminwebflux.repo.SocieteRepository;
import org.sid.reactiveprograminwebflux.repo.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@RestController
public class TransactionReactiveController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SocieteRepository societeRepository;




    @GetMapping("/transactions")
    public Flux<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    @GetMapping(value = "/transactionsBySociete/{id}")
    public Flux<Transaction> transactionsBySociete(@PathVariable String id){

Societe societe=new Societe();
societe.setId(id);
        return transactionRepository.findBySociete(societe);
    }



    @GetMapping(value = "/streamTransactionsBySociete/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Transaction> streamtransactionsBySociete(@PathVariable String id){

      return   societeRepository.findById(id)
              .flatMapMany(societe -> {
            Flux<Long> interval=Flux.interval(Duration.ofMillis(1000));
            Flux<Transaction> transactionFlux=
                    Flux.fromStream(
                            Stream.generate(()->{
                                Transaction  transaction=new Transaction();
                                transaction.setInstant(Instant.now());
                                transaction.setSociete(societe);
                                transaction.setPrice(societe.getPrice()*(1+(Math.random()*12-6)/100));
                         return transaction;
                            })
                    );
            return Flux.zip(interval,transactionFlux)
                    .map(data->{
                        return data.getT2();
                    });
        });

    }


    @GetMapping("/transactions/{id}")
    public Mono<Transaction> getOne(@PathVariable String id){
        return transactionRepository.findById(id);
    }

    @PostMapping("/transactions")
    public Mono<Transaction> save(@RequestBody Transaction transaction){
        return transactionRepository.save(transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public Mono<Void> delete(@PathVariable String id){
        return transactionRepository.deleteById(id);
    }
    @PutMapping("/transactions/{id}")
    public Mono<Transaction> update(@PathVariable String id,@RequestBody Transaction tr){
        tr.setId(id);
        return transactionRepository.save(tr);
    }


    @GetMapping(value = "/event-service/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Double> events(@PathVariable String id){
   WebClient webClient=WebClient.create("http://localhost:8081");

        Flux<Double> result= webClient.get()
                .uri("/streamEvent/"+id)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve().bodyToFlux(Event.class).map(e->e.getValue());
    return  result;
    }



}

@Data
class Event {
    private Instant instant;
    private double value;
    private String societeId;
}