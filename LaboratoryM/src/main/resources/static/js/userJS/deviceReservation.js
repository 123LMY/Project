if(!window.Manager) Manager = {};

Manager.DeviceReservation = function(){
	this.devStatus = new Map();
	this.devStatus.set("0","空闲");
	this.devStatus.set("1","在用");
	this.devStatus.set("2","维修");
	this.devStatus.set("3","损坏");
	this.devStatus.set("4","报废");
	this.devStatus.set("5","已预约");
};

Manager.DeviceReservation.prototype = {
		init: function(){
			var that = this;
			$(".equip_reservation_navbar").click(function(){
				that.loadDeviceReservationPage();
				that.searchDeviceByKeyWords();
				that.submitDeviceAppointment();
				 $("#left_content").css("display","none");
			});
			
		},
		/**
		 * 加载设备预约界面
		 */
		loadDeviceReservationPage : function(){
			var that = this;
			var ts = new Date().getTime();
			$.ajax({
				url:"/Device/User/loadDeviceReservationPage?time="+ts,
				type:"post",
				async:false,
				contentType:"application/json",
				success: function(data){
					$("#right_content").html(data);
					that.initTime();
					that.loadDeviceReservationTable();
					that.loadDeviceReservationTableData(); 
				}
			});
		},
		/**
		 * 初始化设备预约表格
		 */
		loadDeviceReservationTable : function(){
			var obj = this;
		    $('#deviceTab').bootstrapTable({
		    	contentType: "application/x-www-form-urlencoded",
	            method: 'get',                      //请求方式（*）
	            dataType:"json",
	            toolbar: '#query',                //工具按钮用哪个容器
	            striped: true,                      //是否显示行间隔色
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            pagination: true,                   //是否显示分页（*）   
	            sortOrder: "asc", 
	            sidePagination: 'client',           //分页方式：client客户端分页，server服务端分页（*）
	            pageNumber:1,                       //初始化加载第一页，默认第一页
	            pageSize: 6,                       //每页的记录行数（*）
	            pageList: [10,20,30],        //可供选择的每页的行数（*）     
	            paginationPreText: '‹',	//指定分页条中上一页按钮的图标或文字,这里是<
	            paginationNextText: '›',	//指定分页条中下一页按钮的图标或文字,这里是>
	            clickToSelect: false,                //是否启用点击选中行
	            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
	            cardView: false,  				//是否显示详细视图
	            onLoadSuccess : function() {
	                doEnter();
	            },
	            columns: [ {
	                field: 'devModel',
	                align:'center',
	                title: '设备型号',	              
	            }, {
	                field: 'devName',
	                title: '设备名称',
	                align: 'center',
	            }, {
	                field: 'devSn',
	                title: '设备序号',
	                align: 'center',	             
	            }, {
	                field: 'devStatus',
	                title: '设备状态',
	                align: 'center',
	                formatter : function (value, row, index){
	                    return obj.devStatus.get(value);
	                },
	            },{
	                field: 'devoperation',
	                title: '操作',
	                align: 'center',
	                events: deviceClick,
	                formatter : function(value,
							row, index) {
	                	var status = obj.devStatus.get(row.devStatus);
	                	if(status == '空闲'){
	                		var c = '<button type="button"  class="btn btn-info appointBtn">预约 </button> ';
							return c ;
	                	}else if(status== '已预约'){
	                		var c = '<button type="button"  class="btn btn-info cancelBtn"> 撤销 </button>';
							return c ;
	                	}else{
	                		var c = '<button type="button" disabled="disabled"   class="btn btn-info appointBtn" >预约</button> ';
							return c ;
	                	}
						
					}
	                
	            }, ]
	        });
		},
		/**
		 * 加载设备预约表格数据
		 */
		loadDeviceReservationTableData: function(){
			$.ajax({
				url:"/Device/User/loadDeviceReservationData",
				type:"post",
				contentType:"application/json",
				success :function(data){
					 $('#deviceTab').bootstrapTable('load',data);
					 $("#keyWords").keyup(function(){
							if(window.event.keyCode ==13){
								$("#searchBtn").click();
							}
							
						});
				}
			});
		},
		
		/**
		 * 初始化时间
		 */
		initTime : function(){
			var str = new Date();
			$(".form_datetime").datetimepicker({	
		        language:  'fr',
		        weekStart: 1,
		        todayBtn:  1,
				autoclose: 1,
				todayHighlight: 1,
				startDate:str,
				minView:2,
				startView: 2,
				forceParse: 0,
		        showMeridian: 0,
		        keyboardNavigation:true,
		        format:"yyyy-mm-dd "
		    });
		},
		/**
		 * 根据关键字查询设备
		 */
		searchDeviceByKeyWords : function(){
			var that = this;
			$("#searchBtn").click(function(){
				var keyWords = $("#keyWords").val();
				var ts = new Date().getTime();
				$.ajax({
					url:"/Device/User/selectDeviceByKeyWords?time="+ts,
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"keyWords":keyWords}),
					success: function(data){
						 $('#deviceTab').bootstrapTable('destroy');
						 that.loadDeviceReservationTable();
						 $('#deviceTab').bootstrapTable('load',data);
					}
				});
			});
		},
		/**
		 * 提交设备预约
		 */
		submitDeviceAppointment : function(){
			$("#equipsaveBtn").click(function(){
				var devDetailId = $("#devDetailId").val();
				var user = $("#equip_user").val();
				var startDate = $("#modalSTime").data("datetimepicker").getDate();
				var startTime = startDate.getFullYear()+"-"+ (startDate.getMonth() + 1)+"-"+ startDate.getDate()  ;			
				var endDate = $("#modalETime").data("datetimepicker").getDate();
				var endTime = endDate.getFullYear()+"-"+ (endDate.getMonth() + 1)+"-"+ endDate.getDate()  ;			
				var purpose = $("#purpose").val();
				if(startDate > endDate ){
					toastr.error("开始时间不能大于结束时间！");
					return ;
				}
				if(startTime =="" || endTime == ""){
					toastr.error("起始时间不能为空！");
					return ;
				}else if(startTime == endTime){
					toastr.error("起始时间不能一致!");
					return ;
				}else{
					var ts = new Date().getTime();
					$.ajax({
						url:"/DeviceApply/User/insertDeviceApply?time"+ts,
						type:"post",
						contentType:"application/json",
						data:JSON.stringify({"devDetailId":devDetailId,"user":user,"startTime":startTime,
							"endTime":endTime,"purpose":purpose}),
						success: function(data){
							if(data.status == "noneUser"){
								toastr.error("使用者未注册！");
								return ;
							}else if(data.status == "fail"){
								toastr.error("提交失败!");
								return ;
							}else{
								toastr.success("提交成功！");
								 $("#appoint_equip_modal").modal('hide');
								return ;
							};
						},
						error: function(){
							toastr.error("系统出错！");
							return ;
						}
					});
				};
			});
		}
		 
		
};
window.deviceClick = {
		/**
		 * 表格预约按钮点击事件
		 * @param e
		 * @param value
		 * @param row
		 * @param index
		 */
		'click .appointBtn': function (e, value, row, index) {
			 $("#appoint_equip_modal").modal('show');
			 $("#modalDevModel").val(row.devModel);
			 $("#modalDevName").val(row.devName);
			 $("#modalDevSn").val(row.devSn);
			 $("#devDetailId").val(row.id);
     },
     /**
      * 表格撤销按钮点击事件
      * @param e
      * @param value
      * @param row
      * @param index
      */
     'click .cancelBtn': function(e,value,row,index){
    	 	swal({
    	 		title:"您确定要撤销吗？",
    	 		text:"撤销之后将无法恢复，请谨慎操作！",
    	 		type:"warning",
    	 		showCancelButton:"true",
    	 		confirmButtonColor:"#DD6B55",
    	 		confirmButtonText:"确认撤销！",
    	 		cancelButtonText:"取消",
    	 		closeOnConfirm:false
    	 	},
    	 	function(isConfirm){
    	 		if(isConfirm){
    	 			var ts = new Date().getTime();
    	 			 alert(row.id);
    	 			$.ajax({
    	 				url:"/DeviceApply/User/deleteByDeviceDetailIdAndUserId?time="+ts,
    	 				type:"post",
    	 				contentType:"application/json",
    	 				data:JSON.stringify({"devDetailId":row.id}),
    	 				success : function(data){
    	 					if(data == "success"){
    	 						swal({
            	 					title:"撤销成功！",
            	 					text:"您已经成功撤销",
            	 					type:"success"
            	 				});
    	 						$('#deviceTab').bootstrapTable('refresh');
    	 					}
    	 					if(data == "fail"){
    	 						toastr.error("撤销失败");
    	 						return ;
    	 					}
    	 					
    	 				},
    	 				error : function(){
    	 					toastr.error("系统出错");
    	 				}
    	 				
    	 			});
    	 		} 
    	 	}
    	 	);
     }
};
$(function(){
	var dr = new Manager.DeviceReservation();
	dr.init();
});