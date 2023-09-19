package com.webflux.demo.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/22
 * @since 1.0.0
 */
public class ServerRedirectHandler implements HandlerFunction<ServerResponse> {

    final WebClient webClient = WebClient.create();

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        //如果出现“Redirect-Traffic”,他会将流量重定向到另一台服务器
        return webClient.method(request.method())
                .uri(request.headers().header("Redirect-Traffic")
                        .get(0))
                .headers((h) -> h.addAll(request.headers().asHttpHeaders()))
                .body(BodyInserters.fromDataBuffers(
                        request.bodyToFlux(DataBuffer.class)
                ))
                .cookies(c -> request.cookies().forEach((key, list) -> list.forEach(httpCookie -> c.add(key, httpCookie.getValue()))))
                .exchange().flatMap(clientResponse -> ServerResponse.status(clientResponse.statusCode())
                        .cookies(c -> c.addAll(clientResponse.cookies()))
                        .headers(httpHeaders -> httpHeaders.addAll(clientResponse.headers().asHttpHeaders()))
                        .body(BodyInserters.fromDataBuffers(clientResponse.bodyToFlux(DataBuffer.class)))
                );

    }
}
