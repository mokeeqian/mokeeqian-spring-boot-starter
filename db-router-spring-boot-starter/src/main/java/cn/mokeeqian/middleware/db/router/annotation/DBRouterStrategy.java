package cn.mokeeqian.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @description: 路右策略，分表标记
 * @author：mokeeqian
 * @date: 2023/5/12
 * @Copyright： mokeeqian@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DBRouterStrategy {
    /**
     * 是否分表
     * @return
     */
    boolean splitTable() default false;
}