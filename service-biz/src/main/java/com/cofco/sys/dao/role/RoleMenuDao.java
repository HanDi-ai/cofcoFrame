package com.cofco.sys.dao.role;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 权限角色表
 * 
 * @author liming
 * @date 2018-05-11 10:07:59
 */
@Repository
public interface RoleMenuDao {
    int delete(Map<String, Object> map);

    int deleteBatch(Object[] ids);

    int delete(Object id);

    void save(Map<String, Object> map);

    List<String> queryListByRoleId(String roleId);
    List<String> queryappListByRoleId(String roleId);

}
