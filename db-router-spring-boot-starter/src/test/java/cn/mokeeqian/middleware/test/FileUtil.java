package cn.mokeeqian.middleware.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/25
 * @Copyright： mokeeqian@gmail.com
 */
public class FileUtil {

    /**
     * 读取单词文件
     * @param path
     * @return
     */
    public static Set<String> readWordList(String path) {
        Set<String> set = new HashSet<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line=reader.readLine()) != null) {
                String[] ss = line.split(",");
                set.add(ss[0].substring(1, ss[0].length()));
            }
            reader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return set;
    }
}
