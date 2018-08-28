package jp.co.worksap.stm2018.jobhere.config;

import jp.co.worksap.stm2018.jobhere.interceptor.ApiAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiAuthInterceptor())
                .addPathPatterns("/**");
    }
}
