package com.cofco.sys.controller;

import com.cofco.base.utils.*;
import com.cofco.sys.entity.notice.NoticeEntity;
import com.cofco.sys.service.NoticeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


/**
 * 首页 控制台
 * 
 * @author liming
 * @date 2018-05-22 09:41:38
 */
@RestController
@RequestMapping("sys/main")
public class MainController extends BaseController {

	@Autowired
	private NoticeService noticeService;
	/**
	 *JSONArray 转 JSONObject
	 */
	public JSONObject JSONArrayToJSONObject(JSONArray MessageArray) {
		JSONObject  jsonObject = null;
		for(int i=0 ; i < MessageArray.size();i++)
		{
			//获取每一个JsonObject对象
			jsonObject = MessageArray.getJSONObject(i);
		}
		return jsonObject;
	}

	/**
	 * 消息列表
	 */
	@RequestMapping("/list")
	public Result list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<NoticeEntity> list = noticeService.queryList(query);
		int total = noticeService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}
	/**
	 * 查阅首页
	 */
	@ResponseBody
	@RequestMapping(value = "/showNoticeperms/{id}")
	public Result showNoticeperms(@PathVariable("id") String id){
		NoticeEntity notice = noticeService.queryObject(id);
		return Result.ok().put("notice", notice).put("time", DateUtils.format(notice.getReleaseTimee(),"yyyy-MM-dd hh:mm"));
	}

}