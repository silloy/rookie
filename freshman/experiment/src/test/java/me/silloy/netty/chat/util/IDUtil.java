package me.silloy.netty.chat.util;

import java.util.UUID;

/**
 * @author shaohuasu
 * @date 2019-01-03 16:21
 * @since 1.8
 */
public class IDUtil {

    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

}
