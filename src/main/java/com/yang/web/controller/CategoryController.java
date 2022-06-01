package com.yang.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.pojo.Category;
import com.yang.service.CategoryService;
import com.yang.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类
     * @param page 当前页码
     * @param pageSize 每页显示条数
     * @return 分页封装数据
     */
    @RequestMapping("/page")
    public R<Object> pages(Integer page, Integer pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //只查询没有删除的菜品
        queryWrapper.eq(true, Category::getIsDeleted, 0);
        //排序查询
        queryWrapper.orderByDesc(Category::getSort);
        //封装数据
        categoryService.page(pageInfo, queryWrapper);
        //返回结果
        return R.success(pageInfo);
    }

    @PostMapping
    public R<Object> addOne(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<Object> updateDeleted(Long id) {
        log.info("当前接收到的id为：{}", id);
        categoryService.deleteById(id);
        return R.success("删除成功");
    }

    @PutMapping
    public R<Object> updateById(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功！！");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //创建构建条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //根据类型相同查询
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.eq(Category::getIsDeleted, 0);
        //设置排序
        queryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询数据库
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList);
    }

}
