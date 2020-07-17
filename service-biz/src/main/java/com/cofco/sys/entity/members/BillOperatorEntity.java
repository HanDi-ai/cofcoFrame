package com.cofco.sys.entity.members;

/**
 * 仓单操作记录表
 * @author liyang
 * @date 2019 11/05 11:18:05
 */
public class BillOperatorEntity {
    //ID
    private String id;
    //操作类型
    private String operatorType;
    //虚拟仓单ID
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
    //质权人联盟成员代码
    private String pledgeeMemberId;

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

    public String getVirtualWbillId() {
        return virtualWbillId;
    }

    public void setVirtualWbillId(String virtualWbillId) {
        this.virtualWbillId = virtualWbillId;
    }

    public String getPledgerMemberId() { return pledgerMemberId; }

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

    public void setPledgeContractAttach(byte[] pledgeContractAttach) { this.pledgeContractAttach = pledgeContractAttach; }

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

    public String getIsDelete() { return isDelete; }

    public void setIsDelete(String isDelete) { this.isDelete = isDelete; }

    public String getPledgeeMemberId() {
        return pledgeeMemberId;
    }

    public void setPledgeeMemberId(String pledgeeMemberId) {
        this.pledgeeMemberId = pledgeeMemberId;
    }
}
