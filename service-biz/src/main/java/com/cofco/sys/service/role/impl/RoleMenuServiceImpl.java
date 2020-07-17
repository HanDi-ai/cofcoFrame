package com.cofco.sys.service.role.impl;

import com.cofco.sys.dao.role.RoleMenuDao;
import com.cofco.sys.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("roleMenuService")
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService {
	@Autowired
	private RoleMenuDao roleMenuDao;
	
	@Override
	public List<String> queryListByRoleId(String id){
		return roleMenuDao.queryListByRoleId(id);
	}

	@Override
	public List<String> queryappListByRoleId(String id){
		return roleMenuDao.queryappListByRoleId(id);
	}


	@Override
	public void save(Map map){
		roleMenuDao.save(map);
	}


	@Override
	public void delete(String roleId){
		roleMenuDao.delete(roleId);
	}
	
	@Override
	public void deleteBatch(String[] roleIds){
		roleMenuDao.deleteBatch(roleIds);
	}
	
}
