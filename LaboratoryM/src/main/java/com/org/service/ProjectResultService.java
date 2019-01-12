package com.org.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.org.pojo.Project;
import com.org.pojo.ProjectResult;
import com.org.repository.ProjectRepository;
import com.org.repository.ProjectResultRepository;
import com.org.util.DateFactory;
import com.org.vo.ProResultAndMemberInfo;

@Service
public class ProjectResultService {

	@Resource
	private ProjectResultRepository projectResultRepository;
	@Resource
	private ProjectService projectService;
	@Resource
	private LabApplyService labApplyService;
	
	//获取成果和用户
	public List<ProResultAndMemberInfo> selectProResult(String startTime,String endTime){
		List<ProResultAndMemberInfo> list = projectResultRepository.selectProResult(startTime,endTime);
		for(int i=0;i<list.size();i++) {
			StringBuffer proMembers = new StringBuffer();
			StringBuffer teachers = new StringBuffer();
			StringBuffer prize = new StringBuffer();
			StringBuffer patent = new StringBuffer();
			StringBuffer result = new StringBuffer();
			for(int j=0;j<list.get(i).getProMember().size();j++) {
				proMembers.append(list.get(i).getProMember().get(j).getRealName());
				if(j<list.get(i).getProMember().size()-1) {
					proMembers.append(",");
				}
			}
			for(int k=0;k<list.get(i).getTeacher().size();k++) {
				teachers.append(list.get(i).getTeacher().get(k).getRealName());
				if(k<list.get(i).getTeacher().size()-1) {
					teachers.append(",");
				}
			}
			if(list.get(i).getPrizeYear()!=0) {
				prize.append(list.get(i).getContestName()+" "+list.get(i).getPrizeYear()+" "+list.get(i).getPrizeName()+" "+list.get(i).getPrizeLevel());
			}else {
				prize.append("暂无");
			}
			if(list.get(i).getPatentYear()!=0) {
				patent.append(list.get(i).getPatentName()+" "+list.get(i).getPatentYear());
			}else {
				patent.append("暂无");
			}
			if(list.get(i).getResultYear()!=0) {
				result.append(list.get(i).getResultTransfer()+" "+list.get(i).getResultYear());
			}else {
				result.append("暂无");
			}
			list.get(i).setPrize(prize.toString());
			list.get(i).setPatent(patent.toString());
			list.get(i).setResult(result.toString());
			list.get(i).setProMembers(proMembers.toString());
			list.get(i).setTeachers(teachers.toString());
		}
		return list;
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public boolean insertProjectResult(Map<String,Object> map,HttpServletRequest servletRequest) throws IOException{ 
		boolean flag = false;
		List<ProjectResult> projectResultList = (List<ProjectResult>)map.get("projectResultList");
		Project project = (Project) map.get("project");
		MultipartFile[] prizeFiles = (MultipartFile[])map.get("prizeFiles");
		MultipartFile[] patentFiles = (MultipartFile[])map.get("patentFiles");
		MultipartFile[] resultFiles = (MultipartFile[])map.get("resultFiles");
		for(ProjectResult projectResult : projectResultList){
			 int i = projectResultRepository.insertProjectResult(projectResult);
			  if(i>0){
				  flag = true;				   
			  }else{
				  flag = false;
				  break ;
			  }
		}
		flag = projectService.updateProjectEndTimeAndShowLink(project);
		
		if(flag == true){
			for(int i = 0;i<prizeFiles.length;i++){
				String folderPath = servletRequest.getSession().getServletContext()
						.getRealPath("/")
						+ "upload/prize/";
				File file = new File(folderPath);
				if(!file.mkdirs()){
					file.mkdirs();
				}
				for(int j = 0; j<projectResultList.size();j++){
					if(projectResultList.get(j).getPrizeFile() != null){
						String realPath = servletRequest.getSession().getServletContext().getRealPath("/")+projectResultList.get(j).getPrizeFile();			
						prizeFiles[i].transferTo(new File(realPath));
					}
				}	
			}
			
			for(int i = 0;i<patentFiles.length;i++){
				String folderPath = servletRequest.getSession().getServletContext()
						.getRealPath("/")
						+ "upload/patent/";
				File file = new File(folderPath);
				if(!file.mkdirs()){
					file.mkdirs();
				}
								
				for(int j = 0;j<projectResultList.size();j++){
					if(projectResultList.get(j).getPatentFile()!=null){
						String realPath = servletRequest.getSession().getServletContext().getRealPath("/")+projectResultList.get(j).getPatentFile();			
						patentFiles[i].transferTo(new File(realPath));
					}
				}
						
				
				
			}
			for(int i = 0;i<resultFiles.length;i++){
				String folderPath = servletRequest.getSession().getServletContext()
						.getRealPath("/")
						+ "upload/result/";
				File file = new File(folderPath);
				if(!file.mkdirs()){
					file.mkdirs();
				}
				for(int j = 0;j<projectResultList.size();j++){
					if(projectResultList.get(j).getResultFile() != null){
						String realPath = servletRequest.getSession().getServletContext().getRealPath("/")+projectResultList.get(j).getResultFile();			
					
						resultFiles[i].transferTo(new File(realPath));
					}
				}
			}
		}
		return flag;
	}
}
