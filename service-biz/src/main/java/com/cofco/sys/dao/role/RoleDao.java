package com.cofco.sys.dao.role;

import com.cofco.base.dao.BaseDao;
import com.cofco.dto.UserWindowDto;
import com.cofco.sys.entity.role.RoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色表
 * 
 * @author chenshun
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 10:07:59
 */
@Repository
public interface RoleDao extends BaseDao<RoleEntity> {

    /**
     * 根据用户查询角色集合
     * @param userId
     * @return
     */
    List<RoleEntity> queryByUserId(String userId, String status);

    /**
     * 批量保存组织机构与角色关系表
     * @param role
     * @return
     */
    int batchSaveRoleOrgan(RoleEntity role);

    /**
     * 根据角色id查询可用的组织机构
     * @param map 参数1:roleId 参数2:isDel
     * @return
     */
    RoleEntity queryOrganRoleByRoleId(String roleId);

    /**
     * 根据角色id删除角色和组织关系表
     * @param roleId
     * @return
     */
    int delRoleOrganByRoleId(String roleId);

    /**
     * 查询角色审批选择范围
     * @return
     */
    List<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto);

    /**
     * 批量更新角色状态
     * @param params key:ids 角色ids
     * @return
     */
    int updateBatchStatus(Map<String,Object> params);

    /**
     * 判断是否重复角色
     * @param role
     * @return
     */
    int checkrole(RoleEntity role);

	
}
