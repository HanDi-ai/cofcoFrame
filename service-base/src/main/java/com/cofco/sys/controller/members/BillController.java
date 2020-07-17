package com.cofco.sys.controller.members;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.constant.Const;
import com.cofco.constant.LogOper;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.members.*;
import com.cofco.sys.service.members.BillService;
import com.cofco.sys.service.members.FreezeService;
import com.cofco.sys.service.members.MembersService;
import com.cofco.utils.*;
import net.sf.json.JSONArray;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.noggit.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

import java.net.URLEncoder;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 仓单业务管理
 * @author handi
 * @date 2019 11/05 13:54:30
 * @email 876053009@qq.com
 */
@RestController
@RequestMapping("sys/bill")
public class BillController extends BaseController {
    @Autowired
    private BillService billService;
    @Autowired
    private MembersService membersService;
    @Autowired
    private FreezeService freezeService;
    Logger log = Logger.getLogger(MembersController.class);
    /**
     * 仓单查询
     */
    @RequestMapping("/queryList")
    public Result queryList(@RequestParam Map<String, Object> params){
        params.put("varietyId","1073611");
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(),query.getLimit());
        //因视图无数据，自写了一条数据---
        /*BillEntity billEntity = new BillEntity();
        billEntity.setId(101);
        billEntity.setVarietyId("2");
        billEntity.setVarietyName("44");
        billEntity.setClientContactName("66");
        billEntity.setClientContactTel("18940");
        billEntity.setWbillType("1");
        List<BillEntity> roleList =new ArrayList<BillEntity>();
        roleList.add(billEntity);
        PageHelper.endPage();
        int total = 1;*/
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
    public Result info(@PathVariable("id") String id){
        BillEntity bill = billService.queryObject(id);
        /*BillEntity bill = new BillEntity();
        bill.setId(101);
        bill.setVarietyId("2");
        bill.setVarietyName("44");
        bill.setClientContactName("66");
        bill.setClientContactTel("18940");
        bill.setWbillType("1");*/
        return Result.ok().put("role",bill);
    }

