if (!window.Index)
	Index = {};
Index.Node = function() {
};

Index.Node.prototype = {
	init : function() {
		var that = this;
		that.Nodetable();
		that.loadInfo();
		$('#btn_add').click(function(){
			$('#addnodeform')[0].reset();
		});
		$("#btn_edit").click(function() {
			var a = $("#table_node").bootstrapTable('getSelections');
			if (a.length == 1) {
				$('#editnodeform')[0].reset();
				$("#btn_edit").attr("data-target", "#editModalNode");
				$("#editnodeNo").val(a[0].nodeNo);
				$("#editnodeName").val(a[0].nodeName);
				$("#editipAddress").val(a[0].ipAddress);
				$("#editid").val(a[0].id);
				
			} else if (a.length == 0) {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				$("#btn_edit").attr("data-target","");
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '不可多选'
                });
				$("#btn_edit").attr("data-target","");
			}
		});
		$("#btn_delete").click(function() {
			var a = $("#table_node").bootstrapTable('getSelections');
			if (a.length != 0) {
				that.deleteNode(a);
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
		$("#addbtn").click(function(){
			if($('#addnodeNo').val()==""||$('#addnodeName').val()==""||$('#addipAddress').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全节点信息'
                });
				return;
			}
			that.insertNode();
		});
		$("#editbtn").click(function(){
			if($('#editnodeNo').val()==""||$('#editnodeName').val()==""||$('#editipAddress').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全节点信息'
                });
				return;
			}
			that.updateNode();
		});
	},
	Nodetable : function() {
		var that=this;
		$('#table_node').bootstrapTable({
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
			// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "id", // 每一行的唯一标识，一般为主键列
			showToggle : false, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'nodeNo',
				title : '编号'
			}, {
				field : 'nodeName',
				title : '节点名'
			}, {
				field : 'ipAddress',
				title : '节点地址'
			},  {
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
							return [
								'<button type="button" class="btn btn-danger deletenodebtn">删除</button>'
							].join('');	
				},
				events:{
					'click .deletenodebtn': function(e, value, row, index) {
					  
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
									that.deleteNodebyone(row.id);
								}
							}
							
						});

					},
				}
			 }
			]
		});
	},
	loadInfo : function() {
		$.ajax({
			url : "/Node/Admin/selectNode?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#table_node").bootstrapTable('load', data);
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
	updateNode : function() {
		var that = this;
		$.ajax({
			url : "/Node/Admin/updateNode?time=new Date().getTime()",
			type : "get",
			data : {
				'nodeNo' : $('#editnodeNo').val(),
				'nodeName' : $('#editnodeName').val(),
				'ipAddress' : $('#editipAddress').val(),
				'id' : $("#editid").val()
			},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#editModalNode").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
	                });
				}else if(data == "fail"){
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新失败'
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
	insertNode:function(){
		var that = this;
		$.ajax({
			url : "/Node/Admin/insertNode?time=new Date().getTime()",
			type : "get",
			data : {'nodeNo' : $('#addnodeNo').val(),'nodeName' : $('#addnodeName').val(),'ipAddress' : $('#addipAddress').val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#addModalNode").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增成功'
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
	deleteNode : function(a) {
		var that = this;
		var array = new Array();
		$.each(a, function(i, item) {
			array[i] = item.id;
		});
		$.ajax({
			url : "/Node/Admin/deleteNode?time=new Date().getTime()",
			type : "post",
			data : {
				'ids' : array
			},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '删除成功'
	                });
				} else {
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
	deleteNodebyone : function(a) {
		var that = this;
		var array = new Array();
		array[0] = a;
		$.ajax({
			url : "/Node/Admin/deleteNode?time=new Date().getTime()",
			type : "post",
			data : {
				'ids' :array
			},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '删除成功'
	                });
				} else {
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
	var index = new Index.Node();
	index.init();
});