package com.cofco.sys.service.members;

import com.cofco.sys.entity.members.*;

import java.util.List;
import java.util.Map;

public interface FreezeService {

    /**
     * 获取List列表
     */
    List<BillEntity> queryList(Map<String, Object> map);

    /**
     * 获取总记录数
     */
    int queryBillTotal(Map<String, Object> map);

    /**
     * 获取List列表
     */
    List<BillPledgeEntity> queryPledgeList(Map<String, Object> map);

    /**
     * 获取总记录数
     */
    int queryPledgeTotal(Map<String, Object> map);

    /**
     * 仓单质押预解质信息
     * @return
     */
    BillOperatorEntity queryObject(Map<String, Object> var);

    /**
     * 预解冻信息查询
     */
    List<BillFlowEntity> preUnfreezeInfo(Map<String, Object> var);

    /**
     * 仓单质押预解质撤销提交
     */
    void preUnfreezeCancel(String tempId, String operatorType);

    /**
     * 仓单质押预解质提交
     */
    void preUnfreeze(String tempId, String operatorType);

    /**
     * 仓单质押解质提交
     */
    void unfreeze(String tempId, String operatorType);

    /**
     * 仓单注销可否判定信息查询
     */
    int queryBillDecisionTotal(String id);

    /**
     * 仓单信息查询
     */
    BillEntity queryBillInfo(String id);

    /**
     * 仓单注销提交
     * @param billEntity
     */
    void billCancel(BillEntity billEntity);

    /**
     * 仓单操作流水表
     */
    List<BillFlowEntity> queryBillFlowList(String wbillId);

    /**
     * 仓单操作记录表注销
     */
    void updateBillOperator(String id);

    /**
     * 仓单操作流水表注销
     */
    void updateBillFlow(String id);

    /**
     * 仓单操作记录日志表登录
     * @param operatorLogEntity
     */
    void saveLog(BillOperatorLogEntity operatorLogEntity);

    /**
     * 仓单操作记录日志表更新
     * @param operatorLogEntity
     */
    void updateLog(BillOperatorLogEntity operatorLogEntity);

    /**
     * 仓单日志表登录
     * @param billLogEntity
     */
    void saveBillLog(BillLogEntity billLogEntity);

    /**
     * 仓单操作记录日志表更新
     * @param billLogEntity
     */
    void updateBillLog(BillLogEntity billLogEntity);

    /**
     * 仓单操作流水表ID集合 TODO
     */
    List<String> queryFlowId(String tempId);

    /**
     * 仓单操作流水表的重量更新 TODO
     */
    void updateWeight(List<String> val);

    /**
     * 防止重复提交申请
     */
    int queryOperatorLogTotal(String id, String operatorType);
}
