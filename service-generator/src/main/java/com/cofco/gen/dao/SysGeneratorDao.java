package com.cofco.gen.dao;

import com.cofco.gen.entity.TableEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 类SysGeneratorDao的功能描述:
 *  代码生成器
 * @auther cofco
 * @date 2017-08-25 16:19:43
 */
@Repository
public interface SysGeneratorDao {
	
	List<TableEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	Map<String, String> queryTable(String tableName);
	
	List<Map<String, String>> queryColumns(String tableName);

	String queryP(String tableName);

	List<TableEntity> view(Map<String, Object> map);

	int viewTo(Map<String, Object> map);

	List<Map<String, String>> queryColumnsView(String tableName);
}
