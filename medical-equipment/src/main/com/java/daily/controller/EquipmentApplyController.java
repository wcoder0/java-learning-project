package com.java.daily.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.*;
import com.java.daily.service.*;
import com.java.daily.vo.RespModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/apply")
public class EquipmentApplyController {

    @Autowired
    private EquipmentApplyService equipmentApplyService;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/list")
    public RespModel equipmentTypeList(EquipmentApply equipmentApply, Page<EquipmentApply> page) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();

            if(equipmentApply != null && !StringUtils.isEmpty(equipmentApply.getEquipmentName())) {
                queryWrapper.likeLeft("equipment_name", equipmentApply.getEquipmentName());
            }

            if(equipmentApply != null && !StringUtils.isEmpty(equipmentApply.getUserId())) {
                queryWrapper.eq("user_id", equipmentApply.getUserId());
            }

            page = equipmentApplyService.page(page, queryWrapper);
            return RespModel.success(page);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }


    @PostMapping("/add")
    public RespModel equipmentAdd(@RequestBody EquipmentApply equipmentApply) {
        try {
            Integer equipmentId = equipmentApply.getEquipmentId();

            if(equipmentId != null) {
                Equipment equipment = equipmentService.getById(equipmentId);
                equipmentApply.setEquipmentName(equipment.getName());
            }

            if(equipmentApply.getId() == null) {
                equipmentApply.setCreateTime(new Date());
                equipmentApply.setUpdateTime(new Date());
                equipmentApplyService.save(equipmentApply);
            }
            else {
                equipmentApply.setUpdateTime(new Date());
                equipmentApplyService.updateById(equipmentApply);
            }

            return RespModel.success();
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

    @PutMapping("/update")
    public RespModel updateAdd(@RequestBody EquipmentApply equipmentApply) {
        try {
            equipmentApplyService.updateById(equipmentApply);
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
            equipmentApplyService.removeById(id);
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
            EquipmentApply equipmentApply = equipmentApplyService.getById(id);
            return RespModel.success(equipmentApply);
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

        equipmentApplyService.removeByIds(Arrays.asList(ids));
        responseModel.setSuccess(true);
        return responseModel;
    }

}
