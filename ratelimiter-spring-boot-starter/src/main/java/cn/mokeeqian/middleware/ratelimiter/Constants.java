package cn.mokeeqian.middleware.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author：mokeeqian
 * @date: 2023/3/24
 * @Copyright： mokeeqian@gmail.com
 */
public class Constants {

    public static Map<String, RateLimiter> rateLimiterValveMap = new ConcurrentHashMap<>();

}
