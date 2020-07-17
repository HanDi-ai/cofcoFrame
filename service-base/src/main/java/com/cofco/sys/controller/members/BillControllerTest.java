
package com.cofco.sys.controller.members;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cofco.base.annotation.SysLog;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.constant.Const;
import com.cofco.constant.LogOper;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.members.*;
import com.cofco.sys.service.members.BillService;
import com.cofco.sys.service.members.MembersService;
import com.cofco.utils.*;
import net.sf.json.JSONArray;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.util.*;

/**
 * 仓单业务管理
 * @author handi
 * @date 2019 11/05 13:54:30
 * @email 876053009@qq.com
 */
@RestController
@RequestMapping("sys/billt")
public class BillControllerTest extends BaseController {
    @Autowired
    private BillService billService;
    @Autowired
    private MembersService membersService;
    Logger log = Logger.getLogger(MembersController.class);
    /**
     * 仓单查询
     */
    @RequestMapping("/queryList")
    public Result queryList(@RequestParam Map<String, Object> params){
        params.put("id","15611460628");
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<BillEntity> roleList = billService.queryList(query);
        PageHelper.endPage();
        int total = billService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(roleList, total, query.getLimit(), query.getPage());
        return Result.ok().put("page", pageUtil);
    }

    /**
     * 根据id号查询仓单信息
     */
    @RequestMapping("/info/{id}")
    @SysLog("查看角色")
    public Result info(@PathVariable("id") String id){
        BillEntity bill = billService.queryObject(id);
        return Result.ok().put("role",bill);
    }

