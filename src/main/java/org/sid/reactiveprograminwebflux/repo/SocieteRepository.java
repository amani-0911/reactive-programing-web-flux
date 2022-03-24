package org.sid.reactiveprograminwebflux.repo;

import org.sid.reactiveprograminwebflux.entities.Societe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SocieteRepository extends ReactiveMongoRepository<Societe,String> {
}
