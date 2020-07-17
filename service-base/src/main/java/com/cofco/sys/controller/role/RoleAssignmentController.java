package com.cofco.sys.controller.role;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.role.RoleEntity;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.sys.service.RoleAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;

/**
 * 角色分配用户
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 10:07:59
 */
@RestController
@RequestMapping("sys/roleAssignment")
public class RoleAssignmentController extends BaseController {

	@Autowired
	private RoleAssignmentService roleAssignmentService;

	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("sys:roleAssignment:list")
	@SysLog("查看启用角色列表")
	public Result list(@RequestParam Map<String, Object> params) throws UnsupportedEncodingException {
		//查询列表数据
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<RoleEntity> roleList = roleAssignmentService.queryList(query);
		PageHelper.endPage();
		int total = roleAssignmentService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(roleList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 查看有此角色的用户列表
	 */
	@RequestMapping("/infoYes")
    //@RequiresPermissions("sys:roleAssignment:list")
	@SysLog("查看有此角色的人员")
	public Result infoYes(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<UserEntity> userList = roleAssignmentService.queryListUserYes(query);
		PageHelper.endPage();
		int total = roleAssignmentService.UserYesTotal(query);
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 查看无此角色的用户列表
	 */
	@RequestMapping("/infoNo")
    //@RequiresPermissions("sys:roleAssignment:list")
	@SysLog("查看无此角色的人员")
	public Result infoNo(@RequestParam Map<String, Object> params){
		Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<UserEntity> userList = roleAssignmentService.queryListUserNo(query);
		PageHelper.endPage();
		int total =  roleAssignmentService.userTotal(query);
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

    /**
     * 修改人员角色权限（去除）
     */
    @RequestMapping("/updateYes")
    //@RequiresPermissions("sys:roleAssignment:update")
    @SysLog("修改人员角色权限")
    public Result deleteYes(@RequestBody RoleEntity role){
		int i = roleAssignmentService.deleteYes(role);
		if(i<1){
//			return Result.error("更改失败");
		}
        return Result.ok();
    }

    /**
     * 修改人员角色权限（添加）
     */
    @RequestMapping("/updateNo")
    //@RequiresPermissions("sys:roleAssignment:update")
    @SysLog("修改人员角色权限")
    public Result saveNo(@RequestBody RoleEntity role){
		int i = roleAssignmentService.saveNo(role);
		if(i<1){
//			return Result.error("更改失败");
		}
		return Result.ok();
    }
}
