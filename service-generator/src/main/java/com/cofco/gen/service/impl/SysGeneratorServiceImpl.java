package com.cofco.gen.service.impl;

import com.cofco.gen.dao.SysGeneratorDao;
import com.cofco.gen.entity.TableEntity;
import com.cofco.gen.service.SysGeneratorService;
import com.cofco.gen.utils.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Service
public class SysGeneratorServiceImpl implements SysGeneratorService {
	@Autowired
	private SysGeneratorDao sysGeneratorDao;

	@Override
	public List<TableEntity> queryList(Map<String, Object> map) {
		return sysGeneratorDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysGeneratorDao.queryTotal(map);
	}

	@Override
	public Map<String, String> queryTable(String tableName) {
		return sysGeneratorDao.queryTable(tableName);
	}

	@Override
	public List<Map<String, String>> queryColumns(String tableName) {
		return sysGeneratorDao.queryColumns(tableName);
	}

	@Override
	public String queryP(String tableName) {
		return sysGeneratorDao.queryP(tableName);
	}

	@Override
	public List<TableEntity> view(Map<String, Object> map) {
		return sysGeneratorDao.view(map);
	}

	@Override
	public int viewTo(Map<String, Object> map) {
		return sysGeneratorDao.viewTo(map);
	}

	@Override
	public List<Map<String, String>> queryColumnsView(String tableName) {
		return sysGeneratorDao.queryColumnsView(tableName);
	}

	@Override
	public byte[] generatorCode(String[] tableNames,int genType,String path) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		
		for(String tableName : tableNames){
			//查询表信息
			Map<String , String> table = queryTable(tableName);
			if(table == null || table.size() == 0){
				table = new HashMap<String , String>();
				table.put("TABLE_NAME",tableName);
				table.put("COMMENTS","");
			}
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			HashSet set = new  HashSet(columns);
			columns.clear();
			columns.addAll(set);

			//oracle中主键查询以及赋值
			String tableP = queryP(tableName);
			if(null!=tableP){
				for(Map<String, String> column : columns){
					if(tableP.equals(column.get("COLUMN_NAME"))){
						column.put("columnKey","PRI");
					}
				}
			}


			//生成代码
			GenUtils.generatorCode(table, columns, zip, genType , path);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

}
