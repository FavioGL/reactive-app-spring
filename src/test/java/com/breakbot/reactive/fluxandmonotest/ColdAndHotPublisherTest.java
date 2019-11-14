package com.breakbot.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("a","b","c","d","e","f").delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(s-> System.out.println("subscriber 1:" + s));
        Thread.sleep(2000);
        stringFlux.subscribe(s-> System.out.println("subscriber 2:" + s));
        Thread.sleep(4000);

    }

    @Test
    //publish elemnts as stream not from begining.
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("a","b","c","d","e","f","g","h","i").delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(s-> System.out.println("subscriber 1:" + s));
        Thread.sleep(3000);
        connectableFlux.subscribe(s-> System.out.println("subscriber 2:" + s));
        Thread.sleep(4000);
        stringFlux.subscribe(s-> System.out.println("subscriber 3 (cold):" + s));
        Thread.sleep(2000);

    }
}
