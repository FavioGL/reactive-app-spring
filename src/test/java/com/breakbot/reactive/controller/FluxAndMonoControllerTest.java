package com.breakbot.reactive.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTest {
    @Autowired
    WebTestClient testClient;

    @Test
    public void fluxApproachOne(){
        Flux<Integer> integerFlux = testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(integerFlux).expectSubscription().expectNext(1,2,3,4,5,6).verifyComplete();
    }
    //test
    @Test
    public void fluxApproachTwo(){
        testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Integer.class)
                .hasSize(6);
        //StepVerifier.create(integerFlux).expectSubscription().expectNext(1,2,3,4,5,6).verifyComplete();
    }
    @Test
    public void fluxApproachThree(){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        EntityExchangeResult<List<Integer>> result =  testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Integer.class)
                .returnResult();
         assertEquals(list,result.getResponseBody());
        //StepVerifier.create(integerFlux).expectSubscription().expectNext(1,2,3,4,5,6).verifyComplete();
    }
    @Test
    public void fluxApproachFour(){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Integer.class)
                .consumeWith((response)->{
                    assertEquals(list,response.getResponseBody());
                });
        //StepVerifier.create(integerFlux).expectSubscription().expectNext(1,2,3,4,5,6).verifyComplete();
    }

    @Test
    public void fluxStream(){
        Flux<Long> longFlux = testClient.get().uri("/fluxstream")
                .accept(MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();
        StepVerifier.create(longFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();

    }
    @Test
    public void monoStream(){
        testClient.get().uri("/mono")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .consumeWith((response)->{
                    assertEquals(1,response.getResponseBody());
                });
    }


}
