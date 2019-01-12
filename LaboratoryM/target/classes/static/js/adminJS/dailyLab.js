if (!window.Index)
	Index = {};
Index.Lab = function() {
};

Index.Lab.prototype = {
	init : function() {
		var that = this;
		that.Labtable();
		that.LabEcharts();
		that.Lablaydate();
		//that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
	},
	LabEcharts : function() {
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var app = {};
		option = null;
		option = {
			tooltip : {
				trigger : 'axis'
			},
			legend : {
				data : [ '使用次数', '累计小时数' ]
			},
			toolbox : {
				show : true
			},
			calculable : true,
			xAxis : [ {
				type : 'category',
				data : ''
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				name : '使用次数',
				type : 'bar',
				data : '',
				markPoint : {
					data : [ {
						type : 'max',
						name : '最大值'
					}, {
						type : 'min',
						name : '最小值'
					} ]
				},
				markLine : {
					data : [ {
						type : 'average',
						name : '平均值'
					} ]
				}
			}, {
				name : '累计小时数',
				type : 'bar',
				data : '',
				markPoint : {
					data : [ {
						type : 'max',
						name : '最大值'
					}, {
						type : 'min',
						name : '最小值'
					} ]
				},
				markLine : {
					data : [ {
						type : 'average',
						name : '平均值'
					} ]
				}
			} ]
		};

		if (option && typeof option === "object") {
			myChart.setOption(option, true);
			var labName = [];
			var useNum = [];
			var useTime = [];
			$.ajax({
				url : "/Lab/Admin/selectLabUseInfo?time=new Date().getTime()",
				type : "get",
				data : {'startTime':$('#LstartTime').val(),'endTime':$('#LendTime').val()},
				contentType : "json",//传出去的参数类型
				dataType : "json",//传来的参数类型
				success : function(data) {
					$("#table_lab").bootstrapTable('load', data);
					for(var i=0;i<data.length;i++){
						labName.push(data[i].labName);
						useNum.push(data[i].useNum);
						useTime.push(data[i].useTime);
					}
					myChart.setOption({        //加载数据图表
	                    xAxis: {
	                        data: labName
	                    },
	                    series: [{
	                        // 根据名字对应到相应的系列
	                        name: '使用次数',
	                        data: useNum
	                    },{
	                    	name: '累计小时数',
	                        data: useTime
	                    }]
	                });
					
				},
				error : function(data) {
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '服务器出错'
	                });
				}

			});
			
		}
	},
	Labtable : function() {
		$('#table_lab').bootstrapTable({
			method : 'get', //请求方式（*）
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
			sortable : false, //是否启用排序
			sortOrder : "asc", //排序方式
			//queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination : "client", //分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], //可供选择的每页的行数（*）
			//search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			//strictSearch: true,
			// showColumns: true,                  //是否显示所有的列
			// showRefresh: true,                  //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			//height : 300, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			//uniqueId: "ID",                     //每一行的唯一标识，一般为主键列

			columns : [ {
				field : 'labName',
				title : '实验室名称'
			}, {
				field : 'useNum',
				title : '使用次数'
			}, {
				field : 'useTime',
				title : '累计使用小时'
			} ]
		});
	},
	Lablaydate : function() {//日历
		//执行一个laydate实例
		var start = laydate.render({
			elem : '#LstartTime', 
			show: true,
			type: 'datetime',
			trigger: 'click',
			done: function (value, date) {
				end.config.min = {
					year: date.year,
					month: date.month - 1,
					date: date.date,
					hours: date.hours + 1,
				};
			}
		});
		//执行一个laydate实例
		var end = laydate.render({
			elem : '#LendTime' ,
			show: true,
			type: 'datetime',
			trigger: 'click'
		});
	},
	loadInfo:function(){
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var labName = [];
		var useNum = [];
		var useTime = [];
		$.ajax({
			url : "/Lab/Admin/selectLabUseInfo?time=new Date().getTime()",
			type : "get",
			data : {'startTime':$('#LstartTime').val(),'endTime':$('#LendTime').val()},
			contentType : "json",//传出去的参数类型
			dataType : "json",//传来的参数类型
			success : function(data) {
				$("#table_lab").bootstrapTable('load', data);
				for(var i=0;i<data.length;i++){
					labName.push(data[i].labName);
					useNum.push(data[i].useNum);
					useTime.push(data[i].useTime);
				}
				myChart.setOption({        //加载数据图表
                    xAxis: {
                        data: labName
                    },
                    series: [{
                        // 根据名字对应到相应的系列
                        name: '使用次数',
                        data: useNum
                    },{
                    	name: '累计小时数',
                        data: useTime
                    }]
                });
			},
			error : function(data) {
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '服务器出错'
                });
			}

		});
	}
};

$(function() {
	var index = new Index.Lab();
	index.init();
});