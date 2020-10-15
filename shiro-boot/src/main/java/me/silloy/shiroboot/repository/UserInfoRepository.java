package me.silloy.shiroboot.repository;

import me.silloy.shiroboot.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shaohuasu
 * @date 2020/3/10 1:24 PM
 * @since 1.8
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
}
