if (!window.Index)
	Index = {};
Index.Main = function() {
};

Index.Main.prototype = {

	init : function() {
		var that = this;
		that.loadMenu();
		that.loadInfo();
		that.checkRecord();
		that.checkDevApply();
		that.checkLabApply();
		$("#userPwd").blur(function() {
			that.checkpassword();
		});
		$("#confirmPassword").blur(function() {
			that.checkconfirmpassword();
		});
		$("#noticenav").click(function() {
			$("#welcome").css("display", "none");
			$("#daily").css("display", "block");
		});
		$("#navAdminName").click(function() {
			that.loadInfo();
		});
		$("#submitBut").click(
				function() {
					if ($('#email').val() == '' || $('#phone').val() == '') {
						Lobibox.notify('error', {
							icon : false,
							delay : false,
							title : '提示',
							msg : '邮箱或电话号码不可以为空'
						});
						return;
					}
					if (that.checkemail($('#email').val())
							&& that.checkphone($('#phone').val())) {
						that.updateInfo();
					}

				});
		$(".noticeNav").click(function() {// 公告管理
			that.dailyNotice();
			$(".androidUl").css("display", "none");
		});
		$(".topicNav").click(function() {
			that.dailyTopic();
			$(".androidUl").css("display", "none");
		});
		$(".attendanceNav").click(function() {
			that.dailyAttendance();
			$(".androidUl").css("display", "none");		
		});
		$(".equipNav").click(function() {
			that.dailyEquip();
			$(".androidUl").css("display", "none");
		});
		$(".labNav").click(function() {
			that.dailyLab();
			$(".androidUl").css("display", "none");
		});
		$(".projNav").click(function() {
			that.dailyProject();
			$(".androidUl").css("display", "none");
		});
		$(".classBrandNav").click(function() {
			that.dailyIntelligence();
		});
		$(".tempShowNav").click(function(){
			that.dailyTemp();
		});
		$(".switchNav").click(function() {
			that.dailySwitch();
		});
		$("#recordNav").click(function() {
			that.dataRecord();
		});
		$("#nodeNav").click(function() {
			that.dataNode();
		});
		$("#userNav").click(function() {
			that.dataUser();
		});
		$("#resourceLab").click(function() {
			that.resourceLab();
		});
		$("#resourceDevice").click(function() {
			that.resourceDevice();
		});
		$("#resourceDeviceCheck").click(function() {
			that.resourceDeviceCheck();
		});
		$("#labLend").click(function() {
			that.useLabloan();
		});
		$("#devLend").click(function() {
			that.useEquiploan();
		});
		$(".leave").click(function() {// 退出登录
			that.leave();
		});
		
		$(".androidtitle").click(function() {
			if ($(".androidUl").css("display") == "none") {
				$(".androidUl").css("display", "block");
			} else {
				$(".androidUl").css("display", "none");
			}
		});
	},
	loadMenu : function() {
		$(".list_dt").on("click",function() {
					$('.list_dd').stop();
					$(this).siblings("dt").removeAttr("id");
					if ($(this).attr("id") == "open") {
						$(this).removeAttr("id").siblings("dd").slideUp();
					} else {
						$(this).attr("id", "open").next().slideDown().siblings("dd").slideUp();
					}
				});
	},
	// 验证密码
	checkpassword : function() {
		var userPwd = $("#userPwd").val();
		var reg = /^[A-Za-z0-9]{6,20}$/;
		var result = reg.test(userPwd);
		if (userPwd == "" || userPwd == " ") {
			$("#passwordDiv").html("请输入合法密码");
		} else if (result == false) {
			$("#passwordDiv").html("请输6-20位的字母或数字");

		} else {
			$("#passwordDiv").html("");
		}
	},
	// 验证确定密码
	checkconfirmpassword : function() {
		var userPwd = $("#userPwd").val();
		var confirmPassword = $("#confirmPassword").val();
		if (confirmPassword == "" || confirmPassword == " ") {
			$("#conpasswordDiv").html("请再次输入密码");
		} else if (confirmPassword != userPwd) {
			$("#conpasswordDiv").html("两次密码不一致");

		} else {
			$("#conpasswordDiv").html("");
		}
	},
	// 验证邮箱
	checkemail : function(email) {
		var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		var result = reg.test(email);
		if (result == false) {
			Lobibox.notify('error', {
				icon : false,
				delay : false,
				title : '提示',
				msg : '请输入正确的邮箱'
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
				icon : false,
				delay : false,
				title : '提示',
				msg : '请输入正确的电话'
			});
			return false;
		}
		return true;
	},
	dailyNotice : function() {
		$.ajax({
			url : "dailyNotice.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyTopic : function() {
		$.ajax({
			url : "dailyTopic.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyAttendance : function() {
		$.ajax({
			url : "dailyAttendance.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyEquip : function() {
		$.ajax({
			url : "dailyEquip.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyLab : function() {
		$.ajax({
			url : "dailyLab.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyProject : function() {
		$.ajax({
			url : "dailyProject.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyIntelligence : function() {
		$.ajax({
			url : "dailyIntelligence.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailyTemp : function() {
		$.ajax({
			url : "dailyTemp.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dailySwitch:function(){
		$.ajax({
			url : "dailySwitch.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dataRecord : function() {
		$.ajax({
			url : "dataRecord.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dataNode : function() {
		$.ajax({
			url : "dataNode.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	dataUser : function() {
		$.ajax({
			url : "dataUser.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	resourceLab : function() {
		$.ajax({
			url : "resourceLabmsg.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	resourceDevice : function() {
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
	},
	resourceDeviceCheck : function() {
		$.ajax({
			url : "resourceInventory.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	useEquiploan : function() {
		$.ajax({
			url : "useEquiploan.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	useLabloan : function() {
		$.ajax({
			url : "useLabloan.html?time=new Date().getTime()",
			type : "GET",
			async : false,
			dataType : "html",
			success : function(data) {
				// 将返回的页面（主内容部分）填入主页面div中
				$("#main_content").html(data);
			}
		});
	},
	leave : function() {
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
			message : "确定退出吗?",
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
								msg : '退出成功'
							});
						}
					});
				}
			}

		});
	},

	loadInfo : function() {
		$.ajax({
			url : "/adminindex?time=new Date().getTime()",
			type : "post",
			dataType : "json",
			success : function(data) {
				$("#navAdminName").text(data.administrators.realName);
				$("#contentUserName").text(data.administrators.realName);
				for ( var k in data.administrators) {
					var type = $("[name=" + k + "]").prop("type");

					$("textarea[name=" + k + "]").val(data.administrators[k]);
					if (type != undefined && type != null) {
						if (type == "text") {
							if (data.administrators[k] != ""
									&& data.administrators[k] != null) {
								$("[name=" + k + "]").val(
										data.administrators[k]);
							}
						} else if (type == "radio") {
							if (data.administrators[k] != ""
									&& data.administrators[k] != null) {
								$(
										"[name=" + k + "][value="
												+ data.administrators[k] + "]")
										.attr("checked", "true");
							}
						} else if (type == "checkbox") {
							var ckeckboxVal = data.administrators[k];
							if (ckeckboxVal != "" && ckeckboxVal != undefined
									&& ckeckboxVal != null) {
								if (ckeckboxVal.length == 1) {
									$(
											"[name=" + k + "][value="
													+ ckeckboxVal + "]").attr(
											"checked", "true");
								} else {
									var str = ckeckboxVal.split(",");
									for (var i = 0; i < str.length; i++) {
										$(
												"[name=" + k + "][value="
														+ str[i] + "]").attr(
												"checked", "true");
									}
								}
							}
						} else if (type == "select-one") {
							$("[name=" + k + "]").find(
									"option[value=" + data.administrators[k]
											+ "]").attr("selected", true);

						} else if (type == "password") {
							$("[name=" + k + "]").val("");
						}
					}
				}
			},
			error : function(data) {
				Lobibox.notify('error', {
					icon : false,
					delay : false,
					title : '提示',
					msg : '服务器出错'
				});
			}

		});

	},
	updateInfo : function() {
		$.ajax({
			url : "/Admin/updateInfo?time=new Date().getTime()",
			type : "post",
			data : $("#adminform").serializeArray(),
			contentType : "application/x-www-form-urlencoded",
			success : function(data) {
				if (data == "success") {
					$("#myModal").modal('hide');
					Lobibox.notify('success', {
						icon : false,
						title : '提示',
						msg : '更新成功'
					});
				} else {
					Lobibox.notify('error', {
						icon : false,
						title : '提示',
						msg : '更新失败'
					});
				}
			},
			error : function(data) {
				Lobibox.notify('error', {
					icon : false,
					delay : false,
					title : '提示',
					msg : '服务器出错'
				});
			}

		});
	},
	checkRecord : function() {
		$.ajax({
			url : "/EntranceGuardRecord/Admin/checkRecord?time=new Date().getTime()",
			type : "post",
			success : function(data) {
				if (data == false) {
					return;
				}
				Lobibox.notify('info', {
					icon : false,
					delay : false,
					sound : false,
					title : '通知',
					msg : '有超过30天的日志需要清理',
				    
				});
			}
		});
	},
	checkDevApply : function() {
		$.ajax({
			url : "/DeviceApply/Admin/checkDevApply?time=new Date().getTime()",
			type : "post",
			success : function(data) {
				if (data == false) {
					return;
				}
				Lobibox.notify('info', {
					icon : false,
					delay : false,
					sound : false,
					title : '通知',
					msg : '有设备预约需要审核',
				});
			}
		});
	},
	checkLabApply : function() {
		$.ajax({
			url : "/LabApply/Admin/checkLabApply?time=new Date().getTime()",
			type : "post",
			success : function(data) {
				if (data == false) {
					return;
				}
				Lobibox.notify('info', {
					icon : false,
					delay : false,
					sound : false,
					title : '通知',
					msg : '有实验室预约需要审核'
				});
			}
		});
	}

};
$(function() {
	var index = new Index.Main();
	index.init();
});

$.ajaxSetup({complete:function(xhr,status){
	// 若HEADER中含有REDIRECT说明后端想重定向
	if("REDIRECT" == xhr.getResponseHeader("REDIRECT")){ 
        // 将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
        	window.location.href = "/adminlogin";
        }
}});


