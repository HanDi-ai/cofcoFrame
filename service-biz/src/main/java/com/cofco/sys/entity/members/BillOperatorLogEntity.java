package com.cofco.sys.entity.members;

/**
 * 仓单操作记录日志表
 * @author liyang
 * @date 2019-12-03 11:18:05
 */
public class BillOperatorLogEntity {
    //ID
    private String id;
    //仓单操作记录表ID
    private String wbillOperatorId;
    //临时ID
    private String tempId;
    //操作类型
    private String operatorType;
    //审核状态
    private String auditStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWbillOperatorId() {
        return wbillOperatorId;
    }

    public void setWbillOperatorId(String wbillOperatorId) {
        this.wbillOperatorId = wbillOperatorId;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
