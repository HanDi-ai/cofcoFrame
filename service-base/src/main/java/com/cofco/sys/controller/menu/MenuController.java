package com.cofco.sys.controller.menu;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.common.Constant;
import com.cofco.base.utils.Result;
import com.cofco.base.utils.StringUtils;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.menu.MenuEntity;
import com.cofco.sys.service.MenuService;
import com.cofco.utils.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;


/**
 * 菜单表
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-05-03 10:07:59
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("sys:menu:info")
	@SysLog("查看菜单")
	public Result info(@PathVariable("id") String id){
		MenuEntity menu = menuService.queryObject(id);
		
		return Result.ok().put("menu", menu);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("sys:menu:update")
	public Result save(@RequestBody MenuEntity menu){
        String id = menuService.save(menu);
        MenuEntity menuEntity = menuService.queryObject(id);
        return Result.ok().put("menu",menuEntity);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("sys:menu:update")
	@SysLog("修改菜单")
	public Result update(@RequestBody MenuEntity menu){
		menuService.update(menu);
		
		return Result.ok().put("menu",menu);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	@SysLog("删除菜单")
	//@RequiresPermissions("sys:menu:delete")
	public Result delete(@RequestBody  String ids){
		menuService.deleteBatch(StringUtils.getArrayByArray(ids.split(",")));
		return Result.ok();
	}

	/**
	 * 查询当前用户授权菜单
	 * @return
	 */
	@RequestMapping(value = "/userMenu")
	public Result userMenu(){
        List<MenuEntity> menuEntities = menuService.queryListUser(ShiroUtils.getUserId());
        return Result.ok().put("menuList",menuEntities);
	}

	/**
	 * 角色授权
	 * @return
	 */
	@RequestMapping(value = "/perms/{sign}")
	@SysLog("角色授权")
	public Result perms(@PathVariable("sign") String sign){
		//查询列表数据
		List<MenuEntity> menuList = null;

		//只有超级管理员，才能查看所有管理员列表
		if(getUserId().equals(Constant.SUPERR_USER)){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("sign", sign);
			if ("0".equals(sign)) {
				map.put("sign1", null);
			} else {
				map.put("sign1", "3");
			}
			menuList = menuService.queryList(map);
		}else{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("sign", sign);
			map.put("sign1", null);
			map.put("userId", getUserId());
			menuList = menuService.selectByUserIdsign(map);
		}

		return Result.ok().put("menuList", menuList);
	}

    /**
     * 查询除按钮个的其它资源菜单
     * @return
     */
	@RequestMapping(value = "/selectMenu")
	//@RequiresPermissions("sys:menu:list")
	public Result selectMenu(){
        List<MenuEntity> menuEntities = menuService.queryNotButtonnList();
        return  Result.ok().put("menuEntities",menuEntities);
	}
	
}
