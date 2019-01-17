package com.wolf.eshop.inventory.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求内存队列
 *
 * @Author: wolf
 * @Date: 2019/1/17 13:04
 */
public class RequestQueue {

    // 内存队列
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList();

    // 标示位map
    private Map<String, Boolean> flagMap = new ConcurrentHashMap<>();

    /**
     * 添加队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue) {

        this.queues.add(queue);
    }

    /**
     * 获取内存队列数量
     * @return
     */
    public int queueSize() {
        return queues.size();
    }

    /**
     * 获取对应index的queue
     * @param index
     * @return
     */
    public ArrayBlockingQueue<Request> getQueue(int index) {
        return this.queues.get(index);
    }

    public Map<String, Boolean> getFlagMap() {
        return flagMap;
    }

    public static class SingleTon {
        public static RequestQueue instance = new RequestQueue();
    }

    public static RequestQueue getInstance() {
        return SingleTon.instance;
    }
}