package com.cofco.sys.service;

import com.cofco.base.page.Page;
import com.cofco.dto.UserWindowDto;
import com.cofco.sys.entity.organ.OrganEntity;

import java.util.List;
import java.util.Map;

/**
 * 组织表
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-07-14 13:42:42
 */
public interface OrganService {
	
	OrganEntity queryObject(String id);
	
	List<OrganEntity> queryList(Map<String, Object> map);

	List<OrganEntity> queryListByBean(OrganEntity organEntity);

	List<OrganEntity> queryListByBeanDown(OrganEntity organEntity);
	
	int queryTotal(Map<String, Object> map);
	
	String save(OrganEntity organ);

	String selectParentId(String parentId);

	int update(OrganEntity organ);

	int delete(String id);

	int deleteBatch(String[] ids);

	/**
	 * 根据code查询可能的组织
	 * @param code
	 * @return
	 */
	List<OrganEntity> queryListByCode(String code);

	/**
	 * 更新机构状态
	 * @param ids
	 * @param type
	 * @return
	 */
	int updateIsdel(String ids,String type);

	/**
	 * 分页查询组织审批选择范围
	 * @return
	 */
	Page<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto, int pageNum);


	int queryRepetition(OrganEntity organEntity);

	/**
	 * 通过用户ID 查询简称、code、id
	 */
	Map<String,Object> adoptUserIdSelectData(String userId);

    List<OrganEntity> getOrgans();
}
