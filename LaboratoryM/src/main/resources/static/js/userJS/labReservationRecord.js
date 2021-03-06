if(!window.Manager) Manager= {};
Manager.LabReservationRecord = function(){
	this.auditStatus = new Map();
	this.auditStatus.set("0","预约");
	this.auditStatus.set("1","拒绝");
	this.auditStatus.set("2","借出");
	this.auditStatus.set("3","还回");
	this.count = 60;
};
Manager.LabReservationRecord.prototype = {
		
	init : function(){
		var that = this;
		that.loadLabReservationRecordPage();
 
				
	},
	/**
	 * 加载实验室预约记录页面
	 */
	loadLabReservationRecordPage : function(){
		var obj = this;
		$(".labReservationRecord").click(function(){
			var ts = new Date().getTime();
			$.ajax({
				url:"/LabApply/User/loadLabReservationRecordPage?time="+ts,
				type:"post",
				async:false,
				contentType:"application/json",
				success: function(data){
					$("#right_content").html(data);
					obj.loadLabReservationRecordTable();
					obj.initTime();
					obj.loadLabReservationRecordTableData();
					obj.searchByTime();
					 $("#left_content").css("display","none");
				}
			});
		});
	
	},
	/**
	 * 加载实验室预约记录表格
	 */
	loadLabReservationRecordTable : function(){
		var obj = this;
	    $('#tb_laborRecord').bootstrapTable({
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
                field: 'labName',
                title: '实验室名称',
                align: 'center',
            }, {
                field: 'startTime',
                title: '使用开始时间',
                align: 'center',
                formatter : function (value, row, index){
                	if(value == null){
                		return null;
                	}else{
                		value  = value.replace("T"," ");
                  		return value.substr(0,16);    
                	}
                	     
                },
            }, {
                field: 'endTime',
                title: '使用结束时间',
                align: 'center',
                formatter : function (value, row, index){
                	if(value == null){
                		return value;
                	}else{
                		value  = value.replace("T"," ");               	                
                  		return value.substr(0,16); 
                	}
                	        
                },
            },{
                field: 'auditStatus',
                title: '预约状态',
                align: 'center',
               formatter : function (value, row, index){
                    return obj.auditStatus.get(value);
                },
            },{
                field: 'labApplyId',
                title: '操作',
                align: 'center',
                events: labRecordClick,
                formatter : function(value,
						row, index) {
                	var status = obj.auditStatus.get(row.auditStatus);
                		if(status == "预约"){
                			var c = '<button type="button" class="btn btn-info deleteLabBtn">撤销</button> ';
        					return c ;
                		}else{
                			var c = '<button type="button" class="btn btn-danger deleteLabBtn">删除</button> ';
        					return c ;
                		}
					
				}
                
            }, ]
        });
	},
	/**
	 * 加载实验室预约记录表格的数据
	 */
	loadLabReservationRecordTableData : function(){
		var ts = new Date().getTime();
		$.ajax({
			url:"/LabApply/User/loadLabReservationRecordData?time="+ts,
			contentType:"application/json",
			type:"post",
			success : function(data){			
				$("#tb_laborRecord").bootstrapTable('load',data);
			},
			error : function(){
				alert("系统出错");
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
			minView:1,
			startView: 2,
	        keyboardNavigation:true,
	        format:"yyyy-mm-dd hh:00"
	    });
	},
	/**
	 * 根据起始时间查询实验室使用记录
	 */
	searchByTime: function(){
		var obj = this;
		$("#searchBtn").click(function(){
			var startDate = $("#startTime").data("datetimepicker").getDate();
			var startTime = startDate.getFullYear()+"-"+ (startDate.getMonth() + 1)+"-"+ startDate.getDate() +" "+startDate.getHours()+":00:00"  ;			
			var endDate = $("#endTime").data("datetimepicker").getDate();
			var endTime = endDate.getFullYear()+"-"+ (endDate.getMonth() + 1)+"-"+ endDate.getDate()+" "+endDate.getHours()+":00:00";	
			if(startTime == " " || endTime == " "){
				toastr.error("起始时间不能为空！");
			}else if(startTime == endTime){
				toastr.error("起始时间不能一致！");
			}else{
				var ts = new Date().getTime();
				$.ajax({
					url:"/LabApply/User/searchLabApplyByTime?time"+ts,
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"startTime":startTime,"endTime":endTime}),
					success: function(data){
						$("#tb_laborRecord").bootstrapTable('destroy');
						obj.loadLabReservationRecordTable();
						$("#tb_laborRecord").bootstrapTable('load',data);
					}
				});
			}
		
		
		});
	}
		
};


window.labRecordClick = {
		/**
		 * 表格删除按钮的点击事件
		 * @param e
		 * @param value
		 * @param row
		 * @param index
		 */
		'click .deleteLabBtn': function (e, value, row, index) {
			var lr = new Manager.LabReservationRecord();
			var str = null;
			if(lr.auditStatus.get(row.auditStatus) ==  "预约"){
				str="撤销";
			}else{
				str="删除";
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
    	 	},function(isConfirm){
    	 		if(isConfirm){
    	 			var ts = new Date().getTime();
    	 			var ids = [];
    	 			ids.push(row.labApplyId);
       	 		 $.ajax({
       				 url:"/LabApply/User/deleteLabApplyById?time"+ts,
       				 type:"post",
       				 contentType:"application/json",
       				 data:JSON.stringify({"labApplyId":row.labApplyId}),
       				 success : function(data){
       					 if(data == "success"){
       						 swal({
        		 					title:str+"成功！",
        		 					text:"您已经成功"+str,
        		 					type:"success",
        		 					timer:1000,
        		 					showConfirmButton: false 
        		 				});
        					$("#tb_laborRecord").bootstrapTable('remove', {field: 'labApplyId', values: ids}); 
       					 }else{
       						 swal({
         		 					title:str+"失败！",
         		 					text:"请稍后再试",
         		 					type:"error",
         		 					timer:1000,
         		 					showConfirmButton: false 
         		 				});
       					 } 
       				 }
       			 });
    	 		}   	 		
    	 	});
		 	 
     },    
};
$(function(){
	var lr = new Manager.LabReservationRecord();	
			lr.init();			
});