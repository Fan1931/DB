package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

public interface SysRoleDao {
	
	List<CheckBox> findObjects();
	
	/**
	 * 基于角色id查找角色相关信息
	 * @param id
	 * @return
	 */
	SysRoleMenuVo findObjectById(Integer id);
	
	/**
	 * 将角色自身信息更新到数据库
	 * @param entity
	 * @return
	 */
	int updateObject(SysRole entity);
	
	/**
	 * 将角色自身信息写入到数据库
	 * @param entity
	 * @return
	 */
	int insertObject(SysRole entity);
	
	 /**
	  * 基于角色id删除角色自身信息
	  * @param id
	  * @return
	  */
	 int deleteObject(Integer id);

	 /**
	  * 查询总记录数
	  * @param name
	  * @return
	  */
	 int getRowCount(@Param("name")String name);
	 /**
	  * 查询当前页要呈现的记录
	  * @param name
	  * @param startIndex
	  * @param pageSize
	  * @return
	  */
	 List<SysRole> findPageObjects(
			 @Param("name")String name,
			 @Param("startIndex")Integer startIndex,
			 @Param("pageSize")Integer pageSize);
}
