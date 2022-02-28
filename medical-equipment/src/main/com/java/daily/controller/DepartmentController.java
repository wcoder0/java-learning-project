package com.java.daily.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.Department;
import com.java.daily.service.DepartmentService;
import com.java.daily.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wm
 * @since 2022-02-27
 */
@Slf4j
@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @GetMapping("/list")
    public RespModel departmentList(@RequestBody RequestBean requestBean) {
        PageResult pageResult = null;

        try {
            Page<Department> departmentPage = new Page<>();
            departmentPage.setCurrent(requestBean.getPageNum());
            departmentPage.setSize(requestBean.getPageSize());

            Page<Department> page = departmentService.page(departmentPage);

            pageResult = new PageResult();
            pageResult.setCurrPage(requestBean.getPageNum());
            pageResult.setList(page.getRecords());
            pageResult.setTotalCount(page.getTotal());
            return RespModel.success(pageResult);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }


    @PostMapping("/add")
    public RespModel departmentAdd(@RequestBody Department department) {
        try {
            departmentService.save(department);
            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PutMapping("/update")
    public RespModel updateAdd(@RequestBody Department department) {
        try {
            departmentService.updateById(department);
            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @DeleteMapping("/delete")
    public RespModel deletedepartment(@RequestParam Integer id) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            departmentService.remove(queryWrapper);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }

        return RespModel.success();
    }

    @DeleteMapping("/get")
    public RespModel getdepartment(@RequestParam Integer id) {
        try {
            Department department = departmentService.getById(id);
            return RespModel.success(department);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

}

