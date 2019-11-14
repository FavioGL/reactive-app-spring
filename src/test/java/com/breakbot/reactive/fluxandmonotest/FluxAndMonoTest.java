package com.breakbot.reactive.fluxandmonotest;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("spring","favio","gonzalez","reactor").concatWith(Flux.error(new RuntimeException("ooops flux fail!"))).
                concatWith(Flux.just("after error")).log();
        stringFlux.subscribe(System.out::println, (e)-> System.out.println(e.getMessage()),()-> System.out.println("flux completed"));


    }

    @Test
    public void fluxTestElementsWithoutError(){
        Flux<String> stringFlux = Flux.just("spring","favio","gonzalez","reactor").log();
        StepVerifier.create(stringFlux).expectNext("spring").expectNext("favio").expectNext("gonzalez").expectNext("reactor");
    }
    @Test
    public void fluxTestElementsWithError(){
        Flux<String> stringFlux = Flux.just("spring","favio","gonzalez","reactor").concatWith(Flux.error(new RuntimeException("ooops flux fail!"))).log();
        StepVerifier.create(stringFlux).expectNext("spring").expectNext("favio").expectNext("gonzalez").expectNext("reactor").expectError(RuntimeException.class).
                verify();

    }

    @Test
    public void fluxTestElementsCount(){
        Flux<String> stringFlux = Flux.just("spring","favio","gonzalez","reactor").log();
        StepVerifier.create(stringFlux).expectNextCount(4).verifyComplete();
    }
    @Test
    public void monoTest(){
        Mono<String> stringMono = Mono.just("favio").log();
        StepVerifier.create(stringMono).expectNext("favio").verifyComplete();
        //we can do the same as create error run time
    }
}
