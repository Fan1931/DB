package com.cy.pj.sys.entity;
import java.io.Serializable;
import java.util.Date;
/**
 * POJO:普通的java对象
 * 1)PO(持久化对象):实现与数据库表记录之间的映射
 * 2)VO(值对象):封装数据
 * 3)...
 * @author Administrator
 */
public class SysLog implements Serializable{
	private static final long serialVersionUID = -1479538995581206701L;
	private Integer id;
	private String username;
	private String operation;
	/**执行的方法*/
	private String method;
	/**执行方法时传入的参数*/
	private String params;
	private Long  time;
	private String ip;
	private Date createdTime;
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
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
}
