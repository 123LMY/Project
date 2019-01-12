if (!window.Index)
	Index = {};
Index.Record = function() {
};

Index.Record.prototype = {
	init : function() {
		var that = this;
		that.Recordtable();
		that.RecordLablaydate();
		that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
		$("#deletebtn").click(function(){
			var a = $("#table_record").bootstrapTable('getSelections');
			if(a.length!=0){
				that.deleteRecord(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
	},
	Recordtable : function() {
		var that=this;
		$('#table_record').bootstrapTable({
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
			uniqueId : 'id', //每一行的唯一标识，一般为主键列
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'userName',
				title : '用户名'
			}, {
				field : 'realName',
				title : '真实姓名'
			}, {
				field : 'proName',
				title : '所属项目组'
			}, {
				field : 'inTime',
				title : '进入时间'

			}, {
				field : 'outTime',
				title : '离开时间'
			},{
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
							return [
								'<button type="button" class="btn btn-danger deleterecordbtn">删除</button>'
							].join('');	
				},
				events:{
					'click .deleterecordbtn': function(e, value, row, index) {
					  
						bootbox.setLocale("zh_CN");
						bootbox.confirm({
							boxCss:{"width":"400px","matgin-top":"100px" },
							title : "提示",
							animate : true,
							buttons : {
								cancel : {
									label : '<i class="glyphicon glyphicon-remove"></i> 取消'
								},
								confirm : {
									label : '<i class="glyphicon glyphicon-ok"></i> 确定'
								}
							},
							message : "确定删除吗?",
							callback : function(result) {
								if (result == true) {
									that.deleteRecordbyone(row.id);
								}
							}
							
						});

					},
				}
			 } ]
		});
	},
	RecordLablaydate : function() {//日历
		//执行一个laydate实例
		var start = laydate.render({
			elem : '#RstartTime',
			show : true,
			trigger : 'click',
			done: function (value, date) {
				end.config.min = {
					year: date.year,
					month: date.month - 1,
					date: date.date,
				};
			}
		});
		//执行一个laydate实例
		var end = laydate.render({
			elem : '#RendTime',
			show : true,
			trigger : 'click'
		});
	},
	loadInfo:function(){
		$.ajax({
			url : "/EntranceGuardRecord/Admin/selectEntranceGuardRecord?time=new Date().getTime()",
			type : "get",
			data : {'inTime':$('#RstartTime').val(),'outTime':$('#RendTime').val()},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_record").bootstrapTable('load', data);
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
	deleteRecord:function(a){
		var that = this;
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.id;
		});
		$.ajax({
			url : "/EntranceGuardRecord/Admin/deleteEntranceGuardRecord?time=new Date().getTime()",
			type : "post",
			data : {'ids': array},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '删除成功'
	                });
				}else{
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '删除失败'
	                });
				}
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
	deleteRecordbyone:function(a){
		var that = this;
		var array = new Array();
		array[0] =a;
		$.ajax({
			url : "/EntranceGuardRecord/Admin/deleteEntranceGuardRecord?time=new Date().getTime()",
			type : "post",
			data : {'ids': array},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '删除成功'
	                });
				}else{
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '删除失败'
	                });
				}
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
	var index = new Index.Record();
	index.init();
});