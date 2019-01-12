package com.org.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.Device;
import com.org.pojo.User;
import com.org.service.DeviceService;
import com.org.service.UserService;
import com.org.vo.DeviceInfo;
import com.org.vo.DeviceVo;

@Controller
@RequestMapping("/Device")
public class DeviceController {

	@Resource
	private DeviceService deviceService;
	@Resource
	private UserService userService;
	
	//设备使用情况统计 按类型查找
	@RequestMapping("/Admin/selectDeviceUseInfo")
	@ResponseBody
	public List selectDeviceUseInfo(@RequestParam long devType){
		return deviceService.selectDeviceUseInfo(devType);
	}
	
	//根系日期统计设备使用情况
	@RequestMapping("/Admin/selectDeviceUseInfoByDate")
	@ResponseBody
	public List selectDeviceUseInfoByDate(@RequestParam long devType, @RequestParam(required=false) String startDate, @RequestParam(required=false) String endDate) {
		return deviceService.selectDeviceUseInfoByDateTime(devType, startDate, endDate);
	}
	
	//设备信息查询
	@RequestMapping("/Admin/selectDeviceResource")
	@ResponseBody
	public List selectDeviceResource(@RequestParam String deviceName) {
		return deviceService.selectDeviceResource(deviceName);
	}
	
	//删除设备
	@RequestMapping("/Admin/deleteDevice")
	@ResponseBody
	public String deleteDevice(@RequestParam("ids[]") int[] ids) {
		deviceService.deleteDevice(ids);
		return "success";
	}
	
	//新增设备
	@RequestMapping("/Admin/insertDevice")
	@ResponseBody
	public String insertDevice(@RequestParam String devName,@RequestParam String devModel,@RequestParam String vender,@RequestParam String custodian,@RequestParam String remark,@RequestParam long tDevType_id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		User administrators = (User) session.getAttribute("administrators");
		Device device = new Device();
		device.setDevName(devName);
		device.setDevModel(devModel);
		device.settDevType_id(tDevType_id);
		device.setRemark(remark);
		device.setVender(vender);
		device.settUser_id_o(administrators.getId());
		return deviceService.insertDevice(device,custodian);
	}
	
	//根据id查找设备
	@RequestMapping("/Admin/selectDevById")
	@ResponseBody
	public Device selectDevById(@RequestParam long id) {
		return deviceService.selectDevById(id);
	}
	
	//修改设备信息
	@RequestMapping("/Admin/updateDevice")
	@ResponseBody
	public String updateDevice(@RequestParam String devModel,@RequestParam String devName,@RequestParam long tDevType_id,@RequestParam String vender,@RequestParam String custodian,@RequestParam String remark,@RequestParam long id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		User administrators = (User) session.getAttribute("administrators");
		Device device = new Device();
		device.setDevModel(devModel);
		device.setDevName(devName);
		device.setId(id);
		device.setRemark(remark);
		device.settDevType_id(tDevType_id);
		device.settUser_id_o(administrators.getId());
		device.setVender(vender);
		return deviceService.updateDevice(device,custodian);
	}
	
	
	
	@RequestMapping("/User/loadDeviceReservationPage")
	public String loadDeviceReservationPage(){
		return "html/deviceReservation";
	}
	
	@RequestMapping("/User/loadDeviceReservationData")
	@ResponseBody
	public List<DeviceVo> loadDeviceReservationData(){
		List<DeviceVo> dvList = new ArrayList<DeviceVo>();
		List<Device> deviceList = deviceService.selectAllDevice();
		for(int i=0;i<deviceList.size();i++){
			for(int j = 0;j<deviceList.get(i).getDeviceDetail().size();j++) {
				DeviceVo dv = new DeviceVo();
				dv.setId(deviceList.get(i).getDeviceDetail().get(j).getId());
				dv.setDevSn(deviceList.get(i).getDeviceDetail().get(j).getDevSn());
				dv.setDevStatus(deviceList.get(i).getDeviceDetail().get(j).getDevStatus());
				dv.setDevName(deviceList.get(i).getDevName());
				dv.setDevModel(deviceList.get(i).getDevModel());
				dv.setDevApplyId(deviceList.get(i).getId());
				dvList.add(dv);	
			}
			
		}
		return dvList;
	}
	
	@RequestMapping("/User/selectDeviceByKeyWords")
	@ResponseBody
	public List<DeviceVo> selectDeviceByKeyWords(@RequestBody Map map){
		List<DeviceVo> dvList = new ArrayList<DeviceVo>();
		List<Device> deviceList = deviceService.selectDeviceByKeyWords(map.get("keyWords").toString());
		for(int i=0;i<deviceList.size();i++){
			for(int j = 0;j<deviceList.get(i).getDeviceDetail().size();j++) {
				DeviceVo dv = new DeviceVo();
				dv.setId(deviceList.get(i).getId());
				dv.setDevSn(deviceList.get(i).getDeviceDetail().get(j).getDevSn());
				dv.setDevStatus(deviceList.get(i).getDeviceDetail().get(j).getDevStatus());
				dv.setDevName(deviceList.get(i).getDevName());
				dv.setDevModel(deviceList.get(i).getDevModel());
				dvList.add(dv);	
			}
			
		}
		return dvList;
	}
	
}
