package com.cy.pj.common.vo;

import java.io.Serializable;
/**VO:
 * 此对象可以封装树节点信息
 * @author Administrator
 */
public class Node implements Serializable{//Node.class
	private static final long serialVersionUID = -4794314574141335353L;
	private Integer id;
	private String name;
	private Integer parentId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	
}
