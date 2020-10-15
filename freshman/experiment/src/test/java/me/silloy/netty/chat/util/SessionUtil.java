package me.silloy.netty.chat.util;

import me.silloy.netty.chat.attribute.Attributes;
import me.silloy.netty.chat.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shaohuasu
 * @date 2019-01-03 13:32
 * @since 1.8
 */
public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> groupChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel c) {
        userIdChannelMap.put(session.getUserId(), c);
        c.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }


    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }


    /* 群聊 */
    public static ChannelGroup getChannelGroup(String groupId) {
        return groupChannelMap.get(groupId);
    }

    public static void bindGroup(String groupId, ChannelGroup channelGroup) {
        groupChannelMap.put(groupId, channelGroup);
    }
}
