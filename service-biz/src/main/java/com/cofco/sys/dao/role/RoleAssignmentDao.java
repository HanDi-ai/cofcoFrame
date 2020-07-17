package com.cofco.sys.dao.role;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色f分配用户
 * 
 * @author chenshun
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 10:07:59
 */
@Repository
public interface RoleAssignmentDao extends BaseDao<RoleEntity> {

    /**
     * 角色下的有权限用户列表
     * @param map
     * @return
     */
    List<UserEntity> queryListUserYes(Map<String, Object> map);

    /**
     * 角色下的无权限用户列表
     * @param map
     * @return
     */
    List<UserEntity> queryListUserNo(Map<String, Object> map);

   int  userTotal (Map<String, Object> map);

   int  UserYesTotal (Map<String, Object> map);

    /**
     * 修改用户角色权限（去除）
     * @param params
     * @return
     */
    int deleteYes(Map<String,Object> params);

    /**
     * 修改用户角色权限（增加）
     * @param params
     * @return
     */
    int saveNo(Map<String, Object> params);

}
