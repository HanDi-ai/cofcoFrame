package com.cofco.sys.service.members.impl;

import com.cofco.sys.dao.members.MembersDao;
import com.cofco.sys.entity.members.AllianceMemberLogEntity;
import com.cofco.sys.entity.members.MembersEntity;
import com.cofco.sys.service.members.MembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 联盟成员
 * @author handi
 * @date 2019 11/07 31:39:45
 */
@Service("membersService")
@Transactional
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MembersDao membersDao;

    /**
     * SEQ_ALLIANCE_MENBER查询
     * @return
     */
    @Override
    public int seqMemberId() {
        return membersDao.seqMemberId();
    }

    /**
     * SEQ_ALLIANCE_MENBER_LOG查询
     * @return
     */
    @Override
    public int seqMemberLogId() {
        return membersDao.seqMemberLogId();
    }

    /**
     * 联盟成员信息查询
     * @param map
     * @return
     */

    @Override
    public List<MembersEntity> queryList(Map<String, Object> map) {
        return membersDao.queryList(map);
    }
    /**
     * 查询成员表的公私钥
     */
    @Override
    public MembersEntity queryKey(String memberId) {
        return membersDao.queryKey(memberId);
    }
    /**
     * 总条数查询
     * @param map
     * @return
     */
    @Override
    public int queryTotal(Map<String, Object> map) {
        return membersDao.queryTotal(map);
    }

    /**
     * 根据id号查询成员信息
     */
    @Override
    public MembersEntity queryObject(String id) {
        return membersDao.queryObject(id);
    }

    /**
     * 联盟成员信息注册
     * @param membersEntity
     * @throws Exception
     */
    @Override
    public int save(MembersEntity membersEntity) throws Exception {
        try{
            membersDao.save(membersEntity);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }
    /**
     * log新增
     * @param allianceMemberLogEntity
     * @return
     */
    @Override
    public void saveLog(AllianceMemberLogEntity allianceMemberLogEntity) {
        membersDao.saveLog(allianceMemberLogEntity);
    }

    /**
     * 联盟成员信息修改
     * @param membersEntity
     */
    @Override
    public int update(MembersEntity membersEntity) {
        try{
            membersDao.update(membersEntity);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 修改日志表修改日志表审核状态值
     * @param allianceMemberLogEntity
     * @return
     */
    @Override
    public void updateLog(AllianceMemberLogEntity allianceMemberLogEntity) {
        membersDao.updateLog(allianceMemberLogEntity);
    }

    /**
     * 大商所提供的公钥更新入库
     * @param membersEntity
     */
    @Override
    public void updateRevert(MembersEntity membersEntity) {
        membersDao.updateRevert(membersEntity);
    }

    /**
     * 联盟成员ID更新入库
     * @param map
     */
    @Override
    public void updateMemberId(Map<String,Object> map) {
        membersDao.updateMemberId(map);
    }

    /**
     * 联盟成员信息删除
     * @param membersEntity
     */
    @Override
    public void delete(MembersEntity membersEntity) {
        membersDao.delete(membersEntity);
    }


}
