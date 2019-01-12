if (!window.Index)
	Index = {};
Index.Temp = function() {
};

Index.Temp.prototype = {
	init : function() {
		var that = this;
		var websocket = null;
		
		$("#history").click(function() {
			if ($(".historymsg").css("display") == "none") {
				$(".historymsg").css("display", "block");
			} else {
				$(".historymsg").css("display", "none");
			}
			that.loadInfo();
		});

		//判断当前浏览器是否支持WebSocket
		if('WebSocket' in window) {
			websocket = new WebSocket("ws://172.20.103.5:8080/tempHumiRecord");
		} else {
			alert('当前浏览器 Not support websocket');
		}
		

		//连接发生错误的回调方法
		websocket.onerror = function() {
			//setMessageInnerHTML("WebSocket连接发生错误");
		};

		//连接成功建立的回调方法
		websocket.onopen = function() {
			//发送浏览器标识符
			websocket.send("Browser");
		}

		//接收到消息的回调方法
		websocket.onmessage = function(event) {
			//采集的温湿度数据：0#1:6C EB 6D 1B 00 4B 12 00 &0/48&1/18&2018-12-17 10:02:45
			//实时温湿度数据：0#0:0/48&1/18&2018-12-17 10:02:46
			var arr = event.data.split(':');
			if(arr[0] == "0#1"){//采集的温湿度数据
				var info = arr[1].split("&");
				var Class = "C209";
				var Humidity = info[1].split("/")[1];
				var temp = info[2].split("/")[1];
				var getTime = event.data.split('&')[3];
				$("#class").html(Class);//实验室
				$("#getTime").html(getTime);//获取温湿度的时间
				$("#temp").html(temp+"&#8451;");	
			    $("#Humidity").html(Humidity+"%");
			}else if(arr[0] == "0#0"){//实时的温湿度数据
				var info = arr[1].split("&");
				var Class = "C209";
				var Humidity = info[0].split("/")[1];
				var temp = info[1].split("/")[1];
				var getTime = event.data.split('&')[2];
				$("#class").html(Class);//实验室
				$("#getTime").html(getTime);//获取温湿度的时间
				$("#temp").html(temp+"&#8451;");	
			    $("#Humidity").html(Humidity+"%");
			}else if(arr[0] == "3#0"){//温湿度客户端未启动
				$("#class").html("无服务");//实验室
				$("#getTime").html("无服务");//获取温湿度的时间
				$("#temp").html("无服务");	
			    $("#Humidity").html("无服务");
			}else if(arr[0] == "3#1"){//温湿度客户端未启动
				$("#class").html("缓冲...");//实验室
				$("#getTime").html("缓冲...");//获取温湿度的时间
				$("#temp").html("缓冲...");	
			    $("#Humidity").html("缓冲...");
			}else if(arr[0] == "2#1"){//温湿度采集周期数据
				var nowCycle = arr[1];
				$("#nowCycle").val(nowCycle+" 分钟/次");
			}else if(arr[0] == "2#0"){//修改温湿度采集周期数据失败
				Lobibox.notify('error', {
		            icon: false,
		            title: '提示',
		            msg: '更改失败，温湿度客户端未启动！'
		        });
				sleep(500);
				$("#deviceName").val("");
			}else if(arr[0] == "0#2"){//修改温湿度采集周期数据成功
				Lobibox.notify('success', {
		            icon: false,
		            title: '提示',
		            msg: '更改成功！'
		        });
				var nowCycle = arr[1];
				$("#nowCycle").val(nowCycle+" 分钟/次");
				sleep(500);
				$("#deviceName").val("");
			}else if(arr[0] == "4#0"){//重启温湿度传感器失败
				Lobibox.notify('error', {
		            icon: false,
		            title: '提示',
		            msg: '重启失败，温湿度客户端未启动！'
		        });
			}else if(arr[0] == "0#4"){//重启温湿度传感器成功
				Lobibox.notify('success', {
		            icon: false,
		            title: '提示',
		            msg: '重启温湿度传感器成功！'
		        });
			}
				
		}	
		$('#myStat').circliful();
		$('#myStat2').circliful();
		
		//睡眠方法
		function sleep(numberMillis) {
			var now = new Date();    
			var exitTime = now.getTime() + numberMillis;   
			while (true) { 
				now = new Date();       
				if (now.getTime() > exitTime) 
					return;    
			} 
		}
			
		//连接关闭的回调方法
		websocket.onclose = function() {}
		
		//关闭WebSocket连接
		function closeWebSocket() {
			websocket.close();
		}

		//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
		window.onbeforeunload = function() {
			closeWebSocket();
		}
		
		//重启温湿度传感器
		$("#resetBtn").click(function(){
			websocket.send("1#3:reset");//1#3为标识符，表示这条消息是重启温湿度传感器
		});
		
		//修改采集周期时间
		$("#checkbtn").click(function(){
			var cycle = $("#deviceName").val();//需要修改的采集周期值
		    if(cycle != ''){
		    	websocket.send("1#2:"+cycle);//1#2为标识符，表示这条消息是改变采集周期时间
		    }
		});
		
		

		//限定输入的采集周期只能为数字
		$("#deviceName").keyup(function(){
			var o = $(this).val();
		    var temp_amount = '';
		    if (/[^\d]/.test(o)) { //替换非数字字符
		      var temp_amount = o.replace(/[^\d]/g, '');
		      $(this).val(temp_amount); 
		    }else if (/^[0]*/g.test(o)) {
		      var temp_amount = RegExp.rightContext;
		      $(this).val(temp_amount); 
		    } 
		});

		

	},
	TempEcharts : function(data1, data2) {
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var app = {};
		option = null;
		var dateList1 = data1.map(function(item) {
			return item[0];
		});
		var valueList1 = data1.map(function(item) {
			return item[1];
		});
		var dateList2 = data2.map(function(item) {
			return item[0];
		});
		var valueList2 = data2.map(function(item) {
			return item[1];
		});

		option = {
			// Make gradient line here
			visualMap : [ {
				show : false,
				type : 'continuous',
				seriesIndex : 0,
				min : 0,
				max : 400
			}, {
				show : false,
				type : 'continuous',
				seriesIndex : 1,
				dimension : 0,
				min : 0,
				max : 400
			} ],

			title : [ {
				left : 'center',
				text : '湿度-时间'
			}, {
				top : '55%',
				left : 'center',
				text : '温度-时间'
			} ],
			tooltip : {
				trigger : 'axis'
			},
			xAxis : [ {
				data : dateList1
			}, {
				data : dateList2,
				gridIndex : 1
			} ],
			yAxis : [ {
				splitLine : {
					show : false
				}
			}, {
				splitLine : {
					show : false
				},
				gridIndex : 1
			} ],
			grid : [ {
				bottom : '60%'
			}, {
				top : '60%'
			} ],
			series : [ {
				type : 'line',
				showSymbol : false,
				data : valueList1
			}, {
				type : 'line',
				showSymbol : false,
				data : valueList2,
				xAxisIndex : 1,
				yAxisIndex : 1
			} ]
		};
		;
		if (option && typeof option === "object") {
			myChart.setOption(option, true);
		}
	},
	loadInfo : function() {
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var array1 = [];
		var array2 = [];
		var that = this;
		$
				.ajax({
					url : "/TempHumiRecord/Admin/selectTempHumiRecourd?time=new Date().getTime()",
					type : "get",
					data : {
						'startTime' : '',
						'endTime' : ''
					},
					contentType : "json",
					dataType : "json",
					success : function(data) {
						for (var i = 0; i < data.Humidity.length; i++) {
							var a = [];
							a.push(data.Humidity[i].takeTime);
							a.push(data.Humidity[i].t_h_value);
							array1.push(a);
						}
						for (var i = 0; i < data.Temperature.length; i++) {
							var a = [];
							a.push(data.Temperature[i].takeTime);
							a.push(data.Temperature[i].t_h_value);
							array2.push(a);
						}
						that.TempEcharts(array1, array2);
					},
					error : function(data) {
						alert("服务器出错了！！");
					}

				});
	}
};

$(function() {
	var index = new Index.Temp();
	index.init();
});
