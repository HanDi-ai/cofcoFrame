package com.cofco.sys.service;

import java.util.List;
import java.util.Map;

/**
 * 权限角色表
 * 
 * @author liming
 * @date 2018-05-11 10:07:59
 */
public interface RoleMenuService {

	
	List<String> queryListByRoleId(String id);

	List<String> queryappListByRoleId(String id);

	void save(Map<String, Object> map);


	void delete(String roleId);
	
	void deleteBatch(String[] roleIds);
}
