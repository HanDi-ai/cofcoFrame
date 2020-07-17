package com.cofco.sys.entity.members;

/**
 * 联盟成员日志表
 * @author liyang
 * @date 2019-12-03 11:18:05
 */
public class AllianceMemberLogEntity {
    //ID
    private String id;
    //联盟成员表ID
    private String allianceMemberId;
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

    public String getAllianceMemberId() {
        return allianceMemberId;
    }

    public void setAllianceMemberId(String allianceMemberId) {
        this.allianceMemberId = allianceMemberId;
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
