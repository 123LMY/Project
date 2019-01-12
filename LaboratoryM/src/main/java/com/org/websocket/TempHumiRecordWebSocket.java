package com.org.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.org.service.IntelligentDeviceService;
import com.org.service.TempHumiRecordService;

/**
 * 该控制器用于处理温湿度设备<br/>
 * 使用websocket实现，其主要作用是做浏览器与树莓派（智能网关）沟通的桥梁
 */

@ServerEndpoint(value = "/tempHumiRecord")
@Component
public class TempHumiRecordWebSocket {

	public static TempHumiRecordService tempHumiRecordService;
	public static IntelligentDeviceService intelDevService;

	// 连接session
	private Session session;
	// 判断有无树莓派连接
	private static boolean hasRaspi = false;
	// 静态变量，用来标识连接的客户端是浏览器还是树莓派 ，浏览器：1，树莓派：0
	private int pointType;
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	// 静态变量，用来记录当前浏览器在线连接数。应该把它设计成线程安全的。
	private static int onlineBrowserCount = 0;
	// concurrent包的线程安全Set，用来存放每个客户端对应的TempHumiRecordWebSocket对象
	private static CopyOnWriteArraySet<TempHumiRecordWebSocket> tempHumiRecordWebSocket = new CopyOnWriteArraySet<TempHumiRecordWebSocket>();
	// 静态变量，用来记录采集温湿度周期，默认60分钟采集一次
	private static int cycle = 60;

	/**
	 * @描述 当有客户端连接时调用该方法<br/>
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		tempHumiRecordWebSocket.add(this);
		addOnlineCount();
		System.out.println("message: 已连接温湿度服务，当前连接数 ：" + getOnlineCount());
	}

	/**
	 * @描述 接收客户端的消息 这里接收到的messae分为两种类型：<br/>
	 *     一、设备标识message：<br/>
	 *     message="树莓派"标识该连接为树莓派端点（智能网关），pointType值设为0<br/>
	 *     message="浏览器"标识该连接为浏览器端点（设备控制页面），pointType值设为1<br/>
	 *     二、普通消息message：<br/>
	 *     message格式：[pointType]#[info]<br/>
	 *     例如：1#L1,代表将指令L1发送到树莓派端<br/>
	 *     0#L1,代表将指令L1发送到浏览器端<br/>
	 */
	@OnMessage
	public void onMessage(String message) {
		//System.out.println(message);
		if (message.equalsIgnoreCase("Raspberry")) {
			setPointType(0); // 设置树莓派标识值
			hasRaspi = true; // 树莓派已连接
			sendForRaspi("NowTH#");
		} else if (message.equalsIgnoreCase("Browser")) {
			setPointType(1); // 设置浏览器标识值
			addOnlineBrowserCount();
			if (getHasRaspi() == false) {
				this.sendTextMessage("3#0:");// 未连接温湿度客户端
			} else {
				this.sendTextMessage("3#1:");// 已连接温湿度客户端
			}
			this.sendTextMessage("2#1:" + cycle);// 发送历史采集周期给浏览器客户端
			System.out.println("message: 已连接温湿度服务，当前浏览器连接数 ：" + getOnlineBrowserCount());
		} else {
			sendCommand(message); // 发送指令/数据
		}
	}

	/**
	 * @描述 给温湿度客户端发送消息
	 */
	public void sendForRaspi(String message) {
		for (TempHumiRecordWebSocket item : tempHumiRecordWebSocket) {
			if (item.getPointType() == 0) {
				item.sendTextMessage(message);
			}
		}
	}

	/**
	 * @描述 给所有浏览器客户端发送消息
	 */
	public void sendForAllBrowser(String message) {
		for (TempHumiRecordWebSocket item : tempHumiRecordWebSocket) {
			if (item.getPointType() == 1) {
				item.sendTextMessage(message);
			}
		}
	}

	/**
	 * @描述 给温湿度客户端和所有浏览器客户端发送消息
	 */
	public void sendForRaspiAndAllBrowser(String message) {
		for (TempHumiRecordWebSocket item : tempHumiRecordWebSocket) {
			item.sendTextMessage(message);
		}
	}

