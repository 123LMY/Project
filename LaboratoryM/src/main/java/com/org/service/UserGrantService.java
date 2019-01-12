package com.org.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.UserGrant;
import com.org.repository.LabRepository;
import com.org.repository.ProjectRepository;
import com.org.repository.UserGrantRepository;
import com.org.util.DateFactory;
import com.org.vo.GrantInfo;


@Service
public class UserGrantService {

	@Resource
	private UserGrantRepository userGrantRepository;
	@Resource
	private ProjectRepository projectRepository;
	@Resource
	private LabRepository labRepository;
	
	//添加门禁授权
	@Transactional
	public void addAuthorization(int[] ids,long tUser_id_o) {
		userGrantRepository.addAuthorization(ids,tUser_id_o);
	}
	
	//撤销门禁授权
	@Transactional
	public void cancelAuthorization(int[] ids,long tUser_id_o) {
		userGrantRepository.cancelAuthorization(ids,tUser_id_o);
	}
	
	//插入用户门禁授权
	@Transactional
	public void insertUsersGrant(List<UserGrant> userGrantList,long tUser_id_o) {
		userGrantRepository.insertUsersGrant(userGrantList,tUser_id_o);
	}
	
	//查找门禁授权记录
	public List<GrantInfo> selectGrantInfo() {
		List<GrantInfo> g = userGrantRepository.selectGrantInfo();
		List proNameList = new ArrayList();
		List<GrantInfo> timeList = new ArrayList<GrantInfo>();
		for(int i=0;i<g.size();i++) {
			proNameList = projectRepository.selectProNameByUserId(g.get(i).gettUser_id_a());
			timeList = projectRepository.selectendTimeByUserId(g.get(i).gettUser_id_a());	
			g.get(i).setUserName(g.get(i).getUser().getRealName());
			if(g.get(i).getNoteTopic()=='1') {
				g.get(i).setTopic("已授权");
			}else if(g.get(i).getNoteTopic()=='0') {
				g.get(i).setTopic("未授权");
			}
			
			StringBuffer sb = new StringBuffer();
			for(int k=0;k<timeList.size();k++) {
				if(timeList.get(k).getEndTime()==null) {
					if(timeList.get(k).getStartTime().before(DateFactory.utilDateTosqlDate(new java.util.Date()))) {
						sb.append("未开始 ");
					}else {
						sb.append("进行中 ");
					}
				}else {
					if(timeList.get(k).getEndTime().before(DateFactory.utilDateTosqlDate(new java.util.Date()))) {
						sb.append("已结束 ");
					}
				}
			}
			g.get(i).setStatus(sb.toString());
			g.get(i).setLabName(labRepository.selectLabNameById(g.get(i).getLab_id()));
			if(g.get(i).gettUser_id_a()!=0) {
				proNameList = projectRepository.selectProNameByUserId(g.get(i).gettUser_id_a());
				StringBuffer sb1 = new StringBuffer();
				if(proNameList.size()==0) {
					sb1.append("暂无");
					g.get(i).setProName(sb1.toString());
					continue;
				}
				for(int j=0;j<proNameList.size();j++) {
					sb1.append(proNameList.get(j));
					if(j<proNameList.size()-1) {
						sb1.append(",");
					}
				}
				g.get(i).setProName(sb1.toString());
			}
		}
		return g;
	}
	
}
