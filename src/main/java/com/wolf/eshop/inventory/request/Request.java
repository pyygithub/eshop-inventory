package com.wolf.eshop.inventory.request;


public interface Request {

    void process();

    String getProductId();

    boolean isForceRefresh();
}