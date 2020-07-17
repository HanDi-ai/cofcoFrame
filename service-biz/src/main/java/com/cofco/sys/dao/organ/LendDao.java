package com.cofco.sys.dao.organ;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.user.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 查询借调关联表
 * @author liming
 * @date 2018-04-25 09:41:38
 */
@Repository
public interface LendDao extends BaseDao<UserEntity> {
    /**
     * 查询借调人员
     * @param map
     * @return UserEntity
     */
    List<UserEntity> newList(Map<String, Object> map);

    int newTotal(Map<String, Object> map);

    /**
     * 解除借调关系
     * @param map
     * @return
     */
    int relieve(Map<String, Object> map);
	
}
