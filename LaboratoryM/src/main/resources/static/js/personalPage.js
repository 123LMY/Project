if(!window.Manager) Manager = {};

Manager.Personal = function(){

};

  var oldPasswordFlag = false;
  var newPasswordFlag = false;
  var resPasswordFlag = false;
  var emailFlag = true;
  var phoneFlag = true;
Manager.Personal.prototype = {
	init : function(){
		var that = this;
		//点击修改个人信息导航栏
		$("#updatePersonal_navbar").click(function(){
			that.loadPersonalPage();
			that.loadPersonalData();
			that.checkEmail();
			that.checkPhone();
			that.updatePersonal();
		});

		//点击项目管理导航栏
		$("#project_management_navbar").click(function(){
			that.loadProjectManagementPage();
			that.loadProjectManagementPageData();
			that.search();
			that.updateProjectManagement();
		});
		
		//点击项目登记导航栏
		$("#project_registration_navbar").click(function(){
			that.loadProjectRegistrationPage();
			that.checkMember();
			that.submitProjectRegustrationPage();
		});
		
		//点击项目结题导航栏
		$("#project_junctions_navbar").click(function(){
			$("#update_personal_msg").css("display","none");
			$("#update_password").css("display","none");
			$("#project_management").css("display","none");
			$("#project_registration").css("display","none");
			$("#project_junctions").css("display","block");
		});
			
		/*点击修改密码导航栏*/
		$("#updatePassword_navbar").click(function(){
			that.loadUpdatePwdPage();
			that.checkOldPassword();
			that.checkPassword();
			that.checkRepet();
			that.updatePassword();
		});
		
		
	},

	
	/*--------------------修改个人信息页面-------------------------*/
	
	/*验证邮箱*/
	checkEmail : function() {
		var reg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)$/;// 邮箱正则表达式
		$("#email").blur(function() {
			var email = $("#email").val();
			var errLabel = $("#email_err_label");
			if (email == "") {
				$("#email").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				$("#email").focus();
				emailFlag = false;
			} else {
				if (!reg.exec(email)) {
					$("#email").css("border-color", "red");
					errLabel.html("");
					errLabel.css("color", "red");
					$("#email").focus();
					emailFlag = false;
				} else {
					$("#email").css("border-color", "");
					errLabel.html("√");
					errLabel.css("color", "green");
					emailFlag = true;
				}
			}

		});

	},
	
	/*验证手机*/
	checkPhone : function() {
		var re = /^6[0-9]{3,5}$/;// 以6开头的4位或5位或6位的短号
		var reg = /^1[358]\d{9}$/;// 以13、15、18开头的11号码
		$("#phone").blur(function() {
			var phone = $("#phone").val();
			var errLabel = $("#phone_err_label");
			if (phone == "") {
				$("#phone").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				$("#phone").focus();
				phoneFlag = false;
			} else {
				if (reg.exec(phone) || re.exec(phone)) {
					$("#phone").css("border-color", "");
					errLabel.html("√");
					errLabel.css("color", "green");
					phoneFlag = true;
				} else {
					$("#phone").css("border-color", "red");
					errLabel.html("×");
					errLabel.css("color", "red");
					$("#phone").focus();
					phoneFlag = false;
				}
			}

		});

	},
	/*加载修改个人信息的界面*/
	loadPersonalPage : function() {
		$.ajax({
			url : "loadPersonalPage",
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
	},
	
	/*获取修改个人信息页面的数据*/
	loadPersonalData : function() {
		$.ajax({
			url : "loadPersonalData",
			type : "post",
			async : false,
			contentType : "application/json",
			success : function(data) {
				$("input[name='userName']").val(data.userName);
				$("input[name='realName']").val(data.realName);
				$("input[name='email']").val(data.email);
				$("input[name='phone']").val(data.phone);
				$("[name='remark']").val(data.remark);
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
			if (emailFlag == true && phoneFlag == true) {
				var str = $("#updateForm").serializeArray();
				$.ajax({
					url : "updatePersonalMsg",
					type : "post",
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					data : str,
					success : function(data) {
						alert(data);
					},
					error : function() {
						alert("修改失败");
					}
				});
			} else {
				obj.checkEmail();
				obj.checkPhone();
			}

		});
	},
	
	/*------------------修改密码页面-----------------------*/
	
	/*加载修改密码页面*/
	loadUpdatePwdPage : function() {
		$.ajax({
			url : "loadPasswordPage",
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
		$("#newPassword").blur(function() {
			var reg = /^[a-zA-Z0-9]{5,19}$/;
			var newPass = $("#newPassword").val();
			var errLabel = $("#new_err_label");
			if (newPass == "") {
				$("#newPassword").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				newPasswordFlag = false;
			} else {
				if (!reg.exec(newPass)) {
					$("#newPassword").css("border-color", "red");
					errLabel.html("×！");
					errLabel.css("color", "red");
					newPasswordFlag = false;
				} else {
					$("#newPassword").css("border-color", "");
					errLabel.html("√");
					errLabel.css("color", "green");
					newPasswordFlag = true;
				}
			}
		});

	},
	
	/*检验输入的两个密码是否一致*/
	checkRepet : function() {
		$("#repassword").blur(function() {
			var newPass = $("#newPassword").val();
			var rePass = $("#repassword").val();
			var errLabel = $("#rep_err_label");
			if (newPass == rePass && rePass != "") {
				$("#repassword").css("border-color", "");
				errLabel.html("√");
				errLabel.css("color", "green");
				resPasswordFlag = true;
			} else {
				$("#repassword").css("border-color", "red");
				errLabel.html("×");
				errLabel.css("color", "red");
				resPasswordFlag = false;
			}
		});
	},
	

		/* 检验输入原密码是否正确 */
	checkOldPassword : function() {
		$("#oldUserPwd").blur(function() {
			var oldPassword = $("#oldUserPwd").val();
			if (oldPassword == "") {
				$("#old_err_label").html("×");
				$("#old_err_label").css("color", "red");
				oldPasswordFlag = false;
			} else {
				$.ajax({
					url : "checkPassword",
					type : "post",
					contentType : "application/json",
					data : JSON.stringify({
						"oldPassword" : oldPassword
					}),
					success : function(data) {
						if (data == "the same") {
							$("#old_err_label").html("√");
							$("#old_err_label").css("color", "green");
							oldPasswordFlag = true;
						}
						if (data == "different") {
							$("#old_err_label").html("×");
							$("#old_err_label").css("color", "red");
							oldPasswordFlag = false;
						}
					}
				});
			}
		});

	},

	/*提交修改后的密码*/
	updatePassword : function() {
		var obj = this;
		$("#updataPasBtn").click(
				function() {
					if (newPasswordFlag == true && oldPasswordFlag == true
							&& resPasswordFlag == true) {
						var newPas = $("#newPassword").val();
						$.ajax({
							url : "updatePassword",
							type : "post",
							contentType : "application/json",
							data : JSON.stringify({
								"newPassword" : newPas
							}),
							success : function(data) {
								alert(data);
							},
							error : function() {
								alert("update error");
							}
						});
					} else {
						obj.checkOldPassword();
						obj.checkRepet();
						obj.checkPassword();
					}
				});
	},
	
	/*------------------项目登记表页面-----------------------*/
	loadProjectRegistrationPage: function(){
		$.ajax({
			url:"loadProjectRegistrationPage",
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
	},
	checkMember: function(){
		$("#projectMembers").blur(function(){
			var str = $(this).val();
			str=str.replace(/，/ig,',');
			$.ajax({
				url:"checkMembers",
				type:"post",
				contentType:"application/json",
				data:JSON.stringify({"members":str}),
				success: function(data){
					if(data.status == "error")
						alert(data.noneUser+"用户未登记！")
				},
				error: function(){
					alert("error");
				}
			});
		});
	},
	submitProjectRegustrationPage : function(){
		$("#subBtn").click(function(){
			var str = $("#PRForm").serializeObject();
			$.ajax({
				url:"insertProject",
				type:"POST",
				contentType:"application/json",
				data:JSON.stringify(str),
				success: function(data){
					alert(data.fingerMark+"未录入掌脉信息！"+"请尽快登录录入掌脉，否则无法获得门禁权限");
				},
				error: function(){
					alert("插入失败!");
				}
			});
		});
	},
	
	/*------------------项目管理界面-----------------------*/
	loadProjectManagementPage: function(){
		var obj = this;
		$.ajax({
			url:"loadProjectManagementPage",
			type : "POST",
			async : false,
			contentType : "application/json",
			success : function(data) {
				$("#right_content").html(data);
				obj.loadProjectManagementTable();
			},
			error : function() {
				alert("加载失败，请刷新页面重试");
			}
		});
	},
	loadProjectManagementPageData: function(){
		$.ajax({
			url:"loadProjectManagementPageData",
			type:"post",
			contentType:"application/json",
			dataType:"json",
			success: function(data){
				$('#tb_project').bootstrapTable("load",data);
				/*	var str = new String() ;
				$.each(data.resource,function(index,item){
					str += "<input type='hidden' name='projectId' value='"+item.id+"'/>";
					str += "<tr> <td><input type='checkbox' name='select'> </td>";
					str += "<td> "+item.proName+"</td>";
					str += "<td> "+item.projectMembers+"</td>";
					str += "<td> "+item.projectTutor+"</td>";
					str += "<td> "+item.proType+"</td>";
					str += "<td><input type='button' class='btn btn-link btn-simple-primary' data-toggle='modal' data-target='#myModal'>"
							+ "<i class='la la-edit'></i>"
							+ "<input type='button' class='btn btn-link btn-simple-primary'>"
							+ "	<i class=]la la-times]·></i>	</td></tr>";			
				});
				$("#proTable").html(str);*/
			}
		});			     
	},
	loadProjectManagementTable: function(){
		$('#tb_project').bootstrapTable({
            //url: 'loadProjectManagementPageData',         //请求后台的URL（*）
            method: 'post',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            //sortable: false,                     //是否启用排序
            //sortOrder: "asc",                   //排序方式
            //queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            //search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            //strictSearch: true,
            //showColumns: true,                  //是否显示所有的列
            //showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            //height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            //showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            //detailView: false,                   //是否显示父子表
            columns: [{
                checkbox: true
            }, {
                field: 'proName',
                title: '项目名称',
                align: 'center',
            }, {
                field: 'projectMembers',
                title: '项目成员',
                align: 'center',
            }, {
                field: 'projectTutor',
                title: '指导老师',
                align: 'center',
            }, {
                field: 'proType',
                title: '项目状态',
                align: 'center',
            },{
                field: 'Operation',
                title: '操作',
                align: 'center',
                formatter : function(value,
						row, index) {
					var c = '<input type="button" class="green-color" value="查看"  '
						+ row.id
						+ ' "/> ';
					return c ;
				}
                
            }, ]
        });	
	},
	search : function(){
		$("#searchBtn").click(function(){
			var searchName = $("#searchName").val();
			$.ajax({
				url:"selectProjectBySerachName",
				type:"post",
				contentType:"application/json",
				data:JSON.stringify({"searchName":searchName}),
				success : function(data){
					var str = new String() ;
					$.each(data.resource,function(index,item){
						str += "<input type='hidden' name='projectId' value='"+item.id+"'/>";
						str += "<tr> <td><input type='checkbox' name='select'> </td>";
						str += "<td> "+item.proName+"</td>";
						str += "<td> "+item.projectMembers+"</td>";
						str += "<td> "+item.projectTutor+"</td>";
						str += "<td> "+item.proType+"</td>";
						str += "<td><button type='button' class='btn btn-link btn-simple-primary' data-toggle='modal' data-target='#myModal'>"
								+ "<i class='la la-edit'></i></button>"
								+ "<button type='button' class='btn btn-link btn-simple-primary'>"
								+ "	<i class=]la la-times]></i>	</button></td></tr>";			
					});
					$("#proTable").html(str);
				}
			});
		});
	},
	updateProjectManagement: function(){
		$('#myModal').on('shown.bs.modal', function () {
			var oldProjectMembers = $("#projectMembers").val();
			oldprojectMembers=oldProjectMembers.replace(/，/ig,',');
			var oldProjectLeader = $("#projectLeader").val();
			var oldProjectTutor = $("#projectLeader").val();	
		$("#updateBtn").click(function(){
			var proName = $("#proName").val();
			var proType = $("#proType").val();
			var projectMembers = $("#projectMembers").val();
			projectMembers=projectMembers.replace(/，/ig,',');
			var projectTutor = $("#projectTutor").val();
			var projectLeader = $("#projectLeader").val();
			$.ajax({
				url:"updateProject",
				type:"post",
				contentType:"application/json",
				data:JSON.stringify({"proName":proName,"proType":proType,
					 "projectMembers":projectMembers,"projectTutor":projectTutor,
					 "projectLeader":projectLeader,"id":"11","oldProjectMembers":oldProjectMembers,
					 "oldProjectLeader":oldProjectLeader,"oldProjectTutor":oldProjectTutor}),
				success : function(data){
					alert(data);
				}
			});
		});
	
		});
	}
};
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

$(function(){
	var per = new Manager.Personal();
	per.init();
});