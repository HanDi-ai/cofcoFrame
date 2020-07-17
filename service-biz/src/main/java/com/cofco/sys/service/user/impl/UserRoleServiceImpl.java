package com.cofco.sys.service.user.impl;

import com.cofco.sys.dao.user.UserRoleDao;
import com.cofco.sys.entity.user.UserRoleEntity;
import com.cofco.sys.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userRoleService")
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public UserRoleEntity queryObject(String userId){
		return userRoleDao.queryObject(userId);
	}
	
	@Override
	public List<UserRoleEntity> queryList(Map<String, Object> map){
		return userRoleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userRoleDao.queryTotal(map);
	}
	
	@Override
	public void save(UserRoleEntity userRole){
		userRoleDao.save(userRole);
	}
	
	@Override
	public void update(UserRoleEntity userRole){
		userRoleDao.update(userRole);
	}
	
	@Override
	public void delete(String userId){
		userRoleDao.delete(userId);
	}
	
	@Override
	public void deleteBatch(String[] userIds){
		userRoleDao.deleteBatch(userIds);
	}

	@Override
	public void saveOrUpdate(String userId, List<String> roleIdList) {
		//先删除用户与角色关系
		userRoleDao.delete(userId);
		if(roleIdList.size() == 0){
			return ;
		}
		//保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("roleIdList", roleIdList);
		userRoleDao.save(map);
	}

	@Override
	public List<String> queryRoleIdList(String userId) {
		return userRoleDao.queryRoleIdList(userId);
	}


}
