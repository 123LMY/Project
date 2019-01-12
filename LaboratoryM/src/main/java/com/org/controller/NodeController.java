package com.org.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.org.pojo.Node;
import com.org.pojo.User;
import com.org.service.NodeService;

@Controller
@RequestMapping("/Node")
public class NodeController {

	@Resource
	private NodeService nodeService;
	
	//获取节点信息
	@RequestMapping("/Admin/selectNode")
	@ResponseBody
	public List selectNode() {
		return nodeService.selectNode();
	}
	
	//修改节点信息
	@RequestMapping("/Admin/updateNode")
	@ResponseBody
	public String updateNode(@RequestParam long id,@RequestParam String nodeNo,@RequestParam String nodeName,@RequestParam String ipAddress,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Node node = new Node();
		node.setId(id);
		node.setNodeNo(nodeNo);
		node.setNodeName(nodeName);
		node.setIpAddress(ipAddress);
		node.settUser_id(((User)(session.getAttribute("administrators"))).getId());
		return nodeService.updateNode(node);
	}
	
	//删除节点
	@RequestMapping("/Admin/deleteNode")
	@ResponseBody
	public String deleteNode(@RequestParam("ids[]") int[] ids) {
		nodeService.deleteNode(ids);
		return "success";
	}
	
	//新增节点
	@RequestMapping("/Admin/insertNode")
	@ResponseBody
	public String insertNode(@RequestParam String nodeNo,@RequestParam String nodeName,@RequestParam String ipAddress,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Node node = new Node();
		node.setNodeNo(nodeNo);
		node.setNodeName(nodeName);
		node.setIpAddress(ipAddress);
		node.settUser_id(((User)(session.getAttribute("administrators"))).getId());
		return nodeService.insertNode(node);
	}
}
