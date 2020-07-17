package com.cofco.sys.controller.code;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.utils.*;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.code.CodeEntity;
import com.cofco.sys.service.code.impl.CodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 系统数据字典
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-07-14 13:42:42
 */
@RestController
@RequestMapping("/sys/code")
public class CodeController extends BaseController {
	@Autowired
	private CodeService codeService;
	
	/**
	 * 查询码值树
	 * @return
	 */
	@RequestMapping(value = "/codeTree")
	//@RequiresPermissions("sys:code:codeTree")
	@SysLog("查看字典")
	public Result codeTree(){
        List<CodeEntity> codeEntities = codeService.queryListByBean(new CodeEntity());
        return Result.ok().put("codeTree", codeEntities);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("sys:code:info")
	@SysLog("查看字典")
	public Result info(@PathVariable("id") String id){
		CodeEntity code = codeService.queryObject(id);
		
		return Result.ok().put("code", code);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("sys:code:update")
	@SysLog("保存字典")
	public Result save(@RequestBody CodeEntity code){
		CodeEntity queryCode = new CodeEntity();
		queryCode.setMark(code.getMark());
		List<CodeEntity> entityList = codeService.queryListByCode(queryCode);
		if(entityList != null && entityList.size()>0){
			return Result.error("字典标识已经存在,请重新输入!");
		}
        String id = codeService.save(code);
        CodeEntity codeEntity = codeService.queryObject(id);
        return Result.ok().put("codeInfo",codeEntity);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("sys:code:update")
	@SysLog("修改字典")
	public Result update(@RequestBody CodeEntity code){
		codeService.update(code);
		
		return Result.ok().put("codeInfo",code);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("sys:code:delete")
	@SysLog("删除字典")
	public Result delete(@RequestBody String ids){
        codeService.deleteBatch(StringUtils.getArrayByArray(ids.split(",")));
		return Result.ok();
	}

	/**
	 * 字典通用取值方法
	 * @return
	 */
	@RequestMapping(value = "/getCodeByMark")
	@SysLog("字典通用取值")
	public Result getCodeByMark(@RequestParam String mark){
		List<CodeEntity> codeEntities = codeService.getCodeByMark(mark);
		return Result.ok().put("codes", codeEntities);
	}

	
}
