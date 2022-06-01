package com.yang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.mapper.CategoryMapper;
import com.yang.pojo.Category;
import com.yang.pojo.Dish;
import com.yang.pojo.SetMeal;
import com.yang.service.CategoryService;
import com.yang.service.DishService;
import com.yang.service.SetMealService;
import com.yang.vo.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;

    @Override
    public void deleteById(Long id) {
        //判断当前分类是否包含菜品或者套餐
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(true, Dish::getCategoryId, id);
        if (dishService.count(queryWrapper) > 0) {
            //当前分类的菜品数量大于0
            throw new CustomException("当前分类关联了菜品，不能删除");
        }

        LambdaQueryWrapper<SetMeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(true, SetMeal::getCategoryId, id);
        if (setMealService.count(queryWrapper1) > 0) {
            //当前分类的菜品数量大于0
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        //逻辑处理
        Category category = getById(id);
        category.setIsDeleted(1);
        updateById(category);
    }
}
