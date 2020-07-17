package com.cofco.sys.dao.members;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.members.AllianceClientEntity;
import com.cofco.sys.entity.members.AllianceClientLogEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 联盟客户注册
 *
 * @author cofco
 * @date 2019-10-29 13:42:42
 */
@Repository
public interface ClientDao extends BaseDao<AllianceClientEntity> {

    String clientSeq();

    String clientSeqLog();

    int containNums(String memberId);

    void saveLog(AllianceClientLogEntity clientLogEntity);

    void updateLog(AllianceClientLogEntity clientLogEntity);

    void updateClientId(@Param("clientId") String clientId, @Param("clientTmpId") String clientTmpId);

    int delete();
}
