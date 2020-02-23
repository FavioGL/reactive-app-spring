package com.breakbot.reactive.controller.v1;

import com.breakbot.reactive.constants.ItemConstants;
import com.breakbot.reactive.document.Item;
import com.breakbot.reactive.repository.ItemReactorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemControllerTest {
    @Autowired
    WebTestClient webTestClient;
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
    public void getAllItems(){
        webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4)
        .consumeWith(response -> {
            List<Item> itemlist = response.getResponseBody();
            itemlist.forEach( item ->{
                System.out.println("checking " + item);
            });
        });
    }
    @Test
    public void getAllItemsApproach3(){
        Flux<Item> itemsFlux = webTestClient.get().uri(ItemConstants.ITEM_END_POINT_V1).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(itemsFlux.log()).expectNextCount(4).verifyComplete();

                //.hasSize(4)
                /*.consumeWith(response -> {
                    List<Item> itemlist = response.getResponseBody();
                    itemlist.forEach( item ->{
                        System.out.println("checking " + item);
                    });
                })*/
    }
    @Test
    public void getOneItem(){
       webTestClient.get().uri(ItemConstants.LOAD_ONE_ITEM_V1.concat("/{id}"),"2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price",1378.32);

    }
    @Test
    public void saveOneItem(){
        Item item = new Item(null, "item 10",928.2);
        webTestClient.post().uri(ItemConstants.LOAD_ONE_ITEM_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.price",928.2).isNotEmpty();

    }

    @Test
    public void updateItem(){
        double newPrice = 938.93;
        String newDescription = "item desc X";
        Item item = new Item(null,newDescription,newPrice);
        webTestClient.put().uri(ItemConstants.UPDATE_ONE_ITEM_V1.concat("/{id}"),"2")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price",5);
    }

    @Test
    public void deleteItem(){
        webTestClient.delete().uri(ItemConstants.UPDATE_ONE_ITEM_V1.concat("/{id}"),"3")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

    }

}
