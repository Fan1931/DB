package com.cy.pj.sys.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
@Controller
@RequestMapping("/log/")//日志模块根路径
public class SysLogController {
	  @Autowired
	  private SysLogService sysLogService;
	  @RequestMapping(value= {"doLogListUI","logListUI"})
	  public String doLogListUI() {
		  return "sys/log_list";
	  }
	  @PostMapping("doDeleteObjects")
	  @ResponseBody
	  public JsonResult doDeleteObjects(Integer...ids) {
		  int rows=sysLogService.deleteObjects(ids);
		  return new JsonResult("delete ok,rows="+rows);
	  }
	  /**
	   * 分页查询用户行为日志信息
	   * @param username
	   * @param pageCurrent
	   * @return
	   * 返回值要转换为json格式的串
	   * 1)添加依赖(例如jackson)
	   * 2)配置文件中添加<mvc:annotation-driven/>
	   * 3)使用@ResponseBody注解修饰方法
	   */
	  @GetMapping("doFindPageObjects")
	  @ResponseBody
	  public JsonResult doFindPageObjects(
			  String username,
			  Integer pageCurrent) {
		  PageObject<SysLog> pageObject=
		  sysLogService.findPageObjects(username,
				  pageCurrent);
		  return new JsonResult(pageObject);
	  }
	  
	  
}