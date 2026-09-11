package com.hmall.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;
import com.hmall.gateway.util.JwtTool;

import java.util.List;


@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request
        ServerHttpRequest request = exchange.getRequest();

        //2.判断是否需要做登录拦截
        if (isExclude(request.getPath().toString())) {
            //放行
            return chain.filter(exchange);
        }

        //3.获取token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");//获取authorization头
        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        }

        //4.检验并解析token
        Long userId = null;
        try{
            userId = jwtTool.parseToken(token);
        }catch (UnauthorizedException e){
            //拦截，设置响应状态码为401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }


        //TODO: 5.传递用户信息
        System.out.println("userId= " + userId);

        //6.放行
        return chain.filter(exchange);
    }

    private boolean isExclude(String path) {//判断是否需要做登录拦截
        for(String pathPattern : authProperties.getExcludePaths()){
            if(antPathMatcher.match(pathPattern, path)){
                return true;
            }
        }
        return false;
    }


    @Override
    public int getOrder() {
        return 0;
    }

}