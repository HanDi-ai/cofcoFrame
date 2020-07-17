package com.cofco.sys.dao.user;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.role.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 查询用户角色关联表
 * 获取选择用户角色
 *
 * @author liming
 * @date 2018-04-25 09:41:38
 */
@Repository
public interface UserGrantRolesDao extends BaseDao<RoleEntity> {
    /**
     * 查询未拥有角色
     * @param map
     * @return RoleEntity
     */
    List<RoleEntity> notinList(Map<String, Object> map);

    int notinTotal(Map<String, Object> map);

    /**
     * 删除用户角色中间表 解除关系用
     * @param map
     * @return
     */
    int relieve(Map<String, Object> map);
	
}
