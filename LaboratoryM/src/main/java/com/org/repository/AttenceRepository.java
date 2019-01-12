package com.org.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;


import com.org.util.SqlFactory;
import com.org.vo.AttenceInfo;

public interface AttenceRepository {

	//根据条件获取考勤信息
	@SelectProvider(type=SqlFactory.class,method = "selectAttence")
	@Results({
			@Result(column="id",property="id"),
			@Result(column="userName",property="userName"),
			@Result(column="realName",property="realName"),
			@Result(column="signinTime",property="signinTime"),
			@Result(column="signoutTime",property="signoutTime"),
			@Result(column="proName",property="proName")
	})
	public List<AttenceInfo> selectAttence(@Param("signinTime") String signinTime, @Param("signoutTime") String signoutTime, @Param("realName")String realName );
	
	//获取所有考勤信息
	@Select("select c.id,c.signinTime,c.signoutTime,a.userName,a.realName,a.id as tUser_id FROM tuser a INNER JOIN tattence c ON c.tUser_id = a.id order by c.signinTime desc")
	@Results({
		@Result(column="id",property="id"),
		@Result(column="userName",property="userName"),
		@Result(column="realName",property="realName"),
		@Result(column="signinTime",property="signinTime"),
		@Result(column="signoutTime",property="signoutTime"),
		@Result(column="proName",property="proName")
	})
	public List<AttenceInfo> selectAllAttence();
	
}
