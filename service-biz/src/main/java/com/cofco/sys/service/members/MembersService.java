package com.cofco.sys.service.members;

import com.cofco.sys.entity.members.AllianceMemberLogEntity;
import com.cofco.sys.entity.members.MembersEntity;

import java.util.List;
import java.util.Map;

/**
 * 联盟成员注册
 * @author handi
 * @date 2019-10-29 9:25:29
 */
public interface MembersService {

    /**
     * 获取List列表
     * @param map
     * @return
     */
    List<MembersEntity> queryList(Map<String, Object> map);
    /**
     * SEQ_ALLIANCE_MENBER查询
     */
    int seqMemberId();
    /**
     * SEQ_ALLIANCE_MENBER_LOG查询
     */
    int seqMemberLogId();
    /**
     * 查询成员表的公私钥
     * @param memberId
     * @return
     */
    MembersEntity queryKey(String memberId);
    /**
     * 总条数查询
     * @param map
     * @return
     */
    int queryTotal(Map<String, Object> map);
    /**
     * 根据id号查询成员信息
     * @param id
     * @return
     */
    MembersEntity queryObject(String id);
    /**
     * 保存
     * @param membersEntity
     * @throws Exception
     */
    int save(MembersEntity membersEntity)throws Exception;
    /**
     * log新增
     * @param allianceMemberLogEntity
     * @return
     */
    void saveLog(AllianceMemberLogEntity allianceMemberLogEntity);
    /**
     * 修改
     * @param membersEntity
     */
    int update(MembersEntity membersEntity)throws Exception;
    /**
     * 修改日志表修改日志表审核状态值
     * @param allianceMemberLogEntity
     * @return
     */
    void updateLog(AllianceMemberLogEntity allianceMemberLogEntity);
    /**
     * 大商所提供的公钥更新入库
     * @param membersEntity
     */
    void updateRevert(MembersEntity membersEntity);

    /**
     * 将联盟成员ID更新入库
     * @param map
     */
    void updateMemberId(Map<String,Object> map);

    /**
     * 删除
     * @param membersEntity
     */
    void delete(MembersEntity membersEntity);

}
