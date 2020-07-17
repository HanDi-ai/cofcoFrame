package com.cofco.sys.aop;

import com.alibaba.fastjson.JSON;
import com.cofco.base.annotation.SysLog;
import com.cofco.base.utils.HttpContextUtils;
import com.cofco.base.utils.IPUtils;
import com.cofco.base.utils.Utils;
import com.cofco.sys.entity.log.SysLogEntity;
import com.cofco.sys.service.SysLogService;
import com.cofco.utils.ShiroUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * 类SysLogAspect的功能描述:
 * 系统日志，切面处理类
 * @auther cofco
 * @date 2017-08-25 16:13:10
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;
	@AfterReturning(returning="rvt",pointcut="@annotation(com.cofco.base.annotation.SysLog)")
	public void saveSysLog(JoinPoint joinPoint,Object rvt) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		JSONArray resultrvt=null;
		resultrvt = JSONArray.fromObject(rvt);
		SysLogEntity sysLog = new SysLogEntity();
		Random rand =new Random();
		//生成logid
		sysLog.setId(rand.nextInt(999999999));
		//保存方法返回值
		sysLog.setResult(resultrvt.toString());
		//取得url 截取判断是系统端还是app端
		String url =  joinPoint.getTarget().toString();
		url=url.substring(0, url.indexOf(".", url.indexOf(".", url.indexOf(".", 0)+1)+1)+1);
		//url=url.substring(0, url.indexOf(".",url.indexOf(".")+1 ));

		//判断是系统端 还是 移动端
		if(url.equals("com.cofco.app.")){
			sysLog.setSign("0");
		}else{
			sysLog.setSign("1");
		}
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//注解上的描述 
			sysLog.setOperation(syslog.value());
		}
		
		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		
		//请求的参数
		Object[] args = joinPoint.getArgs();
		if(args.length>0){
			String params = JSON.toJSONString(args[0]);
			sysLog.setParams(params);
		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));
		
		//用户名
		String username = ShiroUtils.getUserEntity().getUserName();

		sysLog.setUsername(username);
		
		sysLog.setCreateDate(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
	
}
