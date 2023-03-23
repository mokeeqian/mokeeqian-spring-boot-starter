package cn.mokeeqian.middleware.whitelist;

import cn.mokeeqian.middleware.whitelist.annotation.DoWhiteList;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @description: 切面逻辑实现
 * @author：mokeeqian
 * @date: 2023/3/23
 * @Copyright： mokeeqian@gmail.com
 */
@Aspect
@Component
public class DoJoinPoint {

    private Logger logger = LoggerFactory.getLogger(DoJoinPoint.class);

    @Resource
    private String whiteListConfig;

    @Pointcut("@annotation(cn.mokeeqian.middleware.whitelist.annotation.DoWhiteList)")
    public void aopPoint() {
    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        // 获取内容
        Method method = this.getMethod(proceedingJoinPoint);
        DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);

        // 获取字段值
        String key = getFieldValue(whiteList.key(), proceedingJoinPoint.getArgs());
        logger.info("middleware whitelist handler method: {} value: {}", method.getName(), key);

        if (null == key || "".equals(key)) {
            return proceedingJoinPoint.proceed();
        }

        // 白名单过滤
        String[] split = whiteListConfig.split(",");
        // TODO: 这里可以使用哈希结构加速过滤
        for (String s : split) {
            if (key.equals(s)) {
                return proceedingJoinPoint.proceed();
            }
        }
        // 拦截
        return returnObject(whiteList, method);
    }

    private Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    /**
     * 获取属性值
     * @param filed
     * @param args
     * @return
     */
    private String getFieldValue(String filed, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (null == filedValue || "".equals(filedValue)) {
                    filedValue = BeanUtils.getProperty(arg, filed);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }

        }
        return filedValue;
    }

    /**
     *
     * @param doWhiteList
     * @param method
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object returnObject(DoWhiteList doWhiteList, Method method) throws InstantiationException, IllegalAccessException {
        Class<?> returnType = method.getReturnType();
        String returnJson = doWhiteList.returnJson();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }
}
