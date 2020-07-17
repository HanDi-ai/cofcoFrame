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
import com.cofco.sys.entity.members.AllianceMemberLogEntity;
import com.cofco.sys.entity.members.MembersEntity;
import com.cofco.sys.service.members.MembersService;
import com.cofco.utils.*;
import okhttp3.*;
import org.apache.commons.lang.RandomStringUtils;
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
 * 联盟成员注册
 * @author handi
 * @email 876053009@qq.com
 * @date 2019-10-31 17:00:49
 */
@RestController
@RequestMapping("sys/memberst")
public class MembersControllerTest extends BaseController {
    //日志
    Logger log = Logger.getLogger(MembersController.class);

    @Autowired
    private  MembersService MembersService;


    /**
     * 联盟成员信息查询
     *  @author handi
     * @date 2019/10/30 17:09:22
     */
    @RequestMapping("/queryList")
    public Result queryList(@RequestParam Map<String, Object> params){
        params.put("is_delete","0");
        Query query = new Query(params);
        PageHelper.startPage(query.getPage(),query.getLimit());
        List<MembersEntity> roleList = MembersService.queryList(query);
        PageHelper.endPage();
        int total = MembersService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(roleList, total, query.getLimit(), query.getPage());
        return Result.ok().put("page", pageUtil);
    }

    /**
     * 根据id号查询仓单信息
     */
    @RequestMapping("/info/{id}")
    @SysLog("查看角色")
    public Result info(@PathVariable("id") String id){
        MembersEntity membersEntity = MembersService.queryObject(id);
        return Result.ok().put("role",membersEntity);
    }

