package cn.mokeeqian.middleware.ratelimiter.valve;

import cn.mokeeqian.middleware.ratelimiter.annotation.DoRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public interface IValveService {

    /**
     *
     * @param proceedingJoinPoint
     * @param method
     * @param doRateLimiter
     * @param args
     * @return
     * @throws Throwable
     */
    Object access(ProceedingJoinPoint proceedingJoinPoint, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable;
}
