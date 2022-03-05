package com.java.daily.controller;


import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.dao.EquipmentTypeMapper;
import com.java.daily.model.EquipmentType;
import com.java.daily.model.User;
import com.java.daily.service.EquipmentTypeService;
import com.java.daily.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
@RestController
@RequestMapping("/equipmentType")
public class EquipmentTypeController {

    @Autowired
    private EquipmentTypeService equipmentTypeService;

    @GetMapping("/list")
    public RespModel equipmentTypeList(EquipmentType equipmentType, Page<EquipmentType> page) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();

            if(equipmentType != null && !StringUtils.isEmpty(equipmentType.getName())) {
                queryWrapper.likeLeft("name", equipmentType.getName());
            }

            page = equipmentTypeService.page(page, queryWrapper);

            return RespModel.success(page);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }


    @PostMapping("/add")
    public RespModel equipmentTypeAdd(@RequestBody EquipmentType equipmentType) {
        try {
            if(equipmentType.getId() == null) {
                equipmentType.setCreateTime(new Date());
                equipmentType.setUpdateTime(new Date());
                equipmentTypeService.save(equipmentType);
            }
            else {
                equipmentType.setUpdateTime(new Date());
                equipmentTypeService.updateById(equipmentType);
            }

            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PutMapping("/update")
    public RespModel updateAdd(@RequestBody EquipmentType equipmentType) {
        try {
            if(equipmentType.getId() == null) {
                equipmentType.setCreateTime(new Date());
                equipmentType.setUpdateTime(new Date());
                equipmentTypeService.save(equipmentType);
            }
            else {
                equipmentType.setUpdateTime(new Date());
                equipmentTypeService.updateById(equipmentType);
            }

            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @DeleteMapping("/delete/{id}")
    public RespModel deleteequipmentType(@PathVariable("id") Integer id) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            equipmentTypeService.remove(queryWrapper);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }

        return RespModel.success();
    }

    @GetMapping("/get")
    public RespModel getequipmentType(@RequestParam Integer id) {
        try {
            EquipmentType equipmentType = equipmentTypeService.getById(id);
            return RespModel.success(equipmentType);
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

        equipmentTypeService.removeByIds(Arrays.asList(ids));
        responseModel.setSuccess(true);
        return responseModel;
    }

}

