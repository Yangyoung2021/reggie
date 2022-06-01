package com.yang.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.pojo.Orders;
import com.yang.service.OrderService;
import com.yang.vo.OrderVo;
import com.yang.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    public R<Object> page(OrderVo orderVo) {
        log.info("{}", orderVo);
        Page<Orders> pageInfo = new Page<>(orderVo.getPage(), orderVo.getPageSize());
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(orderVo.getNumber() != null, Orders::getNumber, orderVo.getNumber());
        queryWrapper.ge(orderVo.getBeginTime() != null, Orders::getOrderTime, orderVo.getBeginTime());
        queryWrapper.le(orderVo.getEndTime() != null, Orders::getOrderTime, orderVo.getEndTime());
        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }
}
