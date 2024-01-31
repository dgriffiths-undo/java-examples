package io.undo;

import java.util.*;

public class ConcurrentModificationExceptionTest {
    static List<String> list = new ArrayList<String>();

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            list.add("something");
        }

        // CON_LINE1:
        new Thread(new BackgroundThread(), "BackgroundThread").start();

        for (int i = 0; ; i++) {
            try {
                for (String str : list) {
                    // stop the compiler optimizing the loop away!
                    if (str.equals("boo")) {
                        throw new Error("boo!");
                    }
                }
            } catch (Exception e) {
                // CON_LINE2:
                System.out.println("caught " + e + " with i = " + i);
                System.exit(1);
            }
        }
    }

    static class BackgroundThread implements Runnable {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(10);
                    Thread t = new Thread(new MutatorThread(true), "MutatorThread");
                    t.start();
                    t.join();
                    Thread.sleep(10);
                    t = new Thread(new MutatorThread(false), "MutatorThread");
                    t.start();
                    t.join();
                }
            } catch (Exception e) {
            }
        }
    }

    static class MutatorThread implements Runnable {
        boolean add;

        MutatorThread(boolean add) {
            this.add = add;
        }

        public void run() {
            if (add) {
                list.add("add");
            } else {
                list.remove(0);
            }
        }
    }
}
