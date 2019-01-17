package com.wolf.eshop.inventory.service;

import com.wolf.eshop.inventory.request.Request;

/**
 * 请求异步执行的service
 *
 * 请求路由处理
 */
public interface RequestAsyncProcessService {
    void process(Request request);
}
