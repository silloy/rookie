package me.silloy.netty.chat.protocol;

/**
 * @author shaohuasu
 * @date 2018-12-26 21:58
 * @since 1.8
 */
public interface Command {

    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte LOGOUT_REQUEST = 3;

    Byte LOGOUT_RESPONSE = 4;

    Byte MESSAGE_REQUEST = 5;

    Byte MESSAGE_RESPONSE = 6;

    Byte GROUP_REQUEST = 7;

    Byte GROUP_RESPONSE = 8;

    Byte JOIN_GROUP_REQUEST = 9;

    Byte JOIN_GROUP_RESPONSE = 10;

    Byte QUIT_GROUP_REQUEST = 11;

    Byte QUIT_GROUP_RESPONSE = 12;

    Byte LIST_GROUP_MEMBER_REQUEST = 13;

    Byte LIST_GROUP_MEMBER_RESPONSE = 14;

    Byte GROUP_MESSAGE_REQUEST = 15;

    Byte GROUP_MESSAGE_RESPONSE = 16;

    Byte HEARTBEAT_REQUEST = 17;

    Byte HEARTBEAT_RESPONSE = 18;

}
