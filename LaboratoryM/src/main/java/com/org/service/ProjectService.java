package com.org.service;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.LabApply;
import com.org.pojo.Project;
import com.org.pojo.ProjectMember;
import com.org.pojo.User;
import com.org.repository.LabApplyRepository;
import com.org.repository.ProjectMemberRepository;
import com.org.repository.ProjectRepository;
import com.org.repository.UserRepository;
import com.org.util.DateFactory;
import com.org.vo.EntryProjectInfo;
import com.org.vo.ProjectVo;

@Service
public class ProjectService {

	@Resource
	private ProjectRepository projectRepository;
	@Resource
	private UserRepository userRepository;
	@Resource
	private ProjectMemberRepository projectMemberRepository;
	@Resource
	private LabApplyRepository labApplyRepository;
	
	//入驻项目统计
	public List<EntryProjectInfo> selectEntryProjectInfo(String startTime,String endTime){
		List<EntryProjectInfo> epInfoList1 = projectRepository.selectEntryProjectInfo1(startTime,endTime);
		List<EntryProjectInfo> epInfoList2 = projectRepository.selectEntryProjectInfo2(startTime,endTime);
		for(int i=0;i<epInfoList2.size();i++) {
			if(epInfoList1.get(i).getProType()=='0') {
				epInfoList1.get(i).setProTypeName("创新项目");
			}else if(epInfoList1.get(i).getProType()=='1') {
				epInfoList1.get(i).setProTypeName("竞赛项目");
			}else if(epInfoList1.get(i).getProType()=='2') {
				epInfoList1.get(i).setProTypeName("企业项目");
			}else if(epInfoList1.get(i).getProType()=='3') {
				epInfoList1.get(i).setProTypeName("其他项目");
			}
			epInfoList1.get(i).setSchoolLevel(epInfoList2.get(i).getSchoolLevel());
			epInfoList1.get(i).setProvincialLevel(epInfoList2.get(i).getProvincialLevel());
			epInfoList1.get(i).setNationalLevel(epInfoList2.get(i).getNationalLevel());
			epInfoList1.get(i).setInternationalLevel(epInfoList2.get(i).getInternationalLevel());
			epInfoList1.get(i).setPatent(epInfoList2.get(i).getPatent());
			epInfoList1.get(i).setAchievements(epInfoList2.get(i).getAchievements());
		}
		
		return epInfoList1;
	}
	
	
	/**
	 * 新增项目
	 * @param project
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public Map<String,Object> insertProject(ProjectVo projectVo,User loginUser) throws ParseException{
		 Map<String,Object> map = new HashMap<String,Object>();
		 String msg = new String();		 
		 Calendar c = Calendar.getInstance();
		 int year = c.get(Calendar.YEAR); 
		 int month = c.get(Calendar.MONTH)+1; 
		 int day = c.get(Calendar.DATE) +3; 
		 int hour = c.get(Calendar.HOUR_OF_DAY);
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		 String startDate =String.valueOf(year)+"-"+String.valueOf(month)+"-"
				 +String.valueOf(day)+" " +String.valueOf(hour)+":00:00";
		 Date date = format.parse(startDate);
			User plUser = userRepository.getUserByRealName(projectVo.getProjectLeader());
			List<ProjectMember> projectMemberList = projectMemberRepository.selectProjectMemberByUserId(plUser.getId());
			for(ProjectMember pm : projectMemberList){				 
				if(pm.getProject().getProName().equals(projectVo.getProName())){
					map.put("status","the same pj");
					return map;
				}
			}
			//新增项目
			Project project = new Project();
			project.setProName(projectVo.getProName());
			project.setProType(projectVo.getProType());
			project.setStartTime( DateFactory .utilDateTosqlDate(date));
			project.settUser_id(loginUser.getId());
			int i = projectRepository.insertProject(project);
		
			ArrayList<String> members = projectVo.getProjectMembers();
			List<ProjectMember> pmList = new ArrayList<ProjectMember>();
			for(String membera : members){
				if(membera.equals(projectVo.getProjectLeader())){
					User user = userRepository.getUserByRealName(membera);
					if(user !=null){
						if(user.getFingerMark() == '\0'){
							msg += membera+" ";
						}
					}
					membera = " ";
				}
					User user = userRepository.getUserByRealName(membera);
					if(user !=null){
					ProjectMember projectMember = new ProjectMember();
					projectMember.settUser_id(user.getId());
					projectMember.setIsAdviser('0');
					projectMember.setIsLeader('0');
					projectMember.settProject_id(project.getId());
					pmList.add(projectMember);	
					if(user.getFingerMark() == '\0'){
						msg += membera+" ";
					}
				}
			
			}
			//新增项目指导老师
			ProjectMember projectTutor = new ProjectMember();
			User user = userRepository.getUserByRealName(projectVo.getProjectTutor());
			projectTutor.settUser_id(user.getId());
			projectTutor.setIsAdviser('1');
			projectTutor.setIsLeader('0');
			projectTutor.settProject_id(project.getId());
			pmList.add(projectTutor);
			//新增项目负责人
			ProjectMember projectLeader = new ProjectMember();
		
			projectLeader .settUser_id(plUser.getId());
			projectLeader .setIsAdviser('0');
			projectLeader .setIsLeader('1');
			projectLeader .settProject_id(project.getId());
			pmList.add(projectLeader );
			int j =projectMemberRepository.insertProjectMembers(pmList);
			
			//新增实验室预约申请
			LabApply labApply = new LabApply(); 
			labApply.settLab_id(projectVo.getLabId());
			labApply.setAuditStatus('0');
			labApply.settUser_id_b(loginUser.getId());
			labApply.settProject_id(project.getId());
			labApply.setPurpose(project.getProName());
			labApply.setStartTime(DateFactory.stringToTimestamp(startDate));
			int k = labApplyRepository.insertProjectLab(labApply);
			if(i>0 || j>0 || k>0){
				map.put("status", "success");
				map.put("fingerMark", msg);
				return map;
			}
			map.put("status", "fail");
		 return map;
	}
	/**
	 * 根据用户id获得其所有项目
	 * @param userId
	 * @return
	 */
	public List<Project> seletctProjectByUserId(long userId){
		return projectRepository.seletctProjectByUserId(userId);
	}
	
