package cn.mokeeqian.middleware.hystrix.value;

import cn.mokeeqian.middleware.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/23
 * @Copyright： mokeeqian@gmail.com
 */
public interface IValveService {
    /**
     * 熔断接口
     * @param proceedingJoinPoint
     * @param method
     * @param doHystrix
     * @param args
     * @return
     * @throws Throwable
     */
    Object access(ProceedingJoinPoint proceedingJoinPoint, Method method, DoHystrix doHystrix, Object[] args) throws Throwable;
}
