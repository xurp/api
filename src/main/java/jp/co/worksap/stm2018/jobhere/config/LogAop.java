package jp.co.worksap.stm2018.jobhere.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by xu_xi-pc on 2018/9/3.
 */



@Aspect
@Component
public class LogAop{
    private Logger logger = LoggerFactory.getLogger(this.getClass());//@slf4j doesn't work in aop?

    @Pointcut("execution(* jp.co.worksap.stm2018.jobhere.controller..*.*(..))")
    public void setLogger(){}

    @Before("setLogger()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("url : " + request.getRequestURL().toString());
        logger.info("type : " + request.getMethod());
        logger.info("api url : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

    }
/*  stackoverflow...
    @AfterReturning(returning = "ret", pointcut = "setLogger()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("RESPONSE : " + ret);
    }
*/

}
