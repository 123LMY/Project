package com.org.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.Device;
import com.org.pojo.DeviceDetail;
import com.org.pojo.User;
import com.org.service.DeviceDetailService;
import com.org.service.DeviceService;
import com.org.service.LabService;
import com.org.util.DateFactory;
import com.org.util.Pager;
import com.org.vo.DeviceDetailInfo;
import com.org.vo.DeviceVo;

@Controller
@RequestMapping("/DeviceDetail")
public class DeviceDetailController {
	@Resource
	private DeviceDetailService deviceDetailService;
	@Resource
	private LabService labService;
	@Resource
	private DeviceService deviceService;

	@RequestMapping(value = "/User/getAllDeviceDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<DeviceDetail> getAllDeviceDetail(@RequestBody Map currentPage) {
		Pager pager = new Pager();
		String currentPager = currentPage.get("currentPage").toString();
		if (currentPager.equals(" ")) {
			currentPager = "1";
		}
		pager.setCurrentPage(Integer.parseInt(currentPager));
		pager.setPageSize(2);
		List<DeviceDetail> deviceDetailList = deviceDetailService.getAllDeviceDetail(pager);
		pager.setTotalResults(deviceDetailList.size());
		if (deviceDetailList != null) {
			return deviceDetailList;
		}
		return null;
	}

	@RequestMapping(value = "/User/getDeviceDetailByKeyWords", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<DeviceDetail> getDeviceDetailByKeyWords(@RequestBody String keyWords) {
		List<DeviceDetail> deviceDetailList = deviceDetailService.getDeviceDetailByKeyWords(keyWords);
		if (deviceDetailList != null) {
			return deviceDetailList;
		}
		return null;
	}

	// 新增子设备
	@RequestMapping("/Admin/insertDeviceDetail")
	@ResponseBody
	public String insertDeviceDetail(@RequestParam String devSn, @RequestParam String rfidNo,
			@RequestParam char devStatus, @RequestParam long tDevice_id, @RequestParam String remark,
			@RequestParam String labName, @RequestParam String cabNo, HttpServletRequest request) {
		if (labService.isLabNameRepeat(labName) == 0) {
			return "fail";
		}
		HttpSession session = request.getSession();
		User administrators = (User) session.getAttribute("administrators");
		//
		DeviceDetail deviceDetail = new DeviceDetail();
		deviceDetail.setDevSn(devSn);
		deviceDetail.setDevStatus(devStatus);
		deviceDetail.setRemark(remark);
		deviceDetail.setRfidNo(rfidNo);
		deviceDetail.settDevice_id(tDevice_id);
		deviceDetail.settUser_id_o(administrators.getId());
		//
		deviceDetail.setCabNo(cabNo);
		deviceDetail.setInDate(DateFactory.utilDateTosqlDate(new Date()));
		return deviceDetailService.insertDeviceDetail(deviceDetail, labName);
	}

	// 获取子设备信息
	@RequestMapping("/Admin/selectDevDetailInfo")
	@ResponseBody
	public List<DeviceDetailInfo> selectDevDetailInfo(@RequestParam long id) {
		return deviceDetailService.selectDevDetailInfo(id);
	}

	// 删除子设备
	@RequestMapping("/Admin/deleteDevDetailById")
	@ResponseBody
	public String deleteDevDetailById(@RequestParam("ids[]") int[] ids) {
		deviceDetailService.deleteDevDetailById(ids);
		return "success";
	}

	// 更新子设备信息
	@RequestMapping("/Admin/updateDevDetail")
	@ResponseBody
	public String updateDevDetail(@RequestParam String devSn, @RequestParam String RFID, @RequestParam char devStatus,
			@RequestParam long id, @RequestParam String labName, @RequestParam String cabNo,
			@RequestParam String remark, HttpServletRequest request) {
		if (labService.isLabNameRepeat(labName) == 0) {
			return "fail";
		}
		HttpSession session = request.getSession();
		User administrators = (User) session.getAttribute("administrators");
		DeviceDetail deviceDetail = new DeviceDetail();
		deviceDetail.setCabNo(cabNo);
		deviceDetail.setDevSn(devSn);
		deviceDetail.setDevStatus(devStatus);
		deviceDetail.setId(id);
		deviceDetail.setRemark(remark);
		deviceDetail.setRfidNo(RFID);
		deviceDetail.settUser_id_o(administrators.getId());
		return deviceDetailService.updateDevDetail(deviceDetail, labName);
	}

	// 修改子设备状态
	@RequestMapping("/Admin/updateDevDetailStatus")
	@ResponseBody
	public String updateDevDetailStatus(@RequestParam("devdetailids[]") int[] ids, @RequestParam char devdetailstatus,
			HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		deviceDetailService.updateDevDetailStatus(ids, devdetailstatus, administrators.getId());
		return "success";
	}

}
