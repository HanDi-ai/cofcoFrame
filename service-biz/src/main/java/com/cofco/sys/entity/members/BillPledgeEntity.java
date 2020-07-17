package com.cofco.sys.entity.members;

import java.util.Date;

public class BillPledgeEntity {
    //ID
    private String id;
    //操作类型
    private String operatorType;
    //操作类型Code
    private String operatorTypeCode;
    //虚拟仓单号
    private String virtualWbillId;
    //出质人联盟成员ID
    private String pledgerMemberId;
    //成员类型
    private String memberType;
    //联盟客户ID
    private String clientId;
    //质押合同ID
    private String pledgerContractId;
    //经办人姓名
    private String operatorName;
    //经办人联系电话
    private String operatorTel;
    //经办人身份证号码
    private String operatorId;
    //经办人身份证复印件
    private byte[] operatorIdAttach;
    //质押融资协议附件
    private byte[] pledgeContractAttach;
    //质押协议生效日期
    private String pledgeBeginDate;
    //质押协议失效日期
    private String pledgeEndDate;
    //版本
    private String version;
    //注销标识
    private String isDelete;
    //仓单操作记录表ID
    private String billOperatorId;
    //流水时间
    private Date timeCreate;
    //仓单重量
    private String weight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperatorTypeCode() {
        return operatorTypeCode;
    }

    public void setOperatorTypeCode(String operatorTypeCode) {
        this.operatorTypeCode = operatorTypeCode;
    }

    public String getVirtualWbillId() {
        return virtualWbillId;
    }

    public void setVirtualWbillId(String virtualWbillId) {
        this.virtualWbillId = virtualWbillId;
    }

    public String getPledgerMemberId() {
        return pledgerMemberId;
    }

    public void setPledgerMemberId(String pledgerMemberId) {
        this.pledgerMemberId = pledgerMemberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPledgerContractId() {
        return pledgerContractId;
    }

    public void setPledgerContractId(String pledgerContractId) {
        this.pledgerContractId = pledgerContractId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorTel() {
        return operatorTel;
    }

    public void setOperatorTel(String operatorTel) {
        this.operatorTel = operatorTel;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public byte[] getOperatorIdAttach() {
        return operatorIdAttach;
    }

    public void setOperatorIdAttach(byte[] operatorIdAttach) {
        this.operatorIdAttach = operatorIdAttach;
    }

    public byte[] getPledgeContractAttach() {
        return pledgeContractAttach;
    }

    public void setPledgeContractAttach(byte[] pledgeContractAttach) {
        this.pledgeContractAttach = pledgeContractAttach;
    }

    public String getPledgeBeginDate() {
        return pledgeBeginDate;
    }

    public void setPledgeBeginDate(String pledgeBeginDate) {
        this.pledgeBeginDate = pledgeBeginDate;
    }

    public String getPledgeEndDate() {
        return pledgeEndDate;
    }

    public void setPledgeEndDate(String pledgeEndDate) {
        this.pledgeEndDate = pledgeEndDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getBillOperatorId() {
        return billOperatorId;
    }

    public void setBillOperatorId(String billOperatorId) {
        this.billOperatorId = billOperatorId;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
