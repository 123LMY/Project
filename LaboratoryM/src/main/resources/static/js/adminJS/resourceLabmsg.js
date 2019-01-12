if (!window.Index)
	Index = {};
Index.Labmsg = function() {
};

Index.Labmsg.prototype = {
	init : function() {
		var that = this;
		that.Labmsgtable();
		that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
		$("#Elap_delect").click(function(){
			var a= $("#table_labmsg").bootstrapTable('getSelections');
			if(a.length!=0){
				that.deleteLab(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
		$("#Elap_edit").click(function(){
			var a= $("#table_labmsg").bootstrapTable('getSelections');
			if(a.length==1){
				$('#labdetailform')[0].reset();
				$("#Elap_edit").attr("data-target","#myModalLabmsg1");
				$("#editlabName").val(a[0].labName);
				$("#editlabSite").val(a[0].labSite);
				$("#editlabFunction").val(a[0].labFunction);
				$("#editadministrators").val(a[0].administrators);
				$("#editlabStatus").find(
						"option:contains('" + a[0].labStatus + "')")
						.attr("selected", true);
				$("#editId").val(a[0].id);
				$("#editremark").val(a[0].remark);
			}else if(a.length==0){
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '只能选择一项'
                });
			}
		});
		$("#changebtn").click(function(){
			if($('#editlabName').val()==""||$('#editlabSite').val()==""||$('#editlabFunction').val()==""||$('#editadministrators').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全实验室信息'
                });
				return;
			}
			that.updateLab();
		});
		$("#addbtn").click(function(){
			if($('#addlabName').val()==""||$('#addlabSite').val()==""||$('#addlabFunction').val()==""||$('#addadministrators').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全实验室信息'
                });
				return;
			}
			that.insertLab();
			$('#addlabform')[0].reset();
		});
	},
	Labmsgtable : function() {
		var that=this;
		$('#table_labmsg').bootstrapTable({
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
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			uniqueId : "id", //每一行的唯一标识，一般为主键列
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'labName',
				title : '实验室名称'
			}, {
				field : 'labSite',
				title : '地点'
			}, {
				field : 'labFunction',
				title : '职能'
			}, {
				field : 'administrators',
				title : '管理员'
			}, {
				field : 'labStatus',
				title : '状态',
				sortable: true
			}, {
				field : 'remark',
				title : '备注'
			}, {
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
							return [
								'<button type="button" class="btn btn-danger deletelabbtn">删除</button>'
							].join('');	
				},
				events:{
					'click .deletelabbtn': function(e, value, row, index) {
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
									  that.deleteLabbyone(row.id);
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
			url : "/Lab/Admin/selectLabInfo?time=new Date().getTime()",
			type : "get",
			data : {
				'labName' : $('#checkLabName').val()
			},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_labmsg").bootstrapTable('load', data);
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
	deleteLab:function(a){
		var that = this;
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.id;
		});
		$.ajax({
			url : "/Lab/Admin/deleteLab?time=new Date().getTime()",
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
	deleteLabbyone:function(a){
		var that = this;
		var array = new Array();
		array[0] = a;
		$.ajax({
			url : "/Lab/Admin/deleteLab?time=new Date().getTime()",
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
	updateLab:function(){
		var that = this;
		$.ajax({
			url : "/Lab/Admin/updateLab?time=new Date().getTime()",
			type : "get",
			data : {'labName':$('#editlabName').val(),'labSite':$('#editlabSite').val(),'labFunction':$('#editlabFunction').val(), 'id':$("#editId").val()
				,'administrators':$('#editadministrators').val(),'labStatus':$('#editlabStatus').val(),'remark':$("#editremark").val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#myModalLabmsg1").modal('hide');
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

	insertLab:function(){
		var that = this;
		$.ajax({
			url : "/Lab/Admin/insertLab?time=new Date().getTime()",
			type : "get",
			data : {'labName':$('#addlabName').val(),'labSite':$('#addlabSite').val(),'labFunction':$('#addlabFunction').val(),
				'administrators':$('#addadministrators').val(),'labStatus':$('#addlabStatus').val(),'remark':$("#addremark").val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#myModalLabmsg").modal('hide');
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
	}
};
$(function() {
	var index = new Index.Labmsg();
	index.init();
});