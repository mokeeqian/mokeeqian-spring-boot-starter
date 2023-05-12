package cn.mokeeqian.middleware.db.router.dynamic;

import cn.mokeeqian.middleware.db.router.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @description: 动态数据源
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 返回 db 路由
     * @return aka db01, db02, ...
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return "db" + DBContextHolder.getDBKey();
    }
}
