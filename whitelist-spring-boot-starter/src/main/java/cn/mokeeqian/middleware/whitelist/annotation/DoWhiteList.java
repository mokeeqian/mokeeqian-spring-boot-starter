package cn.mokeeqian.middleware.whitelist.annotation;

import java.lang.annotation.*;

/**
 * @description: 自定义白名单注解
 * @author：mokeeqian
 * @date: 2023/3/23
 * @Copyright： mokeeqian@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoWhiteList {

    /**
     * 接口入参需要提取的属性
     * 例如按照 uid 进行拦截
     * @return
     */
    String key() default "";

    /**
     * 拦截用户请求后给出的返回信息
     * @return
     */
    String returnJson() default "";
}
