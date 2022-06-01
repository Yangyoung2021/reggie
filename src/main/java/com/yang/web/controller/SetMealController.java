package com.yang.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.pojo.Category;
import com.yang.pojo.SetMeal;
import com.yang.service.CategoryService;
import com.yang.service.SetMealService;
import com.yang.vo.R;
import com.yang.vo.SetMealDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Common common;

    /**
     * 分页查询套餐信息
     * @param page 当前页码
     * @param pageSize 每页显示条数
     * @param name 搜索的菜名
     * @return 套餐集合
     */
    @RequestMapping("/page")
    public R<Object> page(Integer page, Integer pageSize, String name) {
        //构建套餐的查询条件
        Page<SetMeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetMeal::getIsDeleted, 0);
        queryWrapper.like(name != null, SetMeal::getName, name);
        //查询数据库
        setMealService.page(pageInfo, queryWrapper);

        //构建返回数据的page对象
        Page<SetMealDto> dtoPage = new Page<>();
        //将pageInfo中的信息复制到返回数据中
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        log.info("{}", dtoPage.getRecords());
        //给categoryName属性赋值
        List<SetMealDto> setMealDtoList = pageInfo.getRecords().stream().map((setMeal -> {
            //构建查询分类的条件
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.select(Category::getName);
            categoryLambdaQueryWrapper.eq(Category::getId, setMeal.getCategoryId());

            Category category = categoryService.getOne(categoryLambdaQueryWrapper);
            log.info("查询到的category对象：{}", category);

            //创建Dto对象
            SetMealDto setMealDto = new SetMealDto();
            BeanUtils.copyProperties(setMeal, setMealDto);
            setMealDto.setCategoryName(category.getName());

            return setMealDto;
        })).collect(Collectors.toList());

        dtoPage.setRecords(setMealDtoList);

        return R.success(dtoPage);
    }

    /**
     * 保存套餐信息
     * @param setMealDto 套餐交互类
     * @return 操作结果
     */
    @PostMapping
    public R<Object> save(@RequestBody SetMealDto setMealDto) {
        setMealService.saveWithDishes(setMealDto);
        return R.success("添加成功！！");
    }

    @GetMapping("/{id}")
    public R<Object> selectById(@PathVariable() String id) {
        SetMealDto setMealDto = setMealService.getWithDishes(id);
        return R.success(setMealDto);
    }

    @PutMapping
    public R<Object> updateSetMeal(@RequestBody SetMealDto setMealDto) {
        SetMeal setMeal = setMealService.getById(setMealDto.getId());
        common.updateImage(setMeal);
        setMealService.updateWithDishes(setMealDto);
        return R.success("更新成功！！");
    }

    @DeleteMapping
    public R<Object> deleteBatch(@RequestParam List<String> ids) {
        setMealService.removeWithDishes(ids);

        return R.success("删除成功");
    }

    @PostMapping("status/{status}")
    public R<Object> updateStatus(@PathVariable() Integer status, @RequestParam List<String> ids) {
        ids.forEach(id -> {
            SetMeal setMeal = new SetMeal();
            setMeal.setStatus(status);
            setMeal.setId(id);

            setMealService.updateById(setMeal);
        });

        return R.success("修改成功！！");
    }


}
