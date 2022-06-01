package com.yang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.mapper.SetMealMapper;
import com.yang.pojo.SetMeal;
import com.yang.pojo.SetMealDish;
import com.yang.service.SetMealDishService;
import com.yang.service.SetMealService;
import com.yang.vo.SetMealDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {

    @Autowired
    private SetMealDishService setMealDishService;


    /**
     * 将套餐信息保存
     * @param setMealDto 封装了套餐信息的封装类
     */
    @Override
    public void saveWithDishes(SetMealDto setMealDto) {
        //将套餐保存
        this.save(setMealDto);

        //将套餐中的菜品信息保存到另一个表格
        //获取菜品信息
        List<SetMealDish> setMealDishes = setMealDto.getSetmealDishes();
        setMealDishes.forEach(setMealDish -> {
            //设置套餐id
            setMealDish.setSetmealId(setMealDto.getId());
        });
        setMealDishService.saveBatch(setMealDishes);

    }

    /**
     * 根据套餐id获取套餐信息
     * @param setMealId 套餐id
     * @return 套餐全部信息
     */
    @Override
    public SetMealDto getWithDishes(String setMealId) {
        SetMealDto setMealDto = new SetMealDto();
        SetMeal setMeal = this.getById(setMealId);
        BeanUtils.copyProperties(setMeal, setMealDto);

        LambdaQueryWrapper<SetMealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetMealDish::getSetmealId, setMealId);
        List<SetMealDish> setmealDishes = setMealDishService.list(queryWrapper);

        setMealDto.setSetmealDishes(setmealDishes);

        return setMealDto;
    }

    @Override
    public void updateWithDishes(SetMealDto setMealDto) {
        String setMealId = setMealDto.getId();

        //删除数据库中原来的菜品信息
        LambdaQueryWrapper<SetMealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetMealDish::getSetmealId, setMealId);
        setMealDishService.remove(queryWrapper);

        //保存菜品
        this.updateById(setMealDto);

        //将套餐中的菜品信息保存到另一个表格
        //获取菜品信息
        List<SetMealDish> setMealDishes = setMealDto.getSetmealDishes();
        setMealDishes.forEach(setMealDish -> {
            //设置套餐id
            setMealDish.setSetmealId(setMealDto.getId());
        });
        setMealDishService.saveBatch(setMealDishes);
    }

    /**
     * 将套餐与其中菜品删除
     * @param setMealIds 要删除的套餐id集合
     */
    @Override
    public void removeWithDishes(List<String> setMealIds) {
        setMealIds.forEach(setMealId -> {
            //删除菜品
            LambdaQueryWrapper<SetMealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetMealDish::getSetmealId, setMealId);
            setMealDishService.remove(queryWrapper);
        });

        this.removeByIds(setMealIds);
    }


}
