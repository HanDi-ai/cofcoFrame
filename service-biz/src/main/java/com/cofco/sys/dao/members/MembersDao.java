package com.cofco.sys.dao.members;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.members.AllianceMemberLogEntity;
import com.cofco.sys.entity.members.MembersEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 *联盟成员注册
 * @author handi
 * @date 2019/11/01 9:26:40
 * @email 876053009@qq.com
 */
@Repository
public interface MembersDao extends BaseDao<MembersEntity> {
    /**
     * 查询成员表的公私钥
     * @param whMemberId
     * @return
     */
    MembersEntity queryKey(String whMemberId);
    /**
     * SEQ_ALLIANCE_MENBER查询
     */
    int seqMemberId();
    /**
     * SEQ_ALLIANCE_MENBER查询
     */
    int seqMemberLogId();
    /**
     * 保存
     * @param membersEntity
     */
    @Override
    int save(MembersEntity membersEntity);
    /**
     * log新增
     * @param allianceMemberLogEntity
     * @return
     */
    int saveLog(AllianceMemberLogEntity allianceMemberLogEntity);
    /**
     * 修改
     * @param membersEntity
     */
    @Override
    int update(MembersEntity membersEntity);
    /**
     * 修改日志表修改日志表审核状态值
     * @param allianceMemberLogEntity
     * @return
     */
    int updateLog(AllianceMemberLogEntity allianceMemberLogEntity);
    /**
     * 大商所提供的公钥更新入库
     * @param membersEntity
     */
    int updateRevert(MembersEntity membersEntity);

    /**
     * 将联盟成员ID更新入库
     * @param map
     * @return
     */
    int updateMemberId(Map<String,Object> map);

    /**
     * 删除
     * @param membersEntity
     */
    int delete(MembersEntity membersEntity);


}
