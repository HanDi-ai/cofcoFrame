package com.cofco.sys.entity.members;

import java.util.Date;

/**
 * IOT数据类
 * @author handi
 * @date 2020 03/20 11:18:05
 */
public class BillIotEntity {
    //id
    String id;
    //设备类型
    String deviceType;
    //仓位id
    String locationId;
    //设备id
    String sn;
    //仓库id
    String scode;
    //iot设备返回值
    String iotValue;
    //创建时间
    Date createTime;
    //创建人
    String createUser;
    //修改时间
    Date updateTime;
    //修改人
    String updateUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getIotValue() {
        return iotValue;
    }

    public void setIotValue(String iotValue) {
        this.iotValue = iotValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
