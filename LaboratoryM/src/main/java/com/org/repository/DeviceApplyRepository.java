package com.org.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.org.pojo.DeviceApply;
import com.org.util.SqlFactory;
import com.org.vo.DeviceApplyInfo;

public interface DeviceApplyRepository {
	
	/*新增deviceApply*/
	@Insert("insert into tdeviceapply(tDevice_id,tUser_id_b,tUser_id_a,startDate,endDate,purpose,auditStatus)"
			+ "values(#{tDevice_id},#{tUser_id_b},#{tUser_id_a},#{startDate},#{endDate},#{purpose},#{auditStatus})")
	public int insertDeviceApply(DeviceApply deviceApply);
	
	/*根据startDate和endDate查询处于startDate~endDate时段内的设备信息*/
	@Select("select * from tdeviceapply WHERE startDate < #{startDate} and endDate > #{endDate} ")
	public List<DeviceApply> selectDeviceApplyByTime(@Param("startDate") Timestamp startDate,@Param("endDate") Timestamp endDate);

	//删除设备申请
	@DeleteProvider(type=SqlFactory.class,method="deleteDevApply")
	public void deleteDevApply(@Param("ids")int[] ids);
	
	//根据子设备id删除设备申请记录
	@DeleteProvider(type=SqlFactory.class,method="deleteDevApplyBytDeviceDetail_id")
	public void deleteDevApplyBytDeviceDetail_id(@Param("ids")int[] ids);
	
	//查找设备申请记录
	@SelectProvider(type=SqlFactory.class,method="selectDevApplyInfo")
	public List<DeviceApplyInfo> selectDevApplyInfo(@Param("auditStatus")char auditStatus,@Param("name")String name);
	
	//修改设备申请状态
	@UpdateProvider(type=SqlFactory.class,method="updateDevApplyStatus")
	public void updateDevApplyStatus(@Param("ids")int[] ids,@Param("devapplystatus")char devapplystatus,@Param("tUser_id_o")long tUser_id_o);
	
	//系统消息检测是否有需要处理的设备申请
	@Select("select count(*) from tdeviceapply where auditStatus = '0'")
	public int checkDevApply();
	
	//查找通过的设备申请记录
	@Select("select * from tdeviceapply where auditStatus = '2'")
	public List<DeviceApply> selectPassDevDetail();
	
	//查找还没审核的设备申请
	@Select("select * from tdeviceapply where auditStatus = '0'")
	public List<DeviceApply> selectOrderDevDetail();
	
	//查找已预约的设备申请记录
	@Select("select * from tdeviceapply where auditStatus = '2' and tDeviceDetail_id IN (select id from tdevicedetail where devStatus = '5')")
	public List<DeviceApply> selectBookedDevDetail();
	
	@Insert("insert into tdeviceapply(tDeviceDetail_id,tUser_id_b,tUser_id_a,tUser_id_o,startDate,endDate,purpose,auditStatus)"
			+ "values(#{tDeviceDetail_id,jdbcType=BIGINT},#{tUser_id_b,jdbcType=BIGINT},"
			+ "#{tUser_id_a,jdbcType=BIGINT},#{tUser_id_o,jdbcType=BIGINT},#{startDate,jdbcType=DATE},#{endDate,jdbcType=DATE},"
			+ "#{purpose,jdbcType=VARCHAR},#{auditStatus,jdbcType=CHAR})")
	public int insertDeviceApplys(DeviceApply deviceApply);

	@Select("select * from tdeviceapply where tuser_id_b = #{tUser_id_b}")
	@Results(value={
			@Result(column="tDeviceDetail_id", property="deviceDetail",one=@One(select="com.org.repository.DeviceDetailRepository.selectDeviceDetailById"))
	})
	public List<DeviceApply> selectDeviceApplyByUserId(@Param("tUser_id_b")long tUser_id_b);
	
	@Delete("delete from tdeviceApply where id = #{id}")
	public int deleteDeviceApplyById(long id);
	

	/**
	 * 根据userId获得设备预约状态为通过或者拒绝的数据
	 * @param userId
	 * @return
	 */
	@Select("select count(*) from tdeviceapply where auditStatus = '1' or auditStatus ='2' and tUser_id_b = #{userId}")
	public int checkDeviceApplyByUserId(@Param("userId") long userId);
	
	
	@Delete("delete from tdeviceApply where tdevicedetail_id = #{deviceDetailId} and tUser_id_b = #{userId}")
	public int deleteByUserIdAndDeviceDetailId(@Param("deviceDetailId")long deviceDetailId,@Param("userId") long userId);
	
}
