if(!window.Manager) Manager = {};
Manager.ProjectManagerment = function(){
	this.projectType = new Map();
	this.projectType.set("0","创新项目");
	this.projectType.set("1","竞赛项目");
	this.projectType.set("2","企业项目");
	this.projectType.set("3","其他");
	this.ids = new Array();
	this.AwardGrade =['校级','省级','国家','国际'];
	
};

Manager.ProjectManagerment.prototype = {
		init : function(){
			var that = this;
			$(".project_management_navbar").click(function(){
				that.ids = new Array();
				that.loadProjectManagementPage();
				that.loadProjectManagementPageData();
				that.loadGlyphicon();
				that.search();
				that.deleteProjectByIds();
			
				that.projectConclusion();
				
			});
			
		},
		
		/**
		 * 加载项目管理界面
		 */
		loadProjectManagementPage: function(){
			var obj = this;
			var timestamp = new Date().getTime();
			$.ajax({
				url:"/Project/User/loadProjectManagementPage?time="+timestamp,
				type : "POST",
				async : false,
				contentType : "application/json",
				success : function(data) {
					$("#right_content").html(data);
					obj.loadProjectManagementTable();
					obj.updateProjectManagement();
					 
				},
				error : function() {
					alert("加载失败，请刷新页面重试");
				}
			});
		},
		/**
		 * 加载项目管理界面的数据
		 */
		loadProjectManagementPageData: function(){
			var timestamp = new Date().getTime();
			$.ajax({
				url:"/Project/User/loadProjectManagementPageData?time="+timestamp,
				type:"post",
				contentType:"application/json",
				dataType:"json",
				success: function(data){				
					$('#tb_project').bootstrapTable("load",data);
					$("#searchName").keyup(function(){
						if(window.event.keyCode ==13){
							$(".searchBtn").click();
						}
					})
				}
			});			     
		},
		/**
		 * 加载表格
		 */
		loadProjectManagementTable: function(){
			var obj = this;
			$('#tb_project').bootstrapTable({	    
	            method: 'get',                      //请求方式（*）
	            toolbar: '#toolbar',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）	            
	            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 6,                       //每页的记录行数（*）
	            pageList: [10, 25, 50, 100,'ALL'],        //可供选择的每页的行数（*）	         
	            minimumCountColumns: 2,             //最少允许的列数
	            clickToSelect: false,                //是否启用点击选中行
	            uniqueId: "id",                     //每一行的唯一标识，一般为主键列         
	            cardView: false,                    //是否显示详细视图	  
	            onCheck:function(row){
	               obj.ids.push(row.id);      
	            },
	            onUncheck:function(row){
	            	for(var i=0; i<obj.ids.length; i++) {
	            	    if(obj.ids[i] == row.id) {
	            	      obj.ids.splice(i, 1);

	            	    }  
	            	}
	            },
	            onCheckAll:function(rows){
	            	for(var i=0;i<rows.length;i++){
	            		obj.ids.push(rows[i].id);
	            	}
	            },
	            onUncheckAll:function(rows){
	            	for(var i=0; i<obj.ids.length; i++) {
	            		for(var j=0;j<rows.length;j++){
	            			if(obj.ids[i] == rows[j].id) {
	  	            	      obj.ids.splice(i, 1);	  	            	    
	  	            	    }   
		            	}
	            	    
	            	}
	            },
	            columns: [{
	                checkbox: true
	            }, {
	                field: 'proName',
	                title: '项目名称',
	                align: 'center',
	            }, {
	                field: 'foreMember',
	                title: '项目成员',
	                align: 'center',	   
	            }, {
	                field: 'projectTutor',
	                title: '指导老师',
	                align: 'center',
	            }, {
	                field: 'projectStatus',
	                title: '项目状态',
	                align: 'center',
	            },{
	                field: 'Operation',
	                title: '操作',
	                align: 'center',
	                events: projectClick,
	                formatter : function(value,
							row, index) {
	                	if(row.projectStatus =='已结题'){
	                		var e = '<button type="button"   class="concluBtn btn '
	                			+'btn-link btn-simple-primary"  style="background-color:#90EE90;color:white;margin-left:2%;border-radius:20%;">'
								+'<i class="glyphicon glyphicon-time"></i> </button>'
	                		var d = '<button type="button" disabled="disabled" class="deletePro btn btn-link btn-simple-primary" '
	                			+'style="background-color:#CE212A;color:white;margin-left:2%;border-radius:20%;">'
								+'<i class="glyphicon glyphicon-remove"></i> </button>'					
								return e+d;
	                	}
	                	if(row.projectOperation == "true"){
	                		var e = '<button type="button"  class="editPro btn btn-link btn-simple-primary"  '
	                    		+'style="background-color:#265A88;color:white;margin-left:2%;border-radius:20%;">'
							+'<i class="glyphicon glyphicon-edit"></i> </button>'
						
							var d = '<button type="button"   class="deletePro btn btn-link btn-simple-primary" '
								+'style="background-color:#CE212A;color:white;margin-left:2%;border-radius:20%;">'
							+'<i class="glyphicon glyphicon-remove"></i> </button>'
						
							return e+d;
	                	}else if(row.projectOperation == "false"){
	                		var e = '<button type="button"  class="editPro btn btn-link btn-simple-primary"  '
	                    		+'style="background-color:#265A88;color:white;margin-left:2%;border-radius:20%;">'
							+'<i class="glyphicon glyphicon-edit"></i> </button>'
						
							var d = '<button type="button" disabled="disabled"   class="deletePro btn btn-link btn-simple-primary" '
								+'style="background-color:#CE212A;color:white;margin-left:2%;border-radius:20%;">'
							+'<i class="glyphicon glyphicon-remove"></i> </button>'
						
							return e+d;
	                	}else{
	                		var e = '<button type="button"  class="editPro btn btn-link btn-simple-primary"  '
	                    		+'style="background-color:#265A88;color:white;margin-left:2%;border-radius:20%;">'
							+'<i class="glyphicon glyphicon-edit"></i> </button>'
						
							var d = '<button type="button"     class="deletePro btn btn-link btn-simple-primary" '
								+'style="background-color:#CE212A;color:white;margin-left:2%;border-radius:20%;">'
							+'<i class="glyphicon glyphicon-remove"></i> </button>'
						
							return e+d;
	                	}
	                    	
						 
					}
	                
	            }, ]
	        });	
		},
		/**
		 * 加载项目成员增加按钮事件
		 */
		loadGlyphicon : function(){
			var inputdiv = $("#inputProjectMember");//输入框包装ID
			var addbtn = $("#addinputBtn");
			var x = inputdiv.length;
			var l = $("#inputProjectMember input").length;
			$(addbtn).click(function (e) //on add input button click
			{
			    //添加输入框
				if(x>3){					 
					swal({
						title:"已经加到底啦！",
						type:"info",
						timer:1000,
						showConfirmButton: false
					});
					return false;
				}else{
					inputdiv.append('<div> <input type =“text” class="form-control" name ="projectMembers'+(l+1)+'"/>'
						    +'<input type =“text” class="form-control" name ="projectMembers'+(l+2)+'"/>' 
						    +'<input type =“text” class="form-control" name ="projectMembers'+(l+3)+'"/>' 
						    +'<button class="removeclass btn btn-link btn-simple-primary" ><i class="glyphicon glyphicon-minus"></i></button> </ div>');
							x++;
							l = l+3;
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
		 * 根据关键字搜索
		 */
		search : function(){
			var obj = this;
			$(".searchBtn").click(function(){
				var searchName = $("#searchName").val();
				if(searchName == ""){
					toastr.warning("请输入关键字");
					return ;
				}
				var timestamp = new Date().getTime();
				$.ajax({
					url:"/Project/User/selectProjectBySerachName?time="+timestamp,
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"searchName":searchName}),
					success : function(data){	
						$('#tb_project').bootstrapTable('destroy');
						obj.loadProjectManagementTable();
						$('#tb_project').bootstrapTable('load',data);
					}
				});
			});
		},
		
		/**
		 * 更新项目
		 */
		updateProjectManagement: function(){
			
				
			$(".pmBtn").click(function(){
				var proName = $("#modalProName").val();
				if(proName == ""){		
					 swal({
						title:"项目名称不能为空",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					 });
					return;
				}else{
					var ref = /^[A-Za-z0-9\u4e00-\u9fa5]+$/;
					if(!ref.exec(proName)){
						 swal({
								title:"项目名称包含非法字符串",
								type:"warning",
								timer:1000,
								showConfirmButton: false
							 });
						 return ;
					}
				}
				var proType = $(".modalProType").val();
				var newProjectMembers = new Array();
				$("#inputProjectMember input[type='text']").each(function(){
					newProjectMembers.push($(this).val());
				}) ;
				var newProjectTutor = $("#modalTutor").val();
				var newProjectLeader = $("#modalLeader").val();
				var projectId = $("#projectId").val();
				var timestamp = new Date().getTime();
				$.ajax({
					url:"/Project/User/updateProject?time="+timestamp,
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"proName":proName,"proType":proType,
						 "newProjectMembers":newProjectMembers,"projectTutor":newProjectTutor,
						 "projectLeader":newProjectLeader,"projectId":projectId}),
					success : function(data){
						$("#editProject").modal('hide');					 
					}
				});
			});
		},
		
		/**
		 * 批量删除项目
		 */
		deleteProjectByIds : function(){
			var obj = this;	
			$("#btn_delete").click(function(){
				if(obj.ids.length<1){
					 swal({
  	 					title:"警告",
  	 					text:"至少选择一项以上",
  	 					type:"warning",
  	 					timer:1000,
  	 					showConfirmButton: false 
  	 				});
				}else{		
					swal({
		    	 		title:"您确定要删除吗？",
		    	 		text:"删除之后将无法恢复，请谨慎操作！",
		    	 		type:"warning",
		    	 		showCancelButton:"true",
		    	 		confirmButtonColor:"#DD6B55",
		    	 		confirmButtonText:"确认删除！",
		    	 		cancelButtonText:"取消",
		    	 		closeOnConfirm:false
		    	 	},function(isConfirm){
		    	 		if(isConfirm){
		    	 			var timestamp = new Date().getTime();
		    	 			$.ajax({
					    		 url:"/Project/User/deleteProjectById?time="+timestamp,
					    		 contentType:"application/x-www-form-urlencoded",
					    		 type:"post",
					    		 data:{"projectId":obj.ids},
					    		 success :function(data){
					    			 if(data == "success"){
					    				 swal({
				         	 					title:"删除成功！",
				         	 					text:"您已经成功删除",
				         	 					type:"success",
				         	 					timer:1000,
				         	 					showConfirmButton: false 
				         	 				});
					    				 $("#tb_project").bootstrapTable('remove', {field: 'id', values: obj.ids});
					    			 }
					    			 
					    		 },
					    		 error : function(){
					    			 alert("出错了！");
					    		 }
					    	 
					    	 });
		    	 		}
		    	 	});
					
			 
				}
			
			});
		},
		
		/**
		 * 加载项目结题页面
		 */
		projectConclusion : function(){
			var obj = this; 				 
			$("#btn_conclusion").click(function(){
				if(obj.ids.length>1){
					swal({
						title:"只能选择一项结题！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					});
					return ;
				}else if(obj.ids.length<1){
					swal({
						title:"至少选择一项结题！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					});
					return ;
				}else{
					var str = $('#tb_project').bootstrapTable('getSelections');	  
					var timestamp = new Date().getTime();
					$.ajax({
						url:"/ProjectResult/User/loadProjectConclusionPage?time="+timestamp,
						type:"post",
						contentType:"application/json",
						success : function(data){
							$("#right_content").html(data);
							obj.initTime();
							obj.addAwardInformation();
							obj.addPatent();
							obj.addTransfer();
							obj.subProjectConclusion();
							 
							str.forEach(function(value,item){
								if(value.endTime != null){
									$("#pjEndTime").val(value.endTime);
									$("#projectEndtime").val(value.endTime);
									$(".pjtime").datetimepicker("remove");
								}
								 $("#proName").text(value.proName);
								 $("#proType").text(obj.projectType.get(value.proType));
								 $("#proMember").text(value.foreMember);
								 $("#proTutor").text(value.projectTutor);
								 $("#proLeader").text(value.projectLeader);
								 $("#projectId").val(value.id);
							});
							
							 obj.AwardGrade.forEach(function(value,item){
								 $("#prizeLevel").append("<option value = '"+value+"'>"+value+"</option>"); 
							 });
							 
						}
					});
				}
				
			});
		},
		/**
		 * 初始化时间
		 */
		initTime : function(){
			 $('.form_datetime_word').datetimepicker({
			        language:  'fr',
			        weekStart: 1,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,				
					startView: 4,
					minView: 4,
					forceParse: 0,
			        showMeridian: 0,
			        keyboardNavigation:true,
			        format:"yyyy"
			    });
			 $('.form_datetime').datetimepicker({
			        language:  'fr',
			        weekStart: 1,
			        todayBtn:  1,
					autoclose: 1,
					todayHighlight: 1,				
					minView: "month",
					forceParse: 0,
			        showMeridian: 0,
			        keyboardNavigation:true,
			        format:"yyyy-mm-dd"
			    });
		},
		/**
		 * 添加获奖信息的表格
		 */
		addAwardInformation : function(){
			var x = 1;		
			var obj = this;
			$("#addTrWord").click(function(){
				if(x>3){
					swal({
						title:"已经加到底啦！！！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					});
				}else{
					var trHtml = "<tr>"
						+"<td><input type='text' name='contestName' class='form-control' placeholder='输入赛事名称'/></td>"
						+"<td><select class='form-control prizeLevel' name='prizeLevel'>"
						+"<option>校级</option><option>省级</option><option>国家</option><option>国际</option>"
						+"</select></td>"
						+'<td><input type="text" name="prizeName" class="form-control" placeholder="输入获奖奖项"/></td>'
						+'<td>'
							+'<div class="input-group date form_datetime_word">'
							+'<input class="form-control" type="text" name="prizeYear" value="" readonly placeholder="获奖时间">'
							+'<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div> '
						+'</td>'
						+'<td>'
							+'<div class="file-container" style="display:inline-block;position:relative;overflow: hidden;vertical-align:middle">'
							+'<button class="btn btn-danger fileinput-button" type="button" style="width:100%"><i class="glyphicon glyphicon-file"></i>上传文件</button>'
							+'<input type="file" name="prizeFile" style="position:absolute;top:0;left:0;font-size:34px; opacity:0">'
							+'</div>'
						+'</td>'
						+'<td>'
							+'<button type="button" class="removeAITrWord btn btn-primary" style="float: left;width:100%;">'
							+'<i class="glyphicon glyphicon-minus" style="width:auto"></i>'
							+'</button>'
						+'</td>'
						+"</tr>";
					$("#table_word").append(trHtml);//在表格最后一行添加tr
				
					obj.initTime();
				}	
				x++;
				return false;
			});
			$("body").on("click",".removeAITrWord", function(e){ 
			    if( x > 1 ) {
			        $(this).parent('td').parent('tr').remove();
			        x--; 
			    }
				return false;
			});
		},
		/**
		 * 添加专利的表格
		 */
		addPatent : function(){
			var x = 1;
			var obj = this;
			$("#addPatentTr").click(function(){
				if(x>3){
					swal({
						title:"已经加到底啦！！！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					});					
				}else{
					var trHtml = '<tr>'
						+'<td><input type="text" name="patentName" class="form-control" placeholder="输入专利名称"/></td>'
						+'<td>'
							+'<div class="input-group date form_datetime_word">'
							+'<input class="form-control" type="text" name="patentYear" value="" readonly placeholder="专利时间">'
							+'<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div> '
						+'</td>'
						+'<td>'
							+'<div class="file-container" style="display:inline-block;position:relative;overflow: hidden;vertical-align:middle">'
							+'<button class="btn btn-danger fileinput-button" type="button" style="width:100%"><i class="glyphicon glyphicon-file"></i>上传文件</button>'
							+'<input type="file" name="patentFile"  style="position:absolute;top:0;left:0;font-size:34px; opacity:0">'
							+'</div>'
						+'</td>'
						+'<td>'
							+'<button type="button" class="removePatentTrWord btn btn-primary" style="float: left;width:100%;">'
							+'<i class="glyphicon glyphicon-minus" style="width:auto"></i>'
							+'</button>'
						+'</td>'
						+'</tr>';
						$("#table_patent").append(trHtml);//在表格最后一行添加tr	
						obj.initTime();
				}
				x++;
				return false;
			});
			$("body").on("click",".removePatentTrWord", function(e){ 
			    if( x > 1 ) {
			        $(this).parent('td').parent('tr').remove();
			        x--; 
			    }
				return false;
			});
		},
		/**
		 * 添加成果转让的表格
		 */
		addTransfer : function(){
			var x = 1;
			var obj = this;
			$("#addTransferTr").click(function(){
				if(x>3){
					swal({
						title:"已经加到底啦！！！",
						type:"warning",
						timer:1000,
						showConfirmButton: false
					});					
				}else{
					var trHtml = '<tr>'
						+'<td><input type="text" name="resultTransfer" class="form-control" placeholder="成果转让情况描述，未转让可不填"/></td>'
						+'<td>'
							+'<div class="input-group date form_datetime_word">'
							+'<input class="form-control" type="text" name="resultYear" value="" readonly placeholder="转让时间">'
							+'<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span></div> '
						+'</td>'
						+'<td>'
							+'<div class="file-container" style="display:inline-block;position:relative;overflow: hidden;vertical-align:middle">'
							+'<button class="btn btn-danger fileinput-button" type="button" style="width:100%"><i class="glyphicon glyphicon-file"></i>上传文件</button>'
							+'<input type="file" name="resultFile"  style="position:absolute;top:0;left:0;font-size:34px; opacity:0">'
							+'</div>'
						+'</td>'
						+'<td>'
							+'<button type="button" class="removeTFTrWord btn btn-primary" style="float: left;width:100%;">'
							+'<i class="glyphicon glyphicon-minus" style="width:auto"></i>'
							+'</button>'
						+'</td>'
						+'</tr>';
						$("#table_transfer").append(trHtml);//在表格最后一行添加tr
						obj.initTime();
				}	
				x++;
				return false;
			});
			$("body").on("click",".removeTFTrWord", function(e){ 
			    if( x > 1 ) {
			        $(this).parent('td').parent('tr').remove();
			        x--; 
			    }
				return false;
			});
	 
		},
		
		/**
		 * 提交项目结题成果
		 */
		subProjectConclusion : function(){
			
			$("#subBtn").click(function(){
				var endTime = $("#projectEndtime").val();
				 
				if(endTime == ""){
					swal({
					title : "警告",
					text : "结题时间不能为空!",
					type : "warning",
					timer : 1000,
					showConfirmButton : false
				})
					return;
				}
				var formData = new FormData($('#conclusionForm')[0]);
				var timestamp = new Date().getTime();
				$.ajax({
    				url:"/ProjectResult/User/projectConclusion?time"+timestamp,
    				type:"post",
    				data:formData,
    				cache: false,
                    processData: false,
                    contentType: false,
    				success:function(data){
    					if(data.msg == "different"){
    						swal({
        						title:"警告",
        						text:"文件类型只能为 'jpg','png','jpeg','pdf'",
        						type:"warning",
        						timer:1000,
        						showConfirmButton: false
        					});
    					}else if(data.msg == "success"){
    						swal({
        						title:"提交成功！",
        						type:"success",
        						timer:1000,
        						showConfirmButton: false
        					});
    					}else{
    						swal({
        						title:"系统出错！",
        						type:"error",
        						timer:1000,
        						showConfirmButton: false
        					});
    					}
    					
    				},
    				error: function(){
    					alert("upload error");
    				}
    			});
			});
			
		},
		checkMember: function(){
			$("[name='projectMembers']").blur(function(){
				var str = $(this).val();
				var obj = $(this);
				var that = this;
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
				var that = this;
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
	
				
};
window.projectClick = {
		/**
		 * table里编辑按钮的点击事件
		 * @param e
		 * @param value
		 * @param row
		 * @param index
		 */
		'click .editPro': function (e, value, row, index) {
			var pm = new Manager.ProjectManagerment();
			pm.checkMember();
			pm.projectType.forEach(function (value, key, map) {
				if($(".modalProType option").length >=4){		 
				}else{
					$(".modalProType").append("<option  value=" +key + ">" +value+ "</option>");
				}
			});	
			$("#editProject").modal('show');
			$("#modalProName").val(row.proName);
			$(".modalProType").val(row.proType);
			$("#modalTutor").val(row.projectTutor);
			$("#modalLeader").val(row.projectLeader);
			var projectMenber = row.foreMember;
			var pmList =new Array(); 
			pmList = projectMenber.split(" ");
			var length = $("#inputProjectMember input").length;
			if(pmList.length>4){
				$("#inputProjectMember").append('<div> <input type =“text” class="form-control" name ="projectMembers'+(length+1)+'" />'
					    +'<input type =“text” class="form-control" name ="projectMembers'+(length+2)+'"/>' 
					    +'<input type =“text” class="form-control" name ="projectMembers'+(length+3)+'"/>' 
					    +'<button class="removeclass btn btn-link btn-simple-primary" ><i class="glyphicon glyphicon-minus"></i></button> </ div>');		
				length = length+3;
			}
			for(var j = pmList.length;j>=0;j--){ 
					$("#inputProjectMember input[name = 'projectMembers"+(j+1)+"']").val(pmList[j]);
			}
			$("#projectId").val(row.id); 
			
     },
     /**
      * table删除按钮的点击事件
      * @param e
      * @param value
      * @param row
      * @param index
      */
     'click .deletePro': function(e,value,row,index){
    	 var ids = [];//定义一个数组
    	 ids.push(row.id);//将要删除的id存入数组
    	 swal({
 	 		title:"您确定要删除吗？",
 	 		text:"删除之后将无法恢复，请谨慎操作！",
 	 		type:"warning",
 	 		showCancelButton:"true",
 	 		confirmButtonColor:"#DD6B55",
 	 		confirmButtonText:"确认删除！",
 	 		cancelButtonText:"取消",
 	 		closeOnConfirm:false
 	 	},function(isConfirm){
 	 		if(isConfirm){
 	 			var timestamp = new Date().getTime();
 	 			 $.ajax({
 	 	    		 url:"/Project/User/deleteProjectById?time"+timestamp,
 	 	    		 contentType:"application/x-www-form-urlencoded",
 	 	    		 type:"post",
 	 	    		 data:{"projectId":ids},
 	 	    		 success :function(data){
 	 	    			 swal({
      	 					title:"删除成功！",
      	 					text:"您已经成功删除",
      	 					type:"success",
      	 					timer:1000,
      	 					showConfirmButton: false 
      	 				});
 	 	    			 $("#tb_project").bootstrapTable('remove', {field: 'id', values: ids});
 	 	    		 },
 	 	    		 error : function(){
 	 	    			 alert("出错了！");
 	 	    		 }
 	 	    	 
 	 	    	 });
 	 		}
 	 		
 	 	});
    
    
    	 
     },
     /**
      * table里结题按钮的点击事件
      * @param e
      * @param value
      * @param row
      * @param index
      */
     'click .concluBtn': function(e,value,row,index){
    	 var pm = new Manager.ProjectManagerment();
    	 var timestamp = new Date().getTime();
    	 $.ajax({
				url:"/ProjectResult/User/loadProjectConclusionPage?time="+timestamp,
				type:"post",
				contentType:"application/json",
				success : function(data){
					$("#right_content").html(data);		  
						 $("#proName").text(row.proName);
						 $("#proType").text(pm.projectType.get(row.proType));
						 $("#proMember").text(row.foreMember);
						 $("#proTutor").text(row.projectTutor);
						 $("#proLeader").text(row.projectLeader);
						 $("#projectId").val(row.id);	
						 if(row.endTime != null){
								$("#pjEndTime").val(row.endTime);
								$("#projectEndtime").val(row.endTime);
								$(".pjtime").removeClass("form_datetime");
							}
						 	pm.initTime();
							pm.addAwardInformation();
							pm.addPatent();
							pm.addTransfer();
							pm.subProjectConclusion();
						 pm.AwardGrade.forEach(function(value,item){
								 $("#prizeLevel").append("<option value = '"+value+"'>"+value+"</option>"); 
						 });
				}
			});
     }
};

$(function(){
	var pm = new Manager.ProjectManagerment();
		pm.init();
});