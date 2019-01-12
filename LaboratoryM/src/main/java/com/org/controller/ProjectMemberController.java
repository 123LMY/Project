package com.org.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.User;
import com.org.service.ProjectMemberService;
import com.org.service.UserService;

@Controller
@RequestMapping("/ProjectMember")
public class ProjectMemberController {
	@Resource 
	private ProjectMemberService projectMemberService;
	
	@Resource 
	private UserService userService;
	
	
	@RequestMapping("/User/checkMembers")
	@ResponseBody
	public Map checkMembers(@RequestBody Map userMap){
		Map map = new HashMap<>();
		String[] member = userMap.get("members").toString().split("\\,");
		String userList = new String();
	
		for(String membera : member){
			 
			User user = userService.getUserByRealName(membera);
			if(user == null){
				userList += membera+" " ;
				map.put("status","error");
			}else{
				map.put("status","success");
				userList = user.getRealName();
				map.put("msg", user);
				return map;
			}
			
		}
		return map; 
	}

}
