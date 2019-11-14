package com.breakbot.reactive.fluxandmonotest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.*;
import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
    @Test
    public void transformUsingMap(){
        Flux<String> stringFlux = Flux.fromIterable(myList).map(s->s.toUpperCase()).log();
        StepVerifier.create(stringFlux).expectNextCount(5).verifyComplete();
    }

    @Test
    public void transformUsingMapInt(){
        Flux<Integer> stringFlux = Flux.fromIterable(myList).map(s->s.length()).log();
        StepVerifier.create(stringFlux).expectNext(3,5,7,7,7).verifyComplete();
    }
    @Test
    public void transformUsingMapIntRepeat(){
        Flux<Integer> stringFlux = Flux.fromIterable(myList).map(s->s.length()).repeat().log();
        StepVerifier.create(stringFlux).expectNext(3,5,7,7,7).verifyComplete();
    }
    @Test
    public void transformUsingFlatMap(){
        Flux<String> stringFlux = Flux.fromIterable(myList).flatMap(s-> {
            return Flux.fromIterable(convertToList(s));
        }).log();
        StepVerifier.create(stringFlux).expectNextCount(10).verifyComplete();
    }
    @Test
    public void transformUsingFlatMapParallel(){
        Flux<String> stringFlux = Flux.fromIterable(generateRandomListString(10)).window(2).flatMap(s->
            s.map(this::convertToList).subscribeOn(parallel())).flatMap( s-> Flux.fromIterable(s))
            //Flux of list of stings
        .log();
        StepVerifier.create(stringFlux).expectNextCount(20).verifyComplete();
    }
    @Test
    public void transformUsingFlatMapParallelInOrder(){
        Flux<String> stringFlux = Flux.fromIterable(generateRandomListString(10)).window(2).
                /*concatMap(s->
                    s.map(this::convertToList).subscribeOn(parallel())).flatMap( s-> Flux.fromIterable(s))*/
                //Flux of list of stings
                        flatMapSequential(s->
                        s.map(this::convertToList).subscribeOn(parallel())).flatMap( s-> Flux.fromIterable(s))
                .log();
        StepVerifier.create(stringFlux).expectNextCount(20).verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"new Value");
    }

    public List<String> generateRandomListString(int size) {
        List<String> result = new ArrayList<>();
        int length = 5;
        boolean useLetters = true;
        boolean useNumbers = false;
        for(int x=0; x<size; x++) {
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
            result.add(generatedString.toLowerCase());
        }

        return  result;
    }
}
