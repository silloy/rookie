package me.silloy.study.websocket.socketio.spring.handler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.BroadcastAckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import me.silloy.study.websocket.socketio.spring.entity.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shaohuasu
 * @since 1.8
 */
@Component
public class MessageEventHandler {

    private static Map<String, List<UUID>> concurrentHashMap = new ConcurrentHashMap<>();


    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEventHandler.class);

    public static SocketIOServer socketIoServer;
    static ArrayList<UUID> listClient = new ArrayList<>();
    static final int limitSeconds = 60;

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
        String clientName = client.getHandshakeData().getSingleUrlParam("clientname");
        String sa = client.getRemoteAddress().toString();
        String clientIp = sa.substring(1, sa.indexOf(":"));// 获取设备ip
        listClient.add(client.getSessionId());
        // 如果roomId 不为空，则加入指定的房间编号, 并通知指定房间的人上线信息
        String roomId = "123";
        String socketMsg = "message";
        System.out.println("客户端:" + client.getSessionId() + "已连接");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        System.out.println("客户端:" + client.getSessionId() + "断开连接");
    }

    @OnEvent(value = "join")
    public void joinEvent(SocketIOClient client, AckRequest request, String roomId) {
        System.out.println("发来ssss消息：" + roomId);
        client.joinRoom(roomId);
    }


    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, MessageInfo data) {
        System.out.println("发来消息：" + data.getMsgContent());
        socketIoServer.getClient(client.getSessionId()).sendEvent("messageevent", "back data");
        if (request.isAckRequested()) {
            request.sendAckData("查无此人");
        }
    }


    /**
     * 给单人发信息
     */
    public static void sendMessageToPeople(UUID clientId, String messageContent) {
        socketIoServer.getClient(clientId).sendEvent("messageevent", messageContent);
    }

    /**
     * 给连接池中所有人发信息
     */
    public static void sendMessageToListClient(String messageContent) {   //这里就是向客户端推消息了
        for (UUID clientId : listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;
            socketIoServer.getClient(clientId).sendEvent("messageevent", messageContent);
        }
    }

    /**
     * 给指定房间发送消息
     */
    public static void sendMessageToRoom(String roomNumber, String messageContent) {
        socketIoServer.getRoomOperations(roomNumber).sendEvent("messageevent", messageContent);
        socketIoServer.getBroadcastOperations().sendEvent("receiveMessage", messageContent, new BroadcastAckCallback<String>(String.class, 5000) {
            @Override
            protected void onAllSuccess() {
                LOGGER.info("全部应答：" + messageContent);
            }

            @Override
            protected void onClientTimeout(SocketIOClient client) {
                LOGGER.info("应答超时：" + client.getSessionId().toString());
            }
        });
    }


    @PreDestroy
    public void stop() {
        if (socketIoServer != null) {
            socketIoServer.stop();
        }
    }

    public static void main(String[] args) throws UnknownHostException, SocketException {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        System.out.println("SUCCESS");
    }

}
