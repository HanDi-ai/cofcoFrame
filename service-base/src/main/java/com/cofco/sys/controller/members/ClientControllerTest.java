package com.cofco.sys.controller.members;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cofco.base.utils.Result;
import com.cofco.constant.Const;
import com.cofco.constant.LogOper;
import com.cofco.sys.entity.members.AllianceClientEntity;
import com.cofco.sys.entity.members.AllianceClientLogEntity;
import com.cofco.sys.entity.members.MembersEntity;
import com.cofco.sys.service.members.ClientService;
import com.cofco.sys.service.members.MembersService;
import com.cofco.utils.PhotoUtils;
import com.cofco.utils.SHA256Util;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 联盟客户注册
 *
 * @author cofco
 * @date 2019-10-29 13:42:42
 */
@RestController
@RequestMapping("/sys/clientt")
public class ClientControllerTest {

    @Autowired
    ClientService clientService;

    @Autowired
    MembersService membersService;
    Logger log = Logger.getLogger(MembersController.class);

    /**
     * 联盟客户注册
     */
    @RequestMapping("/save")
    public Result save(@RequestBody AllianceClientEntity client) {
        // 请求参数
        SortedMap<String, String> requestMap = new TreeMap<>();
        Map<String,Object> bankMap = new HashMap<>();
        //联盟成员ID
        String memberId="wh000069";
        //请求ID
        String requestId= SHA256Util.getUUID32();
        //查询出当前所属联盟成员的公私钥
        MembersEntity membersEntity = membersService.queryKey(memberId);
        requestMap.put("memberId", memberId);
        //公司名称
        requestMap.put("clientName", client.getClientName());
        //公司地址
        requestMap.put("clientAddr", client.getClientAddr());
        //公司电话
        requestMap.put("clientTel", client.getClientTel());
        //公司组织机构代码
        requestMap.put("clientOrgNo", client.getClientOrgNo());
        //公司营业执照附件
        requestMap.put("clientOrgAttach", PhotoUtils.getImageString(client.getClientOrgAttach(), PhotoUtils.getImageType(client.getClientOrgAttach())));
        //注册资金
        requestMap.put("regMoney", client.getRegMoney());
        //开户银行信息
        //requestMap.put("bankInfo", client.getBankInfo());
        bankMap.put("农业银行","122416433627");
        bankMap.put("大连银行","226432474676");
        String  param= JSON.toJSONString(bankMap);
        requestMap.put("bankInfo",param);
        //单位性质
        requestMap.put("property", client.getProperty());
        //法人代表姓名
        requestMap.put("corporateName", client.getCorporateName());
        //法人代表电话
        requestMap.put("corporateTel", client.getCorporateTel());
        //法人代表身份证号码
        requestMap.put("corporateId", client.getCorporateId());
        //法人代表身份证附件
        requestMap.put("corporateIdAttach", PhotoUtils.getImageString(client.getCorporateIdAttach(), PhotoUtils.getImageType(client.getCorporateIdAttach())));
        //联系人姓名
        requestMap.put("contactName", client.getContactName());
        //联系人电话
        requestMap.put("contactTel", client.getContactTel());
        //联系人身份证号码
        requestMap.put("contactId", client.getContactId());
        //联系人身份证附件
        requestMap.put("contactIdAttach", PhotoUtils.getImageString(client.getContactIdAttach(), PhotoUtils.getImageType(client.getContactIdAttach())));
        //版本
        requestMap.put("version", "v1");
        //请求ID
        requestMap.put("requestId", requestId);
        //时间戳
        requestMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        //随机数
        requestMap.put("nonce", RandomStringUtils.randomAlphanumeric(20));
        requestMap.put("sign", SHA256Util.createSignT(requestMap,membersEntity));
        // 签名
        try {
            String url="http://59.46.215.173:8999/wbc/api/clients";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("memberId",memberId)
                    .add("clientName",requestMap.get("clientName"))
                    .add("clientAddr",requestMap.get("clientAddr"))
                    .add("clientTel",requestMap.get("clientTel"))
                    .add("clientOrgNo",requestMap.get("clientOrgNo"))
                    .add("clientOrgAttach",requestMap.get("clientOrgAttach"))
                    .add("regMoney",requestMap.get("regMoney"))
                    //.add("bankInfo","{\"农业银行\":\"122416433627\",\"大连银行\":\"226432474676\"}")
                    .add("bankInfo",requestMap.get("bankInfo"))
                    .add("property",requestMap.get("property"))
                    .add("corporateName",requestMap.get("corporateName"))
                    .add("corporateTel",requestMap.get("corporateTel"))
                    .add("corporateId",requestMap.get("corporateId"))
                    .add("corporateIdAttach",requestMap.get("corporateIdAttach"))
                    .add("contactName",requestMap.get("contactName"))
                    .add("contactTel",requestMap.get("contactTel"))
                    .add("contactId",requestMap.get("contactId"))
                    .add("contactIdAttach",requestMap.get("contactIdAttach"))
                    .add("version","v1")
                    .add("requestId",requestId)
                    .add("nonce", requestMap.get("nonce"))
                    .add("timestamp",requestMap.get("timestamp"))
                    .add("sign",requestMap.get("sign"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);
            // 响应码:202
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //client表序列号
                String clientSeq = clientService.clientSeq();
                //ID
                client.setId(clientSeq);
                //联盟客户注册
                clientService.save(client);
                //联盟客户日志表
                AllianceClientLogEntity clientLogEntity = new AllianceClientLogEntity();
                //联盟客户表ID
                clientLogEntity.setAllianceClientId(clientSeq);
                //临时ID
               clientLogEntity.setTempId(String.valueOf(jsonObject.getJSONObject("data").get("clientTmpId")));
                //操作类型
                clientLogEntity.setOperatorType(LogOper.CLIENT_SAVE);
                //联盟客户日志表注册
                clientService.saveLog(clientLogEntity);
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    /**
     * 联盟客户注册回调接口
     */
    @RequestMapping(value = "/saveApi", method = RequestMethod.POST)
    public Result saveApi(@RequestParam Map<String, Object> param){
        log.info("返回的参数值"+param);
        //申请ID
        String clientTmpId = String.valueOf(param.get("clientTmpId"));
        //客户ID
        String clientId = String.valueOf(param.get("clientId"));
        //审批结果
        String auditState = String.valueOf(param.get("auditState"));
        if(clientTmpId.equals("null") || clientTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(clientId.equals("null") || clientId.equals("")){
            return Result.error().put("msg","客户ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审批结果不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("联盟客户注册失败!").put("resultCode","0");
        }
        //将联盟客户ID更新到client表
        clientService.updateClientId(clientId, clientTmpId);
        //审核结果
        AllianceClientLogEntity clientLogEntity = new AllianceClientLogEntity();
        clientLogEntity.setTempId(clientTmpId);
        clientLogEntity.setAuditStatus(auditState);
        clientService.updateLog(clientLogEntity);
        return Result.ok().put("resultCode","1");
    }

    /**
     * 联盟客户信息查询
     */
    @RequestMapping("/queryClient")
    public Result queryClient() {
        try {
            SortedMap<String, String> requestMapf = new TreeMap<>();
            //联盟成员ID
            String memberIds = "wh00006";
            //客户组织机构代码
            String clientOrgNos = "752689752";
            //客户ID
            String clientId ="c000000017";
            //查询当前联盟成员的公私钥
            MembersEntity membersEntity = membersService.queryKey(memberIds);
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            //随机串
            String nonces = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestIds = SHA256Util.getUUID32();
            requestMapf.put("memberId",memberIds);
            requestMapf.put("clientOrgNo",clientOrgNos);
            requestMapf.put("version","v1");
            requestMapf.put("requestId",requestIds);
            requestMapf.put("timestamp",String.valueOf(timeNew));
            requestMapf.put("nonce",nonces);
            log.info("生成签名前的参数值：" + requestMapf);
            requestMapf.put("sign",SHA256Util.createSignT(requestMapf,membersEntity));
            log.info("生成签名后的参数值：" + requestMapf);

            String memberId = requestMapf.get("memberId").trim();
            String clientOrgNo = requestMapf.get("clientOrgNo").trim();
            String version = requestMapf.get("version").trim();
            String requestId = requestMapf.get("requestId").trim();
            String timestamp = requestMapf.get("timestamp").trim();
            String nonce = requestMapf.get("nonce").trim();
            String sign = requestMapf.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://59.46.215.173:8999/wbc/api/clients/"+clientId+"?memberId="+memberId+"&clientOrgNo="+clientOrgNo+"&version="+version+"&requestId="+requestId+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
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
                log.info("联盟客户信息:"+response.message());
            }else{
                log.info("联盟客户信息查询接口请求失败!");
                log.info("返回响应值：" + jsonObject);
            }
            } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    /**
     * 联盟客户汇聚状态查询
     */
    @RequestMapping("/queryStatus")
    public Result queryStatus(){
        SortedMap<String, String> requestMapf = new TreeMap<>();
        //联盟成员ID
        String memberIds = "9999";
        //临时ID
        String clientTmpId = "33";
        //查询当前联盟成员的公私钥
        MembersEntity membersEntity = membersService.queryKey(memberIds);
        //时间戳
        Long  timeNew =  System.currentTimeMillis();
        //随机数
        String nonces = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        //请求ID
        String requestIds = SHA256Util.getUUID32();
        requestMapf.put("memberId",memberIds);
        requestMapf.put("version","v1");
        requestMapf.put("requestId",requestIds);
        requestMapf.put("timestamp",String.valueOf(timeNew));
        requestMapf.put("nonce",nonces);
        log.info("生成签名前的参数值：" + requestMapf);
        requestMapf.put("sign",SHA256Util.createSignT(requestMapf,membersEntity));
        log.info("生成签名后的参数值：" + requestMapf);
        String memberId = String.valueOf(requestMapf.get("memberId")).trim();
        String version = String.valueOf(requestMapf.get("version")).trim();
        String requestId = String.valueOf(requestMapf.get("requestId")).trim();
        String timestamp = String.valueOf(requestMapf.get("timestamp")).trim();
        String nonce = String.valueOf(requestMapf.get("nonce")).trim();
        String sign = String.valueOf(requestMapf.get("sign")).trim();
        try{
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://59.46.215.173:8999/wbc/api/clients/as/"+clientTmpId+"?memberId="+memberId+"&version="+version+"&requestId="+requestId+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
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
                log.info("联盟客户汇聚状态:"+response.message());
            }else{
                log.info("联盟客户汇聚状态接口请求失败!");
                log.info("返回响应值：" + jsonObject);
            }
            } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return null;
    }

    /**
     * 客户信息修改
     * @param client
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody AllianceClientEntity client) {
        SortedMap<String, String> requestMap = new TreeMap<>();
        Map<String,Object> bankMap = new HashMap<>();
        //联盟成员ID
        String memberId="wh00008";
        //联盟客户ID
        String clientId="c000000017";
        //时间戳
        Long  timeNew =  System.currentTimeMillis();
        //随机数
        String nonces = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        //请求ID
        String requestIds = SHA256Util.getUUID32();
        //查询当前联盟成员的公私钥
        MembersEntity membersEntity = membersService.queryKey(memberId);
        requestMap.put("memberId", memberId);
        //公司名称
        requestMap.put("clientName", client.getClientName());
        //公司地址
        requestMap.put("clientAddr", client.getClientAddr());
        //公司电话
        requestMap.put("clientTel", client.getClientTel());
        //公司组织机构代码
        requestMap.put("clientOrgNo", client.getClientOrgNo());
        //公司营业执照附件
        requestMap.put("clientOrgAttach", PhotoUtils.getImageString(client.getClientOrgAttach(), PhotoUtils.getImageType(client.getClientOrgAttach())));
        //注册资金
        requestMap.put("regMoney", client.getRegMoney());
        //开户银行信息
        //requestMap.put("bankInfo", client.getBankInfo());
        bankMap.put("农业银行","122416433627");
        bankMap.put("大连银行","226432474676");
        String  param= JSON.toJSONString(bankMap);
        requestMap.put("bankInfo",param);
        //单位性质
        requestMap.put("property", client.getProperty());
        //法人代表姓名
        requestMap.put("corporateName", client.getCorporateName());
        //法人代表电话
        requestMap.put("corporateTel", client.getCorporateTel());
        //法人代表身份证号码
        requestMap.put("corporateId", client.getCorporateId());
        //法人代表身份证附件
        requestMap.put("corporateIdAttach", PhotoUtils.getImageString(client.getCorporateIdAttach(), PhotoUtils.getImageType(client.getCorporateIdAttach())));
        //联系人姓名
        requestMap.put("contactName", client.getContactName());
        //联系人电话
        requestMap.put("contactTel", client.getContactTel());
        //联系人身份证号码
        requestMap.put("contactId", client.getContactId());
        //联系人身份证附件
        requestMap.put("contactIdAttach", PhotoUtils.getImageString(client.getContactIdAttach(), PhotoUtils.getImageType(client.getContactIdAttach())));
        //版本
        requestMap.put("version", "v1");
        //请求ID
        requestMap.put("requestId", requestIds);
        //时间戳
        requestMap.put("timestamp", String.valueOf(timeNew));
        //随机数
        requestMap.put("nonce", nonces);
        //签名
        requestMap.put("sign", SHA256Util.createSignT(requestMap,membersEntity));
        try {
            /*String url="http://59.46.215.173:8999/wbc/api/clients/"+clientId;
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            okhttp3.FormBody formBody = new okhttp3.FormBody.Builder()
                    .add("memberId",memberId)
                    .add("clientName",requestMap.get("clientName"))
                    .add("clientAddr",requestMap.get("clientAddr"))
                    .add("clientTel",requestMap.get("clientTel"))
                    .add("clientOrgNo",requestMap.get("clientOrgNo"))
                    .add("clientOrgAttach",requestMap.get("clientOrgAttach"))
                    .add("regMoney",requestMap.get("regMoney"))
                    //.add("bankInfo","{\"农业银行\":\"122416433627\",\"大连银行\":\"226432474676\"}")
                    .add("bankInfo",requestMap.get("bankInfo"))
                    .add("property",requestMap.get("property"))
                    .add("corporateName",requestMap.get("corporateName"))
                    .add("corporateTel",requestMap.get("corporateTel"))
                    .add("corporateId",requestMap.get("corporateId"))
                    .add("corporateIdAttach",requestMap.get("corporateIdAttach"))
                    .add("contactName",requestMap.get("contactName"))
                    .add("contactTel",requestMap.get("contactTel"))
                    .add("contactId",requestMap.get("contactId"))
                    .add("contactIdAttach",requestMap.get("contactIdAttach"))
                    .add("version","v1")
                    .add("requestId",requestMap.get("requestId"))
                    .add("nonce", requestMap.get("nonce"))
                    .add("timestamp",requestMap.get("timestamp"))
                    .add("sign",requestMap.get("sign"))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .put(formBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            log.info("响应结果：" + jsonObject);*/
            // 响应码:201
            if (Const.STATUS_CODE_202.equals("202")) {
            //if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {

                    //联盟客户修改
                clientService.update(client);

                //联盟客户日志表
                AllianceClientLogEntity clientLogEntity = new AllianceClientLogEntity();
                //联盟客户表ID
                clientLogEntity.setAllianceClientId(client.getId());
                //临时ID
                //clientLogEntity.setTempId(jsonObject.getString("clientTmpId"));
                clientLogEntity.setTempId("32");
                //操作类型
                clientLogEntity.setOperatorType(LogOper.CLIENT_UPDATE);
                //联盟客户日志表
                clientService.saveLog(clientLogEntity);
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
        return Result.ok();
    }

    /**
     *联盟客户信息修改回调接口
     */
    @RequestMapping(value = "/updateApi", method = RequestMethod.POST)
    public Result updateApi(@RequestParam Map<String, Object> param){
        log.info("联盟客户信息修改回调接口入参：" + param);
        //申请ID
        String clientTmpId =String.valueOf(param.get("clientTmpId"));
        //客户ID
        String clientId =String.valueOf(param.get("clientId"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(clientTmpId.equals("null") || clientTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(clientId.equals("null") || clientId.equals("")){
            return Result.error().put("msg","正式ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        if(auditState.equals("0")){
            return Result.error("联盟客户修改失败!").put("resultCode","0");
        }
        AllianceClientLogEntity clientLogEntity = new AllianceClientLogEntity();
        clientLogEntity.setTempId(clientTmpId);
        clientLogEntity.setAuditStatus(auditState);
        clientService.updateLog(clientLogEntity);
        return Result.ok("联盟客户信息修改成功!").put("resultCode","1");
    }

    /**
     * 联盟客户信息注销始
     */
    @RequestMapping("/delete")
    public Result delete(){
        Result result = Result.ok();
        //客户ID
        String id="41";
        //联盟客户信息查询
        AllianceClientEntity client = clientService.queryObject(id);
        if (client == null) {
            return Result.error().put("msg","客户不存在!");
        }
        try{
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String memberId =client.getMemberId();
            //客户ID
            String clientId = client.getClientId();
            //公司组织机构编码
            String clientOrgNo = client.getClientOrgNo();
            //查询当前联盟成员的公私钥
            MembersEntity membersEntity = membersService.queryKey(memberId);
            map.put("memberId",client.getMemberId());
            map.put("clientOrgNo",client.getClientOrgNo());
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSignT(map,membersEntity));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            String methodUrl ="http://59.46.215.173:8999/wbc/api/clients/"+clientId+"?memberId="+memberId+"&clientOrgNo="+clientOrgNo+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
                    //联盟客户信息删除
                clientService.delete(client);
                //联盟客户日志表
                AllianceClientLogEntity clientLogEntity = new AllianceClientLogEntity();
                //联盟客户表ID
                clientLogEntity.setAllianceClientId(client.getId());
                //临时ID
                clientLogEntity.setTempId(jsonObject.getString("clientTmpId"));
                //操作类型
                clientLogEntity.setOperatorType(LogOper.CLIENT_DELETE);
                //联盟客户日志表
                clientService.saveLog(clientLogEntity);
            } else {
                return Result.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error();
        }
        return result;
    }



    /**
     *联盟客户信息注销回调接口
     */
    @RequestMapping(value = "/deleteApi", method = RequestMethod.POST)
    public Result deleteApi(@RequestParam Map<String, Object> param){
        log.info("联盟客户信息注销回调接口入参：" + param);
        //申请ID
        String clientTmpId =String.valueOf(param.get("clientTmpId"));
        //客户ID
        String clientId =String.valueOf(param.get("clientId"));
        //注销原因
        String reason =String.valueOf(param.get("reason"));
        //审核状态
        String auditState = String.valueOf(param.get("auditState"));
        if(clientTmpId.equals("null") || clientTmpId.equals("")){
            return Result.error().put("msg","申请ID不能为空");
        }
        if(clientId.equals("null") || clientId.equals("")){
            return Result.error().put("msg","正式ID不能为空");
        }
        if(auditState.equals("null") || auditState.equals("")){
            return Result.error().put("msg","审核状态不能为空");
        }
        AllianceClientLogEntity clientLogEntity = new AllianceClientLogEntity();

        clientLogEntity.setTempId(clientTmpId);
        clientLogEntity.setAuditStatus(auditState);
        clientService.updateLog(clientLogEntity);
        return Result.ok("联盟客户信息注销成功!").put("resultCode","1");
    }
}
