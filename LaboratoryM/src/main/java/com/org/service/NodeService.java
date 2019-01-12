package com.org.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.pojo.Node;
import com.org.repository.NodeRepository;

@Service
public class NodeService {

	@Resource
	private NodeRepository nodeRepository;
	
	//获取节点
	public List<Node> selectNode(){
		return nodeRepository.selectNode();
	}
	
	//修改信息
	@Transactional
	public String updateNode(Node node) {
		if(nodeRepository.isNodeRepeat(node.getNodeNo())>0) {
			if(nodeRepository.isNodeRepeat2(node.getNodeNo(), node.getId())==0) {
				return "fail";
			}
		}
		nodeRepository.updateNode(node);
		return "success";
	}
	
	//删除节点
	@Transactional
	public void deleteNode(int[] ids) {
		nodeRepository.deleteNode(ids);
	}
	
	//新增节点
	@Transactional
	public String insertNode(Node node) {
		if(nodeRepository.isNodeRepeat(node.getNodeNo())>0) {
			return "fail";
		}
		nodeRepository.insertNode(node);
		return "success";
	}
}
