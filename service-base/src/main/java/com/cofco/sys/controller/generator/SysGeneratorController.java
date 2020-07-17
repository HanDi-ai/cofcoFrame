package com.cofco.sys.controller.generator;

import com.alibaba.fastjson.JSON;
import com.cofco.base.common.Constant;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.base.xss.XssHttpServletRequestWrapper;
import com.cofco.gen.entity.TableEntity;
import com.cofco.gen.service.SysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 类SysGeneratorController的功能描述:
 * 代码生成器
 * @auther cofco
 * @date 2017-08-25 16:20:43
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 表列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	//@RequiresPermissions("sys:generator:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TableEntity> list = sysGeneratorService.queryList(query);
		PageHelper.endPage();
		int total = sysGeneratorService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 视图列表
	 */
	@ResponseBody
	@RequestMapping("/view")
	//@RequiresPermissions("sys:generator:list")
	public Result view(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<TableEntity> list = sysGeneratorService.view(query);
		PageHelper.endPage();
		int total = sysGeneratorService.viewTo(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}
	
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	//@RequiresPermissions("sys:generator:code")
	public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String[] tableNames = new String[]{};
		//获取表名，不进行xss过滤
		HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
		String tables = orgRequest.getParameter("tables");
		tableNames = JSON.parseArray(tables).toArray(tableNames);
		//取得根目录路径
		String rootPath=request.getSession().getServletContext().getRealPath("/");
		String rPath=StringUtils.substringBefore(rootPath,"target");
		String path=rPath.substring(0,rPath.length()-14);

		byte[] data = sysGeneratorService.generatorCode(tableNames, Constant.genType.local.getValue() , path);
		
		response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"cofcoFrame.zip\"");
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
  
        IOUtils.write(data, response.getOutputStream());
	}
}
