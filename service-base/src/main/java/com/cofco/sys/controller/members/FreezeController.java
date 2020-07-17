package com.cofco.sys.controller.members;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.constant.Const;
import com.cofco.constant.LogOper;
import com.cofco.sentinelRedis.SetSentinel;
import com.cofco.sys.entity.members.*;
import com.cofco.sys.service.members.FreezeService;
import com.cofco.sys.service.members.MembersService;
import com.cofco.utils.SHA256Util;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * 仓单业务管理
 *
 * @author liyang
 * @date 2019-11-05 13:42:42
 */
@RestController
@RequestMapping("/sys/bill")
public class FreezeController {

    @Autowired
    FreezeService freezeService;

    @Autowired
    MembersService membersService;

    private static Logger log = Logger.getLogger(SetSentinel.class);

    /**
     * 仓单信息查询
     *
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        params.put("clientId", "00000001");
        //params.put("varietyId", "1073611");
        //查询列表数据
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<BillEntity> configList = freezeService.queryList(query);
        PageHelper.endPage();
        int total = freezeService.queryBillTotal(query);
        PageUtils pageUtil = new PageUtils(configList, total, query.getLimit(), query.getPage());
        return Result.ok().put("page", pageUtil);
    }

    /**
     * 仓单质押信息查询
     *
     * @param params
     * @return
     */
    @RequestMapping("/queryPledgeList")
    public Result queryPledgeList(@RequestParam Map<String, Object> params) {
        params.put("clientId", "00000001");
        //查询列表数据
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<BillPledgeEntity> configList = freezeService.queryPledgeList(query);
        PageHelper.endPage();
        int total = freezeService.queryPledgeTotal(query);
        PageUtils pageUtil = new PageUtils(configList, total, query.getLimit(), query.getPage());
        return Result.ok().put("page", pageUtil);
    }

    /**
     * 仓单质押预解质提交(暂改名字)
     */
    @RequestMapping("/preUnfreezes")
    public Result preUnfreezes(@RequestBody BillPledgeEntity params) {
        //防止重复提交申请 TODO
        int count = freezeService.queryOperatorLogTotal(params.getId(), LogOper.PRE_UNFREEZETX);
        if(count > 0){
            return Result.ok("审核中,请等待");
        }

        Map<String, Object> param = new HashMap<>();
        //ID
        param.put("id", params.getId());
        //操作类型
        param.put("operatorType", params.getOperatorTypeCode());

        // 仓单质押预解质信息
        BillOperatorEntity billOperatorEntity = freezeService.queryObject(param);
        // 预解冻信息查询
        List<BillFlowEntity> billFlowEntities = freezeService.preUnfreezeInfo(param);

        // 请求参数
        SortedMap<String, String> requestMap = new TreeMap<>();
        // 出质人联盟成员ID
        requestMap.put("pledgerMemberId", billOperatorEntity.getPledgerMemberId());
        // 联盟成员类型
        requestMap.put("memberType", billOperatorEntity.getMemberType());
        // 联盟客户ID
        requestMap.put("clientId", billOperatorEntity.getClientId());
        // 质押合同ID
        requestMap.put("pledgerContractId", billOperatorEntity.getPledgerContractId());
        // 虚拟仓单ID
        requestMap.put("virtualWbillId", billOperatorEntity.getVirtualWbillId());
        // Json列表格式 ，预冻结多个仓单及每个仓单的预冻结数量
        SortedMap<String, String> map = new TreeMap<>();
        for (int j = 0; j < billFlowEntities.size(); j++) {
            // 仓单ID 仓单数量
            map.put(billFlowEntities.get(j).getWbillId(), billFlowEntities.get(j).getWeight());
        }
        // 预解冻信息
        requestMap.put("parentWbillUnfreeze", JSON.toJSONString(map));
        // 经办人姓名
        requestMap.put("operatorName", billOperatorEntity.getOperatorName());
        // 经办人联系电话
        requestMap.put("operatorTel", billOperatorEntity.getOperatorTel());
        // 经办人身份证号码
        requestMap.put("operatorId", billOperatorEntity.getOperatorId());
        // 版本
        requestMap.put("version", billOperatorEntity.getVersion());
        // 请求ID
        requestMap.put("requestId", "");
        // 时间戳
        requestMap.put("timestamp", "");
        // 随机数
        requestMap.put("nonce", "");
        //查询出当前所属联盟成员的公私钥 TODO
        MembersEntity member = membersService.queryKey("111000");
        // 签名
        requestMap.put("sign", SHA256Util.createSign(requestMap));

        // 接口
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");
        String methodUrl = "http://localhost:8082/wbc/api/bill/preUnfreeze";
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(requestMap));
        Request reqBuild = new Request.Builder()
                .url(methodUrl)
                .post(body)
                .build();

