package com.org.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.LabApply;
import com.org.repository.DeviceApplyRepository;
import com.org.repository.LabApplyRepository;
import com.org.repository.LabRepository;
import com.org.repository.ProjectRepository;
import com.org.vo.LabUseInfo;
import com.org.vo.LabUsingInfo;

@Service
public class LabApplyService {

	@Resource
	private LabApplyRepository labApplyRepository;
	@Resource
	private DeviceApplyRepository deviceApplyRepository;
	@Resource
	private LabRepository labRepository;
	@Resource
	private ProjectRepository projectRepository;
	
	//获取实验室使用信息
	public List<LabUsingInfo> selectLabUsingInfo(char auditStatus,String name){
		List<LabUsingInfo> luInfoList = labApplyRepository.selectLabUsingInfo(auditStatus,name);
		List proNameList = new ArrayList();
		for(int i=0;i<luInfoList.size();i++) {
			luInfoList.get(i).setUserNum(labApplyRepository.selectLabUserNumByLabId(luInfoList.get(i).getLabid()));
			if(luInfoList.get(i).getAuditStatus()=='0') {
				luInfoList.get(i).setAuditStatusName("预约");
			}else if(luInfoList.get(i).getAuditStatus()=='1') {
				luInfoList.get(i).setAuditStatusName("拒绝");
			}else if(luInfoList.get(i).getAuditStatus()=='2') {
				luInfoList.get(i).setAuditStatusName("借出");
			}else if(luInfoList.get(i).getAuditStatus()=='3') {
				luInfoList.get(i).setAuditStatusName("还回");
			}
			
			if(luInfoList.get(i).getLabStatus()=='0') {
				luInfoList.get(i).setLabStatusName("空闲");
			}else if(luInfoList.get(i).getLabStatus()=='1') {
				luInfoList.get(i).setLabStatusName("在用");
			}else if(luInfoList.get(i).getLabStatus()=='2') {
				luInfoList.get(i).setLabStatusName("维修");
			}
			if(luInfoList.get(i).gettUser_id_a()!=0) {
				proNameList = projectRepository.selectProNameByUserId(luInfoList.get(i).gettUser_id_a());
				StringBuffer sb = new StringBuffer();
				if(proNameList.size()==0) {
					sb.append("暂无");
					luInfoList.get(i).setProName(sb.toString());
					continue;
				}
				for(int j=0;j<proNameList.size();j++) {
					sb.append(proNameList.get(j));
					if(j<proNameList.size()-1) {
						sb.append(",");
					}
				}
				luInfoList.get(i).setProName(sb.toString());
			}else {
				String proName = projectRepository.selectProNameById(luInfoList.get(i).gettProject_id());
				luInfoList.get(i).setProName(proName);
				luInfoList.get(i).setLuser(proName);
				luInfoList.get(i).setReservations(proName);
			}
			
			
		}
		return luInfoList;
	}
	
	//修改实验室通过状态
	@Transactional
	public void updatePassLabApplyStatus(int[] labapplyids,long tUser_id_o) {
		labApplyRepository.updatePassLabApplyStatus(labapplyids,tUser_id_o);
		
		
	}
	
	//修改拒绝状态
	@Transactional
	public void updateRefuseLabApplyStatus(int[] labapplyids,long tUser_id_o) {
		labApplyRepository.updateRefuseLabApplyStatus(labapplyids, tUser_id_o);
	}
	
	//撤销审核状态
	@Transactional
	public void cancelLabApplyStatus(int[] ids,long tUser_id_o) {
		labApplyRepository.cancelLabApplyStatus(ids,tUser_id_o);
	}
	
	//获取借出的实验室
	public List<LabApply> selectLendLabApply(){
		return labApplyRepository.selectLendLabApply();
	}
	
	//修改还回状态
	@Transactional
	public void backLabApplyStatus(List ids,long tUser_id_o) {
		labApplyRepository.backLabApplyStatus(ids,tUser_id_o);
	}
	
	//查询实验室状态
	public List<LabUseInfo> selectLabApplyStatus(){
		return labApplyRepository.selectLabApplyStatus();
	}
	
	//系统消息实验室申请审核
	public boolean checkLabApply() {
		if(labApplyRepository.checkLabApply()!=0) {
			return true;
		}
		return false;
	}
	
	//查询未审核的实验室申请记录
	public List<LabApply> selectBookLabApply(){
		return labApplyRepository.selectBookLabApply();
	}
	
	public int selectLabUserNumByLabId(@Param("id")long id) {
		return labApplyRepository.selectLabUserNumByLabId(id);
	}
	
	public List<LabApply> selectLabApplyByTime(Timestamp startTime,Timestamp endTime){
		return labApplyRepository.selectLabApplyByTime(startTime, endTime);
	}
	public int insertLabApply(LabApply labApply){
		return labApplyRepository.insertLabApply(labApply);
	}
	public List<LabApply> selectLabApplyByUserId(long userId){
		return labApplyRepository.selectLabApplyByUserId(userId);
	}
	
	public int deleteLabApplyById(long id){
		return labApplyRepository.deleteLabApplyById(id);
	}
	
	public int insertProjectLab(LabApply labApply){
		return labApplyRepository.insertProjectLab(labApply);
	}

	public int deleteLabApplyByProjectId(long projectId){
		return labApplyRepository.deleteLabApplyByProjectId(projectId);
	}
	public LabApply selectLabApplyByProjectId(long projectId) {
		return labApplyRepository.selectLabApplyByProjectId(projectId);
	}
	@Transactional
	public Map<String,Object> checkLabApplyByUserId(long userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		int i = labApplyRepository.checkLabApplyByUserId(userId);
		if(i>0) {
			map.put("status","true");
		}else {
			map.put("status","false");
		}
		
		int j = deviceApplyRepository.checkDeviceApplyByUserId(userId);
		if(j>0) {
			map.put("status","true");
		}else {
			map.put("status","false");
		}
			
		return map;
	}
	
	
	public int updateLabApplyByProjectId(Timestamp endTime,long projectId) {
		return labApplyRepository.updateLabApplyByProjectId(endTime, projectId);
	}
}
