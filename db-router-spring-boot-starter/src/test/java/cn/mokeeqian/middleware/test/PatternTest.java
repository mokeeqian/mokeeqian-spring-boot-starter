package cn.mokeeqian.middleware.test;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 正则表达式替换 SQL 测试
 * @author：mokeeqian
 * @date: 2023/5/12
 * @Copyright： mokeeqian@gmail.com
 */
public class PatternTest {
    @Test
    public void test_regrex() {
        Pattern pattern = Pattern.compile("(from|into|update)[\\s]{1,}(\\w{1,})", Pattern.CASE_INSENSITIVE);
        String[] sqls = new String[] {"select * from tb_user where id=1", "insert into tb_user values (1, 1)", "update tb_user set name = mokeeqian"};
        for (String sql : sqls) {
            Matcher matcher = pattern.matcher(sql);
            String tableName = null;
            if (matcher.find()) {
                tableName = matcher.group().trim();
            }
            assert tableName != null;

            String replacedSql = matcher.replaceAll(tableName + "_" + "01");
            System.out.println(replacedSql);
        }
    }
}
