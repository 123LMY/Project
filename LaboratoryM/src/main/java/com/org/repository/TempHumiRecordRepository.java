package com.org.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.org.pojo.TempHumiRecord;
import com.org.util.SqlFactory;

public interface TempHumiRecordRepository {

	@SelectProvider(type=SqlFactory.class,method="selectTempHumiRecord")
	public List<TempHumiRecord> selectTempHumiRecord(@Param("startTime")String startTime,@Param("endTime")String endTime);

	@Delete("delete from ttemphumirecord where TIMESTAMPDIFF(DAY,takeTime,NOW()) > 2")
	public void deleteTemphumirecord();
	
	
	@Insert("insert into ttemphumirecord(extAddr,sensorType,t_h_value,takeTime) values(#{extAddr},#{sensorType},#{t_h_value},#{takeTime})")
	@Options(useGeneratedKeys=true,keyProperty="id")
	public boolean insertTemphumirecord(@Param("extAddr") String extAddr,@Param("sensorType")char sensorType,@Param("t_h_value")String t_h_value,@Param("takeTime")String takeTime);
	
	@Select("select t_h_value,takeTime from ttemphumirecord where sensorType = '0' order by takeTime")
	public List<TempHumiRecord> selectHumidity();
	
	@Select("select t_h_value,takeTime from ttemphumirecord where sensorType = '1' order by takeTime")
	public List<TempHumiRecord> selectTemperature();
	
}
