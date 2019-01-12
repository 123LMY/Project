package com.org.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.org.pojo.LabApply;
import com.org.pojo.User;
import com.org.service.LabApplyService;
import com.org.service.UserService;
import com.org.vo.LabUsingInfo;
import com.org.vo.LabVo;

@Controller
@RequestMapping("/LabApply")
public class LabApplyController {

	@Resource
	private LabApplyService labApplyService;
	@Resource
	private UserService userService;
	
	//实验室使用信息获取
	@RequestMapping("/Admin/selectLabUsingInfo")
	@ResponseBody
	public List<LabUsingInfo> selectLabUsingInfo(@RequestParam char auditStatus,@RequestParam String name){
		return labApplyService.selectLabUsingInfo(auditStatus,name);
	}
	
	//通过实验室申请
	@RequestMapping("/Admin/updatePassLabApply")
	@ResponseBody
	public String updatePassLabApply(@RequestParam("labapplyids[]") int[] labapplyids,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		labApplyService.updatePassLabApplyStatus(labapplyids,administrators.getId());
		return "success";
	}
	
	//拒绝实验室申请
	@RequestMapping("/Admin/updateRefuseLabApply")
	@ResponseBody
	public String updateRefuseLabApply(@RequestParam("labapplyids[]") int[] labapplyids,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		labApplyService.updateRefuseLabApplyStatus(labapplyids,administrators.getId());
		return "success";
	}
	
	//撤销实验室状态修改
	@RequestMapping("/Admin/cancelLabApplyStatus")
	@ResponseBody
	public String cancelLabApplyStatus(@RequestParam("labapplyids[]") int[] labapplyids,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		labApplyService.cancelLabApplyStatus(labapplyids,administrators.getId());
		return "success";
	}
	
	//查找预约实验室申请
	@RequestMapping("/Admin/checkLabApply")
	@ResponseBody
	public boolean checkLabApply() {
		return labApplyService.checkLabApply();
	}
	
	/**
	 * 根据起始时间查询实验室使用情况
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/User/serachLabByTime")
	@ResponseBody
	public Map<String,Object> serachLabByTime(@RequestBody Map map) throws ParseException{
		
		Map<String,Object> labMap = new HashMap<String,Object>();
		 DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");  
		 Timestamp  startTime = new Timestamp( sdf.parse(map.get("startTime").toString()).getTime());
		 Timestamp  endTime = new Timestamp(sdf.parse(map.get("endTime").toString()).getTime());
		List<LabApply> labApplyList = labApplyService.selectLabApplyByTime(startTime, endTime);
		List<LabVo> labVoList = new ArrayList<LabVo>();
		for(LabApply labApply : labApplyList){
			int count = labApplyService.selectLabUserNumByLabId(labApply.getLab().getId());
			LabVo labVo = new LabVo();
			labVo.setLabName(labApply.getLab().getLabName());
			labVo.setLabStatus(labApply.getLab().getLabStatus());
			labVo.setUserCount(count);
			labVo.setLabId(labApply.getLab().getId());
			labVoList.add(labVo);
		
		}
	
		labMap.put("rows", labVoList);
		labMap.put("total",labApplyList.size());
		return labMap;
	};
	
	/**
	 * 新增实验室使用情况记录
	 * @param map
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/User/insertLabApply")
	@ResponseBody
	public String insertLabApply(@RequestBody Map map,HttpServletRequest request) throws ParseException{
		User user = userService.getUserByRealName(map.get("appointor").toString());
		User loginUser = (User)request.getSession().getAttribute("user");
		if(user == null){
			return "none";
		}else{	 
		    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");  
		    Date startDate = sdf.parse(map.get("startTime").toString());		
			Date endDate = sdf.parse(map.get("endTime").toString());
				Timestamp  startTime = new Timestamp(startDate.getTime());
				Timestamp  endTime = new Timestamp(endDate.getTime());
				LabApply labApply = new LabApply();
				labApply.setStartTime(startTime);
				labApply.setEndTime(endTime);
				labApply.settLab_id(Integer.valueOf(map.get("labId").toString()));
				labApply.settUser_id_a(user.getId());
				labApply.settUser_id_b(loginUser.getId());
				labApply.setPurpose(map.get("purpose").toString());
				labApply.settUser_id_o(loginUser.getId());
				labApply.setAuditStatus('0');
				int i = labApplyService.insertLabApply(labApply);
				if(i>0) return "success";
				return "fail";
			
		}	
	}
	
	/**
	 * 加载实验室预约记录界面
	 * @return
	 */
	@RequestMapping("/User/loadLabReservationRecordPage")
	public String loadLabReservationRecordPage(){
		return "html/labReservationRecord";
	}
	/**
	 * 加载实验室预约记录页面数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/User/loadLabReservationRecordData")
	@ResponseBody
	public List<LabVo> loadLabReservationRecordData(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		List<LabApply> labApplyList = labApplyService.selectLabApplyByUserId(user.getId());
		List<LabVo> labVoList = new ArrayList<LabVo>();
		for(LabApply labApply:labApplyList){
			LabVo labVo = new LabVo();
			labVo.setLabApplyId(labApply.getId());
			labVo.setLabName(labApply.getLab().getLabName());
			labVo.setStartTime(labApply.getStartTime());
			labVo.setEndTime(labApply.getEndTime());
			labVo.setAuditStatus(labApply.getAuditStatus());
			labVoList.add(labVo);
		}
		return labVoList;
	}
	/**
	 *根据起始时间查询实验室使用记录
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/User/searchLabApplyByTime")
	@ResponseBody
	public List<LabVo> searchLabApplyByTime(@RequestBody Map map,HttpServletRequest request){
		List<LabVo> labVoList = new ArrayList<LabVo>();
		List<LabVo> allLabVo = loadLabReservationRecordData(request);
		String startTime = map.get("startTime").toString();
		String endTime = map.get("endTime").toString();
		Timestamp st =Timestamp.valueOf(startTime);
		Timestamp et =Timestamp.valueOf(endTime);
	 
		for(LabVo lv:allLabVo){
			if(st.after(lv.getStartTime()) && et.before(lv.getEndTime())){
				labVoList.add(lv);
			}
		}
	 
		return labVoList;
	}
	
	/**
	 *  根据id删除实验室使用情况
	 * @param map
	 * @return
	 */
	@RequestMapping("/User/deleteLabApplyById")
	@ResponseBody
	public String deleteLabApplyById(@RequestBody Map map){
		Integer id = Integer.valueOf(map.get("labApplyId").toString());
		labApplyService.deleteLabApplyById(id);
		return "success";
	}
	
	/**
	 * 
	 */
	@RequestMapping("/User/checkApplyByUserId")
	@ResponseBody
	public Map<String,Object> checkLabApplyByUserId(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		User user = (User)request.getSession().getAttribute("user");
		map =  labApplyService.checkLabApplyByUserId(user.getId()); 
		return map;
	}
	
}
