package me.silloy.shiroboot.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shaohuasu
 * @date 2020/3/10 6:48 PM
 * @since 1.8
 *
 * [Shiro用starter方式优雅整合到SpringBoot中](https://segmentfault.com/a/1190000014479154)
 * https://juejin.im/post/5cff0cfc5188250d28510681
 *

配置缩写	对应的过滤器	功能
anon	AnonymousFilter	指定url可以匿名访问
authc	FormAuthenticationFilter	指定url需要form表单登录，默认会从请求中获取username、password,rememberMe等参数并尝试登录，如果登录不了就会跳转到loginUrl配置的路径。我们也可以用这个过滤器做默认的登录逻辑，但是一般都是我们自己在控制器写登录逻辑的，自己写的话出错返回的信息都可以定制嘛。
authcBasic	BasicHttpAuthenticationFilter	指定url需要basic登录
logout	LogoutFilter	登出过滤器，配置指定url就可以实现退出功能，非常方便
noSessionCreation	NoSessionCreationFilter	禁止创建会话
perms	PermissionsAuthorizationFilter	需要指定权限才能访问
port	PortFilter	需要指定端口才能访问
rest	HttpMethodPermissionFilter	将http请求方法转化成相应的动词来构造一个权限字符串，这个感觉意义不大，有兴趣自己看源码的注释
roles	RolesAuthorizationFilter	需要指定角色才能访问
ssl	SslFilter	需要https请求才能访问
user	UserFilter	需要已登录或“记住我”的用户才能访问
 */
@Component
public class CustomShiroConfig {


////         logged in users with the 'document:read' permission
////        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
////
////         all other paths require a logged in user
////        chainDefinition.addPathDefinition("/**", "authc");
////        return chainDefinition;
//
//
//        System.out.println("ShiroConfiguration.shirFilter()");
//        DefaultShiroFilterChainDefinition shiroFilterFactoryBean = new DefaultShiroFilterChainDefinition();
//        // 拦截器.
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        // 配置不会被拦截的链接 顺序判断
//        shiroFilterFactoryBean.addPathDefinition("/static/**", "anon");
//        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        shiroFilterFactoryBean.addPathDefinition("/logout", "logout");
//        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
//        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//        shiroFilterFactoryBean.addPathDefinition("/**", "authc");
//
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
////        shiroFilterFactoryBean.setLoginUrl("/login");
//        // 登录成功后要跳转的链接
////        shiroFilterFactoryBean.setSuccessUrl("/index");
//
//        //未授权界面;
////        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
////        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
////        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
////
////         logged in users with the 'admin' role
////        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
////


//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
//
//        /**
//         * 这里小心踩坑！我在application.yml中设置的context-path: /api/v1
//         * 但经过实际测试，过滤器的过滤路径，是context-path下的路径，无需加上"/api/v1"前缀
//         */
//
//        //访问控制
//        chain.addPathDefinition("/user/login", "anon");//可以匿名访问
//        chain.addPathDefinition("/page/401", "anon");//可以匿名访问
//        chain.addPathDefinition("/page/403", "anon");//可以匿名访问
//        chain.addPathDefinition("/t4/hello", "anon");//可以匿名访问
//
//        chain.addPathDefinition("/t4/changePwd", "authc");//需要登录
//        chain.addPathDefinition("/t4/user", "user");//已登录或“记住我”的用户可以访问
//        chain.addPathDefinition("/t4/mvnBuild", "authc,perms[mvn:install]");//需要mvn:build权限
//        chain.addPathDefinition("/t4/gradleBuild", "authc,perms[gradle:build]");//需要gradle:build权限
//        chain.addPathDefinition("/t4/js", "authc,roles[js]");//需要js角色
//        chain.addPathDefinition("/t4/python", "authc,roles[python]");//需要python角色
//
//        // shiro 提供的登出过滤器，访问指定的请求，就会执行登录，默认跳转路径是"/"，或者是"shiro.loginUrl"配置的内容
//        // 由于application-shiro.yml中配置了 shiro:loginUrl: /page/401，返回会返回对应的json内容
//        // 可以结合/user/login和/t1/js接口来测试这个/t4/logout接口是否有效
//        chain.addPathDefinition("/t4/logout", "anon,logout");
//
//        //其它路径均需要登录
//        chain.addPathDefinition("/**", "authc");
//        return chain;
//    }

//    @Bean
//    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
//        System.out.println("ShiroConfiguration.shirFilter()");
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        // 拦截器.
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put("/static/**", "anon");
//        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/logout", "logout");
//        // <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
//        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//        filterChainDefinitionMap.put("/**", "authc");
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//
//
//        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
}
