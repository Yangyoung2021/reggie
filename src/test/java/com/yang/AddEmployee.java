package com.yang;

import com.yang.pojo.Employee;
import com.yang.service.EmployeeService;
import com.yang.service.SetMealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest(classes = ReggieApplication.class)
public class AddEmployee {

    @Resource
    private EmployeeService employeeService;

    @Autowired
    private SetMealService setMealService;

    @Test
    public void addEmployee() {

        for (int i = 0; i < 50; i++) {
            Employee employee = new Employee();
            employee.setUsername(String.valueOf(1000 + i));
            employee.setName(String.valueOf(1000 + i));
            employee.setPassword(Employee.DEFAULT_PASSWORD);
            employee.setPhone(String.valueOf(13800000000L + i * 1000));
            employee.setSex(i % 2 == 0 ? "男" : "女");
            employee.setIdNumber(String.valueOf(10000L + i * 20));
            employee.setStatus(1);
            employee.setUpdateTime(LocalDateTime.now());
            employee.setCreateTime(LocalDateTime.now());
            employeeService.addOne(employee);
        }
    }

    @Test
    public void SetMealTest() {
        System.out.println(setMealService);
    }

}
