package com.cofco.sys.dao.members;

import com.cofco.base.dao.BaseDao;
import com.cofco.sys.entity.members.*;

import java.util.List;
import java.util.Map;

/**
 * 仓单业务管理
 * @author handi
 * @date 2019 11/05 15:45:50
 */
public interface BillDao extends BaseDao<BillEntity> {

    /**
     * 锁货通过后，查询operator表的信息
     * @param id
     * @return
     */
    BillOperatorEntity queryPreFreezing(String id);

    /**
     * 查询flow表中的 仓单id+仓单重量
     * @param id
     * @return
     */
    List<BillFlowEntity> queryFlow(String id);

    /**
     * 总条数查询
     * @param map
     * @return
     */
    @Override
    int queryTotal(Map<String, Object> map);

    /**
     *根据仓单id查询需传给大商所物联网接口的仓单基础信息
     */
    BillEntity queryIot(Map<String, Object> map);

    /**
     *W_BILL_IOT表数据查询
     */
    List<BillIotEntity> queryIotInfo(Map<String, Object> map);

    /**
     * W_BILL_OPERATOR_LOG表中查询临时ID
     */
    String queryPrefreezeId(String id);
    /**
     * SEQ_Bill查询
     */
    int seqBillId();

    /**
     * SEQ_BILL_LOG查询
     */
    int seqBillLogId();

    /**
     * SEQ_BILL_OPERATOR查询
     */
    int seqBillOperatorId();

    /**
     * SEQ_BILL_OPERATOR_LOG查询
     */
    int seqBillOperatorlogId();

    /**
     * SEQ_BILL_IOT查询
     */
    int seqBillIot();

    /**
     * 仓单备案提交
     * @param billEntity
     * @return
     */
    @Override
    int save(BillEntity billEntity);

    /**
     * 仓单备案临时ID入库
     * @param billLogEntity
     * @return
     */
    int saveOnrecordLog(BillLogEntity billLogEntity);
    /**
     * 仓单操作临时ID入库(操作包含:预冻结，预冻结撤销，冻结)
     * @param billOperatorLogEntity
     * @return
     */
    int saveOperation(BillOperatorLogEntity billOperatorLogEntity);

    /**
     * iot数据表新增数据
     * @param billIotEntity
     * @return
     */
    int saveIot(BillIotEntity billIotEntity);

    /**
     * 仓单质押预冻结提交
     * @param billOperatorEntity
     */
    void preFreezing(BillOperatorEntity billOperatorEntity);

    /**
     * 仓单质押预冻结提交
     * @param billFlowEntity
     */
    void preFreezingFlow(BillFlowEntity billFlowEntity);

    /**
     * 修改W_BILL_LOG表状态值
     * @param billLogEntity
     * @return
     */
    int updateLog(BillLogEntity billLogEntity);
    /**
     *修改操作类型状态
     * @param billOperatorEntity
     * @return
     */
    void update(BillOperatorEntity billOperatorEntity);

    /**
     * 修改联盟仓单临时ID
     * @param billEntity
     */
    /*void updateWbillTmpId(BillEntity billEntity);*/

    /**
     * 修改联盟仓单ID
     * @param map
     */
    void updateWbillId(Map<String,Object> map);


    /**
     * 将虚拟仓单ID更新入库及修改操作类型
     * @param map
     */
    int updateVir(Map<String,Object> map);

    /**
     * 修改W_BILL_OPERATOR_LOG表状态值
     * @param billOperatorLogEntity
     * @return
     */
    int updateOperatorLog(BillOperatorLogEntity billOperatorLogEntity);

    /**
     * 修改W_BILL_OPERATOR_LOG表类型值
     * @param billOperatorLogEntity
     * @return
     */
    void updateOperatorType(BillOperatorLogEntity billOperatorLogEntity);

    /**
     * 删除W_BILL_FLOW表数值
     * @param billFlowEntity
     * @return
     */
    void delFlow(BillFlowEntity billFlowEntity);

    /**
     * 删除W_BILL_FLOW表数值,当预冻结被驳回时
     * @param billFlowEntity
     * @return
     */
    void preFreedelFlow(BillFlowEntity billFlowEntity);

    /**
     * 仓单质押预冻结撤销提交
     * @param
     */
    //void delete(BillOperatorEntity billOperatorEntity);
    int delete(Map<String,Object> map);


}