    //联调测试时候使用--------------------------------------------------
    /**
     *仓单备案提交
     */
    @RequestMapping("/save")
    public Result save(@RequestBody BillEntity billEntity){
        Result result = Result.ok();
        int id = billService.seqBillId();
        //联盟成员ID
        //String whMemberID = "wh00379";
        String whMemberID = "wh00324";
        String requestId =SHA256Util.getUUID32();
        billEntity.setWhMemberId(whMemberID);
        billEntity.setVersion("v1");
        billEntity.setId(id);
        billEntity.setTime_Create(new java.util.Date());
        billEntity.setCreator(UserUtils.getCurrentUserId());
        billEntity.setIs_Delete(Integer.valueOf(Const.DELETE_FLG_NO));
        /*billEntity.setInspectedBeginDate("20191201");
        billEntity.setInspectedEndDate("20201231");
        billEntity.setStoreBeginDate("20190102");
        billEntity.setStoreEndDate("20201231");
        billEntity.setInsuranceBeginDate("20191205");
        billEntity.setInsuranceEndDate("20201231");*/
        try{
            SortedMap map = SignatureUtil.entityToMap(billEntity);
            Long  timeNew =  System.currentTimeMillis();
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
           /* map.put("inspectedBeginDate","20191201");
            map.put("inspectedEndDate","20991231");
            map.put("insuranceBeginDate","20191205");
            map.put("insuranceEndDate","20991231");*/
            map.put("storeBeginDate",billEntity.getStoreBeginDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("storeEndDate","20991231");
            map.put("lossStandard","{}");
            map.put("markContent","{}");
            map.remove("inspectedBeginDate");
            map.remove("inspectedEndDate");
            map.remove("insuranceBeginDate");
            map.remove("insuranceEndDate");
            map.remove("inspectionOrgName");
            map.remove("inspectionOrgNo");
            map.remove("inspectionAttach");
            map.remove("insuranceOrg");
            map.remove("insuranceOrgNo");
            map.remove("insuranceContact");
            map.remove("insuranceContactTel");
           /* map.put("inspectedBeginDate",billEntity.getInspectedBeginDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("inspectedEndDate",billEntity.getInspectedEndDate().replaceAll("[[\\s-:punct:]]",""));

            map.put("storeBeginDate",billEntity.getStoreBeginDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("storeEndDate",billEntity.getStoreEndDate().replaceAll("[[\\s-:punct:]]",""));

            map.put("insuranceBeginDate",billEntity.getInsuranceBeginDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("insuranceEndDate",billEntity.getInsuranceEndDate().replaceAll("[[\\s-:punct:]]",""));*/
            //噸位信息
            LocationInfoEntity locationInfoEntity = new LocationInfoEntity();
            //仓位ID
            locationInfoEntity.setPositionId("159");
            //垛位ID
            locationInfoEntity.setLocationId("159");
            //仓位类型
            locationInfoEntity.setPositionType("1");
            //最大存储量
            locationInfoEntity.setMaxCapacity("10000");
            //重量
            locationInfoEntity.setWeight("1522.4");

            List<LocationInfoEntity.InitCapacityBean> initCapacityBeansList = new ArrayList<LocationInfoEntity.InitCapacityBean>();
            LocationInfoEntity.InitCapacityBean initCapacityBean = new LocationInfoEntity.InitCapacityBean();
            initCapacityBean.setWhWbillId("wh00001");
            initCapacityBean.setWeight(0);
            initCapacityBeansList.add(initCapacityBean);
            //垛位里初始粮食重量
            locationInfoEntity.setInitCapacity(initCapacityBeansList);
            locationInfoEntity.setBaseSize(100);
            locationInfoEntity.setMaxHeight(10);
            //iotDevices
            List<LocationInfoEntity.IotDevicesBean> iotDevicesBeansList = new ArrayList<LocationInfoEntity.IotDevicesBean>();
            LocationInfoEntity.IotDevicesBean iotDevicesBean = new LocationInfoEntity.IotDevicesBean();
            iotDevicesBean.setDeviceId("D0CF5EFFFEB64AA3_2821051994031645");
            iotDevicesBean.setDeviceType("1");
            iotDevicesBean.setMacAddr("1.2.3.4");
            iotDevicesBean.setIotValue("-6.56");
            iotDevicesBean.setRawValue("-6.56");
            LocationInfoEntity.IotDevicesBean iotDevicesBean1 = new LocationInfoEntity.IotDevicesBean();
            iotDevicesBean1.setDeviceId("D0CF5EFFFEB64AA3_28210519943B2123");
            iotDevicesBean1.setDeviceType("1");
            iotDevicesBean1.setMacAddr("1.2.3.4");
            iotDevicesBean1.setIotValue("7.75");
            iotDevicesBean1.setRawValue("7.75");
            LocationInfoEntity.IotDevicesBean iotDevicesBean2 = new LocationInfoEntity.IotDevicesBean();
            iotDevicesBean2.setDeviceId("D0CF5EFFFEB64AA3_28210519976CC9F5");
            iotDevicesBean2.setDeviceType("1");
            iotDevicesBean2.setMacAddr("1.2.3.4");
            iotDevicesBean2.setIotValue("-4.56");
            iotDevicesBean2.setRawValue("-4.56");
            LocationInfoEntity.IotDevicesBean iotDevicesBean3 = new LocationInfoEntity.IotDevicesBean();
            iotDevicesBean3.setDeviceId("D0CF5EFFFEB64AA3_282107199414185B");
            iotDevicesBean3.setDeviceType("1");
            iotDevicesBean3.setMacAddr("1.2.3.4");
            iotDevicesBean3.setIotValue("-9.06");
            iotDevicesBean3.setRawValue("-9.06");

            LocationInfoEntity.IotDevicesBean iotDevicesBean4 = new LocationInfoEntity.IotDevicesBean();
            iotDevicesBean4.setDeviceId("581749033");
            iotDevicesBean4.setDeviceType("0");
            iotDevicesBean4.setMacAddr("1.2.3.4");
            iotDevicesBean4.setIotValue("01031A3B7800003B7800000EDE00010001000000000000000000030000FDF6");
            //扫描仪点位坐标具体信息
            List<BillIotEntity> billIotEntityList = new ArrayList<BillIotEntity>();
            BillIotEntity billIotEntity = new BillIotEntity();
            billIotEntity.setScode("1-40");
            billIotEntity.setIotValue("0103F0012C008C089C014D008C0877016E008C0AA7018F008C0B1301B0008C0B6B01D1008C0B5E01F2008C0B70012C00AD0B75014D00AD0B99016E00AD0BA3018F00AD0BA201B000AD0BA001D100AD0B9F01F200AD0B9E012C00CE0BB0014D00CE0BB0016E00CE0BB0018F00CE0BB001B000CE0BB001D100CE0BB001F200CE0BB0012C00EF0BB7014D00EF0BB7016E00EF0BB7018F00EF0BB701B000EF0BB701D100EF0BB701F200EF0BB7012C01100BB7014D01100BB7016E01100BB7018F01100BB701B001100BB701D101100BB701F201100BB7012C006B0889014D006B0A32016E006B0AFC018F006B0B5701B0006B0B119194");
            BillIotEntity billIotEntity1 = new BillIotEntity();
            billIotEntity1.setScode("41-80");
            billIotEntity1.setIotValue("0103F001D1006B093501F2006B0913012C004A0876014D004A0874016E004A086A018F004A085E01B0004A08EF01D1004A08F801F2004A08D7012C00290981014D00290866016E00290861018F002908B501B00029084C01D10029084001F20029089B012C0008097D014D00080906016E000808E5018F000808E901B0000808A201D10008088001F20008085F010B006B0BB800EA006B0BB800C9006B0BB800A8006B0BB80087006B0BB80066006B0BB80045006B0BB80024006B0BB80003006B0BB8010B004A0BB800EA004A0BB800C9004A0BB800A8004A0BB80087004A0BB80066004A0BB80045004A0BB80024004A0BB80573");
            billIotEntityList.add(billIotEntity);
            billIotEntityList.add(billIotEntity1);
            Map<String,String> rawMap = new HashMap<>();
            for(int i =0 ;i<billIotEntityList.size();i++){
                rawMap.put(billIotEntityList.get(i).getScode(),billIotEntityList.get(i).getIotValue());
            }
            String rawVal = JSON.toJSONString(rawMap);
            iotDevicesBean4.setRawValue(rawVal);
            //温度仪
            iotDevicesBeansList.add(iotDevicesBean);
            iotDevicesBeansList.add(iotDevicesBean1);
            iotDevicesBeansList.add(iotDevicesBean2);
            iotDevicesBeansList.add(iotDevicesBean3);
            //激光扫描仪
            iotDevicesBeansList.add(iotDevicesBean4);
            locationInfoEntity.setIotDevices(iotDevicesBeansList);

            LocationInfoEntity[] locationInfoEntities = new LocationInfoEntity[1];
            locationInfoEntities[0] = locationInfoEntity;
            String locationInfo = JSON.toJSONString(locationInfoEntities);
            map.put("locationInfo",locationInfo.trim());
            //品质
            /*String photo = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a\n" +
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
                    "ozj2pR6U4DI/pSAjAJp4Bzx+NKAQ3TFW4thxjGfQ0mykis";*/
            String qua = "{\"1522.4\":\"一等\"}";
            JSON qual = JSON.parseObject(qua);
            String quality =JSON.toJSONString(qual);
            map.put("quality",quality);
            //map.put("inspectionAttach",PhotoUtils.getImageString(billEntity.getInspectionAttach(), PhotoUtils.getImageType(billEntity.getInspectionAttach())));
            log.info("生成签名前的参数值：" + map);
            //MembersEntity membersEntity = membersService.queryKey(whMemberID);
            //map.put("sign", SHA256Util.createSignT(map,membersEntity));
            map.put("sign", SHA256Util.createSign(map));
            log.info("生成签名后的参数值：" + map);
            //请求大商所接口
            //老IP
            //String url="http://59.46.215.173:8999/wbc/api/wbill";
            //新IP
            String url="http://124.93.249.143:8999/wbc/api/wbill";
            //String url="";
            String sig = String.valueOf(map.get("sign"));
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
                    .add("quality",String.valueOf(map.get("quality")))
                    .add("isInspected",String.valueOf(map.get("isInspected")))
                    /*.add("inspectionOrgName",String.valueOf(map.get("inspectionOrgName")))
                    .add("inspectionOrgNo",String.valueOf(map.get("inspectionOrgNo")))
                    .add("inspectionAttach",String.valueOf(map.get("inspectionAttach")))
                    .add("inspectedBeginDate",String.valueOf(map.get("inspectedBeginDate")))
                    .add("inspectedEndDate",String.valueOf(map.get("inspectedEndDate")))*/
                    .add("locationInfo",String.valueOf(map.get("locationInfo")))
                    .add("storeBeginDate",String.valueOf(map.get("storeBeginDate")))
                    .add("storeEndDate",String.valueOf(map.get("storeEndDate")))
                    .add("storageFee",String.valueOf(map.get("storageFee")))
                    .add("lossStandard",String.valueOf(map.get("lossStandard")))
                    .add("operatorName",String.valueOf(map.get("operatorName")))
                    .add("operatorTel",String.valueOf(map.get("operatorTel")))
                    .add("clientId",String.valueOf(map.get("clientId")))
                    .add("clientContactName",String.valueOf(map.get("clientContactName")))
                    .add("clientContactTel",String.valueOf(map.get("clientContactTel")))
                    .add("isInsurance",String.valueOf(map.get("isInsurance")))
                    /*.add("insuranceOrg",String.valueOf(map.get("insuranceOrg")))
                    .add("insuranceOrgNo",String.valueOf(map.get("insuranceOrgNo")))
                    .add("insuranceContact",String.valueOf(map.get("insuranceContact")))
                    .add("insuranceContactTel",String.valueOf(map.get("insuranceContactTel")))
                    .add("insuranceBeginDate",String.valueOf(map.get("insuranceBeginDate")))
                    .add("insuranceEndDate",String.valueOf(map.get("insuranceEndDate")))*/
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
            log.info("响应结果：" + jsonObject);
            int responseCode = response.code();
            log.info("responseCode：" + responseCode);
            if (Const.STATUS_CODE_202.equals(String.valueOf(responseCode))) {
            //if(true){
                //将仓单信息存入本地库
                billEntity.setQuality("1");
                billEntity.setInspectionAttach("");
                billEntity.setLocationInfo("2");
                billEntity.setLossStandard("3");
                billService.save(billEntity);
                //仓单备案入库失败
                /*if(t == 0){
                    return Result.error().put("msg","仓单信息有误,入库失败");
                }*/
                //临时ID
                String wbillTmpId = jsonObject.getJSONObject("data").getString("wbillTmpId");
                //String wbillTmpId = "5005";
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
                return Result.ok("仓单备案回调成功!").put("resultCode","1");
            }
            try {
                //将审批结果更新到log表
                BillLogEntity billLogEntity = new BillLogEntity();
                billLogEntity.setTempId(wbillTmpId);
                billLogEntity.setAuditStatus(auditState);
                billService.updateLog(billLogEntity);
                //将仓单ID更新到bill表
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("wbillId", wbillId);
                map.put("wbillTmpId", wbillTmpId);
                billService.updateWbillId(map);
            }catch(Exception e) {
                System.out.print(e + "================仓单备案回调异常信息");
                e.printStackTrace();
            }
        return Result.ok("仓单备案回调成功!").put("resultCode","1");
    }

    //----预冻结起
    /**
     *预冻结提交
     */
    @RequestMapping("/preFreTes")
    public Result preFreTes(){
        Result result = Result.ok();
        try{
        TreeMap<String,String> map = new TreeMap<>();
        Map<String,Object> hsMap = new HashMap<>();
        String wbillId = "w0000000000000000164";
        hsMap.put(wbillId,"300");
        String  param= JSON.toJSONString(hsMap);
        String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        String requestId = SHA256Util.getUUID32();
        Long  timeNew =  System.currentTimeMillis();
        //String clientId ="c000000176";
            String clientId ="c000000156";
        //String pledgerMemberId ="wh00379";
            String pledgerMemberId ="wh00324";
        String pledgeeMemberId ="bk00369";
        map.put("pledgerMemberId",pledgerMemberId);
        map.put("pledgeeMemberId",pledgeeMemberId);
        map.put("memberType","0");
        map.put("clientId",clientId);
        map.put("parentWbill",param);
        map.put("operatorName","改革");
        map.put("operatorTel","13304111566");
        map.put("operatorId","220303198310263213");
        map.put("version","v1");
        map.put("requestId",requestId);
        map.put("timestamp",String.valueOf(timeNew));
        map.put("nonce", none);
        map.put("sign", SHA256Util.createSign(map));
        log.info("请求参数：" + map);
        //String url="http://59.46.215.173:8999/wbc/api/wbill/prefreezeTx";
            String url="http://124.93.249.143:8999/wbc/api/wbill/prefreezeTx";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
            .add("pledgerMemberId",pledgerMemberId)
            .add("pledgeeMemberId",pledgeeMemberId)
            .add("memberType","0")
            .add("clientId",clientId)
            .add("parentWbill",param)
            .add("operatorName","改革")
            .add("operatorTel","13304111566")
            .add("operatorId","220303198310263213")
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
            int responseCode = response.code();
            log.info("responseCode：" + responseCode);
            if (Const.STATUS_CODE_202.equals(String.valueOf(responseCode))) {
                int cid = billService.seqBillOperatorlogId();
                //仓单在数据表中的ID
                String wbillOpeId = "77";
                String prefreezeId = jsonObject.getJSONObject("data").getString("prefreezeId");
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(wbillOpeId);
                billOperatorLogEntity.setTempId(prefreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.PREFREEZING_SUB);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押预冻结提交成功!"+jsonObject.get("msg").toString());
            }else {
                result = Result.ok("仓单质押预冻结失败!"+jsonObject.get("msg").toString());
            }
        } catch (IOException e) {
        e.printStackTrace();
        return Result.error();
        }
        return result;
        }

    /**
     *预冻结提交回调接口
     * @auth Handi
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
        //为0说明预冻结信息不匹配，需要把operator表中对应的此条数据删除
        if(auditState.equals("0")){
            //将状态改为预冻结被交易所驳回的状态
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("is_delete","1");
            map.put("virtualWbillId", virtualWbillId);
            map.put("operatorType",Const.PRE_FREEZING_FAIL);
            map.put("tempId", prefreezeId);
            billService.delete(map);
            //将flow表中那笔仓单的值删除
            BillFlowEntity billFlowEntity = new BillFlowEntity();
            billFlowEntity.setIsDelete("1");
            billFlowEntity.setBillOperatorId(prefreezeId);
            billService.preFreedelFlow(billFlowEntity);
            //将审核状态更新入库
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setTempId(prefreezeId);
            billOperatorLogEntity.setAuditStatus(auditState);
            billService.updateOperatorLog(billOperatorLogEntity);
            return Result.ok("仓单质押预冻结已被驳回!").put("resultCode","1");
        }
        //将虚拟仓单ID更新入库
        try {
           Map<String, Object> map = new HashMap<String, Object>();
            map.put("virtualWbillId", virtualWbillId);
            map.put("operatorType", Const.PRE_FREEZING_SUCCESS);
            map.put("tempId", prefreezeId);
            billService.updateVir(map);
            //将审核状态更新入库
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setTempId(prefreezeId);
            billOperatorLogEntity.setAuditStatus(auditState);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e){
            System.out.print(e.getMessage()+"================预冻结回调异常信息");
            e.printStackTrace();
        }
        return Result.ok("仓单质押预冻结成功!").put("resultCode","1");
    }



    //------预冻结撤销起
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
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String memberId ="wh00379";
            //预冻结流水号
            String prefreezeId ="219";
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
            //String methodUrl ="http://59.46.215.173:8999/wbc/api/wbill/prefreezeTx/?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&prefreezeId="+map.get("prefreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
            String methodUrl ="http://124.93.249.143:8999/wbc/api/wbill/prefreezeTx/?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&prefreezeId="+map.get("prefreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //仓单在数据表中的ID
                String id ="77";
                //1.将它从operator表中删除
                /*BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setIsDelete("1");
                billOperatorEntity.setId(id);
                billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_FREEZING_REVOKE);
                billService.update(billOperatorEntity);*/
                // 2.在operator_log表中新增预冻结撤销记录值
                String prefreezeCancleId = jsonObject.getJSONObject("data").getString("prefreezeCancleId");
                int cid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(prefreezeCancleId);
                billOperatorLogEntity.setOperatorType(LogOper.REVOKE_PREFREEZING);
                billService.saveOperation(billOperatorLogEntity);
                //3.将flow表中那笔仓单的值删除
                /*BillFlowEntity billFlowEntity = new BillFlowEntity();
                billFlowEntity.setIsDelete("1");
                billFlowEntity.setBillOperatorId(id);
                billService.delFlow(billFlowEntity);*/
                result = Result.ok("仓单预冻结撤销成功!"+jsonObject.get("msg").toString());
            }else{
                result =  Result.error("仓单预冻结撤销失败!"+jsonObject.get("msg").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *仓单预冻结撤销回调接口(临时测试hd)
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
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(prefreezeCancleId);
            billService.updateOperatorLog(billOperatorLogEntity);
            //将状态改为质押预冻结撤销被交易所驳回的状态
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType",Const.PLEDGE_PRE_FREEZING_REVOKE_FAIL);
            map.put("tempId", prefreezeCancleId);
            billService.delete(map);
            return Result.error("仓单预冻结撤销失败!").put("resultCode","1");
        }
        try{
            //将状态改为预冻结撤销的状态，并从operator表中删除
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("is_delete","1");
            map.put("operatorType",Const.PLEDGE_PRE_FREEZING_REVOKE_SUCCESS);
            map.put("tempId", prefreezeCancleId);
            billService.delete(map);
            //将flow表中那笔仓单的值删除
            BillFlowEntity billFlowEntity = new BillFlowEntity();
            billFlowEntity.setIsDelete("1");
            billFlowEntity.setBillOperatorId(prefreezeCancleId);
            billService.preFreedelFlow(billFlowEntity);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(prefreezeCancleId);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e) {
            System.out.print(e + "================仓单预冻结撤销回调异常信息");
            e.printStackTrace();
        }
        return Result.ok("仓单预冻结撤销成功!").put("resultCode","1");
    }

    /**
     *仓单质押冻结(临时测试hd)
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
            String clientId ="c000000156";
            String pledgerMemberId ="wh00324";
            String pledgeeMemberId ="bk00369";
            //虚拟仓单id
            String virtualWbillId ="vw00000000000000000886";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("virtualWbillId",virtualWbillId);
            map.put("pledgeContractAttach",photo);
            map.put("pledgeBeginDate","20200609");
            map.put("pledgeEndDate","20200731");
            map.put("operatorName","改革");
            map.put("operatorTel","13304111566");
            map.put("operatorId","220303198310263213");
            map.put("operatorIdAttach",photo);
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            //String url="http://59.46.215.173:8999/wbc/api/wbill/freezeTx";
            String url="http://124.93.249.143:8999/wbc/api/wbill/freezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",pledgerMemberId)
                    .add("pledgeeMemberId",pledgeeMemberId)
                    .add("memberType","0")
                    .add("clientId",clientId)
                    .add("virtualWbillId",virtualWbillId)
                    .add("pledgeContractAttach",photo)
                    .add("pledgeBeginDate","20200609")
                    .add("pledgeEndDate","20200731")
                    .add("operatorName","改革")
                    .add("operatorTel","13304111566")
                    .add("operatorId","220303198310263213")
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //仓单在数据表中的ID
                String id ="77";
                //1.修改operator表中此条数据的状态
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setOperatorType(Const.PLEDGE_FREEZING);
                billOperatorEntity.setId(id);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增冻结记录值
                int cid = billService.seqBillOperatorlogId();
                String freezeId = jsonObject.getJSONObject("data").getString("freezeId");
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(freezeId);
                billOperatorLogEntity.setOperatorType(LogOper.FREEZING);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押冻结成功!"+jsonObject.get("msg").toString());
            } else {
                result = Result.error("仓单质押冻结失败!"+jsonObject.get("msg").toString());
            }
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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_FREEZING_FAIL);
            map.put("tempId", freezeId);
            billService.updateVir(map);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(freezeId);
            billService.updateOperatorLog(billOperatorLogEntity);
            return Result.error("仓单质押冻结失败!").put("resultCode","1");
        }
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_FREEZING_SUCCESS);
            map.put("tempId", freezeId);
            billService.updateVir(map);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(freezeId);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e) {
            System.out.print(e.getMessage() + "================冻结提交回调异常信息");
            e.printStackTrace();
        }
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
            String wbillId="w0000000000000000163";
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            SortedMap<String, String> requestMapf = new TreeMap<>();
            requestMapf.put("memberId","wh00379");
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
            String methodUrl ="http://124.93.249.143:8999/wbc/api/wbill/"+wbillId+"?memberId="+memberId+"&memberType="+memberType+"&version="+version
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
     *仓单质押预解质
     * @auth handi
     * @date 2019/12/28
     */
    @RequestMapping("/preUnfreeze")
    public Result preUnfreeze(){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            Map<String,Object> hsMap = new HashMap<>();
            String wbillId = "w0000000000000000164";
            hsMap.put(wbillId,"300");
            String  parentWbillUnfreeze= JSON.toJSONString(hsMap);
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String clientId ="c000000156";
            String pledgerMemberId ="wh00324";
            String pledgeeMemberId ="bk00369";
            //虚拟仓单id
            String virtualWbillId ="vw00000000000000000886";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("pledgeContractId","972317627");
            map.put("virtualWbillId",virtualWbillId);
            map.put("parentWbillUnfreeze",parentWbillUnfreeze);
            map.put("operatorName","改革");
            map.put("operatorTel","13304111566");
            map.put("operatorId","220303198310263213");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            //String url="http://59.46.215.173:8999/wbc/api/wbill/preUnfreezeTx";
            String url="http://124.93.249.143:8999/wbc/api/wbill/preUnfreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",pledgerMemberId)
                    .add("pledgeeMemberId",pledgeeMemberId)
                    .add("memberType","0")
                    .add("clientId",clientId)
                    .add("pledgeContractId","972317627")
                    .add("virtualWbillId",virtualWbillId)
                    .add("parentWbillUnfreeze",parentWbillUnfreeze)
                    .add("operatorName","改革")
                    .add("operatorTel","13304111566")
                    .add("operatorId","220303198310263213")
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
                if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                String id = "77";
                ///1.修改operator表中此条数据的状态
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_UNFREEZE);
                billOperatorEntity.setId(id);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增预解质记录值
                int cid = billService.seqBillOperatorlogId();
                String preunfreezeId = jsonObject.getJSONObject("data").getString("preunfreezeId");
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(preunfreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.PRE_UNFREEZETX);
                billService.saveOperation(billOperatorLogEntity);
                    result = Result.ok("仓单质押预解质提交成功!"+jsonObject.get("msg").toString());
            }else{
                    result =  Result.error("仓单质押预解质失败!"+jsonObject.get("msg").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *预解质提交回调接口
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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_PRE_UNFREEZE_FAIL);
            map.put("tempId", preunfreezeId);
            billService.updateVir(map);
            return Result.ok("仓单质押预解质失败!").put("resultCode","1");
        }
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_PRE_UNFREEZE_SUCCESS);
            map.put("tempId", preunfreezeId);
            billService.updateVir(map);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(preunfreezeId);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e) {
            System.out.print(e + "================仓单预解质回调异常信息");
            e.printStackTrace();
        }
        return Result.ok("仓单质押预解质成功!").put("resultCode","1");
    }
    //----预解质止

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
            String clientId ="c000000156";
            String pledgerMemberId ="wh00324";
            String pledgeeMemberId ="bk00369";
            //虚拟仓单id
            String virtualWbillId ="vw00000000000000000886";
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("pledgeContractId","972317627");
            map.put("virtualWbillId",virtualWbillId);
            map.put("operatorName","改革");
            map.put("operatorTel","13304111566");
            map.put("operatorId","220303198310263213");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            //String url="http://59.46.215.173:8999/wbc/api/wbill/unfreezeTx";
            String url="http://124.93.249.143:8999/wbc/api/wbill/unfreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",pledgerMemberId)
                    .add("pledgeeMemberId",pledgeeMemberId)
                    .add("memberType","0")
                    .add("clientId",clientId)
                    .add("pledgeContractId","972317627")
                    .add("virtualWbillId",virtualWbillId)
                    .add("operatorName","改革")
                    .add("operatorTel","13304111566")
                    .add("operatorId","220303198310263213")
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                String id ="77";
                ///1.修改operator表中此条数据的状态
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setOperatorType(Const.PLEDGE_UNFREEZE);
                billOperatorEntity.setId(id);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增解质记录值
                int cid = billService.seqBillOperatorlogId();
                String unfreezeId = jsonObject.getJSONObject("data").getString("unfreezeId");
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(unfreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.UNFREEZETX);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押解质成功!"+jsonObject.get("msg").toString());
            }else{
                result = Result.error("仓单质押解质失败!"+jsonObject.get("msg").toString());
            }
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
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_UNFREEZE_FAIL);
            map.put("tempId", unfreezeId);
            billService.updateVir(map);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(unfreezeId);
            billService.updateOperatorLog(billOperatorLogEntity);
            return Result.error("仓单质押解质失败!").put("resultCode","1");
        }
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("is_delete","1");
            map.put("operatorType",Const.PLEDGE_UNFREEZE_SUCCESS);
            map.put("tempId", unfreezeId);
            billService.delete(map);
            //将flow表中那笔仓单的值删除
            BillFlowEntity billFlowEntity = new BillFlowEntity();
            billFlowEntity.setIsDelete("1");
            billFlowEntity.setBillOperatorId(unfreezeId);
            billService.preFreedelFlow(billFlowEntity);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(unfreezeId);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e) {
            System.out.print(e + "================仓单解质回调异常信息");
            e.printStackTrace();
        }
        return Result.ok("仓单质押解质成功!").put("resultCode","1");
    }
    //----解质止


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
            String memberId ="wh00324";
            //预解质ID
            String preunfreezeId ="2633";
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
            String methodUrl ="http://124.93.249.143:8999/wbc/api/wbill/preUnfreezeTx?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&preunfreezeId="+map.get("preunfreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                String id = "77";
                //1.将它的状态从operator表中改为预解质撤销中
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setId(id);
                billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_UNFREEZE_REVOKE);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增预解质撤销记录值
                String preunfreezeCancleId = jsonObject.getJSONObject("data").getString("preunfreezeCancleId");
                int cid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(preunfreezeCancleId);
                billOperatorLogEntity.setOperatorType(LogOper.PRE_UNFREEZE_CANCEL);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单预解质撤销成功!"+jsonObject.get("msg").toString());
            } else {
                result = Result.error("仓单预解质撤销失败!"+jsonObject.get("msg").toString());
            }
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
        //申请IDpreunfreezeCancleId
        String preunfreezeCancleId =String.valueOf(param.get("preunfreezeCancleId"));
        //审核状态auditState
        String auditState = String.valueOf(param.get("auditState"));
        if(preunfreezeCancleId.equals("null") || preunfreezeCancleId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_PRE_UNFREEZE_REVOKE_FAIL);
            map.put("tempId", preunfreezeCancleId);
            billService.updateVir(map);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(preunfreezeCancleId);
            billService.updateOperatorLog(billOperatorLogEntity);
            return Result.ok("仓单质押预解质撤销失败!").put("resultCode","1");
        }
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("operatorType", Const.PLEDGE_PRE_UNFREEZE_REVOKE_SUCCESS);
            map.put("tempId", preunfreezeCancleId);
            billService.updateVir(map);
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(preunfreezeCancleId);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e) {
            System.out.print(e + "================仓单预解质回调异常信息");
            e.printStackTrace();
        }
        return Result.ok("仓单预解质撤销成功!").put("resultCode","1");
    }
    //------预解质撤销止


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
            String whMemberId ="wh00324";
            //联盟客户ID
            String clientId ="c000000156";
            //联盟仓单ID
            String wbillId ="w0000000000000000162";
            //仓库仓单ID
            String whWbillId ="156";
            map.put("whMemberId",whMemberId);
            map.put("clientId",clientId);
            map.put("wbillId",wbillId);
            map.put("whWbillId",whWbillId);
            map.put("operatorName","韩迪");
            map.put("operatorTel","18940855709");
            map.put("clientContactName","韩迪");
            map.put("clientContactTel","18940855709");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://124.93.249.143:8999/wbc/api/wbill?whMemberId="+whMemberId+"&clientId="+clientId+"&wbillId="+wbillId+"&whWbillId="+whWbillId+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&clientContactName="+map.get("clientContactName")+"&clientContactTel="+map.get("clientContactTel")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                result = Result.ok("仓单注销成功!"+jsonObject.get("msg").toString());
            }else{
                result = Result.ok("仓单注销失败!"+jsonObject.get("msg").toString());
            }
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
       /* if(auditState.equals("0")){
            return Result.error("仓单注销失败!").put("resultCode","0");
        }*/
        try{
            BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
            billOperatorLogEntity.setAuditStatus(auditState);
            billOperatorLogEntity.setTempId(wbillTmpId);
            billService.updateOperatorLog(billOperatorLogEntity);
        }catch(Exception e) {
            System.out.print(e + "================仓单注销回调异常信息");
            e.printStackTrace();
        }
        //请求iot接口告知仓单ID
        return Result.ok("仓单注销成功!").put("resultCode","1");
    }
    //----仓单注销止


    /**
     * 仓库查询所有银行的memberId
     * @return
     */
    @RequestMapping("/banks")
    public Result queryBanks(){
        try {
            TreeMap<String, String> map = new TreeMap<>();
            //随机数
            String none = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            //请求ID
            String requestId = SHA256Util.getUUID32();
            //时间戳
            Long timeNew = System.currentTimeMillis();
            map.put("memberId", "wh00379");
            map.put("version", "v1");
            map.put("requestId", requestId);
            map.put("timestamp", String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign, "UTF-8");
            String methodUrl = "http://124.93.249.143:8999/wbc/api/raw/banks?memberId="+map.get("memberId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+map.get("timestamp")+"&nonce="+map.get("nonce")+"&sign="+signn;
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder().
                    url(methodUrl).
                    build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.ok();
    }

    /**
     * 仓库根据银行名字模糊查询银行memberId
     * @return
     */
    @RequestMapping("/fuzzyQuery")
    public Result fuzzyQuery(){
        try {
            TreeMap<String, String> map = new TreeMap<>();
            //随机数
            String none = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            //请求ID
            String requestId = SHA256Util.getUUID32();
            //时间戳
            Long timeNew = System.currentTimeMillis();
            //银行名称
            String bankName ="二";
            map.put("memberId", "wh00379");
            map.put("version", "v1");
            map.put("requestId", requestId);
            map.put("timestamp", String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign, "UTF-8");
            String methodUrl = "http://124.93.249.143:8999/wbc/api/raw/bank/"+bankName+"?memberId="+map.get("memberId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+map.get("timestamp")+"&nonce="+map.get("nonce")+"&sign="+signn;
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder().
                    url(methodUrl).
                    build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.ok();
    }


    //有页面后点击按钮使用-------------------------handi------------------------------
    //1.点击预冻结按钮
    /**
     * 客户发起预冻结提交
     * 将提交的信息存入到W_BILL_OPERATOR和W_BILL_FLOW表进行备份
     */
    @RequestMapping("/preFreezingSub")
    public Result preFreezingSub(@RequestBody List<BillEntity> list){
        //判断下数据库是否已经有了本条记录，防止点了很多次
        BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
        BillFlowEntity billFlowEntity = new BillFlowEntity();
        int id = billService.seqBillOperatorId();
        //操作表
        billOperatorEntity.setId(String.valueOf(id));
        //质押1预冻结提交，仓库锁货中
        billOperatorEntity.setOperatorType(Const.PRE_FREEZING);
        billOperatorEntity.setPledgerMemberId(list.get(0).getWhMemberId());
        billOperatorEntity.setMemberType("1");
        billOperatorEntity.setClientId(list.get(0).getClientId());
        //TODO  质押合同id录入不进去
        billOperatorEntity.getPledgerContractId();
        billOperatorEntity.setOperatorName(list.get(0).getOperatorName());
        billOperatorEntity.setOperatorTel(list.get(0).getOperatorTel());
        //经办人身份证号(暂无此参数)
        billOperatorEntity.setOperatorId("39746273849574627x");
        billOperatorEntity.setVersion("v1");
        billOperatorEntity.setIsDelete("0");
        billService.preFreezing(billOperatorEntity);
        for(int i=0;i<list.size();i++){
            //流水表
            int fid = billService.seqBillOperatorlogId();
            billFlowEntity.setId(String.valueOf(fid));
            billFlowEntity.setBillOperatorId(String.valueOf(id));
            billFlowEntity.setClientId(list.get(i).getClientId());
            billFlowEntity.setWbillId(list.get(i).getWbillId());
            billFlowEntity.setMemberId(list.get(i).getWhMemberId());
            //仓单状态
            billFlowEntity.setWbillStatus("");
            billFlowEntity.setTimeCreate(new java.util.Date());
            billFlowEntity.setWeight(list.get(i).getWeight());
            billFlowEntity.setIsDelete("0");
            billService.preFreezingFlow(billFlowEntity);
        }
        return Result.ok("请联系仓管人员进行锁货");
    }

    //2.告知数字粮库锁货
    /**
     *仓单锁货信息查询接口
     */
    @RequestMapping(value = "/lockGoods", method = RequestMethod.GET)
    public Result lockGoods(String id){
        log.info("仓单锁货信息查询接口入参id：" + id);
        if(id == null || id.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        //需要进行锁货的仓单信息
        List<BillFlowEntity> list = billService.queryFlow(id);
        Map<String,Object> map = new HashMap<String, Object>();
        for(int i=0;i<list.size();i++){
            map.put(list.get(i).getWbillId(),list.get(i).getWeight());
        }
        Map<String,Object> mapf = new HashMap<String, Object>();
        mapf.put("varietyName","m");
        mapf.put("id",id);
        mapf.put("goodsInfo",map);
        return Result.ok().put("data",mapf);
    }


    /**
     *仓单上锁结果返回接口
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
        //数字粮库锁货成功后，调用大商所接口进行预冻结操作
        if(auditState.equals("1")){
            pre(id);
        }else{
            return Result.error().put("msg","锁货失败");
        }
        return Result.ok().put("resultCode","1");
    }

    //供中心端预冻结使用
    @RequestMapping("/pre")
    public Result pre(String id){
        Result result = Result.ok();
        try{
            //去本地库查询出预冻结的基本信息
            BillOperatorEntity billOperatorEntity = billService.queryPreFreezing(id);
            //需要进行预冻结操作的仓单id+重量
            List<BillFlowEntity> list = billService.queryFlow(id);
            TreeMap<String,String> map = new TreeMap<>();
            Map<String,Object> hsMap = new HashMap<>();
            for(int i=0;i<list.size();i++){
                hsMap.put(list.get(i).getWbillId(),list.get(i).getWeight());
            }
            String  param= JSON.toJSONString(hsMap);
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String pledgeeMemberId ="bk00369";
            map.put("pledgerMemberId",billOperatorEntity.getPledgerMemberId());
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",billOperatorEntity.getClientId());
            map.put("parentWbill",param);
            map.put("operatorName",billOperatorEntity.getOperatorName());
            map.put("operatorTel",billOperatorEntity.getOperatorTel());
            //map.put("operatorId",billOperatorEntity.getOperatorId());
            map.put("operatorId","210902199409151018");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://124.93.249.143:8999/wbc/api/wbill/prefreezeTx";
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
            int responseCode = response.code();
            log.info("responseCode：" + responseCode);
            if (Const.STATUS_CODE_202.equals(String.valueOf(responseCode))) {
            //if(true){
                //质押预冻结已提交，交易所审核中
                BillOperatorEntity billOperatorEntity1 = new BillOperatorEntity();
                billOperatorEntity1.setId(id);
                billOperatorEntity1.setOperatorType(Const.PLEDGE_PRE_FREEZING);
                billService.update(billOperatorEntity1);
                int cid = billService.seqBillOperatorlogId();
                //临时ID
                String prefreezeId = jsonObject.getJSONObject("data").getString("prefreezeId");
                //String prefreezeId = "208";
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(prefreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.PREFREEZING_SUB);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押预冻结提交成功! 交易所审核中");
            }else {
                result = Result.ok("仓单质押预冻结失败!"+jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     * 仓单质押预冻结撤销提交
     */
    @RequestMapping("/revokePreFreezingss")
    public Result  revokePreFreezingss(@RequestBody BillPledgeEntity billPledgerInfoEntity){
        Result result = Result.ok();
        try {
            String id = billPledgerInfoEntity.getId();
            String requestId = SHA256Util.getUUID32();
            //临时id
            //String prefreezeId = billService.queryPrefreezeId(id);
            TreeMap<String,String> map = new TreeMap<>();
            map.put("memberId",billPledgerInfoEntity.getPledgerMemberId());
            map.put("memberType","0");
            map.put("prefreezeId","");
            map.put("operatorName",billPledgerInfoEntity.getOperatorName());
            map.put("operatorTel",billPledgerInfoEntity.getOperatorTel());
            map.put("operatorId","210902199409151018");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(System.currentTimeMillis()));
            map.put("nonce",String.valueOf(RandomStringUtils.randomAlphanumeric(20)));
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            Long  timeNew =  System.currentTimeMillis();
            log.info("请求参数：" + map);
            //调用大商所接口
            String methodUrl ="http://224.93.249.143:8999/wbc/api/wbill/prefreezeTx/?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&prefreezeId="+map.get("prefreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //质押预冻结撤销已提交，交易所审核中
                BillOperatorEntity billOperatorEntity1 = new BillOperatorEntity();
                billOperatorEntity1.setId(id);
                billOperatorEntity1.setOperatorType(Const.PLEDGE_PRE_FREEZING_REVOKE);
                billService.update(billOperatorEntity1);
                // 在operator_log表中新增预冻结撤销记录值
                String prefreezeCancleId = jsonObject.getJSONObject("data").getString("prefreezeCancleId");
                int cid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(prefreezeCancleId);
                billOperatorLogEntity.setOperatorType(LogOper.REVOKE_PREFREEZING);
                billService.saveOperation(billOperatorLogEntity);
                //4.将预冻结撤销的的仓单重量值退还给这笔仓单的总额里
                result = Result.ok("仓单预冻结撤销提交成功，交易所审核中"+jsonObject.get("msg").toString());
            } else {
                result =  Result.error("仓单预冻结撤销失败!"+jsonObject.get("msg").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 仓单质押冻结提交(小改名字)
     */
    @RequestMapping("/freezess")
    public Result freezess(@RequestBody BillPledgeEntity billPledgeEntity){
        Result result = Result.ok();
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
        //需要根据id号查询出数据值，一些参数无值
        String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        String requestId = SHA256Util.getUUID32();
        Long  timeNew =  System.currentTimeMillis();
        String id = billPledgeEntity.getId();
        TreeMap<String,String> map = new TreeMap<>();
        map.put("pledgerMemberId",billPledgeEntity.getPledgerMemberId());
        map.put("pledgeeMemberId","bk00369");
        map.put("memberType","0");
        map.put("clientId",billPledgeEntity.getClientId());
        map.put("virtualWbillId",billPledgeEntity.getVirtualWbillId());
        /*map.put("pledgeContractAttach",PhotoUtils.getImageString(billPledgeEntity.getPledgeContractAttach(), PhotoUtils.getImageType(billPledgeEntity.getPledgeContractAttach())));
        map.put("pledgeBeginDate",billPledgeEntity.getPledgeBeginDate());
        map.put("pledgeEndDate",billPledgeEntity.getPledgeEndDate());*/
        map.put("pledgeContractAttach",photo);
        map.put("pledgeBeginDate","20200609");
        map.put("pledgeEndDate","20200731");
        map.put("operatorName",billPledgeEntity.getOperatorName());
        map.put("operatorTel",billPledgeEntity.getOperatorTel());
        map.put("operatorId",billPledgeEntity.getOperatorId());
        //map.put("operatorIdAttach",PhotoUtils.getImageString(billPledgeEntity.getOperatorIdAttach(), PhotoUtils.getImageType(billPledgeEntity.getOperatorIdAttach())));
        map.put("operatorIdAttach",photo);
        map.put("version","v1");
        map.put("requestId",requestId);
        map.put("timestamp",String.valueOf(timeNew));
        map.put("nonce",none);
        map.put("sign", SHA256Util.createSign(map));
        String url="http://224.93.249.143:8999/wbc/api/wbill/freezeTx";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                .add("pledgerMemberId",String.valueOf(map.get("pledgerMemberId")))
                .add("pledgeeMemberId",String.valueOf(map.get("pledgeeMemberId")))
                .add("memberType","0")
                .add("clientId",String.valueOf(map.get("clientId")))
                .add("virtualWbillId",String.valueOf(map.get("virtualWbillId")))
                .add("pledgeContractAttach",String.valueOf(map.get("pledgeContractAttach")))
                .add("pledgeBeginDate",String.valueOf(map.get("pledgeBeginDate")))
                .add("pledgeEndDate",String.valueOf(map.get("pledgeEndDate")))
                .add("operatorName",String.valueOf(map.get("operatorName")))
                .add("operatorTel",String.valueOf(map.get("operatorTel")))
                .add("operatorId",String.valueOf(map.get("operatorId")))
                .add("operatorIdAttach",String.valueOf(map.get("operatorIdAttach")))
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
        try {
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
            int responseCode = response.code();
            log.info("responseCode：" + responseCode);
            if (Const.STATUS_CODE_202.equals(String.valueOf(responseCode))) {
            //if(true){
                //1.修改operator表中此条数据的状态为质押冻结已提交，交易所审核中
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setOperatorType(Const.PLEDGE_FREEZING);
                billOperatorEntity.setId(id);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增冻结记录值
                int cid = billService.seqBillOperatorlogId();
                //临时ID
                String freezeId = jsonObject.getJSONObject("data").getString("freezeId");
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(freezeId);
                billOperatorLogEntity.setOperatorType(LogOper.FREEZING);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押冻结成功!"+jsonObject.get("msg").toString());
            } else {
                result = Result.error("仓单质押冻结失败!"+jsonObject.get("msg").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     *仓单质押预解质
     * @auth handi
     * @date 2019/12/28
     */
    @RequestMapping("/preUnjz")
    public Result preUnjz(@RequestBody BillPledgeEntity params){
        Result result = Result.ok();
        //防止重复提交申请 TODO
        /*int count = freezeService.queryOperatorLogTotal(params.getId(), LogOper.PRE_UNFREEZETX);
        if(count > 0){
            return Result.ok("审核中,请等待");
        }*/
        try{
            String id = params.getId();
            List<BillFlowEntity> list = billService.queryFlow(params.getId());
            Map<String,Object> hsMap = new HashMap<>();
            for(int i=0;i<list.size();i++){
                hsMap.put(list.get(i).getWbillId(),list.get(i).getWeight());
            }
            TreeMap<String,String> map = new TreeMap<>();
            String  parentWbillUnfreeze= JSON.toJSONString(hsMap);
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            /*String memberType;
            if(params.getMemberType().equals("出质人")){
                 memberType="0";
            }else{
                 memberType="1";
            }*/
            String clientId = params.getClientId();
            String pledgerMemberId = params.getPledgerMemberId();
            //暂时无此字段值
            String pledgeeMemberId = "bk00369";
            //虚拟仓单id
            String virtualWbillId = params.getVirtualWbillId();
            //合同id
            //String pledgeContractId = params.getPledgerContractId();
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("pledgeContractId","972317627");
            map.put("virtualWbillId",virtualWbillId);
            map.put("parentWbillUnfreeze",parentWbillUnfreeze);
            map.put("operatorName",params.getOperatorName());
            map.put("operatorTel",params.getOperatorTel());
            map.put("operatorId","210902199409151018");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://124.93.249.143:8999/wbc/api/wbill/preUnfreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",String.valueOf(map.get("pledgerMemberId")))
                    .add("pledgeeMemberId",String.valueOf(map.get("pledgeeMemberId")))
                    .add("memberType",String.valueOf(map.get("memberType")))
                    .add("clientId",String.valueOf(map.get("clientId")))
                    .add("pledgeContractId",String.valueOf(map.get("pledgeContractId")))
                    .add("virtualWbillId",String.valueOf(map.get("virtualWbillId")))
                    .add("parentWbillUnfreeze",String.valueOf(map.get("parentWbillUnfreeze")))
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
            int responseCode = response.code();
            log.info("responseCode：" + responseCode);
            if (Const.STATUS_CODE_202.equals(String.valueOf(responseCode))) {
               String preunfreezeId = jsonObject.getJSONObject("data").getString("wbillTmpId");
                ///1.修改operator表中此条数据的状态
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_UNFREEZE);
                billOperatorEntity.setId(id);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增预解质记录值
                int cid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(preunfreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.PRE_UNFREEZETX);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押预解质提交成功!"+jsonObject.get("msg").toString());
            }else{
                result =  Result.error("仓单质押预解质失败!"+jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     * 仓单预解质撤销
     * @auth handi
     * @return
     */
    @RequestMapping("/preUnfreCancel")
    public Result preUnfreCancel(@RequestBody BillPledgeEntity params){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String memberId =params.getPledgerMemberId();
            //临时id
            String id = params.getId();
            //String preunfreezeId = billService.queryPrefreezeId(id);
            /*String memberType;
            if(params.getMemberType().equals("出质人")){
                memberType="0";
            }else{
                memberType="1";
            }*/
            map.put("memberId",params.getPledgerMemberId());
            map.put("memberType","0");
            map.put("preunfreezeId","");
            map.put("operatorName",params.getOperatorName());
            map.put("operatorTel",params.getOperatorTel());
            map.put("operatorId","210902199409151018");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://124.93.249.143:8999/wbc/api/wbill/preUnfreezeTx?memberId="+map.get("memberId")+"&memberType="+map.get("memberType")+"&preunfreezeId="+map.get("preunfreezeId")+"&operatorName="+map.get("operatorName")+"&operatorTel="+map.get("operatorTel")+"&operatorId="+map.get("operatorId")+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //1.将它的状态从operator表中改为质押预解质撤销已提交，交易所审核中
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setId(id);
                billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_UNFREEZE_REVOKE);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增预解质撤销记录值
                String preunfreezeCancleId = jsonObject.getJSONObject("data").getString("preunfreezeCancleId");
                int cid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(preunfreezeCancleId);
                billOperatorLogEntity.setOperatorType(LogOper.PRE_UNFREEZE_CANCEL);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单预解质撤销成功!"+jsonObject.get("msg").toString());
            } else {
                result = Result.error("仓单预解质撤销失败!"+jsonObject.get("msg").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *仓单质押解质(临时测试hd)
     * @auth handi
     */
    @RequestMapping("/unfre")
    public Result unfre(@RequestBody BillPledgeEntity params){
        Result result = Result.ok();
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            String id = params.getId();
            String clientId =params.getClientId();
            String pledgerMemberId =params.getPledgerMemberId();
            //暂无此参数值
            String pledgeeMemberId ="bk00369";
            //虚拟仓单id
            String virtualWbillId =params.getVirtualWbillId();
            /*String memberType;
            if(params.getMemberType().equals("出质人")){
                memberType="0";
            }else{
                memberType="1";
            }*/
            map.put("pledgerMemberId",pledgerMemberId);
            map.put("pledgeeMemberId",pledgeeMemberId);
            map.put("memberType","0");
            map.put("clientId",clientId);
            map.put("pledgeContractId","972317627");
            map.put("virtualWbillId",virtualWbillId);
            map.put("operatorName",params.getOperatorName());
            map.put("operatorTel",params.getOperatorTel());
            map.put("operatorId","210902199409151018");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            log.info("请求参数：" + map);
            String url="http://124.93.249.143:8999/wbc/api/wbill/unfreezeTx";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("pledgerMemberId",String.valueOf(map.get("pledgerMemberId")))
                    .add("pledgeeMemberId",String.valueOf(map.get("pledgeeMemberId")))
                    .add("memberType",String.valueOf(map.get("memberType")))
                    .add("clientId",String.valueOf(map.get("clientId")))
                    .add("pledgeContractId",String.valueOf(map.get("pledgeContractId")))
                    .add("virtualWbillId",String.valueOf(map.get("virtualWbillId")))
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                String unfreezeId = jsonObject.getJSONObject("data").getString("unfreezeId");
                ///1.修改operator表中此条数据的状态
                BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
                billOperatorEntity.setOperatorType(Const.PLEDGE_UNFREEZE);
                billOperatorEntity.setId(id);
                billService.update(billOperatorEntity);
                // 2.在operator_log表中新增解质记录值
                int cid = billService.seqBillOperatorlogId();
                BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setId(String.valueOf(cid));
                billOperatorLogEntity.setWbillOperatorId(id);
                billOperatorLogEntity.setTempId(unfreezeId);
                billOperatorLogEntity.setOperatorType(LogOper.UNFREEZETX);
                billService.saveOperation(billOperatorLogEntity);
                result = Result.ok("仓单质押解质成功!"+jsonObject.get("msg").toString());
            }else{
                result = Result.error("仓单质押解质失败!"+jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }



//----数字粮库接口

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

//-----iot接口(仓单备案成功就可以给大商所推iot数据，仓单注销的数据就要拦截)
    @RequestMapping(value = "/iotData", method = RequestMethod.POST)
    public Result iotData(@RequestParam Map<String, Object> param){
        //系统当前时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tim = simpleDateFormat.format(date);
        log.info("iot结果返回接口入参：" + param+"当前时间:"+tim);
        //组织编码
        String scode =String.valueOf(param.get("scode"));
        //剁位ID
        String locationId = String.valueOf(param.get("locationId"));
        //设备ID
        String sn =String.valueOf(param.get("sn"));
        //设备类型
        String deviceType =String.valueOf(param.get("deviceType"));
        //IOT数据
        String iotValue = String.valueOf(param.get("iotValue"));
        if(scode.equals("null") || scode.equals("")){
            return Result.error().put("msg","组织编码不能为空");
        }
        if(locationId.equals("null") || locationId.equals("")){
            return Result.error().put("msg","剁位ID不能为空");
        }
        if(sn.equals("null") || sn.equals("")){
            return Result.error().put("msg","设备ID不能为空");
        }
        if(deviceType.equals("null") || deviceType.equals("")){
            return Result.error().put("msg","设备类型不能为空");
        }
        if(iotValue.equals("null") || iotValue.equals("")){
            return Result.error().put("msg","IOT数据不能为空");
        }
        //随机数
        String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        //请求ID
        String requestId = SHA256Util.getUUID32();
        //时间戳
        Long  timeNew =  System.currentTimeMillis();
        //iot传感器数据
        String iotDevices = "";
        List<Map<String,Object>> list = new ArrayList<>();
        BillIotEntity billIotEntity = new BillIotEntity();
        //205为温度传感器数据
        if(deviceType.equals("205")){
        //将iot数据入库
        int sid = billService.seqBillIot();
        billIotEntity.setId(String.valueOf(sid));
        billIotEntity.setDeviceType(deviceType);
        billIotEntity.setLocationId(locationId);
        billIotEntity.setSn(sn);
        billIotEntity.setScode(scode);
        billIotEntity.setIotValue(iotValue);
        billIotEntity.setCreateTime(new java.util.Date());
        //billIotEntity.setCreateUser(UserUtils.getCurrentUserId());
        log.info("billIotEntity========="+billIotEntity);
        billService.saveIot(billIotEntity);
        JSONArray arr = JSONArray.fromObject(param.get("iotValue"));
        for(int i=0;i<arr.size();i++){
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("deviceId",sn+"_"+arr.getJSONObject(i).get("id"));
            dataMap.put("deviceType",deviceType);
            dataMap.put("macAddr","1.2.3.4");
            dataMap.put("iotValue",arr.getJSONObject(i).get("val"));
            dataMap.put("rawValue",arr.getJSONObject(i).get("val"));
            list.add(dataMap);
        }
        iotDevices = JSON.toJSONString(list);
            //207为激光头数据
        }else if(deviceType.equals("207")){
            //01031A采集重量和体积
            //0103O2坐标点位个数
            //0103FO坐标具体内容
            boolean weigBool = iotValue.contains("01031A");
            boolean numBool= iotValue.contains("010302");
            boolean contentBool= iotValue.contains("0103F0");
            //如果成功则是有用数据
            if(weigBool||numBool||contentBool) {
                //将iot数据入库
                int sid = billService.seqBillIot();
                billIotEntity.setId(String.valueOf(sid));
                billIotEntity.setDeviceType(deviceType);
                billIotEntity.setLocationId(locationId);
                billIotEntity.setSn(sn);
                billIotEntity.setScode(scode);
                billIotEntity.setIotValue(iotValue);
                billIotEntity.setCreateTime(new java.util.Date());
                //billIotEntity.setCreateUser(UserUtils.getCurrentUserId());
                log.info("billIotEntity=========" + billIotEntity);
                billService.saveIot(billIotEntity);
                List<LocationInfoEntity.IotDevicesBean> iotDevicesBeansList = new ArrayList<LocationInfoEntity.IotDevicesBean>();
                LocationInfoEntity.IotDevicesBean iotDevicesBean4 = new LocationInfoEntity.IotDevicesBean();
                iotDevicesBean4.setDeviceId("581749033");
                iotDevicesBean4.setDeviceType("0");
                iotDevicesBean4.setMacAddr("1.2.3.4");
                iotDevicesBean4.setIotValue("01031A0b9b00000b9b00000EDE00010001000000000000000000030000FDF6");
                //iotDevicesBean4.setIotValue("01031A3B7800003B7800000EDE00010001000000000000000000030000FDF6");
                //扫描仪点位坐标具体信息
                List<BillIotEntity> billIotEntityList = new ArrayList<BillIotEntity>();
                BillIotEntity billIotEntityT = new BillIotEntity();
                billIotEntityT.setScode("1-40");
                billIotEntityT.setIotValue("0103F0012C008C089C014D008C0877016E008C0AA7018F008C0B1301B0008C0B6B01D1008C0B5E01F2008C0B70012C00AD0B75014D00AD0B99016E00AD0BA3018F00AD0BA201B000AD0BA001D100AD0B9F01F200AD0B9E012C00CE0BB0014D00CE0BB0016E00CE0BB0018F00CE0BB001B000CE0BB001D100CE0BB001F200CE0BB0012C00EF0BB7014D00EF0BB7016E00EF0BB7018F00EF0BB701B000EF0BB701D100EF0BB701F200EF0BB7012C01100BB7014D01100BB7016E01100BB7018F01100BB701B001100BB701D101100BB701F201100BB7012C006B0889014D006B0A32016E006B0AFC018F006B0B5701B0006B0B119194");
                BillIotEntity billIotEntityY = new BillIotEntity();
                billIotEntityY.setScode("41-80");
                billIotEntityY.setIotValue("0103F001D1006B093501F2006B0913012C004A0876014D004A0874016E004A086A018F004A085E01B0004A08EF01D1004A08F801F2004A08D7012C00290981014D00290866016E00290861018F002908B501B00029084C01D10029084001F20029089B012C0008097D014D00080906016E000808E5018F000808E901B0000808A201D10008088001F20008085F010B006B0BB800EA006B0BB800C9006B0BB800A8006B0BB80087006B0BB80066006B0BB80045006B0BB80024006B0BB80003006B0BB8010B004A0BB800EA004A0BB800C9004A0BB800A8004A0BB80087004A0BB80066004A0BB80045004A0BB80024004A0BB80573");
                billIotEntityList.add(billIotEntityT);
                billIotEntityList.add(billIotEntityY);
                Map<String,String> rawMap = new HashMap<>();
                for(int i =0 ;i<billIotEntityList.size();i++){
                    rawMap.put(billIotEntityList.get(i).getScode(),billIotEntityList.get(i).getIotValue());
                }
                String rawVal = JSON.toJSONString(rawMap);
                iotDevicesBean4.setRawValue(rawVal);
                iotDevicesBeansList.add(iotDevicesBean4);
                iotDevices = JSON.toJSONString(iotDevicesBeansList);
            }else{
                log.info("当前数据为错误数据");
                return Result.error().put("errMsg","当前数据为错误数据");
            }
        }else{
            log.info("设备类型不匹配");
            return Result.error().put("errMsg","设备类型不匹配");
        }
        TreeMap<String,String> map = new TreeMap<>();
        map.put("whMemberId","wh00324");
        map.put("clientId","c000000156");
        map.put("wbillId","w0000000000000000165");
        map.put("positionId",scode);
        map.put("locationId",locationId);
        map.put("iotDevices",iotDevices);
        map.put("requestId",requestId);
        map.put("timestamp",String.valueOf(timeNew));
        map.put("nonce",none);
        map.put("version","v1");
        map.put("sign", SHA256Util.createSign(map));
        log.info("请求参数：" + map);
       try {
            String url="http://124.93.249.143:8999/wbc/api/iotdata";
           OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
           okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                   .add("whMemberId",String.valueOf(map.get("whMemberId")))
                   .add("clientId",String.valueOf(map.get("clientId")))
                   .add("wbillId",String.valueOf(map.get("wbillId")))
                   .add("positionId",String.valueOf(map.get("positionId")))
                   .add("locationId",String.valueOf(map.get("locationId")))
                   .add("IotDevices",String.valueOf(map.get("iotDevices")))
                   .add("version","v1")
                   .add("requestId",requestId)
                   .add("timestamp",String.valueOf(map.get("timestamp")))
                   .add("nonce",String.valueOf(map.get("nonce")))
                   .add("sign",String.valueOf(map.get("sign")))
                   .build();
           Request request = new Request.Builder()
                   .url(url)
                   .post(formBody)
                   .build();
           Response response = okHttpClient.newCall(request).execute();
           log.info(response.body().string());
           log.info("响应结果：" + response);
            if(Const.STATUS_CODE_202.equals(String.valueOf(response.code()))){
                /*BillOperatorLogEntity billOperatorLogEntity = new BillOperatorLogEntity();
                billOperatorLogEntity.setAuditStatus(locationId);
                billOperatorLogEntity.setTempId("31");
                billService.updateOperatorLog(billOperatorLogEntity);

                billOperatorLogEntity.setAuditStatus(scode);
                billOperatorLogEntity.setTempId("32");
                billService.updateOperatorLog(billOperatorLogEntity);

                billOperatorLogEntity.setAuditStatus(sn);
                billOperatorLogEntity.setTempId("33");
                billService.updateOperatorLog(billOperatorLogEntity);

                billOperatorLogEntity.setAuditStatus(deviceType);
                billOperatorLogEntity.setTempId("34");
                billService.updateOperatorLog(billOperatorLogEntity);

                billOperatorLogEntity.setAuditStatus(iotValue);
                billOperatorLogEntity.setTempId("35");
                billService.updateOperatorLog(billOperatorLogEntity);*/
                return Result.ok().put("resultCode","1");
            }else{
                return Result.error().put("resultCode","0");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.ok().put("resultCode","1");
    }

    @RequestMapping(value = "/iotSelectV")
    public Result iotSelectV(){
        int val = 0x0098;
        //获取坐标点个数（288）
        int dec_num = Integer.parseInt("0120", 16);
        //计算需要请求几次来获取坐标点信息
        int num;
        //(7)
         num = dec_num/40;
        if(num<1){
            //如果小于1说明点位个数小于四十
             int fa = dec_num%40;
        }else{
            //(7)
            for(int i =0;i<num;i++){

            }
        }
        System.out.print(dec_num);
        long dec_numf = Long.parseLong("0325", 16);
        System.out.print(dec_numf);
        return Result.ok();
    }

    //iot新接口请求激光头数据(体积，重量)
    @RequestMapping(value="/iotWeight")
    public Result iotWeight(){
       try {
            String url = "http://config.cfciot.com:8080/iotbmp/app/Laserhead/instructions";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("sode", "000001")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        }catch(IOException e){
            e.printStackTrace();
        }
        return Result.ok();
    }

    //iot新接口请求激光头数据(坐标点数量)
    @RequestMapping(value="/iotNumbers")
    public Result iotNumbers(){
        try {
            String url = "http://config.cfciot.com:8080/iotbmp/app/Laserhead/numbers";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("sode", "000001")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        }catch(IOException e){
            e.printStackTrace();
        }
        return Result.ok();
    }

    //iot新接口请求激光头数据(坐标点坐标值)
    @RequestMapping(value="/iotNumberData")
    public Result iotNumberData(){
        try {
            String url = "http://config.cfciot.com:8080/iotbmp/app/Laserhead/numberData";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("sode", "000001")
                    .add("numberCode","0098")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
        }catch(IOException e){
            e.printStackTrace();
        }
        return Result.ok();
    }
}