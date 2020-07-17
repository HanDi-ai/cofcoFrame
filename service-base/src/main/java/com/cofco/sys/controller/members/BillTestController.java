package com.cofco.sys.controller.members;



import com.alibaba.fastjson.JSONObject;
import com.cofco.base.utils.Result;
import com.cofco.constant.Const;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.members.BillEntity;
import com.cofco.sys.entity.members.BillOperatorEntity;
import com.cofco.sys.service.members.BillService;
import com.cofco.utils.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/sys/BillTest")
public class BillTestController extends BaseController {
    @Autowired
    BillService billService;

    /**
     * 从视图拉取对应客户的仓单信息
     * 客户选择需要的仓单信息进行备案
     */
    @RequestMapping("/beian")
    public void beian(){
        //String clientId="联盟客户ID";-
        BillEntity billEntity = new BillEntity();
        //billEntity.setClientId("clientId");
        billEntity.setClientContactTel("15611460628");
        //List list=billService.queryList(billEntity);
        //将客户选择需要备案的仓单信息循环插入到bill表中
        //for(int i=0;i<list.size();i++){
            //billService.save(list.get(i));
            //billService.save(billEntity);
        //}

        //将此参数推送到大商所进行仓单备案
        //请求大商所的仓单备案接口
        TreeMap<String,String> paraMap = new TreeMap<String,String>();
        paraMap.put("isDelivery","2");
        paraMap.put("whType", "1");
        paraMap.put("whName", "玉米库");
        paraMap.put("whAbbr","yumi");
        //ascii排序后的字符串
        String stringA= SignatureUtil.genOrderedString(paraMap);
        //sha256运算后的字符串(sign即为签名)
        try{
            Map<String, Object> keyMap = SHA256Util.createKey(SHA256Util.KEY_ALGORITHM, SHA256Util.RAS_KEY_SIZE);
            PublicKey publicKey = (PublicKey) keyMap.get("publicKey");
            PrivateKey privateKey = (PrivateKey) keyMap.get("privateKey");

            // 得到公钥字符串
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            //membersEntity.setMemPublicKey(publicKeyString);
            //membersEntity.setMemPrivateKey(privateKeyString);
            //解码获得公钥
            byte[] decoded = Base64.decodeBase64(publicKeyString);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            System.out.print(pubKey);
            //解码获得私钥
            byte[] decodedd = Base64.decodeBase64(privateKeyString);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedd));
            System.out.print(priKey);
            /* //私钥加密
            byte[] signBytes = SHA256Util.sign(privateKey,stringA);
            //公钥验签
            Boolean b=SHA256Util.verifySign(publicKey, stringA, signBytes);
            System.out.print(b);*/
        }catch (Exception e){
            e.printStackTrace();
        }

        //String sign=SignatureUtil.genRegSignSortedMap(stringA);
        /*MediaType media = MediaType.parse("text/x-markdown;charset=utf-8");
        String url="/wbc/api/wbill/prefreezeTx";
        String jso = JSONObject.toJSONString("");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(media,jso);
        Request reqBuild= new Request.Builder()
                .url(url)
                .post(body)
                .build();*/
    }

    /**
     * 客户发起预冻结点击提交后
     */
    @RequestMapping("/ydj")
    public void ydj(@org.springframework.web.bind.annotation.RequestBody List list){


        System.out.print("锁货中，请联系仓管人员");
    }
    /**
     * 等待中心端调我接口告诉我锁货成功
     */
    @RequestMapping("/zxd")
    public void zxd(JSONObject json){
        String msg = String.valueOf(json.getString("msg"));

        if(msg.equals("已锁货")){
            //先去本地operator表中将OPERATORTYPE字段状态修改为"预冻结状态"
            BillOperatorEntity billOperatorEntity = new BillOperatorEntity();
            billOperatorEntity.setId("10001");
            billOperatorEntity.setOperatorType(Const.PLEDGE_PRE_FREEZING);
            billService.update(billOperatorEntity);
            //统计多少参数需要提交给大商所进行预冻结
            String name="预冻结的参数";
            //List list=billService.queryList();

            //请求大商所的预冻结接口
            MediaType media = MediaType.parse("text/x-markdown;charset=utf-8");
            String url="/wbc/api/wbill/prefreezeTx";
            String jso = JSONObject.toJSONString("");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(media,jso);
            Request reqBuild= new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
    }

    /**
     * 仓单质押预冻结提交回调接口方法
     */
    @RequestMapping("/preFreezingApi")
    public Result preFreezingApi(Map<String,String> map){
        //接口传回的接受预冻结的结果
        String msg = String.valueOf(map.get("msg"));
        if(msg=="同意预冻结"){
            //先去本地operator表中将OPERATORTYPE字段状态修改为"冻结状态"
            String name="冻结的参数";
            //List list=billService.queryList(name);
            //请求大商所的冻结接口
            MediaType media = MediaType.parse("text/x-markdown;charset=utf-8");
            String url="/wbc/api/wbill/freezeTx";
            String jso = JSONObject.toJSONString("");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(media,jso);
            Request reqBuild= new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }else{
            //大商所返回结果，预冻结失败,告诉中心端结果
            dsxjg("大商所不同意预冻结");
            //先去本地operator表中将OPERATORTYPE字段状态修改为"初始状态"

        }

        return Result.ok().put("msg",msg);
    }
    /**
     * 仓单质押冻结提交回调接口方法
     */
    @RequestMapping("/freezeApi")
    public Result freezeApi(Map<String,String> map){
        //接受仓单质押冻结提交的结果
        String msg = String.valueOf(map.get("msg"));
        if(msg=="同意冻结"){
            //结束
        }else{
            dsxjg("大商所不同意冻结");
            //先去本地operator表中将OPERATORTYPE字段状态修改为"初始状态"
        }
        return Result.ok().put("msg",msg);
    }
    /**
     * 失败后通知大商所的回调接口
     */
    @RequestMapping("/dsxjg")
    public String dsxjg(String msg){
        return msg;
    }
}
