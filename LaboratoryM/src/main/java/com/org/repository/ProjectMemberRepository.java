package com.org.repository;

import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.org.pojo.ProjectMember;

public interface ProjectMemberRepository {
	
	@Select("select * from tprojectmember where tproject_id=#{id}")
	@Results(id="projectMemberResults",value={
			@Result(id=true,column="id",property="id"),
			@Result(column="tUser_id",property="user",one=@One(select="com.org.repository.UserRepository.selectUserById")),
			@Result(column="tProject_id",property="project",one=@One(select="com.org.repository.ProjectRepository.selectProjectById"))			
	})
	public List<ProjectMember> selectProjectMementByProjectId(long userId);
	
	
	@Insert("insert into tprojectmember values(#{tUser_id,jdbcType=VARCHAR},"
			+ "#{tProject_id,jdbcType=BIGINT},"
			+ "#{isLeader,jdbcType=CHAR},"
			+ "#{isAdviser,jdbcType=CHAR},"
			+ "#{remark,jdbcType=VARCHAR})")
	@Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")
	public int insertProjectMember(ProjectMember projectMember);
	

	@Update({"<script> ",
			"<foreach collection='projectMemberList' item='item' index='index' separator=';'>",
			"update tprojectmember set tUser_id=#{item.tUser_id,jdbcType=BIGINT},",
			 "isLeader=#{item.isLeader,jdbcType=CHAR},",
			 "isAdviser=#{item.isAdviser,jdbcType=CHAR}, ",
			"remark=#{item.remark,jdbcType=VARCHAR} ",
			" where tUser_id = #{item.tUser_id} and tProject_id=#{item.tProject_id}",
			"</foreach> ",
			"</script>"})
	public int updateProjectMemberByUserId(@Param("projectMemberList")List<ProjectMember> pmList);
	
	
	@Delete("delete from tprojectmember where tProject_id = #{tProject_id}")
	public int deleteProjectMemberByProjectId(@Param("tProject_id") long tProject_id);
	
	
	@Insert({	"<script>"
				+"insert into tprojectmember(tUser_id,tProject_id, isLeader,isAdviser,remark) values "
				+ "<foreach collection='projectMemberList' item='item' index='index' separator=','>"
				+ "(#{item.tUser_id,jdbcType=VARCHAR}, "
				+ "#{item.tProject_id,jdbcType=BIGINT},"
				+ "#{item.isLeader,jdbcType=CHAR},"
				+ "#{item.isAdviser,jdbcType=CHAR},"
				+ "#{item.remark,jdbcType=VARCHAR})",
				"</foreach> </script>"})
	public int insertProjectMembers(@Param("projectMemberList")List<ProjectMember> projectMember);

	
	@Select("select * from tprojectmember where tuser_id = #{userId}")
	@ResultMap("projectMemberResults")
	public List<ProjectMember> selectProjectMemberByUserId(@Param("userId")long userId);
}
