package me.silloy.shiroboot.service;

import me.silloy.shiroboot.entity.UserInfo;

/**
 * @author shaohuasu
 * @date 2020/3/10 12:14 PM
 * @since 1.8
 */
public interface UserInfoService {
    UserInfo findByUsername(String username);
}
