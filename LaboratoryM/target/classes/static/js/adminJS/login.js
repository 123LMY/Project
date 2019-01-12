if (!window.LoginCheck)
	LoginCheck = {};

LoginCheck.Main = function() {

};

LoginCheck.Main.prototype = {
	init : function() {
		var flag = false;
		var that = this;
		$(document).keyup(
				function(event) {
					if (event.keyCode == 13) {
						var username = $("#username");
						var password = $("#password");
						var inputcode = $("#verify");
						var code = $("#code");
						var msg1 = null;
						if (username.val() == "") {
							msg1 = "登录名不可为空";
							username.focus();
							$(".codeflxed").css("display", " flex");
						} else if (password.val() == "") {
							msg1 = "密码不可为空";
							password.focus();
							$(".codeflxed").css("display", " flex");
						} else if ($(".codeflxed").css("display") == "flex") {
							if (inputcode.val() == '') {
								msg1 = "验证码不能为空"
								inputcode.focus();
							} else if (inputcode.val().toUpperCase() != code
									.val().toUpperCase()) {
								msg1 = "验证码错误";
								inputcode.focus();
								inputcode.val("");
								that.drawCode();
							}
						}
						if (msg1 != null) {
							Lobibox.notify('error', {
								icon : false,
								title : '提示',
								msg : msg1
							});

						} else {
							that.login();
						}
					}
				});
		$("#loginbtn").click(
				function() {
					var username = $("#username");
					var password = $("#password");
					var inputcode = $("#verify");
					var code = $("#code");
					var msg1 = null;
					if (username.val() == "") {
						msg1 = "登录名不可为空";
						username.focus();
						$(".codeflxed").css("display", " flex");
					} else if (password.val() == "") {
						msg1 = "密码不可为空";
						password.focus();
						$(".codeflxed").css("display", " flex");
					} else if ($(".codeflxed").css("display") == "flex") {
						if (inputcode.val() == '') {
							msg1 = "验证码不能为空"
							inputcode.focus();
						} else if (inputcode.val().toUpperCase() != code.val()
								.toUpperCase()) {
							msg1 = "验证码错误";
							inputcode.focus();
							inputcode.val("");
							that.drawCode();
						}
					}
					if (msg1 != null) {
						Lobibox.notify('error', {
							icon : false,
							title : '提示',
							msg : msg1
						});

					} else {
						that.login();
					}

				});
		that.drawCode();// 画验证码
		$("#code_img").click(
						function() {// 点击刷新验证码
							$('#verifyCanvas').remove();
							$('#verify').after('<canvas width="100%" height="36" id="verifyCanvas"></canvas>');
							that.drawCode();
						});

	},
	login : function() {
		var that = this;
		$.ajax({
			url : "/logincheck?time=new Date().getTime()",
			type : "post",
			data : $("#loginform").serializeArray(),
			contentType : "application/x-www-form-urlencoded",
			success : function(data) {
				if (data == 'fail') {
					Lobibox.notify('error', {
						icon : false,
						title : '提示',
						msg : "密码错误"
					});
					that.drawCode();
					$("#verify").val("");
					$(".codeflxed").css("display", " flex");
				} else if (data == 'success') {
					$(window).attr('location', 'index.html');
				}
			},
			error : function(data) {
				alert("服务器出错了！！");
			}

		});
	},
	check : function(loginname, password) {
		var regName = /^\d{13}$/;
		var regPword = /^[A-Za-z0-9]$/;
		if (regName.test(loginname) || regPword.test(password)) {
			return true;
		} else {
			return false;
		}
	},
	// 绘制验证码
	drawCode : function() {
		var that = this;
		var nums = [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", 'A',
				'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
				'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z' ];
		var canvas = document.getElementById("verifyCanvas"); // 获取HTML端画布
		var context = canvas.getContext("2d"); // 获取画布2D上下文
		context.fillStyle = "cornflowerblue"; // 画布填充色
		context.fillRect(0, 0, canvas.width, canvas.height); // 清空画布
		context.fillStyle = "white"; // 设置字体颜色
		context.font = "1.8rem Arial"; // 设置字体
		var rand = new Array();
		var x = new Array();
		var y = new Array();
		for (var i = 0; i < 5; i++) {
			rand[i] = nums[Math.floor(Math.random() * nums.length)]
			x[i] = i * 16 + 10;
			y[i] = Math.random() * 16 + 20;
			context.fillText(rand[i], x[i], y[i]);
		}
		// 画3条随机线
		for (var i = 0; i < 3; i++) {
			that.drawline(canvas, context);
		}

		// 画30个随机点
		for (var i = 0; i < 30; i++) {
			that.drawDot(canvas, context);
		}
		$("#code").val(rand.join(""));
		that.convertCanvasToImage(canvas);

	},
	// 随机线
	drawline : function(canvas, context) {
		context.moveTo(Math.floor(Math.random() * canvas.width), Math
				.floor(Math.random() * canvas.height)); // 随机线的起点x坐标是画布x坐标0位置，y坐标是画布高度的随机数
		context.lineTo(Math.floor(Math.random() * canvas.width), Math
				.floor(Math.random() * canvas.height)); // 随机线的终点x坐标是画布宽度，y坐标是画布高度的随机数
		context.lineWidth = 0.5; // 随机线宽
		context.strokeStyle = 'rgba(50,50,50,0.3)'; // 随机线描边属性
		context.stroke(); // 描边，即起点描到终点
	},
	// 随机点(所谓画点其实就是画1px像素的线，方法不再赘述)
	drawDot : function(canvas, context) {
		var px = Math.floor(Math.random() * canvas.width);
		var py = Math.floor(Math.random() * canvas.height);
		context.moveTo(px, py);
		context.lineTo(px + 1, py + 1);
		context.lineWidth = 0.2;
		context.stroke();

	},
	// 绘制图片
	convertCanvasToImage : function(canvas) {
		document.getElementById("verifyCanvas").style.display = "none";
		var image = document.getElementById("code_img");
		image.src = canvas.toDataURL("image/png");
		return image;
	}
};

$(function() {

	var lc = new LoginCheck.Main();
	lc.init();
});
