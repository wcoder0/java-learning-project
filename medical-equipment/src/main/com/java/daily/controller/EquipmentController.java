package com.java.daily.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.Equipment;
import com.java.daily.model.EquipmentType;
import com.java.daily.service.EquipmentService;
import com.java.daily.service.EquipmentTypeService;
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
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;


    @GetMapping("/list")
    public RespModel equipmentList(@RequestBody RequestBean requestBean) {
        PageResult pageResult = null;

        try {
            Page<Equipment> equipmentPage = new Page<>();
            equipmentPage.setCurrent(requestBean.getPageNum());
            equipmentPage.setSize(requestBean.getPageSize());

            Page<Equipment> page = equipmentService.page(equipmentPage);

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
    public RespModel equipmentAdd(@RequestBody Equipment equipment) {
        try {
            equipmentService.save(equipment);
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

    @DeleteMapping("/delete")
    public RespModel deleteequipment(@RequestParam Integer id) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
            equipmentService.remove(queryWrapper);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }

        return RespModel.success();
    }

    @DeleteMapping("/get")
    public RespModel getequipment(@RequestParam Integer id) {
        try {
            Equipment equipment = equipmentService.getById(id);
            return RespModel.success(equipment);
        }
        catch(Exception e) {
            log.error("操作失败", e);
            return RespModel.error();
        }
    }

}

