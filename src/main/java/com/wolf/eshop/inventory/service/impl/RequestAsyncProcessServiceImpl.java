package com.wolf.eshop.inventory.service.impl;


import com.wolf.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.wolf.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.wolf.eshop.inventory.request.Request;
import com.wolf.eshop.inventory.request.RequestQueue;
import com.wolf.eshop.inventory.service.RequestAsyncProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求异步处理的service实现
 */
@Service
@Slf4j
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService{

    @Override
    public void process(Request request) {
        try {
            // 做请求路由，根据每个请求的商品ID，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());

            // 将请求放入对应的队列中，完成路由操作
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据商品ID查询对应的路由队列
     * @param productId
     * @return
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(String productId){
        RequestQueue requestQueue = RequestQueue.getInstance();

        // 获取productId的hash值
        String key = productId;
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，获取对应的内存队列index
        // 比如内存队列大小8，用内存队列的数量对hash取模后，结果一定在 0 ~ 7 之间
        // 任何一个商品的ID都会被路由到固定的内存队列中去
        int index = (requestQueue.queueSize() - 1) & hash;

        log.info("### 路由内存队列：index={}, productId={}", index, key);
        // 返回对应的内存队列
        return requestQueue.getQueue(index);
    }

}