    /**
     * 新增联盟成员
     * @author handi
     * @date 2019/10/31 17:02:59
     * @return Result
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MembersEntity membersEntity){
        Result result = Result.ok();
        //sequence主键值
        int id = MembersService.seqMemberId();
        membersEntity.setId(String.valueOf(id));
        membersEntity.setVersion("v1");
        membersEntity.setTime_create(new Date());
        membersEntity.setCreator(UserUtils.getCurrentUserId());
        membersEntity.setIs_delete(Const.DELETE_FLG_NO);
        try {
            //生成公私钥
            Map<String, Object> keyMap = SHA256Util.createKey(SHA256Util.KEY_ALGORITHM, SHA256Util.RAS_KEY_SIZE);
            PublicKey publicKey = (PublicKey) keyMap.get("publicKey");
            PrivateKey privateKey = (PrivateKey) keyMap.get("privateKey");
            // 将公私钥转成字符串
            String publicKeyString = SHA256Util.pubToStr(publicKey);
            String privateKeyString = SHA256Util.priToStr(privateKey);
           //将字符串转化成二进制
            String publicKeyBinary = SHA256Util.strToBinary(publicKeyString);
            String privateKeyBinary = SHA256Util.strToBinary(privateKeyString);
            membersEntity.setZmemPublicKey(publicKeyBinary);
            membersEntity.setZmemPrivateKey(privateKeyBinary);

            membersEntity.setProductType("[\"ag\",\"ind\"]");
            membersEntity.setProductCategory("[\"m\",\"y\"]");
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            //随机字符串
            String nonce = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            //请求ID
            String requestId = SHA256Util.getUUID32();
            TreeMap map = SignatureUtil.entityToMap(membersEntity);
            map.remove("modifier");
            map.remove("time_modify");
            map.remove("id");
            map.remove("time_create");
            map.remove("memPrivateKey");
            map.remove("memPublicKey");
            map.remove("is_delete");
            map.remove("creator");
            map.remove("whtmpid");
            map.remove("memberId");
            map.remove("sign");
            map.put("productType","[\"ag\",\"ind\"]");
            map.put("productCategory","[\"m\",\"y\"]");
            map.put("requestId",requestId);
            map.put("timestamp",timeNew);
            map.put("orgAttach",PhotoUtils.getImageString(membersEntity.getOrgAttach(), PhotoUtils.getImageType(membersEntity.getOrgAttach())));
            map.put("corporateIdAttach",PhotoUtils.getImageString(membersEntity.getCorporateIdAttach(), PhotoUtils.getImageType(membersEntity.getCorporateIdAttach())));
            map.put("contactIdAttach",PhotoUtils.getImageString(membersEntity.getContactIdAttach(), PhotoUtils.getImageType(membersEntity.getContactIdAttach())));
            map.put("positionDiagram",PhotoUtils.getImageString(membersEntity.getPositionDiagram(), PhotoUtils.getImageType(membersEntity.getPositionDiagram())));
            map.put("upperOrgAttach",PhotoUtils.getImageString(membersEntity.getUpperOrgAttach(), PhotoUtils.getImageType(membersEntity.getUpperOrgAttach())));
            map.put("version","v1");
            map.put("publicKey",publicKeyString);
            map.put("nonce", nonce);
            log.info("生成签名前的参数值：" + map);
            //按照参数名ASCII码从小到大排序拼接成字符串stringA。
            String stringA = SignatureUtil.genOrderedString(map);
            //sha256运算后的字符串(sign即为签名)
            String sign=SignatureUtil.genRegSignSortedMap(stringA,nonce);
            map.put("sign",sign);
            log.info("生成签名后的参数值：" + map);
            //调用大商所接口
            String url="http://59.46.215.173:8999/wbc/api/members/whs";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("isDelivery",String.valueOf(map.get("isDelivery")))
                    .add("whType",String.valueOf(map.get("whType")))
                    .add("whName",String.valueOf(map.get("whName")))
                    .add("whAbbr",String.valueOf(map.get("whAbbr")))
                    .add("regAddr",String.valueOf(map.get("regAddr")))
                    .add("officeAddr",String.valueOf(map.get("officeAddr")))
                    .add("tel",String.valueOf(map.get("tel")))
                    .add("orgNo",String.valueOf(map.get("orgNo")))
                    .add("orgAttach",String.valueOf(map.get("orgAttach")))
                    .add("property",String.valueOf(map.get("property")))
                    .add("productType",String.valueOf(map.get("productType")))
                    .add("productCategory",String.valueOf(map.get("productCategory")))
                    .add("bankName",String.valueOf(map.get("bankName")))
                    .add("bankAccount",String.valueOf(map.get("bankAccount")))
                    .add("regMoney",String.valueOf(map.get("regMoney")))
                    .add("totalAsset",String.valueOf(map.get("totalAsset")))
                    .add("fixedAsset",String.valueOf(map.get("fixedAsset")))
                    .add("netAsset",String.valueOf(map.get("netAsset")))
                    .add("corporateName",String.valueOf(map.get("corporateName")))
                    .add("corporateTel",String.valueOf(map.get("corporateTel")))
                    .add("corporateId",String.valueOf(map.get("corporateId")))
                    .add("corporateIdAttach",String.valueOf(map.get("corporateIdAttach")))
                    .add("contactName",String.valueOf(map.get("contactName")))
                    .add("contactTel",String.valueOf(map.get("contactTel")))
                    .add("contactId",String.valueOf(map.get("contactId")))
                    .add("contactIdAttach",String.valueOf(map.get("contactIdAttach")))
                    .add("isLease",String.valueOf(map.get("isLease")))
                    .add("leaseBeginDate",String.valueOf(map.get("leaseBeginDate")))
                    .add("leaseEndDate",String.valueOf(map.get("leaseEndDate")))
                    .add("regionArea",String.valueOf(map.get("regionArea")))
                    .add("houseArea",String.valueOf(map.get("houseArea")))
                    .add("cementArea",String.valueOf(map.get("cementArea")))
                    .add("totHeapQty",String.valueOf(map.get("totHeapQty")))
                    .add("ftrHeapQty",String.valueOf(map.get("ftrHeapQty")))
                    .add("bulkHeapQty",String.valueOf(map.get("bulkHeapQty")))
                    .add("loadometerTon",String.valueOf(map.get("loadometerTon")))
                    .add("positionDiagram",String.valueOf(map.get("positionDiagram")))
                    .add("upperType",String.valueOf(map.get("upperType")))
                    .add("upperName",String.valueOf(map.get("upperName")))
                    .add("upperAbbr",String.valueOf(map.get("upperAbbr")))
                    .add("upperTel",String.valueOf(map.get("upperTel")))
                    .add("upperAddr",String.valueOf(map.get("upperAddr")))
                    .add("upperOrgNo",String.valueOf(map.get("upperOrgNo")))
                    .add("upperOrgAttach",String.valueOf(map.get("upperOrgAttach")))
                    .add("upperCorporateName",String.valueOf(map.get("upperCorporateName")))
                    .add("publicKey",String.valueOf(map.get("publicKey")))
                    .add("version",String.valueOf(map.get("version")))
                    .add("requestId",String.valueOf(map.get("requestId")))
                    .add("timestamp",String.valueOf(map.get("timestamp")))
                    .add("nonce",String.valueOf(map.get("nonce")))
                    .add("sign",String.valueOf(map.get("sign")))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
            //将临时ID以及公钥存入数据库当中
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //if (Const.STATUS_CODE_202.equals(String.valueOf("202"))) {
                //临时成员号
                String whtmpid = String.valueOf(jsonObject.getJSONObject("data").get("whTmpId"));
                    //String whtmpid ="123";
                //大商所发来的公钥
                String publicK = String.valueOf(jsonObject.getJSONObject("data").get("publicKey"));
                    //String publicK ="CDLJT666";
                //将联盟成员信息新增入库
                membersEntity.setPublicKey(publicK);
                int t = MembersService.save(membersEntity);
                //联盟成员新增入库失败
                if(t == 0){
                    return Result.error().put("msg","成员新增失败");
                }
            //将whTmpId(临时ID)新增到ALLIANCE_MEMBER_LOG表
            int lid = MembersService.seqMemberLogId();
            AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
            allianceMemberLogEntity.setId(String.valueOf(lid));
            allianceMemberLogEntity.setAllianceMemberId(String.valueOf(id));
            allianceMemberLogEntity.setTempId(whtmpid);
            allianceMemberLogEntity.setOperatorType(LogOper.MEMBER_SAVE);
            MembersService.saveLog(allianceMemberLogEntity);
            result = Result.ok("仓库注册提交成功!");
            }else {
                result =  Result.error("与交易所通信异常,请联系管理员");
                //log.info("返回响应值：" + jsonObject);
                    log.info("失败了==========");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 新增联盟成员回调接口方法
     * @author handi
     * @date 2019/11/07 9:19:23
     */
    @RequestMapping(value = "/saveApi", method = RequestMethod.POST)
    public Result saveApi(@RequestParam Map<String, Object> param){
        log.info("新增联盟成员回调接口入参：" + param);
        //联盟成员ID
        String memberId = String.valueOf(param.get("memberId"));
        //申请ID
        String whTmpId = String.valueOf(param.get("whTmpId"));
        //审批结果
        String auditState = String.valueOf(param.get("auditState"));
        if(memberId.equals("null") || memberId.equals("")){
            return Result.error().put("msg","联盟成员ID不能为空");
        }
        if(whTmpId.equals("null") || whTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审批结果不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("新增联盟成员失败!").put("resultCode","0");
        }
        //将审批结果更新到log表
        AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
        allianceMemberLogEntity.setTempId(whTmpId);
        allianceMemberLogEntity.setAuditStatus(auditState);
        MembersService.updateLog(allianceMemberLogEntity);
        //将联盟成员ID更新到member表
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("memberId",memberId);
        map.put("whTmpId",whTmpId);
        MembersService.updateMemberId(map);
        return Result.ok().put("resultCode","1");
    }

