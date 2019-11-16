package com.breakbot.reactive.repository;

import com.breakbot.reactive.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
            ,new Item("3","This is item 3",182.32)
            ,new Item("4","This is item 4",1322.32)
    );

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

    @Test
    public void getItemById(){
        StepVerifier.create(itemReactorRepository.findById("3")).expectSubscription()
                .expectNextMatches((item -> item.getDescription().equals("This is item 3")))
                .verifyComplete();
    }
    @Test
    public void getItemByDescription(){
        StepVerifier.create(itemReactorRepository.findByDescription("This is item 1").log() ).expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    public void saveItem(){
        Item item = new Item("6","THIS IS ITEM 6",382.32);
        Mono<Item> itemMono = itemReactorRepository.save(item);
        StepVerifier.create(itemMono.log() ).expectSubscription()
                .expectNextMatches(item1 -> item.getId().equals("6"))
                .verifyComplete();
    }
    @Test
    public void updateItem(){
        Item item = new Item("6","THIS IS ITEM 6",382.32);
        Mono<Item> itemMono = itemReactorRepository.findById("1")
                .log().map(
                        item1 -> { item1.setPrice(3988.4); //setting the new price
                        return item1;})
                .flatMap( (item2) -> {return itemReactorRepository.save(item2);});

        StepVerifier.create(itemMono.log() ).expectSubscription()
                .expectNextMatches(item1 -> item.getPrice() == 382.32)
                .verifyComplete();
    }
    @Test
    public void deleteItem(){
        Mono<Void> itemMono = itemReactorRepository
                .findById("6")
                .map(Item::getId)
                .flatMap( id-> {
                    return itemReactorRepository.deleteById(id);
                });

        StepVerifier.create(itemMono.log() ).expectSubscription()
                .verifyComplete();
    }
}
