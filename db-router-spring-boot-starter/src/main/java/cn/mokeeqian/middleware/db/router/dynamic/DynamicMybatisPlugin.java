package cn.mokeeqian.middleware.db.router.dynamic;

import cn.mokeeqian.middleware.db.router.DBContextHolder;
import cn.mokeeqian.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: MyBatis 拦截器，通过拦截 + 修改 SQL 语句，实现分表
 * @author：mokeeqian
 * @date: 2023/5/12
 * @Copyright： mokeeqian@gmail.com
 */
@Intercepts(
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
)
public class DynamicMybatisPlugin implements Interceptor {

    /**
     * 正则匹配 SQL 语句
     * 由于分表主要涉及到对表的操作，因此这里直接匹配 from, into, update
     */
    private Pattern pattern = Pattern.compile("(from|into|update)[\\s]{1,}(\\w{1,})", Pattern.CASE_INSENSITIVE);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取 StatementHandler
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        // 获取自定义注解，判断是否需要进行分表
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        Class<?> clazz = Class.forName(className);
        DBRouterStrategy dbRouterStrategy = clazz.getAnnotation(DBRouterStrategy.class);

        // 若无需分表，直接放行
        if (null == dbRouterStrategy || !dbRouterStrategy.splitTable()) {
            return invocation.proceed();
        }

        // 获取 MyBatis 原始 SQL
        BoundSql boundSql = statementHandler.getBoundSql();
        String originalSql = boundSql.getSql();

        // sql 动态替换（eg: user -> user_01）
        Matcher matcher = pattern.matcher(originalSql);
        String tableName = null;
        if (matcher.find()) {
            tableName = matcher.group().trim();
        }
        assert null != tableName;
        String replacedSql = matcher.replaceAll(tableName + "_" + DBContextHolder.getTBKey());


        // 通过反射修改 sql 语句
        // getDeclaredField：可以获取所有已声明字段（无视访问限定符）; getField：只能获取public 字段
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, replacedSql);
        field.setAccessible(false);

        return invocation.proceed();
    }

}
