if(!window.Manager) Manager = {};
Manager.ProjectRegistration = function(){
	this.type = false;
	this.projectType = new Map();
	this.projectType.set("0","创新项目");
	this.projectType.set("1","竞赛项目");
	this.projectType.set("2","企业项目");
	this.projectType.set("3","其他");
	
	
};
Manager.ProjectRegistration.prototype = {
	init : function(){
		var that = this;
		$(".project_registration_navbar").click(function(){
			that.loadProjectRegistrationPage();
			that.checkMember();
			that.submitProjectRegustrationPage();
		});
	}, 
	/**
	 * 加载项目注册的页面
	 */
	loadProjectRegistrationPage: function(){
		var obj = this;
		var timestamp = new Date().getTime();
		$.ajax({
			url:"/Project/User/loadProjectRegistrationPage?time="+timestamp,
			type : "POST",
			async : false,
			contentType : "application/json",
			success : function(data) {
				$("#right_content").html(data);
				obj.loadGlyphicon();			
				obj.projectType.forEach(function (value, key, map) {
					$("#pro_sort").append("<option  value=" +key + ">" +value+ "</option>");
				 });
				obj.loadProjectRegistrationPageData();
			},
			error : function() {
				alert("加载失败，请刷新页面重试");
			}
		});
	},
	/**
	 * 加载项目注册页面的数据
	 */
	loadProjectRegistrationPageData : function(){
		var timestamp = new Date().getTime();
		$.ajax({
			url:"/Project/User/loadProjectRegistrationPageData?time="+timestamp,
			contentType:"application/json",
			type:"post",
			success : function(data){
			  data.lab.forEach(function(value,key){
				  $("#lab").append("<option  value=" +value.id + ">" +value.labName+ "</option>");
			  });
			}
		});
	},
	/**
	 * 添加成员输入框
	 */
	loadGlyphicon : function(){
		var inputdiv = $("#inputProjectMember");//输入框包装ID
		var addbtn = $("#addinputBtn");
		var x = inputdiv.length;
		$(addbtn).click(function (e) //on add input button click
		{
		    //添加输入框
			if(x>3){
				swal({
					title:"已经加到底啦！",
					type:"info",
					timer:3000,
					showConfirmButton: false
				 });
				return false;
			}else{
				inputdiv.append('<div> <input type =“text” class="form-control" name ="proMember" />'
					    +'<input type =“text” class="form-control" name ="proMember" />' 
					    +'<input type =“text” class="form-control" name ="proMember" />' 
					    +'<button class="removeclass btn btn-link btn-simple-primary" ><i class="glyphicon glyphicon-minus"></i></button> </ div>');
						x++;
			}
		    
			return false;
		});
		$("body").on("click",".removeclass", function(e){ 
		    if( x > 1 ) {
		        $(this).parent('div').remove();
		        x--; 
		    }
			return false;
		});
	},
	/**
	 * 项目成员输入框失去焦点后检测成员是否注册
	 */
	checkMember: function(){
		var that = this;
		$("[name='projectMembers']").blur(function(){
			var str = $(this).val();
			var obj = $(this);
			
			if(str == ""){
				return ;
			}else{
				$.ajax({
					url:"/ProjectMember/User/checkMembers",
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"members":str}),
					success: function(data){
						if(data.status == "error"){
							toastr.error("该用户未注册!");
							obj.css("border-color", "red");
							that.type=false;			
						}else{
							if(data.msg.userType= '0'){
								obj.css("border-color", "");
								that.type = true;
							}else{
								toastr.error("该用户不是学生!");
								obj.css("border-color", "red");
								that.type=false;
							}
							
						}
							
							 
					}
				});
			}
			
		});	
		$(".check").blur(function(){
			var str = $(this).val();
			var obj = $(this);
		 
			if(str == ""){
				return ;
			}else{
				$.ajax({
					url:"/ProjectMember/User/checkMembers",
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"members":str}),
					success: function(data){
						if(data.status == "error"){
							toastr.error("该用户未注册!");
							obj.css("border-color", "red");
							that.type=false;			
						}else{
							if(data.msg.userType == '1'){
								obj.css("border-color", "");
								that.type = true;
							}else{
								toastr.error("该用户不是老师!");
								obj.css("border-color", "red");
								that.type=false;
							}
							
						}
							
							 
					}
				});
			}
			
		});	
	 
		
	},
	/**
	 * 提交项目登记页面
	 */
	submitProjectRegustrationPage : function(){
		var obj = this;
			$("#subBtn").click(function(){			 
				var str = $("#PRForm").serializeObject();			 
				if(str.proName == ""){		
					 swal({
						title:"项目名称不能为空",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					 });
					return;
				}else{
					var ref = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
					if(!ref.exec(str.proName)){
						 swal({
								title:"项目名称包含非法字符串",
								type:"warning",
								timer:1000,
								showConfirmButton: false
							 });
						 return ;
					}
				}
				if(str.labId == ""){
					swal({
						title:"请选择一个实验室！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					 });
					return;
				}
				if(str.proType == "" ){				 
					swal({
						title:"请选择项目类型！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					 });
					return;
				} 
				for(var i in str.projectMembers){
					if(str.projectMembers[i] != ""){	
						break ;
					}else{
						swal({
							title:"至少输入一个成员！",
							type:"warning",
							timer:1000,
							showConfirmButton: false
						 });
						return ;
					} 
				}
				if(str.projectTutor == ""){
					swal({
						title:"指导老师不能为空！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					 });
					return ;
				}
				if(str.projectLeader == ""){
					swal({
						title:"负责人不能为空！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					 });
					return ;
				}
				var timestamp = Date.parse(new Date());
				if(obj.type == true){
					$.ajax({
						url:"/Project/User/insertProject?time="+timestamp,
						type:"POST",
						contentType:"application/json",
						data:JSON.stringify(str),
						success: function(data){
							if(data.status == "the same pj"){
								swal({
									title:"项目已存在！",
									type:"warning",
									timer:2000,
									showConfirmButton: false
								 });
								return ;
							}else{
								swal({
									title:"警告！",
									text:data.fingerMark+"未录入掌脉信息！"+"请尽快登录录入掌脉，否则无法获得门禁权限",
									type:"info",
									timer:2000,
									showConfirmButton: false
								 });
								
							}
							
						},
						error: function(){
							swal({
								title:"插入失败！",
								type:"error",
								timer:1000,
								showConfirmButton: false
							 });
						}
					});
				}
		
			});
		
		
	},
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
$(function(){
	var pr = new Manager.ProjectRegistration();
	pr.init();
});