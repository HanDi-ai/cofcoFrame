package com.cofco.sys.service;

import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 查询用户角色关联表
 * 获取选择用户角色
 *
 * @author liming
 * @date 2018-04-25 09:41:38
 */
public interface UserGrantRolesService {


	List<RoleEntity> queryList(Map<String, Object> map);


	int queryTotal(Map<String, Object> map);

	List<RoleEntity> notinList(Map<String, Object> map);

	int notinTotal(Map<String, Object> map);

	void save(UserEntity user);

	void relieve(UserEntity user);



}
