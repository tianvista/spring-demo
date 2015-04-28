package com.elong.pb.example.service.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project：pb-example
 * Date: 15/4/28
 * Time: 11:53
 * file: ThreadPoolExecutorDemo.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        fixedThreadPool.shutdown();
    }
}
