package com.org.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.org.pojo.Project;
import com.org.pojo.ProjectResult;
import com.org.pojo.User;
import com.org.service.ProjectResultService;
import com.org.util.DateFactory;
import com.org.vo.ProResultAndMemberInfo;

@Controller
@RequestMapping("/ProjectResult")
public class ProjectResultController {
	@Resource
	private ProjectResultService projectResultService;

	// 智能班牌成果展示
	@RequestMapping("/Admin/selectProResult")
	@ResponseBody
	public List<ProResultAndMemberInfo> selectProResult(@RequestParam(required = false) String startTime,
			@RequestParam(required = false) String endTime) {
		return projectResultService.selectProResult(startTime, endTime);
	}

	/**
	 * 项目结题
	 * 
	 * @param prizeFiles
	 * @param patentFiles
	 * @param resultFiles
	 * @param servletRequest
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	@RequestMapping("/User/projectConclusion")
	@ResponseBody
	public Map<String,Object> insertProjectResult(@RequestParam("prizeFile") MultipartFile[] prizeFiles,
			@RequestParam("patentFile") MultipartFile[] patentFiles,
			@RequestParam("resultFile") MultipartFile[] resultFiles,
			HttpServletRequest servletRequest,
			MultipartHttpServletRequest request) throws IOException, ParseException{
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> msgMap = new HashMap<String,Object>();
		List<ProjectResult> projectResultList = new ArrayList<ProjectResult>();
		User user = (User) servletRequest.getSession().getAttribute("user");
		//获奖信息部分
		String[] prizeNames = request.getParameterValues("prizeName"); 
		String[] prizeLevels = request.getParameterValues("prizeLevel");
		String[] contestNames = request.getParameterValues("contestName");
		String[] prizeYears = request.getParameterValues("prizeYear");
		int projectId = Integer.valueOf(request.getParameter("projectId"));	 
				for(int i = 0;i< prizeFiles.length;i++){			
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
						String date = df.format(new Date());
						ProjectResult projectResult = new ProjectResult();						
						String fileName =date + prizeFiles[i].getOriginalFilename();					
						projectResult.setPrizeFile("upload/prize/"+fileName);
						String s = prizeFiles[i].getOriginalFilename().substring(prizeFiles[i].getOriginalFilename().lastIndexOf("."),prizeFiles[i].getOriginalFilename().length()).toLowerCase();
						
						if( !s.equals(".jpg") && !s.equals(".jpeg") && !s.equals(".png") && !s.equals(".pdf")) {
							msgMap.put("msg", "different");
							return msgMap;
						}
						if(contestNames[i] !=null){
							projectResult.setContestName(contestNames[i]);
						}
						if(prizeNames[i] !=null){
							projectResult.setPrizeName(prizeNames[i]);
						}
						if(prizeLevels[i] !=null){
							projectResult.setPrizeLevel(prizeLevels[i]);
						}
						if(prizeYears[i] !=null){
							projectResult.setPrizeYear(Integer.valueOf(prizeYears[i]));
						}
						projectResult.setResultType("0");
						projectResult.settProject_id(projectId);
						projectResult.settUser_id(user.getId());
						projectResultList.add(projectResult);
							
						
				}	
				
			//专利部分
			String[] patentNames = request.getParameterValues("patentName");
			String[] patentYears = request.getParameterValues("patentYear");	
			for(int i = 0;i<patentFiles.length;i++){
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				String date = df.format(new Date());
				ProjectResult projectResult = new ProjectResult();
				String fileName =date + patentFiles[i].getOriginalFilename();
				String s = patentFiles[i].getOriginalFilename().substring(patentFiles[i].getOriginalFilename().lastIndexOf("."),patentFiles[i].getOriginalFilename().length()).toLowerCase();
				
				if(!s.equals(".jpg") && !s.equals(".jpeg") && !s.equals(".png") && !s.equals(".pdf")) {
					msgMap.put("msg", "different");
					return msgMap;
				}else {
					projectResult.setPatentFile("upload/patent/"+fileName);
					if(patentNames[i] != null){
						projectResult.setPatentName(patentNames[i]);
					}
					if(patentYears[i] != null){
						projectResult.setPatentYear(Integer.valueOf(patentYears[i]));
					}
					projectResult.setResultType("1");
					projectResult.settProject_id(projectId);
					projectResult.settUser_id(user.getId());
					projectResultList.add(projectResult);
				}
			
			}
			
			//成果转让
			String[] resultTransfers = request.getParameterValues("resultTransfer");
			String[] resultYears = request.getParameterValues("resultYear");			
			for(int i = 0;i<resultFiles.length;i++){
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				String date = df.format(new Date());
				ProjectResult projectResult = new ProjectResult();				
				String fileName =date + resultFiles[i].getOriginalFilename();				
				projectResult.setResultFile("upload/result/"+fileName);
				 
				String s = resultFiles[i].getOriginalFilename().substring(resultFiles[i].getOriginalFilename().lastIndexOf("."),resultFiles[i].getOriginalFilename().length()).toLowerCase();
				if( !s.equals(".jpg") && !s.equals(".jpeg") && !s.equals(".png") && !s.equals(".pdf")) {
					msgMap.put("msg", "different");
					return msgMap;
				}
				if(resultTransfers[i] != null){
					projectResult.setResultTransfer(resultTransfers[i]);
				}
				if(resultYears[i] != null){
					projectResult.setResultYear(Integer.valueOf(resultYears[i]));
				}
				projectResult.setResultType("2");
				projectResult.settProject_id(projectId);
				projectResult.settUser_id(user.getId());
				projectResultList.add(projectResult);
			}
			
			//修改项目结题时间及项目展示路径
			String endTime = request.getParameter("endTime");
			String showLink = request.getParameter("showLink");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Project project = new Project();
			project.setId(projectId);
			project.setShowLink(showLink);
			if(endTime != null){
				Date d = sdf.parse(endTime);
				DateFactory df = new DateFactory();
				project.setEndTime(df.utilDateTosqlDate(d));
				
			}
			
			map.put("project", project);
			map.put("projectResultList", projectResultList);
			map.put("prizeFiles", prizeFiles);
			map.put("patentFiles", patentFiles);
			map.put("resultFiles", resultFiles);
			boolean flag = projectResultService.insertProjectResult(map,servletRequest);
			if(flag == true){
				msgMap.put("msg", "success");
				return msgMap;
			}else {
				msgMap.put("msg","fail");
				return msgMap;
			}
			 
	}
	

	/**
	 * 加载项目结题页面
	 * 
	 * @return
	 */
	@RequestMapping("/User/loadProjectConclusionPage")
	public String loadProjectConclusionPage() {
		return "html/projectConclusion";
	}

}
