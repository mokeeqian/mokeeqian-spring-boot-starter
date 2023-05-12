package cn.mokeeqian.middleware.db.router.annotation;

import java.lang.annotation.*;


/**
 * @description: 路由注解
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DBRouter {

    /**
     * 路由字段
     * @return
     */
    String key() default "";
}
