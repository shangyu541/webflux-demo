package com.webflux.demo.dao;

import com.webflux.demo.entity.Order;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/22
 * @since 1.0.0
 */
@Repository
public interface OrderRepository {
    Mono<Order> findById(String id);
    Mono<Order> save(Order order);
    Mono<Void> deleteById(String id);
}
