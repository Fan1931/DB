package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 此dao基于sys_role_menus关系表进行操作
 * @author Administrator
 */
public interface SysRoleMenuDao {
	
	/**
	 * 基于多个角色id获取对应的多个菜单id?
	 * 为什么时候多个角色呢?因为一个用户可以有多个角色
	 * @param roleIds
	 * @return
	 */
	List<Integer> findMenuIdsByRoleIds(
			@Param("roleIds")Integer[] roleIds);
	
	/**
	 * 保存角色和菜单的关系数据
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	int insertObjects(@Param("roleId")Integer roleId,
			          @Param("menuIds")Integer[] menuIds);
	/**
	 * 基于角色id删除角色和菜单关系数据
	 * @param id
	 * @return
	 */
	int deleteObjectsByRoleId(Integer roleId);
     /**
      * 基于菜单id删除角色和菜单的关系数据.
      * @param menuId
      * @return
      */
	int deleteObjectsByMenuId(Integer menuId);
}
