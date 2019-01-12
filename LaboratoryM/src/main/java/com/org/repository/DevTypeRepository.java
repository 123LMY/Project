package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.org.pojo.DevType;

public interface DevTypeRepository {

	//查询设备类型
	@Select("select * from tdevtype")
	public List<DevType> selectDevType();
}
