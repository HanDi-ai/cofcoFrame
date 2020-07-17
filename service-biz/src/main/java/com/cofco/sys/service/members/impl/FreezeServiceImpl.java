package com.cofco.sys.service.members.impl;

import com.cofco.constant.Const;
import com.cofco.sys.dao.members.FreezeDao;
import com.cofco.sys.entity.members.*;
import com.cofco.sys.service.members.FreezeService;
import com.cofco.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("freezeService")
@Transactional
public class FreezeServiceImpl implements FreezeService {

    @Autowired
    private FreezeDao freezeDao;

    @Override
    public List<BillEntity> queryList(Map<String, Object> var) {
        return freezeDao.queryListInfo(var);
    }

    @Override
    public int queryBillTotal(Map<String, Object> var) {
        return freezeDao.queryBillTotal(var);
    }

    @Override
    public List<BillPledgeEntity> queryPledgeList(Map<String, Object> var) {
        return freezeDao.queryPledgeList(var);
    }

    @Override
    public int queryPledgeTotal(Map<String, Object> var) {
        return freezeDao.queryPledgeTotal(var);
    }

    /**
     * 仓单质押预解质信息
     *
     * @return 仓单操作记录
     */
    @Override
    public BillOperatorEntity queryObject(Map<String, Object> var) {
        return freezeDao.queryObject(var);
    }

    /**
     * 预解冻信息查询
     *
     * @return
     */
    @Override
    public List<BillFlowEntity> preUnfreezeInfo(Map<String, Object> var) {
        return freezeDao.preUnfreezeInfo(var);
    }


    /**
     * 仓单质押预解质撤销提交
     *
     * @param tempId 临时ID
     * @param operatorType 操作类型
     */
    @Override
    public void preUnfreezeCancel(String tempId, String operatorType) {
        Map<String, Object> var = new HashMap<>();
        // 临时ID
        var.put("tempId", tempId);
        // 操作类型
        var.put("operatorType", operatorType);

        freezeDao.update(var);
    }

    /**
     * 仓单质押预解质提交
     *
     * @param tempId 临时ID
     * @param operatorType 操作类型
     */
    @Override
    public void preUnfreeze(String tempId, String operatorType) {
        Map<String, Object> var = new HashMap<>();
        // 临时ID
        var.put("tempId", tempId);
        // 操作类型
        var.put("operatorType", operatorType);

        freezeDao.update(var);
    }

    /**
     * 仓单质押解质提交
     *
     * @param tempId 临时ID
     * @param operatorType 操作类型
     */
    @Override
    public void unfreeze(String tempId, String operatorType) {
        Map<String, Object> var = new HashMap<>();
        // 临时ID
        var.put("tempId", tempId);
        // 操作类型
        var.put("operatorType", operatorType);

        freezeDao.update(var);
    }

    /**
     * 仓单注销可否判定信息查询
     *
     * @param id
     * @return
     */
    @Override
    public int queryBillDecisionTotal(String id) {
        return freezeDao.queryBillDecisionTotal(id);
    }

    /**
     * 仓单信息查询
     *
     * @param id
     * @return
     */
    @Override
    public BillEntity queryBillInfo(String id) {
        return freezeDao.queryBillInfo(id);
    }

    /**
     * 仓单注销提交
     *
     * @param billEntity 仓单
     */
    @Override
    public void billCancel(BillEntity billEntity) {
        //修改时间
        billEntity.setTime_Modify(new Date());
        //修改人
        billEntity.setModifier(UserUtils.getCurrentUserId());
        //注销标识
        billEntity.setIs_Delete(Integer.valueOf(Const.DELETE_FLG_YES));
        // 仓单表更新
        freezeDao.billCancel(billEntity);
    }

    /**
     * 仓单操作流水表
     * @param wbillId
     * @return
     */
    @Override
    public List<BillFlowEntity> queryBillFlowList(String wbillId) {
        return freezeDao.queryBillFlowList(wbillId);
    }

    /**
     * 仓单操作记录表注销
     * @param id
     */
    @Override
    public void updateBillOperator(String id) {
        freezeDao.updateBillOperator(id);
    }

    /**
     * 仓单操作流水表注销
     * @param id
     */
    @Override
    public void updateBillFlow(String id) {
        freezeDao.updateBillFlow(id);
    }

    /**
     * 仓单操作记录日志表登录
     * @param operatorLogEntity
     */
    @Override
    public void saveLog(BillOperatorLogEntity operatorLogEntity) {
        freezeDao.saveLog(operatorLogEntity);
    }

    /**
     * 仓单操作记录日志表更新
     * @param operatorLogEntity
     */
    @Override
    public void updateLog(BillOperatorLogEntity operatorLogEntity) {
        freezeDao.updateLog(operatorLogEntity);
    }

    /**
     * 仓单日志表登录
     * @param billLogEntity
     */
    @Override
    public void saveBillLog(BillLogEntity billLogEntity) {
        freezeDao.saveBillLog(billLogEntity);
    }

    /**
     * 仓单日志表更新
     * @param billLogEntity
     */
    @Override
    public void updateBillLog(BillLogEntity billLogEntity) {
        freezeDao.updateBillLog(billLogEntity);
    }

    /**
     * 仓单操作流水表ID集合 TODO
     * @param tempId
     * @return
     */
    @Override
    public List<String> queryFlowId(String tempId) {
        return freezeDao.queryFlowId(tempId);
    }

    /**
     * 仓单操作流水表的重量更新 TODO
     * @param val
     */
    @Override
    public void updateWeight(List<String> val) {
        freezeDao.updateWeight(val);
    }

    /**
     * 防止重复提交申请
     * @param id
     * @param operatorType
     * @return
     */
    @Override
    public int queryOperatorLogTotal(String id, String operatorType) {
        Map<String, Object> var = new HashMap<>();
        // 临时ID
        var.put("id", id);
        // 操作类型
        var.put("operatorType", operatorType);

        return freezeDao.queryOperatorLogTotal(var);
    }
}