        try {
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());

            // TODO
            if (Const.STATUS_CODE_202.equals(jsonObject.getString("statusCode"))) {
                //临时ID
                String tempId = jsonObject.getString("preunfreezeId");
                //仓单操作记录日志表
                BillOperatorLogEntity operatorLogEntity = new BillOperatorLogEntity();
                //联盟客户表ID
                operatorLogEntity.setWbillOperatorId(params.getId());
                //临时ID
                operatorLogEntity.setTempId(tempId);
                //操作类型
                operatorLogEntity.setOperatorType(LogOper.PRE_UNFREEZETX);
                //仓单操作记录日志表登录
                freezeService.saveLog(operatorLogEntity);

                // 仓单质押预解质
                freezeService.preUnfreeze(tempId, Const.PLEDGE_PRE_UNFREEZING);

                log.info("-----仓单质押预解质提交成功-----");
            } else {
                return Result.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("-----仓单质押预解质提交失败-----");
            return Result.error();
        }
        return Result.ok("仓单质押预解质提交成功! 交易所审核中");
    }

    /**
     * 仓单质押预解质提交回调接口(暂改名字)
     */
    @RequestMapping("/preUnfreezeApis")
    public Result preUnfreezeApis(@RequestBody JSONObject jsonParam) {
        // 接收交易所的仓单质押预解质结果
        if (Const.STATUS_CODE_200.equals(jsonParam.getString("statusCode"))) {
            //临时ID
            String tempId = jsonParam.getString("preunfreezeId");
            // 仓单质押预解质
            freezeService.preUnfreeze(tempId, Const.PLEDGE_PRE_UNFREEZE);

            //仓单操作记录日志表
            BillOperatorLogEntity operatorLogEntity = new BillOperatorLogEntity();
            //临时ID
            operatorLogEntity.setTempId(tempId);
            //审核状态
            operatorLogEntity.setAuditStatus(jsonParam.getString("auditState"));
            //仓单操作记录日志表更新
            freezeService.updateLog(operatorLogEntity);
        } else {
            log.info("-----仓单质押预解质提交回调失败-----");
            return Result.error("交易所拒绝仓单质押预解质申请!");
        }
        return Result.ok();
    }

    /**
     * 仓单质押预解质撤销提交(暂改名字)
     */
    @RequestMapping("/preUnfreezeCancels")
    public Result preUnfreezeCancels(@RequestBody BillPledgeEntity params) {
        //防止重复提交申请 TODO
        int count = freezeService.queryOperatorLogTotal(params.getId(), LogOper.PRE_UNFREEZE_CANCEL);
        if(count > 0){
            return Result.ok("审核中,请等待");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("id", params.getId());
        param.put("operatorType", params.getOperatorTypeCode());
        // 仓单质押预解质信息
        BillOperatorEntity billOperatorEntity = freezeService.queryObject(param);
        // 请求参数
        SortedMap<String, String> requestMap = new TreeMap<>();
        // 联盟成员ID
        requestMap.put("memberId", billOperatorEntity.getPledgerMemberId());
        // 联盟成员类型
        requestMap.put("memberType", billOperatorEntity.getMemberType());
        // 联盟客户经办人姓名
        requestMap.put("operatorName", billOperatorEntity.getOperatorName());
        // 联盟客户经办人联系电话
        requestMap.put("operatorTel", billOperatorEntity.getOperatorTel());
        // 联盟客户经办人身份证号码
        requestMap.put("operatorId", billOperatorEntity.getOperatorId());
        // 版本
        requestMap.put("version", billOperatorEntity.getVersion());
        // 请求ID
        requestMap.put("requestId", "");
        // 时间戳
        requestMap.put("timestamp", "");
        // 随机数
        requestMap.put("nonce", "");
        //查询出当前所属联盟成员的公私钥 TODO
        MembersEntity member = membersService.queryKey("111000");
        // 签名
        requestMap.put("sign", SHA256Util.createSign(requestMap));

        // 接口
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");
        String methodUrl = "http://localhost:8082/wbc/api/bill/preUnfreezeCancel";
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(requestMap));
        Request reqBuild = new Request.Builder()
                .url(methodUrl)
                .delete(body)
                .build();

        try {
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());

            // TODO
            if (Const.STATUS_CODE_200.equals(jsonObject.getString("statusCode"))) {
                // 临时ID
                String tempId = jsonObject.getString("preunfreezeCancleId");
                //仓单操作记录日志表
                BillOperatorLogEntity operatorLogEntity = new BillOperatorLogEntity();
                //联盟客户表ID
                operatorLogEntity.setWbillOperatorId(params.getId());
                //临时ID
                operatorLogEntity.setTempId(tempId);
                //操作类型
                operatorLogEntity.setOperatorType(LogOper.PRE_UNFREEZE_CANCEL);
                //仓单操作记录日志表登录
                freezeService.saveLog(operatorLogEntity);

                // 仓单质押预解质撤销
                freezeService.preUnfreezeCancel(tempId,Const.PLEDGE_PRE_UNFREEZE_REVOKING);

                log.info("-----仓单质押预解质撤销提交成功-----");
            } else {
                return Result.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("-----仓单质押预解质撤销提交失败-----");
            return Result.error("与交易所通信异常,请联系管理员");
        }
        return Result.ok("仓单质押预解质撤销提交成功! 交易所审核中");
    }

    /**
     * 仓单质押预解质撤销提交回调接口(暂改名字)
     */
    @RequestMapping("/preUnfreezeCancelApis")
    public Result preUnfreezeCancelApis(@RequestBody JSONObject jsonParam) {
        // 接收交易所的仓单质押预解质撤销结果
        if (Const.STATUS_CODE_200.equals(jsonParam.getString("statusCode"))) {
            // 临时ID
            String tempId = jsonParam.getString("preunfreezeCancleId");
            // 仓单质押预解质撤销
            freezeService.preUnfreezeCancel(tempId,Const.PLEDGE_PRE_UNFREEZE_REVOKE);

            //仓单操作记录日志表
            BillOperatorLogEntity operatorLogEntity = new BillOperatorLogEntity();
            //临时ID
            operatorLogEntity.setTempId(tempId);
            //审核状态
            operatorLogEntity.setAuditStatus(jsonParam.getString("auditState"));
            //仓单操作记录日志表更新
            freezeService.updateLog(operatorLogEntity);
        } else {
            log.info("-----仓单质押预解质撤销提交回调失败-----");
            return Result.error("交易所拒绝仓单质押预解质撤销申请!");
        }
        return Result.ok();
    }

    /**
     * 仓单质押解质提交(暂改名字)
     */
    @RequestMapping("/unfreezes")
    public Result unfreezes(@RequestBody BillPledgeEntity params) {
        //防止重复提交申请 TODO
        int count = freezeService.queryOperatorLogTotal(params.getId(), LogOper.UNFREEZETX);
        if(count > 0){
            return Result.ok("审核中,请等待");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("id", params.getId());
        param.put("operatorType", params.getOperatorTypeCode());
        // 仓单质押预解质信息
        BillOperatorEntity billOperatorEntity = freezeService.queryObject(param);
        // 请求参数
        SortedMap<String, String> requestMap = new TreeMap<>();
        // 出质人联盟成员ID
        requestMap.put("pledgerMemberId", billOperatorEntity.getPledgerMemberId());
        // 联盟成员类型
        requestMap.put("memberType", billOperatorEntity.getMemberType());
        // 联盟客户ID
        requestMap.put("clientId", billOperatorEntity.getClientId());
        // 质押合同ID
        requestMap.put("pledgeContractId", billOperatorEntity.getPledgerContractId());
        // 虚拟仓单ID
        requestMap.put("virtualWbillId", billOperatorEntity.getVirtualWbillId());
        // 经办人姓名
        requestMap.put("operatorName", billOperatorEntity.getOperatorName());
        // 经办人联系电话
        requestMap.put("operatorTel", billOperatorEntity.getOperatorTel());
        // 经办人身份证号码
        requestMap.put("operatorId", billOperatorEntity.getOperatorId());
        // 版本
        requestMap.put("version", billOperatorEntity.getVersion());
        // 请求ID
        requestMap.put("requestId", "");
        // 时间戳
        requestMap.put("timestamp", "");
        // 随机数
        requestMap.put("nonce", "");
        //查询出当前所属联盟成员的公私钥 TODO
        MembersEntity member = membersService.queryKey("111000");
        // 签名
        requestMap.put("sign", SHA256Util.createSign(requestMap));

        // 接口
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");
        String methodUrl = "http://localhost:8082/wbc/api/bill/unfreeze";
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(requestMap));
        Request reqBuild = new Request.Builder()
                .url(methodUrl)
                .post(body)
                .build();

        try {
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());

            // TODO
            if (Const.STATUS_CODE_202.equals(jsonObject.getString("statusCode"))) {
                // 临时ID
                String tempId = jsonObject.getString("unfreezeId");
                //仓单操作记录日志表
                BillOperatorLogEntity operatorLogEntity = new BillOperatorLogEntity();
                //联盟客户表ID
                operatorLogEntity.setWbillOperatorId(params.getId());
                //临时ID
                operatorLogEntity.setTempId(tempId);
                //操作类型
                operatorLogEntity.setOperatorType(LogOper.UNFREEZETX);
                //仓单操作记录日志表登录
                freezeService.saveLog(operatorLogEntity);

                // 仓单质押解质
                freezeService.unfreeze(tempId, Const.PLEDGE_UNFREEZING);

                log.info("-----仓单质押解质提交成功-----");
            } else {
                return Result.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("与交易所通信异常,请联系管理员");
        }
        return Result.ok("仓单质押解质提交成功! 交易所审核中");
    }

    /**
     * 仓单质押解质提交回调接口(暂改名字)
     */
    @RequestMapping("/unfreezeApis")
    public Result unfreezeApis(@RequestBody JSONObject jsonParam) {
        // 接收交易所的仓单质押解质提交结果
        if (Const.STATUS_CODE_200.equals(jsonParam.getString("statusCode"))) {
            // 临时ID
            String tempId = jsonParam.getString("unfreezeId");
            // 仓单质押解质
            freezeService.unfreeze(tempId, Const.PLEDGE_UNFREEZE);

            List<String> ids = freezeService.queryFlowId(tempId);

            freezeService.updateWeight(ids);

            //仓单操作记录日志表
            BillOperatorLogEntity operatorLogEntity = new BillOperatorLogEntity();
            //临时ID
            operatorLogEntity.setTempId(tempId);
            //审核状态
            operatorLogEntity.setAuditStatus(jsonParam.getString("auditState"));
            //仓单操作记录日志表更新
            freezeService.updateLog(operatorLogEntity);
        } else {
            log.info("-----仓单质押解质提交回调失败-----");
            return Result.error("交易所拒绝仓单质押解质提交申请!");
        }
        return Result.ok();
    }

    /**
     * 仓单注销提交(暂改名字)
     */
    @RequestMapping("/billCancels")
    public Result billCancels(@RequestBody BillEntity billEntities) {

        //仓单注销可否信息查询
        int total = freezeService.queryBillDecisionTotal(String.valueOf(billEntities.getId()));

        if (total > 0) {
            return Result.error("目前仓单不可注销!");
        }

        // 仓单信息查询
        BillEntity billEntity = freezeService.queryBillInfo(String.valueOf(billEntities.getId()));
        // 请求参数
        SortedMap<String, String> requestMap = new TreeMap<>();
        // 联盟成员ID
        requestMap.put("whMemberId", billEntity.getWhMemberId());
        // 联盟客户ID
        requestMap.put("clientId", billEntity.getClientId());
        // 联盟仓单ID
        requestMap.put("wbillId", billEntity.getWbillId());
        // 仓库仓单ID
        requestMap.put("whWbillId", billEntity.getWhWbillId());
        // 仓储机构经办人姓名
        requestMap.put("operatorName", billEntity.getOperatorName());
        // 仓储机构经办人电话
        requestMap.put("operatorTel", billEntity.getOperatorTel());
        // 货主联系人姓名
        requestMap.put("clientContactName", billEntity.getClientContactName());
        // 货主联系人电话
        requestMap.put("clientContactTel", billEntity.getClientContactTel());
        // 版本
        requestMap.put("version", billEntity.getVersion());
        // 请求ID
        requestMap.put("requestId", "");
        // 时间戳
        requestMap.put("timestamp", "");
        // 随机数
        requestMap.put("nonce", "");
        //查询出当前所属联盟成员的公私钥 TODO
        MembersEntity member = membersService.queryKey("111000");
        // 签名
        requestMap.put("sign", SHA256Util.createSign(requestMap));

        // 接口
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");
        String methodUrl = "http://localhost:8082/wbc/api/bill/billCancel";
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONString(requestMap));
        Request reqBuild = new Request.Builder()
                .url(methodUrl)
                .delete(body)
                .build();

        try {
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());

            // TODO
            if (Const.STATUS_CODE_201.equals(jsonObject.getString("statusCode"))) {
                //仓单操作记录日志表
                BillLogEntity billLogEntity = new BillLogEntity();
                //联盟客户表ID
                billLogEntity.setWbillId(String.valueOf(billEntities.getId()));
                //临时ID
                billLogEntity.setTempId(jsonObject.getString("wbillTmpId"));
                //操作类型
                billLogEntity.setOperatorType(LogOper.BILL_LOGOF);
                //仓单操作记录日志表登录
                freezeService.saveBillLog(billLogEntity);
            } else {
                return Result.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("与交易所通信异常,请联系管理员");
        }
        return Result.ok("仓单注销成功!");
    }

    /**
     * 仓单注销提交回调接口(暂改名字)
     */
    @RequestMapping("/billCancelApis")
    public Result billCancelApis(@RequestBody JSONObject jsonParam) {
        // 接收交易所的仓单注销提交结果
        if (Const.STATUS_CODE_200.equals(jsonParam.getString("statusCode"))) {
            BillEntity billEntity = new BillEntity();
            //临时ID
            //billEntity.setId(jsonParam.getString("wbillTmpId"));
            billEntity.setId(Integer.valueOf(jsonParam.getString("wbillTmpId")));
            // 仓单注销
            freezeService.billCancel(billEntity);

            List<BillFlowEntity> billFlowEntities = freezeService.queryBillFlowList(jsonParam.getString("wbillId"));

            for (int i = 0; i < billFlowEntities.size(); i++) {
                freezeService.updateBillOperator(billFlowEntities.get(i).getBillOperatorId());

                freezeService.updateBillFlow(billFlowEntities.get(i).getBillOperatorId());
            }

            //仓单日志表
            BillLogEntity billLogEntity = new BillLogEntity();
            //临时ID
            billLogEntity.setTempId(jsonParam.getString("wbillTmpId"));
            //审核状态
            billLogEntity.setAuditStatus(jsonParam.getString("auditState") + ":" + jsonParam.getString("reason"));
            //仓单操作记录日志表更新
            freezeService.updateBillLog(billLogEntity);
        } else {
            log.info("-----仓单质押解质提交回调失败-----");
            return Result.error("交易所拒绝仓单质押解质提交申请!");
        }
        return Result.ok();
    }
}
