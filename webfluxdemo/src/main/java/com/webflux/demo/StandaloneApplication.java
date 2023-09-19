package com.webflux.demo;

import com.webflux.demo.entity.PasswordDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 商玉
 * @create 2021/11/22
 * @since 1.0.0
 */
@Slf4j
public class StandaloneApplication {


    //函数式Web 框架允许在不启动整个Spring 基础设施的情况下构建Web 应用程序。
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        //调用routes方法，然后将routerFunction转换为httpHandler
        HttpHandler httpHandler = RouterFunctions.toHttpHandler(routes(
                //BCrypt算法进行18轮散列，这可能需要几秒钟来编码/匹配
                new BCryptPasswordEncoder(18))
        );
        //内置httpHandler适配器
        ReactorHttpHandlerAdapter reactorHttpHandlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
        //创建httpServer实例，它是ReactorNetty API的一部分
        DisposableServer localhost = HttpServer.create()
                //配置host
                .host("localhost")
                //配置端口
                .port(8080)
                //指定handler
                .handle(reactorHttpHandlerAdapter)
                //调用bindNow启动服务
                .bindNow();
        log.debug("Started in " + (System.currentTimeMillis() - l) + " ms");
        //为了使应用保持活动状态，阻塞住Thread 并监听服务器的处理事件
        localhost.onDispose().block();
    }

    private static RouterFunction<?> routes(BCryptPasswordEncoder bCryptPasswordEncoder) {
        //使用/check 路径吃力任何post方法的请求
        return route(POST("/password"), request -> request.bodyToMono(PasswordDTO.class)
                //使用passwordEncoder检查已加密密码的原始密码
                .map(p -> bCryptPasswordEncoder.matches(p.getRaw(), p.getSecured()))
                //如果密码与存储的密码匹配，则ServerResponse 将返回ok状态（200） 否则，返回EXPECTATION_FAILED（417）
                .flatMap(isMatched -> isMatched ? ServerResponse.ok().build()
                        : ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build())
        );
    }
}
