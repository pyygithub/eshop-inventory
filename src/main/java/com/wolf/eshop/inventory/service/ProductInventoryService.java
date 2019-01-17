package com.wolf.eshop.inventory.service;

import com.wolf.eshop.inventory.model.ProductInventory;

/**
 * 商品库存Service
 */
public interface ProductInventoryService {

    void updateProductInventory(ProductInventory productInventory);

    void removeProductInventoryCache(ProductInventory productInventory);

    ProductInventory findProductInventory(String productId);

    ProductInventory getProductInventoryCache(String productId);

    void setProductInventoryCache(ProductInventory productInventory);
}
