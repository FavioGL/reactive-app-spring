package com.breakbot.reactive.repository;

import com.breakbot.reactive.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactorRepositoryTest {
    @Autowired
    ItemReactorRepository itemReactorRepository;

    List<Item> itemList = Arrays.asList(new Item("1","This is item 1",13.54)
            ,new Item("2","This is item 2",1378.32)
            ,new Item("3","This is item 3",182.32));

    @Before
    public void setup(){
        this.itemReactorRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactorRepository::save)
                .doOnNext((item -> {System.out.println("inserted item: " + item);}))
                .blockLast();
    }

    @Test
    public void getAllElements(){
        Flux<Item> itemFlux = itemReactorRepository.findAll();
        StepVerifier.create(itemFlux).expectSubscription()
                .expectNextCount(3).verifyComplete();
    }
}
