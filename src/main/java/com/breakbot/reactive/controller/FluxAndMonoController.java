package com.breakbot.reactive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.time.Duration;

@RestController
public class FluxAndMonoController {
    @GetMapping(value = "/flux", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Integer> returnFlux(){
        return Flux.just(1,2,3,4,5,6).delayElements(Duration.ofMillis(250)).log();
    }
    @GetMapping(value = "/mono", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> returnMono(){
        return Mono.just(1).delayElement(Duration.ofMillis(250)).log();
    }
    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnFluxStream(){
        return Flux.interval(Duration.ofMillis(350)).log();
    }
}
