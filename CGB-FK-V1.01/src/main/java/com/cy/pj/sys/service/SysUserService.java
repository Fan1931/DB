package com.cy.pj.sys.service;
import java.util.Map;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;
public interface SysUserService 
     extends PageService<SysUserDeptVo>{
	/**
	 * 修改密码
	 * @param password 原密码
	 * @param newPassword 新密码(还没加密)
	 * @param cfgPassword 确认密码
	 * @return
	 */
	int updatePassword(String password,
			           String newPassword,
			           String cfgPassword);
	
	 /**
	  * 基于用户id查询用户以及用户关联的数据
	  * @param id
	  * @return
	  */
	 Map<String,Object> findObjectById(Integer id);
	
	 
	 /**
	  * 更新用户以及用户对应的角色信息
	  * @param entity
	  * @param roleIds
	  * @return
	  */
	 int updateObject(SysUser entity,Integer[]roleIds);
	 
	 /**
	  * 保存用户以及用户对应的角色信息
	  * @param entity
	  * @param roleIds
	  * @return
	  */
	 int saveObject(SysUser entity,Integer[]roleIds);
	
	 /**
	  * 禁用或启用用户状态
	  * @param id
	  * @param valid
	  * @param modifiedUser
	  * @return
	  */
	 int validById(Integer id,
			 Integer valid,String modifiedUser);
	 
	
}
