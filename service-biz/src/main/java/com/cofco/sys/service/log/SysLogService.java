package com.cofco.sys.service;


import com.cofco.sys.entity.log.SysLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 类SysLogService的功能描述:
 * 系统日志
 * @auther cofco
 * @date 2017-08-25 16:14:27
 */
public interface SysLogService {
	
	SysLogEntity queryObject(Long id);
	
	List<SysLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysLogEntity sysLog);
	
	void update(SysLogEntity sysLog);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
