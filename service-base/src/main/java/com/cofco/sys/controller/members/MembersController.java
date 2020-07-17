package com.cofco.sys.controller.members;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cofco.base.annotation.SysLog;
import com.cofco.base.page.PageHelper;
import com.cofco.base.utils.PageUtils;
import com.cofco.base.utils.Query;
import com.cofco.base.utils.Result;
import com.cofco.base.utils.StringUtils;
import com.cofco.constant.Const;
import com.cofco.constant.LogOper;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.members.AllianceMemberLogEntity;
import com.cofco.sys.entity.members.BillEntity;
import com.cofco.sys.entity.members.BillOperatorLogEntity;
import com.cofco.sys.entity.members.MembersEntity;
import com.cofco.sys.service.members.ClientService;
import com.cofco.sys.service.members.MembersService;
import com.cofco.utils.*;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Console;
import java.io.IOException;

import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 联盟成员注册
 * @author handi
 * @email 876053009@qq.com
 * @date 2019-10-31 17:00:49
 */
@RestController
@RequestMapping("sys/members")
public class MembersController extends BaseController {

    Logger log = Logger.getLogger(MembersController.class);

    @Autowired
    private  MembersService MembersService;
    @Autowired
    private ClientService clientService;

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
    //@SysLog("查看角色")
    public Result info(@PathVariable("id") String id){
        MembersEntity membersEntity = MembersService.queryObject(id);
        return Result.ok().put("role",membersEntity);
    }

    /**
     * 根据id号查询仓单信息
     */
    @RequestMapping("/containNums/{memberId}")
    //@SysLog("查看角色")
    public Result containNums(@PathVariable("memberId") String memberId){
        //查询当前联盟成员下是否包含联盟客户
        int countNums = clientService.containNums(memberId);
        return Result.ok().put("countNums",countNums);
    }

