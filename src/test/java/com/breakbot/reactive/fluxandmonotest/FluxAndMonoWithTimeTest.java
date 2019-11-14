package com.breakbot.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static reactor.core.publisher.Flux.interval;

public class FluxAndMonoWithTimeTest {
    @Test
    public void infiniteSequence(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(200)).log(); //start from 0 to ... n value
        longFlux.subscribe((e) ->{ System.out.println(e);});

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void infiniteSequenceControlled(){
        Flux<Long> longFlux = Flux.interval(Duration.ofMillis(200)).take(3).log(); //start from 0 to ... n value
        longFlux.subscribe((e) ->{ System.out.println(e);});

        StepVerifier.create(longFlux).expectSubscription().expectNextCount(3).verifyComplete();

    }
    @Test
    public void infiniteSequenceControlledMap(){
        Flux<Integer> longFlux = Flux.interval(Duration.ofMillis(200))
                .map(l->new Integer(l.intValue())).take(3).log(); //start from 0 to ... n value
        longFlux.subscribe((e) ->{ System.out.println(e);});

        StepVerifier.create(longFlux).expectSubscription().expectNextCount(3).verifyComplete();

    }
    @Test
    public void infiniteSequenceControlledMapDelay(){
        Flux<Integer> longFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map(l->new Integer(l.intValue())).take(3); //start from 0 to ... n value
        longFlux.subscribe((e) ->{ System.out.println(e);});

        StepVerifier.create(longFlux).expectSubscription().expectNextCount(3).verifyComplete();

    }
}
