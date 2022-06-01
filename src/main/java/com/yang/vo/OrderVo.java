package com.yang.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class OrderVo {

    private Integer page;
    private Integer pageSize;
    private String number;
    private String beginTime;
    private String endTime;
}
