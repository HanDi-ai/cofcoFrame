package com.cofco.sys.entity.members;

import java.util.Date;

/**
 * 仓单操作流水表
 * @author liyang
 * @date 2019 11/05 11:18:05
 */
public class BillFlowEntity {
    //ID
    private String id;
    //仓单操作记录表ID
    private String billOperatorId;
    //联盟成员ID
    private String memberId;
    //联盟客户ID
    private String clientId;
    //联盟仓单ID
    private String wbillId;
    //仓单状态
    private String wbillStatus;
    //流水时间
    private Date timeCreate;
    //仓单重量
    private String weight;
    //注销标识
    private String isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillOperatorId() {
        return billOperatorId;
    }

    public void setBillOperatorId(String billOperatorId) {
        this.billOperatorId = billOperatorId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWbillId() {
        return wbillId;
    }

    public void setWbillId(String wbillId) {
        this.wbillId = wbillId;
    }

    public String getWbillStatus() {
        return wbillStatus;
    }

    public void setWbillStatus(String wbillStatus) {
        this.wbillStatus = wbillStatus;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getWeight() { return weight; }

    public void setWeight(String weight) { this.weight = weight; }

    public String getIsDelete() { return isDelete; }

    public void setIsDelete(String isDelete) { this.isDelete = isDelete; }
}
