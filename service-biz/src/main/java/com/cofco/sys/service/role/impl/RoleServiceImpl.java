package com.cofco.sys.service.role.impl;

import com.cofco.base.common.Constant;
import com.cofco.base.page.Page;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.Utils;
import com.cofco.dto.UserWindowDto;
import com.cofco.sys.dao.role.RoleDao;
import com.cofco.sys.dao.role.RoleMenuDao;
import com.cofco.sys.dao.user.UserRoleDao;
import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.RoleService;
import com.cofco.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private RoleMenuDao roleMenuDao;

	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public RoleEntity queryObject(String id){
		return roleDao.queryObject(id);
	}
	
	@Override
	public List<RoleEntity> queryList(Map<String, Object> map){
		map.put("userid",UserUtils.getCurrentUserId());
		return roleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		map.put("userid",UserUtils.getCurrentUserId());
		return roleDao.queryTotal(map);
	}


	@Override
	public int checkrole(RoleEntity role){
		return roleDao.checkrole(role);
	}
	@Override
    @Transactional
	public void save(RoleEntity role) throws Exception{
        UserEntity currentUser = UserUtils.getCurrentUser();
		role.setBapid(role.getOrganId());
		role.setBaid(currentUser.getBaid());
		role.setCreateId(currentUser.getId());
        role.setId(Utils.uuid());
        role.setCreateTime(new Date());
		roleDao.save(role);
		saveRtable(role);
	}

	/**
	 * 保存角色与菜单，角色与组织关系表
	 * @param role
	 */
	public void saveRtable(RoleEntity role){
		if(role.getMenuIdList() != null && role.getMenuIdList().size()>0){
			Map map = new HashMap();
			map.put("roleId",role.getId());
			map.put("menuIdList",role.getMenuIdList());
			roleMenuDao.save(map);

		}
        if(role.getAppmenuIdList() != null && role.getAppmenuIdList().size()>0){
            Map appmap = new HashMap();
            appmap.put("roleId",role.getId());
            appmap.put("menuIdList",role.getAppmenuIdList());
            roleMenuDao.save(appmap);

        }
		if(role.getOrganId() != null){
				roleDao.batchSaveRoleOrgan(role);
			}
	}

	@Transactional
	@Override
	public void update(RoleEntity role){
		role.setUpdateTime(new Date());
		roleDao.update(role);
		//先删除所有角色菜单关系，再批量保存
		roleMenuDao.delete(role.getId());
		//先删除所有角色组织关系，再批量保存
		roleDao.delRoleOrganByRoleId(role.getId());
		saveRtable(role);
	}
	
	@Override
	public void delete(String id){
		roleDao.delete(id);
	}

	@Transactional
	@Override
	public void deleteBatch(String[] ids) throws Exception{
		roleDao.deleteBatch(ids);
        roleMenuDao.deleteBatch(ids);
		userRoleDao.deleteBatchByRoleId(ids);
	}

	@Override
	public List<RoleEntity> queryByUserId(String userId, String status) {
		return roleDao.queryByUserId(userId,status);
	}

	@Override
	public RoleEntity queryOrganRoleByRoleId(String roleId) {
		return roleDao.queryOrganRoleByRoleId(roleId);
	}

	@Override
	public Page<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto,int pageNum) {
		PageHelper.startPage(pageNum, Constant.pageSize);
		roleDao.queryPageByDto(userWindowDto);
		return PageHelper.endPage();
	}

	@Override
	public int updateBatchStatus(String[] ids, String status) {
		Map<String,Object> params = new HashMap<>();
		params.put("ids",ids);
		params.put("status",status);
		return roleDao.updateBatchStatus(params);
	}
}
