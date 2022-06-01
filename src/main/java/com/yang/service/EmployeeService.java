package com.yang.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.pojo.Employee;


public interface EmployeeService {
    Page<Employee> page(Integer currentPage, Integer pageSize, String name);

    Employee getOne(Wrapper<Employee> queryWrapper);

    int addOne(Employee employee);

    Employee selectById(String id);

    void updateEmployee(Employee employee);
}
