package com.cofco.sys.dao.config;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.config.SysConfigEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 类SysConfigDao的功能描述:
 * 系统配置信息
 * @auther cofco
 * @date 2017-08-25 16:14:20
 */
public interface SysConfigDao extends BaseDao<SysConfigEntity> {
	
	/**
	 * 根据key，查询value
	 */
	String queryByKey(String paramKey);
	
	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);
	
}
