package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.org.pojo.User;
import com.org.util.SqlFactory;

public interface UserRepository {

	//登录验证
	@Select("select * from tuser where userName=#{userName} and userPwd=#{userPwd} and grants = '1' and status = '1'")
	@Results(id="adminResult", value={
			@Result(id=true, column="id", property="id"),
			@Result( column="userName", property="userName"),
			@Result( column="realName", property="realName"),
			@Result( column="phone", property="phone"),
			@Result( column="email", property="email"),
			@Result( column="grants", property="grants"),
			@Result( column="userType", property="userType"),
			@Result( column="fingerMark", property="fingerMark"),
			@Result( column="status",property="status"),
			@Result( column="remark", property="remark")
		})
	public User selectManagerByUsernameandPassword(@Param("userName")String userName,@Param("userPwd")byte[] userPwd);

	//修改信息
	@UpdateProvider(type=SqlFactory.class,method = "updateManagerInfo")
	public void updateManagerInfo (User user);
	
	//根据id查用户
	@Select("select * from tuser where id= #{id}")
	public User selectUserById(@Param("id")long id);
	
	//根据真实姓名查
	@Select("select id from tuser where realName = #{realName} limit 1")
	public int selectUserByRealName(@Param("realName")String realName);
	
	//根据真实姓名模糊查询
	@SelectProvider(type=SqlFactory.class,method = "selectUsersByRealName")
	public List<User> selectUsersByRealName(@Param("realName")String realName);
	
	//新增用户
	@InsertProvider(type=SqlFactory.class,method = "insertUsers")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void insertUsers(@Param("list")List<User> userList);
	
	//修改用户信息
	@UpdateProvider(type=SqlFactory.class,method="updateUser")
	public void updateUser(@Param("userPwd")byte[] userPwd,@Param("status")char status,@Param("userType")char userType,@Param("id")long id,@Param("grants")char grants);
	
	//验证用户名重复
	@Select("select count(*) from tuser where userName = #{userName}")
	public int isUserNameRepeat(@Param("userName")String userName);
	
	//根据项目id查询用户
	@Select("select * from tuser where id in (select tUser_id from tprojectmember where tProject_id = #{tProject_id})")
	public List<User> selectUserByProjectId(@Param("tProject_id")long tProject_id);
	
	//根据项目id查询教师
	@Select("select * from tuser where id in (select tUser_id from tprojectmember where tProject_id = #{tProject_id} and isAdviser = '1')")
	public List<User> selectTeachByProjectId(@Param("tProject_id")long tProject_id);
	
	//验证用户是否存在
	@Select("select count(*) from tuser where realName = #{realName}")
	public int checkUserByRealName(@Param("realName")String realName);
	
	//获取所有用户id和用户权限
	@Select("select id,grants from tuser")
	public List<User> selectUserIdAndGrants();
	
	/**
	 *用户登录验证
	 */
	@Select("select * from tuser where userName=#{userName} and userPwd=#{userPwd}")
	@ResultMap("userResult")
	public User SelectUserByUsernameAndPassword(@Param("userName")String userName,@Param("userPwd")byte[] userPwd);
	
	/**
	 * 新增
	 */
	@Insert("insert into tuser(userName,userPwd)"
			+ " values(#{userName},#{userPwd})")
	public int insertUser(User user);
	
	/**
	 * 获得所有的用户
	 */
	@Select("select * from tuser")
	@Results(id="userResult",value= {
			@Result(id=true,column="id",property="id"),
			@Result(column="userName",property="userName"),
			@Result(column="userPwd",property="userPwd"),
			@Result(column="realName",property="realName"),
			@Result(column="phone",property="phone"),
			@Result(column="email",property="email"),
			@Result(column="grants",property="grants"),
			@Result(column="userType",property="userType"),
			@Result(column="fingerMark",property="fingerMark"),
			@Result(column="remark",property="remark")
	})
	public List<User> getAllUser();
	
	/**	
	 * 根据用户ID查找用户
	 */
	@Select("select * from tuser where id = #{userId}")
	@ResultMap("userResult")
	public User selectUserByUserId(@Param("userId") Integer userId);
	
	/**
	 * 根据用户id修改密码
	*/
	@Update("update tuser set userPwd = #{userPwd} where id = #{userId}")
	public int updatePasswordByUserId(@Param("userId") long userId,@Param("userPwd") byte[] userPwd); 
	
	
	@Select("select userPwd from tuser where id = #{userId}")
	public byte[] checkPasswordById(Integer userId);
	
	
	@Select("select * from tuser where realName = #{userName}")
	@ResultMap("userResult")
	public User getUserByRealName(@Param("userName") String userName);
	
	
	@Select("select * from tuser where realName like CONCAT('%',#{searchName},'%')")
	public List<User> selectUserByName(@Param("searchName") String searchName);
	
	
	@Update("update tuser set userName=#{userName,jdbcType=VARCHAR},"
			+ "realName=#{realName,jdbcType=VARCHAR},"
			+ "phone=#{phone,jdbcType=VARCHAR},"
			+ "email=#{email,jdbcType=VARCHAR},"
			+ "grants=#{grants,jdbcType=CHAR},"
			+ "userType=#{userType,jdbcType=CHAR},"
			+ "fingerMark=#{fingerMark,jdbcType=BINARY},"
			+ "status=#{status,jdbcType=CHAR},"
			+ "remark=#{remark,jdbcType=VARCHAR} "
			+ " where id = #{id} ")
	public int updateUserInfo(User user);
	
	@Select("select * from tuser where userName = #{userName}")
	@ResultMap("userResult")
	public User selectUserByUserName(@Param("userName") String userName);

}
