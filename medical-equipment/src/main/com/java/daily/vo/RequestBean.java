package com.java.daily.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: Hannibal
 * @Date: 2021/7/7 13:57
 * @Version 1.0
 * @description
 */
@Data
public class RequestBean implements Serializable {

    private static final long serialVersionUID = -7443304902819898146L;

    protected int pageNum = 1;

    protected int pageSize = 10;

    protected Object data;

}
