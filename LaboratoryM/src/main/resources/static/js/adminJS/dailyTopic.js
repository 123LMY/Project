if (!window.Index)
	Index = {};
Index.Topic = function() {
};

Index.Topic.prototype = {
	init : function() {
		var that = this;
		that.Topictable();
		that.loadInfo();
		$("#btn_add1").click(function(){
			var a= $("#table_topic").bootstrapTable('getSelections');
			if(a.length!=0){
				that.addAuthorization();
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
			
		});
		$("#btn_delete1").click(function(){
			var a= $("#table_topic").bootstrapTable('getSelections');
			if(a.length!=0){
				that.cancelAuthorization();
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
	},
	Topictable : function() {
		$('#table_topic').bootstrapTable({

			method : 'get', //请求方式（*）
			toolbar : '#toolbar', //工具按钮用哪个容器
			striped : true, //是否显示行间隔色
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, //是否显示分页（*）
			sortable : true, //是否启用排序
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
			uniqueId : "id", //每一行的唯一标识，一般为主键列
			showToggle : false, //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'userName',
				title : '用户名'
			}, {
				field : 'labName',
				title : '实验室'
			}, {
				field : 'proName',
				title : '所属项目组'
			}, {
				field : 'status',
				title : '项目状态'
			}, {
				field : 'topic',
				title : '门禁状态',
                sortable: true
			}, ]
		});
	},
	loadInfo:function(){
		$.ajax({
			url : "/UserGrant/Admin/selectGrantInfo?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#table_topic").bootstrapTable('load', data);
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
	addAuthorization:function(){
		var that = this;
		var a= $("#table_topic").bootstrapTable('getSelections');
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.tUser_id_a;
		});
		$.ajax({
			url:"/UserGrant/Admin/addAuthorization?time=new Date().getTime()",
			type:"post",
			data : {'ids': array},
			success:function(data){
				if(data=='success'){
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '授权成功'
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
	cancelAuthorization:function(){
		var that = this;
		var a= $("#table_topic").bootstrapTable('getSelections');
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.tUser_id_a;
		});
		$.ajax({
			url:"/UserGrant/Admin/cancelAuthorization?time=new Date().getTime()",
			type:"post",
			data : {'ids': array},
			success:function(data){
				if(data=='success'){
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '撤销成功'
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
	
};
$(function() {
	var index = new Index.Topic();
	index.init();
});