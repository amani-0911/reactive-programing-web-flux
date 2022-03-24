package org.sid.reactiveprograminwebflux.repo;

import org.sid.reactiveprograminwebflux.entities.Societe;
import org.sid.reactiveprograminwebflux.entities.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction,String> {

    Flux<Transaction> findBySociete(Societe societe);

}
