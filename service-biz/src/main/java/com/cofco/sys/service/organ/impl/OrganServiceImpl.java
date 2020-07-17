package com.cofco.sys.service.organ.impl;

import com.cofco.base.common.Constant;
import com.cofco.base.exception.MyException;
import com.cofco.base.page.Page;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.StringUtils;
import com.cofco.base.utils.Utils;
import com.cofco.dto.UserWindowDto;
import com.cofco.sys.entity.user.UserEntity;
import com.cofco.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cofco.sys.dao.organ.OrganDao;
import com.cofco.sys.entity.organ.OrganEntity;
import com.cofco.sys.service.OrganService;
import com.cofco.base.common.Constant.OrganType;
import org.springframework.transaction.annotation.Transactional;

import javax.management.MalformedObjectNameException;


@Service("organService")
@Transactional
public class OrganServiceImpl implements OrganService {
	@Autowired
	private OrganDao organDao;
	
	@Override
	public OrganEntity queryObject(String id){
		return organDao.queryObject(id);
	}

	@Override
	public List<OrganEntity> queryList(Map<String, Object> map){
		return organDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return organDao.queryTotal(map);
	}
	
	@Override
	public String save(OrganEntity organ){
        UserEntity currentUser = UserUtils.getCurrentUser();
        setPublicSaveUpdate(organ);
	    organ.setId(Utils.uuid());
	    organ.setIsDel(Constant.YESNO.YES.getValue());
	    organ.setCreateTime(new Date());
	    organ.setCreateId(currentUser.getId());
	    organ.setBapid(organ.getParentId());
        int count = organDao.save(organ);
        if(count>0){
            return organ.getId();
        }
        return "";

	}

    @Override
    public String selectParentId(String parentId) {
	    String code=organDao.selectParentId(parentId);//查询当前机构的code
	    if(code==null || code.equals("null")){
            return code;
        }else{
            String [] strs=code.split("[^0-9]");
            String numStr = strs[strs.length-1];
            if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
                int n = numStr.length();//取出字符串的长度
                int num = Integer.parseInt(numStr)+1;//将该数字加一
                String added = String.valueOf(num);
                n = Math.min(n, added.length());//拼接字符串
                return code.subSequence(0, code.length()-n)+added;//返回当前字符串
            }else{
                throw new NumberFormatException();
            }
        }

    }

    /**
     * 保存和更新,公共操作
     * @param organ
     */
	public void setPublicSaveUpdate(OrganEntity organ){
	    if(StringUtils.isEmpty(organ.getParentId())){
	        throw new MyException("父节点不能为空!");
        }
        OrganEntity organEntity = organDao.queryObject(organ.getParentId());
	    //组织为部门类型
        String depType = Constant.OrganType.DEPART.getValue();
        //如果父节点是部门，那子节点只能是部门类型
	    if( organEntity !=null && depType.equals(organEntity.getType()) && !depType.equals(organ.getType())){
            throw new MyException("父节点是部门,子节点只能是部门类型");
        }
        if(Constant.OrganType.DEPART.getValue().equals(organ.getType())){
            //归属机构
            String bapid = findBapid(organ.getParentId());
            organ.setBapid(bapid);
        }else {
            organ.setBapid("0");
        }
    }

    /**
     * 查询父机构
     * @param organId 组织id
     * @return
     */
    public  String findBapid(String organId){
        OrganEntity organEntity = organDao.queryObject(organId);
        //如果组织为机构或者根节点,停止递归,返回机构
        if(organEntity != null && (OrganType.ORGAN.getValue().equals(organEntity.getType()) || OrganType.CATALOG.getValue().equals(organEntity.getType()))){
            return organEntity.getId();
        }else{
            return findBapid(organEntity.getParentId());
        }
    }
	
	@Override
	public int update(OrganEntity organ){
        setPublicSaveUpdate(organ);
	    organ.setUpdateId(UserUtils.getCurrentUserId());
	    organ.setUpdateTime(new Date());
        return organDao.update(organ);
	}
	
	@Override
	public int delete(String id){
        return organDao.delete(id);
	}
	
	@Override
	public int deleteBatch(String[] ids){
        return organDao.deleteBatch(ids);
	}

	@Override
	public List<OrganEntity> queryListByBean(OrganEntity organEntity) {
	    organEntity.setIsDel(Constant.YESNO.YES.getValue());
        organEntity.setUserid(UserUtils.getCurrentUserId());
        String userId=UserUtils.getCurrentUserId();
        List<OrganEntity> organEntities =null;
        if(userId.equals(Constant.SUPERR_USER)){
           organEntities = organDao.ListByBeanadmin(organEntity);
        }else {
           organEntities = organDao.queryListByBean(organEntity);
        }
            return organEntities;

	}
    @Override
    public List<OrganEntity> queryListByBeanDown(OrganEntity organEntity) {
        organEntity.setIsDel(Constant.YESNO.YES.getValue());
        organEntity.setUserid(UserUtils.getCurrentUserId());
        String userId=UserUtils.getCurrentUserId();
        List<OrganEntity> organEntities =null;
        if(userId.equals(Constant.SUPERR_USER)){
            organEntities = organDao.ListByBeanadmin(organEntity);
        }else {
            organEntities = organDao.queryListByBeanDown(organEntity);
        }
        return organEntities;

    }

    @Override
    public List<OrganEntity> queryListByCode(String code) {
        OrganEntity organEntity = new OrganEntity();
        organEntity.setIsDel(Constant.YESNO.NO.getValue());
        organEntity.setCode(code);
        return organDao.queryListByBean(organEntity);
    }

    @Override
    @Transactional
    public int updateIsdel(String ids, String type) {
        int num=0;
        for (String id:ids.split(",")){
            //1、查询当前组织机构下是否有用户
            String organId=id;
            int i=organDao.organOfUser(organId);
            //2、查询当前机构下是否有角色
            int j=organDao.organOfRole(organId);
            //3、查询当前机构下是否有子节点
            int y=organDao.oragnChildren(organId);
            num=i+y+j;
            if(num>0){
                break;
            }
            OrganEntity organEntity = new OrganEntity();
            organEntity.setId(id);
            organEntity.setIsDel(type);
            organDao.update(organEntity);
        }
        return num;
    }

    @Override
    public Page<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto, int pageNum) {
        PageHelper.startPage(pageNum,Constant.pageSize);
        organDao.queryPageByDto(userWindowDto);
        return PageHelper.endPage();
    }

    /**
     * 查询组织机构全称 简称是否重名
     * @param organEntity
     * @return
     */
    @Override
    public int queryRepetition(OrganEntity organEntity) {
        int i=0;
        int y=0;
        i=organDao.queryRepetition(organEntity);
        y=organDao.queryDesertEagleBase(organEntity);
        int num=i+y;
        return num;
    }
    /**
     * 通过用户ID 查询简称、code、id
     */
    @Override
    public Map<String, Object> adoptUserIdSelectData(String userId) {
        Map<String, Object> map = organDao.adoptUserIdSelectData(userId);
        return map;
    }

    @Override
    public List<OrganEntity> getOrgans() {
        return organDao.getOrgans();
    }
}
