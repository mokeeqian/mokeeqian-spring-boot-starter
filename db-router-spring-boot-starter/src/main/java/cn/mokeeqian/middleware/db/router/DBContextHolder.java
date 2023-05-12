package cn.mokeeqian.middleware.db.router;

/**
 * @description: ThreadLocal 存放路由结果
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public class DBContextHolder {

    /**
     * 借鉴 Spring Security的安全上下文SecurityContextHolder和Spring Cloud的RequestContextHolder
     * 用完之后记得 remove
     */
    private static final ThreadLocal<String> dbKey = new ThreadLocal<>();
    private static final ThreadLocal<String> tbKey = new ThreadLocal<>();

    public static String getDBKey() {
        return dbKey.get();
    }

    public static String getTBKey() {
        return tbKey.get();
    }

    public static void setDBKey(String dbKeyIdx) {
        dbKey.set(dbKeyIdx);
    }

    public static void setTBKey(String tbKeyIdx) {
        tbKey.set(tbKeyIdx);
    }

    public static void clearDBKey() {
        dbKey.remove();
    }

    public static void clearTBKey() {
        tbKey.remove();
    }
}
