package com.org.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.Device;
import com.org.repository.DeviceApplyRepository;
import com.org.repository.DeviceDetailRepository;
import com.org.repository.DeviceRepository;
import com.org.repository.UserRepository;
import com.org.vo.DeviceInfo;

@Service
public class DeviceService {

	@Resource
	private DeviceRepository deviceRepository;
	@Resource
	private DeviceDetailRepository deviceDetailRepository;
	@Resource
	private DeviceApplyRepository deviceApplyRepository;
	@Resource
	private UserRepository userRepository;
	
	//按类型查找设备
	public List selectDeviceUseInfo(long devType){
		List<DeviceInfo> dList = deviceRepository.selectDeviceUseInfo(devType);
		return dList;
	}
	
	//根据时间查询设备
	public List<DeviceInfo> selectDeviceUseInfoByDateTime(long devType,String startDate,String endDate){
		return deviceRepository.selectDeviceUseInfoByDateTime(startDate, endDate, devType);
	}
	
	//设备信息查询
	public List<DeviceInfo> selectDeviceResource(String deviceName){
		return deviceRepository.selectDeviceResource(deviceName);
	}
	
	//删除设备
	@Transactional
	public void deleteDevice(int[] ids) {
		deviceApplyRepository.deleteDevApply(ids);
		deviceDetailRepository.deleteDeviceDetail(ids);
		deviceRepository.deleteDevice(ids);
	}
	
	//新增设备
	@Transactional
	public String insertDevice(Device device,String realName) {
		if(deviceRepository.isDevRepeat(device.getDevName(),device.getDevModel())>0) {
			return "fail";
		}
		if(userRepository.checkUserByRealName(realName)==0) {
			return "fail";
		}
		device.settUser_id_k(userRepository.selectUserByRealName(realName));
		deviceRepository.insertDevice(device);
		return "success";
	}
	
	//根据id查找设备
	public Device selectDevById(long id) {
		return deviceRepository.selectDevById(id);
	}

	//更新设备信息
	@Transactional
	public String updateDevice(Device device,String realName) {
		if(deviceRepository.isDevRepeat(device.getDevName(),device.getDevModel())>0) {
			if(deviceRepository.isDevRepeat2(device.getDevName(),device.getDevModel(), device.getId())==0) {
				return "fail";
			}
		}
		if(userRepository.checkUserByRealName(realName)==0) {
			return "fail";
		}
		device.settUser_id_k(userRepository.selectUserByRealName(realName));
		deviceRepository.updateDevice(device);
		return "success";
	}
	
	public List<Device> selectAllDevice(){
		return deviceRepository.selectAllDevice();
	}
	
	public List<Device> selectDeviceByKeyWords(String keyWords){
		return deviceRepository.selectDeviceByKeyWords(keyWords);
	}
	
	
}
