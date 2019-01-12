package com.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.Lab;
import com.org.pojo.User;
import com.org.service.LabApplyService;
import com.org.service.LabService;
import com.org.vo.LabVo;

@Controller
@RequestMapping("/Lab")
public class LabController {

	@Resource
	private LabService labService;
	@Resource
	private LabApplyService labApplyService;
	
	//实验室使用情况统计-日常管理
	@RequestMapping("/Admin/selectLabUseInfo")
	@ResponseBody
	public List selectLabUseInfo(@RequestParam(required=false)String startTime,@RequestParam(required=false)String endTime) {
		return labService.selectLabUseInfo(startTime, endTime);
	}
	
	//实验室信息查询-资源信息管理
	@RequestMapping("/Admin/selectLabInfo")
	@ResponseBody
	public List selectLabInfo(@RequestParam(required=false)String labName) {
		return labService.selectLabInfo(labName);
	}
	
	//删除实验室
	@RequestMapping("/Admin/deleteLab")
	@ResponseBody
	public String deleteLab(@RequestParam("ids[]") int[] ids) {
		labService.deleteLab(ids);
		return "success";
	}
	
	//修改实验室信息
	@RequestMapping("/Admin/updateLab")
	@ResponseBody
	public String updateLab(@RequestParam String labName,@RequestParam String labSite,
			@RequestParam String labFunction,@RequestParam String administrators,
			@RequestParam long id,@RequestParam char labStatus,@RequestParam String remark,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		return labService.updateLab(labName, labSite, labFunction, administrators, id, labStatus, ((User)session.getAttribute("administrators")).getId(),remark);
	}
	
	//新增实验室
	@RequestMapping("/Admin/insertLab")
	@ResponseBody
	public String insertLab(@RequestParam String labName,@RequestParam String labSite,
			@RequestParam String labFunction,@RequestParam String administrators,
			@RequestParam char labStatus,@RequestParam String remark,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		return labService.insertLab(labName, labSite, labFunction, administrators, labStatus, ((User)session.getAttribute("administrators")).getId(),remark);
	}
	
	@RequestMapping("/User/loadLabtoryReservationPage")
	public String loadLabtoryReservationPage(){
		return "html/laboratoryReservation";
	}
	
	
	@RequestMapping("/User/loadLabtoryReservationPageData")
	@ResponseBody
	public Map<String,Object> loadLabtoryReservationPageData(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Lab> labList = labService.getAllLab();
		List<LabVo> labVoList = new ArrayList<LabVo>();
		for(Lab lab:labList){
			LabVo labVo = new LabVo();
			labVo.setLabId(lab.getId());
			labVo.setLabName(lab.getLabName());
			labVo.setLabStatus(lab.getLabStatus());
			labVo.setUserCount(labApplyService.selectLabUserNumByLabId((lab.getId())));
			labVoList.add(labVo);
		}
		int count = labService.getCount();
		map.put("rows", labVoList);
		map.put("total",count);
		return map;
	}
	
}
