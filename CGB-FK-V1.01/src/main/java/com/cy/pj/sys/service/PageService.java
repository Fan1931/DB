package com.cy.pj.sys.service;
import com.cy.pj.common.vo.PageObject;
public interface PageService<T> {
	 PageObject<T> findPageObjects(
			 String username,Integer pageCurrent);
}
