package com.org.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.org.pojo.LabApply;
import com.org.util.SqlFactory;
import com.org.vo.LabUseInfo;
import com.org.vo.LabUsingInfo;

public interface LabApplyRepository {

	//获取实验室申请记录
	@SelectProvider(type=SqlFactory.class,method="selectLabUsingInfo")
	public List<LabUsingInfo> selectLabUsingInfo(@Param("auditStatus")char auditStatus,@Param("name")String name);
	
	//统计实验室在用人数
	@Select("SELECT " + 
			"IFNULL(SUM(CASE WHEN a.auditStatus='2' AND ISNULL(a.tProject_id) THEN 1 ELSE 0 END)+count(b.id),0) " + 
			"FROM " + 
			"tlabapply a " + 
			"LEFT JOIN tprojectmember b ON a.tProject_id = b.tProject_id " + 
			"WHERE a.tLab_id = #{id} and a.startTime <= NOW()")
	public int selectLabUserNumByLabId(@Param("id")long id);
	
	//更新通过实验室审核状态
	@UpdateProvider(type=SqlFactory.class,method="updatePassLabApplyStatus")
	public void updatePassLabApplyStatus(@Param("ids")int[] ids,@Param("tUser_id_o")long tUser_id_o);
	
	//更新拒绝实验室审核状态
	@UpdateProvider(type=SqlFactory.class,method="updateRefuseLabApplyStatus")
	public void updateRefuseLabApplyStatus(@Param("ids")int[] ids,@Param("tUser_id_o")long tUser_id_o);
	
	//撤销实验室审核状态
	@UpdateProvider(type=SqlFactory.class,method="cancelLabApplyStatus")
	public void cancelLabApplyStatus(@Param("ids")int[] ids,@Param("tUser_id_o")long tUser_id_o);
	
	//还回
	@UpdateProvider(type=SqlFactory.class,method="backLabApplyStatus")
	public void backLabApplyStatus(@Param("list")List ids,@Param("tUser_id_o")long tUser_id_o);
	
	//查询借出的实验室
	@Select("select * from tlabapply where auditStatus = '2'")
	public List<LabApply> selectLendLabApply();
	
	@Select("SELECT b.labName as labName,SUM(CASE WHEN a.auditStatus ='2' THEN 1 ELSE 0 END) as lendNum from tlabapply a LEFT JOIN tlab b ON a.tLab_id = b.id AND b.labStatus !='2' GROUP BY a.tLab_id")
	public List<LabUseInfo> selectLabApplyStatus();
	
	@Select("select count(*) from tlabapply where auditStatus = '0'")
	public int checkLabApply();
	
	//查询未审核的实验室申请记录
	@Select("select * from tlabapply where auditStatus = '0'")
	public List<LabApply> selectBookLabApply();
	
	
	/**
	 *根据实验室id获取实验室
	 * @param labId
	 * @return
	 */
	@Select("select * from tlabapply where tlab_id = #{tlab_id}")
	public List<LabApply> selectLabApplyByLabId(@Param("tlab_id") long labId);
	

	
	/**
	 * 根据起始时间查询实验室使用情况
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Select("select * from tlabapply where unix_timestamp(startTime) <= unix_timestamp(#{startTime}) and unix_timestamp(endTime) >= unix_timestamp(#{endTime}) ")
	@Results(value={
		@Result(column="tLab_id",property="lab", one = @One(select="com.org.repository.LabRepository.selectLabById")),
		@Result(column="tLab_id",property="tLab_id")
	})
	public List<LabApply> selectLabApplyByTime(@Param("startTime")Timestamp startTime,@Param("endTime") Timestamp endTime);

	/**
	 * 普通用户新增实验室使用记录
	 * @param labApply
	 * @return
	 */
	@Insert("insert into tlabapply(tlab_id,tuser_id_b,tuser_id_a,startTime,endTime,purpose,auditStatus,tuser_id_o) values("
			+ "#{tLab_id},#{tUser_id_b,jdbcType=BIGINT},"
			+ "#{tUser_id_a,jdbcType=BIGINT},#{startTime},"
			+ "#{endTime},#{purpose,jdbcType=VARCHAR},"
			+ "#{auditStatus,jdbcType=CHAR},#{tUser_id_o,jdbcType=BIGINT}) ")
	public int insertLabApply(LabApply labApply);
	
	/**
	 * 项目组用户新增实验室使用记录
	 * @param labApply
	 * @return
	 */
	@Insert("insert into tlabapply(tUser_id_b,tlab_id,tProject_id,startTime,purpose,auditStatus) values("
			+ "#{tUser_id_b,jdbcType=BIGINT},"
			+ "#{tLab_id},#{tProject_id,jdbcType=BIGINT},#{startTime},"		 
			+ "#{purpose,jdbcType=VARCHAR},"
			+ "#{auditStatus,jdbcType=CHAR}) ")
	public int insertProjectLab(LabApply labApply);
	
	/**
	 * 根据用户id获得实验室
	 * @param userId
	 * @return
	 */
	@Select("select * from tlabapply where tuser_id_b = #{userId}")
	@Results(value={
		@Result(column="tLab_id",property="lab", one = @One(select="com.org.repository.LabRepository.selectLabById")),
		@Result(column="tLab_id",property="tLab_id")
	})
	public List<LabApply> selectLabApplyByUserId(@Param("userId")long userId);
	
	/**
	 * 根据实验室id删除实验室
	 * @param id
	 * @return
	 */
	@Delete("delete from tlabapply where id= #{id}")
	public int deleteLabApplyById(@Param("id") long id);

	/**
	 * 根据项目id删除实验室申请表
	 * @param projectId
	 * @return
	 */
	@Delete("delete from tlabapply where tProject_id = #{projectId}")
	public int deleteLabApplyByProjectId(@Param("projectId") long projectId);
	
	/**
	 * 根据项目id获得实验室申请表
	 * @param projectId
	 * @return
	 */
	@Select("select * from tlabapply where tProject_id = #{projectId}")
	public LabApply selectLabApplyByProjectId(@Param("projectId") long projectId);
	
	/**
	 * 根据userId获得实验室预约状态为通过或者拒绝的数据
	 * @param userId
	 * @return
	 */
	@Select("select count(*) from tlabapply where auditStatus = '1' or auditStatus ='2' and tUser_id_b = #{userId}")
	public int checkLabApplyByUserId(@Param("userId") long userId);
	
	@Update("update tlabapply set endTime = #{endTime} where tProject_id = #{projectId}")
	public int updateLabApplyByProjectId(@Param("endTime") Timestamp endTime,@Param("projectId") long projectId);
	
}
