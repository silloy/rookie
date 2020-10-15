package me.silloy.study.lettuce.redis;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class ThreadTest {

    public void begin(int threadCount, Run run, Finish finish) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            fixedThreadPool.execute(() -> {
                try {
                    countDownLatch.await();
                    run.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        countDownLatch.countDown();
        fixedThreadPool.shutdown();
        while (!fixedThreadPool.isTerminated()) {
        }
        finish.finish();
    }


    public interface Run {
        void run();
    }

    public interface Finish {
        void finish();
    }

    static int m = 0;


    @Test
    public void test() {
        AtomicInteger integer = new AtomicInteger(0);
        new ThreadTest().begin(10, () -> {
            for (int i = 0; i < 10000; i++) {
//                integer.addAndGet(1);
                m++;
            }
        }, () -> System.out.println(m));
    }

}
