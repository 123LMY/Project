package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.org.pojo.Lab;
import com.org.util.SqlFactory;
import com.org.vo.LabUseInfo;

public interface LabRepository {

	//实验室使用情况统计
	@SelectProvider(type=SqlFactory.class,method="selectLabUseInfo")
	@Results(value= {
		@Result(column="labName", property="labName"),
		@Result(column="useNum", property="useNum"),
		@Result(column="useTime", property="useTime")
	})
	public List<LabUseInfo> selectLabUseInfo(@Param("startTime")String startTime,@Param("endTime")String endTime);

	//根据实验室名称查询
	@SelectProvider(type=SqlFactory.class,method="selectLabInfo")
	@Results(value= {
		@Result(id=true,column="id",property="id"),
		@Result(column="labName",property="labName"),
		@Result(column="labSite",property="labSite"),
		@Result(column="labFunction",property="labFunction"),
		@Result(column="labFunction",property="labFunction"),
		@Result(column="remark",property="remark"),
		@Result(column="tUser_id_k", property="administrators", one= @One(select="com.org.repository.UserRepository.selectUserById"))
	})
	public List<Lab> selectLabInfo(@Param("labName")String labName);
	
	//删除实验室
	@DeleteProvider(type=SqlFactory.class,method="deleteLab")
	public void deleteLab(@Param("ids")int[] ids);
	
	//修改实验室
	@UpdateProvider(type=SqlFactory.class,method="updateLab")
	public void updateLab(@Param("labName")String labName,@Param("labSite")String labSite,@Param("labFunction")String labFunction,@Param("administrators")String administrators,@Param("id")long id,@Param("labStatus")char labStatus,@Param("tUser_id_o")long tUser_id_o,@Param("remark")String remark);
	
	//新增实验室
	@Insert("insert into tlab (labName,labSite,labFunction,tUser_id_k,labStatus,tUser_id_o,remark) values (#{labName},#{labSite},#{labFunction},#{tUser_id_k},#{labStatus},#{tUser_id_o},#{remark})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void insertLab(Lab lab);
	
	//获取实验室id
	@Select("select id from tlab where labName = #{labName}")
	public int selectLabBylabName(@Param("labName")String labName);
	
	//更新实验室状态
	@UpdateProvider(type=SqlFactory.class,method="updateLabStatus")
	public void updateLabStatus(@Param("ids")int[] ids,@Param("labstatus")char labstatus);
	
	//验证实验室名字是否重复
	@Select("select count(*) from tlab where labName = #{labName}")
	public int isLabNameRepeat(@Param("labName")String labName);
	
	@Select("select count(*) from tlab where labName = #{labName} and id = #{id}")
	public int isLabNameRepeat2(@Param("labName")String labName,@Param("id") long id);
	
	//获取所有实验室的id
	@Select("select id from tlab")
	public List<Object> selectAllLabId();
	
	@Select("select labName from tlab where id = #{id}")
	public String selectLabNameById(@Param("id")long id);
	
	/**
	 * 获得所有的实验室
	 * @return
	 */
	@Select("select * from tlab ")
	@Results(id="labResults",value={
			@Result(id=true,column="id",property="id"),
			@Result(column="labName",property="labName"),
			@Result(column="labSite",property="labSite"),
			@Result(column="labFunction",property="labFunction"),
			@Result(column="labStatus",property="labStatus"),
			@Result(column="tUser_id_k",property="tUser_id_k"),
			@Result(column="tUser_id_o",property="tUser_id_o"),
			@Result(column="remark",property="remark"),
	})
	public List<Lab> selectAllLab();
	

	/**
	 * 获得实验室的数量
	 * @return
	 */
	@Select("select count(*) from tlab")
	public int getCount();
	
	
	/**
	 * 根据关键字获得实验室
	 * @param keyWords
	 * @return
	 */
	@Select({"select * from tlab where concat(labName,labSite) like '%' || 'keyWords' || '%' "})
	@ResultMap("labResults")
	public List<Lab> getLabByKeyWords(@Param("keyWords")String keyWords);
		
	/**
	 * 根据id获得实验室
	 * @param id
	 * @return
	 */
	@Select("select * from tlab where id = #{id}")
	public Lab selectLabById(@Param("id")long id);


}
