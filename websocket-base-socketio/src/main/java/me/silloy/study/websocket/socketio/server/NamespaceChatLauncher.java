package me.silloy.study.websocket.socketio.server;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.listener.*;

import javax.annotation.PreDestroy;

public class NamespaceChatLauncher {

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("10.200.56.107");
        config.setPort(9092);
        config.getSocketConfig().setReuseAddress(true);

        final SocketIOServer server = new SocketIOServer(config);
        final SocketIONamespace chat1namespace = server.addNamespace("/chat1");
        final SocketIONamespace chat2namespace = server.addNamespace("/chat2");

        chat1namespace.addEventListener("message", ChatObject.class,
                (SocketIOClient client, ChatObject data, AckRequest ackRequest) -> {
            // broadcast messages to all clients
            chat1namespace.getBroadcastOperations().sendEvent("message", data);
            chat2namespace.getBroadcastOperations().sendEvent("message", data);
        });


        chat2namespace.addEventListener("message", ChatObject.class,
                (SocketIOClient client, ChatObject data, AckRequest ackRequest) -> {
            // broadcast messages to all clients
            chat1namespace.getBroadcastOperations().sendEvent("message", data);
            chat2namespace.getBroadcastOperations().sendEvent("message", data);
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }


    public void sendMessageToNamespaceRoom(String namespace, String room, String content) {
//        BroadcastOperations broadcastOperations = server.getNamespace("/" + namespace).getRoomOperations(room);
//        broadcastOperations.sendEvent("advert_data", content);
    }
}