	/**
	 * 根据proName模糊查询
	 * @param searchName
	 * @return
	 */
	public List<Project> selectProjectByProName(String searchName){
		return projectRepository.selectProjectByProName(searchName);
	}
	
	/**
	 * 根据id更新项目
	 * @param project
	 * @return
	 */
	@Transactional
	public boolean updateProjectById(Map<String,Object> map){
		 
		String newProjectMembers = map.get("newProjectMembers").toString();
		newProjectMembers = newProjectMembers.replaceAll("\\[","").replaceAll("\\]","").replaceAll(",","");
		String[] newProjectMembersList = newProjectMembers.split(" ");
		String projectId = map.get("projectId").toString();
		String proName = map.get("proName").toString();
		String proType = map.get("proType").toString();
		String proTutor = map.get("projectTutor").toString();
		String proLeader = map.get("projectLeader").toString();
		Project project = new Project();
		project.setId(Integer.valueOf(projectId));
		project.setProName(proName);
		project.setProType(proType.charAt(0));
		projectRepository.updateProjectById(project);
		
		projectMemberRepository.deleteProjectMemberByProjectId(Integer.valueOf(projectId));
		List<ProjectMember> pmList = new ArrayList<ProjectMember>();
		for(String membera : newProjectMembersList){	
			if(membera.equals(proLeader)) {
				membera = " ";
			}
			User user = userRepository.getUserByRealName(membera);
			if(user!=null){
				ProjectMember projectMember = new ProjectMember();
				projectMember.settUser_id(user.getId());
				projectMember.setIsAdviser('0');
				projectMember.setIsLeader('0');
				projectMember.settProject_id(Integer.valueOf(projectId));
				pmList.add(projectMember);	
			}
		
		}
		//项目指导老师
		ProjectMember projectTutor = new ProjectMember();
		User adviser = userRepository.getUserByRealName(proTutor);
		projectTutor.settUser_id(adviser.getId());
		projectTutor.setIsAdviser('1');
		projectTutor.setIsLeader('0');
		projectTutor.settProject_id(Integer.valueOf(projectId));
		pmList.add(projectTutor);
		//项目负责人
		ProjectMember projectLeader = new ProjectMember();
		User plUser = userRepository.getUserByRealName(proLeader);
		projectLeader.settUser_id(plUser.getId());
		projectLeader.setIsAdviser('0');
		projectLeader.setIsLeader('1');
		projectLeader.settProject_id(Integer.valueOf(projectId));
		pmList.add(projectLeader); 
		int i = projectMemberRepository.insertProjectMembers(pmList);
			if(i>0){
				return true;
			}
		return false;
	}
	
	/**
	 * 根据id批量删除项目
	 * @param idList
	 * @return
	 */
	@Transactional
	public boolean deleteProjectById(List<Integer> idList){
		for(Integer id :idList){
			projectMemberRepository.deleteProjectMemberByProjectId(id);
			labApplyRepository.deleteLabApplyByProjectId(id);
		}
		int i = projectRepository.deleteProjectByIds(idList);
		if(i>0){
			return true;
		}
		return false;
	}
	
	//跟新项目的结题时间与展示网址
	public boolean updateProjectEndTimeAndShowLink(Project project){
		int i =  projectRepository.updateProjectEndTimeAndShowLink(project);
		if(i>0)return true;
		return false;
	}
}
