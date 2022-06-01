package com.yang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.mapper.DishMapper;
import com.yang.pojo.Dish;
import com.yang.pojo.DishFlavor;
import com.yang.service.DishFlavorService;
import com.yang.service.DishService;
import com.yang.vo.DishDto;
import com.yang.web.controller.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private Common common;

    @Override
    public void removeWithFlavors(List<String> ids) {
        log.info("{}", ids);
        List<Dish> dishList = new ArrayList<>();
        ids.forEach(id -> {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);

            Dish dish = new Dish();
            dish.setId(id);
            dish.setIsDeleted(1);
            dishList.add(dish);
        });

        this.updateBatchById(dishList);
    }

    @Override
    public DishDto getWithFlavor(String id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    @Override
    public void updateWithFlavors(DishDto dishDto) {
        common.updateImage(getById(dishDto.getId()));
        //保存菜品
        this.updateById(dishDto);
        //清除原来的当前菜品的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //获取口味集合
        List<DishFlavor> flavors = dishDto.getFlavors();
        //给每个口味添加分类id
        flavors.forEach(item -> {
            item.setDishId(dishDto.getId());
        });
        //保存口味
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void insert(DishDto dishDto) {
        //保存菜品
        this.save(dishDto);
        //获取口味集合
        List<DishFlavor> flavors = dishDto.getFlavors();
        //给每个口味添加分类id
        flavors.forEach(item -> {
            item.setDishId(dishDto.getId());
        });
        //保存口味
        dishFlavorService.saveBatch(flavors);
    }
}
