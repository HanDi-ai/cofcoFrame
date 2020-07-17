package com.cofco.sys.service;

import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色分配用户
 * 
 * @author caofa
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 10:07:59
 */
public interface RoleAssignmentService {

	List<UserEntity> queryListUserYes(Map<String, Object> map);

	List<UserEntity> queryListUserNo(Map<String, Object> map);

	int userTotal(Map<String, Object> map);

	int UserYesTotal(Map<String, Object> map);

	int deleteYes(RoleEntity role);

	int saveNo(RoleEntity role);

	List<RoleEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

}
