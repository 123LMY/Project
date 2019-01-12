package com.org.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.org.pojo.Department;
import com.org.pojo.IntelligentDevice;
import com.org.pojo.ProjectMember;
import com.org.pojo.User;
import com.org.pojo.UserGrant;
import com.org.service.DeparmentService;
import com.org.service.IntelligentDeviceService;
import com.org.service.ProjectMemberService;
import com.org.service.TempHumiRecordService;
import com.org.service.UserGrantService;
import com.org.service.UserService;
import com.org.util.DateFactory;
import com.org.util.MD5Password;
import com.org.util.ExcelUtil;

@Controller
public class UserController {

	@Resource
	private UserService userService;
	@Autowired
	private MD5Password md5Password;
	@Resource
	private UserGrantService userGrantService;
	@Resource
	private ProjectMemberService  projectMemberService;
	@Resource
	private DeparmentService  deparmentService;
	@Resource
	private JavaMailSender jms;
	
	//登录验证
	@RequestMapping("/logincheck")
	@ResponseBody
	public String selectManagerByUsernameandPassword(User user, HttpServletRequest request) {
		User loginadmin = userService.selectManagerByUsernameandPassword(user.getUserName(), user.getUserPwd());
		HttpSession session = request.getSession();
		if (loginadmin != null) {
			session.setAttribute("administrators", loginadmin);
			session.setMaxInactiveInterval(60*60);
			return "success";
		} else {
			return "fail";
		}
	}

	//登录后传递管理员信息到管理端页面
	@RequestMapping("/adminindex")
	@ResponseBody
	public String loadAdminIndex(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User administrators = (User) session.getAttribute("administrators");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("administrators", administrators);
		return JSON.toJSONString(resultMap);
	}

	//访问管理端登录页面
	@RequestMapping("/adminlogin")
	public String test() {
		return "Login";
	}

	//修改管理员信息
	@RequestMapping("/Admin/updateInfo")
	@ResponseBody
	public String updateManagerInfo(HttpServletRequest request, User user) {
		user.setId(((User) (request.getSession().getAttribute("administrators"))).getId());
		userService.UpdateManagerInfo(user);
		return "success";
	}

