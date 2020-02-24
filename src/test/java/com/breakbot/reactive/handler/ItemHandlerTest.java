package com.breakbot.reactive.handler;

import com.breakbot.reactive.constants.ItemConstants;
import com.breakbot.reactive.document.Item;
import com.breakbot.reactive.repository.ItemReactorRepository;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ItemHandlerTest {
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
        webTestClient.get().uri(ItemConstants.FUN_ITEM_END_POINT_V1).exchange()
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
    public void getAllItemsV2(){
        String testStr = "";
        Flux<Item> flux = webTestClient.get().uri(ItemConstants.FUN_ITEM_END_POINT_V1).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();
                /*.expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(response -> {
                    List<Item> itemlist = response.getResponseBody();
                    itemlist.forEach( item ->{
                        System.out.println("checking " + item);

                    });
                });*/
        StepVerifier.create(flux).expectNextCount(4).verifyComplete();
    }

    @Test
    public void createItem(){
        Item itemToBeCreated = new Item(null,"Iphone x",999.99);
        webTestClient.post().uri(ItemConstants.FUN_LOAD_ONE_ITEM_V1)
                .body(Mono.just(itemToBeCreated),Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone x")
                .jsonPath("$.price").isEqualTo(999.99);
    }
}
