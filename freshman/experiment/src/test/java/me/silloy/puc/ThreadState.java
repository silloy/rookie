package me.silloy.puc;

import lombok.Data;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author shaohuasu
 * @date 2019/9/15 2:58 PM
 * @since 1.8
 */
public class ThreadState {

    @Test
    public void state() {
        Thread t = new Thread(() -> {
            System.out.println("helllo");
        });
        t.getState();
    }


    @Test
    public void multi() throws InterruptedException {
        System.out.println("Testing Gate.....");
        Gate gate = new Gate();
        new UserThread(gate, "Alice", "Alaska").start();
        new UserThread(gate, "Blice", "Blaska").start();
        new UserThread(gate, "Clice", "Claska").start();

        TimeUnit.SECONDS.sleep(10L);
    }

    public static class UserThread extends Thread {
        private final Gate gate;
        private final String myname;
        private final String myaddress;

        public UserThread(Gate gate, String name, String address) {
            this.gate = gate;
            this.myname = name;
            this.myaddress = address;
        }

        public void run() {
            System.out.println(myname + " BEGIN");
            while (true) {
                gate.pass(myname, myaddress);
            }
        }
    }

    @Data
    public static class Gate {
        private int counter = 0;
        private String name = "no";
        private String address = "addr";

        public synchronized void pass(String name, String address) {
            this.counter++;
            this.name= name;
            this.address = address;
            check();
        }

        private void check() {
            if (name.charAt(0) != address.charAt(0)) {
                System.out.println(" ******* BROKEN ****** " + toString());
            }
        }
    }


}
