package com.cofco.sys.service.oss.impl;

import com.cofco.sys.dao.oss.SysOssDao;
import com.cofco.sys.entity.oss.SysOssEntity;
import com.cofco.sys.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("sysOssService")
@Transactional
public class SysOssServiceImpl implements SysOssService {
	@Autowired
	private SysOssDao sysOssDao;
	
	@Override
	public SysOssEntity queryObject(Long id){
		return sysOssDao.queryObject(id);
	}
	
	@Override
	public List<SysOssEntity> queryList(Map<String, Object> map){
		return sysOssDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysOssDao.queryTotal(map);
	}
	
	@Override
	public void save(SysOssEntity sysOss){
		sysOssDao.save(sysOss);
	}
	
	@Override
	public void update(SysOssEntity sysOss){
		sysOssDao.update(sysOss);
	}
	
	@Override
	public void delete(Long id){
		sysOssDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		sysOssDao.deleteBatch(ids);
	}
	
}
