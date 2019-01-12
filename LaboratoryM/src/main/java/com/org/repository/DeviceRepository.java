package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.org.pojo.Device;
import com.org.util.SqlFactory;
import com.org.vo.DeviceInfo;

public interface DeviceRepository {

	//查询设备使用情况
	@Select("SELECT " + 
			"a.devModel as devModel," + 
			"a.devName as devName," + 
			"COUNT(b.id) as useNum," + 
			"COUNT(b.id) as userNum," + 
			"SUM(TIMESTAMPDIFF(DAY,b.startDate,b.endDate)) as useDate," + 
			"COUNT(c.id) as buyNum," + 
			"SUM(CASE WHEN devStatus = '0' THEN 1 ELSE 0 END) as stock," + 
			"SUM(CASE WHEN devStatus = '4' THEN 1 ELSE 0 END) as damageNum " + 
			"FROM " + 
			"tdevice a, " + 
			"tdeviceapply b," + 
			"tdevicedetail c " + 
			"where " + 
			"c.tDevice_id = a.id and " + 
			"c.id = b.tDeviceDetail_id and a.tDevType_id = #{devType}")
	@Results(id="deviceResult",value= {
		@Result( column="devModel", property="devModel"),
		@Result( column="devName", property="devName"),
		@Result( column="useNum", property="useNum"),
		@Result( column="userNum", property="userNum"),
		@Result( column="useDate", property="useDate"),
		@Result( column="buyNum", property="buyNum"),
		@Result( column="stock", property="stock"),
		@Result( column="damageNum", property="damageNum")	
	})
	public List<DeviceInfo> selectDeviceUseInfo(@Param("devType")long devType);
	
	//按时间查找设备使用情况
	@SelectProvider(type=SqlFactory.class,method = "selectDeviceUseInfoByDateTime")
	@ResultMap("deviceResult")
	public List<DeviceInfo> selectDeviceUseInfoByDateTime(@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("devType")long devType);

	//设备信息查询
	@SelectProvider(type=SqlFactory.class,method="selectDeviceResource")
	public List<DeviceInfo> selectDeviceResource(@Param("deviceName")String deviceName);
	
	//删除设备
	@DeleteProvider(type=SqlFactory.class,method="deleteDevice")
	public void deleteDevice(@Param("ids")int[] ids);
	
	//新增设备
	@Insert("insert into tdevice (devModel,devName,vender,tDevType_id,tUser_id_k,tUser_id_o,remark) "
			+ "values (#{devModel},#{devName},#{vender},#{tDevType_id},#{tUser_id_k},#{tUser_id_o},#{remark})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void insertDevice(Device device);
	
	//根据id查找设备
	@Select("select * from tdevice where id = #{id}")
	@Results(value= {
		@Result(property="custodian",column="tUser_id_k",one= @One(select="com.org.repository.UserRepository.selectUserById"))	
	})
	public Device selectDevById(@Param("id")long id);
	
	//修改设备信息
	@Update("update tdevice set devModel=#{devModel},devName=#{devName},tDevType_id=#{tDevType_id},vender=#{vender},tUser_id_k=#{tUser_id_k},remark=#{remark},tUser_id_o=#{tUser_id_o} where id = #{id}")
	public void updateDevice(Device device);
	
	//验证设备型号和设备名称是否重复
	@Select("select count(*) from tdevice where devName=#{devName} and devModel=#{devModel}")
	public int isDevRepeat(@Param("devName") String devName,@Param("devModel") String devModel);
	
	@Select("select count(*) from tdevice where devName=#{devName} and devModel=#{devModel} and id = #{id}")
	public int isDevRepeat2(@Param("devName") String devName,@Param("devModel") String devModel,@Param("id")long id);
	
	@Select("select * from tdevice ")
	@Results(id="devResults",value={
			@Result(column="id",property="deviceDetail",many=@Many(select="com.org.repository.DeviceDetailRepository.selectDeviceDetailByDeviceId")),
			@Result(column="id",property="id")
	})
	public List<Device> selectAllDevice();
	
	
	
	@Select({"select * from tdevice  where CONCAT(devModel,devName) like CONCAT('%',#{keyWords},'%')"})
	@ResultMap("devResults")
	public List<Device> selectDeviceByKeyWords(@Param("keyWords") String keyWords);
	
	
	@Select("select * from tdevice where id =#{id}")
	@ResultMap("devResults")
	public Device getDeviceById(long id);
}
