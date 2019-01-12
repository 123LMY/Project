if (!window.Manager)
	Manager = {};
Manager.UpdatePersonalMsg = function() {
	this.emailFlag = true;
	this.phoneFlag = true;
};
Manager.UpdatePersonalMsg.prototype = {

	init : function() {
		var that = this;
			that.loadPersonalPage();
	},
	checkEmail : function() {
		var reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)$/;// 邮箱正则表达式
		var obj = this;
		$("#email").blur(function() {
			var email = $("#email").val();
			var errLabel = $("#email_err_label");
			if (email == "") {
				$("#email").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				$("#email").focus();
				obj.emailFlag = false;
			} else {
				if (!reg.exec(email)) {
					$("#email").css("border-color", "red");
					errLabel.html("");
					errLabel.css("color", "red");
					$("#email").focus();
					obj.emailFlag = false;
				} else {
					$("#email").css("border-color", "");
					errLabel.html("√");
					errLabel.css("color", "green");
					obj.emailFlag = true;
				}
			}

		});

	},

	/*验证手机*/
	checkPhone : function() {
		var re = /^6[0-9]{3,5}$/;// 以6开头的4位或5位或6位的短号
		var reg = /^1[358]\d{9}$/;// 以13、15、18开头的11号码
		var obj = this;
		$("#phone").blur(function() {
			var phone = $("#phone").val();
			var errLabel = $("#phone_err_label");
			if (phone == "") {
				$("#phone").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				$("#phone").focus();
				obj.phoneFlag = false;
			} else {
				if (reg.exec(phone) || re.exec(phone)) {
					$("#phone").css("border-color", "");
					errLabel.html("√");
					errLabel.css("color", "green");
					obj.phoneFlag = true;
				} else {
					$("#phone").css("border-color", "red");
					errLabel.html("×");
					errLabel.css("color", "red");
					$("#phone").focus();
					obj.phoneFlag = false;
				}
			}

		});

	},
	/*加载修改个人信息的界面*/
	loadPersonalPage : function() {	
		var obj = this;
		$(".updatePersonal_navbar").click(function() {
			var ts = new Date().getTime();
	 
			$.ajax({
				url : "/User/loadPersonalPage?time="+ts,
				type : "POST",
				async : false,
				contentType : "application/json",
				success : function(data) {
					$("#right_content").html(data);
					obj.loadPersonalData();
					obj.checkEmail();
					obj.checkPhone();
					obj.updatePersonal();
					 $("#left_content").css("display","none");
				},
				error : function() {
					alert("加载失败，请刷新页面重试");
				}
			});
		});
	},

	/*获取修改个人信息页面的数据*/
	loadPersonalData : function() {
		var obj = this;
		var ts = new Date().getTime();
		$.ajax({
			url : "/User/loadPersonalData?time="+ts,
			type : "post",
			async : false,
			contentType : "application/json",
			success : function(data) {
				$("input[name='userName']").val(data.user.userName);
				$("input[name='realName']").val(data.user.realName);		
				$("input[name='email']").val(data.user.email);
				$("input[name='phone']").val(data.user.phone);
				$("[name='remark']").val(data.user.remark);				 
				$("[name='major']").val(data.major);
				$("[name='class']").val(data.grades);
				$("[name='institute']").val(data.institute);
			},
			error : function() {
				alert("加载失败，请刷新页面重试");
			}
		});
	},

	/*提交修改后的个人信息*/
	updatePersonal : function() {
		var obj = this;
		$("#updateBtn").click(function() {
							if (obj.emailFlag == true && obj.phoneFlag == true) {								
								var str = $("#updateForm").serializeArray();
								var ts = new Date().getTime();
								$.ajax({
											url : "/User/updatePersonalMsg?time="+ts,
											type : "post",
											contentType : "application/x-www-form-urlencoded;charset=UTF-8",
											data : str,
											success : function(data) {
												if(data == "update success"){
													toastr.success("修改成功!");												
												}else{
													toastr.error("修改失败");
												}									
											},
											error : function() {
												toastr.error("系统出错");
											}
										});
							} else {
								obj.checkEmail();
								obj.checkPhone();
							}

						});
	},
};
$(function() {
	var upm = new Manager.UpdatePersonalMsg();
	upm.init();
});