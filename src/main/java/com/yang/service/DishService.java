package com.yang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.pojo.Dish;
import com.yang.vo.DishDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService extends IService<Dish> {

    @Transactional
    void insert(DishDto dishDto);

    DishDto getWithFlavor(String id);

    void updateWithFlavors(DishDto dishDto);

    void removeWithFlavors(List<String> ids);
}
