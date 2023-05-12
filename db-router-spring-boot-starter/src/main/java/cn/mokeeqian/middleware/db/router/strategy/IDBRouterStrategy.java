package cn.mokeeqian.middleware.db.router.strategy;

/**
 * @description: 路由策略
 * @author：mokeeqian
 * @date: 2023/5/12
 * @Copyright： mokeeqian@gmail.com
 */
public interface IDBRouterStrategy {
    /**
     * 路由计算
     * @param dbKeyAttr 路由字段
     */
    void doRouter(String dbKeyAttr);

    /**
     * 手动设置分库路由
     * @param dbIdx 路由库，需要在系统配置范围内，例如：01, 02 之类
     */
    void setDBKey(int dbIdx);

    /**
     * 手动设置分表路由
     * @param tbIdx 路由表，需要在系统配置范围内
     */
    void setTBKey(int tbIdx);

    /**
     * 获取分库数目
     */
    int dbCount();

    /**
     * 获取分表数目
     */
    int tbCount();

    /**
     * 清除路由结果
     */
    void clear();
}
