package com.wolf.eshop.inventory.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static Result success() {
        return new Result(0, "success", null);
    }

    public static Result error() {
        return new Result(500, "error", null);
    }
}