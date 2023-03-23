package cn.mokeeqian.middleware.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 超时熔断注解
 * @author：mokeeqian
 * @date: 2023/3/23
 * @Copyright： mokeeqian@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoHystrix {
    /**
     * 返回信息
     * @return
     */
    String returnJson() default "";

    /**
     * 超时时间
     * @return
     */
    int timeoutValue() default 0;
}
