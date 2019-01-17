package com.wolf.eshop.inventory.service.impl;


import com.wolf.eshop.inventory.mapper.ProductInventoryMapper;
import com.wolf.eshop.inventory.model.ProductInventory;
import com.wolf.eshop.inventory.service.ProductInventoryService;
import com.wolf.eshop.inventory.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
 * 商品库存Service实现类
 */
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 修改商品库存数据库
     * @param productInventory
     */
    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    /**
     * 清除商品库存缓存
     * @param productInventory
     */
    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisService.del(key);
    }

    /**
     * 根据商品id查询库存信息
     * @param productId
     * @return
     */
    @Override
    public ProductInventory findProductInventory(String productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    /**
     * 获取商品库存缓存数据
     * @param productId
     * @return
     */
    @Override
    public ProductInventory getProductInventoryCache(String productId) {
        long inventoryCnt = 0l;
        String key = "product:inventory:" + productId;
        String value = redisService.get(key);
        if (!StringUtils.isEmpty(value)) {
            try {
                inventoryCnt = Long.valueOf(value);
                return new ProductInventory(key, inventoryCnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将商品库存添加到缓存中
     * @param productInventory
     */
    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisService.set(key, productInventory.getInventoryCnt() + "");
    }
}