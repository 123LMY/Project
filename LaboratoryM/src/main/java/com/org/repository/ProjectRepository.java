package com.org.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.org.pojo.Project;
import com.org.util.SqlFactory;
import com.org.vo.EntryProjectInfo;
import com.org.vo.GrantInfo;

public interface ProjectRepository {

	// 项目统计
	@SelectProvider(type = SqlFactory.class, method = "selectEntryProjectInfo1")
	@Results(value = { @Result(column = "proType", property = "proType"),
			@Result(column = "proTotal", property = "proTotal"), @Result(column = "stuTotal", property = "stuTotal"),
			@Result(column = "teachTotal", property = "teachTotal"), @Result(column = "finish", property = "finish") })
	public List<EntryProjectInfo> selectEntryProjectInfo1(@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	// 项目统计
	@SelectProvider(type = SqlFactory.class, method = "selectEntryProjectInfo2")
	@Results(value = { @Result(column = "schoolLevel", property = "schoolLevel"),
			@Result(column = "provincialLevel", property = "provincialLevel"),
			@Result(column = "nationalLevel", property = "nationalLevel"),
			@Result(column = "internationalLevel", property = "internationalLevel"),
			@Result(column = "patent", property = "patent"),
			@Result(column = "achievements", property = "achievements"), })
	public List<EntryProjectInfo> selectEntryProjectInfo2(@Param("startTime") String startTime,
			@Param("endTime") String endTime);
	
	//获取用户项目组
	@Select("select proName from tproject where id IN (select tProject_id from tprojectmember where tUser_id = #{tUser_id})")
	public List<String> selectProNameByUserId(@Param("tUser_id") long tUser_id);
	
	@Select("select startTime,endTime from tproject where id IN (select tProject_id from tprojectmember where tUser_id = #{tUser_id})")
	public List<GrantInfo> selectendTimeByUserId(@Param("tUser_id") long tUser_id);
	
	@Select("select * from tproject where id = #{id}")
	public Project selectProjectById(long id);

	@Insert("insert into tproject(proName,proType,startTime,endTime,showLink,tUser_id,remark) values("
			+ "#{proName,jdbcType=VARCHAR}," + "#{proType,jdbcType=CHAR}," + "#{startTime,jdbcType=DATE},"
			+ "#{endTime,jdbcType=DATE}," + "#{showLink,jdbcType=VARCHAR}," + "#{tUser_id,jdbcType=BIGINT},"
			+ "#{remark,jdbcType=VARCHAR})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int insertProject(Project project);

	@Select("select * from tproject where proName like CONCAT('%',#{searchName},'%')")
	@ResultMap("projectResult")
	public List<Project> selectProjectByProName(@Param("searchName") String searchName);

	@Update("update tproject set proName=#{proName,jdbcType=VARCHAR}," + "proType=#{proType,jdbcType=CHAR},"
			+ "endTime=#{endTime,jdbcType=DATE}," + "showLink=#{showLink,jdbcType=VARCHAR},"
			+ "remark=#{remark,jdbcType=VARCHAR} " + "where id = #{id}")
	public int updateProjectById(Project project);

	@Update("update tproject set  " + "endTime=#{endTime,jdbcType=DATE}," + "showLink=#{showLink,jdbcType=VARCHAR}"
			+ " where id = #{id}")
	public int updateProjectEndTimeAndShowLink(Project project);

	@Delete("<script>" + "delete from tproject where id in ( "
			+ "<foreach collection='idList' item='item' index = 'index' separator=','>" + "#{item}"
			+ "</foreach>)</script>")
	public int deleteProjectByIds(@Param("idList") List<Integer> ids);

	@Select("select * from tproject where id in (select tproject_id from tprojectmember where tuser_id = #{userId})")
	@Results(id = "projectResult", value = {
			@Result(column = "id", property = "projectMemberList", many = @Many(select = "com.org.repository.ProjectMemberRepository.selectProjectMementByProjectId")),
			@Result(id = true, column = "id", property = "id") })
	public List<Project> seletctProjectByUserId(@Param("userId") long userId);

	@Select("select proName from tproject where id = #{id}")
	public String selectProNameById(@Param("id")long id);
}
