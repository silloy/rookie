package me.silloy.netty.chat.attribute;

import me.silloy.netty.chat.session.Session;
import io.netty.util.AttributeKey;

/**
 * @author shaohuasu
 * @date 2018-12-27 13:32
 * @since 1.8
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

}
