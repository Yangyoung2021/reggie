package com.yang.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐菜品关系
 */
@Data
public class SetMealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    //套餐id
    private String setmealId;


    //菜品id
    private String dishId;


    //菜品名称 （冗余字段）
    private String name;

    //菜品原价
    private BigDecimal price;

    //份数
    private Integer copies;


    //排序
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private String createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;


    //是否删除
    private Integer isDeleted;
}
