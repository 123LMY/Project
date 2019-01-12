package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.SelectProvider;

import com.org.util.SqlFactory;
import com.org.vo.EntranceGuardRecordInfo;

public interface EntranceGuardRecordRepository {

	//获取门禁记录
	@SelectProvider(type=SqlFactory.class,method="selectEntranceGuardRecord")
	@Results(value= {
		@Result(column="id",property="id"),
		@Result(column="userName",property="userName"),
		@Result(column="realName",property="realName"),
		@Result(column="proName",property="proName"),
		@Result(column="inTime",property="inTime"),
		@Result(column="outTime",property="outTime"),
		@Result(column="tUser_id",property="tUser_id")
	})
	public List<EntranceGuardRecordInfo> selectEntranceGuardRecord(@Param("inTime")String inTime,@Param("outTime")String outTime);
	
	//删除门禁记录
	@DeleteProvider(type=SqlFactory.class,method="deleteEntranceGuardRecord")
	public void deleteEntranceGuardRecord(@Param("ids")int[] ids);
	
	//系统消息提示清理
	@Select("SELECT COUNT(*) FROM tentranceguardrecord WHERE DATE_FORMAT( outTime, '%Y%m' ) < DATE_FORMAT( CURDATE( ) , '%Y%m' )")
	public int checkRecord();
	
}
