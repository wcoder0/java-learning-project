package com.java.daily.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Author: Hannibal
 * @Date: 2019/7/2 13:59
 * @Version 1.0
 * @description 分页查询参数
 */
public class PageQuery extends Page {

    public PageQuery(RequestBean requestBean) {
        //super.setPages(requestBean.getPageNum());
        super.setSize(requestBean.getPageSize());
        super.setCurrent(requestBean.getPageNum());
    }
}
