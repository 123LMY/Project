//登录验证	
if(!window.Manager) Manager = {};

Manager.User = function(){
	
};

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
  };

var nums = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "0", 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z'];
var rand = new Array();

var userNameFlag=false;
var userPwdFlag=false;
var codeFlag = true;

Manager.User.prototype = {
	
	init : function(){
		var that = this;
		$("#userName").keyup(function(){
			var str = $(this);
			that.placeUserName(str);
		});
		$("#userName").blur(function(){
			var str = $(this);
			that.checkUserName(str);
  		});
		$("#userPwd").blur(function(){
  			that.checkPassword();
  		});
  		
  		
  		$("#code_img").click(function(){
  			that.updateCode();
  			that.drawCode();
  		});

  		$("#verify").blur(function(){
  			that.checkVerify();
  		});
  		
  		$("#loginBtn").click(function(){
  			if(codeFlag == true  && userNameFlag == true && userPwdFlag == true){
  				that.loginCheck();
  			}else{
  				that.checkUserName();
  				that.checkPassword();
  				that.checkVerify();
  				
  			}
  			
  		});
  		
  		$("#registerBtn").click(function(){
  			var val = $("#login_form").serializeObject();	
			var jsonStr = JSON.stringify(val);
			$.ajax({
				url:"insertUser",
				type:"POST",
				contentType:"application/json",
				data:jsonStr,
				dataType:"text",
				success:function(resultData){
					alert(resultData);
				},
				error:function(){
					alert("insert fail");
				}
			});
  		});
  		$("#allBtn").click(function(){
  			$.ajax({
				url:"getAllUser",
				type:"POST",
				contentType: "application/json",
				data:JSON.stringify({pageNum:2, pageSize:2}),
				dataType:"text",
				success:function(resultData){
					alert(resultData);
				},
				error:function(){
					alert("fail");
				}
			});
  		});
		
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
			 userPwdFlag = false;
		}else if(pass != ""){
			if(!reg.exec(pass)){
				$("#userPwd").attr("title","密码必须由6-20字母或数字组成！");
				$("#userPwd").tooltip("show");
				$("#userPwd").css("border-color","#C12E2A");	
				userPwdFlag = false;
			}else{	
				$("#userPwd").tooltip("hide");
				$("#userPwd").tooltip("destroy");
				$("#userPwd").css("border-color","");
				userPwdFlag = true;
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
		 if(str.val()==""){
			 str.attr("title","用户名不能为空！");
			 str.tooltip("show");
			 str.css("border-color","#C12E2A");
			 str.focus();
			 userNameFlag = false;
		 }else if(str.val() !=""){
			 	str.tooltip("hide");
				str.tooltip("destroy");
				str.css("border-color","");
				userNameFlag =  true;
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
		var val = $("#login_form").serializeObject();	
		var jsonStr = JSON.stringify(val);
		$.ajax({
			url:"userLogin?time=new Date()",
			type:"POST",
			contentType:"application/json",
			data:jsonStr,
			success:function(resultData){
				if(resultData == "success"){
					that.checkTime();
					alert("login success");
				}
				if(resultData == "login  fail"){
					alert("用户名或密码错误");
					$("#verDiv").css("display","block");
					$("#userName").val("");
					$("#userPwd").val("");
					$("#verify").val("");
					that.updateCode();
					that.drawCode();
					userNameFlag = false;
					userPwdFlag = false;
					codeFlag = false;
				}
			},
			error:function(){
				alert("login error");
				$("#verDiv").css("display","block");
				$("#userName").val("");
				$("#userPwd").val("");
				$("#verify").val("");
				that.updateCode();
				that.drawCode();
				userNameFlag = false;
				userPwdFlag = false;
				codeFlag = false;
			}
		});
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
            rand[i] = nums[Math.floor(Math.random() * nums.length)]
            x[i] = i * 16 + 10;
            y[i] = Math.random() * 10 + 20;
            context.fillText(rand[i], x[i], y[i]);
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
        that.convertCanvasToImage(canvas)
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
		var val = rand.join("");
        var codeValue = document.getElementById("code_value").value;
        codeValue = val.toLowerCase();
		if(codeValue == verify){
			$("#verify").tooltip("hide");
			$("#verify").tooltip("destroy");
			$("#verify").css("border-color","");
			codeFlag =  true;
		}else{
			$("#verify").attr("title","验证码错误");
			$("#verify").tooltip("show");
			$("#verify").css("border-color","#C12E2A");
			that.updateCode();
			that.drawCode();
			codeFlag =  false;
		}

	}
	
};

$(function(){
	
	var mu = new Manager.User();
  			mu.init();
});



