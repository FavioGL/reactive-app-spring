package com.breakbot.reactive.router;

import com.breakbot.reactive.handler.ItemsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@Slf4j
public class ExceptionsRoute {
    @Bean
    public RouterFunction<ServerResponse> exceptionRouter(ItemsHandler itemsHandler){
        return RouterFunctions.route(GET("/fun/exception").and(accept(MediaType.APPLICATION_JSON)), itemsHandler::getAllItemsError);
    }
}
