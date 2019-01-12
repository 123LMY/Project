if (!window.Index)
	var websocket = null; 
	Index = {};

Index.Switch = function() {};

Index.Switch.prototype = {
    
	init : function() {
		var that = this;
		that.Switchtable();
		
	},
	Switchtable : function(){
		//判断当前浏览器是否支持WebSocket
		if('WebSocket' in window) {
			websocket = new WebSocket("ws://172.20.103.5:8080/intelligentDevice");
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
			var arr = event.data.split(';');
			//alert(event.data);
			if(arr[0] == 0){//0表示智能开关信息
				$('#table_Switch').bootstrapTable("load",JSON.parse(arr[1]));
				that.mySwitch();
			/*	mystatus=JSON.parse(arr[1]).status;
				console.log(mystatus);*/
			}else if(arr[0] == "1#0"){//10表示智能开关客户端未启动
				var info = arr[2].split('/');
				if(info[2]=="开"){
					$("input[name='"+info[0]+"']").get(0).checked=true; 
					
				}else if(info[2]=="关"){
					$("input[name='"+info[0]+"']").get(1).checked=true; 
				}
				
				Lobibox.notify('error', {
		            icon: false,
		            title: '提示',
		            msg: '操作失败，智能开关客户端未启动！'
		        });
			}else if(arr[0] == "0#1"){//11表示智能开关操作成功
				//var index = arr[1]; 
				//$('#table_Switch').bootstrapTable('updateRow', {index: index, row: JSON.parse(arr[2])});
				$('#table_Switch').bootstrapTable("load",JSON.parse(arr[1]));
				that.mySwitch();
				Lobibox.notify('success', {
		            icon: false,
		            title: '提示',
		            msg: '操作成功！'
		        });
			}
			
		}	
		
		//连接关闭的回调方法
		websocket.onclose = function() {}
		
		//关闭WebSocket连接
		function closeWebSocket() {
			websocket.close();
		}
		
		var that = this;
		$('#table_Switch').bootstrapTable({
			method : 'get', //请求方式（*）
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
			sortable : false, //是否启用排序
			sortOrder : "asc", //排序方式
			sidePagination : "client", //分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行

			columns : [  {
				field : 'id',
				title : 'ID'
			},{
				field : 'node',
				title : '名称'
			}, {
				field : 'status',
				title : '状态'
			}, {
				field : 'useTime',
				title : '操作时间'
			}, {
				field : 'extAddr',
				title : '设备地址'
			},{
				field : 'useName',
				title : '操作人'
			}, {
				field : 'button',
				title : '操作',
				events: that.mySwitch(),
				formatter:function (value, row, index) {
					//return '<input type="checkbox" checked  data-on-text="YES" data-off-text="NO"/> ';
					if(row.status=="关"){
						return '　<input type="radio" id="on" class="switchbutton" name ="'+row.extAddr+'" value="'+row.extAddr+'/'+row.node+'/'+row.status+'/'+index+'" >开'
					    + '　<input type="radio" id="off" checked class="switchbutton" name ="'+row.extAddr+'"  value="'+row.extAddr+'/'+row.node+'/'+row.status+'/'+index+'">关'
					}else if(row.status=="开"){
						return '　<input type="radio" id="on" checked class="switchbutton" name ="'+row.extAddr+'" value="'+row.extAddr+'/'+row.node+'/'+row.status+'/'+index+'" >开'
					    + '　<input type="radio" id="off"  class="switchbutton" name ="'+row.extAddr+'" value="'+row.extAddr+'/'+row.node+'/'+row.status+'/'+index+'">关'
					}
				}
				
			},],
			
		});
	},
	mySwitch:function(){
    	$(":radio").click(function(){
    		//alert($('input[name="1"]').filter(':checked').val());
    		//alert($(this).val());
    		websocket.send("1#1;"+$(this).val());
		});	 
	}
	
	/*mySwitch:function(){*/
      /*  $('.switchbutton').bootstrapSwitch({
        	offText: "OFF",
            onText: "ON",
            onColor: "success",
            offColor: "info",
            size: "small",
            
            onSwitchChange: function (event, state) {
                if (state == true) {
                	console.log(this);
                	var str=this.value;
                	websocket.send(str);
                } else {
                	console.log(this);
                	var str=this.value;
                }
            }
            
        });*/
         /* if ($('.switchbutton').val().split('/')[2]=="关") {
        	   console.log("关");
    	        $('#on').attr(" checked","ture");
    	        
           }else if ($('.switchbutton').val().split('/')[2]=="开") {
        	   $('#off').attr(" checked","false");
           }*/
           
        //console.log(JSON.stringify(this));
        //alert(JSON.stringify(this));
       /* if(this.status == "关"){
        	$('[name="status"]').bootstrapSwitch('toggleState');
            $('[name="status"]').bootstrapSwitch('setState',false);
        }else if(this.status == "开"){
        	$('[name="status"]').bootstrapSwitch('toggleState');
            $('[name="status"]').bootstrapSwitch('setState',true);
        }*/
        
/*    }*/
	
};

$(function() {
	var index = new Index.Switch();
	index.init();
});