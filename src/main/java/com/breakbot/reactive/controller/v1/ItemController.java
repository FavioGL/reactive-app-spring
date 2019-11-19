package com.breakbot.reactive.controller.v1;

import com.breakbot.reactive.constants.ItemConstants;
import com.breakbot.reactive.document.Item;
import com.breakbot.reactive.repository.ItemReactorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    ItemReactorRepository itemReactorRepository;
    List<Item> itemList = Arrays.asList(new Item("1","This is item 1",13.54)
            ,new Item("2","This is item 2",1378.32)
            ,new Item("3","This is item 3",182.32)
            ,new Item("4","This is item 4",1322.32)
    );


    @GetMapping(ItemConstants.ITEM_END_POINT_V1)
    public Flux<Item> getAllItems(){
        return itemReactorRepository.findAll();
    }

    @GetMapping(ItemConstants.LOAD_ALL_ITEMS_V1)
    public Mono<String> loadAllItems(){
        setup();
        Mono<String> stringMono = Mono.just("Done!");
        return stringMono;
    }

    public void setup() {
        this.itemReactorRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactorRepository::save)
                .doOnNext((item -> {
                    System.out.println("inserted item: " + item);
                }));
    }
}