package com.cy.pj.sys.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.PageUtil;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;


@Transactional(rollbackFor=Throwable.class,
isolation=Isolation.READ_COMMITTED,
timeout=30,
propagation=Propagation.REQUIRED)
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
		//long t1=System.currentTimeMillis();
		//1.判定新密码与密码确认是否相同
		if(StringUtils.isEmpty(newPassword))
		throw new IllegalArgumentException("新密码不能为空");
		if(StringUtils.isEmpty(cfgPassword))
		throw new IllegalArgumentException("确认密码不能为空");
		if(!newPassword.equals(cfgPassword))
		throw new IllegalArgumentException("两次输入的密码不相等");
		//2.判定原密码是否正确
		if(StringUtils.isEmpty(password))
		throw new IllegalArgumentException("原密码不能为空");
		//获取登陆用户
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh=new SimpleHash("MD5",
		password, user.getSalt(), 1);
		if(!user.getPassword().equals(sh.toHex()))
		throw new IllegalArgumentException("原密码不正确");
		//3.对新密码进行加密
		String salt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5",newPassword,salt, 1);
		//4.将新密码加密以后的结果更新到数据库
		int rows=sysUserDao.updatePassword(sh.toHex(), salt,user.getId());
		if(rows==0)
		throw new ServiceException("修改失败");
		//long t2=System.currentTimeMillis();
		//System.out.println("t2-t1="+(t2-t1));
		return rows;
	}
	
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		//1.参数校验
		if(id==null||id<1)
		throw new IllegalArgumentException("参数值无效");
		//2.基于用户id获取用户以及对应的部门信息
		SysUserDeptVo user=sysUserDao.findObjectById(id);
		if(user==null)
		throw new ServiceException("记录可能已经不存在");
		//3.基于用户id获取用户对应的角色信息
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
		//4.对如上两次查询结果进行封装
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		if(entity==null)
		throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
		throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
		throw new IllegalArgumentException("必须为用户分配角色");
		//2.更新用户自身信息
		int rows=sysUserDao.updateObject(entity);
		if(rows==0)
	    throw new ServiceException("记录可能已经不存在");
		//4.保存用户与角色关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//5.返回结果
		return rows;
	}
	@Transactional
	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new IllegalArgumentException("密码不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new IllegalArgumentException("必须为用户分配角色");
		//...
		//2.对密码进行加密
		String salt=UUID.randomUUID().toString();
		SimpleHash sh=new SimpleHash(
				"MD5",//algorithmName加密算法
				entity.getPassword(),//source 被加密的对象
				salt, //salt 盐值
				1);//hashIterations 加密
		entity.setSalt(salt);
		entity.setPassword(sh.toHex());
		//3.保存用户自身信息
		int rows=sysUserDao.insertObject(entity);
		//4.保存用户与角色关系数据
		int urows=sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		if(urows>0)
		throw new ServiceException("关系数据保存失败");
		//5.返回结果
		return rows;
	}
	/**
	 * 访问此方法时,要求必须授权才能访问
	 * 1)从登陆用上身上获取它具备的权限
	 * 2)判定用户权限中是否包含@RequiresPermissions
	 * 注解中包含的权限,假如包含,则授权访问,不包含则
	 * 给出提示信息.
	 */
	@RequiresPermissions("sys:user:valid")
	@RequiredLog("禁用启用")
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		//.....
		if(id==null||id<1)
	    throw new IllegalArgumentException("id值无效");
		if(valid!=1&&valid!=0)
		throw new IllegalArgumentException("状态值无效");
		int rows=sysUserDao.validById(id, valid, modifiedUser);
		if(rows==0)
		throw new ServiceException("记录可能已经不存在");
		return rows;
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)//并不是没有事务,相当于是使用了中乐观锁机制
	@RequiredLog
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		//1.判定参数有效性
		if(pageCurrent==null||pageCurrent<1)
		throw new IllegalArgumentException("当前页码值不正确");
		//2.查询总记录数
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
		throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录信息
		int pageSize=3;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptVo> records=sysUserDao.findPageObjects(username,
				startIndex, 
				pageSize);
		//4.封装数据并返回
		return PageUtil.newInstance(pageCurrent, rowCount, pageSize, records);
	}

}
