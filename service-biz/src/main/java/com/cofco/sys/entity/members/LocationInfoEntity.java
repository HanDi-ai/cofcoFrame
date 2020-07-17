package com.cofco.sys.entity.members;

import java.util.List;

/**
 * 垛位信息实体类
 * @author Handi
 * @date 2020-04-08 14:21:42
 */

public class LocationInfoEntity {

    /**
     * positionId : 11
     * locationId : 33
     * positionType : 1
     * maxCapacity : 10000
     * weight : 1522.4
     * initCapacity : [{"whWbillId":"wh00003","weight":0}]
     * baseSize : 100
     * maxHeight : 10
     * iotDevices : [{"deviceId":"D0CF5EFFFEB64AA3_2821051994031645","deviceType":"1","macAddr":"1.2.3.4","iotValue":"-6.56","rawValue":"-6.56"},{"deviceId":"D0CF5EFFFEB64AA3_28210519943B2123","deviceType":"1","macAddr":"1.2.3.4","iotValue":"-7.75","rawValue":"-7.75"},{"deviceId":"D0CF5EFFFEB64AA3_28210519976CC9F5","deviceType":"1","macAddr":"1.2.3.4","iotValue":"-4.56","rawValue":"-4.56"},{"deviceId":"D0CF5EFFFEB64AA3_282107199414185B","deviceType":"1","macAddr":"1.2.3.4","iotValue":"-9.06","rawValue":"-9.06"},{"deviceId":"581749033","deviceType":"0","macAddr":"1.2.3.4","iotValue":"01031A3B7800003B7800000EDE00010001000000000000000000030000FDF6","rawValue":"{'1-40':'0103F0012C008C089C014D008C0877016E008C0AA7018F008C0B1301B0008C0B6B01D1008C0B5E01F2008C0B70012C00AD0B75014D00AD0B99016E00AD0BA3018F00AD0BA201B000AD0BA001D100AD0B9F01F200AD0B9E012C00CE0BB0014D00CE0BB0016E00CE0BB0018F00CE0BB001B000CE0BB001D100CE0BB001F200CE0BB0012C00EF0BB7014D00EF0BB7016E00EF0BB7018F00EF0BB701B000EF0BB701D100EF0BB701F200EF0BB7012C01100BB7014D01100BB7016E01100BB7018F01100BB701B001100BB701D101100BB701F201100BB7012C006B0889014D006B0A32016E006B0AFC018F006B0B5701B0006B0B119194','41-80':'0103F001D1006B093501F2006B0913012C004A0876014D004A0874016E004A086A018F004A085E01B0004A08EF01D1004A08F801F2004A08D7012C00290981014D00290866016E00290861018F002908B501B00029084C01D10029084001F20029089B012C0008097D014D00080906016E000808E5018F000808E901B0000808A201D10008088001F20008085F010B006B0BB800EA006B0BB800C9006B0BB800A8006B0BB80087006B0BB80066006B0BB80045006B0BB80024006B0BB80003006B0BB8010B004A0BB800EA004A0BB800C9004A0BB800A8004A0BB80087004A0BB80066004A0BB80045004A0BB80024004A0BB80573'}"}]
     */
    //仓单ID
    private String positionId;
    //垛位ID
    private String locationId;
    //仓位类型
    private String positionType;
    //最大存储量
    private String maxCapacity;
    //重量
    private String weight;
    //底面积
    private int baseSize;
    //最大高度
    private int maxHeight;
    //初始容量
    private List<InitCapacityBean> initCapacity;
    //iot设备参数
    private List<IotDevicesBean> iotDevices;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(int baseSize) {
        this.baseSize = baseSize;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public List<InitCapacityBean> getInitCapacity() {
        return initCapacity;
    }

    public void setInitCapacity(List<InitCapacityBean> initCapacity) {
        this.initCapacity = initCapacity;
    }

    public List<IotDevicesBean> getIotDevices() {
        return iotDevices;
    }

    public void setIotDevices(List<IotDevicesBean> iotDevices) {
        this.iotDevices = iotDevices;
    }

    public static class InitCapacityBean {
        /**
         * whWbillId : wh00003
         * weight : 0
         */

        private String whWbillId;
        private int weight;

        public String getWhWbillId() {
            return whWbillId;
        }

        public void setWhWbillId(String whWbillId) {
            this.whWbillId = whWbillId;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    public static class IotDevicesBean {
        /**
         * deviceId : D0CF5EFFFEB64AA3_2821051994031645
         * deviceType : 1
         * macAddr : 1.2.3.4
         * iotValue : -6.56
         * rawValue : -6.56
         */
        //设备ID
        private String deviceId;
        //设备类型
        private String deviceType;
        //mac地址
        private String macAddr;
        //若是温度仪就填写温度，若是扫描仪就填写体积和重量的十六进制值
        private String iotValue;
        //若是温度仪就填写温度,若是扫描仪就填写全部坐标点的具体坐标值
        private String rawValue;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getMacAddr() {
            return macAddr;
        }

        public void setMacAddr(String macAddr) {
            this.macAddr = macAddr;
        }

        public String getIotValue() {
            return iotValue;
        }

        public void setIotValue(String iotValue) {
            this.iotValue = iotValue;
        }

        public String getRawValue() {
            return rawValue;
        }

        public void setRawValue(String rawValue) {
            this.rawValue = rawValue;
        }
    }
}
