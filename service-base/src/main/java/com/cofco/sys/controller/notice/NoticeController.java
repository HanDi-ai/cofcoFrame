package com.cofco.sys.controller.notice;

import com.cofco.base.annotation.SysLog;
import com.cofco.base.page.Page;
import com.cofco.base.utils.DateUtils;
import com.cofco.base.utils.Result;
import com.cofco.base.utils.Utils;
import com.cofco.sys.controller.BaseController;
import com.cofco.sys.entity.notice.NoticeEntity;
import com.cofco.sys.service.NoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 通知
 * 
 * @author cofco
 * @email huangxianyuan@gmail.com
 * @date 2017-08-31 15:59:09
 */
@Controller
@RequestMapping("sys/notice")
public class NoticeController extends BaseController {
	@Autowired
	private NoticeService noticeService;
	
	/**
	 * 我的通知列表
	 */
	@RequestMapping("/myList")
	//@RequiresPermissions("sys:notice:all")
	public String list(Model model, NoticeEntity noticeEntity, HttpServletRequest request){
		int pageNum = Utils.parseInt(request.getParameter("pageNum"), 1);
		Page<NoticeEntity> page = noticeService.findMyNoticePage(noticeEntity, pageNum);
		model.addAttribute("page",page);
		model.addAttribute("notice",noticeEntity);
		return "notice/noticeList.jsp";
	}


	/**
	 * 查阅
	 */
	@RequestMapping("/showNotice")
	//@RequiresPermissions("sys:notice:all")
	@ResponseBody
	@SysLog("查阅通知")
	public Result showNotice(String id){
		NoticeEntity notice = noticeService.queryObject(id);
		return Result.ok().put("notice", notice).put("time", DateUtils.format(notice.getReleaseTimee(),"yyyy-MM-dd hh:mm"));
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("sys:notice:all")
	@SysLog("保存通知")
	public Result save(@RequestBody NoticeEntity notice){
		noticeService.save(notice);

		return Result.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("sys:notice:all")
	@SysLog("修改通知")
	public Result update(@RequestBody NoticeEntity notice){
		noticeService.update(notice);
		
		return Result.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("sys:notice:all")
	@SysLog("删除通知")
	public Result delete(@RequestBody String[] ids){
		noticeService.deleteBatch(ids);
		
		return Result.ok();
	}
	
}
