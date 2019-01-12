package com.org.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.repository.EntranceGuardRecordRepository;
import com.org.repository.ProjectRepository;
import com.org.vo.EntranceGuardRecordInfo;

@Service
public class EntranceGuardRecordService {

	@Resource
	private EntranceGuardRecordRepository entranceGuardRecordRepository;
	@Resource
	private ProjectRepository projectRepository;
	
	//获取门禁记录
	public List<EntranceGuardRecordInfo> selectEntranceGuardRecord(String inTime,String outTime){
		List<EntranceGuardRecordInfo> list = entranceGuardRecordRepository.selectEntranceGuardRecord(inTime, outTime);
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
	
	//删除门禁记录
	@Transactional
	public void deleteEntranceGuardRecord(int[] ids) {
		entranceGuardRecordRepository.deleteEntranceGuardRecord(ids);
	}
	
	//系统消息超过30天的门禁记录提示
	public boolean checkRecord() {
		if(entranceGuardRecordRepository.checkRecord()!=0) {
			return true;
		}
		return false;
	}
}
