package com.cofco.sys.entity.user;

import com.cofco.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 用户权限表
 * 
 * @author qixin
 * @email 237360279@qq.com
 * @date 2018-05-04 09:41:38
 */
public class UserLocationEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//id主键
	private String relationId;
	//位置id
	private String locationId;
	//用户id
	private String id;
	//创建人
	private String createPerson;
	//创建时间
	private Date createTime;
	//有效标识
	private double delflag;
	//版本
	private double version;
	//用户Id List
	private List<String> locationIdList;
	//操作标识
	private double operateFlag;
	//操作时间
	private Date operationTime;
	//操作人
	private String operationPerson;
	//数据标识
	private double dataFlag;
	//入库时间
	private Date inDbTime;
	//组织id
	private String scode;
	//组织id
	private String success;
	//组织id
	private String message;
	//用户ID
	private String userId;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public double getDelflag() {
		return delflag;
	}

	public void setDelflag(double delflag) {
		this.delflag = delflag;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public List<String> getLocationIdList() {
		return locationIdList;
	}

	public void setLocationIdList(List<String> locationIdList) {
		this.locationIdList = locationIdList;
	}

	public double getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(double operateFlag) {
		this.operateFlag = operateFlag;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getOperationPerson() {
		return operationPerson;
	}

	public void setOperationPerson(String operationPerson) {
		this.operationPerson = operationPerson;
	}

	public double getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(double dataFlag) {
		this.dataFlag = dataFlag;
	}

	public Date getInDbTime() {
		return inDbTime;
	}

	public void setInDbTime(Date inDbTime) {
		this.inDbTime = inDbTime;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
