package com.cy.pj.sys.service;

import com.cy.pj.sys.entity.SysLog;
public interface SysLogService extends PageService<SysLog>{
	
	   
	   /**
	    * 基于id删除日志信息
	    * @param ids
	    * @return
	    */
	   int deleteObjects(Integer... ids);
	  

}







