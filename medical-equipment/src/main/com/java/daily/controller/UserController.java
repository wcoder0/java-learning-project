package com.java.daily.controller;


import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.User;
import com.java.daily.service.UserService;
import com.java.daily.vo.RespModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public RespModel list(User user, Page<User> page) {
        try {
            page = userService.page(page);
            return RespModel.success(page);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PostMapping("/add")
    public RespModel userAdd(@RequestBody User user) {
        try {
            if(user.getId() == null) {
                user.setCreateTime(new Date());
                user.setUpdateTime(new Date());
                userService.save(user);
            }
            else {
                user.setUpdateTime(new Date());
                userService.updateById(user);
            }

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

    @DeleteMapping("/delete/{id}")
    public RespModel deleteUser(@PathVariable Integer id) {
        try {
            userService.removeById(id);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }

        return RespModel.success();
    }

    @GetMapping("/get/{id}")
    public RespModel getUser(@PathVariable Integer id) {
        try {
            User user = userService.getById(id);
            return RespModel.success(user);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @DeleteMapping("/deleteByIds")
    public RespModel deleteUserByIds(@RequestBody String[] ids) {
        RespModel responseModel = new RespModel();

        if(null == ids) {
            return RespModel.error("入参为空");
        }

        userService.removeByIds(Arrays.asList(ids));
        responseModel.setSuccess(true);
        return responseModel;
    }

}

