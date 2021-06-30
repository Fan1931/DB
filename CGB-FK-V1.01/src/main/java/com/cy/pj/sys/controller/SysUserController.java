package com.cy.pj.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;

@Controller
@RequestMapping("/user/")
public class SysUserController {

	 @Autowired
	 private SysUserService sysUserService;
	 
	 @RequestMapping("doUserListUI")
	 public String doUserListUI() {
		 return "sys/user_list";
	 }
	 @RequestMapping("doUserEditUI")
	 public String doUserEditUI() {
		 return "sys/user_edit";
	 }
	 @RequestMapping("doPwdEditUI")
	 public String doPwdEditUI() {
		 return "sys/pwd_edit";
	 }
	 
	 @RequestMapping("doUpdatePassword")
	 @ResponseBody
	 public JsonResult doUpdatePassword(
			 String pwd,
			 String newPwd,
			 String cfgPwd) {
		 sysUserService.updatePassword(pwd, newPwd, cfgPwd);
		 return new JsonResult("update ok");
	 }
	 
	 @RequestMapping("doLogin")
	 @ResponseBody
	 public JsonResult doLogin(
			 boolean isRememberMe,
			 String username,
			 String password) {
		 
		 //1.封装用户信息
		 UsernamePasswordToken token=
		 new UsernamePasswordToken(username, password);
		 if(isRememberMe) {
			token.setRememberMe(true); 
		 }
		 //2.提交用户信息
		 Subject subject=SecurityUtils.getSubject();
		 subject.login(token);//token会提交给securityManager
		 return new JsonResult("login ok");
	 }
	 
	 @RequestMapping("doFindObjectById")
	 @ResponseBody
	 public JsonResult doFindObjectById(Integer id) {
		 return new JsonResult(
		 sysUserService.findObjectById(id));
	 }
	 @RequestMapping("doUpdateObject")
	 @ResponseBody
	 public JsonResult doUpdateObject(SysUser entity,Integer[] roleIds) {
		 sysUserService.updateObject(entity, roleIds);
		 return new JsonResult("update ok");
	 }
	 @RequestMapping("doSaveObject")
	 @ResponseBody
	 public JsonResult doSaveObject(SysUser entity,Integer[] roleIds) {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	 }
	 
	 @RequestMapping("doValidById")
	 @ResponseBody
	 public JsonResult doValidById(Integer id,Integer valid) {
		 //获取登陆的用户信息
		 SysUser user=(SysUser)
		 SecurityUtils.getSubject().getPrincipal();
		 sysUserService.validById(id, valid,user.getUsername());
		 return new JsonResult("update ok");
	 }
	 
	 @RequestMapping("doFindPageObjects")
	 @ResponseBody
	 public JsonResult doFindPageObjects(
			 String username,Integer pageCurrent) {
		 return new JsonResult(
		sysUserService.findPageObjects(username, pageCurrent));
	 }
	 
}
