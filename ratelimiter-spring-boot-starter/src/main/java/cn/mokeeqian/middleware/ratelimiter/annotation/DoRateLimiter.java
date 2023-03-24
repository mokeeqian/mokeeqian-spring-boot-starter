package cn.mokeeqian.middleware.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 自定义限流注解
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoRateLimiter {

    /**
     * 限流许可量
     * @return
     */
    double permitsPerSecond() default 0D;

    /**
     * 返回信息
     * @return
     */
    String returnJSon() default "";
}
