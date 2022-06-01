package com.yang.vo;

import com.yang.pojo.Dish;
import com.yang.pojo.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
    private String categoryName;
    private Integer copies;
}
