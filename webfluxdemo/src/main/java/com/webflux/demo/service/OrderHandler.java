package com.webflux.demo.service;

import com.webflux.demo.dao.OrderRepository;
import com.webflux.demo.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/22
 * @since 1.0.0
 */
@Service
public class OrderHandler {

    final OrderRepository orderRepository;

    public OrderHandler(OrderRepository repository){
        orderRepository=repository;
    }

    public Mono<ServerResponse> create(ServerRequest request){
        return request.bodyToMono(Order.class)
                .flatMap(orderRepository::save)
                .flatMap(o -> ServerResponse.created(URI.create("/orders/"+o.getId()))
                .build()
                );
    }

    public Mono<ServerResponse> get(ServerRequest request){
        return orderRepository.findById(request.pathVariable("id")).flatMap(order ->
                ServerResponse.ok().syncBody(order)
                ).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> list(ServerRequest request){
        return null;
    }



}
