package com.yqsl.arthur.order.controller;

import com.google.common.collect.ImmutableMap;
import com.yqsl.infra.common.base.result.ApiResponse;
import com.yqsl.infra.common.base.result.BaseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {


    @RequestMapping("/order")
    public ApiResponse response() {
        Map<String, String> map = Map.of("a", "b");
        return ApiResponse.success(map);
    }


    @RequestMapping("/exception")
    public ApiResponse exception() {
        Map<String, String> map = Map.of("a", "b");
        throw new IllegalArgumentException("parameter error");
    }

}
