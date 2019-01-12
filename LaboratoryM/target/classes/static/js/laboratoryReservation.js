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
		$("#labor_reservation_navbar").click(function(){
			that.loadLabtoryReservation();
			that.initTime();
			that.loadLabtoryReservationTable();
			that.submitLaboraToryAppointment();
		});
	},
	
	/**
	 * 加载项目预约界面
	 */
	loadLabtoryReservation : function(){
		$.ajax({
			url:"loadLabtoryReservationPage",
			type:"post",
			async:false,
			contentType:"application/json",
			success:function(data){
				$("#right_content").html(data);
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
            url: 'loadLabtoryReservationPageData',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            dataType:"json",
            toolbar: '#query',                //工具按钮用哪个容器
            dataField:"rows",
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）   
            sortOrder: "asc", 
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 2,                       //每页的记录行数（*）
            pageList: [10,20,30],        //可供选择的每页的行数（*）    
            queryParams: function(params){
            	var temp = {
            			limit:params.limit,
            			offset:params.offset,
            			page:(params.offset/params.limit)+1
            	};
            	return temp;
            },//传递参数（*）
            paginationPreText: '‹',	//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',	//指定分页条中下一页按钮的图标或文字,这里是>
            clickToSelect: true,                //是否启用点击选中行
            uniqueId: "labId",                     //每一行的唯一标识，一般为主键列
            cardView: false,                    //是否显示详细视图
            responseHandler:function(data){
            	return{
            		total:data.total,
            		rows:data.rows
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
                formatter : function(value,
						row, index) {
					var c = '<input type="button"data-toggle="modal" data-target="#appointment" class="btn btn-info" value="预约" /> ';
					return c ;
				}
                
            }, ]
        });
	},

	/**
	 * 提交实验室预约登记表
	 */
	submitLaboraToryAppointment :function(){
		var that = this;
		$("#laborsave").click(function(){
			var str = $("#labAppointmentForm").serializeArray();			
		});
	},
	initTime : function(){
		$(".form_datetime").datetimepicker({
	        //language:  'fr',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			minView:1,
			startView: 2,
			forceParse: 0,
	        showMeridian: 0,
	        keyboardNavigation:true,
	        format:"yyyy-mm-dd hh:00"
	    });
	}
};

$(function(){
	var lab = new Manager.Laboratory();

	lab.init();
});