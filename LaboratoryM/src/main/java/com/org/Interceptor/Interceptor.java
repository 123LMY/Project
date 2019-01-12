package com.org.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.org.pojo.User;

public class Interceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		User user = (User) request.getSession().getAttribute("user");
		User administrators = (User) request.getSession().getAttribute("administrators");
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		String realPath[] = request.getRequestURI().split("\\/");
		for (String rp : realPath) {
			if (user == null && rp.equals("User")) {
				if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
					// 告诉ajax我是重定向
					response.setHeader("REDIRECT", "REDIRECT");
					// 告诉ajax我重定向的路径
					response.setHeader("CONTENTPATH", basePath + "/");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				} else {
					response.sendRedirect(request.getContextPath() + "/");
					return false;
				}
			}
			if (administrators == null && rp.equals("Admin")) {
				if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
					// 告诉ajax我是重定向
					response.setHeader("REDIRECT", "REDIRECT");
					// 告诉ajax我重定向的路径
					response.setHeader("CONTENTPATH", basePath + "/adminlogin");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return false;
				} else {
					response.sendRedirect(request.getContextPath() + "/adminlogin");
					return false;
				}
			}

		}
		return true;
	}
}
