package com.owner.order.controller;

import com.owner.order.service.order.OrderService;
import com.owner.order.vo.OrderReqVO;
import com.owner.order.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 订单前端控制器
 * </p>
 *
 * @author pengdonglan123
 * @version V1.0
 * @date 2022-04-21
 */
@RefreshScope
@Slf4j
@Api(value = "订单控制器", tags = "订单控制器")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Value("${name}")
    private String name;
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "添加订单")
    @PostMapping("/v1/createOrder")
    public Result<String> createOrder(@RequestBody @Valid OrderReqVO vo) {
        return orderService.createOrder(vo);
    }

    @ApiOperation(value = "测试nacos")
    @GetMapping("/v1/testNacos")
    public Result<String> testNacos() {
        return Result.success(name);
    }

}
