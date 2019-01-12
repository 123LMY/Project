package com.org.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.service.DevTypeService;

@Controller
@RequestMapping("/DevType")
public class DevTypeController {

	@Resource
	private DevTypeService devTypeService;
	
	//获取设备类型
	@RequestMapping("/Admin/selectDevType")
	@ResponseBody
	public List selectDevType() {
		return devTypeService.selectDevType();
	}
}