    /**
     * 联盟成员信息查询
     * @author handi
     * @date 2019/10/31 16:03:12
     */
    @RequestMapping("/queryMember")
    public Result queryMember(){
        SortedMap<String, String> requestMap = new TreeMap<>();
        //当前登录人的联盟成员ID
        String memberId = "wh00002";
        //在数据库查询出当前联盟成员自己的一套公私钥
        MembersEntity member = MembersService.queryKey(memberId);
        //组织机构代码
        String orgNo = "9999";
        //随机数
        String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        //请求ID
        String requestIds = SHA256Util.getUUID32();
        //时间戳
        Long  timeNew =  System.currentTimeMillis();
        requestMap.put("version","v1");
        requestMap.put("requestId",requestIds);
        requestMap.put("timestamp",String.valueOf(timeNew));
        requestMap.put("nonce",none);
        log.info("生成签名前的参数值：" + requestMap);
        requestMap.put("sign",SHA256Util.createSignT(requestMap,member));
        log.info("生成签名后的参数值：" + requestMap);
        String version = String.valueOf(requestMap.get("version")).trim();
        String requestId = String.valueOf(requestMap.get("requestId")).trim();
        String timestamp = String.valueOf(requestMap.get("timestamp")).trim();
        String nonce = String.valueOf(requestMap.get("nonce")).trim();
        String sign = String.valueOf(requestMap.get("sign")).trim();
        try{
            //GET请求拼接签名，需要对签名进行encode操作
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://59.46.215.173:8999/wbc/api/members/whs/"+orgNo+"?version="+version+"&requestId="+requestId+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder().
                    url(methodUrl).
                    build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
            if (Const.STATUS_CODE_200.equals(String.valueOf(response.code()))) {
                log.info("联盟成员信息:"+response.message());
            }else{
                log.info("联盟成员信息查询接口请求失败!");
                log.info("返回响应值：" + jsonObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return null;
    }

    /**
     * 联盟成员汇聚状态查询
     * @author handi
     * @date 2019/12/31 13:06:12
     */
    @RequestMapping("/queryStatus")
    public Result queryStatus(){
        SortedMap<String, String> requestMapf = new TreeMap<>();
        //当前登录人的联盟成员ID
        String memberId = "wh00002";
        //在数据库查询出当前联盟成员自己的一套公私钥
        MembersEntity member = MembersService.queryKey(memberId);
        //组织机构代码
        String orgNos = "9999";
        //仓库临时ID
        String whTmpId ="3";
        //随机数
        String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        //请求ID
        String requestIds = SHA256Util.getUUID32();
        //时间戳
        Long  timeNew =  System.currentTimeMillis();
        requestMapf.put("orgNo",orgNos);
        requestMapf.put("version","v1");
        requestMapf.put("requestId",requestIds);
        requestMapf.put("timestamp",String.valueOf(timeNew));
        requestMapf.put("nonce",none);
        log.info("生成签名前的参数值：" + requestMapf);
        requestMapf.put("sign",SHA256Util.createSignT(requestMapf,member));
        log.info("生成签名后的参数值：" + requestMapf);
        String orgNo = String.valueOf(requestMapf.get("orgNo")).trim();
        String version = String.valueOf(requestMapf.get("version")).trim();
        String requestId = String.valueOf(requestMapf.get("requestId")).trim();
        String timestamp = String.valueOf(requestMapf.get("timestamp")).trim();
        String nonce = String.valueOf(requestMapf.get("nonce")).trim();
        String sign = String.valueOf(requestMapf.get("sign")).trim();
        try{
            //GET请求拼接签名，需要对签名进行encode操作
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://59.46.215.173:8999/wbc/api/members/whs/as/"+whTmpId+"?orgNo="+orgNo+"&version="+version+"&requestId="+requestId+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
            OkHttpClient okHttpClient = new OkHttpClient();
            log.info("url地址：" + methodUrl);
            okhttp3.Request reqBuild = new okhttp3.Request.
                    Builder().
                    url(methodUrl).
                    build();
            Response response = okHttpClient.newCall(reqBuild).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("返回响应值：" + jsonObject);
            if (Const.STATUS_CODE_200.equals(String.valueOf(response.code()))) {
                log.info("联盟成员汇聚状态查询:"+response.message());
            }else{
                log.info("联盟成员汇聚状态查询接口请求失败!");
                log.info("返回响应值：" + jsonObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return null;
    }

    /**
     * 成员信息修改
     * @author handi
     * @date 2019/10/31 17:04:13
     * todo 公钥查询
     */
    @RequestMapping("/update")
    public Result update(@RequestBody MembersEntity membersEntity){
        Result result = Result.ok();
        //当前登录人的联盟成员ID
        String memberId = "wh00002";
        //在数据库查询出当前联盟成员自己的一套公私钥
        MembersEntity member = MembersService.queryKey(memberId);
        String publicKeyBinary = String.valueOf(member.getZmemPublicKey());
        //公钥字符串
        String publicKeyStr = SHA256Util.binaryToString(publicKeyBinary);
        //修改时间
        membersEntity.setTime_modify(new Date());
        // 修改人
        membersEntity.setModifier(UserUtils.getCurrentUserId());
        //当前修改人ID号
        String id = membersEntity.getId();
        //当前修改人成员ID号
        //String memberId = membersEntity.getMemberId();
        try {
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            //随机串
            String nonce =RandomStringUtils.randomAlphanumeric(20);
            //请求ID
            String requestIds = SHA256Util.getUUID32();
            //实体类转map
            TreeMap map = SignatureUtil.entityToMap(membersEntity);
            map.put("orgAttach",PhotoUtils.getImageString(membersEntity.getOrgAttach(), PhotoUtils.getImageType(membersEntity.getOrgAttach())));
            map.put("corporateIdAttach",PhotoUtils.getImageString(membersEntity.getCorporateIdAttach(), PhotoUtils.getImageType(membersEntity.getCorporateIdAttach())));
            map.put("contactIdAttach",PhotoUtils.getImageString(membersEntity.getContactIdAttach(), PhotoUtils.getImageType(membersEntity.getContactIdAttach())));
            map.put("positionDiagram",PhotoUtils.getImageString(membersEntity.getPositionDiagram(), PhotoUtils.getImageType(membersEntity.getPositionDiagram())));
            map.put("upperOrgAttach",PhotoUtils.getImageString(membersEntity.getUpperOrgAttach(), PhotoUtils.getImageType(membersEntity.getUpperOrgAttach())));
            map.put("productType","[\"A\",\"I\"]");
            map.put("productCategory","[\"C\",\"P\"]");
            map.put("publicKey",publicKeyStr);
            map.put("requestId",requestIds);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", String.valueOf(nonce));
            map.remove("modifier");
            map.remove("time_modify");
            map.remove("id");
            map.remove("time_create");
            map.remove("memPrivateKey");
            map.remove("memPublicKey");
            map.remove("is_delete");
            map.remove("creator");
            map.remove("whtmpid");
            map.remove("memberId");
            log.info("签名前参数"+map);
            map.put("sign", SHA256Util.createSignT(map,member));
            log.info("签名后参数"+map);
            //组织机构代码
            String orgNo = "9999";
            String url="http://59.46.215.173:8999/wbc/api/members/whs/"+orgNo;
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("isDelivery",String.valueOf(map.get("isDelivery")))
                    .add("whType",String.valueOf(map.get("whType")))
                    .add("whName",String.valueOf(map.get("whName")))
                    .add("whAbbr",String.valueOf(map.get("whAbbr")))
                    .add("regAddr",String.valueOf(map.get("regAddr")))
                    .add("officeAddr",String.valueOf(map.get("officeAddr")))
                    .add("tel",String.valueOf(map.get("tel")))
                    .add("orgNo",String.valueOf(map.get("orgNo")))
                    .add("orgAttach",String.valueOf(map.get("orgAttach")))
                    .add("property",String.valueOf(map.get("property")))
                    .add("productType",String.valueOf(map.get("productType")))
                    .add("productCategory",String.valueOf(map.get("productCategory")))
                    .add("bankName",String.valueOf(map.get("bankName")))
                    .add("bankAccount",String.valueOf(map.get("bankAccount")))
                    .add("regMoney",String.valueOf(map.get("regMoney")))
                    .add("totalAsset",String.valueOf(map.get("totalAsset")))
                    .add("fixedAsset",String.valueOf(map.get("fixedAsset")))
                    .add("netAsset",String.valueOf(map.get("netAsset")))
                    .add("corporateName",String.valueOf(map.get("corporateName")))
                    .add("corporateTel",String.valueOf(map.get("corporateTel")))
                    .add("corporateId",String.valueOf(map.get("corporateId")))
                    .add("corporateIdAttach",String.valueOf(map.get("corporateIdAttach")))
                    .add("contactName",String.valueOf(map.get("contactName")))
                    .add("contactTel",String.valueOf(map.get("contactTel")))
                    .add("contactId",String.valueOf(map.get("contactId")))
                    .add("contactIdAttach",String.valueOf(map.get("contactIdAttach")))
                    .add("isLease",String.valueOf(map.get("isLease")))
                    .add("leaseBeginDate",String.valueOf(map.get("leaseBeginDate")))
                    .add("leaseEndDate",String.valueOf(map.get("leaseEndDate")))
                    .add("regionArea",String.valueOf(map.get("regionArea")))
                    .add("houseArea",String.valueOf(map.get("houseArea")))
                    .add("cementArea",String.valueOf(map.get("cementArea")))
                    .add("totHeapQty",String.valueOf(map.get("totHeapQty")))
                    .add("ftrHeapQty",String.valueOf(map.get("ftrHeapQty")))
                    .add("bulkHeapQty",String.valueOf(map.get("bulkHeapQty")))
                    .add("loadometerTon",String.valueOf(map.get("loadometerTon")))
                    .add("positionDiagram",String.valueOf(map.get("positionDiagram")))
                    .add("upperType",String.valueOf(map.get("upperType")))
                    .add("upperName",String.valueOf(map.get("upperName")))
                    .add("upperAbbr",String.valueOf(map.get("upperAbbr")))
                    .add("upperTel",String.valueOf(map.get("upperTel")))
                    .add("upperAddr",String.valueOf(map.get("upperAddr")))
                    .add("upperOrgNo",String.valueOf(map.get("upperOrgNo")))
                    .add("upperOrgAttach",String.valueOf(map.get("upperOrgAttach")))
                    .add("upperCorporateName",String.valueOf(map.get("upperCorporateName")))
                    .add("publicKey",String.valueOf(map.get("publicKey")))
                    .add("version",String.valueOf(map.get("version")))
                    .add("requestId",String.valueOf(map.get("requestId")))
                    .add("timestamp",String.valueOf(map.get("timestamp")))
                    .add("nonce",String.valueOf(map.get("nonce")))
                    .add("sign",String.valueOf(map.get("sign")))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .put(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //将信息更新到数据库
                int t = MembersService.update(membersEntity);
                //联盟成员信息修改失败
                if(t == 0){
                    return Result.error().put("msg","成员信息修改失败");
                }
                //Sequence
                int lid = MembersService.seqMemberLogId();
                //申请ID
                String whtmpid = jsonObject.getString("whTmpId");
                //将临时ID新增到log表
                AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
                allianceMemberLogEntity.setId(String.valueOf(lid));
                allianceMemberLogEntity.setAllianceMemberId(id);
                allianceMemberLogEntity.setTempId(whtmpid);
                allianceMemberLogEntity.setOperatorType(LogOper.MEMBER_UPDATE);
                MembersService.saveLog(allianceMemberLogEntity);
                result = Result.ok("仓库信息修改提交成功! ");
            } else {
                result = Result.error("请求异常,请联系管理员");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 成员信息修改回调接口
     * @author handi
     * @date 2019/12/02 11:13:23
     */
    @RequestMapping(value="/updateApi" , method = RequestMethod.POST)
    public Result updateApi(@RequestParam Map<String, Object> param){
        //申请ID
        String whTmpId = String.valueOf(param.get("whTmpId"));
        //审批结果
        String auditState = String.valueOf(param.get("auditState"));
        //成员ID
        String memberId = String.valueOf(param.get("memberId"));
        if(whTmpId.equals("null") || whTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(memberId.equals("null") || memberId.equals("")){
            return Result.error().put("msg","成员ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审批结果不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("联盟成员信息修改失败!").put("resultCode","0");
        }
        //将审批结果更新到log表
        AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
        allianceMemberLogEntity.setTempId(whTmpId);
        allianceMemberLogEntity.setAuditStatus(auditState);
        MembersService.updateLog(allianceMemberLogEntity);
        //将联盟成员ID更新到member表
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("memberId",memberId);
        map.put("whTmpId",whTmpId);
        MembersService.updateMemberId(map);
        return Result.ok().put("resultCode","1");
    }

    /**
     * 联盟成员信息注销
     * @author handi
     * @date 2019/12/03 16:13:23
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody MembersEntity membersEntity){
        Result result = Result.ok();
        MembersEntity membersEntitys = new MembersEntity();
        //被删除成员的ID
        membersEntitys.setId(membersEntity.getId());
        membersEntitys.setIs_delete(Const.DELETE_FLG_YES);
        //联盟成员ID号
        //String memberId = membersEntity.getMemberId();
        String memberId = "wh00003";
        //组织机构编码
        //String orgNo = membersEntity.getOrgNo();
        String orgNo = "123456781234567891";
        //在数据库查询出当前联盟成员自己的一套公私钥
        MembersEntity member = MembersService.queryKey(memberId);
        try{
            TreeMap<String,String> map = new TreeMap<>();
            //随机数
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            //请求ID
            String requestId = SHA256Util.getUUID32();
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            map.put("memberId",memberId);
            //map.put("logoutContent","wrong format");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSignT(map,member));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://59.46.215.173:8999/wbc/api/members/whs/"+orgNo+"?memberId="+map.get("memberId")+"&version="+map.get("version")+"&requestId="+map.get("requestId")+"&timestamp="+map.get("timestamp")+"&nonce="+map.get("nonce")+"&sign="+signn;
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
                MembersService.delete(membersEntitys);
                //Sequence
                int lid = MembersService.seqMemberLogId();
                //申请ID
                String whTmpId = jsonObject.getString("whTmpId");
                //将临时ID新增到log表
                AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
                allianceMemberLogEntity.setId(String.valueOf(lid));
                allianceMemberLogEntity.setAllianceMemberId(membersEntity.getId());
                allianceMemberLogEntity.setTempId(whTmpId);
                allianceMemberLogEntity.setOperatorType(LogOper.MEMBER_DELETE);
                MembersService.saveLog(allianceMemberLogEntity);
                result = Result.ok("仓库信息注销提交成功! ");
            } else {
                result = Result.error("请求异常,请联系管理员");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }

    /**
     *联盟成员信息注销回调接口
     * @author handi
     * @date 2019/12/03 17:23:28
     */
    @RequestMapping(value = "/deleteApi", method = RequestMethod.POST)
    public Result deleteApi(@RequestParam Map<String, Object> param){
        log.info("联盟成员信息注销回调接口入参：" + param);
        //申请ID
        String whTmpId =String.valueOf(param.get("whTmpId"));
        //成员ID
        String memberId =String.valueOf(param.get("memberId"));
        //注销原因
        String reason =String.valueOf(param.get("reason"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(whTmpId.equals("null") || whTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(memberId.equals("null") || memberId.equals("")){
            return Result.error().put("msg","正式ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("联盟成员注销失败!").put("resultCode","0");
        }
        //将审批结果更新到log表
        AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
        allianceMemberLogEntity.setTempId(whTmpId);
        allianceMemberLogEntity.setAuditStatus(auditState);
        MembersService.updateLog(allianceMemberLogEntity);
        return Result.ok("联盟成员信息注销成功!").put("resultCode","1");
    }
}
