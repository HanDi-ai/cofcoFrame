package com.cofco.sys.entity.user;

import com.cofco.base.entity.BaseEntity;
import com.cofco.sys.entity.role.RoleEntity;

import java.io.Serializable;


/**
 * 系统用户表
 * 
 * @author chenshun
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 09:41:38
 */
public class UserGrantRolesEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//用户角色授予  中的用户属性位置
	private UserEntity userEntity;
	//用户角色授予  中的角色属性位置
	private RoleEntity roleEntity;
	//用户id
	private String userId;
	//角色id
	private String roleId;

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}
