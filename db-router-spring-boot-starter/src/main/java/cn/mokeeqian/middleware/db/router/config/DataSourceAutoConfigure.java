package cn.mokeeqian.middleware.db.router.config;

import cn.mokeeqian.middleware.db.router.DBRouterConfig;
import cn.mokeeqian.middleware.db.router.DBRouterJoinPoint;
import cn.mokeeqian.middleware.db.router.dynamic.DynamicDataSource;
import cn.mokeeqian.middleware.db.router.dynamic.DynamicMybatisPlugin;
import cn.mokeeqian.middleware.db.router.strategy.IDBRouterStrategy;
import cn.mokeeqian.middleware.db.router.strategy.impl.DBRouterStrategyHashCode;
import cn.mokeeqian.middleware.db.router.util.PropertyUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 配置、加载、创建数据源
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
@Configuration
public class DataSourceAutoConfigure implements EnvironmentAware {

    /**
     * 数据源配置组
     */
    private Map<String, Map<String, Object>> dataSourceMap = new HashMap<>();

    /**
     * 默认数据源配置
     * 也就是：不走路由组件的数据源
     */
    private Map<String, Object> defaultDataSourceConfig;

    /**
     * 分库数目
     */
    private int dbCount;

    /**
     * 分表数目
     */
    private int tbCount;

    /**
     * 路由字段
     */
    private String routerKey;

    @Bean
    public IDBRouterStrategy doRouterStrategy(DBRouterConfig dbRouterConfig) {
        return new DBRouterStrategyHashCode(dbRouterConfig);
    }

    @Bean
    public DBRouterConfig dbRouterConfig() {
        return new DBRouterConfig(this.dbCount, this.tbCount, this.routerKey);
    }

    @Bean
    public Interceptor plugin() {
        return new DynamicMybatisPlugin();
    }

    @Bean(name = "db-router-point")
    @ConditionalOnMissingBean
    public DBRouterJoinPoint dbRouterJoinPoint(DBRouterConfig dbRouterConfig, IDBRouterStrategy dbRouterStrategy) {
        return new DBRouterJoinPoint(dbRouterConfig, dbRouterStrategy);
    }

    /**
     * 创建动态数据源
     * 这个数据源就会被 MyBatis SpringBoot Starter 中 SqlSessionFactory sqlSessionFactory(DataSource dataSource) 注入使用
     * @return
     */
    @Bean
    public DataSource dataSource() {
        // 创建数据源
        Map<Object, Object> targetDataSources = new HashMap<>();

        for (String dbInfo : dataSourceMap.keySet()) {
            Map<String, Object> objMap = dataSourceMap.get(dbInfo);
            targetDataSources.put(dbInfo, new DriverManagerDataSource(
                    objMap.get("url").toString(), objMap.get("username").toString(), objMap.get("password").toString())
            );
        }

        // 设置数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(new DriverManagerDataSource(defaultDataSourceConfig.get("url").toString(), defaultDataSourceConfig.get("username").toString(), defaultDataSourceConfig.get("password").toString()));

        return dynamicDataSource;
    }

    @Bean
    public TransactionTemplate transactionTemplate(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager);
        transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return transactionTemplate;
    }


    /**
     * 读取 yml 中自定义的配置
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "mini-db-router.jdbc.datasource.";

        dbCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty(prefix + "dbCount")));
        tbCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty(prefix + "tbCount")));
        routerKey = environment.getProperty(prefix + "routerKey");

        // 分库列表 db01,db02
        String dataSources = environment.getProperty(prefix + "list");
        assert dataSources != null;
        for (String dbInfo : dataSources.split(",")) {
            Map<String, Object> dataSourceProps = PropertyUtil.handle(environment, prefix + dbInfo, Map.class);
            dataSourceMap.put(dbInfo, dataSourceProps);
        }

        // 默认数据源
        String defaultData = environment.getProperty(prefix + "default");
        defaultDataSourceConfig = PropertyUtil.handle(environment, prefix + defaultData, Map.class);
    }
}
