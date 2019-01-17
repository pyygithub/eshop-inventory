package com.wolf.eshop.inventory.thread;


import com.wolf.eshop.inventory.request.Request;
import com.wolf.eshop.inventory.request.RequestQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RequestProcessorThreadPool {

    // 默认10个线程
    private int nThreads = 10;

    // 默认100个队列
    private int queueCapacity = 100;

    // 线程池
    private ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);

    private RequestProcessorThreadPool() {
        RequestQueue requestQueue = RequestQueue.getInstance();
        for (int i = 0; i < nThreads; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(queueCapacity);
            requestQueue.addQueue(queue);

            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    /**
     * 静态内部类方式实现单例模式
     * JVM的机制去保障多线程并发安全
     * 内部类的初始化，一定只会发生一次，不过多少个线程并发访问
     */
    public static class SingleTon{
        private static RequestProcessorThreadPool instance = new RequestProcessorThreadPool();
    }

    public static RequestProcessorThreadPool getInstance() {
        return SingleTon.instance;
    }

    /**
     * 初始化方法
     */
    public static void init() {
        getInstance();
    }
}