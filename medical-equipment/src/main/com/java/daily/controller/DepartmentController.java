package com.java.daily.controller;


import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.daily.model.Department;
import com.java.daily.model.EquipmentType;
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
@RestController
@RequestMapping("/department")
public class DepartmentController {

   @Autowired
   private DepartmentService departmentService;


   @GetMapping("/list")
   public RespModel equipmentTypeList(Department department, Page<Department> page) {
      try {
         page = departmentService.page(page);
         return RespModel.success(page);
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

   @GetMapping("/get/{id}")
   public RespModel deletedepartment(@PathVariable Integer id) {
      try {
         departmentService.removeById(id);
      }
      catch(Exception e) {
         log.error("操作失败", e);
         return RespModel.error();
      }

      return RespModel.success();
   }

   @DeleteMapping("/delete/{id}")
   public RespModel getdepartment(@PathVariable Integer id) {
      try {
         Department department = departmentService.getById(id);
         return RespModel.success(department);
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

      departmentService.removeByIds(Arrays.asList(ids));
      responseModel.setSuccess(true);
      return responseModel;
   }

}

