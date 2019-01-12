package com.org.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.DeviceApply;
import com.org.pojo.User;
import com.org.service.DeviceApplyService;
import com.org.service.UserService;
import com.org.vo.DeviceApplyInfo;
import com.org.vo.DeviceVo;

@Controller
@RequestMapping("/DeviceApply")
public class DeviceApplyController {

	@Resource
	private DeviceApplyService deviceApplyService;
	@Resource
	private UserService userService;

	/* 新增deviceApply */
	@RequestMapping("/User/insertDeviceApply")
	@ResponseBody
	public Map<String, Object> insertDeviceApplys(@RequestBody Map dataMap, HttpServletRequest request)
			throws ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Map<String, Object> map = new HashMap<String, Object>();
		String userName = dataMap.get("user").toString();
		User user = userService.getUserByRealName(userName);
		User loginUser = (User) request.getSession().getAttribute("user");
		if (user == null) {
			map.put("status", "noneUser");
			return map;
		} else {
			String startTime = dataMap.get("startTime").toString();
			String endTime = dataMap.get("endTime").toString();
			String purpose = dataMap.get("purpose").toString();
			java.sql.Date date = java.sql.Date.valueOf(startTime);
			String deviceDetailId = dataMap.get("devDetailId").toString();
			DeviceApply deviceApply = new DeviceApply();

			deviceApply.setStartDate(java.sql.Date.valueOf(startTime));
			deviceApply.setEndDate(java.sql.Date.valueOf(endTime));
			deviceApply.settUser_id_a(user.getId());
			deviceApply.settUser_id_b(loginUser.getId());
			deviceApply.settUser_id_o(loginUser.getId());
			deviceApply.settDeviceDetail_id(Integer.valueOf(deviceDetailId));
			deviceApply.setPurpose(purpose);
			deviceApply.setAuditStatus('0');
			int i = deviceApplyService.insertDeviceApplys(deviceApply);
			if (i > 0) {
				map.put("status", "success");
				return map;
			}
			map.put("status", "fail");
			return map;
		}

	}

	/* 根据预约人的id查询该人的所有预约单 */
	@RequestMapping("/User/selectDeviceApplyByUserId")
	@ResponseBody
	public List<DeviceApply> selectDeviceApplyByUserId(@RequestBody Map idMap) {
		Integer userId = Integer.valueOf(idMap.get("tUser_id_b").toString());
		List<DeviceApply> deviceApplyList = deviceApplyService.selectDeviceApplyByUserId(userId);

		return deviceApplyList;
	}

	/**
	 * 加载设备预约记录界面
	 * 
	 * @return
	 */
	@RequestMapping("/User/loadDevReservationRecordPage")
	public String loadDevReservationRecordPage() {
		return "html/devReservationRecord";
	}

	/**
	 * 加载设备预约记录界面数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/User/loadDevReservationRecordData")
	@ResponseBody
	public List<DeviceVo> loadDevReservationRecordData(HttpServletRequest request) {
		List<DeviceVo> dvList = new ArrayList<DeviceVo>();
		User user = (User) request.getSession().getAttribute("user");
		List<DeviceApply> deviceApplyList = deviceApplyService.selectDeviceApplyByUserId(user.getId());
		if (deviceApplyList != null) {
			for (int i = 0; i < deviceApplyList.size(); i++) {
				DeviceVo dv = new DeviceVo();
				dv.setDevName(deviceApplyList.get(i).getDeviceDetail().getDevice().getDevName());
				dv.setDevSn(deviceApplyList.get(i).getDeviceDetail().getDevSn());
				dv.setStartTime(deviceApplyList.get(i).getStartDate());
				dv.setEndTime(deviceApplyList.get(i).getEndDate());
				dv.setAuditStatus(deviceApplyList.get(i).getAuditStatus());
				dv.setDevApplyId(deviceApplyList.get(i).getId());
				dvList.add(dv);
			}
		}

		return dvList;
	}

	/**
	 * 根据id删除deviceApplyId
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/User/deleteDeviceApplyById")
	@ResponseBody
	public String deleteDeviceApplyById(@RequestBody Map map) {
		Integer id = Integer.valueOf(map.get("devApplyId").toString());
		int i = deviceApplyService.deleteDeviceApplyById(id);
		if (i > 0) {
			return "success";
		} else {
			return "fail";
		}

	}

	@RequestMapping("/User/searchDevApplyByTime")
	@ResponseBody
	public List<DeviceVo> searchDevApplyByTime(@RequestBody Map map, HttpServletRequest request) throws ParseException {
		List<DeviceVo> dvList = new ArrayList<DeviceVo>();
		String startTime = map.get("startTime").toString();
		String endTime = map.get("endTime").toString();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<DeviceVo> allDvList = loadDevReservationRecordData(request);
		for (DeviceVo dv : allDvList) {
			if (startTime.compareTo(sdf.format(dv.getStartTime())) > 0
					&& endTime.compareTo(sdf.format(dv.getEndTime())) < 0) {

				dvList.add(dv);
			}
		}
		return dvList;
	}

	// 获取设备申请记录
	@RequestMapping("/Admin/selectDevApplyInfo")
	@ResponseBody
	public List<DeviceApplyInfo> selectDevApplyInfo(@RequestParam char auditStatus, @RequestParam String name) {
		return deviceApplyService.selectDevApplyInfo(auditStatus, name);
	}

	// 修改设备申请状态
	@RequestMapping("/Admin/updateDevApplyStatus")
	@ResponseBody
	public String updateDevApplyStatus(@RequestParam("devapplyids[]") int[] ids, @RequestParam char devapplystatus,
			HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		deviceApplyService.updateDevApplyStatus(ids, devapplystatus, administrators.getId());
		return "success";
	}

	// 系统消息查询是否有要处理的设备申请
	@RequestMapping("/Admin/checkDevApply")
	@ResponseBody
	public boolean checkDevApply() {
		return deviceApplyService.checkDevApply();
	}

	// 根据deviceDetailId 和userId删除deviceapply
	@RequestMapping("/User/deleteByDeviceDetailIdAndUserId")
	@ResponseBody
	public String deleteByDeviceDetailIdAndUserId(@RequestBody Map<String, Object> map, HttpServletRequest request) {
		int detailId = Integer.valueOf(map.get("devDetailId").toString());
		User user = (User) request.getSession().getAttribute("user");
		int i = deviceApplyService.deleteByUserIdAndDeviceDetailId(detailId, user.getId());
		if (i > 0) {
			return "success";
		} else {
			return "fail";
		}

	}
}
