package com.org.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.org.pojo.DevType;
import com.org.repository.DevTypeRepository;

@Service
public class DevTypeService {

	@Resource
	private DevTypeRepository devTypeRepository;
	
	//获取设备类型
	public List<DevType> selectDevType(){
		return devTypeRepository.selectDevType();
	}
}
