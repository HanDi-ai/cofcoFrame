package com.cofco.sys.entity.members;

/**
 * 仓单日志表
 * @author liyang
 * @date 2019-12-03 11:18:05
 */
public class BillLogEntity {
    //ID
    private String id;
    //仓单表ID
    private String wbillId;
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

    public String getWbillId() {
        return wbillId;
    }

    public void setWbillId(String wbillId) {
        this.wbillId = wbillId;
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
