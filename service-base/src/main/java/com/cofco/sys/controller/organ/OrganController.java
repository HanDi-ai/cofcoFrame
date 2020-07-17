package com.cofco.sys.controller.organ;

import java.util.List;
import java.util.Map;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.common.Constant;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.StringUtils;
import com.cofco.sys.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cofco.sys.entity.organ.OrganEntity;
import com.cofco.sys.service.OrganService;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;


/**
 * 组织机构
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-07-14 13:42:42
 */
@RestController
@RequestMapping("sys/organ")
public class OrganController extends BaseController {
	@Autowired
	private OrganService organService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("sys:organ:all")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		PageHelper.startPage(query.getPage(),query.getLimit());
		List<OrganEntity> organList = organService.queryList(query);
		PageHelper.endPage();
		int total = organService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(organList, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("sys:organ:all")
	@SysLog("查看组织")
	public Result info(@PathVariable("id") String id){
		OrganEntity organ = organService.queryObject(id);
		return Result.ok().put("organ", organ);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("sys:organ:all")
	@SysLog("保存组织")
	public Result save(@RequestBody OrganEntity organ){
		if(organ.getParentId()==null||organ.getParentId().equals("0")){//判断是否是根节点
			String subcode=organService.selectParentId(organ.getParentId());//获取组织机构的code
			if(subcode==null ||subcode.equals("")){
				organ.setCode("000001");//设置根节点code
			}else{
				organ.setCode(subcode);
			}
		}else{
			String subcode=organService.selectParentId(organ.getParentId());
			if(subcode==null){
				OrganEntity parentcode=organService.queryObject(organ.getParentId());
				String parentCode=parentcode.getCode();
				String addCode=parentCode+"@000001";
				organ.setCode(addCode);
			}else{
				organ.setCode(subcode);
			}
		}
        String id = organService.save(organ);//保存组织机构
        if(StringUtils.isEmpty(id)){
            return Result.error("保存"+organ.getName()+"失败!");
        }
        OrganEntity organEntity = organService.queryObject(id);
        return Result.ok("保存"+organ.getName()+"成功!").put("organInfo",organEntity);
	}

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("sys:organ:all")
	@SysLog("修改组织")
    public Result update(@RequestBody OrganEntity organEntity){
        int count = organService.update(organEntity);
        if(count<1){
            return Result.error("修改"+organEntity.getName()+"失败!");
        }
		organEntity = organService.queryObject(organEntity.getId());
        return Result.ok("修改"+organEntity.getName()+"成功!").put("organInfo",organEntity);
    }


    /**
	 * 查询组织机构树
	 * @return
	 */
	@RequestMapping(value = "/organTree")
	//@RequiresPermissions("sys:organ:all")
	public Result codeTree(){
        List<OrganEntity> organEntities = organService.queryListByBean(new OrganEntity());
        return Result.ok().put("organTree", organEntities);
	}
	/**
	 * 查询当前登录人组织机构下级所有组织机构
	 * @return
	 */
	@RequestMapping(value = "/codeTreeDown")
	//@RequiresPermissions("sys:organ:all")
	public Result codeTreeDown(){
		List<OrganEntity> organEntities = organService.queryListByBeanDown(new OrganEntity());
		return Result.ok().put("organTree", organEntities);
	}
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("sys:organ:all")
	@SysLog("删除组织")
	public Result delete(@RequestBody String ids){
		int num=organService.updateIsdel(ids,Constant.YESNO.NO.getValue());
		if(num>0){
			return Result.error();
		}else{
			return Result.ok();
		}
	}

	/**
	 * 查询当前层级下是否有重复的简称
	 */
	@RequestMapping("/queryRepetition")
	//@RequiresPermissions("sys:organ:all")
	@SysLog("查询当前层级下是否有重复的简称")
	public Result queryRepetition(@RequestBody OrganEntity organEntity){
			int num=organService.queryRepetition(organEntity);
			if(num>0){
				return Result.error();
			}else{
				return Result.ok();
			}
	}

	/**
	 * 获取计划列表
	 * @return
	 */
	@RequestMapping(value = "/getOrgans")
	@SysLog("获取机构列表")
	public Result getOrgans(){
		List<OrganEntity> organs = organService.getOrgans();
		return Result.ok().put("organs", organs);
	}
	
}
