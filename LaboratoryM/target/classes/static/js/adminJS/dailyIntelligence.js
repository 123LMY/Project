if (!window.Index)
	Index = {};
Index.Intelligence = function() {
};

Index.Intelligence.prototype = {
	init : function() {
		var that = this;
		that.IntellResulttable();
		that.IntellNoticetable();
		that.Intelllaydate();
		/*that.loadInfo();*/
		$("#checkboxResult").click(function() {
			if ($("#checkboxResult").is(":checked")) {
				that.loadResult();
				$("#intellResult").css("display", "block");
			} else {
				$("#intellResult").css("display", "none");
			}
		});
		$("#checkboxNoitce").change(function() {
			if ($("#checkboxNoitce").is(":checked")) {
				that.loadNotice();
				$("#intellNoitce").css("display", "block");
			} else {
				$("#intellNoitce").css("display", "none");
			}
		});
		$("#checkbtn").click(function(){
			that.loadResult();
		});
	},
	IntellResulttable : function() {
		$('#table_intellResult').bootstrapTable({
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
			// search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : true,
			showColumns : false, //是否显示所有的列
			showRefresh : false, //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			//uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : "proName",
				title : '项目名称'
			}, {
				field : 'prize',
				title : '获奖情况'
			}, {
				field : 'patent',
				title : '专利情况'
			}, {
				field : 'result',
				title : '技术成果转让情况'

			}, {
				field : 'proMembers',
				title : '项目组成员'

			}, {
				field : 'teachers',
				title : '指导老师'

			}, ]
		});
	},
	IntellNoticetable : function() {
		$('#table_intellNoitce').bootstrapTable({
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
			// search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : true,
			showColumns : false, //是否显示所有的列
			showRefresh : false, //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			//height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			//uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : "operator",
				title : '发布人'
			}, {
				field : 'typeName',
				title : '标题'
			}, {
				field : 'description',
				title : '内容'
			}, {
				field : 'publishDate',
				title : '操作时间'

			}, ]
		});
	},

	Intelllaydate : function() {//日历
		//执行一个laydate实例
		var start = laydate.render({
			elem : '#IntellstartTime', //指定元素
			show : true,
			trigger : 'click',
			type: 'year',
			done: function (value, date) {
				end.config.min = {
					year: date.year,
				};
			}
		});
		//执行一个laydate实例
		var end = laydate.render({
			elem : '#IntellendTime', //指定元素
			show : true,
			trigger : 'click',
			type: 'year'
		});
	},
	loadNotice : function() {
		$.ajax({
			url : "/Notice/Admin/selectNoticelimit?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#table_intellNoitce").bootstrapTable('load', data);
			},
			error : function(data) {
				Lobibox.notify('error', {
					icon : false,
					title : '提示',
					msg : '服务器出错'
				});
			}

		});
	},
	loadResult : function() {
		$.ajax({
			url : "/ProjectResult/Admin/selectProResult?time=new Date().getTime()",
			type : "get",
			data : {'startTime':$('#IntellstartTime').val(),'endTime':$('#IntellendTime').val()},
			dataType : "json",
			success : function(data) {
				$("#table_intellResult").bootstrapTable('load', data);
			},
			error : function(data) {
				Lobibox.notify('error', {
					icon : false,
					title : '提示',
					msg : '服务器出错'
				});
			}

		});
	}
};
$(function() {
	var index = new Index.Intelligence();
	index.init();
});