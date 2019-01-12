package com.org.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Select;

import com.org.pojo.Department;

public interface DepartmentRepository {
	@Select("select * from tdepartment where deptNo = #{deptNo}")
	@Results(value= {
			@Result(column="id",property="professionList",many = @Many(select="com.org.repository.ProfessionRepository.selectProfessionByDepartmentId"))
	})
	public Department selectDepartmentByDeptNo(@Param("deptNo")String deptNo);
}
