package com.org.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.Lab;
import com.org.pojo.LabApply;
import com.org.pojo.Project;
import com.org.pojo.ProjectMember;
import com.org.pojo.User;
import com.org.service.LabApplyService;
import com.org.service.LabService;
import com.org.service.ProjectService;
import com.org.service.UserService;
import com.org.vo.ProjectVo;

@Controller
@RequestMapping("/Project")
public class ProjectController {

	@Resource
	private ProjectService projectService;
	@Resource
	private LabService labService;
	@Resource
	private LabApplyService labApplyService;
	@Resource
	private UserService userService;
	
	//获取入驻项目情况统计
	@RequestMapping("/Admin/selectEntryProjectInfo")
	@ResponseBody
	public List selectEntryProjectInfo(@RequestParam(required=false)String startTime,@RequestParam(required=false)String endTime) {
		return projectService.selectEntryProjectInfo(startTime,endTime);
	}
	
	/**
	 * 新增项目
	 * @param projectVo
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/User/insertProject")
	@ResponseBody
	public Map<String,Object> insertProject(@RequestBody ProjectVo projectVo,HttpServletRequest request) throws ParseException{
		Map<String,Object>  map = new HashMap<>();
		User loginUser = (User)request.getSession().getAttribute("user");
		map = projectService.insertProject(projectVo, loginUser);	  
		return map;
	}
	
	/**
	 * 加载项目登记表界面
	 * @return
	 */
	@RequestMapping("/User/loadProjectRegistrationPage")
	public String loadProjectRegistrationPage(){
		return "html/projectRegistrationForm";
	}
	
	/**
	 * 加载项目登记表界面数据
	 * @return
	 */
	@RequestMapping("/User/loadProjectRegistrationPageData")
	@ResponseBody
	public Map<String,Object> loadProjectRegistrationPageData(){
		 Map<String,Object> map = new HashMap<String,Object>();
		 List<Lab> labList = labService.getAllLab();
		 map.put("lab",labList);
		 return map;
	}
	
	/**
	 * 加载项目管理界面
	 * @return
	 */
	@RequestMapping("/User/loadProjectManagementPage")
	public String loadProjectManagementPage(){
		return "html/projectManagement";
	}
	
	/**
	 * 加载项目管理界面数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/User/loadProjectManagementPageData")
	@ResponseBody
	public List<ProjectVo> loadProjectManagementPageData(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		List<Project> projectList = projectService.seletctProjectByUserId(user.getId());
		List<ProjectVo> pvList = screenOut(projectList);	
		return pvList;
	}
	
	/**
	 * 根据关键字搜索项目
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/User/selectProjectBySerachName")
	@ResponseBody
	public List<ProjectVo> selectProjectBySearchName(@RequestBody Map<String,Object> map,HttpServletRequest request){
		
		String searchName = map.get("searchName").toString();
		List<ProjectVo> projectVoList =  loadProjectManagementPageData(request);		
		List<Project> projectList = projectService.selectProjectByProName(searchName);
		List<ProjectVo> pvList = screenOut(projectList);	
		List<String> proNames = new ArrayList<String>();
		for(int i = 0;i<projectVoList.size();i++){
			proNames.add(projectVoList.get(i).getProName());
		}
		for(int j=0;j<pvList.size();j++){	 
			boolean flag = proNames.contains(pvList.get(j).getProName());
			if(flag == false){
				pvList.remove(j);
				j--;
			}
		}
		List<User> userList = userService.selectUserByName(searchName);		
		for(User user :userList){
			for(ProjectVo prjectVo:projectVoList){
				if(user.getRealName().equals(prjectVo.getProjectLeader())){
					pvList.add(prjectVo);
				}else if(user.getRealName().equals(prjectVo.getProjectTutor())){
					pvList.add(prjectVo);
				}
			}
		}
	 
		return pvList;
	}
	/**
	 * 筛选出传到前端的数据
	 * @param projectList
	 * @return
	 */
	public List<ProjectVo> screenOut(List<Project> projectList){
		List<ProjectVo> pvList = new ArrayList<ProjectVo>();
		for(Project project:projectList){
			String memberStr  = new String();		 
			ProjectVo projectVo = new ProjectVo();		
			LabApply la = labApplyService.selectLabApplyByProjectId(project.getId());
			if(la != null) {
				if(la.getAuditStatus() == '0') {
					projectVo.setProjectOperation("true");
				}else {
					projectVo.setProjectOperation("false");
				}
			}
					
			if(project.getEndTime() !=null){
				projectVo.setProjectStatus("已结题");
			}else{
				projectVo.setProjectStatus("在研");
			}
			projectVo.setId(project.getId());
			projectVo.setProName(project.getProName());
			projectVo.setProType(project.getProType());
			projectVo.setEndTime(project.getEndTime());
			List<ProjectMember> pmList = project.getProjectMemberList();  
			for(ProjectMember projectMember: pmList){
				if(projectMember.getIsAdviser() == '0' && projectMember.getIsLeader()=='1'){
					memberStr += projectMember.getUser().getRealName()+" " ;
					projectVo.setProjectLeader(projectMember.getUser().getRealName());
				}else if(projectMember.getIsAdviser() == '1' && projectMember.getIsLeader()=='0'){
					projectVo.setProjectTutor(projectMember.getUser().getRealName());
				}else {
					memberStr += projectMember.getUser().getRealName()+" " ;
				}
				 			
			}			
			projectVo.setForeMember(memberStr);
			pvList.add(projectVo);
		}
		return pvList;
	}
	
	
	/**
	 * 根据id更新project
	 * @param map
	 * @return
	 */
	@RequestMapping("/User/updateProject")
	@ResponseBody
	public String updateProject(@RequestBody Map<String,Object> map){		
		boolean flag = projectService.updateProjectById(map);
		if(flag == true){
			return "success";
		}
		return "fail";
	}

	/**
	 * 根据id批量删除project
	 * @param idList
	 * @return
	 */
	@RequestMapping("/User/deleteProjectById")
	@ResponseBody
	public String deleteProjectById(@RequestParam("projectId[]") List<Integer> idList){
		boolean flag = projectService.deleteProjectById(idList);
		if(flag == true){
			return "success";
		}
		return "fail";
	}
}
