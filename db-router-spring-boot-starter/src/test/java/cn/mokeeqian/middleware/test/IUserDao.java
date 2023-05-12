package cn.mokeeqian.middleware.test;

import cn.mokeeqian.middleware.db.router.annotation.DBRouter;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public interface IUserDao {

    @DBRouter(key = "userId")
    void insertUser(String req);
}
