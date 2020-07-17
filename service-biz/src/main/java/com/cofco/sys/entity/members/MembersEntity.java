package com.cofco.sys.entity.members;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Date;

/**
 * 联盟成员注册
 * ALLIANCE_MEMBER 数据库表
 * @author handi
 * @date 2019/10/28 16:20:48
 */
public class MembersEntity implements Serializable {
    //ID
    private String id;
    //临时ID
    private String whtmpid;
    //是否指定交割仓库
    private String isDelivery;
    //仓库类型
    private String whType;
    //库名称
    private String whName;
    //库简称
    private String whAbbr;
    //注册地址
    private String regAddr;
    //办公地址
    private String officeAddr;
    //联系电话
    private String tel;
    //组织机构代码
    private String orgNo;
    //经营性质
    private String property;
    //产品类型
    private String productType;
    //产品种类
    private String productCategory;
    //开户银行
    private String bankName;
    //银行账号
    private String bankAccount;
    //注册资金
    private String regMoney;
    //总资产
    private String totalAsset;
    //固定资产
    private String fixedAsset;
    //净资产
    private String netAsset;
    //法人代表姓名
    private String corporateName;
    //法人代表电话
    private String corporateTel;
    //法人代表身份证号码
    private String corporateId;
    //联系人姓名
    private String contactName;
    //联系人电话
    private String contactTel;
    //联系人身份证号码
    private String contactId;
    //是否为租赁库
    private String isLease;
    //租赁起始日期
    private String leaseBeginDate;
    //租赁终止日期
    private String leaseEndDate;
    //库区面积
    private String regionArea;
    //库房面积
    private String houseArea;
    //水泥硬化地面面积
    private String cementArea;
    //总堆储能力
    private String totHeapQty;
    //期货堆储能力
    private String ftrHeapQty;
    //散粮堆储能力
    private String bulkHeapQty;
    //地磅吨数
    private String loadometerTon;
    //上级仓库类型
    private String upperType;
    //上级仓库名称
    private String upperName;
    //上级仓库简称
    private String upperAbbr;
    //上级仓库联系电话
    private String upperTel;
    //上级仓库地址
    private String upperAddr;
    //上级仓库组织机构代码
    private String upperOrgNo;
    //上级仓库法人代表姓名
    private String upperCorporateName;
    //大商所提供的联盟成员公钥
    private String publicKey;
    //版本
    private String version;
    //创建时间
    private Date time_create;
    //创建人
    private String creator;
    //修改时间
    private Date time_modify;
    //修改人
    private String modifier;
    //注销标识
    private String is_delete;
    //联盟成员号
    private String memberId;
    //仓库自己生成的公钥
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "ZMEMPUBLICKEY", columnDefinition = "CLOB", nullable = true)
    private String zmemPublicKey;
    //仓库自己生成的私钥
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "ZMEMPRIVATEKEY", columnDefinition = "CLOB", nullable = true)
    private String zmemPrivateKey;
    //营业执照
    private byte[] orgAttach;
    //法人代表身份证附件
    private byte[] corporateIdAttach;
    //联系人身份证附件
    private byte[] contactIdAttach;
    //库平面图
    private byte[] positionDiagram;
    //上级仓库营业执照
    private byte[] upperOrgAttach;
    //联盟成员当前所处状态
    private String status;

    //随机字符串
    //private String nonce;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWhtmpid() {
        return whtmpid;
    }

    public void setWhtmpid(String whtmpid) {
        this.whtmpid = whtmpid;
    }

    public String getIsDelivery() {
        return isDelivery;
    }

    public void setIsDelivery(String isDelivery) {
        this.isDelivery = isDelivery;
    }

    public String getWhType() {
        return whType;
    }

    public void setWhType(String whType) {
        this.whType = whType;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getWhAbbr() {
        return whAbbr;
    }

    public void setWhAbbr(String whAbbr) {
        this.whAbbr = whAbbr;
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr;
    }

    public String getOfficeAddr() {
        return officeAddr;
    }

    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public byte[] getOrgAttach() {
        return orgAttach;
    }

    public void setOrgAttach(byte[] orgAttach) {
        this.orgAttach = orgAttach;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getRegMoney() {
        return regMoney;
    }

    public void setRegMoney(String regMoney) {
        this.regMoney = regMoney;
    }

    public String getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(String totalAsset) {
        this.totalAsset = totalAsset;
    }

    public String getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(String fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public String getNetAsset() {
        return netAsset;
    }

    public void setNetAsset(String netAsset) {
        this.netAsset = netAsset;
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

    public String getIsLease() {
        return isLease;
    }

    public void setIsLease(String isLease) {
        this.isLease = isLease;
    }

    public String getLeaseBeginDate() {
        return leaseBeginDate;
    }

    public void setLeaseBeginDate(String leaseBeginDate) {
        this.leaseBeginDate = leaseBeginDate;
    }

    public String getLeaseEndDate() {
        return leaseEndDate;
    }

    public void setLeaseEndDate(String leaseEndDate) {
        this.leaseEndDate = leaseEndDate;
    }

    public String getRegionArea() {
        return regionArea;
    }

    public void setRegionArea(String regionArea) {
        this.regionArea = regionArea;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public String getCementArea() {
        return cementArea;
    }

    public void setCementArea(String cementArea) {
        this.cementArea = cementArea;
    }

    public String getTotHeapQty() {
        return totHeapQty;
    }

    public void setTotHeapQty(String totHeapQty) {
        this.totHeapQty = totHeapQty;
    }

    public String getFtrHeapQty() {
        return ftrHeapQty;
    }

    public void setFtrHeapQty(String ftrHeapQty) {
        this.ftrHeapQty = ftrHeapQty;
    }

    public String getBulkHeapQty() {
        return bulkHeapQty;
    }

    public void setBulkHeapQty(String bulkHeapQty) {
        this.bulkHeapQty = bulkHeapQty;
    }

    public String getLoadometerTon() {
        return loadometerTon;
    }

    public void setLoadometerTon(String loadometerTon) {
        this.loadometerTon = loadometerTon;
    }

    public byte[] getPositionDiagram() {
        return positionDiagram;
    }

    public void setPositionDiagram(byte[] positionDiagram) {
        this.positionDiagram = positionDiagram;
    }

    public String getUpperType() {
        return upperType;
    }

    public void setUpperType(String upperType) {
        this.upperType = upperType;
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(String upperName) {
        this.upperName = upperName;
    }

    public String getUpperAbbr() {
        return upperAbbr;
    }

    public void setUpperAbbr(String upperAbbr) {
        this.upperAbbr = upperAbbr;
    }

    public String getUpperTel() {
        return upperTel;
    }

    public void setUpperTel(String upperTel) {
        this.upperTel = upperTel;
    }

    public String getUpperAddr() {
        return upperAddr;
    }

    public void setUpperAddr(String upperAddr) {
        this.upperAddr = upperAddr;
    }

    public String getUpperOrgNo() {
        return upperOrgNo;
    }

    public void setUpperOrgNo(String upperOrgNo) {
        this.upperOrgNo = upperOrgNo;
    }

    public byte[] getUpperOrgAttach() {
        return upperOrgAttach;
    }

    public void setUpperOrgAttach(byte[] upperOrgAttach) {
        this.upperOrgAttach = upperOrgAttach;
    }

    public String getUpperCorporateName() {
        return upperCorporateName;
    }

    public void setUpperCorporateName(String upperCorporateName) {
        this.upperCorporateName = upperCorporateName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTime_create() {
        return time_create;
    }

    public void setTime_create(Date time_create) {
        this.time_create = time_create;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getTime_modify() {
        return time_modify;
    }

    public void setTime_modify(Date time_modify) {
        this.time_modify = time_modify;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getZmemPublicKey() {
        return zmemPublicKey;
    }

    public void setZmemPublicKey(String zmemPublicKey) {
        this.zmemPublicKey = zmemPublicKey;
    }

    public String getZmemPrivateKey() {
        return zmemPrivateKey;
    }

    public void setZmemPrivateKey(String zmemPrivateKey) {
        this.zmemPrivateKey = zmemPrivateKey;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
