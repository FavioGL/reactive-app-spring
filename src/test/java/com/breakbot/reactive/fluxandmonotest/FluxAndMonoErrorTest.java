package com.breakbot.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FluxAndMonoErrorTest {
    @Test
    public void fluxErrorHandlingTest(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<String> stringFlux = Flux.fromIterable(myList).concatWith(Flux.error(new RuntimeException("error oops!"))).concatWith(Flux.just("hugo")).log();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("ana","favio","cinthia","valeria","nicolas").expectError(RuntimeException.class).verify();
    }
    @Test
    public void fluxErrorHandlingResumeTest(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<String> stringFlux = Flux.fromIterable(myList).concatWith(Flux.error(new RuntimeException("error oops!")))//.concatWith(Flux.just("hugo"))
                .onErrorResume((e) -> {System.out.println("Exception: " + e); return Flux.just("default", "default2"); } ).log();
        StepVerifier.create(stringFlux).expectSubscription().expectNext("ana","favio","cinthia","valeria","nicolas").
                //expectError(RuntimeException.class)
                expectNext("default", "default2")
                .verifyComplete();
    }
    @Test
    public void fluxErrorHandlingCustomReturnErrorTest(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<String> stringFlux = Flux.fromIterable(myList).concatWith(Flux.error(new RuntimeException("error oops!"))).concatWith(Flux.just("hugo"))
                .onErrorMap((e)-> new CustomException(e))
                .log();


        StepVerifier.create(stringFlux).expectSubscription().expectNext("ana","favio","cinthia","valeria","nicolas").
                expectError(CustomException.class).verify();
    }
    @Test
    public void fluxErrorHandlingCustomReturnErrorRetryTest(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<String> stringFlux = Flux.fromIterable(myList).concatWith(Flux.error(new RuntimeException("error oops!"))).concatWith(Flux.just("hugo"))
                .onErrorMap((e)-> new CustomException(e)).retry(2) //retry will allow us to try again for example when a db is not available
                .log();


        StepVerifier.create(stringFlux).expectSubscription().expectNext("ana","favio","cinthia","valeria","nicolas").
                expectNext("ana","favio","cinthia","valeria","nicolas").expectNext("ana","favio","cinthia","valeria","nicolas").
                expectError(CustomException.class).verify();
    }
    @Test
    public void fluxErrorHandlingCustomReturnErrorRetry2Test(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<String> stringFlux = Flux.fromIterable(myList).concatWith(Flux.just("hugo"))
                .retry(2) //retry will allow us to try again for example when a db is not available
                .log();


        StepVerifier.create(stringFlux).expectSubscription().expectNext("ana","favio","cinthia","valeria","nicolas","hugo").
              //  expectNext("ana","favio","cinthia","valeria","nicolas").expectNext("ana","favio","cinthia","valeria","nicolas").
               verifyComplete();
    }

    @Test
    public void fluxErrorHandlingCustomReturnErrorRetryBackoffTest(){
        List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
        Flux<String> stringFlux = Flux.fromIterable(myList).concatWith(Flux.error(new RuntimeException("error oops!"))).concatWith(Flux.just("hugo"))
                .onErrorMap((e)-> new CustomException(e)).retryBackoff(2, Duration.ofSeconds(5))//retry will allow us to try again for example when a db is not available
                .log();


        StepVerifier.create(stringFlux).expectSubscription().expectNext("ana","favio","cinthia","valeria","nicolas").
                expectNext("ana","favio","cinthia","valeria","nicolas").expectNext("ana","favio","cinthia","valeria","nicolas").
                expectError(CustomException.class).verify();
    }
}
