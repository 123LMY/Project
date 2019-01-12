if (!window.Index)
	Index = {};
Index.Equip = function() {
};
Index.Equip.prototype = {
	init : function() {
		var that = this;
		that.Equiptable();
		that.Echarts();
		that.Equiplaydate();
		that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
		$("#eqType").change(function() {
			that.loadInfo();
		});
	},
	Echarts : function() {//柱状图
		var that = this;
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
					name : '累计天数',
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
		}
	},

	Equiptable : function() {//表格
		$('#table_equip').bootstrapTable({
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
				field : 'devModel',
				title : '设备型号'
			}, {
				field : 'devName',
				title : '设备名称'
			}, {
				field : 'useNum',
				title : '使用次数'
			}, {
				field : 'userNum',
				title : '使用人数'
			}, {
				field : 'useDate',
				title : '累计天数'

			}, {
				field : 'buyNum',
				title : '购入总数'
			}, {
				field : 'stock',
				title : '在库数量'
			}, {
				field : 'damageNum',
				title : '损坏数量'

			}, ]
		});
	},
	Equiplaydate : function() {//日历
		//执行一个laydate实例
		var start = laydate.render({
			elem : '#EstartTime',
			show: true,
			trigger: 'click',
			done: function (value, date) {
				end.config.min = {
					year: date.year,
					month: date.month - 1,
					date: date.date + 1,
					
				};
			}
			
		});
		//执行一个laydate实例
		var end = laydate.render({
			elem : '#EendTime',
			show: true,
			trigger: 'click'
			
		});
	},
	loadInfo : function() {
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var devName = [];
		var useNum = [];
		var useDate = [];
		$.ajax({
			url : "/Device/Admin/selectDeviceUseInfoByDate?time=new Date().getTime()",
			type : "get",
			data : {'startDate':$('#EstartTime').val(),'endDate':$('#EendTime').val(),'devType':$('#eqType').val()},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_equip").bootstrapTable('load', data);
				for(var i=0;i<data.length;i++){
					devName.push(data[i].devName);
					useNum.push(data[i].useNum);
					useDate.push(data[i].useDate);
				}
				myChart.setOption({        //加载数据图表
                    xAxis: {
                        data: devName
                    },
                    series: [{
                        // 根据名字对应到相应的系列
                        name: '使用次数',
                        data: useNum
                    },{
                    	name: '累计小时数',
                        data: useDate
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
	var index = new Index.Equip();
	index.init();
});