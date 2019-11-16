package com.breakbot.reactive.repository;

import com.breakbot.reactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactorRepository extends ReactiveMongoRepository<Item,String> {
    Flux<Item> findByDescription(String description);
}
