package jp.co.worksap.stm2018.jobhere.interceptor;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiAuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            NeedLogin needLogin = handlerMethod.getMethodAnnotation(NeedLogin.class);
            if (needLogin == null) {
                needLogin = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NeedLogin.class);
            }
            if (needLogin != null) {
                String token = String.valueOf(request.getHeader("Api-Token"));
                // TODO: add permission check
            }
        }
        return super.preHandle(request, response, handler);
    }
}
