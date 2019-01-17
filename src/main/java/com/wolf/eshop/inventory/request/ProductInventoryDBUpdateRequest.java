package com.wolf.eshop.inventory.request;

import com.wolf.eshop.inventory.model.ProductInventory;
import com.wolf.eshop.inventory.service.ProductInventoryService;
import com.wolf.eshop.inventory.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 比如一个商品发生了交易，那么就要修改这个商品对应的库存
 *
 * 此时就会发送请求过来，要求修改库存，那么这个请求就是所谓的：data update request  数据更新请求
 *
 * 我们采用 Cache Aside Pattern 缓存模式
 *
 *   (1) 删除缓存
 *   (2) 更新数据库
 *
 *   我们这里通过简化业务逻辑，来完成库存更新相关业务，而真实的业务场景下库存肯定是通过计算之后得到的，具体的
 *   场景需要根据系统的业务来编码
 */
@Slf4j
public class ProductInventoryDBUpdateRequest implements Request {

    private ProductInventory productInventory;

    @Autowired
    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
       this.productInventory = productInventory;
       this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {
        // 删除redis缓存
        productInventoryService.removeProductInventoryCache(productInventory);
        log.info("### 删除redis中的商品库存缓存，key={} ###", productInventory.getProductId());

        // 模拟删除redis缓存卡顿，没来得及更新数据库
        try {
            log.info("### 模拟删除redis缓存卡顿5s，再更新数据库 ###");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 修改数据库中的库存数量
        productInventoryService.updateProductInventory(productInventory);
        log.info("### 修改数据库商品数量成功 ###");
    }

    @Override
    public String getProductId() {
        return productInventory.getProductId();
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}