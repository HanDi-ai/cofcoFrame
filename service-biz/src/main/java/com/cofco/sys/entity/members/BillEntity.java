package com.cofco.sys.entity.members;

import oracle.sql.CLOB;

import java.util.Date;

/**
 * 仓单实体类
 * @author handi
 * @date 2019 11/05 10:26:05
 */
public class BillEntity {
    //ID
    private int id;
    //联盟成员ID
    private String whMemberId;
    //仓单类型
    private String wbillType;
    //是否标准仓单
    private String isStandard;
    //仓库仓单编号
    private String whWbillId;
    //品种代码
    private String varietyId;
    //品种名称
    private String varietyName;
    //是否包装
    private int packageType;
    //数量
    private String counts;
    //重量
    private String weight;
    //重量Check
    private String weightChk;
    //是否标记
    private int isMarked;
    //标记内容
    private String markContent;
    //产地
    private String productArea;
    //品质（先做一个品种）
    private String quality;
    //检验检疫是否生效
    private int isInspected;
    //检验机构名称
    private String inspectionOrgName;
    //检验机构组织机构代码
    private String inspectionOrgNo;
    //检验检疫证明
    private String inspectionAttach;
    //检验检疫生效日期
    private String inspectedBeginDate;
    //检验检疫失效日期
    private String inspectedEndDate;
    //垛位信息
    private String locationInfo;
    //仓储起始日期
    private String storeBeginDate;
    //仓储终止日期
    private String storeEndDate;
    //仓储费用
    private String storageFee;
    //损耗标准
    private String lossStandard;
    //仓储机构经办人姓名
    private String operatorName;
    //仓储机构经办人电话
    private String operatorTel;
    //货主联盟客户代码
    private String clientId;
    //货主联系人姓名
    private String clientContactName;
    //货主联系人电话
    private String clientContactTel;
    //是否有保险
    private int isInsurance;
    //保险公司名称
    private String insuranceOrg;
    //保险公司组织机构代码
    private String insuranceOrgNo;
    //保险公司联系人姓名
    private String insuranceContact;
    //保险公司联系人电话
    private String insuranceContactTel;
    //保险生效日期
    private String insuranceBeginDate;
    //保险失效日期
    private String insuranceEndDate;
    //版本
    private String version;
    //创建时间
    private Date time_Create;
    //创建人
    private String creator;
    //修改时间
    private Date time_Modify;
    //修改人
    private String modifier;
    //注销标识
    private int is_Delete;
    //操作类型
    private String operatorType;
    // 质押重量
    private String pledgedWeight;
    //联盟仓单ID
    private String wbillId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWhMemberId() {
        return whMemberId;
    }

    public void setWhMemberId(String whMemberId) {
        this.whMemberId = whMemberId;
    }

    public String getWbillType() {
        return wbillType;
    }

    public void setWbillType(String wbillType) {
        this.wbillType = wbillType;
    }

    public String getIsStandard() {
        return isStandard;
    }

    public void setIsStandard(String isStandard) {
        this.isStandard = isStandard;
    }

    public String getWhWbillId() {
        return whWbillId;
    }

    public void setWhWbillId(String whWbillId) {
        this.whWbillId = whWbillId;
    }

