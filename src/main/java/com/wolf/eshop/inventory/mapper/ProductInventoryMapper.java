package com.wolf.eshop.inventory.mapper;

import com.wolf.eshop.inventory.model.ProductInventory;

public interface ProductInventoryMapper {
    /**
     * 更新库存数量
     * @param productInventory
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 根据商品ID查询商品库存
     * @param productId
     * @return
     */
    ProductInventory findProductInventory(String productId);
}
