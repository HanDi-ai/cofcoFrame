package com.cofco.sys.controller.organ;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.organ.LendEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.LendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 查询借调信息
 *
 * @author liming
 * @date 2018-04-25 09:41:38
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@RestController
@RequestMapping("sys/lend")
public class LendController extends BaseController {
	@Autowired
	private LendService LendService;

	/**
	 *原机构人员
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("sys:lend:list")
	@SysLog("查询原机构人员")
	public Result list(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<UserEntity> userList = LendService.queryList(query);
		PageHelper.endPage();
		int total = LendService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);

	}

	/**
	 * 借调机构人员
	 */
	@RequestMapping("/newlist")
	//@RequiresPermissions("sys:lend:list")
	@SysLog("查询借调机构人员")
	public Result notinlist(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<UserEntity> userList = LendService.newList(query);
		PageHelper.endPage();
		int total = LendService.newTotal(query);
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}
	/**
	 * 借调用户到选择机构
	 */
	@RequestMapping("/toloan")
	//@RequiresPermissions("sys:lend:toloan")
	@SysLog("借调用户到选择机构")
	public Result distribution(@RequestBody LendEntity lend){
		Result result = Result.ok();
		try {
			LendService.save(lend);
		} catch (Exception e) {
			result=Result.error();
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 取消借调
	 */
	@RequestMapping("/relieve")
	//@RequiresPermissions("sys:lend:relieve")
	@SysLog("取消借调")
	public Result relieve(@RequestBody LendEntity lend){
		Result result = Result.ok();
		try {
			LendService.relieve(lend);
		} catch (Exception e) {
			result=Result.error();
			e.printStackTrace();
		}
		return result;
	}


}
