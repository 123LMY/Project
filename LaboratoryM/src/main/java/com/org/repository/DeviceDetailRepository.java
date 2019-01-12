package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.org.pojo.DeviceDetail;
import com.org.util.Pager;
import com.org.util.SqlFactory;
import com.org.vo.DeviceDetailInfo;

public interface DeviceDetailRepository {

	@Select("select * from tdevicedetail limit #{startResults},#{pageSize}")
	@Results(id = "deviceResult", value = { @Result(id = true, column = "id", property = "id"),
			@Result(column = "devSn", property = "devSn"), @Result(column = "rfidNo", property = "rfidNo"),
			@Result(column = "inDate", property = "inDate"), @Result(column = "devStatus", property = "devStatus"),
			@Result(column = "tDevice_id", property = "tDevice_id"), @Result(column = "tLab_id", property = "tLab_id"),
			@Result(column = "tUser_id_a", property = "tUser_id_a"),
			@Result(column = "tUser_id_o", property = "tUser_id_o"), @Result(column = "remark", property = "remark"), })
	public List<DeviceDetail> getAllDeviceDetail(Pager pager);

	// 删除子设备
	@DeleteProvider(type = SqlFactory.class, method = "deleteDeviceDetail")
	public void deleteDeviceDetail(@Param("ids") int[] ids);

	// 新增子设备
	@Insert("insert into tdevicedetail (devSn,rfidNo,inDate,devStatus,tDevice_id,tLab_id,tUser_id_o,remark,cabNo) "
			+ "values (#{devSn},#{rfidNo},#{inDate},#{devStatus},#{tDevice_id},#{tLab_id},#{tUser_id_o},#{remark},#{cabNo})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void insertDeviceDetail(DeviceDetail deviceDetail);

	// 获取设备使用信息
	@Select("SELECT a.id as id," + "a.devSn as devSn," + "a.rfidNo as rfidNo," + "a.inDate as inDate,"
			+ "a.devStatus as devStatus," + "a.remark as remark, " + "b.realName as usingName,"
			+ "c.labName as labName, " + "a.cabNo as cabNo " + "FROM " + "tdevicedetail a "
			+ "LEFT JOIN tuser b ON b.id = (select tUser_id_a from tdeviceapply WHERE tDeviceDetail_id = a.id AND startDate<NOW() AND endDate>NOW() LIMIT 1) "
			+ "LEFT JOIN tlab c ON a.tLab_id = c.id " + "WHERE a.tDevice_id = #{devId}")
	public List<DeviceDetailInfo> selectDevDetailInfo(@Param("devId") long id);

	// 根据id批量删除子设备
	@DeleteProvider(type = SqlFactory.class, method = "deleteDevDetailById")
	public void deleteDevDetailById(@Param("ids") int[] ids);

	// 修改子设备信息
	@Update("update tdevicedetail set devSn=#{devSn},rfidNo=#{rfidNo},devStatus=#{devStatus},remark=#{remark},tLab_id=#{tLab_id},tUser_id_o=#{tUser_id_o},cabNo=#{cabNo} where id = #{id}")
	public void updateDevDetail(DeviceDetail deviceDetail);

	// 改变子设备状态
	@UpdateProvider(type = SqlFactory.class, method = "updateDevDetailStatus")
	public void updateDevDetailStatus(@Param("ids") int[] ids, @Param("devdetailstatus") char devdetailstatus,
			@Param("tUser_id_o") long tUser_id_o);

	// 验证设备序号是否重复
	@Select("select count(*) from tdevicedetail where devSn = #{devSn}")
	public int isDevDetailRepeat(@Param("devSn") String devSn);

	@Select("select count(*) from tdevicedetail where devSn = #{devSn} and id = #{id}")
	public int isDevDetailRepeat2(@Param("devSn") String devSn,@Param("id")long id);

	@Select("select * from tdevicedetail where tdevice_id = #{deviceId} ")
	public List<DeviceDetail> selectDeviceDetailByDeviceId(@Param("deviceId") long deviceId);

	@Select({ "select * from tdevicedetail where concat(devSn,rfidNo) like '%' || 'keyWords' || '%' " })
	@ResultMap("deviceResult")
	public List<DeviceDetail> getDeviceDetailByKeyWords(@Param("keyWords") String keyWords);

	@Select("select * from tdevicedetail where id = #{id}")
	@Results(value = {
			@Result(column = "tDevice_id", property = "device", one = @One(select = "com.org.repository.DeviceRepository.getDeviceById")) })
	public DeviceDetail selectDeviceDetailById(@Param("id") long id);

}
