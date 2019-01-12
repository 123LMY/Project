package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.org.pojo.Profession;

public interface ProfessionRepository {
	@Select("select * from tprofession where tDepartment_id = #{departmentId}")
	public List<Profession> selectProfessionByDepartmentId(@Param("departmentId")long departmentId);
}