	/**
	 * @描述 发生错误时调用该方法<br/>
	 */
	@OnError
	public void onError(Throwable t) {
		tempHumiRecordWebSocket.remove(this);
		subOnlineCount();
		if (getPointType() == 1) {
			subOnlineBrowserCount();
		}
		if (getPointType() == 0) {
			hasRaspi = false;
			sendForAllBrowser("温湿度客户端出错！");

		}
		System.out.println("温湿度客户端出错： " + t.getMessage());
	}

	/**
	 * @描述 客户端关闭连接时调用该方法<br/>
	 *     如果树莓派连接关闭，则向所有浏览器客户端发送"树莓派连接关闭"消息
	 */
	@OnClose
	public void onClose(CloseReason cr) {
		tempHumiRecordWebSocket.remove(this);
		subOnlineCount();

		if (getPointType() == 1) {// 浏览器端关闭
			subOnlineBrowserCount();
		}
		if (getPointType() == 0) {// 温湿度客户端已关闭
			hasRaspi = false;
			sendForAllBrowser("温湿度客户端已关闭!");
			System.out.println("温湿度客户端已关闭 ：" + cr.getReasonPhrase());
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
	 * @描述 发送控制指令/数据方法
	 * 
	 *     采集的温湿度数据：0#1:6C EB 6D 1B 00 4B 12 00 &0/48&1/18&2018-12-17 10:02:45
	 *     实时温湿度数据：0#0:0/48&1/18&2018-12-17 10:02:46
	 */
	private void sendCommand(String message) {
		String[] msg = message.split(":");
		if (msg[0].equals("0#0")) {// 0#0表示实时的温湿度数据，发送至浏览器客户端
			sendForAllBrowser(message);
		} else if (msg[0].equals("0#1")) {// 0#1表示需要采集的温湿度数据，保存数据库
			System.out.println("---" + message);
			String[] info = msg[1].split("&");
			String extAddr = info[0];
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			boolean flag = false;
			for (int i = 1; i < (info.length - 1); i++) {
				flag = tempHumiRecordService.insertTemphumirecord(extAddr, info[i].split("/")[0].charAt(0),
						info[i].split("/")[1], df.format(new Date()));
			}
			System.out.println("flag:" + flag);
			// 发送温湿度数据至各个浏览器客户端
			sendForAllBrowser(message);
		} else if (msg[0].equals("1#2")) {// 修改温湿度采集周期
			if (getHasRaspi() == false) {
				this.sendTextMessage("2#0:");// 温湿度客户端未启动
			} else {
				// 发送温湿度采集周期至树莓派
				sendForRaspi("cycle#" + message);
			}
		} else if (msg[0].equals("0#2")) {// 修改温湿度采集周期成功
			// 修改服务器温湿度采集周期
			cycle = Integer.parseInt(msg[1]);
			sendForAllBrowser(message);
		} else if (msg[0].equals("1#3")) { // 重启温湿度传感器
			if (getHasRaspi() == false) {
				this.sendTextMessage("4#0:");// 温湿度客户端未启动
			} else {
				// 发送重启温湿度传感器指令至树莓派
				sendForRaspi("reset#6C EB 6D 1B 00 4B 12 00 ");
			}
		} else if (msg[0].equals("0#3")) {// 重置温湿度传感器成功
			sendForAllBrowser("0#4:");
		}
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
		TempHumiRecordWebSocket.onlineCount++;
	}

	/**
	 * @描述 减少当前连接数
	 */
	public static synchronized void subOnlineCount() {
		if (TempHumiRecordWebSocket.onlineCount > 0) {
			TempHumiRecordWebSocket.onlineCount--;
		}
	}

	/**
	 * @描述 获取当前浏览器连接数
	 */
	public static synchronized int getOnlineBrowserCount() {
		return onlineBrowserCount;
	}

	/**
	 * @描述 获取树莓派是否连接
	 */
	public static synchronized boolean getHasRaspi() {
		return hasRaspi;
	}

	/**
	 * @描述 增加当前浏览器连接数
	 */
	public static synchronized void addOnlineBrowserCount() {
		TempHumiRecordWebSocket.onlineBrowserCount++;
	}

	/**
	 * @描述 减少当前浏览器连接数
	 */
	public static synchronized void subOnlineBrowserCount() {
		if (TempHumiRecordWebSocket.onlineBrowserCount > 0) {
			TempHumiRecordWebSocket.onlineBrowserCount--;
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
