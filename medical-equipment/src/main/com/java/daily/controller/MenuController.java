package com.java.daily.controller;

import java.util.*;

import com.java.daily.model.Menu;
import com.java.daily.vo.RespModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class MenuController {

   @GetMapping("/findMenus")
   public List<Menu> findMenus() {
      RespModel responseModel = new RespModel();

      List<Menu> menus = new ArrayList<>();
      Menu menu = new Menu();
      menu.setMenuId(1);
      menu.setMenuName("用户管理");
      menu.setHref("/userManager/userManager.html");
      menu.setTarget(0);
      menu.setType(0);
      menus.add(menu);

      Menu menu1 = new Menu();
      menu1.setMenuId(2);
      menu1.setMenuName("系统管理");
      menu1.setHref("reportManager");
      menu1.setTarget(0);
      menu1.setType(0);
      menus.add(menu1);

      Menu menu2 = new Menu();
      menu2.setMenuId(3);
      menu2.setMenuName("设备管理");
      menu2.setHref("apponitManager");
      menu2.setTarget(0);
      menu2.setType(0);
      menus.add(menu2);


      Menu menu3 = new Menu();
      menu3.setMenuId(4);
      menu3.setMenuName("业主信息");
      menu3.setHref("ownerManager");
      menu3.setTarget(0);
      menu3.setType(0);
      menus.add(menu3);

      responseModel.setSuccess(true);
      responseModel.setData(menus);
      return menus;
      //return responseModel;
   }

}
