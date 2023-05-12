package cn.mokeeqian.middleware.test;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/25
 * @Copyright： mokeeqian@gmail.com
 */
public class HashTest {

    private Set<String> words;

    @Before
    public void before() {
        words = FileUtil.readWordList("D:\\下载\\103976-master\\EnWords.csv");
        assert words != null;
        System.out.println(words.size());
    }

    @Test
    public void test_hash() {
        Map<Integer, Integer> map = new HashMap<>(16);
        Map<Integer, Integer> map2 = new HashMap<>(16);
        for (String w : words) {
            // 统计下标值分配
            int idx1 = Disturb.disturbHashIdx(w, 128);
            int idx2 = Disturb.naiveHashIdx(w, 128);

            map.merge(idx1, 1, Integer::sum);
            map2.merge(idx2, 1, Integer::sum);
        }
        System.out.println(map.values());
        System.out.println(map2.values());
    }
}

class Disturb{
    public static int disturbHashIdx(String key, int size) {
        return (size - 1) & (key.hashCode() ^ (key.hashCode() >>> 16));
    }
    public static int naiveHashIdx(String key, int size) {
        return (size-1) & key.hashCode();
    }
}