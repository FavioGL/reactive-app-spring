package com.breakbot.reactive.fluxandmonotest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> myList = Arrays.asList("ana","favio","cinthia","valeria","nicolas");
    @Test
    public void filterTest(){
        List<String> myArray = generateRandomListString(1000);
        Flux<String> stringFlux = Flux.fromIterable(myList).filter(s -> s.startsWith("a") ).log();
        StepVerifier.create(stringFlux).expectNext("ana").verifyComplete();

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
