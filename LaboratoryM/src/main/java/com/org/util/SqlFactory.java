package com.org.util;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.org.pojo.Lab;
import com.org.pojo.Notice;
import com.org.pojo.User;
import com.org.pojo.UserGrant;

public class SqlFactory {

	//修改管理员信息
	public String updateManagerInfo(User user) {
		//User manager = user;
		SQL sql = new SQL();
		sql.UPDATE("tuser");
		if(user.getRemark()!=null) {
			sql.SET("remark ='"+user.getRemark()+"'");
		}
		if(user.getUserPwd()!=null) {
			sql.SET("userPwd ='"+user.getUserPwd()+"'");
		}
		if(user.getEmail()!=null) {
			sql.SET("email = '"+user.getEmail()+"'");
		}
		if(user.getPhone()!=null) {
			sql.SET("phone = '"+user.getPhone()+"'");
		}
		if(String.valueOf(user.getUserType())!=null) {
			sql.SET("userType ='"+user.getUserType()+"'");
		}
		sql.WHERE("id="+user.getId());
		return sql.toString();
	}
	
	//添加门禁授权
	public String addAuthorization(Map map) {
		int[] ids = (int[]) map.get("ids");
		long tUser_id_o = (long) map.get("tUser_id_o");
		StringBuilder sb = new StringBuilder();
        sb.append("update tusergrant ");
        sb.append("set noteTopic = '1'");
        if(tUser_id_o!=-1) {
        	sb.append(",tUser_id_o = "+tUser_id_o);
        }
        sb.append(" where tUser_id_a in(");
        for (int i = 0; i < ids.length; i++) {
            sb.append(ids[i]);
            if (i < ids.length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
	}
	
	//撤销门禁授权
	public String cancelAuthorization(Map map) {
		int[] ids = (int[]) map.get("ids");
		long tUser_id_o = (long) map.get("tUser_id_o");
		StringBuilder sb = new StringBuilder();
        sb.append("update tusergrant ");
        sb.append("set noteTopic = '0'");
        if(tUser_id_o!=-1) {
        	sb.append(",tUser_id_o = "+tUser_id_o);
        }
        sb.append(" where tUser_id_a in(");
        for (int i = 0; i < ids.length; i++) {
            sb.append(ids[i]);
            if (i < ids.length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
	}
	
	//新增用户权限
	public String insertUsersGrant(Map map) {
		List<UserGrant> userGrantList = (List)map.get("userGrantList");
		long tUser_id_o = (long) map.get("tUser_id_o");
		StringBuilder sb = new StringBuilder();  
	    sb.append("INSERT INTO tusergrant ");
	    sb.append("(tUser_id_a, noteTopic, tUser_id_o,tLab_id) ");  
	    sb.append("VALUES "); 
	    for(int i=0;i<userGrantList.size();i++) {
	    	sb.append("("+userGrantList.get(i).gettUser_id_a()+","+userGrantList.get(i).getNoteTopic()+","+tUser_id_o+","+userGrantList.get(i).gettLab_id()+")");
	    	if(i<userGrantList.size()-1) {
	    		sb.append(",");
	    	}
	    }
	    
	    return sb.toString();
	}
	
	//删除公告
	public String deleteNotices(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tnotice where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//获取考勤记录
	public String selectAttence(Map map) {
		StringBuilder sb = new StringBuilder();
		String realName = (String) map.get("realName");
		String signinTime = (String) map.get("signinTime");
		String signoutTime = (String) map.get("signoutTime");
		sb.append("select c.id,c.signinTime,c.signoutTime,a.userName,a.realName,a.id as tUser_id FROM tuser a INNER JOIN tattence c ON c.tUser_id = a.id");
		if(realName!="") {
			if(signinTime!=""&&signoutTime!="") {
				sb.append(" where a.realName LIKE CONCAT('%','"+realName+"','%') and unix_timestamp(signinTime) >= unix_timestamp('"+signinTime+"') and unix_timestamp(signoutTime) <= unix_timestamp('"+signoutTime+"')");
			}else {
				sb.append(" where a.realName LIKE CONCAT('%','"+realName+"','%') ");
			}
		}else {
			if(signinTime!=""&&signoutTime!="") {
				sb.append(" where unix_timestamp(signinTime) >= unix_timestamp('"+signinTime+"') and unix_timestamp(signoutTime) <= unix_timestamp('"+signoutTime+"')");
				
			}
		}
		sb.append("  order by c.signinTime desc");
		return sb.toString();
	}
	
	//获取设备使用信息
	public String selectDeviceUseInfoByDateTime(Map map) {
		StringBuffer sb = new StringBuffer();
		String startDate = (String) map.get("startDate");
		String endDate = (String) map.get("endDate");
		long devType = (long)map.get("devType");
		sb.append("select " + 
				"a.devModel as devModel," + 
				"a.devName as devName," + 
				"COUNT(c.id) as useNum," + 
				"COUNT(c.id) as userNum," + 
				"SUM(TIMESTAMPDIFF(DAY,c.startDate,c.endDate)+1) as useDate," + 
				"(SELECT COUNT(*) FROM tdevicedetail WHERE tDevice_id = a.id)as buyNum," + 
				"(SELECT COUNT(*) FROM tdevicedetail WHERE tDevice_id = a.id AND devStatus = '0') as stock," + 
				"(SELECT COUNT(*) FROM tdevicedetail WHERE tDevice_id = a.id AND devStatus = '4')  as damageNum " + 
				"FROM " + 
				"tdevice a " + 
				"INNER JOIN tdevicedetail b ON a.id = b.tDevice_id " + 
				"INNER JOIN tdeviceapply c ON c.tDeviceDetail_id = b.id ");
		sb.append("WHERE a.tDevType_id = "+devType);
		if(!(startDate.equals("")&&endDate.equals(""))) {
			sb.append(" and startDate >= '");
			sb.append(startDate);
			sb.append( "' and endDate <= '");
			sb.append(endDate+"'");
		}
		sb.append(" GROUP BY a.id");
		return sb.toString();
		
	}
	
	//获取实验室使用信息
	public String selectLabUseInfo(Map map) {
		StringBuffer sb = new StringBuffer();
		String startTime = (String) map.get("startTime"); 
		String endTime = (String) map.get("endTime");
		sb.append("select a.labName as labName,count(b.id) as useNum,SUM(TIMESTAMPDIFF(HOUR,b.startTime,b.endTime)) as useTime FROM tlab a,tlabapply b WHERE b.tLab_id = a.id ");
		if(!(startTime.equals("")&&endTime.equals(""))) {
			sb.append(" and startTime >= '");
			sb.append(startTime);
			sb.append( "' and endTime <= '");
			sb.append(endTime+"'");
		}
		sb.append(" GROUP BY a.id");
		return sb.toString();
	}
	
	//获取入驻项目部分信息1
	public String selectEntryProjectInfo1(Map map) {
		StringBuffer sb = new StringBuffer();
		String startTime = (String) map.get("startTime"); 
		String endTime = (String) map.get("endTime");
		sb.append("SELECT " + 
			"a.proType as 'proType'," + 
			"(SELECT COUNT(*) FROM tproject WHERE proType = a.proType) as 'proTotal', " + 
			"SUM(CASE WHEN c.userType = '0' THEN 1 ELSE 0 END) as 'stuTotal'," + 
			"SUM(CASE when c.userType = '1' THEN 1 ELSE 0 END) as 'teachTotal', " + 
			"(SELECT COUNT(*) FROM tproject WHERE endTime < NOW() AND !isnull(endTime) AND proType = a.proType)  as 'finish' " + 
			"FROM " + 
			"tprojectmember b," + 
			"tuser c," + 
			"tproject a " + 
			"WHERE " + 
			"a.id IN (SELECT tProject_id FROM tprojectmember GROUP BY tProject_id) " + 
			"AND c.id IN (SELECT tUser_id FROM tprojectmember GROUP BY tProject_id) " + 
			"AND a.id = b.tProject_id "
			+ "AND c.id = b.tUser_id ");

		if(!(startTime.equals("")&&endTime.equals(""))) {
			sb.append(" and startTime >= '");
			sb.append(startTime);
			sb.append( "' and endTime <= '");
			sb.append(endTime+"'");
		}
		sb.append(" GROUP BY a.proType");
		return sb.toString();
	}
	
	//获取入驻项目部分信息2
	public String selectEntryProjectInfo2(Map map) {
		StringBuffer sb = new StringBuffer();
		String startTime = (String) map.get("startTime"); 
		String endTime = (String) map.get("endTime");
		sb.append("SELECT " + 
				"SUM(CASE WHEN a.prizeLevel='校级' THEN 1 ELSE 0 END) as 'schoolLevel', " + 
				"SUM(CASE WHEN a.prizeLevel='省级' THEN 1 ELSE 0 END) as 'provincialLevel', " + 
				"SUM(CASE WHEN a.prizeLevel='国家' THEN 1 ELSE 0 END) as 'nationalLevel', " + 
				"SUM(CASE WHEN a.prizeLevel='国际' THEN 1 ELSE 0 END) as 'internationalLevel', " + 
				"SUM(CASE WHEN a.resultType='1' THEN 1 ELSE 0 END) as 'patent', " + 
				"SUM(CASE WHEN a.resultType='2' THEN 1 ELSE 0 END) as 'achievements'" + 
				"FROM " + 
				"tprojectresult a, " + 
				"tproject b " + 
				"WHERE " + 
				"a.tProject_id = b.id ");

		if(!(startTime.equals("")&&endTime.equals(""))) {
			sb.append(" and startTime >= '");
			sb.append(startTime);
			sb.append( "' and endTime <= '");
			sb.append(endTime+"'");
		}
		sb.append(" GROUP BY b.proType");
		return sb.toString();
	}
	
	//获取门禁记录
	public String selectEntranceGuardRecord(Map map) {
		StringBuffer sb = new StringBuffer();
		String inTime = (String) map.get("inTime"); 
		String outTime = (String) map.get("outTime");
		sb.append("SELECT a.id,a.inTime,a.outTime, b.realName,b.userName,b.id as tUser_id FROM tentranceguardrecord a INNER JOIN tuser b ON a.tUser_id = b.id ");
		if(!(inTime.equals("")&&outTime.equals(""))) {
			sb.append(" and inTime >= '");
			sb.append(inTime);
			sb.append( "' and outTime <= '");
			sb.append(outTime+"'");
		}
		sb.append(" order by inTime desc");
		return sb.toString();
	}
	
	//删除门禁日志
	public String deleteEntranceGuardRecord(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM tentranceguardrecord WHERE id IN (");
		for(int i=0;i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//删除节点
	public String deleteNode(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM tnode WHERE id IN (");
		for(int i=0;i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//根据真实姓名获取用户信息
	public String selectUsersByRealName(Map map) {
		StringBuffer sb = new StringBuffer();
		String realName = (String) map.get("realName");
		sb.append("select * from tuser ");
		if(!realName.equals("")) {
			sb.append("where realName LIKE '%");
			sb.append(realName+"%'");
		}
		
		return sb.toString();
	}
	
	//新增用户
	public String insertUsers(Map map) {
		List<User> userList = (List<User>) map.get("list");
		StringBuffer sb = new StringBuffer();
		sb.append("insert into tuser (userName,userPwd,realName,phone,email,grants,userType,status,remark) values ");
		for(int i=0;i<userList.size();i++) {
			sb.append("(");
			sb.append("'"+userList.get(i).getUserName()+"',");
			sb.append("'"+new String(userList.get(i).getUserPwd())+"',");
			sb.append("'"+userList.get(i).getRealName()+"',");
			sb.append("'"+userList.get(i).getPhone()+"',");
			sb.append("'"+userList.get(i).getEmail()+"',");
			sb.append("'"+userList.get(i).getGrants()+"',");
			sb.append("'"+userList.get(i).getUserType()+"',");
			sb.append("'"+userList.get(i).getStatus()+"',");
			sb.append("'"+userList.get(i).getRemark()+"'");
			sb.append(")");
			if(i<userList.size()-1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	//更新用户信息
	public String updateUser(Map map) {
		StringBuffer sb = new StringBuffer();
		byte[] userPwd = (byte[]) map.get("userPwd");
		char userType = (char) map.get("userType");
		char status = (char) map.get("status");
		char grants = (char) map.get("grants");
		long id = (long) map.get("id");
		sb.append("update tuser set userType = '");
		sb.append(userType+"', ");
		sb.append("status = '");
		sb.append(status+"', ");
		sb.append("grants = '");
		sb.append(grants+"'");
		if(userPwd.length!=0) {
			sb.append(", userPwd = '");
			sb.append(new String(userPwd)+"' ");
		}
		sb.append("where id = "+id);
		return sb.toString();
	}
	
	//获取实验室信息
	public String selectLabInfo(Map map) {
		SQL sql = new SQL().SELECT("*").FROM("tlab");
		String labName = (String) map.get("labName");
		if(!labName.equals("")) {
			sql.WHERE("labName LIKE CONCAT('%',#{labName},'%')");
		}
		return sql.toString();
	}
	
	//删除实验室
	public String deleteLab(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tlab where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//修改实验室信息
	public String updateLab(Map map) {
		SQL sql = new SQL().UPDATE("tlab");
		sql.SET("labName = #{labName}")
		.SET("labSite = #{labSite}")
		.SET("labFunction = #{labFunction}")
		.SET("labStatus = #{labStatus}")
		.SET("remark = #{remark}")
		.SET("tUser_id_o = #{tUser_id_o}")
		.SET("tUser_id_k = (select id from tuser where realName = #{administrators})")
		.WHERE("id = #{id}");
		
		return sql.toString();
	}
	
	//新增实验室
	public String insertLab(Lab lab) {
		SQL sql = new SQL().INSERT_INTO("tlab")
				.VALUES("labName", "#{labName}")
				.VALUES("labSite", "#{labSite}")
				.VALUES("labFunction", "#{labFunction}")
				.VALUES("tUser_id_k", "#{tUser_id_k}")
				.VALUES("labStatus", "#{labStatus}")
				.VALUES("tUser_id_o", "#{tUser_id_o}")
				.VALUES("remark", "#{remark}");
		return sql.toString();		
		
	}
	
	//设备信息查询
	public String selectDeviceResource(Map map) {
		SQL sql = new SQL()
				.SELECT("a.id as id")
				.SELECT("a.devModel as devModel")
				.SELECT("a.devName as devName")
				.SELECT("COUNT(b.id) as devTotal")
				.SELECT("SUM(CASE WHEN b.devStatus = '0' THEN 1 ELSE 0 END) as freeNum")
				.SELECT("SUM(CASE WHEN b.devStatus = '1' THEN 1 ELSE 0 END) as usingNum")
				.SELECT("SUM(CASE WHEN b.devStatus = '3' THEN 1 ELSE 0 END) as damageNum")
				.SELECT("SUM(CASE WHEN b.devStatus = '4' THEN 1 ELSE 0 END) as scrapNum")
				.FROM("tdevice a")
				.LEFT_OUTER_JOIN("tdevicedetail b ON a.id = b.tDevice_id");
				if(!((String)map.get("deviceName")).equals("")) {
					sql.WHERE("a.devName LIKE CONCAT('%',#{deviceName},'%') or a.devModel  LIKE CONCAT('%',#{deviceName},'%')");
				}
				sql.GROUP_BY("a.id");
		return sql.toString();
	}
	
	//删除设备
	public String deleteDevice(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tdevice where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
		
	}
	
	//删除子设备
	public String deleteDeviceDetail(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tdevicedetail where tDevice_id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
		
	}
	
	//根据id删除子设备
	public String deleteDevDetailById(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tdevicedetail where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
		
	}
	
	//删除设备申请
	public String deleteDevApply(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tdeviceapply where tDeviceDetail_id in (select id from tdevicedetail where tDevice_id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append("))");
		return sb.toString();
	}
	
	//根据子设备id删除设备申请
	public String deleteDevApplyBytDeviceDetail_id(Map map) {
		int[] ids = (int[]) map.get("ids");
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tdeviceapply where tDeviceDetail_id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	public String selectLabUsingInfo(Map map) {
		StringBuffer sb = new StringBuffer();
		char status = (char) map.get("auditStatus");
		String name = (String)map.get("name");
		sb.append("SELECT " + 
				"b.id as labapplyid," + 
				"b.tProject_id as tProject_id," + 
				"a.id as labid," + 
				"a.labName as labName," + 
				"a.labStatus as labStatus," + 
				"b.startTime as startTime," + 
				"b.endTime as endTime," + 
				"b.purpose as purpose," + 
				"b.auditStatus as auditStatus," + 
				"c.realName as reservations," + 
				"e.realName as luser, " + 
				"c.id as tUser_id_a " + 
				"FROM " + 
				"tlabapply b " + 
				"LEFT JOIN tlab a ON b.tLab_id = a.id " + 
				"LEFT JOIN tuser c ON c.id = b.tUser_id_a " + 
				"LEFT JOIN tuser e ON e.id = b.tUser_id_b ");
		if(!name.equals("")) {
			sb.append("WHERE a.labName like '%"+name+"%' OR c.realName like '%"+name+"%' OR e.realName like '%"+name+"%'");
			if(!String.valueOf(status).equals("9")) {
				sb.append("AND b.auditStatus = '"+status+"'");
			}
		}else {
			if(!String.valueOf(status).equals("9")) {
				sb.append("WHERE b.auditStatus = '"+status+"'");
			}
		}
		sb.append(" order by b.startTime desc");
		return sb.toString();
	}
	
	//修改实验室申请状态
	public String updatePassLabApplyStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids = (int[]) map.get("ids");
		long tUser_id_o = (long)map.get("tUser_id_o");
		sb.append("update tlabapply set auditStatus = '2',tUser_id_o = "+tUser_id_o+" where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//修改实验室申请状态
	public String updateLabStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids = (int[]) map.get("ids");
		char labstatus = (char) map.get("labstatus");
		sb.append("update tlab set labStatus = '"+labstatus+"' where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
		
		
	}
	
	//修改实验室申请状态
	public String updateRefuseLabApplyStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids = (int[]) map.get("ids");
		long tUser_id_o = (long)map.get("tUser_id_o");
		sb.append("update tlabapply set auditStatus = '1'");
		if(tUser_id_o!=-1) {
			sb.append(", tUser_id_o = "+tUser_id_o);
		}
		sb.append(" where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//撤销实验室申请审核状态
	public String cancelLabApplyStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids = (int[]) map.get("ids");
		long tUser_id_o = (long)map.get("tUser_id_o");
		sb.append("update tlabapply set auditStatus = '0',tUser_id_o = "+tUser_id_o+" where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//修改实验室申请状态
	public String backLabApplyStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		List ids = (List) map.get("list");
		long tUser_id_o = (long)map.get("tUser_id_o");
		sb.append("update tlabapply set auditStatus = '3'");
		if(tUser_id_o!=-1) {
			sb.append(", tUser_id_o = "+tUser_id_o);
		}
		sb.append(" where id in (");
		for(int i=0; i<ids.size();i++) {
			sb.append(ids.get(i));
			if(i<ids.size()-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//获取设备申请信息
	public String selectDevApplyInfo(Map map) {
		StringBuffer sb = new StringBuffer();
		char status = (char) map.get("auditStatus");
		String name = (String)map.get("name");
		sb.append("SELECT " + 
				"b.id as devDetailId," + 
				"a.id as devApplyId," + 
				"c.devModel as devModel," + 
				"c.devName as devName," + 
				"b.devStatus as devStatus," + 
				"b.devSn as devSn," + 
				"a.startDate as startDate," + 
				"a.endDate as endDate," + 
				"a.purpose as purpose," + 
				"a.auditStatus as auditStatus," + 
				"d.realName as reservations," + 
				"e.realName as duser, " + 
				"e.id as tUser_id_a " + 
				"FROM " + 
				"tdeviceapply a " + 
				"LEFT JOIN tdevicedetail b ON a.tDeviceDetail_id = b.id " + 
				"LEFT JOIN tdevice c ON b.tDevice_id = c.id " + 
				"LEFT JOIN tuser d ON a.tUser_id_a = d.id " + 
				"LEFT JOIN tuser e ON a.tUser_id_b = e.id " + 
				"");
		if(!name.equals("")) {
			sb.append("WHERE c.devName like '%"+name+"%' OR c.devModel like '%"+name+"%' OR b.devSn like '%"+name+"%'");
			if(!String.valueOf(status).equals("9")) {
				sb.append("AND a.auditStatus = '"+status+"'");
			}
		}else {
			if(!String.valueOf(status).equals("9")) {
				sb.append("WHERE a.auditStatus = '"+status+"'");
			}
		}
		sb.append(" order by a.startDate desc");
		return sb.toString();
	}
	
	//修改设备申请状态
	public String updateDevApplyStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids = (int[]) map.get("ids");
		char devapplystatus = (char) map.get("devapplystatus");
		long tUser_id_o = (long)map.get("tUser_id_o");
		sb.append("update tdeviceapply set auditStatus = '"+devapplystatus+"' ");
		if(tUser_id_o!=-1) {
			sb.append(", tUser_id_o = "+tUser_id_o);
		}
		sb.append(" where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//修改子设备状态
	public String updateDevDetailStatus(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids = (int[]) map.get("ids");
		char devdetailstatus = (char) map.get("devdetailstatus");
		long tUser_id_o = (long) map.get("tUser_id_o");
		sb.append("update tdevicedetail set devStatus = '"+devdetailstatus+"'");
		if(tUser_id_o!=-1) {
			sb.append(",tUser_id_o="+tUser_id_o);
		}
		
		sb.append(" where id in (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	//获取项目成果信息
	public String selectProResult(Map map) {
		StringBuffer sb = new StringBuffer();
		String startTime = (String) map.get("startTime"); 
		String endTime = (String) map.get("endTime");
		sb.append("SELECT a.id as proId, a.proName as proName, b.contestName as contestName, b.prizeYear as prizeYear,b.prizeName as prizeName,b.prizeLevel as prizeLevel,b.patentName as patentName,b.patentYear as patentYear,b.resultTransfer as resultTransfer,b.resultYear as resultYear FROM tproject a INNER JOIN tprojectresult b ON a.id = b.tProject_id ");
		if(!(startTime.equals("")&&endTime.equals(""))) {
			sb.append(" where (prizeYear >= '"+startTime+"' and prizeYear <= '"+endTime+"') OR (patentYear >='"+startTime+"' and patentYear <= '"+endTime+"') OR (resultYear >='"+startTime+"' and resultYear <= '"+endTime+"')");
		}
		return sb.toString();
	}
	
	public String selectTempHumiRecord(Map map) {
		String startTime = (String) map.get("startTime");
		String endTime = (String)map.get("endTime");
		StringBuffer sb = new StringBuffer();
		sb.append("select * from ttemphumirecord");
		if(!startTime.equals("")&&!endTime.equals("")) {
			sb.append(" where unix_timestamp(takeTime) >= unix_timestamp('"+startTime+"') and unix_timestamp(takeTime) <= unix_timestamp('"+endTime+"')");
		}
		
		return sb.toString();
	}
	
	public String deleteGrantByLabId(Map map) {
		StringBuffer sb = new StringBuffer();
		int[] ids  = (int[]) map.get("tLab_id");
		sb.append("delete from tusergrant where tLab_id IN (");
		for(int i=0; i<ids.length;i++) {
			sb.append(ids[i]);
			if(i<ids.length-1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
}
