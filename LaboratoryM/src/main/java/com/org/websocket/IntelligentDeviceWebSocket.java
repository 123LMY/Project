package com.org.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.org.pojo.Intelligent;
import com.org.pojo.IntelligentDevice;
import com.org.pojo.User;
import com.org.service.IntelligentDeviceService;
import com.org.service.UserService;

/**
 * 该控制器用于处理智能控制设备开关<br/>
 * 使用websocket实现，其主要作用是做浏览器与树莓派（智能网关）沟通的桥梁
 */
@ServerEndpoint(value = "/intelligentDevice")
@Component
public class IntelligentDeviceWebSocket {

	public static IntelligentDeviceService intelDevService;
	public static UserService userService;

	// 连接session
	private Session session;
	// 判断有无树莓派连接
	private static boolean hasRaspi = false;
	// 静态变量，用来标识连接的客户端是浏览器还是树莓派 ，浏览器：1，树莓派：0
	private int pointType;
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的SwitchWebSokcket对象
	private static CopyOnWriteArraySet<IntelligentDeviceWebSocket> intelligentDeviceWebSocket = new CopyOnWriteArraySet<IntelligentDeviceWebSocket>();

	/**
	 * @描述 当有客户端连接时调用该方法<br/>
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		intelligentDeviceWebSocket.add(this);
		addOnlineCount();
		System.out.println("message: 已连接开关服务，当前连接数 ：" + getOnlineCount());
	}

	/**
	 * @描述 接收客户端的消息 这里接收到的messae分为两种类型：<br/>
	 *     一、设备标识message：<br/>
	 *     message="树莓派"标识该连接为树莓派端点（智能网关），pointType值设为0<br/>
	 *     message="浏览器"标识该连接为浏览器端点（设备控制页面），pointType值设为1<br/>
	 *     二、控制指令message：<br/>
	 *     message格式：[pointType]#[idevName]/[idevStatus]#[userId]<br/>
	 *     例如：1#L1/0#1,代表将指令L1/0发送到树莓派端,操作人Id=1；<br/>
	 *     0#L1/0#1,代表将指令L1/0发送到浏览器端,操作人Id=1；<br/>
	 */
	@OnMessage
	public void onMessage(String message) {
		System.out.println(message);
		if (message.equalsIgnoreCase("Raspberry")) {
			setPointType(0); // 设置树莓派标识值
			hasRaspi = true; // 树莓派已连接
			syncDevStatus(); // 从数据库同步设备状态到树莓派（智能开关客户端）
		} else if (message.equalsIgnoreCase("Browser")) {
			setPointType(1); // 设置浏览器标识值
			String list = getAllIntelDevList(); // 获得所有设备列表
			sendTextMessage("0;" + list); // 同步设备到浏览器，0作为标识符
		} else {
			sendCommand(message); // 发送指令
		}
	}

	/**
	 * @描述 发生错误时调用该方法<br/>
	 */
	@OnError
	public void onError(Throwable t) {
		intelligentDeviceWebSocket.remove(this);
		subOnlineCount();

		if (getPointType() == 0) {
			hasRaspi = false;
			sendForAllBrowser("开关客户端出错！");
		}
		System.out.println("开关服务出错： " + t.getMessage());
	}

	/**
	 * @描述 客户端关闭连接时调用该方法<br/>
	 *     如果树莓派连接关闭，则向所有浏览器客户端发送"树莓派连接关闭"消息
	 */
	@OnClose
	public void onClose(CloseReason cr) {
		intelligentDeviceWebSocket.remove(this);
		subOnlineCount();
		if (getPointType() == 1) {// 浏览器端关闭
		}
		if (getPointType() == 0) {// 温湿度客户端已关闭
			hasRaspi = false;
			sendForAllBrowser("开关客户端已关闭!");
			System.out.println("开关客户端已关闭 ：" + cr.getReasonPhrase());
		}

	}

	/**
	 * @描述 数据发送方法，以文本格式发送
	 */
	private void sendTextMessage(String data) {
		try {
			if (session.isOpen()) {
				session.getBasicRemote().sendText(data);
			}
		} catch (IOException e) {
			System.out.println("发送文本出错   : " + e.getMessage());
		}
	}

	/**
	 * @描述 给所有浏览器客户端发送消息
	 */
	public void sendForAllBrowser(String message) {
		for (IntelligentDeviceWebSocket item : intelligentDeviceWebSocket) {
			if (item.getPointType() == 1) {
				item.sendTextMessage(message);
			}
		}
	}

	/**
	 * @描述 给智能开关客户端发送消息
	 */
	public void sendForRaspi(String message) {
		for (IntelligentDeviceWebSocket item : intelligentDeviceWebSocket) {
			if (item.getPointType() == 0) {
				item.sendTextMessage(message);
			}
		}
	}

	/**
	 * @描述 从数据库同步设备状态到树莓派<br/>
	 *     首先从数据库读取所有智能开关设备信息<br/>
	 *     再睡眠2秒，为了等待树莓端做设备初始化完毕<br/>
	 *     最后将设备状态发送到树莓派，数据格式：[devName]/[devStatus],例如：L1/0<br/>
	 */
	private void syncDevStatus() {
		List<IntelligentDevice> allIntellDevList = intelDevService.getAllIntelDevList();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (IntelligentDevice item : allIntellDevList) {
			String info = item.getExtAddr() + " #" + item.getIdevStatus();
			sendForRaspi(info);
		}
	}

