package com.yang.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.pojo.Category;
import com.yang.pojo.Dish;
import com.yang.service.CategoryService;
import com.yang.service.DishService;
import com.yang.vo.DishDto;
import com.yang.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Common common;


    @RequestMapping("/page")
    public R<Object> pages(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //只查询没有删除的菜品
        queryWrapper.eq(Dish::getIsDeleted, 0);
        queryWrapper.like(!Strings.isBlank(name), Dish::getName, name);
        //排序查询
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //封装数据
        dishService.page(pageInfo, queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //获取菜品列表
        List<Dish> dishList = pageInfo.getRecords();
        List<DishDto> list = dishList.stream().map(item -> {
            //创建封装数据的dto对象
            DishDto dishDto = new DishDto();
            //将数据转储到dto对象中
            BeanUtils.copyProperties(item, dishDto);
            //设置菜品名称
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        //返回结果
        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<Object> list(@RequestBody DishDto dishDto) {
        dishService.insert(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable String id) {
        DishDto dishDto = dishService.getWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavors(dishDto);
        return R.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public R<Object> stopSell(@PathVariable("status") Integer status, @RequestParam List<String> ids) {
        log.info("收到的status：{}, 收到的ids：{}", status, ids);
        List<Dish> dishList = new ArrayList<>();
        ids.forEach(id -> {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishList.add(dish);
            //修改图片
            common.updateImage(dish);
        });
        dishService.updateBatchById(dishList);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<Object> delete(@RequestParam List<String> ids) {
        dishService.removeWithFlavors(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<Object> getDishByCategoryId(String categoryId, String name) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name != null, Dish::getName, name);
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);

        List<Dish> categories = dishService.list(queryWrapper);

        return R.success(categories);
    }
}
