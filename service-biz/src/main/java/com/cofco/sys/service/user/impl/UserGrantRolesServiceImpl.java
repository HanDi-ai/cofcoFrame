package com.cofco.sys.service.user.impl;


import com.cofco.sys.dao.user.UserGrantRolesDao;
import com.cofco.sys.dao.user.UserRoleDao;
import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.UserGrantRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;


@Service("userGrantRolesService")
@Transactional
public class UserGrantRolesServiceImpl implements UserGrantRolesService {
	@Autowired
	private UserGrantRolesDao UserGrantRolesDao;
    @Autowired
    private UserRoleDao userRoleDao;
    /**
     * 已有角色查询
     * @param map
     */
    @Override
    public List<RoleEntity> queryList(Map<String, Object> map) {
        return UserGrantRolesDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return UserGrantRolesDao.queryTotal(map);
    }

    /**
     * 未有角色查询
     * @param map
     */
    @Override
    public List<RoleEntity> notinList(Map<String, Object> map) {
        return UserGrantRolesDao.notinList(map);
    }

    @Override
    public int notinTotal(Map<String, Object> map) {
        return UserGrantRolesDao.notinTotal(map);
    }

    /**
     * 保存用户与角色关系
     * @param user
     */
    @Override
    @Transactional
    public void save(UserEntity user){
        //保存用户与角色关系
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("roleIdList", user.getRoleIdList());
        userRoleDao.save(map);
    }
    /**
     * 解除用户与角色关系
     * @param user
     */
    @Override
    public void relieve(UserEntity user) {
        //解除用户与角色关系
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("roleIdList", user.getRoleIdList());
        UserGrantRolesDao.relieve(map);

    }
}
