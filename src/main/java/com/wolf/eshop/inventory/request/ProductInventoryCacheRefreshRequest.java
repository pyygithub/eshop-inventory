package com.wolf.eshop.inventory.request;


import com.alibaba.fastjson.JSON;
import com.wolf.eshop.inventory.model.ProductInventory;
import com.wolf.eshop.inventory.service.ProductInventoryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 重新加载商品库存缓存
 */
@Slf4j
@Data
public class ProductInventoryCacheRefreshRequest implements Request{
    // 商品ID
    private String productId;

    @Autowired
    private ProductInventoryService productInventoryService;

    // 是否强制刷新缓存
    private boolean forceRefresh = false;

    public ProductInventoryCacheRefreshRequest(String productId, ProductInventoryService productInventoryService, boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;

    }

    @Override
    public void process() {
        // 从数据库中查询最新的商品库存数量
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);
        log.info("### 从数据库查询商品缓存， productInventory={}", JSON.toJSON(productInventory));

        if (productInventory != null) {
            // 将最新的商品库存数量刷新到缓存中
            productInventoryService.setProductInventoryCache(productInventory);
            log.info("### 将最新的商品库存数量刷新到缓存中 ###");
        }
    }

    @Override
    public String getProductId() {
        return this.productId;
    }
}