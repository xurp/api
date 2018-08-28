package jp.co.worksap.stm2018.jobhere.interceptor;

import jp.co.worksap.stm2018.jobhere.annotation.NeedLogin;
import jp.co.worksap.stm2018.jobhere.dao.ApiTokenRepository;
import jp.co.worksap.stm2018.jobhere.http.ForbiddenException;
import jp.co.worksap.stm2018.jobhere.model.domain.ApiToken;
import jp.co.worksap.stm2018.jobhere.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class ApiAuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private AuthService authService;




/*  public ApiAuthInterceptor(){}
    @Autowired
    public ApiAuthInterceptor(ApiTokenRepository apiTokenRepository){
        this.apiTokenRepository=apiTokenRepository;
    }
*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            NeedLogin needLogin = handlerMethod.getMethodAnnotation(NeedLogin.class);
            if (needLogin == null) {
                needLogin = handlerMethod.getMethod().getDeclaringClass().getAnnotation(NeedLogin.class);
            }
            if (needLogin != null) {  //if @needLogin
                String token = String.valueOf(request.getHeader("Api-Token"));
                // TODO: add permission check
                if(!validateLogin(request, response, token)){
                    //response.setHeader("Access-Control-Allow-Origin", "*");
                    throw new LoginException();
                }
            }
        }
        return super.preHandle(request, response, handler);
    }
    private boolean validateLogin(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        if (token == null) {  //if token exists
            return false;
        }
        Optional<ApiToken> apiTokenOptional = authService.findById(token);
        if(apiTokenOptional.isPresent()){
            //ApiToken apiToken=apiTokenOptional.get();
            return true; //user and token exist
        } else {
            return false;
        }
    }
}
