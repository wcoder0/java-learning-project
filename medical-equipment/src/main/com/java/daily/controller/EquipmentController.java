package com.java.daily.controller;


import java.util.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.Equipment;
import com.java.daily.service.EquipmentService;
import com.java.daily.vo.PageResult;
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
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;


    @GetMapping("/list")
    public RespModel equipmentTypeList(Equipment equipment, Page<Equipment> page) {

        try {
            page = equipmentService.page(page);
            return RespModel.success(page);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }


    @PostMapping("/add")
    public RespModel equipmentAdd(@RequestBody Equipment equipment) {
        try {
            if(equipment.getId() == null) {
                equipment.setCreateTime(new Date());
                equipment.setUpdateTime(new Date());
                equipmentService.save(equipment);
            }
            else {
                equipment.setUpdateTime(new Date());
                equipmentService.updateById(equipment);
            }

            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PutMapping("/update")
    public RespModel updateAdd(@RequestBody Equipment equipment) {
        try {
            equipmentService.updateById(equipment);
            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @DeleteMapping("/delete/{id}")
    public RespModel deleteequipment(@PathVariable Integer id) {
        try {
            equipmentService.removeById(id);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }

        return RespModel.success();
    }

    @GetMapping("/get/{id}")
    public RespModel getequipment(@PathVariable Integer id) {
        try {
            Equipment equipment = equipmentService.getById(id);
            return RespModel.success(equipment);
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

        equipmentService.removeByIds(Arrays.asList(ids));
        responseModel.setSuccess(true);
        return responseModel;
    }

}

