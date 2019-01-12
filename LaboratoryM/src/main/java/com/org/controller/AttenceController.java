package com.org.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.service.AttenceService;
import com.org.util.DateFactory;
import com.org.vo.AttenceInfo;

@Controller
@RequestMapping("/Attence")
public class AttenceController {

	@Resource
	private AttenceService attenceService;
	
	//获取所有考勤信息
	@RequestMapping("/Admin/selectAllAttence")
	@ResponseBody
	public List selectAllAttence() {
		return attenceService.selectAllAttence();
	}
	
	//根据时间获取考勤信息
	@RequestMapping("/Admin/selectAttence")
	@ResponseBody
	public List selectAttence(@RequestParam(required = false) String signinTime,@RequestParam(required = false) String signoutTime,@RequestParam(required = false) String realName) throws ParseException {
		return attenceService.selectAttence(signinTime, signoutTime, realName);
	}
}
