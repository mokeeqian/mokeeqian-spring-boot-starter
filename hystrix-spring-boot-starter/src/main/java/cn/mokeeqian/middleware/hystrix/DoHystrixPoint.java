package cn.mokeeqian.middleware.hystrix;

import cn.mokeeqian.middleware.hystrix.annotation.DoHystrix;
import cn.mokeeqian.middleware.hystrix.value.IValveService;
import cn.mokeeqian.middleware.hystrix.value.impl.HystrixValueImpl;
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
 * @description: 超时熔断切点
 * @author：mokeeqian
 * @date: 2023/3/23
 * @Copyright： mokeeqian@gmail.com
 */
@Aspect
@Component
public class DoHystrixPoint {

    /**
     * 切点
     */
    @Pointcut("@annotation(cn.mokeeqian.middleware.hystrix.annotation.DoHystrix)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(doHystrix)")
    public Object doRouter(ProceedingJoinPoint proceedingJoinPoint, DoHystrix doHystrix) throws Throwable {
        IValveService valveService = new HystrixValueImpl();
        // 调用封装好的 Hystrix 逻辑
        return valveService.access(proceedingJoinPoint, this.getMethod(proceedingJoinPoint), doHystrix, proceedingJoinPoint.getArgs());
    }

    private Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
