package cn.mokeeqian.middleware.whitelist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 用于 yml,properties等文件的配置路径
 *              这里就是 mokeeqian.whitelist.users
 * @author：mokeeqian
 * @date: 2023/3/23
 * @Copyright： mokeeqian@gmail.com
 */
@ConfigurationProperties(prefix = "mokeeqian.whitelist")
public class WhiteListProperties {

    private String users;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
