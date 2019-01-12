package com.org.service;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.org.pojo.Department;
import com.org.repository.DepartmentRepository;
@Service
public class DeparmentService {
	@Resource
	private DepartmentRepository departmentRepository;
	
	public Department selectDepartmentByDeptNo(String deptNo) {
		return departmentRepository.selectDepartmentByDeptNo(deptNo);
	}
}
