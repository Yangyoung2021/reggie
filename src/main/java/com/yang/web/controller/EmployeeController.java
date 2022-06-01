package com.yang.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.intercepetor.BaseContext;
import com.yang.pojo.Employee;
import com.yang.service.EmployeeService;
import com.yang.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 分页查询员工
     *
     * @param page     当前页码
     * @param pageSize 每页显示条数
     * @param name     查询的用户名称
     * @return R
     */
    @RequestMapping("/page")
    public R<Page<Employee>> page(Integer page, Integer pageSize, String name) {
        Page<Employee> employeePage = employeeService.page(page, pageSize, name);
        return R.success(employeePage);
    }

    /**
     * 登陆功能，登陆成功需要将员工id放到session中
     *
     * @param request HttpServletRequest用来获取session对象
     * @param loginEmployee 登陆用户
     * @return R
     */
    @RequestMapping("/login")
    public R<Object> login(HttpServletRequest request, @RequestBody Employee loginEmployee) {
        //根据密码进行加密
        String password = DigestUtils.md5DigestAsHex(loginEmployee.getPassword().getBytes());
        //构建查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, loginEmployee.getUsername());
        //根据名称查询数据库
        Employee employee = employeeService.getOne(queryWrapper);
        //如果查询结果不为或者密码不相等直接返回登陆失败
        if (employee == null || !Objects.equals(employee.getPassword(), password)) {
            return R.error("登陆失败");
        }
        //用户状态被禁用
        if (employee.getStatus() == 0) {
            return R.error("用户已被禁用");
        }
        //此时用户已查询到且状态正常，将用户id保存到session
        request.getSession().setAttribute("employee", employee.getId());
        return R.success(employee);
    }

    /**
     * 员工退出登陆
     *
     * @param request 用来移除session信息
     * @return R
     */
    @RequestMapping("/logout")
    public R<Object> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        BaseContext.removeCurrentId();
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param request 用来获取session
     * @param employee 添加的员工
     * @return R
     */
    @PostMapping
    public R<Object> addOne(HttpServletRequest request, @RequestBody Employee employee) {
        System.out.println(employee);
        //完善员工信息
        employee.setPassword(DigestUtils.md5DigestAsHex(Employee.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获取当前登陆对象
        String loginId = (String) request.getSession().getAttribute("employee");
        employee.setCreateUser(loginId);
        employee.setUpdateUser(loginId);
        //调用service保存员工信息
        employeeService.addOne(employee);
        return R.success("添加成功！！");
    }


    /**
     * 根据id查询员工进行数据复现
     * @param id 要查询的员工id
     * @return R
     */
    @GetMapping("/{id}")
    public R<Object> queryById(@PathVariable String id) {
        Employee employee = employeeService.selectById(id);
        return R.success(employee);
    }

    /**
     * 修改员工信息
     * @param employee 修改的员工信息
     * @return R
     */
    @PutMapping
    public R<Object> update(HttpServletRequest request, @RequestBody Employee employee) {
        String loginId = (String) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(loginId);
        employeeService.updateEmployee(employee);
        return R.success("修改成功！！");
    }

}
