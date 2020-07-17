package com.cofco.sys.controller.log;

import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.sys.entity.log.SysLogEntity;
import com.cofco.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 类SysLogController的功能描述:
 * 系统日志
 * @auther cofco
 * @date 2017-08-25 16:20:16
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	//@RequiresPermissions("sys:log:list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

		PageHelper.startPage(query.getPage(),query.getLimit());
		List<SysLogEntity> sysLogList = sysLogService.queryList(query);
		PageHelper.endPage();

		int total = sysLogService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysLogList, total, query.getLimit(), query.getPage());

		return Result.ok().put("page", pageUtil);
	}
	
}
