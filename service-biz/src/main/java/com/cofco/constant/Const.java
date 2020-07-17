package com.cofco.constant;

/**
 * 仓单质押状态码
 *
 * @author handi
 * @date 2019 11/06 16:28:05
 */
public class Const {
    //0.质押预冻结已提交，交易所审核中
    public static final String PLEDGE_PRE_FREEZING = "0";
    //1.质押预冻结撤销已提交，交易所审核中
    public static final String PLEDGE_PRE_FREEZING_REVOKE = "1";
    //2.质押冻结已提交，交易所审核中
    public static final String PLEDGE_FREEZING = "2";
    //3.转质押
    public static final String TRANSFERRED_PLEDGE = "3";
    //4.转质押撤销
    public static final String TRANSFERRED_PLEDGE_REVOKE = "4";
    //5.质押预解质已提交，交易所审核中
    public static final String PLEDGE_PRE_UNFREEZE = "5";
    //6.质押预解质撤销已提交，交易所审核中
    public static final String PLEDGE_PRE_UNFREEZE_REVOKE = "6";
    //7.质押解质已提交，交易所审核中
    public static final String PLEDGE_UNFREEZE = "7";
    //8.过户预冻结
    public static final String TRANSFER_PRE_FREEZING = "8";
    //9.过户预冻结撤销
    public static final String TRANSFER_PRE_FREEZING_REVOKE = "9";
    //10.过户
    public static final String TRANSFER = "10";
    //11.违约处理结果备案
    public static final String DEFAULT_RESULTS_RECORD = "11";
    //12.其他
    public static final String OTHER = "99";
    //0: 否
    public static final String DELETE_FLG_NO = "0";
    //1: 是
    public static final String DELETE_FLG_YES = "1";
    //200: 响应码
    public static final String STATUS_CODE_200 = "200";
    //201: 响应码
    public static final String STATUS_CODE_201 = "201";
    //202: 响应码
    public static final String STATUS_CODE_202 = "202";
    //12锁货中
    public static final String LOCKING = "12";
    //13质押预冻结提交，仓库锁货中
    public static final String PRE_FREEZING = "13";
    //15.质押预解质中
    public static final String PLEDGE_PRE_UNFREEZING = "15";
    //16.质押预解质撤销中
    public static final String PLEDGE_PRE_UNFREEZE_REVOKING = "16";
    //17.质押解质中
    public static final String PLEDGE_UNFREEZING = "17";
    //18.预冻结成功
    public static final String PRE_FREEZING_SUCCESS = "18";
    //19.预冻结被失败，交易所驳回
    public static final String PRE_FREEZING_FAIL= "19";
    //20.质押预冻结撤销成功
    public static final String PLEDGE_PRE_FREEZING_REVOKE_SUCCESS = "20";
    //21.质押预冻结撤销失败，被交易所驳回
    public static final String PLEDGE_PRE_FREEZING_REVOKE_FAIL = "21";
    //22.质押冻结成功
    public static final String PLEDGE_FREEZING_SUCCESS = "22";
    //23.质押冻结失败，被交易所驳回
    public static final String PLEDGE_FREEZING_FAIL= "23";
    //24.质押预解质成功
    public static final String PLEDGE_PRE_UNFREEZE_SUCCESS = "24";
    //25.质押预解质失败，被交易所驳回
    public static final String PLEDGE_PRE_UNFREEZE_FAIL= "25";
    //26.质押预解质撤销成功
    public static final String PLEDGE_PRE_UNFREEZE_REVOKE_SUCCESS = "26";
    //27.质押预解质撤销失败，被交易所驳回
    public static final String PLEDGE_PRE_UNFREEZE_REVOKE_FAIL= "27";
    //28.质押解质成功
    public static final String PLEDGE_UNFREEZE_SUCCESS = "28";
    //29.质押解质失败，被交易所驳回
    public static final String PLEDGE_UNFREEZE_FAIL= "29";
}
