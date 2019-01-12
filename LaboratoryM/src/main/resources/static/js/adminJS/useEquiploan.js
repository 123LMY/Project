if (!window.Index)
	Index = {};
Index.Equiploan = function() {
};

Index.Equiploan.prototype = {
	init : function() {
		var that = this;
		that.Equiploan();
		that.loadInfo();
		$("#checkbtn").click(function() {
			that.loadInfo();
		});
		$("#auditStatus").change(function() {
			that.loadInfo();
		});
		$("#passbtn").click(function() {
			var flag = true;
			var a = $("#table_Equiploan").bootstrapTable('getSelections');
			if (a.length == 0) {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a, function(i, item) {
				if (item.auditStatusName != "预约") {
					flag = false;
				}
			});
			if (flag) {
				that.updateDevApplyStatus(a, '2');
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择预约的记录条'
                });
			}
		});
		$("#refusebtn").click(function() {
			var flag = true;
			var a = $("#table_Equiploan").bootstrapTable('getSelections');
			if (a.length == 0) {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a, function(i, item) {
				if (item.auditStatusName != "预约") {
					flag = false;
				}
			});
			if (flag) {
				that.updateDevApplyStatus(a, '1');
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择预约的记录条'
                });
			}
		});
		$("#cancelbtn").click(function() {
			var flag = true;
			var a = $("#table_Equiploan").bootstrapTable(
				'getSelections');
			if (a.length == 0) {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a, function(i, item) {
				if (item.auditStatusName == "预约"|| item.auditStatusName == "还回"|| item.devStatusName == "已预约") {
					flag = false;
				}
			});
			if (flag) {
				that.updateDevApplyStatus(a, '0');
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '该申请状态不能取消'
                });
			}
		});
		$("#lendbtn").click(function() {
			var flag = true;
			var a = $("#table_Equiploan").bootstrapTable('getSelections');
			if (a.length == 0) {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a, function(i, item) {
				if (item.devStatusName != "已预约") {
					flag = false;
				}
			});
			if (flag) {
				that.updateDevDetailStatus(a,'1');
				that.updateDevApplyStatus(a, '3');
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择已预约的设备记录条'
                });
			}
		});
		$("#backbtn").click(function() {
			var flag = true;
			var a = $("#table_Equiploan").bootstrapTable('getSelections');
			if (a.length == 0) {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a, function(i, item) {
				if (item.auditStatusName != "借出") {
					flag = false;
				}
			});
			if (flag) {
				$("#backbtn").attr("data-target", "#devreturnModal");
				$('#confirmbtn').click(function(){
					$('#devreturnform')[0].reset();
					that.updateDevDetailStatus(a,$('#devStatus').val());
					that.updateDevApplyStatus(a, '4');
					$("#devreturnModal").modal('hide');
				});
			} else {
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择已借出的记录条'
                });
			}
		});
	},
	Equiploan : function() {
		var that = this;
		$('#table_Equiploan').bootstrapTable({
			method : 'get', // 请求方式（*）
			toolbar : '#toolbar', // 工具按钮用哪个容器
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, // 是否显示分页（*）
			sortable : false, // 是否启用排序
			sortOrder : "asc", // 排序方式
			sidePagination : "client", // 分页方式：client客户端分页，server服务端分页（*）
			pageNumber : 1, // 初始化加载第一页，默认第一页
			pageSize : 10, // 每页的记录行数（*）
			pageList : [ 10, 25, 50, 100 ], // 可供选择的每页的行数（*）
			strictSearch : true,
			showColumns : false, // 是否显示所有的列
			showRefresh : false, // 是否显示刷新按钮
			minimumCountColumns : 2, // 最少允许的列数
			clickToSelect : true, // 是否启用点击选中行
			uniqueId : "devApplyId", // 每一行的唯一标识，一般为主键列
			showToggle : false, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'devName',
				title : '设备名称',
				formatter: function operateFormatter(value, row, index) {
					return [
						'<a class="dev" data-toggle="modal" data-target="#devdetaiInfo">'+row.devName+'</a>',
					].join('');
				},
				events:{
					'click .dev': function(e, value, row, index) {
						$("#MdevModel").val(row.devModel);
						$("#MdevSn").val(row.devSn);
						$("#MdevStatus").val(row.devStatusName);
					}
				}
			},{
				field : 'auditStatusName',
				title : '预约状态'
			}, {
				field : 'reservations',
				title : '预约人',
				formatter: function operateFormatter(value, row, index) {
					return [
						'<a class="reservations" data-toggle="modal" data-target="#appointmentsInfo">'+row.reservations+'</a>',
					].join('');
				},
				events:{
					'click .reservations': function(e, value, row, index) {
						$("#MproName").val(row.proName);
						$("#Mpurpose").val(row.purpose);
						$("#Mduser").val(row.duser);
					}
				}
			}, {
				field : 'startDate',
				title : '开始时间'
			}, {
				field : 'endDate',
				title : '结束时间'
			}, {
				field : 'operation',
				title : '操作',
				width : '15%',
				align : 'center',
				formatter: function operateFormatter(value, row, index) {
					if(row.auditStatusName=='预约'){
						return [
							'<button type="button" class="btn btn-success passbtn" style="margin-right:3%;">通过</button>',
							'<button type="button" class="btn btn-danger refusebtn">拒绝</button>'
						].join('');
					}else if(row.auditStatusName=='通过'){
						if(row.devStatusName=='已预约'){
							return [
								'<button type="button" class="btn btn-success lendbtn" style="margin-right:3%;">借出</button>',
								'<button type="button" class="btn btn-info cancelbtn"  disabled="true">取消</button>'
							].join('');
						}else{
							return [
								'<button type="button" class="btn btn-success lendbtn" style="margin-right:3%;" disabled="true">借出</button>',
								'<button type="button" class="btn btn-info cancelbtn">取消</button>'
							].join('');
						}
					}else if(row.auditStatusName=='拒绝'){
						return [
							'<button type="button" class="btn btn-info cancelbtn">取消</button>'
						].join('');
					}else if(row.auditStatusName=='借出'){
						return [
							'<button type="button" class="btn btn-success backbtn" data-toggle="modal" data-target="">还回</button>'
						].join('');
					}else{
						return [
							'<button type="button" class="btn btn-default" disabled="true">完成</button>'
						].join('');
					}
				},
				events:{
					'click .passbtn': function(e, value, row, index) {
						that.updateDevApplyStatusByOne(row.devApplyId, '2');
					},
					'click .refusebtn': function(e, value, row, index) {
						that.updateDevApplyStatusByOne(row.devApplyId, '1');
					},
					'click .lendbtn': function(e, value, row, index) {
						that.updateDevDetailStatusByOne(row.devDetailId,'1');
						that.updateDevApplyStatusByOne(row.devApplyId, '3');
					},
					'click .cancelbtn': function(e, value, row, index) {
						that.updateDevApplyStatusByOne(row.devApplyId, '0');
					},
					'click .backbtn': function(e, value, row, index) {
						$(this).attr("data-target", "#devreturnModal");
						$('#confirmbtn').click(function(){
							$('#devreturnform')[0].reset();
							that.updateDevDetailStatusByOne(row.devDetailId,$('#devStatus').val());
							that.updateDevApplyStatusByOne(row.devApplyId, '4');
							$("#devreturnModal").modal('hide');
						});
						
					}
					
				}
			}]
		});
	},
	loadInfo : function() {
		$.ajax({
			url : "/DeviceApply/Admin/selectDevApplyInfo?time=new Date().getTime()",
			type : "get",
			data : {
				'auditStatus' : $('#auditStatus').val(),
				'name' : $('#keyword').val()
			},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_Equiploan").bootstrapTable('load', data);

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
	updateDevApplyStatus : function(a, status) {
		var devapplyarray = new Array();
		var that = this;
		$.each(a, function(i, item) {
			devapplyarray[i] = item.devApplyId;
		});
		$.ajax({
			url : "/DeviceApply/Admin/updateDevApplyStatus?time=new Date().getTime()",
			type : "post",
			data : {
				'devapplyids' : devapplyarray,
				'devapplystatus' : status
			},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '修改成功'
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
	updateDevDetailStatus : function(a,status) {
		var devdetailarray = new Array();
		$.each(a, function(i, item) {
			devdetailarray[i] = item.devDetailId;
		});
		$.ajax({
			url : "/DeviceDetail/Admin/updateDevDetailStatus?time=new Date().getTime()",
			type : "post",
			data : {
				'devdetailids' : devdetailarray,
				'devdetailstatus' : status
			},
			success : function(data) {

			},
			error : function(data) {
				alert("服务器出错了！！");
			}

		});
	},
	updateDevApplyStatusByOne : function(a, status) {
		var devapplyarray = new Array();
		var that = this;
		devapplyarray[0] = a;
		$.ajax({
			url : "/DeviceApply/Admin/updateDevApplyStatus?time=new Date().getTime()",
			type : "post",
			data : {
				'devapplyids' : devapplyarray,
				'devapplystatus' : status
			},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '修改成功'
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
	updateDevDetailStatusByOne : function(a,status) {
		var devdetailarray = new Array();
		devdetailarray[0] = a;
		$.ajax({
			url : "/DeviceDetail/Admin/updateDevDetailStatus?time=new Date().getTime()",
			type : "post",
			data : {
				'devdetailids' : devdetailarray,
				'devdetailstatus' : status
			},
			success : function(data) {

			},
			error : function(data) {
				alert("服务器出错了！！");
			}

		});
	}
};
$(function() {
	var index = new Index.Equiploan();
	index.init();
});