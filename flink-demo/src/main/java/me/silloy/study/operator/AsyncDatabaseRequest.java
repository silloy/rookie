//package me.silloy.study.operator;
//
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.shaded.netty4.io.netty.util.concurrent.Future;
//import org.apache.flink.streaming.api.functions.async.ResultFuture;
//import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
//
//import java.util.Collections;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.function.Supplier;
//
///**
// * @author shaohuasu
// * @date 2019/12/11 12:12 PM
// * @since 1.8
// */
//public class AsyncDatabaseRequest extends RichAsyncFunction<String, Tuple2<String, String>> {
//
//    private transient DatabaseClient client;
//
//
//    @Override
//    public void open(Configuration parameters) throws Exception {
//        client = new DatabaseClient(host, post, credentials);
//    }
//
//    @Override
//    public void close() throws Exception {
//        client.close();
//    }
//
//    @Override
//    public void asyncInvoke(String key, ResultFuture<Tuple2<String, String>> resultFuture) throws Exception {
//        final Future<String> result = client.query(key);
//
//        // set the callback to be executed once the request by the client is complete
//        // the callback simply forwards the result to the result future
//        CompletableFuture.supplyAsync(new Supplier<String>() {
//
//            @Override
//            public String get() {
//                try {
//                    return result.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    // Normally handled explicitly.
//                    return null;
//                }
//            }
//        }).thenAccept( (String dbResult) -> {
//            resultFuture.complete(Collections.singleton(new Tuple2<>(key, dbResult)));
//        });
//    }
//}