	//退出登录
	@RequestMapping("/adminlogout")
	@ResponseBody
	public String administratorsLogout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("administrators");
		}
		return "adminlogin";
	}

	//根据用户的真实姓名查找用户
	@RequestMapping("/Admin/selectUsersByRealName")
	@ResponseBody
	public List selectUsersByRealName(@RequestParam(required = false) String realName) {
		return userService.selectUsersByRealName(realName);
	}

	//插入用户（验证是否存在）
	@RequestMapping("/Admin/insertUsers")
	@ResponseBody
	public String insertUsers(User user,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		return userService.insertUsers(userList,administrators.getId());
	}

	//修改用户的部分信息
	@RequestMapping("/Admin/updateUser")
	@ResponseBody
	public String updateUser(@RequestParam String userName, @RequestParam char grants, @RequestParam char status, @RequestParam char userType,
			@RequestParam long id) {
		
		userService.updateUser(userName, status, userType, id, grants);
		return "success";
	}

	//导入excel文件批量插入用户
	@RequestMapping("/Admin/insertUsersByFile")
	@ResponseBody
	public Map insertUsersByFile(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
		Map resultMap = new HashMap<String, Object>();
		try {
			FileInputStream fis = (FileInputStream) file.getInputStream();
			LinkedHashMap<String, String> alias = new LinkedHashMap<>();
			alias.put("学号", "userName");
			alias.put("姓名", "realName");
			alias.put("电话", "phone");
			alias.put("邮箱", "email");
			alias.put("备注", "remark");
			List<User> pojoList = ExcelUtil.excel2Pojo(file.getInputStream(), User.class, alias);
			User administrators = (User) request.getSession().getAttribute("administrators");
			String result = userService.insertUsers(pojoList,administrators.getId());
			if(result.equals("success")) {
				resultMap.put("status", "success");
			}else if(result.equals("repeat")) {
				resultMap.put("status", "repeat");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resultMap.put("status", "fail");
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 用户登录验证
	 * 
	 * @param userMap
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/userLogin")
	@ResponseBody
	public Map<String,Object> SelectUserByUsernameAndPassword(@RequestBody Map<String,Object> userMap,Model model,HttpServletRequest request,HttpServletResponse response) {
		String userName = userMap.get("userName").toString();
		String userPwd = userMap.get("userPwd").toString();
		String remFlag = userMap.get("remFlag").toString();
		String mPassword = md5Password.md5Password(userPwd);
		byte[] password = mPassword.getBytes();
		User loginuser = userService.SelectUserByUsernameAndPassword(userName, password);
		HttpSession session = request.getSession();
		Map<String,Object> map = new HashMap();
		if(loginuser != null) {
			session.setAttribute("user", loginuser);
			session.setMaxInactiveInterval(30*60);	
			if(remFlag.equals("yes")){
				String loginInfo = userName+"T"+userPwd;
				Cookie userCookie = new Cookie("loginInfo",loginInfo);
				userCookie.setMaxAge(60*60);
				userCookie.setPath("/");
				response.addCookie(userCookie);
			}
			map.put("src","/User/loadUserIndex");
			map.put("status", "success");
			return map;
		}
		map.put("status", "fail");
		return map;
	}
	
	@RequestMapping(value="/User/loadUserIndex")
	public String loadUserIndex(){	 
			return "html/userIndex";
	}

	@RequestMapping(value="/User/loadUserIndexData")
	@ResponseBody
	public Map<String,Object> loadUserIndexData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		User user = (User)request.getSession().getAttribute("user");
		List<ProjectMember> projectMemberList = projectMemberService.selectProjectMemberByUserId(user.getId());
		if(projectMemberList!=null){
			for(ProjectMember projectMember:projectMemberList){
				Date date = projectMember.getProject().getEndTime();
				if(date == null){
					map.put("flag","true");
					break;
				}else{
					map.put("flag", "false");
				}
			}
		}else {
			map.put("flag","false");
		}
		 
		map.put("user", user);
		return map;
		
	}



	/**
	 * 返回登录界面
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "html/userLogin";
	}
	/*-----个人中心-------*/
	/**
	 * 加载用户修改信息界面
	 * 
	 * @return
	 */
	@RequestMapping(value="/User/loadPersonalPage")
	public String loadPersonalPage(){
		return "html/updatePersonalMsg";
	}

	/**
	 * 加载修改密码界面
	 * 
	 * @return
	 */
	@RequestMapping(value="/User/loadPasswordPage")
	public String loadPasswordPage(){
		return "html/updatePassword";
	}
	/**
	 * 加载用户数据
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/User/loadPersonalData",method = RequestMethod.POST,produces="application/json")
	@ResponseBody
	public Map<String,Object> loadPersonalData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		User loginUser = (User)request.getSession().getAttribute("user");	
		User user = userService.selectUserByUserId(loginUser.getId());
		String deptNo = user.getUserName().substring(4, 6);
		String majorNo = user.getUserName().substring(6, 8);
		String year = user.getUserName().substring(2, 4);
		String grades = user.getUserName().substring(9,10);
	 
		Department d = deparmentService.selectDepartmentByDeptNo(deptNo);
		map.put("institute", d.getDeptName());
		map.put("grades",year+grades);
		for(int i = 0; i<d.getProfessionList().size();i++) {
			if(majorNo.equals(d.getProfessionList().get(i).getProfNo())) {
				map.put("major",d.getProfessionList().get(i).getProfName());
			}
		}
		map.put("user",user);
		return map;
	}


	/**
	 * 修改个人信息
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/User/updatePersonalMsg")
	@ResponseBody
	public String updatePersonalMsg(User user,HttpServletRequest request){		 
		User loginUser =(User)request.getSession().getAttribute("user");
		user.setId(loginUser.getId());
		user.setUserType(loginUser.getUserType());
		userService.UpdateManagerInfo(user);
		return "update success";
	}

	/**
	 * 根据id修改密码
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/User/updatePassword",method = RequestMethod.POST)
	@ResponseBody
	public String updatePasswordByUserId(@RequestBody Map map,HttpServletRequest request){
		User user =(User)request.getSession().getAttribute("user");
		String password = map.get("newPassword").toString();
		String md5Pwd = md5Password.md5Password(password);
		int i = userService.updatePasswordByUserId(user.getId(), md5Pwd.getBytes());
		if(i>0){	
			HttpSession session = request.getSession(false);
			if(session!=null) {
				session.removeAttribute("user");
				session.invalidate();
			}
			return "update success";
		}
		
		return " update fail";
	}

	@RequestMapping(value="/User/checkPassword")
	@ResponseBody
	public String checkPassword(@RequestBody Map map,HttpServletRequest request){
		String oldPassword = map.get("oldPassword").toString();
		String md5password = md5Password.md5Password(oldPassword);
		byte[] oldUserPwd = md5password.getBytes();
		User user = (User)request.getSession().getAttribute("user"); 
		if(Arrays.equals(oldUserPwd, user.getUserPwd())){
			return "the same";
		}
		return "different";
	}
	
	@RequestMapping(value="/User/userLoginOut")
	@ResponseBody
	public Map<String,Object> userLoginOut(HttpServletRequest request){
		 Map<String,Object> map = new HashMap<String,Object>();
		HttpSession session = request.getSession(false);
		if(session!=null) {
			session.removeAttribute("user");
		}
		map.put("status","success");
		map.put("url","/");
		return map;
	}
	
	@RequestMapping("/User/findPassword")
	@ResponseBody
	public Map<String,Object> findPassword(@RequestBody Map<String,Object> map) throws Exception{		
	        String userName = map.get("findName").toString();
	        String email = map.get("findEmail").toString();
			Random random = new Random();
			String result="";
			Map<String,Object> userMap = new HashMap<String,Object>();
			for (int i=0;i<6;i++)
			{
				result+=random.nextInt(10);
			}
			User user = userService.selectUserByUserName(userName);
			if(!user.getEmail().equals(email)){
				userMap.put("msg", "different");
				return userMap;
			}else{
				 SimpleMailMessage mainMessage = new SimpleMailMessage();
			 		//发送者
			 		mainMessage.setFrom("1136436199@qq.com");
			 		//接收者
			 		mainMessage.setTo(email);
			 		//发送的标题
			 		mainMessage.setSubject("验证码");
			 		//发送的内容
			 		mainMessage.setText(result);
			 		jms.send(mainMessage);	
			        userMap.put("code", result);
			        userMap.put("userId", user.getId());
			        return userMap;
			}	        
	}
	
	@RequestMapping(value="/User/midifiedPassword")
	@ResponseBody
	public String midifiedPassword(@RequestBody Map map,HttpServletRequest request){
		String newPassword = map.get("newPassword").toString();
		String userName = map.get("userName").toString();
		User user = userService.selectUserByUserName(userName);
		String md5password = md5Password.md5Password(newPassword);	
		byte[] newUserPwd = md5password.getBytes();
		if(Arrays.equals(newUserPwd, user.getUserPwd())){
			return "the same";
		}else{
			int i = userService.updatePasswordByUserId(user.getId(),newUserPwd);
			if(i>0){	
				HttpSession session = request.getSession(false);
				if(session!=null) {
					session.removeAttribute("user");
					session.invalidate();
				}
				return "update success";
			}
		}
		return "fail";
	}	

}