	/**
	 * @描述 发送控制指令方法
	 */
	private void sendCommand(String message) {
		String[] msg = message.split(";");
		if (msg[0].equals("1#1")) {// 1#1表示接收到的是更改开关状态消息
			if (getHasRaspi() == false) {
				this.sendTextMessage("1#0;" + message);
			} else if (getHasRaspi() == true) {
				// message:1#1;18 F7 6D 1B 00 4B 12 00/L1/关/0
				// 分别：1#1标识 18、 F7 6D 1B 00 4B 12 00开关地址、 L1-开关名称、 0-行
				String[] str = msg[1].split("/");
				String extAddr = str[0] + " ";
				char idevStatus = '0';
				if (str[2].equals("开")) {
					idevStatus = '1';// 关
				} else if (str[2].equals("关")) {
					idevStatus = '0';// 开
				}
				String info = extAddr + "#" + idevStatus;
				// System.out.println("0000000000000000");
				sendForRaspi(info);
			}
		} else if (msg[0].equals("0#0")) {// 0#0表示控制开关之后得到的信息
			updateAndSendinfo(message);
		}
	}

	// 修改数据库开关状态信息并将最新开关状态信息发送至各个浏览器
	public void updateAndSendinfo(String message) {
		// 0#0;18 F7 6D 1B 00 4B 12 00 &open success
		String[] msg = message.split(";");
		String[] str = msg[1].split("&");
		String extAddr = str[0];
		String status = str[1];
		char idevStatus = '0';
		if (status.equals("open success")) {
			idevStatus = '0'; // 开
		} else if (status.equals("close success")) {
			idevStatus = '1'; // 关
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// 更新数据库智能开关信息
		boolean flag = intelDevService.updateIntelDev(extAddr, idevStatus, df.format(new Date()), 1);
		System.out.println("flag:" + flag);
		if (flag) {// 更新数据库智能开关信息成功
			String list = getAllIntelDevList(); // 获得所有设备列表
			sendForAllBrowser("0#1;" + list); // 同步设备到浏览器，0作为标识符
			// 从数据库获取最新智能开关信息
			// IntelligentDevice intelligentDevice =
			// intelDevService.getIntelDevListById(extAddr);
			// Intelligent intelligent = new Intelligent();
			// intelligent.setId(intelligentDevice.getId());
			// intelligent.setExtAddr(intelligentDevice.getExtAddr());
			// intelligent.setNode(intelligentDevice.getIdevName());
			// if (intelligentDevice.getIdevStatus() == '1') {
			// intelligent.setStatus("关");
			// } else if (intelligentDevice.getIdevStatus() == '0') {
			// intelligent.setStatus("开");
			// }
			// intelligent.setUseTime(intelligentDevice.getTakeTime().toString());
			// User user = userService.selectUserByUserId(intelligentDevice.gettUser_id());
			// intelligent.setUseName(user.getRealName());
			// Gson gson = new Gson();
			// String jsonObject = gson.toJson(intelligent);
			// // 发送最新智能开关信息给所有浏览器//str[3]第几行
			// sendForAllBrowser("11;" + str[3] + ";" + jsonObject);
		}

	}

	/**
	 * @描述 从数据库读取所有设备列表
	 * @return String
	 */
	public String getAllIntelDevList() {
		Intelligent intelligent = null;// 此类包含的开关信息是需要发送至开关浏览器端的
		List<Intelligent> intelligentList = new ArrayList<Intelligent>();
		// 从数据库获取所有开关列表信息
		List<IntelligentDevice> allIntellDevList = intelDevService.getAllIntelDevList();
		for (IntelligentDevice item : allIntellDevList) {
			intelligent = new Intelligent();
			intelligent.setId(item.getId());
			intelligent.setExtAddr(item.getExtAddr());
			intelligent.setNode(item.getIdevName());
			if (item.getIdevStatus() == '1') {
				intelligent.setStatus("关");
			} else if (item.getIdevStatus() == '0') {
				intelligent.setStatus("开");
			}
			intelligent.setUseTime(item.getTakeTime().toString());
			User user = userService.selectUserByUserId(item.gettUser_id());
			intelligent.setUseName(user.getRealName());
			intelligentList.add(intelligent);
		}
		Gson gson = new Gson();
		String jsonObject = gson.toJson(intelligentList);
		return jsonObject;
	}

	/**
	 * @描述 获取树莓派是否连接
	 */
	public static synchronized boolean getHasRaspi() {
		return hasRaspi;
	}

	/**
	 * @描述 获取当前连接数
	 */
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	/**
	 * @描述 增加当前连接数
	 */
	public static synchronized void addOnlineCount() {
		IntelligentDeviceWebSocket.onlineCount++;
	}

	/**
	 * @描述 减少当前连接数
	 */
	public static synchronized void subOnlineCount() {
		if (IntelligentDeviceWebSocket.onlineCount > 0) {
			IntelligentDeviceWebSocket.onlineCount--;
		}
	}

	/**
	 * @描述 获得当前连接设备标识<br/>
	 *     0代表树莓派（智能网关）,1代表浏览器端点（设备控制页面）
	 */
	public synchronized int getPointType() {
		return pointType;
	}

	/**
	 * @描述 设置当前连接设备标识
	 * @param type
	 *            0代表树莓派（智能网关）,1代表浏览器端点（设备控制页面）
	 */
	public synchronized void setPointType(int type) {
		pointType = type;
	}

}
