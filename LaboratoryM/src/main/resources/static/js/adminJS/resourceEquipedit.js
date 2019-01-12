if (!window.Index)
	Index = {};
Index.Equipedit = function() {
	
};

Index.Equipedit.prototype = {
	init : function() {
		var that = this;
		that.Equipedittable();
		$("#editbtn").click(function(){
			var a= $("#table_equipedit").bootstrapTable('getSelections');
			$("#editbtn").attr("data-target", "");
			if(a.length==0){
				if($('#devdetaildevModel').val()==""||$('#devdetaildevName').val()==""||$('#devdetailvender').val()==""||$('#devdetailcustodian').val()==""){
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '请补全设备信息'
	                });
					return;
				}
				that.updateDevice();
			}else if(a.length==1){
				if(a[0].devStatusName!='空闲'){
					Lobibox.notify('warning', {
	                    icon: false,
	                    title: '提示',
	                    msg: '该设备处于非空闲状态'
	                });
					return;
				}
				$("#editbtn").attr("data-target", "#modal1");
				$("#editdevSn").val(a[0].devSn);
				$("#editRFID").val(a[0].rfidNo);
				$("#editlabName").val(a[0].labName);
				$("#editcabNo").val(a[0].cabNo);
				$("#editdetailremark").val(a[0].remark);
				$("#devdetailId").val(a[0].id);
				$("#editdevStatus").find(
						"option:contains('" + a[0].devStatusName + "')")
						.attr("selected", true);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '不可多选'
                });
			}
		});
		$("#back").click(function(){
			$.ajax({
				url : "resourceEquipmsg.html?time=new Date().getTime()",
				type : "GET",
				async : false,
				dataType : "html",
				success : function(data) {
					// 将返回的页面（主内容部分）填入主页面div中
					$("#main_content").html(data);
					
				}
			});
		});
		$("#deletebtn").click(function(){
			var a= $("#table_equipedit").bootstrapTable('getSelections');
			if(a.length!=0){
				that.deleteDevDetail(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
		$("#editdevdetailbtn").click(function(){
			if($('#editdevSn').val()==""||$('#editRFID').val()==""||$('#editlabName').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全设备信息'
                });
				return;
			}
			that.updateDeviceDetail();
		});
	},
	Equipedittable : function() {
		var that=this;
		$('#table_equipedit').bootstrapTable({
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
			//strictSearch: true,
			//showColumns: true,                  //是否显示所有的列
			//showRefresh: true,                  //是否显示刷新按钮
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			//height : 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
			uniqueId : "id", //每一行的唯一标识，一般为主键列
			//showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'devSn',
				title : '设备序号'
			}, {
				field : 'rfidNo',
				title : 'RFID'
			}, {
				field : 'inDate',
				title : '入库时间'
			}, {
				field : 'devStatusName',
				title : '设备状态'
			}, {
				field : 'usingName',
				title : '使用人'
			} , {
				field : 'labName',
				title : '实验室'
			} , {
				field : 'cabNo',
				title : '柜子号'
			} , {
				field : 'remark',
				title : '备注'
			},{
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
							return [
                                /* '<button type="button" class="btn btn-info uqdatebtn" style="margin-right:3%;">修改</button>'+*/
								'<button type="button" class="btn btn-danger deletetabbtn">删除</button>'
							].join('');	
				},
				events:{
					'click .deletetabbtn': function(e, value, row, index) {
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
									that.deleteDevDetailbyone(row.id);
								}
							}
						});
					},
				}
			 }
			
		 ]
		});
	},
	updateDevice:function(){
		var that = this;
		$.ajax({
			url : "/Device/Admin/updateDevice?time=new Date().getTime()",
			type : "get",
			data : {'devModel':$('#devdetaildevModel').val(),'devName':$('#devdetaildevName').val(),'tDevType_id':$('#devdetailttypeName').val(), 'id':$("#devId").val(),'vender':$("#devdetailvender").val(),'custodian':$("#devdetailcustodian").val(),'remark':$("#devdetailremark").val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					that.loadDevDetail($("#devId").val());
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
	                });
				}else{
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
	loadDevDetail:function(id){
		$.ajax({
			url : "/Device/Admin/selectDevById?time=new Date().getTime()",
			type : "get",
			data : {
				'id':id
			},
			contentType : "json",
			success : function(data) {
				$("#devdetaildevModel").val(data.devModel);
				$("#devdetaildevName").val(data.devName);
				$("#devdetailvender").val(data.vender);
				$("#devdetailremark").val(data.remark);
				$("#devdetailcustodian").val(data.custodian.realName);
				$("#devdetailttypeName").val(data.tDevType_id);
				$("#devId").val(data.id);
			},
			error : function(data) {
				alert("服务器出错了！！");
			}
		});
	},
	deleteDevDetail:function(a){
		var that = this;
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.id;
		});
		$.ajax({
			url : "/DeviceDetail/Admin/deleteDevDetailById?time=new Date().getTime()",
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
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '服务器出错'
                });
			}

		});
	},
	deleteDevDetailbyone:function(a){
		var that = this;
		var array = new Array();
		array[0]=a;
		$.ajax({
			url : "/DeviceDetail/Admin/deleteDevDetailById?time=new Date().getTime()",
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
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '服务器出错'
                });
			}

		});
	},
	loadInfo:function(){
		$.ajax({
			url : "/DeviceDetail/Admin/selectDevDetailInfo?time=new Date().getTime()",
			type : "get",
			data : {
				'id' : $('#devId').val()
			},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_equipedit").bootstrapTable('load', data);
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
	updateDeviceDetail:function(){
		var that = this;
		$.ajax({
			url : "/DeviceDetail/Admin/updateDevDetail?time=new Date().getTime()",
			type : "get",
			data : {'devSn':$('#editdevSn').val(),'RFID':$('#editRFID').val(),'devStatus':$('#editdevStatus').val(), 'id':$("#devdetailId").val(),'labName':$("#editlabName").val(),'cabNo':$("#editcabNo").val(),'remark':$("#editdetailremark").val()},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#modal1").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
	                });
				}else{
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
	}
};
$(function() {
	var index = new Index.Equipedit();
	index.init();
});