package com.cy.pj.sys.service.realm;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
	
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysMenuDao sysMenuDao;
	
	/**
	 * 设置凭证匹配器,告诉认证管理器使用什么
	 * 加密算法对用户输入的密码进行加密操作
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//构建凭证匹配对象
		HashedCredentialsMatcher cMatcher=
		new HashedCredentialsMatcher();
		//设置加密算法
		cMatcher.setHashAlgorithmName("MD5");
		//设置加密次数
		cMatcher.setHashIterations(1);
		super.setCredentialsMatcher(cMatcher);
	}
	/**
	 * 完成认证信息的获取以及封装
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.获取用户输入的信息
		UsernamePasswordToken upToken=
		(UsernamePasswordToken)token;
		//2.基于用户名查找用户信息,并进行判定
		SysUser user=
		sysUserDao.findUserByUserName(upToken.getUsername());
		//2.1 判定用户是否存在
		if(user==null)
		throw new UnknownAccountException();
		//2.2 判定账户是否已被禁用
		if(user.getValid()==0)
		throw new LockedAccountException();
		//3.对用户信息进行封装,并返回
		//ByteSource对象是对byte 数组以及编码方式的封装
		ByteSource credentialsSalt=
		ByteSource.Util.bytes(user.getSalt());
		
		SimpleAuthenticationInfo info=
		new SimpleAuthenticationInfo(
				user,//principal 身份 user
				user.getPassword(),//hashedCredentials 已加密的凭证的信息
				credentialsSalt,//credentialsSalt 盐值
				getName());//realmName
		return info;//返回给调用者(SecurityManager)
	}
	/**
	 * 完成授权信息的获取以及封装
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		System.out.println("获取授权信息:GetAuthorizationInfo");
		//1.获取登陆用户信息
		SysUser user=
		(SysUser)arg0.getPrimaryPrincipal();
		//2.基于用户id获取用户对应的角色
		if(user==null)
		throw new AuthorizationException();
		List<Integer> roleIds=
		sysUserRoleDao.findRoleIdsByUserId(user.getId());
		//3.基于角色id获取对应的菜单信息
		if(roleIds==null||roleIds.size()==0)
		throw new AuthorizationException();
		Integer[] array= {};
		List<Integer> menuIds=
		sysRoleMenuDao.findMenuIdsByRoleIds(
		roleIds.toArray(array));
		if(menuIds==null||menuIds.size()==0)
		throw new AuthorizationException();
		//4.基于菜单id获取对应的权限标识(permisssion)
		List<String> permissions=
		sysMenuDao.findPermissions(
		menuIds.toArray(array));
		if(permissions==null||permissions.size()==0)
		throw new AuthorizationException();
		//5.对权限标识permission进行封装并返回
		Set<String> setPermissions=new HashSet<>();
		for(String p:permissions) {
			if(!StringUtils.isEmpty(p)) {
				setPermissions.add(p);
			}
		}
		SimpleAuthorizationInfo info=
		new SimpleAuthorizationInfo();
		info.setStringPermissions(setPermissions);
		return info;//返回给SecurityManager
	}

}