    /**
     * 新增联盟成员(已通)
     * @author handi
     * @date 2019/10/31 17:02:59
     */
    @RequestMapping("/save")
    public Result save(@RequestBody MembersEntity membersEntity){
        Result result = Result.ok();
        try {
            int id = MembersService.seqMemberId();
            membersEntity.setId(String.valueOf(id));
            membersEntity.setVersion("v1");
            membersEntity.setTime_create(new Date());
            membersEntity.setCreator(UserUtils.getCurrentUserId());
            membersEntity.setIs_delete(Const.DELETE_FLG_NO);
            //产品类型
            membersEntity.setProductType("[\"c\"]");
            //产品种类
            membersEntity.setProductCategory("[\"ag\"]");
            //生成公私钥
            Map<String, Object> keyMap = SHA256Util.createKey(SHA256Util.KEY_ALGORITHM, SHA256Util.RAS_KEY_SIZE);
            PublicKey publicKey = (PublicKey) keyMap.get("publicKey");
            PrivateKey privateKey = (PrivateKey) keyMap.get("privateKey");
            // 将公私钥转成字符串
            String publicKeyString = SHA256Util.pubToStr(publicKey);
            String privateKeyString = SHA256Util.priToStr(privateKey);
            //String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxZT5ctUOFz5qdFSvQ5Q9zxbYmfqX1dOXNo7meic6cT0oTzns5uOlecFgIxikWcac823Awaw62r6pHOO5VV8XFTEu1CK6LYWvZAxm/WAdWJyxsJJzPZHBme3ZkVU2leXRLprybzgVXmxcYCqK8K3hAwtW0YmpJDoXFey4xOxojhKb2XPbehsy0KEzLdymfDEnFtsW23ayNpqJv9kcr0IJDfnTgXjAi/2EpI/YVgAWHG2TLfiD8+FXyt431jAMMFTXu5UIxr/91ATHECi+KdeOrznvOUY/CsE413DGs4GiB/IdL6lhZlG5ZXwkAvs4finKEI7v1CEr0EaQAmLTxO7CkwIDAQAB";
            //String privateKeyString ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDFlPly1Q4XPmp0VK9DlD3PFtiZ+pfV05c2juZ6JzpxPShPOezm46V5wWAjGKRZxpzzbcDBrDravqkc47lVXxcVMS7UIrotha9kDGb9YB1YnLGwknM9kcGZ7dmRVTaV5dEumvJvOBVebFxgKorwreEDC1bRiakkOhcV7LjE7GiOEpvZc9t6GzLQoTMt3KZ8MScW2xbbdrI2mom/2RyvQgkN+dOBeMCL/YSkj9hWABYcbZMt+IPz4VfK3jfWMAwwVNe7lQjGv/3UBMcQKL4p146vOe85Rj8KwTjXcMazgaIH8h0vqWFmUbllfCQC+zh+KcoQju/UISvQRpACYtPE7sKTAgMBAAECggEAcDuGUsdZQLTQrZHpGJ8dWQQmN5kUJH8TlwcnPl4IIYjW2+IQgrrvJTFGFcmKmgBq6Q///UODDvxcfa+ReEASkZQlgJTVYsRcc51j5ytwoAQqHx2hXmjsKy7jdl2MKyunXizf53NZGBC/jFS+JabveNY5E795l71znZ2g+yM65PAHTR4dwKWEkM1ZELxMV2McybjacrFC2lZXl+M0sjLkFhjipg1uAkvmzVbqCsitvUAsNHZCSllhldPuMEB3kKQn145nKxETQaj363yK3OIvGUbAFB5eIXDDLfSsKl/E3hxjNpZOiZwj+7QCYm7tNfmhvxTyCjEd67CffsO2OiAv6QKBgQDsMkgl6GGbfLQRVsE4P2Hg1dsDbMIhJaZ9ZIZNXAVKOkLS78OQIIdlrBewng6kmiLfypSAlIVt9DJ7GQmMaRFLOTi1sGH1EFmJvKAQwzp4TtEQ+I0wHw17nId3Fv7DHKdIQnC2w9/M8onZNfSl/Szsg5EMIscmcgcpvnsIY8WwpwKBgQDWJd8hgg0Q99VvloZ5Jy+kyhjkkhaOaiEgLkaA3aJhqwTAlOXlhvZJ7YfKSxml5Xf10ekIDSZmMGNTP6OBbZRY3C7LrmyDsAxM9DgguHzV7n2j7OD0kgX+jdpFT0k28SuPn4ZFAsUC1XZoRYD7Apu6BwyHdg1ypAWngIsDgGtQNQKBgQDS3vlGzrcF6EmjCT8sBnpODt+fqSOQEFsZq/uzaSpX4Lf0Npb0P/ZhsUjdJ9+O2QW1otj+CpstVU0UVYAO2WbVL9sPas+pyNUHgV2uWX4EFunUKvNyOOcCqvbOPbtpWlWGz7ptkWZnRUoIoIExCF1pMvT2Tr5Z9oeqnyKclObUFQKBgEa/9FwJr29vseBQBtV4ENGMw3Sgnzsl7RPIthpVHxTNf6BwkROn5y69fyNidHLOwTdBxo+WaJ1SVz5lxAs6vMwu0ncQhWFshGPYO8LzsKqtRG0oEZo7tCdJjldfvnycaRvmRGIAbD5nb9s7O0Wysuhe89C9O+woLIYOfd8GSm4RAoGBAM+7JL3fkKdefkKgL6TwRwvgDyERTJICE0YGwEjDihAt4dwauKm2TazQN9Atn2scUJG18NdrXda4iREGEQyKwbmcjcucau8CefKDTgtNGH3Ko7XoZSHRxdOD7dilP+wY5e58VXZ8AG3H/0dFoIkqswy+0gBW88bfXSo2FgoqBrqi";
            //将字符串转化成二进制
            /*String publicKeyBinary = SHA256Util.strToBinary(publicKeyString);
            String privateKeyBinary = SHA256Util.strToBinary(privateKeyString);
            membersEntity.setZmemPublicKey(publicKeyBinary);
            membersEntity.setZmemPrivateKey(privateKeyBinary);*/
            membersEntity.setZmemPublicKey(publicKeyString);
            membersEntity.setZmemPrivateKey(privateKeyString);
            //时间戳
            Long  timeNew =  System.currentTimeMillis();
            //随机串
            String nonce = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
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
            map.remove("zmemPrivateKey");
            map.remove("zmemPublicKey");
            map.remove("status");
            map.put("leaseBeginDate",membersEntity.getLeaseBeginDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("leaseEndDate",membersEntity.getLeaseEndDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("productType",membersEntity.getProductType());
            map.put("productCategory",membersEntity.getProductCategory());
            map.put("orgAttach",PhotoUtils.getImageString(membersEntity.getOrgAttach(), PhotoUtils.getImageType(membersEntity.getOrgAttach())));
            map.put("corporateIdAttach",PhotoUtils.getImageString(membersEntity.getCorporateIdAttach(), PhotoUtils.getImageType(membersEntity.getCorporateIdAttach())));
            map.put("contactIdAttach",PhotoUtils.getImageString(membersEntity.getContactIdAttach(), PhotoUtils.getImageType(membersEntity.getContactIdAttach())));
            map.put("positionDiagram",PhotoUtils.getImageString(membersEntity.getPositionDiagram(), PhotoUtils.getImageType(membersEntity.getPositionDiagram())));
            map.put("upperOrgAttach",PhotoUtils.getImageString(membersEntity.getUpperOrgAttach(), PhotoUtils.getImageType(membersEntity.getUpperOrgAttach())));
            map.put("publicKey",publicKeyString);
            map.put("requestId",requestId);
            map.put("timestamp",timeNew);
            map.put("nonce", nonce);
            map.put("version","v1");
            log.info("生成签名前的参数值：" + map);
            //按照参数名ASCII码从小到大排序拼接成字符串stringA。
            String stringA = SignatureUtil.genOrderedString(map);
            //sha256运算后的字符串(sign即为签名)
            String sign=SignatureUtil.genRegSignSortedMap(stringA,nonce);
            map.put("sign",sign);
            log.info("生成签名后的参数值：" + map);
            //调用大商所接口
            String url="http://124.93.249.143:8999/wbc/api/members/whs";
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
            if (Const.STATUS_CODE_202.equals(String.valueOf(response.code()))) {
                //临时成员号
                String whtmpid = String.valueOf(jsonObject.getJSONObject("data").get("whTmpId"));
                //大商所发来的公钥
                String publicK = String.valueOf(jsonObject.getJSONObject("data").get("publickey"));
                //String publicK = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvijyo+KCubEs/miU3ryaw6uqNzVSlONJuO+Vks4SlLJe2zKqFJCDKEiSDbFI8jdNLiRIvOwdBK4Nj8Hb3ukj1RH/enqqQtkgnrSH0SwMvxnXcC4yIZi2se6JmlDTKrlFRNMhHX6c4cpj2/VQBfs3NScaO6ZBFlqm/A+mnutVoyop3SY3OQNBv1B0YanTWVaBgrAtk2CF9CN3wyCp8j9eXCXEHGmHs6jrsIhkJDx+2dqXY+15Dle3D80TV2ckbnalbZdvEkC4HfU6OWBhCuWBFU3XzW0fuphU1bnFtFogKHty7k+3PrDFiYCj7QC9dNo4HVfp6/DA0jboEg4uU6wtwFtlgysd1Tfac4wWKJl0UYizZCTi6Ip27r2xZZ4Egh7mBF/64BJqS1DM7E3CLBp96MYnFvOXpN8D4uTpuXnvZRjVC//wGAAmIiQPp+PLqp2dOwVh2B48l62A1/+orrP0/P2nhjSRia+Q0tCnApJkDvcE3/rvEFi4oCdhYzWcEmo1WOomA4HltQdgF1zKyIqZrYFd8fCBR5n9JscWBf/OCXvKfJAXtiWGsav0FvH9Av0tWz1xVWgQ3880e6qc7zoq84GcuV9RcKM9bCcCiuhShjCLQt7xCNu73KlsUd09sE/V+elGSP3M/aJ2pix4Q4HuX/+zd294Lp6n1W/Gu5UvB60CAwEAAQ==";
                membersEntity.setStatus("1");
                //将联盟成员信息新增入库
                int t = MembersService.save(membersEntity);
                //联盟成员新增入库失败
                if(t == 0){
                    return Result.error().put("msg","成员新增失败");
                }
                //将大商所返回的临时ID新增到ALLIANCE_MEMBER_LOG表
            int lid = MembersService.seqMemberLogId();
            AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
            allianceMemberLogEntity.setId(String.valueOf(lid));
            allianceMemberLogEntity.setAllianceMemberId(String.valueOf(id));
            allianceMemberLogEntity.setTempId(whtmpid);
            allianceMemberLogEntity.setOperatorType(LogOper.MEMBER_SAVE);
            MembersService.saveLog(allianceMemberLogEntity);
            //将公钥更新到ALLIANCE_MEMBER表对应的联盟成员信息上去
            MembersEntity member = new MembersEntity();
            member.setId(String.valueOf(id));
            member.setPublicKey(publicK);
            //member.setTime_modify(new Date());
            member.setModifier(UserUtils.getCurrentUserId());
            MembersService.updateRevert(member);
                result = Result.ok("联盟成员注册申请已提交,交易所审核中");
            }else {
                result =  Result.error("联盟成员注册失败!"+jsonObject.get("msg").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("联盟成员新增失败!");
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
            try{
                //将审批结果更新到ALLIANCE_MEMBER_LOG表
                AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
                allianceMemberLogEntity.setTempId(whTmpId);
                allianceMemberLogEntity.setAuditStatus(auditState);
                MembersService.updateLog(allianceMemberLogEntity);
                //将大商所返回的联盟成员ID更新到ALLIANCE_MEMBER表中对应的成员上面
                Map<String,Object> map = new HashMap<String, Object>();
                if(auditState.equals("0")) {
                    map.put("memberId", memberId);
                    map.put("is_delete", "1");
                    map.put("status", "3");
                    map.put("whTmpId", whTmpId);
                    MembersService.updateMemberId(map);
                    return Result.error("联盟成员注册失败!").put("resultCode","1");
                }
                map.put("memberId", memberId);
                map.put("status", "2");
                map.put("whTmpId", whTmpId);
                MembersService.updateMemberId(map);
            }catch(Exception e) {
                e.printStackTrace();
            }
         return Result.ok("联盟成员注册成功").put("resultCode","1");
     }

    /**
     * 联盟成员信息查询(已通)
     * @author handi
     * @date 2019/10/31 16:03:12
     */
    @RequestMapping("/queryMember")
    public Result queryMember(){
        SortedMap<String, String> requestMapf = new TreeMap<>();
        //MembersEntity member = MembersService.queryKey("111000");
        String orgNo = "91261234220200865F";
        Long  timeNew =  System.currentTimeMillis();
        String nonces = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        String requestIds = SHA256Util.getUUID32();
        requestMapf.put("version","v1");
        requestMapf.put("requestId",requestIds);
        requestMapf.put("timestamp",String.valueOf(timeNew));
        requestMapf.put("nonce",nonces);
        log.info("生成签名前的参数值：" + requestMapf);
        String memberId ="wh00379";
        MembersEntity membersEntity = MembersService.queryKey(memberId.trim());
        requestMapf.put("sign", SHA256Util.createSignT(requestMapf,membersEntity));
        //requestMapf.put("sign",SHA256Util.createSign(requestMapf));
        log.info("生成签名后的参数值：" + requestMapf);
        String version = String.valueOf(requestMapf.get("version")).trim();
        String requestId = String.valueOf(requestMapf.get("requestId")).trim();
        String timestamp = String.valueOf(requestMapf.get("timestamp")).trim();
        String nonce = String.valueOf(requestMapf.get("nonce")).trim();
        String sign = String.valueOf(requestMapf.get("sign")).trim();
        try{
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://124.93.249.143:8999/wbc/api/members/whs/"+orgNo+"?version="+version+"&requestId="+requestId+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
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
            return Result.error("成员信息查询失败!");
        }
        return null;
    }

    /**
     * 联盟成员汇聚状态查询(已通)
     */
    @RequestMapping("/queryStatus")
    public Result queryStatus(){
        SortedMap<String, String> requestMapf = new TreeMap<>();
        //MembersEntity member = MembersService.queryKey("111000");
        //组织机构代码
        String orgNos = "91261234220200865F";
        //临时ID
        String whTmpId = "621";
        Long  timeNew =  System.currentTimeMillis();
        String nonces = String.valueOf(RandomStringUtils.randomAlphanumeric(20));
        String requestIds = SHA256Util.getUUID32();
        requestMapf.put("orgNo",orgNos);
        requestMapf.put("version","v1");
        requestMapf.put("requestId",requestIds);
        requestMapf.put("timestamp",String.valueOf(timeNew));
        requestMapf.put("nonce",nonces);
        log.info("生成签名前的参数值：" + requestMapf);
        requestMapf.put("sign",SHA256Util.createSign(requestMapf));
        log.info("生成签名后的参数值：" + requestMapf);
        String orgNo = String.valueOf(requestMapf.get("orgNo")).trim();
        String version = String.valueOf(requestMapf.get("version")).trim();
        String requestId = String.valueOf(requestMapf.get("requestId")).trim();
        String timestamp = String.valueOf(requestMapf.get("timestamp")).trim();
        String nonce = String.valueOf(requestMapf.get("nonce")).trim();
        String sign = String.valueOf(requestMapf.get("sign")).trim();
        try{
            String signn = URLEncoder.encode(sign,"UTF-8");
            String methodUrl ="http://124.93.249.143:8999/wbc/api/members/whs/as/"+whTmpId+"?orgNo="+orgNo+"&version="+version+"&requestId="+requestId+"&timestamp="+timestamp+"&nonce="+nonce+"&sign="+signn;
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
            return Result.error("成员汇聚状态查询失败!");
        }
        return null;
    }

    /**
     * 成员信息修改
     * @author handi
     * @date 2019/10/31 17:04:13
     */
    @RequestMapping("/update")
    public Result update(@RequestBody MembersEntity membersEntity){
        Result result = Result.ok();
        //当前修改人成员ID号
        //String memberIdd = membersEntity.getMemberId();
        //当前登录人的联盟成员ID
        //String memberId = "wh20200525";
        //在数据库查询出当前联盟成员自己的一套公私钥
        /*MembersEntity member = MembersService.queryKey(memberId);
        String publicKeyBinary = String.valueOf(member.getZmemPublicKey());
        //公钥字符串
        String publicKeyStr = SHA256Util.binaryToString(publicKeyBinary);*/
        try {
            //修改时间
            membersEntity.setTime_modify(new Date());
            // 修改人
            membersEntity.setModifier(UserUtils.getCurrentUserId());
            //当前修改人ID号
            String id = membersEntity.getId();
            //修改人组织机构代码
            String orgNo = membersEntity.getOrgNo();
            Long  timeNew =  System.currentTimeMillis();
            String nonce =RandomStringUtils.randomAlphanumeric(20);
            String uuid = SHA256Util.getUUID32();
            TreeMap map = SignatureUtil.entityToMap(membersEntity);
            map.put("leaseBeginDate",membersEntity.getLeaseBeginDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("leaseEndDate",membersEntity.getLeaseEndDate().replaceAll("[[\\s-:punct:]]",""));
            map.put("orgAttach",PhotoUtils.getImageString(membersEntity.getOrgAttach(), PhotoUtils.getImageType(membersEntity.getOrgAttach())));
            map.put("corporateIdAttach",PhotoUtils.getImageString(membersEntity.getCorporateIdAttach(), PhotoUtils.getImageType(membersEntity.getCorporateIdAttach())));
            map.put("contactIdAttach",PhotoUtils.getImageString(membersEntity.getContactIdAttach(), PhotoUtils.getImageType(membersEntity.getContactIdAttach())));
            map.put("positionDiagram",PhotoUtils.getImageString(membersEntity.getPositionDiagram(), PhotoUtils.getImageType(membersEntity.getPositionDiagram())));
            map.put("upperOrgAttach",PhotoUtils.getImageString(membersEntity.getUpperOrgAttach(), PhotoUtils.getImageType(membersEntity.getUpperOrgAttach())));
            map.put("productType",membersEntity.getProductType());
            map.put("productCategory",membersEntity.getProductCategory());
            map.put("publicKey",membersEntity.getZmemPublicKey());
            map.put("requestId",uuid);
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
            map.remove("zmemPrivateKey");
            map.remove("zmemPublicKey");
            map.remove("status");
            log.info("签名前参数"+map);
            //调用大商所接口
            map.put("sign", SHA256Util.createSign(map));
            log.info("签名后参数"+map);
            String url="http://124.93.249.143:8999/wbc/api/members/whs/"+orgNo;
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
                //申请ID
                String whtmpid = jsonObject.getJSONObject("data").getString("whTmpId");
                        //将信息更新到数据库
               /* int t = MembersService.update(membersEntity);
                //联盟成员信息修改失败
                if(t == 0){
                    return Result.error("成员信息修改失败");
                }*/
                //暂时将需要修改的联盟成员信息存入redis中
                String jsonString = JSON.toJSONString(membersEntity);
                RedisUtil.setString(whtmpid,jsonString,-1);
                //将status变为修改审核中
                MembersEntity membersEntitys = new MembersEntity();
                membersEntitys.setId(id);
                membersEntitys.setStatus("4");
                MembersService.delete(membersEntitys);
                //Sequence
                int lid = MembersService.seqMemberLogId();
                //将临时ID新增到log表
                AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
                allianceMemberLogEntity.setId(String.valueOf(lid));
                allianceMemberLogEntity.setAllianceMemberId(id);
                allianceMemberLogEntity.setTempId(whtmpid);
                allianceMemberLogEntity.setOperatorType(LogOper.MEMBER_UPDATE);
                MembersService.saveLog(allianceMemberLogEntity);
                result = Result.ok("联盟成员信息修改已提交，交易所审核中");
            } else {
                result = Result.error("联盟成员信息修改失败!"+jsonObject.get("msg").toString());
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
           log.info("成员信息修改回调接口入参：" + param);
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
                return Result.ok("联盟成员信息修改成功!").put("resultCode","1");
            }
            try{
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
            }catch(Exception e) {
                System.out.print(e + "================联盟成员修改回调异常信息");
                e.printStackTrace();
            }
            /*try{
                //将审批结果更新到log表
                AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
                allianceMemberLogEntity.setTempId(whTmpId);
                allianceMemberLogEntity.setAuditStatus(auditState);
                MembersService.updateLog(allianceMemberLogEntity);
                if(auditState.equals("0")){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("status","6");
                    map.put("whTmpId",whTmpId);
                    MembersService.updateMemberId(map);
                    RedisUtil.del(whTmpId);
                    return Result.error("联盟成员信息修改失败!").put("resultCode","1");
                }
                //将redis中缓存的信息，更新到表中
                MembersEntity membersEntity = JSON.parseObject(RedisUtil.getString(whTmpId), MembersEntity.class);
                membersEntity.setTime_create(null);
                membersEntity.setCreator(null);
                membersEntity.setIs_delete(null);
                membersEntity.setPublicKey(null);
                membersEntity.setWhtmpid(null);
                membersEntity.setTime_modify(null);
                MembersService.update(membersEntity);
                //将联盟成员ID更新到member表
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("memberId",memberId);
                map.put("status","5");
                map.put("whTmpId",whTmpId);
                MembersService.updateMemberId(map);
                //将redis中对应的临时信息删除
                RedisUtil.del(whTmpId);
            }catch(Exception e) {
                e.printStackTrace();
                return Result.error("联盟成员信息修改失败");
            }*/
            return Result.ok("联盟成员信息修改成功").put("resultCode","1");
    }

    //----联盟成员信息注销(临时测试hd)
    @RequestMapping("/delete")
    public Result delete(@RequestBody MembersEntity membersEntity){
        Result result = Result.ok();
        try{
            MembersEntity membersEntitys = new MembersEntity();
            //被删除成员的ID
            membersEntitys.setId(membersEntity.getId());
            //membersEntitys.setIs_delete(Const.DELETE_FLG_YES);
            TreeMap<String,String> map = new TreeMap<>();
            String none =  String.valueOf(RandomStringUtils.randomAlphanumeric(20));
            String requestId = SHA256Util.getUUID32();
            Long  timeNew =  System.currentTimeMillis();
            //联盟成员ID
            String memberId =membersEntity.getMemberId();
            //MembersEntity member = MembersService.queryKey(memberId);
            //组织机构编码
            String orgNo = membersEntity.getOrgNo();
            map.put("memberId",memberId);
            //map.put("logoutContent","wrong format");
            map.put("version","v1");
            map.put("requestId",requestId);
            map.put("timestamp",String.valueOf(timeNew));
            map.put("nonce", none);
            map.put("sign", SHA256Util.createSign(map));
            //map.put("sign", SHA256Util.createSignT(map,member));
            String sign = map.get("sign").trim();
            String signn = URLEncoder.encode(sign,"UTF-8");
            log.info("请求参数：" + map);
            //59.46.215.173 老IP
            String methodUrl ="http://124.93.249.143:8999/wbc/api/members/whs/"+orgNo+"?memberId="+memberId+"&version="+map.get("version")+"&requestId="+requestId+"&timestamp="+timeNew+"&nonce="+none+"&sign="+signn;
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
                //申请ID
                String whTmpId = jsonObject.getJSONObject("data").getString("whTmpId");
                membersEntitys.setStatus("7");
                MembersService.delete(membersEntitys);
                //Sequence
                int lid = MembersService.seqMemberLogId();
                //将临时ID新增到log表
                AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
                allianceMemberLogEntity.setId(String.valueOf(lid));
                allianceMemberLogEntity.setAllianceMemberId(membersEntity.getId());
                allianceMemberLogEntity.setTempId(whTmpId);
                allianceMemberLogEntity.setOperatorType(LogOper.MEMBER_DELETE);
                MembersService.saveLog(allianceMemberLogEntity);
                result = Result.ok("联盟成员注销请求已提交，交易所审核中");
            } else {
                result = Result.error("联盟成员信息注销失败!"+jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
        return result;
    }

    /**
     *联盟成员信息注销回调接口(临时测试hd)
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
        try{
            //将审批结果更新到log表
            AllianceMemberLogEntity allianceMemberLogEntity = new AllianceMemberLogEntity();
            allianceMemberLogEntity.setTempId(whTmpId);
            allianceMemberLogEntity.setAuditStatus(auditState);
            MembersService.updateLog(allianceMemberLogEntity);
            Map<String,Object> map = new HashMap<String, Object>();
            if(auditState.equals("0")) {
                map.put("status","9");
                map.put("whTmpId", whTmpId);
                MembersService.updateMemberId(map);
                return Result.error("联盟成员注销失败!").put("resultCode","1");
            }
            map.put("is_delete","1");
            map.put("status","8");
            map.put("whTmpId",whTmpId);
            MembersService.updateMemberId(map);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return Result.ok("联盟成员信息注销成功!").put("resultCode","1");
    }

}