    /**
     *仓单备案提交
     */
    @RequestMapping("/save")
    public Result save(@RequestBody BillEntity billEntity){
        Result result = Result.ok();
        int id = billService.seqBillId();
        //联盟成员ID
        String whMemberID = "wh00324";
        String requestId =SHA256Util.getUUID32();
        Long  timeNew =  System.currentTimeMillis();
        billEntity.setWhMemberId(whMemberID);
        billEntity.setVersion("v1");
        billEntity.setId(id);
        billEntity.setTime_Create(new java.util.Date());
        billEntity.setCreator(UserUtils.getCurrentUserId());
        billEntity.setIs_Delete(Integer.valueOf(Const.DELETE_FLG_NO));
        billEntity.setInspectedBeginDate("20191201");
        billEntity.setInspectedEndDate("20201231");
        billEntity.setStoreBeginDate("20190102");
        billEntity.setStoreEndDate("20201231");
        billEntity.setInsuranceBeginDate("20191205");
        billEntity.setInsuranceEndDate("20201231");
        billService.save(billEntity);
        try{
            SortedMap map = SignatureUtil.entityToMap(billEntity);
            map.put("requestId",requestId);
            map.put("timestamp",timeNew);
            map.put("nonce", String.valueOf(RandomStringUtils.randomAlphanumeric(20)));
            map.remove("modifier");
            map.remove("id");
            map.remove("creator");
            map.remove("weightChk");
            map.remove("wbillTmpId");
            map.remove("time_Modify");
            map.remove("pledgedWeight");
            map.remove("is_Delete");
            map.remove("time_Create");
            map.remove("operatorType");
            map.remove("operatorType");
            map.remove("wbillId");
            //修改
            map.put("inspectedBeginDate","20191201");
            map.put("inspectedEndDate","20201231");

            map.put("storeBeginDate","20190102");
            map.put("storeEndDate","20201231");

            map.put("insuranceBeginDate","20191205");
            map.put("insuranceEndDate","20201231");
            //垛位信息
            String locationInfo = "[]";
            map.put("locationInfo",locationInfo.trim());
            String qua = "{\"玉米\":\"1\"}";
            String loss = "{\"低耗\":\"2\"}";
            JSON qual = JSON.parseObject(qua);
            JSON losss = JSON.parseObject(loss);
            String quality =JSON.toJSONString(qual);
            String lossStandard =JSON.toJSONString(losss);
            map.put("quality",quality);
            map.put("lossStandard",lossStandard);
            log.info("生成签名前的参数值：" + map);
            //MembersEntity membersEntity = membersService.queryKey(whMemberID);
            //map.put("sign", SHA256Util.createSignT(map,membersEntity));
            log.info("生成签名后的参数值：" + map);
            //String sig = String.valueOf(map.get("sign"));
            //请求大商所接口
            /*String url="http://59.46.215.173:8999/wbc/api/wbill";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("whMemberId",String.valueOf(map.get("whMemberId")))
                    .add("wbillType",String.valueOf(map.get("wbillType")))
                    .add("isStandard",String.valueOf(map.get("isStandard")))
                    .add("whWbillId",String.valueOf(map.get("whWbillId")))
                    .add("varietyId",String.valueOf(map.get("varietyId")))
                    .add("varietyName",String.valueOf(map.get("varietyName")))
                    .add("packageType",String.valueOf(map.get("packageType")))
                    .add("counts",String.valueOf(map.get("counts")))
                    .add("weight",String.valueOf(map.get("weight")))
                    .add("isMarked",String.valueOf(map.get("isMarked")))
                    .add("markContent",String.valueOf(map.get("markContent")))
                    .add("productArea",String.valueOf(map.get("productArea")))
                    .add("quality","{\"玉米\":\"1\"}")
                    .add("isInspected",String.valueOf(map.get("isInspected")))
                    .add("inspectionOrgName",String.valueOf(map.get("inspectionOrgName")))
                    .add("inspectionOrgNo",String.valueOf(map.get("inspectionOrgNo")))
                    .add("inspectionAttach",String.valueOf(map.get("inspectionAttach")))
                    .add("inspectedBeginDate",String.valueOf(map.get("inspectedBeginDate")))
                    .add("inspectedEndDate",String.valueOf(map.get("inspectedEndDate")))
                    .add("locationInfo",String.valueOf(map.get("locationInfo")))
                    .add("storeBeginDate",String.valueOf(map.get("storeBeginDate")))
                    .add("storeEndDate",String.valueOf(map.get("storeEndDate")))
                    .add("storageFee",String.valueOf(map.get("storageFee")))
                    .add("lossStandard","{\"低耗\":\"2\"}")
                    .add("operatorName",String.valueOf(map.get("operatorName")))
                    .add("operatorTel",String.valueOf(map.get("operatorTel")))
                    .add("clientId",String.valueOf(map.get("clientId")))
                    .add("clientContactName",String.valueOf(map.get("clientContactName")))
                    .add("clientContactTel",String.valueOf(map.get("clientContactTel")))
                    .add("isInsurance",String.valueOf(map.get("isInsurance")))
                    .add("insuranceOrg",String.valueOf(map.get("insuranceOrg")))
                    .add("insuranceOrgNo",String.valueOf(map.get("insuranceOrgNo")))
                    .add("insuranceContact",String.valueOf(map.get("insuranceContact")))
                    .add("insuranceContactTel",String.valueOf(map.get("insuranceContactTel")))
                    .add("insuranceBeginDate",String.valueOf(map.get("insuranceBeginDate")))
                    .add("insuranceEndDate",String.valueOf(map.get("insuranceEndDate")))
                    .add("version","v1")
                    .add("requestId",requestId)
                    .add("timestamp",String.valueOf(map.get("timestamp")))
                    .add("nonce",String.valueOf(map.get("nonce")))
                    .add("sign",sig)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);*/
            //if (Const.STATUS_CODE_201.equals(jsonObject.getString("status"))) {
            if (Const.STATUS_CODE_201.equals("201")) {
                //将仓单信息存入本地库
                //billService.save(billEntity);

                //仓单备案入库失败
                /*if(t == 0){
                    return Result.error().put("msg","仓单信息有误,入库失败");
                }*/
                //临时ID
                //String wbillTmpId = jsonObject.getString("wbillTmpId");
                String wbillTmpId = "213123";
                //将临时ID新增到log表
                int bid = billService.seqBillLogId();
                BillLogEntity billLogEntity = new BillLogEntity();
                billLogEntity.setId(String.valueOf(bid));
                billLogEntity.setWbillId(String.valueOf(id));
                billLogEntity.setTempId(wbillTmpId);
                billLogEntity.setOperatorType(LogOper.BILL_SAVE);
                billService.saveOnrecordLog(billLogEntity);
                result = Result.ok("仓单备案提交成功! 交易所审核中");
            }else {
                result =  Result.error("与交易所通信异常,请联系管理员");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     *仓单备案提交回调接口
     */
    @RequestMapping(value = "/saveApi", method = RequestMethod.POST)
    public Result saveApi(@RequestParam Map<String, Object> param){
        log.info("仓单备案回调接口入参：" + param);
        //联盟仓单ID
        String wbillId =String.valueOf(param.get("wbillId"));
        //临时ID
        String wbillTmpId = String.valueOf(param.get("wbillTmpId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(wbillId.equals("null") || wbillId.equals("")){
            return Result.error().put("msg","仓单ID不能为空");
        }
        if(wbillTmpId.equals("null") || wbillTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审批结果不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单备案失败!").put("resultCode","0");
        }
        //将审批结果更新到log表
        BillLogEntity billLogEntity = new BillLogEntity();
        billLogEntity.setTempId(wbillTmpId);
        billLogEntity.setAuditStatus(auditState);
        billService.updateLog(billLogEntity);
        //将仓单ID更新到bill表
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("wbillId",wbillId);
        map.put("wbillTmpId",wbillTmpId);
        billService.updateWbillId(map);
        //请求iot接口告知仓单ID
            try {
                String url="";
                OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
                okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                        .add("wbillId",wbillId)
                        .add("status","true")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                log.info("响应结果：" + jsonObject);
            }catch (IOException e){
                e.printStackTrace();
            }
        return Result.ok("仓单备案成功!").put("resultCode","1");
    }

    /**
     * 上传iot数据
     * @auth handi
     * @date 2020/1/6 14:39
     * @return
     */
    @RequestMapping(value = "/queryIot",method = RequestMethod.POST)
    public  Result queryIot(@RequestParam Map<String,Object> param){
        Result result = Result.ok();
        try{
            //设备ID
            String deviceId = String.valueOf(param.get("deviceId"));
            //IOT数据
            String iotValue = String.valueOf(param.get("iotValue"));
            //仓单ID
            String wbillId = String.valueOf(param.get("wbillId"));
            //随机数
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            //请求ID
            String requestId = SHA256Util.getUUID32();
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            Map<String,Object> maps = new HashMap<>();
            //联盟仓单ID
            maps.put("wbillId",wbillId);
            BillEntity billEntity = billService.queryIot(maps);
            TreeMap<String,String> map = new TreeMap<String, String>();
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce",none);
            map.put("whMemberId",String.valueOf(billEntity.getWhMemberId()));
            map.put("clientId",billEntity.getClientId());
            map.put("wbillId",wbillId);
            map.put("positionId",billEntity.getLocationInfo());
            map.put("locationId",billEntity.getLocationInfo());
            map.put("deviceId",deviceId);
            map.put("iotValue",iotValue);
            //签名
            map.put("sign",SHA256Util.createSign(map));
            String url="";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("deviceId",deviceId)
                    .add("iotValue",iotValue)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
            result = Result.ok("iot数据上传成功!");
        }catch (IOException e){
            e.printStackTrace();
            result = Result.ok("iot数据上传失败!");
        }
        return result;
    }

    /**
     *预冻结提交
     * @auth handi
     * @date 2019/11/21
     */
    @RequestMapping("/preFretest")
    public Result preFretest(String id){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            Map<String,Object> hsMap = new HashMap<>();
            String wbillId = "w0000000000000000013";
            hsMap.put(wbillId,"50");
            String  param= JSON.toJSONString(hsMap);
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String clientId ="c000000017";
            String pledgerMemberId ="wh00006";
            String pledgeeMemberId ="bk00007";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("parentWbill",param);
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("operatorId","321281199407232731");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://59.46.215.173:8999/wbc/api/wbill/prefreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",String.valueOf(map.get("pledgerMemberId")))
                    .add("pledgeeMemberId",String.valueOf(map.get("pledgeeMemberId")))
                    .add("memberType",String.valueOf(map.get("memberType")))
                    .add("clientId",String.valueOf(map.get("clientId")))
                    .add("parentWbill",param)
                    .add("operatorName",String.valueOf(map.get("operatorName")))
                    .add("operatorTel",String.valueOf(map.get("operatorTel")))
                    .add("operatorId",String.valueOf(map.get("operatorId")))
                    .add("version","v1")
                    .add("requestId",requestId)
                    .add("timestamp",String.valueOf(timeNew))
                    .add("nonce",none)
                    .add("sign",map.get("sign"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
            if (Const.STATUS_CODE_202.equals(jsonObject.getString("status"))) {
                int cid = billService.seqBillOperatorlogId();
                //临时ID
                String prefreezeId = jsonObject.getString("prefreezeId");
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(prefreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.PREFREEZING_SUB);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押预冻结提交成功! 交易所审核中");
            }else {
                result =  Result.error("与交易所通信异常,请联系管理员");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *预冻结提交回调接口
     * @auth handi
     */
    @RequestMapping(value = "/preFreezingApi", method = RequestMethod.POST)
    public Result preFreezingApi(@RequestParam Map<String, Object> param){
        log.info("仓单质押预冻结接口入参：" + param);
        //虚拟仓单ID
        String virtualWbillId =String.valueOf(param.get("virtualWbillId"));
        //临时ID
        String prefreezeId = String.valueOf(param.get("prefreezeId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(virtualWbillId.equals("null") || virtualWbillId.equals("")){
            return Result.error().put("msg","虚拟仓单ID不能为空");
        }
        if(prefreezeId.equals("null") || prefreezeId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审批结果不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单预冻结失败!").put("resultCode","0");
        }
        //将虚拟仓单ID更新入库
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("virtualWbillId",virtualWbillId);
        map.put("prefreezeId",prefreezeId);
        billService.updateVir(map);
        //将审核状态更新入库
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setTempId(prefreezeId);
        billOperatorLogEntity.setAuditStatus(auditState);
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok("仓单质押预冻结成功!").put("resultCode","1");
    }

    /**
     * 仓单预冻结撤销
     * @auth handi
     * @return
     */
    @RequestMapping("/revokePreFreezing")
    public Result revokePreFreezing(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            String id = "1";
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String memberId ="wh00006";
            //预冻结流水号
            String prefreezeId ="21";
            map.put("memberId",memberId);
            map.put("memberType","0");
            map.put("prefreezeId",prefreezeId);
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("operatorId","321281199407232731");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://59.46.215.173:8999/wbc/api/wbill/prefreezeTx/?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&prefreezeId="+map.get("prefreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
            //String methodUrl ="";
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder()
                    .url(methodUrl)
                    .delete()
                    .build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
            if (Const.STATUS_CODE_202.equals(jsonObject.getString("status"))) {
                //临时ID
                String prefreezeCancleId= jsonObject.getString("prefreezeCancleId");
                int oid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(oid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(prefreezeCancleId);
                billOperatorLogEntity.setOperatorType(LogOper.REVOKE_PREFREEZING);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押预冻结撤销提交成功! 交易所审核中");
            } else {
                result = Result.error("与交易所通信异常,请联系管理员");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *仓单预冻结撤销回调接口
     * @auth handi
     */
    @RequestMapping(value = "/revokePreFreezingApi", method = RequestMethod.POST)
    public Result revokePreFreezingApi(@RequestParam Map<String, Object> param){
        log.info("仓单预冻结撤销回调接口入参：" + param);
        //申请ID
        String prefreezeCancleId =String.valueOf(param.get("prefreezeCancleId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(prefreezeCancleId.equals("null") || prefreezeCancleId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单预冻结撤销失败!").put("resultCode","0");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId(prefreezeCancleId);
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok("仓单预冻结撤销成功!").put("resultCode","1");
    }

    /**
     *仓单质押冻结
     */
    @RequestMapping("/freeze")
    public Result freeze(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String photo = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\n" +
                    "HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy\n" +
                    "MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAPoAykDASIA\n" +
                    "AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\n" +
                    "AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\n" +
                    "ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\n" +
                    "p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA\n" +
                    "AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\n" +
                    "BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\n" +
                    "U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\n" +
                    "uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDmiOM4\n" +
                    "oCntTgAKeAK+pPAGgGnAGnA4NScHpSGkRbTSgc07FOH6UDsMwaBmn4HajFFwEApcH1oxQATSAOcU\n" +
                    "ozj2pR6U4DI/pSAjAJp4Bzx+NKAQ3TFW4thxjGfQ0mykis";
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String clientId ="c000000017";
            String pledgerMemberId ="wh00006";
            String pledgeeMemberId ="bk00007";
            //虚拟仓单id
            String virtualWbillId ="vw00000000000000000003";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("virtualWbillId",virtualWbillId);
            map.put("pledgeContractAttach",photo);
            map.put("pledgeBeginDate","20191201");
            map.put("pledgeEndDate","20201201");
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("operatorId","321281199407232731");
            map.put("operatorIdAttach",photo);
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://59.46.215.173:8999/wbc/api/wbill/freezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",pledgerMemberId)
                    .add("pledgeeMemberId",pledgeeMemberId)
                    .add("memberType","0")
                    .add("clientId",clientId)
                    .add("virtualWbillId",virtualWbillId)
                    .add("pledgeContractAttach",photo)
                    .add("pledgeBeginDate","20191201")
                    .add("pledgeEndDate","20201201")
                    .add("operatorName","张三")
                    .add("operatorTel","18052125652")
                    .add("operatorId","321281199407232731")
                    .add("operatorIdAttach",photo)
                    .add("version","v1")
                    .add("requestId",requestId)
                    .add("timestamp",String.valueOf(timeNew))
                    .add("nonce",none)
                    .add("sign",map.get("sign"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *冻结提交回调接口(临时测试hd)
     */
    @RequestMapping(value = "/freezeApi", method = RequestMethod.POST)
    public Result freezeApi(@RequestParam Map<String, Object> param){
        log.info("仓单质押冻结接口入参：" + param);
        //申请ID
        String freezeId =String.valueOf(param.get("freezeId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(freezeId.equals("null") || freezeId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单质押冻结失败!").put("resultCode","0");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(freezeId);
        billOperatorLogEntity.setTempId("13");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId("14");
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok("仓单质押冻结成功!").put("resultCode","1");
    }

    /**
     * 通过审核的仓单查询3.1.4.3(临时测试hd)
     * @auth handi
     * @date 2019/12/30
     */
    @RequestMapping("/billCheck")
    public Result billCheck(){
        try {
            String wbillId="w0000000000000000013";
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            SortedMap<String, String> requestMapf = new TreeMap<>();
            requestMapf.put("memberId","wh00006");
            requestMapf.put("memberType","0");
            requestMapf.put("version","v1");
            requestMapf.put("requestId",requestId);
            requestMapf.put("timestamp",String.valueOf(timeNew));
            requestMapf.put("nonce",none);
            log.info("生成签名前的参数值：" + requestMapf);
            requestMapf.put("sign",SHA256Util.createSign(requestMapf));
            log.info("生成签名后的参数值：" + requestMapf);

            String memberId = requestMapf.get("memberId").trim();
            String memberType = requestMapf.get("memberType").trim();
            String version = requestMapf.get("version").trim();
            String requestIdf = requestMapf.get("requestId").trim();
            String timestamp = requestMapf.get("timestamp").trim();
            String nonce = requestMapf.get("nonce").trim();
            String sign = requestMapf.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://59.46.215.173:8999/wbc/api/wbill/"+wbillId+"?memberId="+memberId+"&memberType="+memberType+"&version="+version
                    +"&requestId="+requestIdf+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder().
                    url(methodUrl).
                    build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    /**
     * 通过审核的仓单查询3.1.4.5(临时测试hd)
     * @auth handi
     * @date 2019/12/30
     */
    @RequestMapping("/billPreCheck")
    public Result billPreCheck(){
        try {
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            SortedMap<String, String> requestMapf = new TreeMap<>();
            requestMapf.put("memberId","wh00006");
            requestMapf.put("memberType","0");
            requestMapf.put("whWbillId","1");
            requestMapf.put("version","v1");
            requestMapf.put("requestId",requestId);
            requestMapf.put("timestamp",String.valueOf(timeNew));
            requestMapf.put("nonce",none);
            log.info("生成签名前的参数值：" + requestMapf);
            requestMapf.put("sign",SHA256Util.createSign(requestMapf));
            log.info("生成签名后的参数值：" + requestMapf);

            String memberId = requestMapf.get("memberId").trim();
            String memberType = requestMapf.get("memberType").trim();
            String version = requestMapf.get("version").trim();
            String requestIdf = requestMapf.get("requestId").trim();
            String timestamp = requestMapf.get("timestamp").trim();
            String nonce = requestMapf.get("nonce").trim();
            String sign = requestMapf.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://59.46.215.173:8999/wbc/api/wbill?memberId="+memberId+"&memberType="+memberType+"&whWbillId="+requestMapf.get("whWbillId")+"&version="+version
                    +"&requestId="+requestIdf+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder().
                    url(methodUrl).
                    build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }


    //----预解质始
    /**
     *仓单质押预解质(临时测试hd)
     * @auth handi
     * @date 2019/12/28
     */
    @RequestMapping("/preUnfreeze")
    public Result preUnfreeze(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            Map<String,Object> hsMap = new HashMap<>();
            String wbillId = "w0000000000000000013";
            hsMap.put(wbillId,"100");
            String  parentWbillUnfreeze= JSON.toJSONString(hsMap);
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String clientId ="c000000017";
            String pledgerMemberId ="wh00006";
            String pledgeeMemberId ="bk00007";
            //虚拟仓单id
            String virtualWbillId ="vw00000000000000000003";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("pledgerContractId","972317627");
            map.put("virtualWbillId",virtualWbillId);
            map.put("parentWbillUnfreeze",parentWbillUnfreeze);
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("operatorId","321281199407232731");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://59.46.215.173:8999/wbc/api/wbill/preUnfreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",pledgerMemberId)
                    .add("pledgeeMemberId",pledgeeMemberId)
                    .add("memberType","0")
                    .add("clientId",clientId)
                    .add("pledgerContractId","972317627")
                    .add("virtualWbillId",virtualWbillId)
                    .add("parentWbillUnfreeze",parentWbillUnfreeze)
                    .add("operatorName","张三")
                    .add("operatorTel","18052125652")
                    .add("operatorId","321281199407232731")
                    .add("version","v1")
                    .add("requestId",requestId)
                    .add("timestamp",String.valueOf(timeNew))
                    .add("nonce",none)
                    .add("sign",map.get("sign"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *预解质提交回调接口(临时测试hd)
     * @auth handi
     */
    @RequestMapping(value = "/preUnfreezeApi", method = RequestMethod.POST)
    public Result preUnfreezeApi(@RequestParam Map<String, Object> param){
        log.info("仓单质押预解质接口入参：" + param);
        //申请ID
        String preunfreezeId =String.valueOf(param.get("preunfreezeId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(preunfreezeId.equals("null") || preunfreezeId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单质押预解质失败!").put("resultCode","0");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(preunfreezeId);
        billOperatorLogEntity.setTempId("15");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId("16");
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok("仓单质押预解质成功!").put("resultCode","1");
    }
    //----预解质止

    //------预解质撤销起
    /**
     * 仓单预解质撤销
     * @auth handi
     * @return
     */
    @RequestMapping("/preUnfreezeCancel")
    public Result preUnfreezeCancel(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String memberId ="wh00006";
            //预解质ID
            String preunfreezeId ="15";
            map.put("memberId",memberId);
            map.put("memberType","0");
            map.put("preunfreezeId",preunfreezeId);
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("operatorId","321281199407232731");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://59.46.215.173:8999/wbc/api/preunfreezeTx?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&preunfreezeId="+map.get("preunfreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
            //String methodUrl ="";
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder()
                    .url(methodUrl)
                    .delete()
                    .build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *仓单预解质撤销回调接口(临时测试hd)
     * @auth handi
     */
    @RequestMapping(value = "/preUnfreezeCancelApi", method = RequestMethod.POST)
    public Result preUnfreezeCancelApi(@RequestParam Map<String, Object> param){
        log.info("仓单预解质撤销回调接口入参：" + param);
        //申请ID
        String preunfreezeCancleId =String.valueOf(param.get("preunfreezeCancleId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(preunfreezeCancleId.equals("null") || preunfreezeCancleId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单质押预解质撤销失败!").put("resultCode","0");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(preunfreezeCancleId);
        billOperatorLogEntity.setTempId("25");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId("26");
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok("仓单预解质撤销成功!").put("resultCode","1");
    }
    //------预解质撤销止



    //----解质始
    /**
     *仓单质押解质(临时测试hd)
     * @auth handi
     */
    @RequestMapping("/unfreeze")
    public Result unfreeze(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String clientId ="c000000017";
            String pledgerMemberId ="wh00006";
            String pledgeeMemberId ="bk00007";
            //虚拟仓单id
            String virtualWbillId ="vw00000000000000000003";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("pledgeContractId","972317627");
            map.put("virtualWbillId",virtualWbillId);
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("operatorId","321281199407232731");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://59.46.215.173:8999/wbc/api/wbill/unfreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",pledgerMemberId)
                    .add("pledgeeMemberId",pledgeeMemberId)
                    .add("memberType","0")
                    .add("clientId",clientId)
                    .add("pledgeContractId","972317627")
                    .add("virtualWbillId",virtualWbillId)
                    .add("operatorName","张三")
                    .add("operatorTel","18052125652")
                    .add("operatorId","321281199407232731")
                    .add("version","v1")
                    .add("requestId",requestId)
                    .add("timestamp",String.valueOf(timeNew))
                    .add("nonce",none)
                    .add("sign",map.get("sign"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *解质提交回调接口(临时测试hd)
     * @auth handi
     */
    @RequestMapping(value = "/unfreezeApi", method = RequestMethod.POST)
    public Result unfreezeApi(@RequestParam Map<String, Object> param){
        log.info("仓单质押解质接口入参：" + param);
        //申请ID
        String unfreezeId =String.valueOf(param.get("unfreezeId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(unfreezeId.equals("null") || unfreezeId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单质押解质失败!").put("resultCode","0");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(unfreezeId);
        billOperatorLogEntity.setTempId("17");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId("18");
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok("仓单质押解质成功!").put("resultCode","1");
    }
    //----解质止

    //----仓单注销始

    /**
     * 仓单注销
     * @auth handi
     * @return
     */
    @RequestMapping("/billCancel")
    public Result billCancel(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String whMemberId ="wh00006";
            //联盟客户ID
            String clientId ="c000000017";
            //联盟仓单ID
            String wbillId ="w0000000000000000013";
            //仓库仓单ID
            String whWbillId ="1";
            map.put("whMemberId",whMemberId);
            map.put("clientId",clientId);
            map.put("wbillId",wbillId);
            map.put("whWbillId",whWbillId);
            map.put("operatorName","张三");
            map.put("operatorTel","18052125652");
            map.put("clientContactName","李震");
            map.put("clientContactTel","18340817478");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://59.46.215.173:8999/wbc/api/wbill/"+wbillId+"?whMemberId="+whMemberId+"&clientId="+clientId+"&wbillId="+wbillId+"&whWbillId="+whWbillId+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&clientContactName="+map.get("clientContactName")+"&clientContactTel="+map.get("clientContactTel")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
            //String methodUrl ="";
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder()
                    .url(methodUrl)
                    .delete()
                    .build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *仓单注销回调接口(临时测试hd)
     * @auth handi
     */
    @RequestMapping(value = "/billCancelApi", method = RequestMethod.POST)
    public Result billCancelApi(@RequestParam Map<String, Object> param){
        log.info("仓单注销接口入参：" + param);
        //申请ID
        String wbillTmpId =String.valueOf(param.get("wbillTmpId"));
        //正式ID
        String wbillId =String.valueOf(param.get("wbillId"));
        //注销原因
        String reason =String.valueOf(param.get("reason"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(wbillTmpId.equals("null") || wbillTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(wbillId.equals("null") || wbillId.equals("")){
            return Result.error().put("msg","正式ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("仓单注销失败!").put("resultCode","0");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(wbillTmpId);
        billOperatorLogEntity.setTempId("19");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(wbillId);
        billOperatorLogEntity.setTempId("20");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(reason);
        billOperatorLogEntity.setTempId("21");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId("22");
        billService.updateOperatorLog(billOperatorLogEntity);
        //请求iot接口告知仓单ID
        try {
            String url="";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("wbillId",wbillId)
                    .add("status","false")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        }catch (IOException e){
            e.printStackTrace();
        }
        return Result.ok("仓单注销成功!").put("resultCode","1");
    }
    //----仓单注销止




    /**
     * 仓单质押冻结提交(小改名字)
     */
    @RequestMapping("/freezess")
    public Result freezess(@RequestBody BillPledgeEntity billPledgeEntity){
        //调用大商所接口
        Result result = Result.ok();
        Long  timeNew =  System.currentTimeMillis();
        String id = billPledgeEntity.getId();
        TreeMap<String,String> map = new TreeMap<>();
        map.put("pledgerMemberId",billPledgeEntity.getPledgerMemberId());
        map.put("memberType",billPledgeEntity.getMemberType());
        map.put("clientId",billPledgeEntity.getClientId());
        map.put("virtualWbillId",billPledgeEntity.getVirtualWbillId());
        map.put("pledgeContractAttach",String.valueOf(billPledgeEntity.getPledgeContractAttach()));
        map.put("pledgeBeginDate",billPledgeEntity.getPledgeBeginDate());
        map.put("pledgeEndDate",billPledgeEntity.getPledgeEndDate());
        map.put("operatorName",billPledgeEntity.getOperatorName());
        map.put("operatorTel",billPledgeEntity.getOperatorTel());
        map.put("operatorId",billPledgeEntity.getOperatorId());
        map.put("operatorIdAttach",String.valueOf(billPledgeEntity.getOperatorIdAttach()));
        map.put("version","v1");
        map.put("requestId",id);
        map.put("timestamp",String.valueOf(timeNew));
        map.put("nonce",String.valueOf(RandomStringUtils.randomAlphanumeric(20)));
        //按照参数名ASCII码从小到大排序拼接成字符串stringA。
        String stringA= SignatureUtil.genOrderedString(map);
        //对stringA使用SHA256算法运算作为数字摘要stringDigest
        String stringDigestA = DigestUtils.sha256Hex(stringA);
        MembersEntity member = membersService.queryKey(billPledgeEntity.getPledgerMemberId());
        //公私钥流
        String publicKeyBinary = String.valueOf(member.getZmemPublicKey());
        String PrivateKeyBinary = String.valueOf(member.getZmemPrivateKey());
        //公私钥字符串
        String publicKeyStr = SHA256Util.binaryToString(publicKeyBinary);
        String PrivateKeyStr = SHA256Util.binaryToString(PrivateKeyBinary);
        //公私钥
        PublicKey publicKey = SHA256Util.strToPub(publicKeyStr);
        PrivateKey privateKey = SHA256Util.strToPri(PrivateKeyStr);
        //数字签名
        byte[] signArr = SHA256Util.sign(privateKey,stringDigestA);
        String sign = SHA256Util.bytesToHexString(signArr);
        map.put("sign",sign);
        MediaType media = MediaType.parse("text/x-markdown charset=utf-8");
        String url="/wbc/api/wbill/freezeTx";
        String json = JSON.toJSONString(map);
        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody body =okhttp3.RequestBody.create(media,json);
        Request reqBuild = new Request
                .Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (Const.STATUS_CODE_202.equals(jsonObject.getString("auditState"))) {
                int lid = billService.seqBillOperatorlogId();
                //临时ID
                String freezeId= jsonObject.getString("freezeId");
                //String freezeId= "777";
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(lid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(freezeId);
                billOperatorLogEntity.setOperatorType(LogOper.FREEZING);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押冻结提交成功! 交易所审核中");
            } else {
                result = Result.error("与交易所通信异常,请联系管理员");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 仓单质押冻结提交回调接口方法(小改一下)
     */
    @RequestMapping("/freezeApiss")
    public Result freezeApiss(@RequestBody JSONObject jsonParam){
        if(Const.STATUS_CODE_202.equals(jsonParam.getString("auditState"))){
            //临时ID
            String freezeId = jsonParam.getString("freezeId");
            //String freezeId= "777";
            //审核结果
            String auditState = jsonParam.getString("auditState");
            //String auditState = "auditState5";
            //将审核状态更新入库
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setTempId(freezeId);
            billOperatorLogEntity.setAuditStatus(auditState);
            billService.updateOperatorLog(billOperatorLogEntity);
            //大商所同意预冻结撤销
            if(auditState.equals("yes")){
                //if(true){
                Map<String,Object> map = new HashMap<String, Object>();
                //将W_BILL_OPERATOR表中的OPERATORTYPE改为冻结状态码
                map.put("operatorType",Const.PLEDGE_FREEZING);
                map.put("tempId",freezeId);
                billService.updateVir(map);
            }

        }
        return Result.ok("冻结成功");
    }




    /**
     * 给中心端提供接口接收锁货状态值
     */
    @RequestMapping ("/preFreezingTes")
    public Result preFreezingTes(@RequestBody JSONObject jsonParam){
        Result result = Result.ok();
        //返回的状态信息
        //String msg = jsonParam.getString("statusCode");
        String msg = "已锁货";
        //操作表的主键ID
        //String id= jsonParam.getString("id");
        String id = "100";
        if(msg.equals("已锁货")){
            //先去本地operator表中将OPERATORTYPE字段状态修改为"预冻结中"
            BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
            billOperatorEntity.setId(id);
            billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_FREEZING);
            billService.update(billOperatorEntity);
            //锁货通过后，查询operator表的信息需要提交给大商所进行预冻结
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            List<BillFlowEntity> flowList = billService.queryFlow(id);
            BillOperatorEntity billOperatorEntity1 = billService.queryPreFreezing(id);
            //将flowList转为json字符串
            Map parentWbillMap = new HashMap();
            for(int s=0;s<flowList.size();s++){
                parentWbillMap.put(flowList.get(s).getWbillId(),flowList.get(s).getWeight());
            }
            JSONArray js = JSONArray.fromObject(parentWbillMap);
            String  parentWbill=js.getJSONObject(0).toString();

            Map<String,String> map = new HashMap<>();
            map.put("pledgerMemberId",billOperatorEntity1.getPledgerMemberId());
            map.put("memberType",String.valueOf(billOperatorEntity1.getMemberType()));
            map.put("clientId",billOperatorEntity1.getClientId());
            map.put("parentWbill",parentWbill);
            map.put("operatorName",billOperatorEntity1.getOperatorName());
            map.put("operatorTel",billOperatorEntity1.getOperatorTel());
            map.put("operatorId",billOperatorEntity1.getOperatorId());
            map.put("version","v1");
            map.put("requestId","");
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", String.valueOf(RandomStringUtils.randomAlphanumeric(20)));
            //map.put("sign","");
            //ascii排序后的字符串
            try {
                String stringA= SignatureUtil.genOrderedString(map);
                String stringDigestA = DigestUtils.sha256Hex(stringA);
                //请求大商所的预冻结接口
                //TODO  私钥加密  sign





                MediaType media = MediaType.parse("text/x-markdown;charset=utf-8");
                String url="/wbc/api/wbill/prefreezeTx";
                String jso = JSONObject.toJSONString(map);
                OkHttpClient client = new OkHttpClient();
                okhttp3.RequestBody body = okhttp3.RequestBody.create(media,jso);
                Request reqBuild= new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(reqBuild).execute();
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                if (Const.STATUS_CODE_202.equals(jsonObject.getString("statusCode"))) {
                    result = Result.ok("仓单质押预冻结提交成功! 交易所审核中");
                }else {
                    result =  Result.error("与交易所通信异常,请联系管理员");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
       /* BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
        billOperatorEntity.setId("10086");
        billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_FREEZING);//货物预冻结状态
        billOperatorEntity.setPreFreezeId("1101");
        billOperatorEntity.setPledgerMemberId("0010");
        billOperatorEntity.setMemberType("1");
        billOperatorEntity.setClientId("22");
        billOperatorEntity.setPledgerContractId("33");
        billOperatorEntity.setOperatorName("55");
        billOperatorEntity.setOperatorTel("56");
        billOperatorEntity.setOperatorId("57");
        //billOperatorEntity.setOperatorIdAttach(new byte[2]);
        //billOperatorEntity.setPledgeContractAttach(new byte[3]);
        //billOperatorEntity.setPledgeBeginDate("58");
        //billOperatorEntity.setPledgeEndDate("59");
        billOperatorEntity.setVersion("60");
        billOperatorEntity.setIsDelete("0");
        billService.preFreezing(billOperatorEntity);
        //
        BillFlowEntity billFlowEntity = new BillFlowEntity();
        billFlowEntity.setId("1");
        billFlowEntity.setBillOperatorId("10086");
        billFlowEntity.setMemberId("0010");
        billFlowEntity.setClientId("22");
        billFlowEntity.setWbillId("1111");
        billFlowEntity.setWbillStatus("5");
        billFlowEntity.setTimeCreate(new java.util.Date());
        billFlowEntity.setWeight("200");
        billFlowEntity.setIsDelete("0");
        billService.preFreezingFlow(billFlowEntity);
        return Result.ok();*/
        return result;
    }

    /**
     * 仓单质押预冻结提交回调接口方法(名字先改一下)
     */
    @RequestMapping("/preFreezingApiss")
    public Result preFreezingApiss(@RequestBody JSONObject jsonParam){
        //接口传回的接受预冻结的结果
        String msg = jsonParam.getString("msg");
        String preFreezeId = jsonParam.getString("virtualWbillId");
        //若为false将中心端调用的接口中的flag参数改变，告诉中心端这笔仓单交易需要恢复为解锁状态
        if(msg.equals("成功")){
            //先去本地operator表中将OPERATORTYPE字段状态修改为"预冻结状态"
            BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
            billOperatorEntity.setId("100");
            //流水号
            billOperatorEntity.setVirtualWbillId(preFreezeId);
            billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_FREEZING);
            billService.update(billOperatorEntity);
        }else{
            //大商所返回结果，预冻结失败
            messageReturn("预冻结失败");
        }

        return Result.ok().put("msg",msg);
    }


    /**
     * 失败后通知大商所的回调接口
     */
    @RequestMapping("/messageReturn")
    public String messageReturn(String msg){
        BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
        billOperatorEntity.setOperatorType(Const.OTHER);
        billService.update(billOperatorEntity);
        return msg;
    }
//----数字粮库接口

    /**
     *仓单锁货信息查询接口(临时测试hd)
     */
    @RequestMapping(value = "/lockGoods", method = RequestMethod.GET)
    public Result lockGoods(String id){
        log.info("仓单锁货信息查询接口入参id：" + id);
        if(id == null || id.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("wh001","30");
        map.put("wh002","100");
        Map<String,Object> mapf = new HashMap<String, Object>();
        mapf.put("varietyName","m");
        mapf.put("id","13");
        mapf.put("goodsInfo",map);
        //String parag = StringEscapeUtils.unescapeJava(paramf);
        return Result.ok().put("data",mapf);
    }


    /**
     *仓单上锁结果返回接口(临时测试hd)
     */
    @RequestMapping(value = "/lockResults", method = RequestMethod.POST)
    public Result lockResults(@RequestParam Map<String, Object> param){
        log.info("仓单上锁结果返回接口入参：" + param);
        //请求ID
        String id =String.valueOf(param.get("id"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(id.equals("null") || id.equals("")){
            return Result.error().put("msg","请求ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(id);
        billOperatorLogEntity.setTempId("29");
        billService.updateOperatorLog(billOperatorLogEntity);

        billOperatorLogEntity.setAuditStatus(auditState);
        billOperatorLogEntity.setTempId("30");
        billService.updateOperatorLog(billOperatorLogEntity);
        return Result.ok().put("resultCode","1");
    }

    /**
     *仓单解锁结果返回接口(临时测试hd)
     */
    @RequestMapping(value = "/unlockGoods", method = RequestMethod.POST)
    public Result unlockGoods(@RequestParam Map<String, Object> param){
        log.info("仓单解锁结果返回接口入参：" + param);
        //请求ID
        String id =String.valueOf(param.get("id"));
        if(id.equals("null") || id.equals("")){
            return Result.error().put("msg","请求ID不能为空");
        }
        /*BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
        billOperatorLogEntity.setAuditStatus(id);
        billOperatorLogEntity.setTempId("19");
        billService.updateOperatorLog(billOperatorLogEntity);*/
        return Result.ok("可以解锁!").put("resultCode","0");
    }



}