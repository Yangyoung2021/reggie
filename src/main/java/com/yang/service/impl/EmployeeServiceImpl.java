package com.yang.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.mapper.EmployeeMapper;
import com.yang.pojo.Employee;
import com.yang.service.EmployeeService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public Page<Employee> page(Integer currentPage, Integer pageSize, String name) {
        Page<Employee> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!Strings.isBlank(name),Employee::getName, name);
        employeeMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public Employee getOne(Wrapper<Employee> queryWrapper) {
        return employeeMapper.selectOne(queryWrapper);
    }

    @Override
    public int addOne(Employee employee) {
        return employeeMapper.insert(employee);
    }

    @Override
    public Employee selectById(String id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public void updateEmployee(Employee employee) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, employee.getId());
        employeeMapper.update(employee, queryWrapper);
    }
}
