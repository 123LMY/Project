//登录验证	
if(!window.Manager) Manager = {};

Manager.User = function(){
	this.nums = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
	            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
	            'y', 'z'];
	
	this.rand = new Array();

	this.userNameFlag=true;
	this.userPwdFlag=true;
	this.codeFlag = true;
	this.countDown = 60;
};

Manager.User.prototype = {
	
	init : function(){
		var that = this;
		/**
		 * 限制用户名输入框字符
		 */
		$("#userName").keyup(function(){
			var str = $(this);
			that.placeUserName(str);
		});
		/**
		 * 用户名输入框失去焦点判断
		 */
		$("#userName").blur(function(){
			var str = $(this);
			that.checkUserName(str);
  		});
		/**
		 * 密码输入框失去焦点判断
		 */
		$("#userPwd").blur(function(){
  			that.checkPassword();
  		});
  		
  		/**
  		 * 变换验证码
  		 */
  		$("#code_img").click(function(){
  			that.updateCode();
  			that.drawCode();
  		});
  		
  		/**
  		 * 验证验证码是否正确
  		 */
  		$("#verify").blur(function(){
  			that.checkVerify();
  		});
  		
  		/**
  		 * 点击按钮登录
  		 */
  		$("#loginBtn").click(function(){
  			if(that.codeFlag == true  && that.userNameFlag == true && that.userPwdFlag == true){
  				that.loginCheck();
  			}else{
  				that.checkUserName();
  				that.checkPassword();
  				that.checkVerify();
  				
  			}			
  		});	
  		 that.findPassword();
  		 that.checkEmailCode();
  		 that.initToastr();
  		 that.enterFunction();
	},
	
	/**
	 * 密码验证
	 */
	checkPassword : function(){
		var that = this;
		var reg = /^[a-zA-Z0-9]{6,20}$/;
		var pass = $("#userPwd").val();
		if(pass == ""){
			$("#userPwd").attr("title","密码不能为空！");
			$("#userPwd").tooltip("show");
			$("#userPwd").css("border-color","#C12E2A");
			that.userPwdFlag = false;
		}else if(pass != ""){
			if(!reg.exec(pass)){
				$("#userPwd").attr("title","密码必须由6-20字母或数字组成！");
				$("#userPwd").tooltip("show");
				$("#userPwd").css("border-color","#C12E2A");	
				that.userPwdFlag = false;
			}else{	
				$("#userPwd").tooltip("hide");
				$("#userPwd").tooltip("destroy");
				$("#userPwd").css("border-color","");
				that.userPwdFlag = true;
			}
			
		}
	},	
	
	/**
	 * 用户名输入框替换字符
	 * @param str
	 */
	placeUserName: function(str){
		if(/[^\d]/.test(str.val())){//替换非数字字符  
            var temp_amount=str.val().replace(/[^\d]/g,'');  
            str.val(temp_amount);
		}
	},
	/**
	 * 验证用户名
	 */
	checkUserName:function(){
		var str = $("#userName");
		var that = this;
		 if(str.val()==""){
			 str.attr("title","用户名不能为空！");
			 str.tooltip("show");
			 str.css("border-color","#C12E2A");
			 str.focus();
			 that.userNameFlag = false;
		 }else if(str.val() !=""){
			 	str.tooltip("hide");
				str.tooltip("destroy");
				str.css("border-color","");
				that.userNameFlag =  true;
		 }
	},
	
	/**
	 * 登陆超时验证
	 */
	checkTime :function(){
		var lastTime = new Date().getTime();
        var currentTime = new Date().getTime();
        var timeOut = 60 * 1000; //设置超时时间： 10分
        $(function(){
            /* 鼠标移动事件 */
            $(document).mouseover(function(){
                lastTime = new Date().getTime(); //更新操作时间

            });
        });
        
        /* 定时器  间隔1秒检测是否长时间未操作页面  */
        var quitTime = window.setInterval(testTime, 1000);

        function testTime(){
            currentTime = new Date().getTime(); //更新当前时间
            if(currentTime - lastTime > timeOut){ //判断是否超时
                alert("登陆超时");
                window.clearInterval(quitTime);
            }
        }

        
	},

	/**
	 *登陆验证
	 */
	loginCheck :function(){
		var that = this;	 
		var loginName = $("#userName").val();
		var password = $("#userPwd").val();
		var ts = new Date().getTime();
		var type = new String() ;
		var remFlag = $("input[type='checkbox']").is(':checked');
		if(remFlag == true){
			type="yes";
		}else{
			type="no";
		}
		$.ajax({
			url:"userLogin?time="+ts,
			type:"POST",
			contentType:"application/json",
			data:JSON.stringify({"userName":loginName,"userPwd":password,"remFlag":type}),
			success:function(resultData){
				 if(resultData.status == "success"){ 	 
					that.checkTime();
					swal({
						title:"登录成功",
						type:"success",
						timer:2000,
						showConfirmButton:false
					});
					setTimeout(function(){
						window.location.href=resultData.src; 
					},2000);
				
				   } 
				 if(resultData.status == "fail"){
					 swal({
							title:"用户名或密码错误",
							type:"error",
							timer:1000,
							showConfirmButton:false
						});
					$("#verDiv").css("display","block");
					$("#userName").val("");
					$("#userPwd").val("");
					$("#verify").val("");
					that.updateCode();
					that.drawCode();
					that.userNameFlag = false;
					that.userPwdFlag = false;
					that.codeFlag = false;
					return ;
				} 
			},
			error:function(){
				toastr.error("系统出错！");
				$("#verDiv").css("display","block");
				$("#userName").val("");
				$("#userPwd").val("");
				$("#verify").val("");
				that.updateCode();
				that.drawCode();
				that.userNameFlag = false;
				that.userPwdFlag = false;
				that.codeFlag = false;
			}
		});
	},
	enterFunction : function(){
		var that = this;
		$(document).keyup(function(){
			if(window.event.keyCode ==13){
				if(that.codeFlag == true  && that.userNameFlag == true && that.userPwdFlag == true){
	  				that.loginCheck();
	  			}else{
	  				that.checkUserName();
	  				that.checkPassword();
	  				that.checkVerify();
	  				
	  			}	
			}
		})
		
	},
	//忘记密码
	findPassword : function(){
		var obj = this;
		$("#emailBtn").click(function(){
			var userName = $("#findName").val();
			var findEmail = $("#findEmail").val();
			
			if(userName != "" && findEmail !=""){
				  obj.settime($(this));
				  var ts = new Date().getTime();
				  $.ajax({
					 url:"/User/findPassword?time="+ts ,
					 type:"post",
					 contentType:"application/json",
					 data:JSON.stringify({"findName":userName,"findEmail":findEmail}),
					 success : function(data){
						 if(data.msg == "different"){
							 swal({
								 title:"邮箱与账号所绑定邮箱不一致！",
								 type:"error",
								 timer:2000,
								 showConfirmButton:false
							 });					 
							  return ;
						 }else{
							 $("#emailCode").val(data.code);							 
							 return ;
						 }
					 }
				  });
			}else{
				toastr.error("用户名与邮箱不能为空!");
				return ;
			}
		
		});
	},
	//验证邮箱所收到的验证码
	checkEmailCode : function(){
		var obj = this;
		$("#forBtn").click(function(){
			
			var checkCode = $("#checkCode").val();
			var emailCode = $("#emailCode").val();
			var userName = $("#findName").val();
			if(checkCode != ""){ 
				if(checkCode == emailCode ){
					toastr.success("验证成功");	
					var str  = ' <div class="form-group"><label class="control-label col-xs-2">新密码</label>'
						+'<div class="col-xs-9"><input type="password" id="password" class="form-control"'
						+' placeholder="请输入新密码"/></div></div>'
						+' <div class="form-group"><label class="control-label col-xs-2">确认新密码</label>'
						+'<div class="col-xs-9"><input type="password" id="repassword" class="form-control"'
						+' placeholder="确认密码"/></div></div>'
						+'<input type="hidden" id="name" value="'+userName+'"/>';
						$(".form-horizontal").html(str);
						$("#myModalLabel").text("修改密码");						 
					var mf = '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>'
							+'<button type="button" class="btn btn-primary" data-toggle="modal" id="updateBtn">修改</button>';
						$(".modal-footer").html(mf);
						obj.updatePassword();
				}else{
					toastr.error("验证失败");	
				}
			}
		});
	},
	updatePassword : function(){
		$("#updateBtn").click(function(){
			var pas = $("#password").val();
			var repas = $("#repassword").val();
			var userName = $("#name").val();
			if(pas != repas){
				toastr.error("两个密码不一致");
				return ;
			}else{
				$.ajax({
					url:"/User/midifiedPassword",
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"newPassword":pas,"userName":userName}),
					success : function(data){
						if(data == "the same"){
							toastr.error("新密码不能与旧密码一致!");
							return ;
						}else if(data == "update success"){
							toastr.success("修改成功!");
							$("#forgetPasswordModal").modal("hide");
							return ;
						}else{
							toastr.error("修改失败!");
							return ;
						}
					}
				});
			}
			
		});
	},
	//按钮定时
	settime : function(val){
		var obj = this;
		if (obj.countDown == 0) { 
		    val.attr("disabled",false);    
		    val.val("获取验证码"); 
		    obj.countDown = 10;
		    return;
		    } else { 
		    val.attr("disabled", true); 
		    val.val("重新发送(" + obj.countDown + ")"); 
		    obj.countDown--; 
		    } 
		    setTimeout(function() { 
		    	obj.settime(val); 
		    },1000);
	},
	
	
	//绘制验证码
	drawCode : function(){
		var that = this;
		var canvas = document.getElementById("verifyCanvas"); //获取HTML端画布
        var context = canvas.getContext("2d");                 //获取画布2D上下文
        context.fillStyle = "cornflowerblue";                  //画布填充色
        context.fillRect(0, 0, canvas.width, canvas.height);   //清空画布
        context.fillStyle = "white";                           //设置字体颜色
        context.font = "20px Arial";                           //设置字体
        //var rand = new Array();
        var x = new Array();
        var y = new Array();
        for (var i = 0; i < 5; i++) {
            that.rand[i] = that.nums[Math.floor(Math.random() * that.nums.length)];
            x[i] = i * 16 + 10;
            y[i] = Math.random() * 10 + 20;
            context.fillText(that.rand[i], x[i], y[i]);
        }
        
        //alert(codeValue);
        
        //画3条随机线
        for (var i = 0; i < 3; i++) {
            that.drawline(canvas, context);
        }

        // 画30个随机点
        for (var i = 0; i < 30; i++) {
            that.drawDot(canvas, context);
        }
        that.convertCanvasToImage(canvas);
	},
	
	//画随机线
	drawline : function(canvas,context){
		context.moveTo(Math.floor(Math.random() * canvas.width), Math.floor(Math.random() * canvas.height));             //随机线的起点x坐标是画布x坐标0位置，y坐标是画布高度的随机数
        context.lineTo(Math.floor(Math.random() * canvas.width), Math.floor(Math.random() * canvas.height));  //随机线的终点x坐标是画布宽度，y坐标是画布高度的随机数
        context.lineWidth = 0.5;                                                  //随机线宽
        context.strokeStyle = 'rgba(50,50,50,0.3)';                               //随机线描边属性
        context.stroke();                                                         //描边，即起点描到终点
	},
	
	// 随机点(所谓画点其实就是画1px像素的线，方法不再赘述)
	drawDot : function(canvas,context){
		var px = Math.floor(Math.random() * canvas.width);
        var py = Math.floor(Math.random() * canvas.height);
        context.moveTo(px, py);
        context.lineTo(px + 1, py + 1);
        context.lineWidth = 0.2;
        context.stroke();
	},
	
	//绘制图片
	convertCanvasToImage : function(canvas) {
		$("#verifyCanvas").css("display","none");
		var image = document.getElementById("code_img");
        image.src = canvas.toDataURL("image/png");
        return image;
	},
	
	updateCode :function(){
			$("#verifyCanvas").remove();
			$("#verify").after('<canvas width="100%" height="33%" id="verifyCanvas"></canvas>');
	},
	
	//验证验证码是否正确
	checkVerify : function(){
		var that = this;
		var verify = document.getElementById("verify").value.toLowerCase();
		var val = that.rand.join("");
        var codeValue = document.getElementById("code_value").value;
        codeValue = val.toLowerCase();
		if(codeValue == verify){
			$("#verify").tooltip("hide");
			$("#verify").tooltip("destroy");
			$("#verify").css("border-color","");
			that.codeFlag =  true;
		}else{
			$("#verify").attr("title","验证码错误");
			$("#verify").tooltip("show");
			$("#verify").css("border-color","#C12E2A");
			that.updateCode();
			that.drawCode();
			that.codeFlag =  false;
		}

	},
	getCookie : function(cname){
		var name = cname + "=";
		var ca = document.cookie.split(';');
		for(var i = 0;i<ca.length; i++){
			var c = ca[i];
			while(c.charAt(0) == ' ') c = c.substring(1);
			if(c.indexOf(name) != -1) return c.substring(name.length,c.length);
		}
		return "";
	},
	initToastr:function(){
		toastr.options = {  
		        closeButton: false,  
		        debug: false,  
		        progressBar: true,  			      
		        onclick: null,  
		        showDuration: "300",  
		        hideDuration: "1000",  
		        timeOut: "1000",  
		        extendedTimeOut: "1000",  
		        showEasing: "swing",  
		        hideEasing: "linear",  
		        showMethod: "fadeIn",  
		        hideMethod: "fadeOut"  
		    };
	}
	
};

$(document).ready(function(){
	var mu = new Manager.User();
		mu.init();
	var str = mu.getCookie("loginInfo");
	if(str != null){
		var userName = str.split("T")[0];
		var password = str.split("T")[1];
		$("#userName").val(userName);
		$("#userPwd").val(password);
		$("input[type='checkbox']").attr("checked","checked");
	}
	$('body').on('hidden.bs.modal','.modal', function () {
		 $(this).removeData('bs.modal');
		 mu.countDown = 0;
		$(':input').not(':button,:submit,:reset').val('').removeAttr('checked').removeAttr('checked');
		});
});




