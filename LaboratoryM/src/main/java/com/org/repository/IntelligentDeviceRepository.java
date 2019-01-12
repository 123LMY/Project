package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.org.pojo.IntelligentDevice;

public interface IntelligentDeviceRepository {

	@Select("select * from tintelligentdevice")
	public List<IntelligentDevice> getAllIntelDevList();

	@Select("select * from tintelligentdevice where extAddr = #{extAddr}")
	public IntelligentDevice getIntelDevListById(@Param("extAddr") String extAddr);

	@Update("update tintelligentdevice set idevStatus=#{idevStatus},takeTime=#{takeTime},tUser_id=#{tUser_id} where extAddr=#{extAddr}")
	public boolean updateIntelDev(@Param("extAddr") String extAddr, @Param("idevStatus") char idevStatus,
			@Param("takeTime") String takeTime, @Param("tUser_id") long tUser_id);

	@Delete("delete from tintelligentdevice where id = #{id}")
	public void deleteIntelligentdevice(@Param("id") long id);

	@Insert("insert into tintelligentdevice (idevName,idevStatus,dataValue,takeTime,tUser_id) values (#{idevName},#{idevStatus},#{dataValue},#{takeTime},#{tUser_id})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public void insertIntelligentdevice(IntelligentDevice intelligentdevice);

}
