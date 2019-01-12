if (!window.Index)
	Index = {};
Index.Attendance = function() {
};

Index.Attendance.prototype = {
	init : function() {
		var that = this;
		that.Attendancetable();
		that.laydates();
		that.getAttence();
		$("#query").click(function() {
			if (that.checkForm()) {
				that.queryAttence();
			} else {
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '查询条件不符合'
                });
			}

		});
	},
	Attendancetable : function() {
		$('#table_attendance').bootstrapTable({
			method : 'get', // 请求方式（*）
			toolbar : '#toolbar', // 工具按钮用哪个容器
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			sortOrder : "asc", // 排序方式
			// queryParams: oTableInit.queryParams,//传递参数（*）
			sidePagination : "client", // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, // 初始化加载第一页，默认第一页
			pageSize : 10, // 每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], // 可供选择的每页的行数（*）
			// search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : true,
			showColumns : false, // 是否显示所有的列
			showRefresh : false, // 是否显示刷新按钮
			minimumCountColumns : 2, // 最少允许的列数
			clickToSelect : true, // 是否启用点击选中行
			//height : 500, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "id", // 每一行的唯一标识，一般为主键列
			showToggle : false, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			columns : [ {
				field : 'userName',
				title : '用户名'
			}, {
				field : 'realName',
				title : '姓名'
			}, {
				field : 'proName',
				title : '所属项目组'
			}, {
				field : 'signinTime',
				title : '开始时间'

			}, {
				field : 'signoutTime',
				title : '结束时间'

			}, ]
		});
	},
	laydates : function() {// 日历
		// 执行一个laydate实例
		var signin =  laydate.render({
			elem : '#signinTime', // 指定元素
			type : 'datetime',
			show : true,
			trigger : 'click',
			done: function (value, date) {
				signout.config.min = {
					year: date.year,
					month: date.month - 1,
					date: date.date,
					hours: date.hours,
					minutes: date.minutes,
					seconds: date.seconds + 1,
				};
			}
		});
		// 执行一个laydate实例
		var signout =  laydate.render({
			elem : '#signoutTime', // 指定元素
			type : 'datetime',
			show : true,
			trigger : 'click'
		});
	},
	queryAttence : function() {
		$.ajax({
			url : "/Attence/Admin/selectAttence?time=new Date().getTime()",
			type : "get",
			data : {
				'signinTime' : $('#signinTime').val(),
				'signoutTime' : $('#signoutTime').val(),
				'realName' : $('#realName').val()
			},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_attendance").bootstrapTable('load', data);
			},
			error : function(data) {
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '服务器出错'
                });
			}

		});
	},
	getAttence : function() {
		$.ajax({
			url : "/Attence/Admin/selectAllAttence?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#table_attendance").bootstrapTable('load', data);
			},
			error : function(data) {
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '服务器出错'
                });
			}

		});
	},
	checkForm : function() {
		if ($("#signinTime").val() != null && $("#signoutTime").val() != null
				&& $("#realName").val() != null) {
			return true;
		}
		if ($("#signinTime").val() != null && $("#signoutTime").val() != null
				&& $("#realName").val() == null) {
			return true;
		}
		if ($("#signinTime").val() == null && $("#signoutTime").val() == null
				&& $("#realName").val() != null) {
			return true;
		}
		return false;
	}

};
$(function() {
	var index = new Index.Attendance();
	index.init();
});