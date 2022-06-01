package com.yang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.pojo.SetMeal;
import com.yang.vo.SetMealDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetMealService extends IService<SetMeal> {

    @Transactional
    void saveWithDishes(SetMealDto setMealDto);

    SetMealDto getWithDishes(String id);

    @Transactional
    void updateWithDishes(SetMealDto setMealDto);

    void removeWithDishes(List<String> ids);
}
