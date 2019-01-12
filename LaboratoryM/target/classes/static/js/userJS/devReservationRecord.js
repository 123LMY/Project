if(!window.Manager) Manager= {};
Manager.DevReservationRecord = function(){
	this.auditStatus = new Map();
	this.auditStatus.set("0","预约");
	this.auditStatus.set("1","拒绝");
	this.auditStatus.set("2","通过");
	this.auditStatus.set("3","借出");
	this.auditStatus.set("4","还回");
	
};
Manager.DevReservationRecord.prototype = {
	init : function(){
		var that = this;
		that.loadDevReservationRecordPage();
	
		 
	},
	/**
	 * 加载设备预约记录界面
	 */
	loadDevReservationRecordPage : function(){
		var obj = this;
		$(".devReservationRecord").click(function(){
			var ts = new Date().getTime();
			$.ajax({
				url:"/DeviceApply/User/loadDevReservationRecordPage?time="+ts,
				type:"post",
				async:false,
				contentType:"application/json",
				success: function(data){
					$("#right_content").html(data);
					 obj.loadDevReservationRecordTable();
					 obj.loadDevReservationRecordTableData();
					 obj.initTime();
					 obj.searchByTime();
					 $("#left_content").css("display","none");
				}
			});
		});
	
	},
	/**
	 * 初始化设备预约记录表格
	 */
	loadDevReservationRecordTable : function(){
		var obj = this;
	    $('#tb_equipRecord').bootstrapTable({
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
            
            paginationPreText: '上一页',	//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '下一页',	//指定分页条中下一页按钮的图标或文字,这里是>
            clickToSelect: false,                //是否启用点击选中行
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            cardView: false,  				//是否显示详细视图
            columns: [ {
                field: 'devName',
                title: '设备名称',
                align: 'center',
            },{
                field: 'devSn',
                title: '设备序号',
                align: 'center',
            }, {
                field: 'startTime',
                title: '使用开始时间',
                align: 'center',
             
            }, {
                field: 'endTime',
                title: '使用结束时间',
                align: 'center',
             
            },{
                field: 'auditStatus',
                title: '预约状态',
                align: 'center',
               formatter : function (value, row, index){
                    return obj.auditStatus.get(value);
                },
            },{
                field: 'devApplyId',
                title: '操作',
                align: 'center',
                events: devRecordClick,
                formatter : function(value,
						row, index) {
                	var status = obj.auditStatus.get(row.auditStatus);
                		if(status == "预约" || status == "通过"){
                			var c = '<button type="button" class="btn btn-info deleteDevBtn">撤销</button> ';
        					return c ;
                		}else{
                			var c = '<button type="button" class="btn btn-danger deleteDevBtn" >删除 </button> ';
        					return c ;
                		}
					
				}
                
            }, ]
        });
	},
	/**
	 * 加载设备预约记录表格数据
	 */
	loadDevReservationRecordTableData : function(){
		var ts = new Date().getTime();
		$.ajax({
			url:"/DeviceApply/User/loadDevReservationRecordData?time="+ts,
			contentType:"application/json",
			type:"post",
			success : function(data){
				$("#tb_equipRecord").bootstrapTable('load',data);
			}
		});
	},
	
	/**
	 * 根据起始时间查找记录
	 */
	searchByTime: function(){
		var obj = this;
		$("#searchBtn").click(function(){
			
			var startDate = $("#startTime").data("datetimepicker").getDate();
			var startTime = startDate.getFullYear()+"-"+ (startDate.getMonth() + 1)+"-"+ startDate.getDate()   ;			
			var endDate = $("#endTime").data("datetimepicker").getDate();
			var endTime = endDate.getFullYear()+"-"+ (endDate.getMonth() + 1)+"-"+ endDate.getDate() ;	
		 
			if(startTime == " " || endTime == " "){
				toastr.error("起始时间不能为空！");
			}else if(startTime == endTime){
				toastr.error("起始时间不能一致！");
			}else{
				var ts = new Date().getTime();
				$.ajax({
					url:"/DeviceApply/User/searchDevApplyByTime?time="+ts,
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"startTime":startTime,"endTime":endTime}),
					success: function(data){
						$("#tb_equipRecord").bootstrapTable('destroy');
						obj.loadDevReservationRecordTable();
						$("#tb_equipRecord").bootstrapTable('load',data);
					}
				});
			}
		
		
		});
	},
	/**
	 * 初始化时间
	 */
	initTime : function(){
		$(".form_datetime").datetimepicker({	
	        language:  'fr',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			minView:2,
			startView: 2,
			forceParse: 0,
	        showMeridian: 0,
	        keyboardNavigation:true,
	        format:"yyyy-mm-dd "
	    });
	},
};
window.devRecordClick = {
		/**
		 * 表格里撤销和删除的按钮事件
		 * @param e
		 * @param value
		 * @param row
		 * @param index
		 */
		'click .deleteDevBtn': function (e, value, row, index) {
			var dr = new Manager.DevReservationRecord();
			var str = new String();
			if(dr.auditStatus.get(row.auditStatus)=="预约" ||  dr.auditStatus.get(row.auditStatus)=="通过"){
				str = "撤销";
			}else{
				str = "删除";
			}
						
			swal({
    	 		title:"您确定要"+str+"吗？",
    	 		text:str+"之后将无法恢复，请谨慎操作！",
    	 		type:"warning",
    	 		showCancelButton:"true",
    	 		confirmButtonColor:"#DD6B55",
    	 		confirmButtonText:"确认"+str+"！",
    	 		cancelButtonText:"取消",
    	 		closeOnConfirm:false
    	 	},
    	 	function(isConfirm){
    	 		var ids = [];
    			ids.push(row.devApplyId);
    			 
    	 		if(isConfirm){
    	 			var ts = new Date().getTime();
    	 			 $.ajax({
    					 url:"/DeviceApply/User/deleteDeviceApplyById?time="+ts,
    					 type:"post",
    					 contentType:"application/json",
    					 data:JSON.stringify({"devApplyId":row.devApplyId}),
    					 success : function(data){
    						 swal({
         	 					title:str+"成功！",
         	 					text:"您已经成功"+str,
         	 					type:"success",
         	 					timer:1000,
         	 					showConfirmButton: false,
         	 				});
    						$("#tb_equipRecord").bootstrapTable('remove', {field: 'devApplyId', values: ids});
    					 }
    				 });
    	 		}; 
    	 	});
     },    
};
$(function(){
	var dr = new Manager.DevReservationRecord();
	dr.init();
});