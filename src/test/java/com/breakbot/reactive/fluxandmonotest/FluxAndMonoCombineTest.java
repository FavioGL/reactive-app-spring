package com.breakbot.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge(){
        Flux<String> stringFlux1 = Flux.just("a","b","c");
        Flux<String> stringFlux2 = Flux.just("d","e","f");
        Flux<String> mergeFlux = Flux.merge(stringFlux1,stringFlux2).log();
        StepVerifier.create(mergeFlux).expectSubscription().
                expectNext("a","b","c","d","e","f").verifyComplete();
    }

    @Test
    public void combineUsingMergeDelay(){
        Flux<String> stringFlux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("d","e","f").delayElements(Duration.ofSeconds(2));
        Flux<String> mergeFlux = Flux.merge(stringFlux1,stringFlux2).log();
        StepVerifier.create(mergeFlux).expectSubscription().expectNextCount(6)
                //expectNext("a","b","c","d","e","f")
                .verifyComplete();
    }
    @Test
    public void combineUsingMergeConcat(){
        Flux<String> stringFlux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("d","e","f").delayElements(Duration.ofSeconds(2));
        Flux<String> mergeFlux = Flux.concat(stringFlux1,stringFlux2).log();
        StepVerifier.create(mergeFlux).expectSubscription().expectNextCount(6)
                //expectNext("a","b","c","d","e","f")
                .verifyComplete();
    }
    @Test
    public void combineUsingMergeZip(){
        Flux<String> stringFlux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("d","e","f").delayElements(Duration.ofSeconds(2));
        Flux<String> mergeFlux = Flux.zip(stringFlux1,stringFlux2, (t1,t2)-> {
            return t1.concat(t2);
        }).log();
        StepVerifier.create(mergeFlux).expectSubscription().expectNextCount(3)
                //expectNext("a","b","c","d","e","f")
                .verifyComplete();
    }
}
