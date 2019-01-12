if(!window.Manager) Manager = {};

Manager.Appointment = function(){
	
};

Manager.Appointment.prototype = {
		init: function(){
			$("#get").click(function(){
				$.ajax({
					url:"getAllDeviceDetail",
					type:"POST",
					contentType:"application/json",
					data:JSON.stringify({"currentPage":"2"}),
					dataType:"json",
					success:function(data){
						var item;
						$.each(data,function(i,result){
							item +=
							"<tr><td>"+result.remark+"</td></tr><br/>"; 	 
						});
						$("#main").html(item);
					},
					error: function(){
						alert("get fail");
					}
					
				});
			});
			
			
			$("#find").click(function(){
				var s = $("#keyWords").val();
				$.ajax({
					url:"getDeviceDetailByKeyWords",
					type:"POST",
					contentType:"application/json",
					data:{"keyWords":s},
					dataType:"json",
					success:function(data){
						var item;
						$.each(data,function(i,result){
							item +=
							"<tr><td>"+result.devStatus+"</td></tr><br/>"; 	 
						});
						$("#main").html(item);
					},
					error: function(){
						alert("get dev fail");
					}
					
				});
			});
		},
		
		 
		
};

$(function(){
	var ap = new Manager.Appointment();
	ap.init();
})