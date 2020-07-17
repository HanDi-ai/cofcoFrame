package com.cofco.sys.service;

import com.cofco.sys.entity.organ.LendEntity;
import com.cofco.sys.entity.user.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 查询用户角色关联表
 * 获取选择用户角色
 *
 * @author liming
 * @date 2018-04-25 09:41:38
 */
public interface LendService {


	List<UserEntity> queryList(Map<String, Object> map);


	int queryTotal(Map<String, Object> map);

	List<UserEntity> newList(Map<String, Object> map);

	int newTotal(Map<String, Object> map);

	void save(LendEntity user);

	void relieve(LendEntity lend);



}
