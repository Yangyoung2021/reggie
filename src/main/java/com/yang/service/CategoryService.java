package com.yang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.pojo.Category;

public interface CategoryService extends IService<Category> {
    void deleteById(Long id);
}
