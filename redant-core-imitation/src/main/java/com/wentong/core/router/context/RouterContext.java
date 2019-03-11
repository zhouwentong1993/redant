package com.wentong.core.router.context;

import com.wentong.core.enums.RenderType;
import com.wentong.core.router.RouteResult;
import io.netty.handler.codec.http.HttpMethod;

public interface RouterContext {

    /**
     * 根据请求的方法和地址解析路由结果
     * @param httpMethod 请求方法，比如 get/post 等
     * @param uri 请求地址
     * @return 路由结果
     */
    RouteResult<RenderType> getRouteResult(HttpMethod httpMethod, String uri);

}
