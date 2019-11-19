package com.breakbot.reactive.initialize;

import com.breakbot.reactive.document.Item;
import com.breakbot.reactive.repository.ItemReactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
public class ItemDataInitialize implements CommandLineRunner {
    @Autowired
    ItemReactorRepository itemReactorRepository;

    List<Item> itemList = Arrays.asList(new Item("1","This is item 1",13.54)
            ,new Item("2","This is item 2",1378.32)
            ,new Item("3","This is item 3",182.32)
            ,new Item("4","This is item 4",1322.32)
            ,new Item("5","This is item 5",684.3)
            ,new Item(null,"This is item 6",1342.68)
    );

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    private void initialDataSetup() {
        this.itemReactorRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactorRepository::save)
                .subscribe((item -> {
                    System.out.println("inserted item: " + item);
                }));
    }
}
