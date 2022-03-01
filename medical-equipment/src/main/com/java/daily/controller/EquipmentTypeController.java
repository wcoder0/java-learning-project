package com.java.daily.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.dao.EquipmentTypeMapper;
import com.java.daily.model.EquipmentType;
import com.java.daily.model.User;
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
@RestController
@RequestMapping("/equipmentType")
public class EquipmentTypeController {

   @Autowired
   private EquipmentTypeService equipmentTypeService;

   @GetMapping("/list")
   public RespModel equipmentTypeList(EquipmentType equipmentType, Page<EquipmentType> page) {
      PageResult pageResult = null;

      try {
         page = equipmentTypeService.page(page);

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
         equipmentTypeService.save(equipmentType);
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
         equipmentTypeService.updateById(equipmentType);
         return RespModel.success();
      }
      catch(Exception e) {
         log.error("操作失败", e);
         return RespModel.error();
      }
   }

   @DeleteMapping("/delete")
   public RespModel deleteequipmentType(@RequestParam Integer id) {
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

   @DeleteMapping("/get")
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

}

