package org.sid.reactiveprograminwebflux;

import org.sid.reactiveprograminwebflux.entities.Societe;
import org.sid.reactiveprograminwebflux.entities.Transaction;
import org.sid.reactiveprograminwebflux.repo.SocieteRepository;
import org.sid.reactiveprograminwebflux.repo.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class ReactivePrograminWebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactivePrograminWebFluxApplication.class, args);
    }


    @Bean
    CommandLineRunner start(SocieteRepository societeRepository,
                            TransactionRepository transactionRepository){
        return args -> {
          societeRepository.deleteAll().subscribe(null,null,()->{
              Stream.of("SG","AWB","BMCE","AXA").forEach(s->{
                  societeRepository.save(new Societe(s,s,100+Math.random()*900))
                          .subscribe(societe -> {
                              System.out.println(societe.toString());
                          });
              });
              transactionRepository.deleteAll().subscribe(null,null,() -> {
                  Stream.of("SG","AWB","BMCE","AXA").forEach(s->{
                      societeRepository.findById(s).subscribe(societe -> {
                          for (int i = 0; i <10 ; i++) {

                              Transaction  transaction=new Transaction();
                              transaction.setInstant(Instant.now());
                              transaction.setSociete(societe);
                              transaction.setPrice(societe.getPrice()*(1+(Math.random()*12-6)/100));
                              transactionRepository.save(transaction).subscribe(t->{
                                  System.out.println(t.toString());
                              });
                          }
                      });
                  });
              });

          });
            System.out.println("apres lance delete......");




        };
    }
}
