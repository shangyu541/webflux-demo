package com.webflux.demo.handler;

import com.webflux.demo.entity.PasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sun.security.util.Password;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/23
 * @since 1.0.0
 */
@Component
public class PasswordHandlerFunction implements HandlerFunction {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(18);

    @Override
    public Mono handle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PasswordDTO.class)
                .map(passwordDTO -> passwordEncoder.matches(passwordDTO.getRaw(), passwordDTO.getSecured()))
                .flatMap(ismatched -> ismatched ? ServerResponse.ok().build() : ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build().then(Mono.empty()));
    }
}
