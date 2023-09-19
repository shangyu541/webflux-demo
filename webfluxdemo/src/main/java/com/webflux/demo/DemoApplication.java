package com.webflux.demo;

import com.webflux.demo.service.OrderHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }



    @Bean
    public RouterFunction<ServerResponse> routes(OrderHandler handler) {
        //包含两个参数：1 测试条件是否通过，如果通过，则路由到第二个参数指定的路由函数
        //判断请求路径是否匹配指定的模式（请求路径前缀）
        return nest(path("/orders"),
                //如果匹配通过，则路由到该路由函数
                nest(accept(MediaType.APPLICATION_JSON),
                        //判断请求的报文头字段accept是否匹配application_json媒体类型
                        //如果匹配，则路由到下面的路由函数，将/orders/{id}路由给handler的get方法
                        route(GET("/{ID}"), handler::get)
                                //如果是get请求/orders，则路由到handler的list
                                .andRoute(method(HttpMethod.GET), handler::list)
                //        如果contentType匹配，丙炔路径匹配orders 则路由到函数，
                //        如果是post函数，则路由到handler的create
                ).andNest(contentType(MediaType.APPLICATION_JSON), route(POST("/"), handler::create))

        );

    }

}
