package com.org.repository;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.org.pojo.Node;
import com.org.util.SqlFactory;

public interface NodeRepository {

	//查询节点
	@Select("select id,nodeNo,nodeName,ipAddress from tnode")
	public List<Node> selectNode();
	
	//更新节点
	@Update("update tnode set nodeNo = #{nodeNo},nodeName = #{nodeName},ipAddress = #{ipAddress} where id = #{id}")
	public void updateNode(Node node);
	
	//删除节点
	@DeleteProvider(type=SqlFactory.class,method="deleteNode")
	public void deleteNode(@Param("ids")int[] ids);
	
	//新增节点
	@Insert("insert into tnode (nodeNo,nodeName,ipAddress,tUser_id)values(#{nodeNo},#{nodeName},#{ipAddress},#{tUser_id})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void insertNode(Node node);
	
	//验证节点是否存在
	@Select("select count(*) from tnode where nodeNo=#{nodeNo}")
	public int isNodeRepeat(@Param("nodeNo")String nodeNo);
	
	@Select("select count(*) from tnode where nodeNo=#{nodeNo} and id = #{id}")
	public int isNodeRepeat2(@Param("nodeNo")String nodeNo,@Param("id")long id);
	
}
