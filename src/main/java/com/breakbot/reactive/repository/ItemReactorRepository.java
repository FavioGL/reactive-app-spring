package com.breakbot.reactive.repository;

import com.breakbot.reactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReactorRepository extends ReactiveMongoRepository<Item,String> {
    Mono<Item> findByDescription(String description);

}
