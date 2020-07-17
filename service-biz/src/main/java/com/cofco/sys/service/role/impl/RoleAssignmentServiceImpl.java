package com.cofco.sys.service.role.impl;

import com.cofco.sys.dao.role.RoleAssignmentDao;
import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.RoleAssignmentService;
import com.cofco.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 角色分配用户
 *
 * @author caofa
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 10:07:59
 */
@Service("roleAssignment")
@Transactional
public class RoleAssignmentServiceImpl implements RoleAssignmentService {

	@Autowired
	private RoleAssignmentDao roleAssignmentDao;

    @Override
    public List<UserEntity> queryListUserYes(Map<String, Object> map){
        return roleAssignmentDao.queryListUserYes(map);
    }

    @Override
    public List<UserEntity> queryListUserNo(Map<String, Object> map){
        return roleAssignmentDao.queryListUserNo(map);
    }

	@Override
	public int userTotal(Map<String, Object> map){
		return roleAssignmentDao.userTotal(map);
	}

	@Override
	public int UserYesTotal(Map<String, Object> map){
		return roleAssignmentDao.UserYesTotal(map);
	}

    @Override
    public int deleteYes(RoleEntity role){
		Map<String, Object> params = new HashMap<>();
		params.put("id", role.getId());
		params.put("userList", role.getUserList());
        return roleAssignmentDao.deleteYes(params);
    }

	@Override
	public int saveNo(RoleEntity role){
		Map<String, Object> params = new HashMap<>();
		params.put("id", role.getId());
		params.put("userList", role.getUserList());
		return roleAssignmentDao.saveNo(params);
	}

	@Override
	public List<RoleEntity> queryList(Map<String, Object> map){
		map.put("userid",UserUtils.getCurrentUserId());
		return roleAssignmentDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map){
		map.put("userid",UserUtils.getCurrentUserId());
    	return roleAssignmentDao.queryTotal(map);
	}
}
