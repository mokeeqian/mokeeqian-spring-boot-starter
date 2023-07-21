package cn.mokeeqian.middleware.test;

import cn.mokeeqian.middleware.db.router.annotation.DBRouter;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public class ApiTest {

    public static void main(String[] args) {
        System.out.println("Hi");
    }

    @Test
    public void test_db_hash() {
        String key = "xxxx";

        int dbCount = 2, tbCount = 4;
        int size = dbCount * tbCount;
        // 散列，参考HashMap (n-1) & hash
        int idx = (size - 1) & (key.hashCode() ^ (key.hashCode() >>> 16));

        System.out.println(idx);

        int dbIdx = idx / tbCount + 1;
        int tbIdx = idx - tbCount * (dbIdx - 1);

        System.out.println(dbIdx);
        System.out.println(tbIdx);

    }

    @Test
    public void test_str_format() {
        System.out.println(String.format("db%02d", 1));
        System.out.println(String.format("_%02d", 25));
    }

    @Test
    public void test_annotation() throws NoSuchMethodException {
        Class<IUserDao> iUserDaoClass = IUserDao.class;
        Method method = iUserDaoClass.getMethod("insertUser", String.class);

        DBRouter dbRouter = method.getAnnotation(DBRouter.class);

        System.out.println(dbRouter.key());

    }

}