if (!window.Index)
	Index = {};
Index.User = function() {
};

Index.User.prototype = {
	init : function() {
		var that = this;
		that.Usertable();
		that.initFileInput();
		that.loadInfo();
		$("#checkbtn").click(function() {
			that.loadInfo();
		});
		$("#addbtn").click(function() {
			if($('#adduserName').val()==""||$('#addemail').val()==""||$('#addrealName').val()==""||$('#addphone').val()==""){
				Lobibox.notify('error', {
                    icon: false,
                    title: '提示',
                    msg: '请补全用户信息'
                });
				return;
			}
			if(that.checkemail($('#addemail').val())&&that.checkphone($('#addphone').val())){
				that.insertUser();
			}
		});
		$("#Duser_add").click(function() {
			$('#addForm')[0].reset();
		});
		$("#Duser_edit").click(
				function() {
					var a = $("#table_user").bootstrapTable('getSelections');
					if (a.length == 1) {
						$('#edituserform')[0].reset();
						$("#Duser_edit").attr("data-target", "#myModalUser2");
						$("#edituserType").find(
								"option:contains('" + a[0].userTypeName + "')")
								.attr("selected", true);
						$("#editgrants").val(a[0].grants);
						$("#editUserId").val(a[0].id);
						if(a[0].status=='0'){
							$('#isFrozen').attr("checked", 'checked');
						}
						$("#editbtn").click(function() {
							that.updateUser(a);
						});
					} else if (a.length == 0) {
						Lobibox.notify('warning', {
		                    icon: false,
		                    title: '提示',
		                    msg: '请选择'
		                });
						$("#Duser_edit").attr("data-target", "");
					} else {
						Lobibox.notify('warning', {
		                    icon: false,
		                    title: '提示',
		                    msg: '不可多选'
		                });
						$("#Duser_edit").attr("data-target", "");
					}
				});

	},
	Usertable : function() {
		$('#table_user').bootstrapTable({
			method : 'get', // 请求方式（*）
			toolbar : '#toolbar', // 工具按钮用哪个容器
			striped : true, // 是否显示行间隔色
			cache : false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			pagination : true, // 是否显示分页（*）
			sortable : true, // 是否启用排序
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
				field : 'userName',
				title : '用户名'
			}, {
				field : 'realName',
				title : '真实姓名'
			}, {
				field : 'userTypeName',
				title : '用户类别',
				sortable: true
			}, {
				field : 'email',
				title : '电子邮箱'
			}, {
				field : 'phone',
				title : '手机'

			}, {
				field : 'statusName',
				title : '账号状态',
				sortable: true
			},{
				field : '',
				title : '联系',
				formatter: function operateFormatter(value, row, index) {
					return [
						'<button type="button" class="Qrcode btn btn-default" data-toggle="modal" data-target="#qrcode"><span class="glyphicon glyphicon-qrcode" aria-hidden="true"></span></button>',
					].join('');
				},
				events:{
					'click .Qrcode': function(e, value, row, index) {
						$("#qrcodeImg").attr("src", "/UserGrant/Admin/QRcode?name="+row.realName+"&email="+row.email+"&phone="+row.phone);
					}
				}
			} ]
		});
		
	},
	initFileInput : function() {
		var that = this;
		$("#f_upload").fileinput({
			language : 'zh', // 设置语言
			uploadUrl : "/Admin/insertUsersByFile", // 上传的地址
			allowedFileExtensions : [ 'xlsx', 'xls' ],// 接收的文件后缀
			uploadAsync : true, // 默认异步上传
			showUpload : true, // 是否显示上传按钮
			showRemove : true, // 显示移除按钮
			showPreview : true, // 是否显示预览
			showCaption : true,// 是否显示标题
			browseClass : "btn btn-primary", // 按钮样式
			dropZoneEnabled : false,// 是否显示拖拽区域
			maxFileCount : 1, // 表示允许同时上传的最大文件个数
			enctype : 'multipart/form-data',
			validateInitialCount : true
		});
		
		$("#f_upload").on("fileuploaded",
				function(event, data, previewId, index) {
					if(data.response.status=='success'){
						$("#myModalUser3").modal('hide');
						that.loadInfo();
						Lobibox.notify('success', {
		                    icon: false,
		                    title: '提示',
		                    msg: '新增成功'
		                });
					}else if(data.response.status=='fail'){
						Lobibox.notify('error', {
		                    icon: false,
		                    title: '提示',
		                    msg: '文件出错'
		                });
					}else if (data.response.status == 'repeat') {
						Lobibox.notify('error', {
		                    icon: false,
		                    title: '提示',
		                    msg: '不可插入已存在用户'
		                });
						$(event.target)
			              .fileinput('clear')
			              .fileinput('unlock');
			            $(event.target)
			              .parent()
			              .siblings('.fileinput-remove')
			              .hide();
					}
				});
		// 上传前

		$('#f_upload').on('filepreupload',
				function(event, data, previewId, index) {

				});

	},
	loadInfo : function() {
		$.ajax({
			url : "/Admin/selectUsersByRealName?time=new Date().getTime()",
			type : "get",
			data : {
				'realName' : $('#realNameInput').val()
			},
			contentType : "json",
			dataType : "json",
			success : function(data) {
				$("#table_user").bootstrapTable('load', data);
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
	insertUser : function() {
		var that = this;
		$.ajax({
			url : "/Admin/insertUsers?time=new Date().getTime()",
			type : "post",
			data : $("#addForm").serializeArray(),
			contentType : "application/x-www-form-urlencoded",
			success : function(data) {
				if (data == "success") {
					$("#myModalUser1").modal('hide');
					that.loadInfo();
					Lobibox.notify('success', {
	                    icon: false,
	                    title: '提示',
	                    msg: '新增成功'
	                });
				}else if(data == "repeat"){
					Lobibox.notify('error', {
	                    icon: false,
	                    title: '提示',
	                    msg: '用户已存在'
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
	updateUser : function(a) {
		var that = this;
		var status = '1';
		var username = '';
		if ($("#isFrozen").is(":checked")) {
			status = '0';
		}
		if ($("#isReset").is(":checked")) {
			username = a[0].userName;
		}
		$.ajax({
			url : "/Admin/updateUser?time=new Date().getTime()",
			type : "get",
			data : {
				'userName' : username,
				'userType' : $('#edituserType').val(),
				'grants' : $('#editgrants').val(),
				'id' : $('#editUserId').val(),
				'status' : status
			},
			contentType : "json",
			success : function(data) {
				if (data == "success") {
					$("#myModalUser2").modal('hide');
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
	// 验证邮箱
	checkemail : function(email) {
		var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		var result = reg.test(email);
		if (result == false) {
			Lobibox.notify('error', {
                icon: false,
                delay: false,
                title: '提示',
                msg: '请输入正确的邮箱'
            });
			return false;
		}
		return true;
	},
	// 验证手机号
	checkphone : function(phone) {
		var reg = /^1[3|5|8]\d{9}$|^6\d{5}$|^6\d{4}$/;
		var result = reg.test(phone);
		if (result == false) {
			Lobibox.notify('error', {
                icon: false,
                delay: false,
                title: '提示',
                msg: '请输入正确的电话'
            });
			return false;
		}
		return true;
	}
};
$(function() {
	var index = new Index.User();
	index.init();
});