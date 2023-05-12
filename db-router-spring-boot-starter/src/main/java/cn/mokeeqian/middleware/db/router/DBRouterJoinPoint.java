package cn.mokeeqian.middleware.db.router;

import cn.mokeeqian.middleware.db.router.annotation.DBRouter;
import cn.mokeeqian.middleware.db.router.strategy.IDBRouterStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @description: 切点逻辑
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
@Aspect
public class DBRouterJoinPoint {

    private Logger logger = LoggerFactory.getLogger(DBRouterJoinPoint.class);

    private DBRouterConfig dbRouterConfig;

    private IDBRouterStrategy dbRouterStrategy;

    public DBRouterJoinPoint(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy) {
        this.dbRouterConfig = dbRouterConfig;
        this.dbRouterStrategy = dbRouterStrategy;
    }

    /**
     * 切点
     */
    @Pointcut("@annotation(cn.mokeeqian.middleware.db.router.annotation.DBRouter)")
    public void aopPoint() {}

    /**
     * 切面拦截后的具体操作
     * @param proceedingJoinPoint
     * @param dbRouter
     * @return
     * @throws Throwable
     */
    @Around("aopPoint() && @annotation(dbRouter)")
    public Object doRouter(ProceedingJoinPoint proceedingJoinPoint, DBRouter dbRouter) throws Throwable {
        String dbKey = dbRouter.key();
        if (StringUtils.isBlank(dbKey) && StringUtils.isBlank(dbRouterConfig.getRouterKey())) {
            throw new RuntimeException("annotation DBRouter key is null");
        }
        // 如果注解中没配置 key，则使用配置文件的 key
        dbKey = StringUtils.isNotBlank(dbKey) ? dbKey : dbRouterConfig.getRouterKey();
        // 计算路由
        String dbKeyAttr = getAttrValue(dbKey, proceedingJoinPoint.getArgs());

        // 路由分发
        dbRouterStrategy.doRouter(dbKeyAttr);

        // 返回结果
        try {
            // 放行
            return proceedingJoinPoint.proceed();
        } finally {
            // 清除 ThreadLocal
            dbRouterStrategy.clear();
        }

    }

    /**
     * 解析出路由字段
     * @param attr
     * @param args
     * @return
     */
    private String getAttrValue(String attr, Object[] args) {
        if (1 == args.length) {
            Object arg = args[0];
            if (arg instanceof String) {
                return arg.toString();
            }
        }

        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                filedValue = BeanUtils.getProperty(arg, attr);
            } catch (Exception e) {
                logger.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

}
