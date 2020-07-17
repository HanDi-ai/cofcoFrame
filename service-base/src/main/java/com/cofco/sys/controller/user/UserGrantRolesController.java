package com.cofco.sys.controller.user;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.UserGrantRolesService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 查询用户角色关联表
 * 获取选择用户角色
 *
 * @author liming
 * @date 2018-04-25 09:41:38
 */
@RestController
@RequestMapping("sys/userGrantRoles")
public class UserGrantRolesController extends BaseController {
	@Autowired
	private UserGrantRolesService userGrantRolesService;

	/**
	 *获得该用户已分配角色列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("sys:userGrantRoles:list")
	@SysLog("根据用户查看角色列表")
	public Result list(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<RoleEntity> roleList = userGrantRolesService.queryList(query);
		PageHelper.endPage();
		int total = userGrantRolesService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(roleList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);

	}

	/**
	 * 获得该用户未分配角色列表
	 */
	@RequestMapping("/notinlist")
	//@RequiresPermissions("sys:userGrantRoles:list")
	@SysLog("根据用户查看角色列表")
	public Result notinlist(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<RoleEntity> roleList = userGrantRolesService.notinList(query);
		PageHelper.endPage();
		int total = userGrantRolesService.notinTotal(query);
		PageUtils pageUtil = new PageUtils(roleList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}
	/**
	 * 批量分配角色到用户
	 */
	@RequestMapping("/distribution")
	//@RequiresPermissions("sys:userGrantRoles:distribution")
	@SysLog("批量分配角色到用户")
	public Result distribution(@RequestBody UserEntity user){
		Result result = Result.ok();
		try {
			userGrantRolesService.save(user);
		} catch (Exception e) {
			result=Result.error();
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 批量解除该用户角色
	 */
	@RequestMapping("/relieve")
	//@RequiresPermissions("sys:userGrantRoles:relieve")
	@SysLog("批量分配角色到用户")
	public Result relieve(@RequestBody UserEntity user){
		Result result = Result.ok();
		try {
			userGrantRolesService.relieve(user);
		} catch (Exception e) {
			result=Result.error();
			e.printStackTrace();
		}
		return result;
	}


}
