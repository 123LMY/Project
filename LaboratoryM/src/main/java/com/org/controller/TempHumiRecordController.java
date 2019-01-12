package com.org.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.service.TempHumiRecordService;

@Controller
@RequestMapping("TempHumiRecord")
public class TempHumiRecordController {

	@Resource
	private TempHumiRecordService tempHumiRecordService;
	
	@RequestMapping("/Admin/selectTempHumiRecourd")
	@ResponseBody
	public Map selectTempHumiRecourd() {
		Map resultMap = new HashMap();
		resultMap.put("Humidity", tempHumiRecordService.selectHumidity());
		resultMap.put("Temperature", tempHumiRecordService.selectTemperature());
		return resultMap;
	}
}
