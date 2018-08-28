package jp.co.worksap.stm2018.jobhere.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedLogin {

    boolean value() default true;
}
