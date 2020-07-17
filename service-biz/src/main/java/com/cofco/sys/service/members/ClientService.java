package com.cofco.sys.service.members;

import com.cofco.sys.entity.members.AllianceClientEntity;
import com.cofco.sys.entity.members.AllianceClientLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 联盟客户注册
 * @auther cofco
 * @date 2019-10-29 13:28:27
 */
public interface ClientService {

    String clientSeq();

    String clientSeqLog();

    int containNums(String memberId);

    void save(AllianceClientEntity clientEntity);

    void saveLog(AllianceClientLogEntity clientLogEntity);

    void updateLog(AllianceClientLogEntity clientLogEntity);

    void update(AllianceClientEntity clientEntity);

    void updateClientId(String clientId, String clientTmpId);

    void delete(AllianceClientEntity clientEntity);

    AllianceClientEntity queryObject(String id);

    List<AllianceClientEntity> queryList(Map<String, Object> var);

    int queryTotal(Map<String, Object> var);
}
