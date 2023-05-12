package cn.mokeeqian.middleware.db.router;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
@Deprecated
public class DBRouterBase {
    /**
     *
     */
    private String tbIdx;

    public String getTbIdx() {
        return DBContextHolder.getTBKey();
    }
}
