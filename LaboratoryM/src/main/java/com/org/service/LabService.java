package com.org.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.Lab;
import com.org.pojo.User;
import com.org.pojo.UserGrant;
import com.org.repository.LabRepository;
import com.org.repository.UserGrantRepository;
import com.org.repository.UserRepository;
import com.org.vo.LabUseInfo;

@Service
public class LabService {

	@Resource
	private LabRepository labRepository;
	@Resource
	private UserRepository userRepository;
	@Resource
	private UserGrantRepository userGrantRepository;
	
	//实验室使用信息
	public List<LabUseInfo> selectLabUseInfo(String startTime,String endTime){
		return labRepository.selectLabUseInfo(startTime, endTime);
	}
	
	//实验室信息查询
	public List<Map> selectLabInfo(String labName){
		List<Lab> labList = labRepository.selectLabInfo(labName);
		List<Map> resultList = new ArrayList<Map>();
		Map map = null;
		for(int i=0;i<labList.size();i++) {
			map  = new HashMap();
			map.put("id", labList.get(i).getId());
			map.put("labName", labList.get(i).getLabName());
			map.put("labSite", labList.get(i).getLabSite());
			map.put("labFunction", labList.get(i).getLabFunction());
			map.put("administrators", labList.get(i).getAdministrators().getRealName());
			switch(labList.get(i).getLabStatus()) {
				case '0':
					map.put("labStatus", "空闲");
					break;
				case '1':
					map.put("labStatus", "在用");
					break;
				case '2':
					map.put("labStatus", "维修");
					break;
			}
			map.put("remark", labList.get(i).getRemark());
			resultList.add(map);
		}
		
		return resultList;
	}
	
	//删除实验室
	@Transactional
	public void deleteLab(int[] ids) {
		userGrantRepository.deleteGrantByLabId(ids);
		labRepository.deleteLab(ids);
	}
	
	//修改实验室信息
	@Transactional
	public String updateLab(String labName,String labSite,String labFunction,String administrators,long id,char labStatus,long tUser_id_o,String remark) {
		if(labRepository.isLabNameRepeat(labName)>0) {
			if(labRepository.isLabNameRepeat2(labName, id)==0) {
				return "fail";
			}
		}
		if(userRepository.checkUserByRealName(administrators)==0) {
			return "fail";
		}
		labRepository.updateLab(labName, labSite, labFunction, administrators, id, labStatus, tUser_id_o,remark);
		return "success";
	};
	
	//新增实验室
	@Transactional
	public String insertLab(String labName,String labSite,String labFunction,String administrators,char labStatus,long tUser_id_o,String remark) {
		if(labRepository.isLabNameRepeat(labName)>0) {
			return "fail";
		}
		if(userRepository.checkUserByRealName(administrators)==0) {
			return "fail";
		}
		Lab lab = new Lab();
		List<User> userList = userRepository.selectUserIdAndGrants();
		List<UserGrant> userGrantList = new ArrayList<UserGrant>();
		lab.setLabName(labName);
		lab.setLabSite(labSite);
		lab.setLabFunction(labFunction);
		lab.setLabStatus(labStatus);
		lab.settUser_id_o(tUser_id_o);
		lab.settUser_id_k(userRepository.selectUserByRealName(administrators));
		lab.setRemark(remark);
		labRepository.insertLab(lab);
		for(int i=0;i<userList.size();i++) {
			UserGrant ug = new UserGrant();
			ug.settLab_id(lab.getId());
			ug.settUser_id_a(userList.get(i).getId());
			if(userList.get(i).getGrants()=='0') {
				ug.setNoteTopic('0');
			}else if(userList.get(i).getGrants()=='1') {
				ug.setNoteTopic('1');
			}
			userGrantList.add(ug);
		}
		userGrantRepository.insertUsersGrant(userGrantList, tUser_id_o);
		return "success";
	};
	
	//根据实验室名查找实验室
	public int selectLabBylabName(String labName) {
		return labRepository.selectLabBylabName(labName);
	}
	
	//修改实验室状态
	@Transactional
	public void updateLabStatus(int[] labids,char labstatus) {
		labRepository.updateLabStatus(labids,labstatus);
	}
	
	//验证实验室是否重复
	public int isLabNameRepeat(String labName) {
		return labRepository.isLabNameRepeat(labName);
	}
	
	public List<Lab> getAllLab( ){
		return labRepository.selectAllLab( );
	}

	public List<Lab> getLabByKeyWords(String keyWords){
		return labRepository.getLabByKeyWords(keyWords);
	}
	public int getCount(){
		return labRepository.getCount();
	}
	
}
