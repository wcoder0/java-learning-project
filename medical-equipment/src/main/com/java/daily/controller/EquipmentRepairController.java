package com.java.daily.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.EquipmentRepair;
import com.java.daily.service.EquipmentRepairService;
import com.java.daily.vo.PageResult;
import com.java.daily.vo.RespModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/repair")
public class EquipmentRepairController {

    @Autowired
    private EquipmentRepairService equipmentRepairService;


    @GetMapping("/list")
    public RespModel equipmentTypeList(EquipmentRepair equipment, Page<EquipmentRepair> page) {
        try {
            page = equipmentRepairService.page(page);
            return RespModel.success(page);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }


    @PostMapping("/add")
    public RespModel equipmentAdd(@RequestBody EquipmentRepair equipment) {
        try {
            if(equipment.getId() == null) {
                equipment.setCreateTime(new Date());
                equipment.setUpdateTime(new Date());
                equipmentRepairService.save(equipment);
            }
            else {
                equipment.setUpdateTime(new Date());
                equipmentRepairService.updateById(equipment);
            }

            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PutMapping("/update")
    public RespModel updateAdd(@RequestBody EquipmentRepair equipment) {
        try {
            equipmentRepairService.updateById(equipment);
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
            equipmentRepairService.removeById(id);
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
            EquipmentRepair equipment = equipmentRepairService.getById(id);
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

        equipmentRepairService.removeByIds(Arrays.asList(ids));
        responseModel.setSuccess(true);
        return responseModel;
    }

}
