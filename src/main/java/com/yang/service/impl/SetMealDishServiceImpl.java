package com.yang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.mapper.SetMealDishMapper;
import com.yang.pojo.SetMealDish;
import com.yang.service.SetMealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetMealDishServiceImpl extends ServiceImpl<SetMealDishMapper, SetMealDish>
        implements SetMealDishService {
}
