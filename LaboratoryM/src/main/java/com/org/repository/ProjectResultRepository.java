package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;

import com.org.pojo.ProjectResult;
import com.org.util.SqlFactory;
import com.org.vo.ProResultAndMemberInfo;

public interface ProjectResultRepository {

	//成果展示
	@SelectProvider(type=SqlFactory.class,method="selectProResult")
	@Results({
		@Result(column="proId",property="proMember",many=@Many(select="com.org.repository.UserRepository.selectUserByProjectId")),
		@Result(column="proId",property="teacher",many=@Many(select="com.org.repository.UserRepository.selectTeachByProjectId"))
	})
	public List<ProResultAndMemberInfo> selectProResult(@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	
	@Insert("insert into tprojectresult(tProject_id,resultType,contestName,prizeLevel,"
			+ "prizeName,prizeFile,patentName,patentFile,resultTransfer,resultFile,tUser_id,remark) "
			+ "values(#{tProject_id,jdbcType=BIGINT},"
			+ "#{resultType,jdbcType=CHAR},"
			+ "#{contestName,jdbcType=VARCHAR},"
			+ "#{prizeLevel,jdbcType=VARCHAR},"
			+ "#{prizeName,jdbcType=VARCHAR},"
			+ "#{prizeFile,jdbcType=VARCHAR},"
			+ "#{patentName,jdbcType=VARCHAR},"
			+ "#{patentFile,jdbcType=VARCHAR},"
			+ "#{resultTransfer,jdbcType=VARCHAR},"
			+ "#{resultFile,jdbcType=VARCHAR},"
			+ "#{tUser_id,jdbcType=BIGINT},"
			+ "#{remark,jdbcType=VARCHAR} )")
	public int insertProjectResult(ProjectResult projectResult);
}
