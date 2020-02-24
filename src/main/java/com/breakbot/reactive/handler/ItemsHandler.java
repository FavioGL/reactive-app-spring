package com.breakbot.reactive.handler;

import com.breakbot.reactive.constants.ItemConstants;
import com.breakbot.reactive.document.Item;
import com.breakbot.reactive.repository.ItemReactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemsHandler {
    @Autowired
    ItemReactorRepository itemReactorRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest){
        return  ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactorRepository.findAll(), Item.class);
    }
    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest){

        String id = serverRequest.pathVariable("id");
        Mono<Item> item = itemReactorRepository.findById(id);

        return item.flatMap(item1 -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromObject(item1) )).switchIfEmpty(notFound);
    }
    public Mono<ServerResponse> addItem(ServerRequest serverRequest){
        Mono<Item> itemToBeInserted = serverRequest.bodyToMono(Item.class);
        return itemToBeInserted
                .flatMap(item -> itemReactorRepository.save(item))
                .flatMap(item -> ServerResponse.created(URI.create(ItemConstants.FUN_LOAD_ONE_ITEM_V1+ "/" + item.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(item),Item.class))
                .switchIfEmpty(notFound);

    }
    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        Mono<Void> deletedItem = itemReactorRepository.deleteById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(deletedItem, Void.class);

    }
}
