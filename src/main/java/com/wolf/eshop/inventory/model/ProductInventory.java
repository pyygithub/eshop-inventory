package com.wolf.eshop.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventory {
    // 商品id
    private String productId;

    // 库存数量
    private Long inventoryCnt;
}