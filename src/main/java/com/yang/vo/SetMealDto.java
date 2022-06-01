package com.yang.vo;

import com.yang.pojo.SetMeal;
import com.yang.pojo.SetMealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetMealDto extends SetMeal {

    //套餐名称
    private String categoryName;
    //套餐包含的菜品集合
    private List<SetMealDish> setmealDishes;
}
