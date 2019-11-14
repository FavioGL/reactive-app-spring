package com.breakbot.reactive.fluxandmonotest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFractoryTest {
    List<String> myList = Arrays.asList("favio","cinthia","valeria","nicolas");
    @Test
    public void fluxUsingInterable(){
        Flux<String> stringFlux = Flux.fromIterable(myList).log();
        StepVerifier.create(stringFlux).expectNext("favio","cinthia","valeria","nicolas").verifyComplete();
    }

    @Test
    public void fluxUsingArray(){
        String[] names = new String[]{"favio","cinthia","valeria","nicolas"};
        Flux<String> arrayFlux = Flux.fromArray(names).log();
        StepVerifier.create(arrayFlux).expectNext("favio","cinthia","valeria","nicolas").verifyComplete();

    }

    @Test
    public void fluxUsingStream(){
        List<String> myArray = generateRandomListString(100);
        Flux<String> namesFlux = Flux.fromStream(myArray.stream()).log();
        //StepVerifier.create(namesFlux).expectNext("a").verifyComplete();
        StepVerifier.create(namesFlux).expectNextCount(100).verifyComplete();
    }

    public List<String> generateRandomListString(int size) {
        List<String> result = new ArrayList<>();
        int length = 5;
        boolean useLetters = true;
        boolean useNumbers = false;
        for(int x=0; x<size; x++) {
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
            result.add(generatedString);
        }

        return  result;
    }
}
