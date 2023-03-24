package cn.mokeeqian.middleware.ratelimiter;

import cn.mokeeqian.middleware.ratelimiter.annotation.DoRateLimiter;
import cn.mokeeqian.middleware.ratelimiter.valve.IValveService;
import cn.mokeeqian.middleware.ratelimiter.valve.impl.RateLimiterValve;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
@Aspect
@Component
public class DoRateLimiterPoint {

    @Pointcut("@annotation(cn.mokeeqian.middleware.ratelimiter.annotation.DoRateLimiter)")
    public void aopPoint() {}

    @Around("aopPoint() && @annotation(doRateLimiter)")
    public Object doRouter(ProceedingJoinPoint proceedingJoinPoint, DoRateLimiter doRateLimiter) throws Throwable {
        IValveService valveService = new RateLimiterValve();
        return valveService.access(proceedingJoinPoint, getMethod(proceedingJoinPoint), doRateLimiter, proceedingJoinPoint.getArgs());
    }

    private Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

}
