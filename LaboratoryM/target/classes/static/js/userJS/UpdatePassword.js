if (!window.Manager)
	Manager = {};

Manager.UpdatePassword = function() {
	this.oldPasswordFlag = false;
	this.newPasswordFlag = false;
	this.resPasswordFlag = false;

};

Manager.UpdatePassword.prototype = {
	init : function() {
		var that = this;
		/*点击修改密码导航栏*/
		$(".updatePassword_navbar").click(function() {
			that.loadUpdatePwdPage();
			that.checkOldPassword();
			that.checkPassword();
			that.checkRepet();
			that.updatePassword();
			 $("#left_content").css("display","none");
		});

	},

	/*加载修改密码页面*/
	loadUpdatePwdPage : function() {
		var ts = new Date().getTime();
		$.ajax({
			url : "/User/loadPasswordPage?time="+ts,
			type : "POST",
			async : false,
			contentType : "application/json",
			success : function(data) {
				$("#right_content").html(data);
			},
			error : function() {
				alert("加载失败，请刷新页面重试");
			}
		});
		;
	},

	/*验证输入密码的格式是否正确*/
	checkPassword : function() {
		var obj = this;
		$("#newPassword").blur(function() {
			var reg = /^[a-zA-Z0-9]{5,19}$/;
			var newPass = $("#newPassword").val();
			var errLabel = $("#new_err_label");
			if (newPass == "") {
				$("#newPassword").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				obj.newPasswordFlag = false;
			} else {
				if (!reg.exec(newPass)) {
					$("#newPassword").css("border-color", "red");
					errLabel.html("×！");
					errLabel.css("color", "red");
					obj.newPasswordFlag = false;
				} else {
					$("#newPassword").css("border-color", "");
					errLabel.html("√");
					errLabel.css("color", "green");
					obj.newPasswordFlag = true;
				}
			}
		});

	},

	/*检验输入的两个密码是否一致*/
	checkRepet : function() {
		var obj = this;
		$("#repassword").blur(function() {
			var newPass = $("#newPassword").val();
			var rePass = $("#repassword").val();
			var errLabel = $("#rep_err_label");
			if (newPass == rePass && rePass != "") {
				$("#repassword").css("border-color", "");
				errLabel.html("√");
				errLabel.css("color", "green");
				obj.resPasswordFlag = true;
			} else {
				$("#repassword").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				obj.resPasswordFlag = false;
			}
		});
	},

	/* 检验输入原密码是否正确 */
	checkOldPassword : function() {
		var obj = this;
		$("#oldUserPwd").blur(function() {
			var oldPassword = $("#oldUserPwd").val();
			if (oldPassword == "") {
				$("#old_err_label").html("×");
				$("#old_err_label").css("color", "red");
				obj.oldPasswordFlag = false;
			} else {
				var ts = new Date().getTime();
				$.ajax({
					url : "/User/checkPassword?time="+ts,
					type : "post",
					contentType : "application/json",
					data : JSON.stringify({
						"oldPassword" : oldPassword
					}),
					success : function(data) {
						if (data == "the same") {
							$("#old_err_label").html("√");
							$("#old_err_label").css("color", "green");
							obj.oldPasswordFlag = true;
						}
						if (data == "different") {
							$("#old_err_label").html("×");
							$("#old_err_label").css("color", "red");
							obj.oldPasswordFlag = false;
						}
					}
				});
			}
		});

	},

	/*提交修改后的密码*/
	updatePassword : function() {
		var obj = this;
		$("#updataPasBtn").click(function() {	
					var newPas = $("#newPassword").val();
					var oldPas = $("#oldUserPwd").val();
					if(oldPas == null){
						toastr.error("原密码不能为空");
						return ;
					}
					if(newPas == null){
						toastr.error("新密码不能为空");
						return ;
					}
					if($("#repassword").val() == null){
						toastr.error("确认密码不能为空");
						return ;
					}
					if (obj.newPasswordFlag == true && obj.oldPasswordFlag == true
							&& obj.resPasswordFlag == true) {	
						if(newPas == oldPas){
							toastr.error("新密码不能与原密码一致！");
							return;
						}else{
							var ts = new Date().getTime();
							$.ajax({
								url : "/User/updatePassword?time="+ts,
								type : "post",
								contentType : "application/json",
								data : JSON.stringify({
									"newPassword" : newPas
								}),
								success : function(data) {
									if(data == "update success"){
										toastr.success("修改成功，即将跳往登录界面...");
										setTimeout(function(){
											window.location.href ="/";
										},2000);
										
									}
								},
								error : function() {
									toastr.error("系统出错！");
								}
							});
						}
						
					} else {
						obj.checkOldPassword();
						obj.checkRepet();
						obj.checkPassword();
					}
				});
	},

};

$(function() {
	var per = new Manager.UpdatePassword();
	per.init();
});