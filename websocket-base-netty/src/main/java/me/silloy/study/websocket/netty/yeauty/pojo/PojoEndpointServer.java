package me.silloy.study.websocket.netty.yeauty.pojo;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import me.silloy.study.websocket.netty.yeauty.processor.EndpointProcessor;
import me.silloy.study.websocket.netty.yeauty.standard.ServerEndpointConfig;
import me.silloy.study.websocket.netty.yeauty.support.DefaultPathMatcher;
import me.silloy.study.websocket.netty.yeauty.support.WsPathMatcher;
import org.springframework.beans.TypeMismatchException;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Yeauty
 * @version 1.0
 */
public class PojoEndpointServer {

    private static final AttributeKey<Object> POJO_KEY = AttributeKey.valueOf("WEBSOCKET_IMPLEMENT");

    public static final AttributeKey<WebSession> SESSION_KEY = AttributeKey.valueOf("WEBSOCKET_SESSION");

    private static final AttributeKey<String> PATH_KEY = AttributeKey.valueOf("WEBSOCKET_PATH");

    public static final AttributeKey<Map<String, String>> URI_TEMPLATE = AttributeKey.valueOf("WEBSOCKET_URI_TEMPLATE");

    public static final AttributeKey<Map<String, List<String>>> REQUEST_PARAM = AttributeKey.valueOf("WEBSOCKET_REQUEST_PARAM");

    private final Map<String, EndpointProcessor> processingMapingMap = new HashMap<>();

    private final ServerEndpointConfig config;

    private Set<WsPathMatcher> pathMatchers = new HashSet<>();

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(PojoEndpointServer.class);

    public PojoEndpointServer(EndpointProcessor endpointClass, ServerEndpointConfig config, String path) {
        addPathPojoMethodMapping(path, endpointClass);
        this.config = config;
    }


    public void doBeforeHandshake(Channel channel, FullHttpRequest req, String path) {
        EndpointProcessor processor = getPojoMethodMapping(path, channel);
        channel.attr(POJO_KEY).set(processor);
        WebSession webSession = new WebSession(channel);
        channel.attr(SESSION_KEY).set(webSession);
        processor.beforeHandshake(webSession, channel, req);
    }

    public void doOnOpen(Channel channel, FullHttpRequest req, String path) {
        EndpointProcessor processor = getPojoMethodMapping(path, channel);

        Object implement = channel.attr(POJO_KEY).get();
        if (implement==null){
            try {
//                implement = methodMapping.getEndpointInstance();
                channel.attr(POJO_KEY).set(processor);
            } catch (Exception e) {
                logger.error(e);
                return;
            }
            WebSession webSession = new WebSession(channel);
            channel.attr(SESSION_KEY).set(webSession);
        }

        processor.onOpen(channel, req);
    }

    public void doOnClose(Channel channel) {
        Attribute<String> attrPath = channel.attr(PATH_KEY);
        EndpointProcessor processor = null;
        if (processingMapingMap.size() == 1) {
            processor  = processingMapingMap.values().iterator().next();
        } else {
            String path = attrPath.get();
            processor = processingMapingMap.get(path);
            if (processor == null) {
                return;
            }
        }
        WebSession webSession = new WebSession(channel);

        if (!channel.hasAttr(SESSION_KEY)) {
            return;
        }
        Object implement = channel.attr(POJO_KEY).get();
        try {
            processor.onClose(webSession);
        } catch (Throwable t) {
            logger.error(t);
        }
    }


    public void doOnError(Channel channel, Throwable throwable) {
        Attribute<String> attrPath = channel.attr(PATH_KEY);
        EndpointProcessor processor = null;
        if (processingMapingMap.size() == 1) {
            processor = processingMapingMap.values().iterator().next();
        } else {
            String path = attrPath.get();
            processor = processingMapingMap.get(path);
        }

        if (!channel.hasAttr(SESSION_KEY)) {
            return;
        }

        Object implement = channel.attr(POJO_KEY).get();
        try {
            WebSession webSession = new WebSession(channel);
            processor.onError(webSession, throwable);
        } catch (Throwable t) {
            logger.error(t);
        }
    }

    public void doOnMessage(Channel channel, WebSocketFrame frame) {
        Attribute<String> attrPath = channel.attr(PATH_KEY);
        EndpointProcessor processor = null;
        if (processingMapingMap.size() == 1) {
            processor = processingMapingMap.values().iterator().next();
        } else {
            String path = attrPath.get();
            processor = processingMapingMap.get(path);
        }
        WebSession webSession = new WebSession(channel);
        TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
        try {
            processor.onMessage(webSession, textFrame);
        } catch (Throwable t) {
            logger.error(t);
        }
    }

    public void doOnBinary(Channel channel, WebSocketFrame frame) {
        Attribute<String> attrPath = channel.attr(PATH_KEY);
        EndpointProcessor processor = null;
        if (processingMapingMap.size() == 1) {
            processor = processingMapingMap.values().iterator().next();
        } else {
            String path = attrPath.get();
            processor = processingMapingMap.get(path);
        }
        BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
        WebSession webSession = new WebSession(channel);
        try {
            processor.onBinary(webSession, binaryWebSocketFrame);
        } catch (Throwable t) {
            logger.error(t);
        }
    }

    public void doOnEvent(Channel channel, Object evt) {
        Attribute<String> attrPath = channel.attr(PATH_KEY);
        EndpointProcessor processor = null;
        if (processingMapingMap.size() == 1) {
            processor = processingMapingMap.values().iterator().next();
        } else {
            String path = attrPath.get();
            processor = processingMapingMap.get(path);
        }
        if (!channel.hasAttr(SESSION_KEY)) {
            return;
        }
        WebSession webSession = new WebSession(channel);
        try {
            processor.onEvent(webSession, evt);
        } catch (Throwable t) {
            logger.error(t);
        }
    }

    public String getHost() {
        return config.getHost();
    }

    public int getPort() {
        return config.getPort();
    }

    public Set<WsPathMatcher> getPathMatcherSet() {
        return pathMatchers;
    }

    public void addPathPojoMethodMapping(String path, EndpointProcessor endpointClass) {
        processingMapingMap.put(path, endpointClass);
        pathMatchers.add(new DefaultPathMatcher(path));
    }

    private EndpointProcessor getPojoMethodMapping(String path, Channel channel) {
        EndpointProcessor methodMapping;
        if (processingMapingMap.size() == 1) {
            methodMapping = processingMapingMap.values().iterator().next();
        } else {
            Attribute<String> attrPath = channel.attr(PATH_KEY);
            attrPath.set(path);
            methodMapping = processingMapingMap.get(path);
            if (methodMapping == null) {
                throw new RuntimeException("path " + path + " is not in pathMethodMappingMap ");
            }
        }
        return methodMapping;
    }
}
