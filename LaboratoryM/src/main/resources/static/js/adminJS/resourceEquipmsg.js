if (!window.Index)
	Index = {};
Index.Equipmsg = function() {
};

Index.Equipmsg.prototype = {
	init : function() {
		var that = this;
		that.Equipmsgtable();
		that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
		$("#deletebtn").click(function(){
			var a= $("#table_equipmsg").bootstrapTable('getSelections');
			if(a.length!=0){
				that.deleteDevice(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
			}
		});
		$("#addbtn").click(function(){
			var a= $("#table_equipmsg").bootstrapTable('getSelections');
			if(a.length==0){
				$("#addbtn").attr("data-target", "#myModalEquipmsg");
				$("#addtypeName").find("option:selected").text("");
				$("#addtypeName").empty();
				that.loadDevType("addtypeName");
			}else if(a.length==1){
				$("#addbtn").attr("data-target", "#myModalSubEquipmsg");
				$("#devId").val(a[0].id);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '不能多选'
                });
			}
		});
		$("#adddevbtn").click(function(){
			if($('#adddevModel').val()==""||$('#adddevName').val()==""||$('#addvender').val()==""||$('#addcustodian').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全设备信息'
                });
				return;
			}
			that.insertDevice();
			$('#adddevfrom')[0].reset();
		});
		$("#adddevdetailbtn").click(function(){
			if($('#adddevSn').val()==""||$('#addRFID').val()==""||$('#addlabName').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全设备信息'
                });
				return;
			}
			that.insertDeviceDetail();
			$('#adddevdetailfrom')[0].reset();
		});
		
	},
	Equipmsgtable : function() {
		var that = this;
		$('#table_equipmsg').bootstrapTable({
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
			minimumCountColumns : 2, //最少允许的列数
			clickToSelect : true, //是否启用点击选中行
			uniqueId : "id", //每一行的唯一标识，一般为主键列
			cardView : false, //是否显示详细视图
			detailView : false, //是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'devModel',
				title : '设备型号'
			}, {
				field : 'devName',
				title : '设备名称'
			}, {
				field : 'devTotal',
				title : '设备总数'
			}, {
				field : 'freeNum',
				title : '空闲数'
			}, {
				field : 'usingNum',
				title : '在用数'
			}, {
				field : 'damageNum',
				title : '损坏数'
			}, {
				field : 'scrapNum',
				title : '报废数'
			}, {
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
							return [
                                 '<button type="button" class="btn btn-info editmsgbtn" style="margin-right:3%;">编辑</button>'+
								'<button type="button" class="btn btn-danger deletemsgbtn">删除</button>'
							].join('');	
				},
				events:{
					'click .editmsgbtn': function(e, value, row, index) {
						
						  that.editbtn(row.id);
					},
					'click .deletemsgbtn': function(e, value, row, index) {
					  
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
									that.deleteDevicebyOne(row.id);
								}
							}
							
						});

					},
				}
			 }
			]
		});
	},
	confirmdelete: function() {
		var that = this;
		bootbox.setLocale("zh_CN");
		bootbox.confirm({
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
					$.ajax({
						url : "/adminlogout?time=new Date().getTime()",
						type : "post",
						success : function(data) {
							// 将返回的页面（主内容部分）填入主页面div中
							$(window).attr('location', data);
							Lobibox.notify('success', {
								icon : false,
								delay : false,
								title : '提示',
								msg : '删除成功'
							});
						}
					});
				}
			}

		});
	},
	editbtn: function(a){
		var that = this;
		var array = new Array();
		array[0] = a;
			$.ajax({
				url : "resourceEquipeidt.html?time=new Date().getTime()",
				type : "GET",
				async : false,
				dataType : "html",
				success : function(data) {
					// 将返回的页面（主内容部分）填入主页面div中
					that.loadDevType("devdetailttypeName");
					that.loadDevDetail(array[0]);
					that.loadEidtTable(array[0]);
					$("#main_content").html(data);
					
				}
		});
	},
	loadInfo:function(){
		$.ajax({
			url : "/Device/Admin/selectDeviceResource?time=new Date().getTime()",
			type : "get",
			data : {
				'deviceName' : $('#deviceName').val()
			},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_equipmsg").bootstrapTable('load', data);
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
	deleteDevice:function(a){
		var that = this;
		var array = new Array();
		$.each(a,function(i,item){
			array[i] = item.id;
		});
		$.ajax({
			url : "/Device/Admin/deleteDevice?time=new Date().getTime()",
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
	deleteDevicebyOne:function(a){
		var that = this;
		var array = new Array();
		array[0] = a;
		$.ajax({
			url : "/Device/Admin/deleteDevice?time=new Date().getTime()",
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
	insertDevice:function(){
		var that = this;
		$.ajax({
			url : "/Device/Admin/insertDevice?time=new Date().getTime()",
			type : "get",
			data : {
				'devName' : $('#adddevName').val(),
				'devModel' : $('#adddevModel').val(),
				'vender' : $('#addvender').val(),
				'tDevType_id' : $('#addtypeName').val(),
				'custodian' : $('#addcustodian').val(),
				'remark' : $('#addremark').val()
			},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#myModalEquipmsg").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增设备成功'
	                });
				}else{
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增设备失败'
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
	insertDeviceDetail:function(){
		var that = this;
		$.ajax({
			url : "/DeviceDetail/Admin/insertDeviceDetail?time=new Date().getTime()",
			type : "get",
			data : {
				'devSn' : $('#adddevSn').val(),
				'rfidNo' : $('#addRFID').val(),
				'devStatus' : $('#adddevStatus').val(),
				'tDevice_id' : $('#devId').val(),
				'remark' : $('#adddetailremark').val(),
				'labName' : $('#addlabName').val(),
				'cabNo' : $('#addcabNo').val()
			},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#myModalSubEquipmsg").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增子设备成功'
	                });
				}else{
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增子设备失败'
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
	loadDevType:function(id){
		$.ajax({
			url : "/DevType/Admin/selectDevType?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				for(var i=0;i<data.length;i++){
					$("#"+id).append("<option value='"+data[i].id+"'>"+data[i].typeName+"</option>");
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
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '服务器出错'
                });
			}
		});
	},
	loadEidtTable:function(id){
		$.ajax({
			url : "/DeviceDetail/Admin/selectDevDetailInfo?time=new Date().getTime()",
			type : "get",
			data : {
				'id':id
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
	}
};
$(function() {
	var index = new Index.Equipmsg();
	index.init();
});