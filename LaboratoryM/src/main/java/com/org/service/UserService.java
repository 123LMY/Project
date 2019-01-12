package com.org.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.User;
import com.org.pojo.UserGrant;
import com.org.repository.LabRepository;
import com.org.repository.UserGrantRepository;
import com.org.repository.UserRepository;
import com.org.util.MD5Password;

@Service
public class UserService {

	@Resource
	private UserRepository userRepository;
	
	@Resource
	private UserGrantRepository userGrantRepository;
	
	@Resource
	private LabRepository labRepository;
	
	//验证登录
	public User selectManagerByUsernameandPassword(String userName,byte[] userPwd) {	
		return userRepository.selectManagerByUsernameandPassword(userName, MD5Password.md5Password(new String(userPwd)).getBytes());
	}
	
	//更新信息
	@Transactional
	public void UpdateManagerInfo(User user) {
		userRepository.updateManagerInfo(user);
	}
	
	//根据真实姓名查找用户的数量
	public int selectUserByRealName(String realName) {
		return userRepository.selectUserByRealName(realName);
	}
	
	//根据真实姓名查找用户
	public List<User> selectUsersByRealName(String realName){
		List<User> userList = userRepository.selectUsersByRealName(realName);
		for(int i=0;i<userList.size();i++) {
			if(String.valueOf(userList.get(i).getUserType()).equals("1")) {
				userList.get(i).setUserTypeName("老师");
			}else if(String.valueOf(userList.get(i).getUserType()).equals("0")) {
				userList.get(i).setUserTypeName("学生");
			}
			if(String.valueOf(userList.get(i).getStatus()).equals("1")) {
				userList.get(i).setStatusName("正常");
			}else if(String.valueOf(userList.get(i).getStatus()).equals("0")) {
				userList.get(i).setStatusName("冻结");
			}
		}
		return userList;
	}
	
	//根据用户名密码找用户
	public User SelectUserByUsernameAndPassword(String userName,byte[] userPwd) {
		return userRepository.SelectUserByUsernameAndPassword(userName, userPwd);
		
	}
	
	//新增用户
	@Transactional
	public int insertUser(User user){
		user.setUserPwd(MD5Password.md5Password(user.getUserName()).getBytes());
		return userRepository.insertUser(user);
	}
	
	//获取所有的用户
	public List<User> getAllUser(){
		return userRepository.getAllUser();
	}
	
	//修改用户信息
	@Transactional
	public void updateUser(String userName,char status,char userType,long id,char grants) {
		userRepository.updateUser(MD5Password.md5Password(userName).getBytes(), status, userType, id, grants);
	}
	
	//根据用户id查找用户
	public User selectUserByUserId(Integer userId){
		return userRepository.selectUserById(userId);
	}
	
	//修改密码
	@Transactional
	public int updatePasswordByUserId(long userId,byte[] userPwd){
		return userRepository.updatePasswordByUserId(userId, userPwd);
	}
	
	public byte[] checkPasswordById(Integer userId){
		return userRepository.checkPasswordById(userId);
	}
	
	//批量新增用户
	@Transactional
	public String insertUsers(List<User> userList,long adminId) {
		for(int i=0;i<userList.size();i++) {
			if(userRepository.isUserNameRepeat(userList.get(i).getUserName())>0) {
				return "repeat";
			}
			userList.get(i).setUserPwd(MD5Password.md5Password(userList.get(i).getUserName()).getBytes());
			userList.get(i).setStatus('1');
			if(userList.get(i).getGrants()=='\0') {
				userList.get(i).setGrants('0');
			}
			if(userList.get(i).getUserType()=='\0') {
				userList.get(i).setUserType('0');
			}
		}
		userRepository.insertUsers(userList);
		List<UserGrant> grantList = new ArrayList<UserGrant>();
		List labId = labRepository.selectAllLabId();
		for(int i=0;i<userList.size();i++) {
			for(int j=0;j<labId.size();j++) {
				UserGrant grant = new UserGrant();
				if(userList.get(i).getGrants()!='\0') {
					if(userList.get(i).getGrants()=='0') {
						grant.setNoteTopic('0');
					}else if(userList.get(i).getGrants()=='1') {
						grant.setNoteTopic('1');
					}
				}else {
					grant.setNoteTopic('0');
				}
				grant.settLab_id((long)labId.get(j));
				grant.settUser_id_a(userList.get(i).getId());
				grantList.add(grant);
			}
		}
		userGrantRepository.insertUsersGrant(grantList, adminId);
		return "success";
	}
	
	//验证重复
	public int isUserNameRepeat(String userName) {
		return userRepository.isUserNameRepeat(userName);
	}
	

	public User selectUserByUserId(long userId){
		return userRepository.selectUserById(userId);
	}
	
	public int updateUserInfo(User user){
		return userRepository.updateUserInfo(user);
	}
	public User getUserByRealName(String userName){
		return userRepository.getUserByRealName(userName);
	}
	public List<User> selectUserByName(String searchName){
		return userRepository.selectUserByName(searchName);
	}
	
	public User selectUserByUserName(String userName){
		return userRepository.selectUserByUserName(userName);
	}
}
