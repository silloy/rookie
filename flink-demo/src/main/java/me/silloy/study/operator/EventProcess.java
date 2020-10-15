package me.silloy.study.operator;

import com.alibaba.fastjson.JSON;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import me.silloy.study.aaa.domain.*;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shaohuasu
 * @date 2019/12/17 2:18 PM
 * @since 1.8
 */
public class EventProcess extends RichAsyncFunction<ConnectedResult, EvaluatedResult> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private SQLClient client;

    AtomicLong index = new AtomicLong(0);

    @Override
    public void open(Configuration parameters) throws Exception {

        LOGGER.info("process.config.start.index={}", index.getAndAdd(1));

        ParameterTool parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();

//        String pass = parameterTool.getRequired("db_password");
//        String host = parameterTool.getRequired("db_ip_host");
//        String db = parameterTool.getRequired("db_database");
//        String driver = parameterTool.getRequired("driver_class");
//        String user = parameterTool.getRequired("db_user");
//
//        Vertx vertx = Vertx.vertx(
//                new VertxOptions().setWorkerPoolSize(10)
//                        .setEventLoopPoolSize(10));
//
//        String password = "__NO_VALUE_KEY".equals(pass) ? "" : pass;
//        JsonObject config = new JsonObject()
//                .put("url", "jdbc:mysql://" + host + "/" + db + "?" +
//                        "serverTimezone=GMT%2b8:00&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true&useSSL=false")
//                .put("driver_class", driver)
//                .put("max_pool_size", 10)
//                .put("user", user)
//                .put("password", password);
//
//        client = JDBCClient.createShared(vertx, config);
    }

    @Override
    public void close() throws Exception {
//        client.close();
        LOGGER.info("process.config.end.index={}", index.getAndAdd(1));
    }

    @Override
    public void asyncInvoke(ConnectedResult connectedResult, ResultFuture<EvaluatedResult> resultFuture) throws Exception {
        incremental(connectedResult, resultFuture);
    }

    private void incremental(ConnectedResult connectedResult, ResultFuture<EvaluatedResult> resultFuture) {

        UserEvent userEvent = connectedResult.getUserEvent();
        Map<String, RuleConfig> configState = connectedResult.getConfigState();

        LOGGER.info("copute.event={}", JSON.toJSONString(userEvent));
        LOGGER.info("copute.config={}", JSON.toJSONString(configState));
        ArrayList<EvaluatedResult> l = new ArrayList<EvaluatedResult>();
        client.getConnection(conn -> {
            if (conn.failed()) {
                conn.cause().printStackTrace();
                return;
            }

            String event = userEvent.getEvent();
            final SQLConnection connection = conn.result();
            try {
                if (event.equals("purchase_success")) {
                    String sql = "INSERT INTO purchase_success VALUES (?, ?, ?, ?, ?, ?)";
                    LOGGER.info("insert into sql is : {}", sql);
                    LOGGER.info("userEvent is : {}", userEvent);
                    connection.updateWithParams(sql, new JsonArray().add(userEvent.getOneId()).add(userEvent.getEventTimestamp())
                            .add(userEvent.getEventTime()).add(userEvent.getEventChannel()).add(userEvent.getType()).add(userEvent.getEvent()), res -> {
                        if (res.failed()) {
                            LOGGER.error("insert into purchase_success error is : ", res.cause());
                        } else {
                            try {

                                Iterator<Map.Entry<String, RuleConfig>> iter = configState.entrySet().iterator();
                                while (iter.hasNext()) {
                                    Map.Entry<String, RuleConfig> p = iter.next();
                                    LOGGER.info("rule_config is : {}", p);
                                    Node node = Node.buildNode(p.getValue().getNode());
                                    if (node.calculate()) {
                                        EvaluatedResult evaluatedResult = new EvaluatedResult();
                                        evaluatedResult.setEventId(p.getKey());
                                        evaluatedResult.setOneId(userEvent.getOneId());
                                        l.add(evaluatedResult);
                                        LOGGER.info("evaluatedResult is : {}", evaluatedResult);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        resultFuture.complete(l);
                        connection.close();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                connection.close();
            }
        });
    }


    @Override
    public void timeout(ConnectedResult input, ResultFuture<EvaluatedResult> resultFuture) throws Exception {
    }
}
