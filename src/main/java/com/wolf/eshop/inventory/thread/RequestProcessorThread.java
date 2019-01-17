package com.wolf.eshop.inventory.thread;


import com.wolf.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.wolf.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.wolf.eshop.inventory.request.Request;
import com.wolf.eshop.inventory.request.RequestQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

@Slf4j
public class RequestProcessorThread implements Callable<Boolean> {

    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            while(true) {
                // 这里之所以采用ArrayBlockQueue：如果队列满了或空的，那么都会在执行该操作时阻塞住
                Request request = queue.take();
                log.info("### 工作线程执行请求：productId={}", request.getProductId());

                boolean forceRefresh = request.isForceRefresh();

                if (!forceRefresh) {
                    log.info("### foreRefresh cache ###");
                    // 读请求去重优化：内存队列堆积多个读请求（刷新缓存请求）
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<String, Boolean> flagMap = requestQueue.getFlagMap();
                    if (request instanceof ProductInventoryDBUpdateRequest) {
                        log.info("### current is ProductInventoryDBUpdateRequest. set flagMap=[productId:{}, flag:{}]###", request.getProductId(), true);
                        // 如果是一个更新数据的请求，就将那个productId对应的标识设置为true
                        flagMap.put(request.getProductId(), true);
                    } else if(request instanceof ProductInventoryCacheRefreshRequest) {
                        Boolean flag = flagMap.get(request.getProductId());
                        // 如果是缓存刷新请求，那么就判断：标识为空，证明之前从没有执行过缓存更新（就认为数据就没有改数据） -- 空数据读请求过滤优化
                        if (flag == null) {
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果是缓存刷新请求，那么就判断：标识不为空，而是true，就说明之前有一个更新请求
                        if (flag != null && flag) {
                            log.info("### 当前请求是缓存刷新请求，标识不为空，而是true，说明之前有一个更新请求###",
                                    request.getProductId(), false);
                            flagMap.put(request.getProductId(), false);
                        }


                        // 3:如果是读请求（缓存刷新），那么就判断：标识不为空，而是false，就说明之前有一个更新请求 + 缓存刷新请求 或 之前有一个刷新请求，这里就没必要在刷新了
                        if(flag != null && !flag) {
                            log.info("### 当前缓存刷新请求，标识不为空，而是false，说明之前有一个更新请求 + 缓存刷新请求. 直接过滤掉 ###");
                            // 对于这种请求，直接就过滤掉，不要放在后面的请求队列中了
                            return true;
                        }
                    }
                }
                // 执行这个请求
                request.process();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}