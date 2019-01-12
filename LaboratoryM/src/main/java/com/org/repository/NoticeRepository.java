package com.org.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.org.pojo.Notice;
import com.org.util.SqlFactory;

public interface NoticeRepository {

	// 查询公告
	@Select("select * from tnotice order by publishDate desc")
	@Results(id = "noticeResult", value = { @Result(id = true, column = "id", property = "id"),
			@Result(column = "typeName", property = "typeName"),
			@Result(column = "description", property = "description"),
			@Result(column = "tUser_id_p", property = "tUser_id_p"),
			@Result(column = "tUser_id_o", property = "tUser_id_o"),
			@Result(column = "publishDate", property = "publishDate"),
			@Result(property = "publishers", column = "tUser_id_p", one = @One(select = "com.org.repository.UserRepository.selectUserById")),
			@Result(property = "operator", column = "tUser_id_o", one = @One(select = "com.org.repository.UserRepository.selectUserById")) })
	public List<Notice> selectNotice();

	// 根据id查询公告
	@Select("select * from tnotice where id = #{id}")
	public Notice selectNoticeById(@Param("id") int id);

	// 更新公告
	@Update("update tnotice set typeName = #{typeName},description = #{description},"
			+ "tUser_id_o = #{tUser_id_o},publishDate = #{publishDate} where id = #{id}")
	public void updateNotice(Notice notice);

	// 删除公告
	@DeleteProvider(type = SqlFactory.class, method = "deleteNotices")
	public void deleteNotices(@Param("ids") int[] ids);

	// 新增公告
	@Insert("insert into tnotice (typeName,description,tUser_id_p,publishDate,tUser_id_o) "
			+ "values (#{typeName},#{description},#{tUser_id_p},#{publishDate},#{tUser_id_o})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void insertNotice(Notice notice);

	// 获取前十条公告
	@Select("select * from tnotice order by publishDate desc limit 10")
	@ResultMap("noticeResult")
	public List<Notice> selectNoticelimit();

	// 分页获得公告
	@Select("select * from tnotice order by publishDate desc limit #{start},#{pageSize}")
	@ResultMap("noticeResult")
	public List<Notice> selectNoticeByPage(Map<String, Object> map);

	// 获得所有公告的条数
	@Select("select count(*) from tnotice ")
	public int selectNoticeCount();
}
