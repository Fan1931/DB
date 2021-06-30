package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;

@Controller
@RequestMapping("/role/")
public class SysRoleController {
	 @Autowired
	 private SysRoleService sysRoleService;
	 
	 @GetMapping("doRoleListUI")
	 public String doRoleListUI() {
		 return "sys/role_list";
	 }
	 @GetMapping("doRoleEditUI")
	 public String doRoleEeditUI() {
		 return "sys/role_edit";
	 }
	 
	 @RequestMapping("doFindRoles")
	 @ResponseBody
	 public JsonResult doFindRoles() {
		 return new JsonResult(sysRoleService.findObjects());
	 }
	 
	 @RequestMapping("doFindObjectById")
	 @ResponseBody
	 public JsonResult doFindObjectById(Integer id) {
		 return new JsonResult(sysRoleService.findObjectById(id));
	 }
	 
	 @RequestMapping("doDeleteObject")
	 @ResponseBody
	 public JsonResult doDeleteObject(Integer id) {
		 sysRoleService.deleteObject(id);
		 return new JsonResult("delete ok");
	 }
	 
	 @RequestMapping("doUpdateObject")
	 @ResponseBody
	 public JsonResult doUpdateObject(
			 SysRole entity,Integer[] menuIds) {
		 sysRoleService.updateObject(entity, menuIds);
		 return new JsonResult("update ok");
	 }
	 @RequestMapping("doSaveObject")
	 @ResponseBody
	 public JsonResult doSaveObject(
			 SysRole entity,Integer[] menuIds) {
		 sysRoleService.saveObject(entity, menuIds);
		 return new JsonResult("save ok");
	 }
	 
	 @RequestMapping("doFindPageObjects")
	 @ResponseBody
	 public JsonResult doFindPageObjects(
			 String name,Integer pageCurrent) {
		 return new JsonResult(
		 sysRoleService.findPageObjects(name,
				 pageCurrent));
	 }
	 
	 
}
