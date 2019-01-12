package com.org;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.org.websocket.IntelligentDeviceWebSocket;
import com.org.websocket.TempHumiRecordWebSocket;
import com.org.service.IntelligentDeviceService;
import com.org.service.TempHumiRecordService;
import com.org.service.UserService;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.org.repository")
@Configuration
public class App extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override // 为了打包springboot项目
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个数据大小
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}

	/**
	 * ServerEndpointExporter 用于扫描和注册所有携带 ServerEndPoint 注解的实例， 若部署到外部容器 则无需提供此类。
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	/**
	 * 因 SpringBoot WebSocket 对每个客户端连接都会创建一个 WebSocketServer（@ServerEndpoint 注解对应的）
	 * 对象，Bean 注入操作会被直接略过，因而手动注入一个全局变量
	 *
	 * @param IntelligentDeviceService
	 */
	@Autowired
	public void setIntelligentDeviceService(IntelligentDeviceService intelDevService, UserService userService) {
		IntelligentDeviceWebSocket.intelDevService = intelDevService;
		IntelligentDeviceWebSocket.userService = userService;
	}

	@Autowired
	public void setTempHumiRecordService(TempHumiRecordService tempHumiRecordService) {
		TempHumiRecordWebSocket.tempHumiRecordService = tempHumiRecordService;
	}
}
