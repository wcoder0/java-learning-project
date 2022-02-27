package com.java.daily.vo;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页工具类
 *
 * @Author: Hannibal
 * @Date: 2019/7/2 13:59
 * @Version 1.0
 * @description
 */
@NoArgsConstructor
@Data
public class PageResult implements Serializable {

    //总记录数
    private long totalCount;
    //每页记录数
    private int pageSize;
    //总页数
    private int totalPage;
    //当前页数
    private int currPage;
    //列表数据
    private List<?> list = new ArrayList<>();

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageResult(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public PageResult(Page page) {
        this.list = page.getRecords();
        this.totalCount = Math.toIntExact(page.getTotal());
        this.pageSize = Math.toIntExact(page.getSize());
        this.currPage = Math.toIntExact(page.getCurrent());
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

}
