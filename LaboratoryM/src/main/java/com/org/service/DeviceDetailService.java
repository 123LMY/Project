package com.org.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.DeviceDetail;
import com.org.repository.DeviceApplyRepository;
import com.org.repository.DeviceDetailRepository;
import com.org.repository.LabRepository;
import com.org.util.Pager;
import com.org.vo.DeviceDetailInfo;

@Service
public class DeviceDetailService {
	@Resource
	private DeviceDetailRepository deviceDetailRepository;
	@Resource
	private DeviceApplyRepository deviceApplyRepository;
	@Resource
	private LabRepository labRepository;
	
	//获取所有的子设备信息
	public List<DeviceDetail> getAllDeviceDetail(Pager pager){
		return deviceDetailRepository.getAllDeviceDetail(pager);
	}
	
	//根据关键字查询子设备
	public List<DeviceDetail> getDeviceDetailByKeyWords(String keyWords){
		return deviceDetailRepository.getDeviceDetailByKeyWords(keyWords);
	}
	
	//新增子设备
	@Transactional
	public String insertDeviceDetail(DeviceDetail deviceDetail,String labName) {
		if(deviceDetailRepository.isDevDetailRepeat(deviceDetail.getDevSn())>0) {
			return "fail";
		}
		if(labRepository.isLabNameRepeat(labName)==0) {
			return "fail";
		}
		deviceDetail.settLab_id(labRepository.selectLabBylabName(labName));
		deviceDetailRepository.insertDeviceDetail(deviceDetail);
		return "success";
	}
	
	//查找子设备信息
	public List<DeviceDetailInfo> selectDevDetailInfo(long id){
		List<DeviceDetailInfo> ddl = deviceDetailRepository.selectDevDetailInfo(id);
		for(int i=0;i<ddl.size();i++) {
			if(ddl.get(i).getDevStatus()=='0') {
				ddl.get(i).setDevStatusName("空闲");
			} else if(ddl.get(i).getDevStatus()=='1') {
				ddl.get(i).setDevStatusName("在用");
			} else if(ddl.get(i).getDevStatus()=='2') {
				ddl.get(i).setDevStatusName("维修");
			} else if(ddl.get(i).getDevStatus()=='3') {
				ddl.get(i).setDevStatusName("损坏");
			} else if(ddl.get(i).getDevStatus()=='4') {
				ddl.get(i).setDevStatusName("报废");
			} else if(ddl.get(i).getDevStatus()=='5') {
				ddl.get(i).setDevStatusName("已预约");
			}
		}
		return ddl;
	}
	
	//删除子设备
	@Transactional
	public void deleteDevDetailById(@Param("ids")int[] ids) {
		deviceApplyRepository.deleteDevApplyBytDeviceDetail_id(ids);
		deviceDetailRepository.deleteDevDetailById(ids);
	}
	
	//修改子设备信息
	@Transactional
	public String updateDevDetail(DeviceDetail deviceDetail,String labName) {
		if(deviceDetailRepository.isDevDetailRepeat(deviceDetail.getDevSn())>0) {
			if(deviceDetailRepository.isDevDetailRepeat2(deviceDetail.getDevSn(), deviceDetail.getId())==0) {
				return "fail";
			}
		}
		if(labRepository.isLabNameRepeat(labName)==0) {
			return "fail";
		}
		deviceDetail.settLab_id(labRepository.selectLabBylabName(labName));
		deviceDetailRepository.updateDevDetail(deviceDetail);
		return "success";
	}
	
	//修改子设备状态
	@Transactional
	public void updateDevDetailStatus(int[] ids,char devdetailstatus,long tUser_id_o) {
		deviceDetailRepository.updateDevDetailStatus(ids, devdetailstatus,tUser_id_o);
	}

}
