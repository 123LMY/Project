package com.org.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.org.repository.AttenceRepository;
import com.org.repository.ProjectRepository;
import com.org.vo.AttenceInfo;

@Service
public class AttenceService {

	@Resource
	private AttenceRepository attenceRepository;
	@Resource
	private ProjectRepository projectRepository;

	// 所有考勤记录
	public List<AttenceInfo> selectAllAttence(){
		List<AttenceInfo> list = attenceRepository.selectAllAttence();
		List proNameList = new ArrayList();
		for(int i=0;i<list.size();i++) {
			if(list.get(i).gettUser_id()!=0) {
				proNameList = projectRepository.selectProNameByUserId(list.get(i).gettUser_id());
				StringBuffer sb = new StringBuffer();
				if(proNameList.size()==0) {
					sb.append("暂无");
					list.get(i).setProName(sb.toString());
					continue;
				}
				for(int j=0;j<proNameList.size();j++) {
					sb.append(proNameList.get(j));
					if(j<proNameList.size()-1) {
						sb.append(",");
					}
				}
				list.get(i).setProName(sb.toString());
			}
		}
		return list;
	}

	// 考勤记录
	public List<AttenceInfo> selectAttence(String signinTime, String signoutTime, String realName) {
		if (signinTime == null && signoutTime == null && realName == null) {
			return selectAllAttence();
		}
		List<AttenceInfo> list = attenceRepository.selectAttence(signinTime, signoutTime, realName);
		List proNameList = new ArrayList();
		for(int i=0;i<list.size();i++) {
			if(String.valueOf(list.get(i).gettUser_id())!=null||String.valueOf(list.get(i).gettUser_id()).equals("")) {
				proNameList = projectRepository.selectProNameByUserId(list.get(i).gettUser_id());
				StringBuffer sb = new StringBuffer();
				for(int j=0;j<proNameList.size();j++) {
					sb.append(proNameList.get(j));
					if(j<proNameList.size()-1) {
						sb.append(",");
					}
				}
				list.get(i).setProName(sb.toString());
			}
		}
		return list;
	}
}
