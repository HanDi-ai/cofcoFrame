package com.cofco.sys.entity.members;

import java.io.Serializable;
import java.util.Date;

/**
 * 联盟客户
 * @author liyang
 * @date 2019-10-29 13:42:42
 */
public class AllianceClientEntity implements Serializable {
    // ID
    private String id;
    // 联盟客户号
    private String clientId;
    // 联盟成员ID
    private String memberId;
    // 公司名称
    private String clientName;
    // 公司地址
    private String clientAddr;
    // 公司电话
    private String clientTel;
    // 公司组织机构代码
    private String clientOrgNo;
    // 公司营业执照附件
    private byte[] clientOrgAttach;
    // 注册资金
    private String regMoney;
    // 开户银行信息
    private String bankInfo;
    // 单位性质
    private String property;
    // 单位性质名称
    private String propertyName;
    // 法人代表姓名
    private String corporateName;
    // 法人代表电话
    private String corporateTel;
    // 法人代表身份证号码
    private String corporateId;
    // 法人代表身份证附件
    private byte[] corporateIdAttach;
    // 联系人姓名
    private String contactName;
    // 联系人电话
    private String contactTel;
    // 联系人身份证号码
    private String contactId;
    // 联系人身份证附件
    private byte[] contactIdAttach;
    // 版本
    private String version;
    // 创建时间
    private Date timeCreate;
    // 创建人
    private String creator;
    // 修改时间
    private Date timeModify;
    // 修改人
    private String modifier;
    // 注销标识
    private String isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(String clientAddr) {
        this.clientAddr = clientAddr;
    }

    public String getClientTel() {
        return clientTel;
    }

    public void setClientTel(String clientTel) {
        this.clientTel = clientTel;
    }

    public String getClientOrgNo() {
        return clientOrgNo;
    }

    public void setClientOrgNo(String clientOrgNo) {
        this.clientOrgNo = clientOrgNo;
    }

    public byte[] getClientOrgAttach() {
        return clientOrgAttach;
    }

    public void setClientOrgAttach(byte[] clientOrgAttach) {
        this.clientOrgAttach = clientOrgAttach;
    }

    public String getRegMoney() {
        return regMoney;
    }

    public void setRegMoney(String regMoney) {
        this.regMoney = regMoney;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCorporateTel() {
        return corporateTel;
    }

    public void setCorporateTel(String corporateTel) {
        this.corporateTel = corporateTel;
    }

    public String getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(String corporateId) {
        this.corporateId = corporateId;
    }

    public byte[] getCorporateIdAttach() {
        return corporateIdAttach;
    }

    public void setCorporateIdAttach(byte[] corporateIdAttach) {
        this.corporateIdAttach = corporateIdAttach;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public byte[] getContactIdAttach() {
        return contactIdAttach;
    }

    public void setContactIdAttach(byte[] contactIdAttach) {
        this.contactIdAttach = contactIdAttach;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getTimeModify() {
        return timeModify;
    }

    public void setTimeModify(Date timeModify) {
        this.timeModify = timeModify;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
