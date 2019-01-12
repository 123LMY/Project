package com.org.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.service.EntranceGuardRecordService;

@Controller
@RequestMapping("/EntranceGuardRecord")
public class EntranceGuardRecordController {

	@Resource
	private EntranceGuardRecordService entranceGuardRecordService;
	
	//获取门禁记录
	@RequestMapping("/Admin/selectEntranceGuardRecord")
	@ResponseBody
	public List selectEntranceGuardRecord(@RequestParam(required=false)String inTime,@RequestParam(required=false)String outTime) {
		return entranceGuardRecordService.selectEntranceGuardRecord(inTime, outTime);
	}
	
	//删除门禁记录
	@RequestMapping("/Admin/deleteEntranceGuardRecord")
	@ResponseBody
	public String deleteEntranceGuardRecord(@RequestParam("ids[]") int[] ids) {
		entranceGuardRecordService.deleteEntranceGuardRecord(ids);
		return "success";
	}
	
	//系统消息验证是否有30天以上的门禁记录
	@RequestMapping("/Admin/checkRecord")
	@ResponseBody
	public boolean checkRecord() {
		return entranceGuardRecordService.checkRecord();
	}
	
}
