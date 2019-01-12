if (!window.Index)
	Index = {};
Index.Labloan = function() {
};

Index.Labloan.prototype = {
	init : function() {
		var that = this;
		that.Labloan();
		that.loadInfo();
		$("#checkbtn").click(function(){
			that.loadInfo();
		});
		$("#auditStatus").change(function() {
			that.loadInfo();
		});
		$("#passbtn").click(function(){
			var flag = true;
			var a= $("#table_labloan").bootstrapTable('getSelections');
			if(a.length==0){
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a,function(i,item){
				if(item.auditStatusName!="预约"){
					flag = false;
				}
			});
			if(flag){
				that.updateLabApplyStatus(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择预约的记录条'
                });
			}
		});
		$("#refusebtn").click(function(){
			var flag = true;
			var a= $("#table_labloan").bootstrapTable('getSelections');
			if(a.length==0){
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a,function(i,item){
				if(item.auditStatusName!="预约"){
					flag = false;
				}
			});
			if(flag){
				that.updateRefuseLabApplyStatus(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择预约的记录条'
                });
			}
		});
		$("#cancelbtn").click(function(){
			var flag = true;
			var a= $("#table_labloan").bootstrapTable('getSelections');
			if(a.length==0){
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择'
                });
				return;
			}
			$.each(a,function(i,item){
				if(item.auditStatusName=="预约"||item.auditStatusName=="还回"){
					flag = false;
				}
			});
			if(flag){
				that.cancelLabApplyStatus(a);
			}else{
				Lobibox.notify('warning', {
                    icon: false,
                    title: '提示',
                    msg: '请选择已处理的记录条'
                });
			}
		});
		
	},
	Labloan : function() {
		var that = this;
		$('#table_labloan').bootstrapTable({
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
			uniqueId : "labapplyid", // 每一行的唯一标识，一般为主键列
			showToggle : false, // 是否显示详细视图和列表视图的切换按钮
			cardView : false, // 是否显示详细视图
			detailView : false, // 是否显示父子表
			columns : [ {
				checkbox : true
			}, {
				field : 'labName',
				title : '实验室名称',
					formatter: function operateFormatter(value, row, index) {
						return [
							'<a class="labName" data-toggle="modal" data-target="#labInfo">'+row.labName+'</a>',
						].join('');
					},
					events:{
						'click .labName': function(e, value, row, index) {
							$("#MlabStatus").val(row.labStatusName);
							$("#MuserNum").val(row.userNum);
						}
					}
			}, {
				field : 'startTime',
				title : '使用开始时间'
			}, {
				field : 'endTime',
				title : '实验结束时间'
			}, {
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
						$("#Mluser").val(row.luser);
					}
				}
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
					}else if(row.auditStatusName=='拒绝'||row.auditStatusName=='借出'){
						if(new Date(row.startTime).getTime()<=new Date().getTime()){
							return [
								'<button type="button" class="btn btn-info" disabled="true">借出</button>'
							].join('');
						}else{
							return [
								'<button type="button" class="btn btn-info cancelbtn">取消</button>'
							].join('');
						}
					}else if(row.auditStatusName=='还回'){
						return [
							'<button type="button" class="btn btn-default" disabled="true">完成</button>'
						].join('');
					}
				},
				events:{
					'click .passbtn': function(e, value, row, index) {
						that.updateLabApplyStatusByOne(row.labapplyid);
					},
					'click .refusebtn': function(e, value, row, index) {
						that.updateRefuseLabApplyStatusByOne(row.labapplyid);
					},
					'click .cancelbtn': function(e, value, row, index) {
						that.cancelLabApplyStatusByOne(row.labapplyid);
					}
				}
			}]
		});
	},
	loadInfo:function(){
		$.ajax({
			url : "/LabApply/Admin/selectLabUsingInfo?time=new Date().getTime()",
			type : "get",
			data : {'auditStatus':$('#auditStatus').val(),'name':$('#keyword').val()},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_labloan").bootstrapTable('load', data);
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
	updateLabApplyStatus:function(a){
		var labapplyarray = new Array();
		var that = this;
		$.each(a,function(i,item){
			labapplyarray[i] = item.labapplyid;
		});
		$.ajax({
			url : "/LabApply/Admin/updatePassLabApply?time=new Date().getTime()",
			type : "post",
			data : {'labapplyids': labapplyarray},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
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
	updateRefuseLabApplyStatus:function(a){
		var labapplyarray = new Array();
		var that = this;
		$.each(a,function(i,item){
			labapplyarray[i] = item.labapplyid;
		});
		$.ajax({
			url : "/LabApply/Admin/updateRefuseLabApply?time=new Date().getTime()",
			type : "post",
			data : {'labapplyids': labapplyarray},
			success : function(data) {
				if (data == "success") {
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
	cancelLabApplyStatus:function(a){
		var labapplyarray = new Array();
		var that = this;
		$.each(a,function(i,item){
			labapplyarray[i] = item.labapplyid;
		});
		$.ajax({
			url : "/LabApply/Admin/cancelLabApplyStatus?time=new Date().getTime()",
			type : "post",
			data : {'labapplyids': labapplyarray},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
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
	updateLabApplyStatusByOne:function(a){
		var labapplyarray = new Array();
		var that = this;
		labapplyarray[0] = a;
		$.ajax({
			url : "/LabApply/Admin/updatePassLabApply?time=new Date().getTime()",
			type : "post",
			data : {'labapplyids': labapplyarray},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
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
	updateRefuseLabApplyStatusByOne:function(a){
		var labapplyarray = new Array();
		var that = this;
		labapplyarray[0] = a;
		$.ajax({
			url : "/LabApply/Admin/updateRefuseLabApply?time=new Date().getTime()",
			type : "post",
			data : {'labapplyids': labapplyarray},
			success : function(data) {
				if (data == "success") {
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
	cancelLabApplyStatusByOne:function(a){
		var labapplyarray = new Array();
		var that = this;
		labapplyarray[0] = a;
		$.ajax({
			url : "/LabApply/Admin/cancelLabApplyStatus?time=new Date().getTime()",
			type : "post",
			data : {'labapplyids': labapplyarray},
			success : function(data) {
				if (data == "success") {
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '更新成功'
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
	var index = new Index.Labloan();
	index.init();
});