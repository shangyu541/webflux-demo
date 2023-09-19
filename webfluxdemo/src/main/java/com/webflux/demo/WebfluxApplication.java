package com.webflux.demo;

import com.webflux.demo.handler.PasswordHandlerFunction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/23
 * @since 1.0.0
 */
@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class,args);
    }

    @Bean
    public static RouterFunction<ServerResponse> routerFunction(PasswordHandlerFunction handlerFunction){
        return route(POST("/password"),handlerFunction);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        return serverHttpSecurity.csrf() .disable() .build();
    }


}
