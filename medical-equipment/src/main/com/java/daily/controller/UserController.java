package com.java.daily.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.User;
import com.java.daily.service.UserService;
import com.java.daily.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public RespModel userList(@RequestBody RequestBean requestBean) {
        PageResult pageResult = null;

        try {
            Page<User> userPage = new Page<>();
            userPage.setCurrent(requestBean.getPageNum());
            userPage.setSize(requestBean.getPageSize());

            Page<User> page = userService.page(userPage);

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
    public RespModel userAdd(@RequestBody User user) {
        try {
            userService.save(user);
            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PutMapping("/update")
    public RespModel updateAdd(@RequestBody User user) {
        try {
            userService.updateById(user);
            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @DeleteMapping("/delete")
    public RespModel deleteUser(@RequestParam Integer id) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            userService.remove(queryWrapper);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }

        return RespModel.success();
    }

    @DeleteMapping("/get")
    public RespModel getUser(@RequestParam Integer id) {
        try {
            User user = userService.getById(id);
            return RespModel.success(user);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

}

