package com.cy.pj.sys.entity;
/**
 * pojo:普通的java对象
 * 1)VO(值对象):封装数据(例如JsonResult,PageObject,CheckBox,Node)
 * 2)PO(持久化对象):持久化对象(属性名需要与表中字段有映射关系)
 * 可通过此对象封装页面数据,
 * 并将数据最后持久化到数据库
 * @author Administrator
 */
public class SysUser extends BaseEntity{
	private static final long serialVersionUID = -4262934684900042240L;
	private Integer id;
	private String username;
	private String password;
	private String salt;//盐值
	private String email;
	private String mobile;
	private Integer valid=1;
    private Integer deptId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
    
    
}