    public String getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(String varietyId) {
        this.varietyId = varietyId;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightChk() {
        return weightChk;
    }

    public void setWeightChk(String weightChk) {
        this.weightChk = weightChk;
    }

    public int getIsMarked() {
        return isMarked;
    }

    public void setIsMarked(int isMarked) {
        this.isMarked = isMarked;
    }

    public String getMarkContent() {
        return markContent;
    }

    public void setMarkContent(String markContent) {
        this.markContent = markContent;
    }

    public String getProductArea() {
        return productArea;
    }

    public void setProductArea(String productArea) {
        this.productArea = productArea;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getIsInspected() {
        return isInspected;
    }

    public void setIsInspected(int isInspected) {
        this.isInspected = isInspected;
    }

    public String getInspectionOrgName() {
        return inspectionOrgName;
    }

    public void setInspectionOrgName(String inspectionOrgName) {
        this.inspectionOrgName = inspectionOrgName;
    }

    public String getInspectionOrgNo() {
        return inspectionOrgNo;
    }

    public void setInspectionOrgNo(String inspectionOrgNo) {
        this.inspectionOrgNo = inspectionOrgNo;
    }

    public String getInspectionAttach() {
        return inspectionAttach;
    }

    public void setInspectionAttach(String inspectionAttach) {
        this.inspectionAttach = inspectionAttach;
    }

    public String getInspectedBeginDate() {
        return inspectedBeginDate;
    }

    public void setInspectedBeginDate(String inspectedBeginDate) {
        this.inspectedBeginDate = inspectedBeginDate;
    }

    public String getInspectedEndDate() {
        return inspectedEndDate;
    }

    public void setInspectedEndDate(String inspectedEndDate) {
        this.inspectedEndDate = inspectedEndDate;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getStoreBeginDate() {
        return storeBeginDate;
    }

    public void setStoreBeginDate(String storeBeginDate) {
        this.storeBeginDate = storeBeginDate;
    }

    public String getStoreEndDate() {
        return storeEndDate;
    }

    public void setStoreEndDate(String storeEndDate) {
        this.storeEndDate = storeEndDate;
    }

    public String getStorageFee() {
        return storageFee;
    }

    public void setStorageFee(String storageFee) {
        this.storageFee = storageFee;
    }

    public String getLossStandard() {
        return lossStandard;
    }

    public void setLossStandard(String lossStandard) {
        this.lossStandard = lossStandard;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientContactName() {
        return clientContactName;
    }

    public void setClientContactName(String clientContactName) {
        this.clientContactName = clientContactName;
    }

    public String getClientContactTel() {
        return clientContactTel;
    }

    public void setClientContactTel(String clientContactTel) {
        this.clientContactTel = clientContactTel;
    }

    public int getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(int isInsurance) {
        this.isInsurance = isInsurance;
    }

    public String getInsuranceOrg() {
        return insuranceOrg;
    }

    public void setInsuranceOrg(String insuranceOrg) {
        this.insuranceOrg = insuranceOrg;
    }

    public String getInsuranceOrgNo() {
        return insuranceOrgNo;
    }

    public void setInsuranceOrgNo(String insuranceOrgNo) {
        this.insuranceOrgNo = insuranceOrgNo;
    }

    public String getInsuranceContact() {
        return insuranceContact;
    }

    public void setInsuranceContact(String insuranceContact) {
        this.insuranceContact = insuranceContact;
    }

    public String getInsuranceContactTel() {
        return insuranceContactTel;
    }

    public void setInsuranceContactTel(String insuranceContactTel) {
        this.insuranceContactTel = insuranceContactTel;
    }

    public String getInsuranceBeginDate() {
        return insuranceBeginDate;
    }

    public void setInsuranceBeginDate(String insuranceBeginDate) {
        this.insuranceBeginDate = insuranceBeginDate;
    }

    public String getInsuranceEndDate() {
        return insuranceEndDate;
    }

    public void setInsuranceEndDate(String insuranceEndDate) {
        this.insuranceEndDate = insuranceEndDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTime_Create() {
        return time_Create;
    }

    public void setTime_Create(Date time_Create) {
        this.time_Create = time_Create;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getTime_Modify() {
        return time_Modify;
    }

    public void setTime_Modify(Date time_Modify) {
        this.time_Modify = time_Modify;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public int getIs_Delete() {
        return is_Delete;
    }

    public void setIs_Delete(int is_Delete) {
        this.is_Delete = is_Delete;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getPledgedWeight() {
        return pledgedWeight;
    }

    public void setPledgedWeight(String pledgedWeight) {
        this.pledgedWeight = pledgedWeight;
    }

    public String getWbillId() {
        return wbillId;
    }

    public void setWbillId(String wbillId) {
        this.wbillId = wbillId;
    }
}
