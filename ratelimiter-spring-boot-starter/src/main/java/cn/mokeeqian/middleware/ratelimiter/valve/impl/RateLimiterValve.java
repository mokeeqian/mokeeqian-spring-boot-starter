package cn.mokeeqian.middleware.ratelimiter.valve.impl;

import cn.mokeeqian.middleware.ratelimiter.Constants;
import cn.mokeeqian.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.mokeeqian.middleware.ratelimiter.valve.IValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public class RateLimiterValve implements IValveService {
    @Override
    public Object access(ProceedingJoinPoint proceedingJoinPoint, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable {
        // 是否开启限流
        if (0 == doRateLimiter.permitsPerSecond()) {
            return proceedingJoinPoint.proceed();
        }

        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = method.getName();

        String key = className + "_" + methodName;

        if (null == Constants.rateLimiterValveMap.get(key)) {
            Constants.rateLimiterValveMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
        }

        RateLimiter rateLimiter = Constants.rateLimiterValveMap.get(key);
        if ( rateLimiter.tryAcquire() ) {
            return proceedingJoinPoint.proceed();
        }

        return JSON.parseObject(doRateLimiter.returnJSon(), method.getReturnType());
    }
}
