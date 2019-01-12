package com.org.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.org.pojo.User;
import com.org.pojo.UserGrant;
import com.org.service.UserGrantService;
import com.org.vo.GrantInfo;

@Controller
@RequestMapping("/UserGrant")
public class UserGrantController {

	@Resource
	private UserGrantService userGrantService;

	//添加门禁授权
	@RequestMapping("/Admin/addAuthorization")
	@ResponseBody
	public String addAuthorization(@RequestParam("ids[]") int[] ids,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		userGrantService.addAuthorization(ids,administrators.getId());
		return "success";
	}

	//取消门禁授权
	@RequestMapping("/Admin/cancelAuthorization")
	@ResponseBody
	public String cancelAuthorization(@RequestParam("ids[]") int[] ids,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		userGrantService.cancelAuthorization(ids,administrators.getId());
		return "success";
	}

	//新增门禁权限（新增用户时）
	@RequestMapping("/Admin/insertUsersGrant")
	public String insertUsersGrant(List<UserGrant> userGrantList,HttpServletRequest request) {
		User administrators = (User) request.getSession().getAttribute("administrators");
		userGrantService.insertUsersGrant(userGrantList,administrators.getId());
		return "";
	}

	//获取门禁授权信息
	@RequestMapping("/Admin/selectGrantInfo")
	@ResponseBody
	public List selectGrantInfo() {
		List<GrantInfo> grantInfoList = userGrantService.selectGrantInfo();
		return grantInfoList;
	}

	//根据用户信息生成二维码
	@RequestMapping("/Admin/QRcode")
	public void generateQRcode(HttpServletResponse response, String phone, String email,String name) {
		StringBuffer sb = new StringBuffer();
		ServletOutputStream stream = null;
		try {
			int width = 200;
			int height = 200;
			sb.append("姓名："+name+" 电话："+phone+" 邮箱："+email);
			stream = response.getOutputStream();
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix m = writer.encode(new String(sb.toString().getBytes("utf-8"),"ISO-8859-1"), BarcodeFormat.QR_CODE, height, width);
			MatrixToImageWriter.writeToStream(m, "png", stream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.flush();
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
