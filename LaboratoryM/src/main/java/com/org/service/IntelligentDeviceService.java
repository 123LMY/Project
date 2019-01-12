package com.org.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.org.pojo.IntelligentDevice;
import com.org.repository.IntelligentDeviceRepository;

@Service
public class IntelligentDeviceService {

	@Resource
	private IntelligentDeviceRepository intelligentDeviceRepository;
	
	public List<IntelligentDevice> getAllIntelDevList(){
		return intelligentDeviceRepository.getAllIntelDevList();
	}
	
	public boolean updateIntelDev(String idevName ,char idevStatus,String takeTime,long tUser_id) {
		return intelligentDeviceRepository.updateIntelDev(idevName, idevStatus, takeTime, tUser_id);
	}
	
	public void deleteIntelligentdevice(long id) {
		intelligentDeviceRepository.deleteIntelligentdevice(id);
	}
	
	public void insertIntelligentdevice(IntelligentDevice intelligentdevice) {
		intelligentDeviceRepository.insertIntelligentdevice(intelligentdevice);
	}
}
