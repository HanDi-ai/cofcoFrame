package com.cofco.sys.service.members.impl;

import com.cofco.sys.dao.members.BillDao;
import com.cofco.sys.entity.members.*;
import com.cofco.sys.service.members.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 仓单业务管理
 *
 * @author handi
 * @date 2019 11/05 15:45:50
 */
@Service("/BillService")
public class BillServiceImpl implements BillService {
    @Autowired
    private BillDao billDao;
    /**
     * 仓单备案信息查询
     * @return
     */
    @Override
    public List<BillEntity> queryList(Map<String, Object> map) {
        return billDao.queryList(map);
    }

    /**
     * 根据id号查询仓单信息
     */
    @Override
    public BillEntity queryObject(String id) {
        return billDao.queryObject(id);
    }

    /**
     * SEQ_Bill查询
     * @return
     */
    @Override
    public int seqBillId() {
        return billDao.seqBillId();
    }

    /**
     * SEQ_Bill_LOG查询
     * @return
     */
    @Override
    public int seqBillLogId() {
        return billDao.seqBillLogId();
    }

    /**
     *SEQ_BILL_OPERATOR查询
     * @return
     */
    @Override
    public int seqBillOperatorId() {
        return billDao.seqBillOperatorId();
    }

    /**
     * SEQ_BILL_OPERATOR_LOG查询
     * @return
     */
    @Override
    public int seqBillOperatorlogId() {
        return billDao.seqBillOperatorlogId();
    }

    /**
     * SEQ_BILL_IOT查询
     * @return
     */
    @Override
    public int seqBillIot() {
        return billDao.seqBillIot();
    }


    /**
     * 锁货通过后，查询operator表的信息
     */
    @Override
    public BillOperatorEntity queryPreFreezing(String id) {
        return billDao.queryPreFreezing(id);
    }

    /**
     * 总条数查询
     */
    @Override
    public int queryTotal(Map<String, Object> map) {
        return billDao.queryTotal(map);
    }

    /**
     *根据仓单id查询需传给大商所物联网接口的仓单基础信息
     */
    @Override
    public BillEntity queryIot(Map<String, Object> map) {
        return billDao.queryIot(map);
    }

    /**
     *W_BILL_IOT表数据查询
     */
    @Override
    public List<BillIotEntity> queryIotInfo(Map<String, Object> map) {
        return billDao.queryIotInfo(map);
    }

    /**
     * W_BILL_OPERATOR_LOG表中查询临时ID
     */
    @Override
    public String queryPrefreezeId(String id) {
        return billDao.queryPrefreezeId(id);
    }

    /**
     * 查询flow表中的 仓单id+仓单重量
     */
    @Override
    public List<BillFlowEntity> queryFlow(String id) {
        return billDao.queryFlow(id);
    }



    /**
     * 仓单备案提交
     *
     * @param billEntity
     * @return
     */
    @Override
    public void save(BillEntity billEntity) {
        billDao.save(billEntity);
       /* try{
            billDao.save(billEntity);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }*/
    }

    /**
     * 仓单备案临时ID入库
     * @param billLogEntity
     * @return
     */
    @Override
    public void saveOnrecordLog(BillLogEntity billLogEntity) {
        billDao.saveOnrecordLog(billLogEntity);
    }

    /**
     * 仓单操作临时ID入库(操作包含:预冻结，预冻结撤销，冻结)
     * @param billOperatorLogEntity
     */
    @Override
    public void saveOperation(BillOperatorLogEntity billOperatorLogEntity) {
            billDao.saveOperation(billOperatorLogEntity);
    }

    /**
     * iot数据表新增数据
     * @param billIotEntity
     * @return
     */
    @Override
    public void saveIot(BillIotEntity billIotEntity) {
        billDao.saveIot(billIotEntity);
    }


    /**
     * 仓单质押预冻结提交
     *
     * @param billOperatorEntity
     */
    @Override
    public void preFreezing(BillOperatorEntity billOperatorEntity) {
        billDao.preFreezing(billOperatorEntity);
    }

    /**
     * 仓单质押预冻结提交
     *
     * @param billFlowEntity
     */
    @Override
    public void preFreezingFlow(BillFlowEntity billFlowEntity) {
        billDao.preFreezingFlow(billFlowEntity);
    }

    /**
     * 修改操作类型状态
     *
     * @param billOperatorEntity
     * @return
     */
    @Override
    public void update(BillOperatorEntity billOperatorEntity) {
        billDao.update(billOperatorEntity);
    }

    /**
     * 修改联盟仓单临时ID
     *
     * @param billEntity
     * @return
     */
   /* @Override
    public void updateWbillTmpId(BillEntity billEntity) {
        billDao.updateWbillTmpId(billEntity);
    }*/
    /**
     * 修改联盟仓单ID
     * @param map
     */
    @Override
    public void updateWbillId(Map<String,Object> map) {
        billDao.updateWbillId(map);
    }

    /**
     * 修改W_BILL_LOG表状态值
     * @param billLogEntity
     * @return
     */
    @Override
    public void updateLog(BillLogEntity billLogEntity) {
        billDao.updateLog(billLogEntity);
    }
    /**
     * 将虚拟仓单ID更新入库及修改操作类型
     * @param map
     */
    @Override
    public void updateVir(Map<String,Object> map) {
        billDao.updateVir(map);
    }

    /**
     * 修改W_BILL_OPERATOR_LOG表状态值
     * @param billOperatorLogEntity
     * @return
     */
    @Override
    public void updateOperatorLog(BillOperatorLogEntity billOperatorLogEntity) {
        billDao.updateOperatorLog(billOperatorLogEntity);
    }

    /**
     * 修改W_BILL_OPERATOR_LOG表类型值
     * @param billOperatorLogEntity
     * @return
     */
    @Override
    public void updateOperatorType(BillOperatorLogEntity billOperatorLogEntity) {
        billDao.updateOperatorType(billOperatorLogEntity);
    }

    /**
     * 删除W_BILL_FLOW表数值
     * @param billFlowEntity
     * @return
     */
    @Override
    public void delFlow(BillFlowEntity billFlowEntity) {
        billDao.delFlow(billFlowEntity);
    }

    /**
     * 删除W_BILL_FLOW表数值,当预冻结被驳回时
     * @param billFlowEntity
     * @return
     */
    @Override
    public void preFreedelFlow(BillFlowEntity billFlowEntity) {
        billDao.preFreedelFlow(billFlowEntity);
    }


    @Override
    public int delete(Map<String, Object> map) {
        return billDao.delete(map);
    }

    /**
     * 仓单质押预冻结撤销提交
     */
    /*public void delete(BillOperatorEntity billOperatorEntity) {
        billDao.delete(billOperatorEntity);
    }*/


}
