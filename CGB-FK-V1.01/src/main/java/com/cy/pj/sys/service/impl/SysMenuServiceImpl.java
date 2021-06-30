package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    
	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Override
	public int updateObject(SysMenu entity) {
		//1.进行参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("菜单名不能为空");
		if(StringUtils.isEmpty(entity.getPermission()))
			throw new IllegalArgumentException("权限标识不能为空");
		//....
		//2.将对象写入到数据库
		int rows=0;
		try {
			rows=sysMenuDao.updateObject(entity);
		}catch(Throwable e) {
			e.printStackTrace();
			throw new ServiceException("update error");
		}
		return rows;
	}
	@Override
	public int saveObject(SysMenu entity) {
		//1.进行参数校验
		if(entity==null)
		throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
		throw new IllegalArgumentException("菜单名不能为空");
		if(StringUtils.isEmpty(entity.getPermission()))
		throw new IllegalArgumentException("权限标识不能为空");
		//....
		//2.将对象写入到数据库
		int rows=0;
		try {
		rows=sysMenuDao.insertObject(entity);
		}catch(Throwable e) {
		e.printStackTrace();
		throw new ServiceException("save error");
		}
		return rows;
	}

	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	
	@Override
	public int deleteObject(Integer id) {
		//1.验证参数有效性
		if(id==null||id<1)
		throw new IllegalArgumentException("id值不正确");
		//2.判定菜单是否有子菜单
		int childCount=sysMenuDao.getChildCount(id);
		if(childCount>0)
		throw new ServiceException("请先删除子菜单");
		//3.删除菜单自身信息
		int rows=sysMenuDao.deleteObject(id);
		if(rows==0)
		throw new ServiceException("菜单可能已经不存在");
		//4.删除菜单关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		return rows;
	}
	
	@Override
	public List<Map<String, Object>> findObjects() {
		List<Map<String,Object>> list=sysMenuDao.findObjects();
		if(list==null||list.size()==0)
	    throw new ServiceException("没有对应数据");
		return list;
	}

}
