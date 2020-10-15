package me.silloy.study.lettuce.redis.home;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class LettuceSample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedisURI uri = RedisURI.create("redis://localhost/");

        RedisClient client = RedisClient.create(uri);
        client.setDefaultTimeout(Duration.ofSeconds(200));

        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> commands = connection.sync();

        connection.close();
        client.shutdown();

        RedisAsyncCommands<String, String> asyncCommands = client.connect().async();

        List<RedisFuture<String>> futures = new ArrayList<RedisFuture<String>>();

        for (int i = 0; i < 10; i++) {
            futures.add(asyncCommands.set("key-" + i, "value-" + i));
        }

        LettuceFutures.awaitAll(1, TimeUnit.MINUTES, futures.toArray(new RedisFuture[futures.size()]));
    }
}
