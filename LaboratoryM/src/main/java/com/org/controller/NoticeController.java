package com.org.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.org.pojo.Notice;
import com.org.pojo.User;
import com.org.service.NoticeService;
import com.org.service.UserService;
import com.org.util.DateFactory;

@Controller
@RequestMapping("/Notice")
public class NoticeController {

	@Resource
	private NoticeService noticeService;
	@Resource
	private UserService userService;

	// 获取公告信息
	@RequestMapping("/Admin/selectNotice")
	@ResponseBody
	public List selectNotice() {
		return noticeService.selectNotice();
	}

	// 新增公告信息
	@RequestMapping("/Admin/insertNotice")
	@ResponseBody
	public String insertNotice(@RequestParam String description, @RequestParam String typeName,
			HttpServletRequest request) {
		Notice notice = new Notice();
		User administrators = (User) request.getSession().getAttribute("administrators");
		notice.settUser_id_o(administrators.getId());
		notice.setPublishDate(DateFactory.utilDateTosqlDate(new Date()));
		notice.setDescription(description);
		notice.setTypeName(typeName);
		notice.settUser_id_p(administrators.getId());
		noticeService.insertNotice(notice);
		return "success";
	}

	// 批量删除公告
	@RequestMapping("/Admin/deleteNotices")
	@ResponseBody
	public String deleteNotices(@RequestParam("ids[]") int[] ids) {
		noticeService.deleteNotices(ids);
		return "success";
	}

	// 修改公告信息
	@RequestMapping("/Admin/updateNotice")
	@ResponseBody
	public String updateNotice(@RequestParam String Publisher, @RequestParam String description,
			@RequestParam String typeName, @RequestParam long id, HttpServletRequest request) {
		Notice notice = new Notice();
		User administrators = (User) request.getSession().getAttribute("administrators");
		notice.setId(id);
		notice.settUser_id_o(administrators.getId());
		notice.setPublishDate(DateFactory.utilDateTosqlDate(new Date()));
		notice.setDescription(description);
		notice.setTypeName(typeName);
		noticeService.updateNotice(notice);
		return "success";
	}

	// 获取前十条公告信息
	@RequestMapping("/Admin/selectNoticelimit")
	@ResponseBody
	public List selectNoticelimit() {
		return noticeService.selectNoticelimit();
	}

	@RequestMapping("/User/loadAnnouncementPage")
	public String loadAnnouncementPage() {
		return "html/announcement";
	}

	@RequestMapping("/User/loadAnnouncementData")
	@ResponseBody
	public List<Notice> loadAnnouncementData() {
		List<Notice> noticeList = noticeService.selectNoticelimit();
		return noticeList;
	}

	@RequestMapping("/User/loadAllNotice")
	@ResponseBody
	public Map<String, Object> loadAllNotice(@RequestBody Map<String, Object> map) {
		String startSize = map.get("currentPage").toString();
		int start = (Integer.valueOf(startSize) - 1) * 7;
		Map<String, Object> pageMap = new HashMap<String, Object>();
		pageMap.put("start", start);
		pageMap.put("pageSize", 7);
		Map<String, Object> noticeMap = noticeService.selectNoticeByPage(pageMap);
		return noticeMap;
	}
}
