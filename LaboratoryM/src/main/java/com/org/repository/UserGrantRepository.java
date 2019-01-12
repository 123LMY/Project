package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

import com.org.pojo.UserGrant;
import com.org.util.SqlFactory;
import com.org.vo.GrantInfo;

public interface UserGrantRepository {

	//授权门禁
	@UpdateProvider(type=SqlFactory.class,method="addAuthorization")
	public void addAuthorization(@Param("ids")int[] ids,@Param("tUser_id_o")long tUser_id_o);
	
	//撤销门禁授权
	@UpdateProvider(type=SqlFactory.class,method="cancelAuthorization")
	public void cancelAuthorization(@Param("ids")int[] ids,@Param("tUser_id_o")long tUser_id_o);
	
	//插入门禁权限
	@InsertProvider(type=SqlFactory.class,method="insertUsersGrant")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void insertUsersGrant(@Param("userGrantList")List<UserGrant> userGrantList,@Param("tUser_id_o")long tUser_id_o);
	
	//门禁授权信息
	@Select("SELECT a.id,a.tUser_id_a,a.noteTopic,a.tLab_id as tLab_id FROM tusergrant a ")
	@Results(id="grantResult",value= {
			@Result( column="id", property="id"),
			@Result( column="tUser_id_a", property="tUser_id_a"),
			@Result( column="noteTopic", property="noteTopic"),
			@Result( column="tLab_id", property="lab_id"),
			@Result( column="proName", property="proName"),
			@Result( column="startTime", property="startTime"),
			@Result( column="endTime", property="endTime"),
			@Result( property="user",column="tUser_id_a",one= @One(select="com.org.repository.UserRepository.selectUserById"))
	})
	public List<GrantInfo> selectGrantInfo();
	
	//根据实验室id删除门禁授权
	@DeleteProvider(type=SqlFactory.class,method="deleteGrantByLabId")
	public void deleteGrantByLabId(@Param("tLab_id")int[] tLab_id);
	
	
	
}
