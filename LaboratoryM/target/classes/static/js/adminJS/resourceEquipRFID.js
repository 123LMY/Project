if (!window.Index)
	Index = {};
Index.EquipRFID = function() {
};

Index.EquipRFID.prototype = {
	init : function() {
		var that = this;
		$("#EquRFID_save").click(function() {//保存
			that.EquipRFIDsave();
		});
		$("#EquRFID_uqdate").click(function() {//修改
			that.EquipRFID();
			that.EquRFID_uqdate();
		});
		$("#EquRFID_delete").click(function() {//删除
			that.EquRFID_delete();
		});

	},
	//修改
	EquRFID_uqdate : function() {
		var that = this;
		bootbox.setLocale("zh_CN");
		bootbox.confirm({
			title : "提示",
			animate : true,
			className : "bootbox",
			buttons : {
				cancel : {
					label : '<i class="glyphicon glyphicon-remove"></i> 取消'
				},
				confirm : {
					label : '<i class="glyphicon glyphicon-ok"></i> 确定'
				}
			},
			message : "确定修改吗?",
			callback : function(result) {
				if (result == true) {
					that.EquipRFID();
				} else {

				}
			}
		})
		toastr.options = {
			closeButton : true, //是否在通知弹窗上面显示关闭按钮，true：显示；false：不显示
			debug : false,
			//progressBar:true, //进度条 
			positionClass : "toast-top-center", //位置信息，消息弹窗显示的位置，可以显示的位置对应的值
			maxOpened : 0, //页面一次性最多显示多少个toastr.
			onclick : null,
			showDuration : "300", //显示动作（从无到有这个动作）持续的时间
			hideDuration : "1000", //隐藏动作持续的时间
			timeOut : "1500",
			extendedTimeOut : "1000", //显示的方式，和jquery相同，可以是show()
			showEasing : "swing",
			hideEasing : "linear",
			showMethod : "fadeIn",
			hideMethod : "fadeOut"
		};
	},
	//删除
	EquRFID_delete : function() {
		var that = this;
		bootbox.setLocale("zh_CN");
		bootbox.confirm({
			title : "提示",
			animate : true,
			className : "bootbox",
			buttons : {
				cancel : {
					label : '<i class="glyphicon glyphicon-remove"></i> 取消'
				},
				confirm : {
					label : '<i class="glyphicon glyphicon-ok"></i> 确定'
				}
			},
			message : "确定删除吗?",
			callback : function(result) {
				if (result == true) {

					toastr.success('删除成功');
					that.clear_toastr(1600);
				} else {

				}
			}
		})
		toastr.options = {
			closeButton : true, //是否在通知弹窗上面显示关闭按钮，true：显示；false：不显示
			debug : false,
			//progressBar:true, //进度条 
			positionClass : "toast-top-center", //位置信息，消息弹窗显示的位置，可以显示的位置对应的值
			maxOpened : 0, //页面一次性最多显示多少个toastr.
			onclick : null,
			showDuration : "300", //显示动作（从无到有这个动作）持续的时间
			hideDuration : "1000", //隐藏动作持续的时间
			timeOut : "1500",
			extendedTimeOut : "1000", //显示的方式，和jquery相同，可以是show()
			showEasing : "swing",
			hideEasing : "linear",
			showMethod : "fadeIn",
			hideMethod : "fadeOut"
		};
	},

	EquipRFID : function() {
		$(".update").attr("disabled", false);
		$("#EquRFID_uqdate").css("display", "none");
		$("#EquRFID_save").css("display", "block");
	},
	EquipRFIDsave : function() {
		var that = this;
		toastr.success('修改成功');
		that.clear_toastr(1600);
		$(".update").attr("disabled", true);
		$("#EquRFID_uqdate").css("display", "block");
		$("#EquRFID_save").css("display", "none");
	},
	clear_toastr : function(time) {//提示框关闭时间
		setTimeout(function() {
			toastr.clear();
		}, time);
	},
};
$(function() {
	var index = new Index.EquipRFID();
	index.init();
});