package com.cofco.sys.dao.members;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.members.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FreezeDao extends BaseDao<BillOperatorEntity> {

    List<BillEntity> queryListInfo(Map<String, Object> var);

    int queryBillTotal(Map<String, Object> var);

    List<BillPledgeEntity> queryPledgeList(Map<String, Object> var);

    int queryPledgeTotal(Map<String, Object> var);

    List<BillFlowEntity> preUnfreezeInfo(Map<String, Object> var);

    int queryBillDecisionTotal(String id);

    void billCancel(BillEntity billEntity);

    BillEntity queryBillInfo(String id);

    List<BillFlowEntity> queryBillFlowList(String wbillId);

    void updateBillOperator(String id);

    void updateBillFlow(String id);

    void saveLog(BillOperatorLogEntity operatorLogEntity);

    void updateLog(BillOperatorLogEntity operatorLogEntity);

    void saveBillLog(BillLogEntity billLogEntity);

    void updateBillLog(BillLogEntity billLogEntity);

    List<String> queryFlowId(String tempId);

    void updateWeight(List<String> val);

    int queryOperatorLogTotal(Map<String, Object> var);

}
