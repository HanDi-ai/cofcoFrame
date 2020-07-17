package com.cofco.constant;


/**
 * Log日志表操作类型状态码
 * @auth handi
 * @date 2019 12/03 10:05:01
 */
public class LogOper {
    //联盟成员注册回调状态值
   public static final String MEMBER_SAVE = "1";
    //联盟成员修改回调状态值
    public static final String MEMBER_UPDATE = "2";
    //联盟成员注销回调状态值
    public static final String MEMBER_DELETE = "3";
    //联盟客户注册回调状态值
    public static final String CLIENT_SAVE = "4";
    //联盟客户修改回调状态值
    public static final String CLIENT_UPDATE = "5";
    //联盟客户注销回调状态值
    public static final String CLIENT_DELETE = "6";
    //仓单备案回调状态值
    public static final String BILL_SAVE = "7";
    //仓单质押预冻结回调状态值
    public static final String PREFREEZING_SUB = "8";
    //仓单质押预冻结撤销回调状态值
    public static final String REVOKE_PREFREEZING = "9";
    //仓单质押冻结回调状态值
    public static final String FREEZING = "10";
    //仓单质押预解质回调状态值
    public static final String PRE_UNFREEZETX = "11";
    //仓单质押预解质撤销回调状态值
    public static final String PRE_UNFREEZE_CANCEL = "12";
    //仓单质押解质回调状态值
    public static final String UNFREEZETX = "13";
    //仓单注销回调状态值
    public static final String BILL_LOGOF = "14";
}
