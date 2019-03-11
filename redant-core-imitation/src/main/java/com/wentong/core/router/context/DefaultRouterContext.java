package com.wentong.core.router.context;

import cn.hutool.core.lang.ClassScaner;
import com.wentong.core.annotations.Controller;
import com.wentong.core.annotations.Mapping;
import com.wentong.core.enums.RenderType;
import com.wentong.core.enums.RequestMethod;
import com.wentong.core.init.InitFunc;
import com.wentong.core.router.RouteResult;
import com.wentong.core.router.Router;
import com.wentong.core.utils.CommonUtils;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.Set;

public class DefaultRouterContext implements RouterContext, InitFunc {

    private Router<RenderType> router = new Router<>();

    private DefaultRouterContext(){}

    private DefaultRouterContext instance = new DefaultRouterContext();

    public DefaultRouterContext getInstance() {
        return instance;
    }

    @Override
    public void init() {
        doInit();
    }

    @Override
    public RouteResult<RenderType> getRouteResult(HttpMethod httpMethod, String uri) {
        return router.route(httpMethod, uri);
    }


    public void doInit() {
        Set<Class<?>> classes = ClassScaner.scanPackageByAnnotation(CommonUtils.getPropertyByName("scan.package"), Controller.class);
        for (Class<?> controllerAnnotation : classes) {
            Controller annotation = controllerAnnotation.getAnnotation(Controller.class);
            String controllerMappingUrl = annotation.name();

            Method[] methods = controllerAnnotation.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping methodMapping = method.getAnnotation(Mapping.class);
                    String methodMappingUrl = methodMapping.path();
                    HttpMethod httpMethod = RequestMethod.getHttpMethod(methodMapping.requestMethod());
                    RenderType renderType = methodMapping.renderType();
                    this.addRouter(httpMethod, controllerMappingUrl + methodMappingUrl, renderType);
                }
            }
        }
    }

    public void addRouter(HttpMethod httpMethod,String uri,RenderType renderType) {
        router.addRoute(httpMethod, uri, renderType);
    }
}
