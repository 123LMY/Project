if(!window.Manager) Manager = {};
Manager.Laboratory = function(){
	this.labStatus = new Map();
	this.labStatus.set('0','空闲');
	this.labStatus.set('1','在用');
	this.labStatus.set('2','维修');
};

Manager.Laboratory.prototype = {
	init : function(){
		var that = this;
		$(".labor_reservation_navbar").click(function(){
			that.loadLabtoryReservation();
			that.loadLabtoryReservationTable();
			that.loadLabtoryReservationTableData();
			that.submitLaboraToryAppointment();
			that.searchByTime();
			 $("#left_content").css("display","none");
		});
	},
	
	/**
	 * 加载项目预约界面
	 */
	loadLabtoryReservation : function(){
		var obj = this;
		var ts = new Date().getTime();
		$.ajax({
			url:"/Lab/User/loadLabtoryReservationPage?time="+ts,
			type:"post",
			async:false,
			contentType:"application/json",
			success:function(data){
				$("#right_content").html(data);
				   obj.initTime();
			},
			error: function(){
				alert("加载失败，请刷新页面重试！");
			}
		});
	},
	
	/**
	 *加载表格并获取数据 
	 */
	loadLabtoryReservationTable : function(){
		var obj = this;
	    $('#tb_labor').bootstrapTable({
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
            uniqueId: "labId",                     //每一行的唯一标识，一般为主键列
            cardView: false,  				//是否显示详细视图
            responseHandler:function(data){
            	return{
            		total:data.total,
            		data:data.rows
            	};
            },
            columns: [ {
                field: '',
                align:'center',
                title: '实验室编号',
                formatter:function(value,row,index){        
                    var pageSize=$('#tb_labor').bootstrapTable('getOptions').pageSize;//通过表的#id 可以得到每页多少条
                    var pageNumber=$('#tb_labor').bootstrapTable('getOptions').pageNumber;//通过表的#id 可以得到当前第几页
                    return pageSize * (pageNumber - 1) + index + 1;    //返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
                }
            }, {
                field: 'labName',
                title: '实验室名称',
                align: 'center',
            }, {
                field: 'labStatus',
                title: '实验室状态',
                align: 'center',
                formatter : function (value, row, index){
                    return obj.labStatus.get(value);
                },
            }, {
                field: 'userCount',
                title: '使用人数',
                align: 'center',
            },{
                field: 'labId',
                title: '操作',
                align: 'center',
                events: labClick,
                formatter : function(value,
						row, index) {
					var c = '<button type="button" class="btn btn-info appointBtn">预约</button> ';
					return c ;
				}
                
            }, ]
        });
	},

	/**
	 * 加载实验室预约界面数据
	 */
	loadLabtoryReservationTableData :function(){
		var ts = new Date().getTime();
		$.ajax({
			url:"/Lab/User/loadLabtoryReservationPageData?time="+ts,
			type:"post",
			contentType:"application/json",
			success: function(data){
				 $('#tb_labor').bootstrapTable('load',data.rows);
			} 
		});
	},
	/**
	 * 提交实验室预约登记表
	 */
	submitLaboraToryAppointment :function(){
		var that = this;

		$("#laborsave").click(function(){
			var labId = $("#labId").val();
			var appointor = $("#appointor").val();
			var startDate = $("#modalSTime").data("datetimepicker").getDate();
			var startTime = startDate.getFullYear()+"-"+ (startDate.getMonth() + 1)+"-"+ startDate.getDate()+" "+ startDate.getHours()+":00:00" ;			
			var endDate = $("#modalETime").data("datetimepicker").getDate();
			var endTime = endDate.getFullYear()+"-"+ (endDate.getMonth() + 1)+"-"+ endDate.getDate()+" "+ endDate.getHours()+":00:00" ;			
			var purpose = $("#purpose").val();
			if(appointor == ""){
				toastr.error("使用人不能为空！");
				return ;
			}
			if(startDate > endDate){0
				toastr.error("开始时间不能大于结束时间！");
				return ;
			}
			if(startTime == endTime){
				toastr.error("起始时间不能一致！");
				return ;
			}else if(startTime=="" || endTime==""){
				toastr.error("起始时间不能为空！");
			}else{
				var ts = new Date().getTime();
				$.ajax({
					url:"/LabApply/User/insertLabApply?time"+ts,
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"labId":labId,"appointor":appointor,"startTime":startTime,
						 "endTime":endTime,"purpose":purpose}),
						 success : function(data){
							 if(data = "success"){
								 toastr.success("提交成功");
								 $("#appointment").modal('hide');
							 }else if(data = "none"){
								 toastr.error("使用者未注册！");
							 }else{
								 toastr.error("系统出错！");
							 }
						 }
				});
			}
			
		});
	},
	/**
	 * 根据起始时间查询实验室
	 */
	searchByTime : function(){
		var that = this;
		$("#searchBtn").click(function(){
			var startDate = $("#startTime").data("datetimepicker").getDate();
			var startTime = startDate.getFullYear()+"-"+ (startDate.getMonth() + 1)+"-"+ startDate.getDate()+" "+ startDate.getHours()+":00:00" ;			
			var endDate = $("#endTime").data("datetimepicker").getDate();
			var endTime = endDate.getFullYear()+"-"+(endDate.getMonth() + 1)+"-"+ endDate.getDate()+" "+ endDate.getHours()+":00:00" ;	
			if(startTime == "" || endTime ==""){
				toastr.error("起始时间不能为空");
			}else if(startTime == endTime){
				toastr.error("起始时间不能一致！");
			}else{
				var ts = new Date().getTime();
			 $.ajax({
				 url:"/LabApply/User/serachLabByTime?time="+ts,
				 type:"post",
				 contentType:"application/json",
				 data:JSON.stringify({"startTime":startTime,"endTime":endTime}),
				 success: function(data){
					 $('#tb_labor').bootstrapTable('destroy');
					 that.loadLabtoryReservationTable();
					 $('#tb_labor').bootstrapTable('load',data.rows);
				 }
			 });
			};
			
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
			minView:1,
			startView: 2,
			forceParse: 0,
	        showMeridian: 0,
	        initialDate:" ",
	        keyboardNavigation:true,
	        format:"yyyy-mm-dd hh:00"
	    });
	},
	
};

window.labClick = {
		/**
		 * 表格里预约按钮的点击事件
		 * @param e
		 * @param value
		 * @param row
		 * @param index
		 */
	     'click .appointBtn': function (e, value, row, index) {
			 $("#appointment").modal('show');
			 $("#modalLaborName").val(row.labName);
			 $("#labId").val(row.labId);
	     }
	     
};

$(function(){
	var lab = new Manager.Laboratory();

	lab.init();
});