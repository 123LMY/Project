if (!window.Index)
	Index = {};
Index.Project = function() {
};

Index.Project.prototype = {
	init : function() {
		var that = this;
		that.Projecttable();
		that.ProjectEcharts();
		that.Projectlaydate();
		that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
	},
	ProjectEcharts : function() {
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var app = {};
		option = null;
		var posList = [ 'left', 'right', 'top', 'bottom', 'inside',
				'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
				'insideTopLeft', 'insideTopRight', 'insideBottomLeft',
				'insideBottomRight' ];

		app.configParameters = {
			rotate : {
				min : -90,
				max : 90
			},
			align : {
				options : {
					left : 'left',
					center : 'center',
					right : 'right'
				}
			},
			verticalAlign : {
				options : {
					top : 'top',
					middle : 'middle',
					bottom : 'bottom'
				}
			},
			position : {
				options : echarts.util.reduce(posList, function(map, pos) {
					map[pos] = pos;
					return map;
				}, {})
			},
			distance : {
				min : 0,
				max : 100
			}
		};

		app.config = {
			rotate : 90,
			align : 'left',
			verticalAlign : 'middle',
			position : 'insideBottom',
			distance : 15,
			onChange : function() {
				var labelOption = {
					normal : {
						rotate : app.config.rotate,
						align : app.config.align,
						verticalAlign : app.config.verticalAlign,
						position : app.config.position,
						distance : app.config.distance
					}
				};
				myChart.setOption({
					series : [ {
						label : labelOption
					}, {
						label : labelOption
					}, {
						label : labelOption
					} ]
				});
			}
		};

		var labelOption = {
			normal : {
				show : true,
				position : app.config.position,
				distance : app.config.distance,
				align : app.config.align,
				verticalAlign : app.config.verticalAlign,
				rotate : app.config.rotate,
				formatter : '{c}  {name|{a}}',
				fontSize : 16,
				rich : {
					name : {
						textBorderColor : '#fff'
					}
				}
			}
		};
		option = {
			color : [ '#003366', '#006699', '#4cabce' ],
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'shadow'
				}
			},
			legend : {
				data : [ '入驻项目总数', '省级以上获奖数量', '知识产权数量' ]
			},
			toolbox : {
				show : true,
				orient : 'vertical',
				left : 'right',
				top : 'center'
			},
			calculable : true,
			xAxis : [ {
				type : 'category',
				axisTick : {
					show : false
				},
				data : [ '类别1', '类别2', '类别3', '类别4', '类别5' ]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				name : '入驻项目总数',
				type : 'bar',
				barGap : 0,
				label : labelOption,
				data : ''
			}, {
				name : '省级以上获奖数量',
				type : 'bar',
				label : labelOption,
				data : ''
			}, {
				name : '知识产权数量',
				type : 'bar',
				label : labelOption,
				data : ''
			} ]
		};
		if (option && typeof option === "object") {
			myChart.setOption(option, true);
		}
	},
	Projecttable : function() {
		$('#table_project').bootstrapTable({
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
			strictSearch : true,
			showColumns : false, //是否显示所有的列
			showRefresh : false, //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				field : 'proTypeName',
				title : '项目类型'
			}, {
				field : 'proTotal',
				title : '入住项目总数'
			}, {
				field : 'stuTotal',
				title : '参与学生数量'
			}, {
				field : 'teachTotal',
				title : '参与老师数量'
			}, {
				field : 'finish',
				title : '结题数量'

			}, {
				field : 'schoolLevel',
				title : '校级奖项数量'
			}, {
				field : 'provincialLevel',
				title : '省级奖项数量'
			}, {
				field : 'nationalLevel',
				title : '国家级奖项数量'

			}, {
				field : 'internationalLevel',
				title : '国际级奖项数量'
			}, {
				field : 'patent',
				title : '知识产权数量'

			}, {
				field : 'achievements',
				title : '技术成果转让数量'

			}, ]
		});
	},
	Projectlaydate : function() {//日历
		//执行一个laydate实例
		var start = laydate.render({
			elem : '#PstartTime', //指定元素
			show : true,
			trigger : 'click',
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
			elem : '#PendTime', //指定元素
			show : true,
			trigger : 'click'
		});
	},
	loadInfo:function(){
		var dom = document.getElementById("echarts");
		var myChart = echarts.init(dom);
		var proTypeName = [];
		var proTotal = [];
		var award = [];
		var patent = [];
		$.ajax({
			url : "/Project/Admin/selectEntryProjectInfo?time=new Date().getTime()",
			type : "get",
			data : {'startTime':$('#PstartTime').val(),'endTime':$('#PendTime').val()},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_project").bootstrapTable('load', data);
				for(var i=0;i<data.length;i++){
					proTypeName.push(data[i].proTypeName);
					proTotal.push(data[i].proTotal);
					award.push(data[i].provincialLevel+data[i].nationalLevel+data[i].internationalLevel);
					patent.push(data[i].patent);
				}
				myChart.setOption({        //加载数据图表
                    xAxis: {
                        data: proTypeName
                    },
                    series: [{
                        // 根据名字对应到相应的系列
                        name: '入驻项目总数',
                        data: proTotal
                    },{
                    	name: '省级以上获奖数量',
                        data: award
                    },{
                    	name: '知识产权数量',
                        data: patent
                    }]
                });
			},
			error : function(data) {
				alert("服务器出错了！！");
			}

		});
	}
};

$(function() {
	var index = new Index.Project();
	index.init();
});