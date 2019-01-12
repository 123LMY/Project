if(!window.Manager) Manager = {};

Manager.polling = function(){
	
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


  Manager.polling.prototype = {
		init: function(){		    							
			$("#insert").click(function(){
				var str = $("#deviceApplyForm").serializeObject();
				var jsonStr = JSON.stringify(str);
				alert(jsonStr);
				$.ajax({
					url:"insertDeviceApply",
					type:"POST",
					contentType:"application/json",
					data:jsonStr,
					dataType:"json",
					success:function(resData){
						//var dataObj = eval("("+resdata+")")
						//alert(dataObj);
						console.log(resData);
					},
					error: function(){
						alert("fail");
					}
					
				})
			});
			
			$("#findByTime").click(function(){
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				$.ajax({
					url:"selectDeviceApplyByTime",
					type:"POST",
					contentType:"application/json",
					data:JSON.stringify({"startDate":startDate,"endDate":endDate}),
					dataType:"text",
					success:function(resData){
						//var dataObj = eval("("+resdata+")")
						alert(resData);
					},
					error: function(){
						alert("select fail");
					}
					
				})
				
			});
			
			$("#findById").click(function(){
				var userId = $("#tUser_id_b").val();
				$.ajax({
					url:"selectDeviceApplyByUserId",
					type:"POST",
					contentType:"application/json",
					data:JSON.stringify({"tUser_id_b":userId}),
					
					success: function(resData){
						alert(1);
						$.each(resData,function(i,result){
								alert(result.purpose); 	 
						});
						
					},
					error: function(){
						alert("get error");
					}
				});
			});
		}
};

$(function(){
	var pol = new Manager.polling();
	pol.init();
});