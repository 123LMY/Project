if (!window.Notice)
	Notice = {};
Notice.Main = function() {
};

Notice.Main.prototype = {

	init : function() {
		var that = this;
		that.loadtable();
		that.loadInfo();
		$('#btn_add').click(function(){
			document.getElementById("addNoticeForm").reset();
		});
		$("#submitnoticeBut").click(function(){
			if($('#addTypeName').val()==""||$('#addDescription').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全公告信息'
                });
				return;
			}
			that.addNotice();
		});
		$("#btn_edit").click(function(){
			var a= $("#table_notice").bootstrapTable('getSelections');
			if(a.length==1){
				document.getElementById("editNoticeForm").reset();
				$("#btn_edit").attr("data-target","#editModalNotice");
			     $("#editPublisher").val(a[0].publishers);
			     $("#editTypeName").val(a[0].typeName);
			     $("#editDescription").val(a[0].description);
			     $("#editOperator").val(a[0].operator);
			     $("#publishDate").val(a[0].publishDate);
			     $("#noticeID").val(a[0].id);
			}else if(a.length==0){
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				$("#btn_edit").attr("data-target","");
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '不能多选'
                });
				$("#btn_edit").attr("data-target","");
			}
		});
		$("#btn_delete").click(function(){
			var a= $("#table_notice").bootstrapTable('getSelections');
			if(a.length!=0){
				that.deleteNotices(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
		$("#submitEidtBut").click(function(){
			if($('#editTypeName').val()==""||$('#editDescription').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全公告信息'
                });
				return;
			}
			that.updateNotice();
		});
	},

	loadtable : function() {
		var that = this;
		$('#table_notice').bootstrapTable({
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
			pageList : [ 10, 20, 50, 100 ], // 可供选择的每页的行数（*）
			// search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
			strictSearch : true,
			showColumns : false, // 是否显示所有的列
			showRefresh : false, // 是否显示刷新按钮
			showRefresh: false,
			minimumCountColumns : 2, // 最少允许的列数
			clickToSelect : true, // 是否启用点击选中行
			// height : 500, // 行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "id", // 每一行的唯一标识，一般为主键列
			showToggle : false, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
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
				field : 'publishers',
				title : '操作人'

			}, {
				field : 'publishDate',
				title : '操作时间'

			}, {
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
							return [
								'<button type="button" class="btn btn-danger deletenoticebtn">删除</button>'
							].join('');	
				},
				events:{
					'click .deletenoticebtn': function(e, value, row, index) {
					  
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
									that.deleteNoticebyone(row.id);
								}
							}
							
						});

					},
				}
			 } ]
		});
	},
	
	addNotice:function(){
		var that = this;

		$.ajax({
			url : "/Notice/Admin/insertNotice?time=new Date().getTime()",
			type : "get",
			data : {'typeName':$('#addTypeName').val(),'description':$('#addDescription').val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#addModalNotice").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增成功'
	                });
				}else{
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增失败'
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
	updateNotice:function(){
		var that = this;
		$.ajax({
			url : "/Notice/Admin/updateNotice?time=new Date().getTime()",
			type : "get",
			data : {'typeName':$('#editTypeName').val(),'Publisher':$('#editPublisher').val(),'description':$('#editDescription').val(), 'id':$("#noticeID").val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#editModalNotice").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '修改成功'
	                });
				}else{
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '修改失败'
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
	deleteNotices:function(a){
		var that = this;
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.id;
		});
		$.ajax({
			url : "/Notice/Admin/deleteNotices?time=new Date().getTime()",
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
	deleteNoticebyone:function(a){
		var that = this;
		var array=new Array();
		array[0] = a;
		$.ajax({
			url : "/Notice/Admin/deleteNotices?time=new Date().getTime()",
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
	loadInfo:function(){
		$.ajax({
			url : "/Notice/Admin/selectNotice?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#table_notice").bootstrapTable('load', data);
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

	var notice = new Notice.Main();
	notice.init();
});