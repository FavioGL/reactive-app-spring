package com.breakbot.reactive.repository;

import com.breakbot.reactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactorRepository extends ReactiveMongoRepository<Item,String> {
}
