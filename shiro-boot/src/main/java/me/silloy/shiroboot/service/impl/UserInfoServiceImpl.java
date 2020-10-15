package me.silloy.shiroboot.service.impl;

import me.silloy.shiroboot.entity.UserInfo;
import me.silloy.shiroboot.repository.UserInfoRepository;
import me.silloy.shiroboot.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shaohuasu
 * @date 2020/3/10 12:15 PM
 * @since 1.8
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    UserInfoRepository userInfoRepository;
    @Override
    public UserInfo findByUsername(String username) {
        return userInfoRepository.findByUsername(username);
    }
}
