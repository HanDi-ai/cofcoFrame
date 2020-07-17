package com.cofco.sys.dao.organ;

import com.cofco.dto.UserWindowDto;
import com.cofco.sys.entity.organ.OrganEntity;
import com.cofco.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 组织表
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-07-14 13:42:42
 */
@Repository
public interface OrganDao extends BaseDao<OrganEntity> {
    /**
     * 根据实体条件查询
     * @return
     */
    List<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto);

    List<OrganEntity> queryListByBeanDown(OrganEntity organEntity);

    String selectParentId(String parentId);

    int queryRepetition(OrganEntity organEntity);
    int queryDesertEagleBase(OrganEntity organEntity);
    List<OrganEntity> ListByBeanadmin(OrganEntity OrganEntity);
    Map<String,Object> adoptUserIdSelectData(String userid);

    int organOfUser(String organId);
    int oragnChildren(String organId);
    int organOfRole(String organId);

    List<OrganEntity> getOrgans();
}
