if(!window.Manager)Manager = {};
Manager.Announcement = function(){
	
};
Manager.Announcement.prototype = {
		init : function(){
			var obj = this;
			obj.initToastr();
		    obj.loadAnnouncementPage();
		    obj.loadUserIndexData();
		    obj.userLoginOut();
		    obj.checkLabApply();
		    obj.clickNavbar();
		    obj.showAllNotice();
		    obj.backNotice();
		},

		loadAnnouncementPage : function(){
			var obj = this;
			var ts = new Date().getTime();
				$.ajax({
					url:"/Notice/User/loadAnnouncementPage?time="+ts,
					type:"post",
					contentType:"application/json",
					success : function(data){
						obj.loadAnnouncementData();
						$("#right_content").html(data);							
						 $(".demo1").bootstrapNews({
					            newsPerPage: 15,
					            autoplay: true,
								pauseOnHover:true,
					            direction: 'up',
					            newsTickerInterval: 1000,
					             
					        });
						 obj.checkTime();
					 
					}
				});			
		},
		
		loadAnnouncementData : function(){
			var ts = new Date().getTime();
			var obj = this;
			$.ajax({
				url:"/Notice/User/loadAnnouncementData?time="+ts,
				type:"post",
				contentType:"application/json",
				success : function(data){
					var str = new String();
					$.each(data,function(item,value){					  
						str  = "<tr> "+"<td> <a href='#' value='"+value.description+" ' class='notices'>"+value.typeName+"</a></td></tr>";
						$("."+item).html(str);
						
					});			
					$("#readMore").click(function(){
						$("#display_bulletin").css("display","none");
						$("#showAllBulletin").css("display","block");
						
					});
					obj.noticeClick();					 
				}
			});
		},
		loadUserIndexData : function(){
			var ts = new Date().getTime();
			$.ajax({
				url:"/User/loadUserIndexData?time="+ts,
				type:"post",
				contentType:"application/json",
				success : function(data){
					$("#loginName").text(data.user.realName);
					var userType = data.flag;
					 
					if(userType == "true"){
						$(".labor_reservation_navbar").css("display","none");
					}
				}
			});
		},
		userLoginOut : function(){
			$(".loginOut").click(function(){
				var ts = new Date().getTime();
				$.ajax({
					url:"/User/userLoginOut?time="+ts,
					type:"post",
					contentType:"application/json",
					success : function(data){
						if(data.status == "success"){
							toastr.success("退出成功！");
						}
						setTimeout(function(){
							window.location.href = data.url;
						},2000);
						
					}
				});
			});
		},
		initToastr:function(){
			toastr.options = {  
			        closeButton: false,  
			        debug: false,  
			        progressBar: true,  			      
			        onclick: null,  
			        showDuration: "300",  
			        hideDuration: "1000",  
			        timeOut: "2000",  
			        extendedTimeOut: "1000",  
			        showEasing: "swing",  
			        hideEasing: "linear",  
			        showMethod: "fadeIn",  
			        hideMethod: "fadeOut"  
			    };
		},
		checkLabApply : function(){
			$.ajax({
				url:"/LabApply/User/checkApplyByUserId",
				type:"post",
				contentType:"application/json",
				success : function(data){
					
					if(data.status == "true"){
						Lobibox.notify('info', {
							icon : false,
							title : '提示',
							msg : '您有新的预约信息'
						});
					}else{
						return ;
					}
				}
			})
		},
		showAllNotice : function(){
			var obj = this;
				$.ajax({
					url:"/Notice/User/loadAllNotice",
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"currentPage":"1"}),
					success : function(data){
						var count = data.count;
						
						var str = "<li><a href='#' class='perPage lnPage'>上一页</a></li>"					
						str += "<li><a href='#' class='nextPage lnPage'>下一页</a></li>";
						
						$(".pageUl").append(str);	
						
						var nostr = "<li class='lihead'>显示所有公告</li>";
						data.noticeList.forEach(function(value,item){
							 
							 nostr += "<li><dl class='oneBUlletin'><dt>"+value.typeName+"</dt><dd><a href='#' class='allNotices' value='"+value.typeName+ "' >"+value.description+"</a></dd></dl></li>"
						
						});
						$("#count").attr("value",data.count);
						$(".showBulletinList").html(nostr);
						obj.pageClick();
						obj.noticeClick();
					}
				});
			
		},
		pageClick : function(){
			var obj = this;
			$(".curPage").click(function(){
				var currentPage = $(this).text();
				$("#currentPage").attr("value",currentPage);
				$.ajax({
					url:"/Notice/User/loadAllNotice",
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"currentPage":currentPage}),
					success : function(data){
						var nostr = "<li class='lihead'>显示所有公告</li>";
						data.noticeList.forEach(function(value,item){
							
							 nostr += "<li><dl class='oneBUlletin'><dt>"+value.typeName+"</dt><dd><a href='#' class='allNotices' value='"+value.typeName+ "' >"+value.description+"</a></dd></dl></li>"
						
						});
						
						$(".showBulletinList").html(nostr);	
						 obj.noticeClick();
					}
				});
			});
			
			$(".lnPage").click(function(){
				var lnPage = $(this).text();	
				var currentPage = $("#currentPage").val();
				if(lnPage == "上一页"){
					if(currentPage == 1){
						currentPage = currentPage;
						$("#currentPage").attr("value",currentPage);
					}else{
						currentPage = currentPage - 1;
						$("#currentPage").attr("value",currentPage);	
					}
					 
				}else if(lnPage == "下一页"){
					if(currentPage >= $("#count").val()/7 ){
						currentPage = currentPage;
						$("#currentPage").attr("value",currentPage);
					}else{
						currentPage  = currentPage - 1 +2 ;
						$("#currentPage").attr("value",currentPage);
					}
					
					
				}
				$.ajax({
					url:"/Notice/User/loadAllNotice",
					type:"post",
					contentType:"application/json",
					data:JSON.stringify({"currentPage":currentPage}),
					success : function(data){
						var nostr = "<li class='lihead'>显示所有公告</li>";
						data.noticeList.forEach(function(value,item){
							
							 nostr += "<li><dl class='oneBUlletin'><dt>"+value.typeName+"</dt><dd><ahref='#' class='allNotices' value='"+value.typeName+ "'>"+value.description+"</a></dd></dl></li>"
						
						});
						$(".showBulletinList").html(nostr);
						obj.noticeClick();
					}
				});
			});
			
			
		},
		clickNavbar : function(){
			$("#navbarIcon").click(function(){
				var left_navbar = $("#left_content");
				if(left_navbar.css("display")=="none"){
					left_navbar.css("display","block");
				}else{
					left_navbar.css("display","none");
				}
			})	
		},
		noticeClick : function(){
			$(".notices").click(function(){
				var noticeTitle = $(this).text();
				var noticeDescription = $(this).attr("value");
				$("#display_bulletin").css("display","none");
				$("#showAllBulletin").css("display","block");
				$(".pageUl").css("display","none");
				var str = "<h1 style='font-size:2rem;text-align:center; width:80%;position:absolute;left:10%;top:2%;'>"+noticeTitle+"</h1>"
						 +"<p style='font-size:1rem;text-indent:2em; line-height:150%; width:80%;position:absolute;left:10%;top:10%;'>"+noticeDescription+"</p>";
				$(".showBulletinList").html(str);
				
			});
			
			$(".allNotices").click(function(){
				var noticeTitle = $(this).attr("value");
				var noticeDescription = $(this).text();
				$("#display_bulletin").css("display","none");
				$("#showAllBulletin").css("display","block");
				$(".pageUl").css("display","none");
				var str = "<h1 style='font-size:2rem;text-align:center; width:80%;position:absolute;left:10%;top:2%;'>"+noticeTitle+"</h1>"
						 +"<p style='font-size:1rem;text-indent:2em; line-height:150%; width:80%;position:absolute;left:10%;top:10%;'>"+noticeDescription+"</p>";
				$(".showBulletinList").html(str);
				
			});
			
			
		},
		checkTime :function(){
			var lastTime = new Date().getTime();
	        var currentTime = new Date().getTime();
	        var timeOut = 60 * 1000 * 10; //设置超时时间： 10分
	        $(function(){
	            /* 鼠标移动事件 */
	            $(document).mouseover(function(){
	                lastTime = new Date().getTime(); //更新操作时间

	            });
	        });
	        
	        /* 定时器  间隔1秒检测是否长时间未操作页面  */
	        var quitTime = window.setInterval(testTime, 1000);

	        function testTime(){
	            currentTime = new Date().getTime(); //更新当前时间
	            if(currentTime - lastTime > timeOut){ //判断是否超时
	                alert("登陆 超时,即将返回登陆界面");
	                window.clearInterval(quitTime);
	                setTimeout(function(){
						window.location.href ="/";
					},2000);
	            }
	        }

	        
		},
		//点击系统名字返回公告界面
		
		backNotice : function(){
			var obj = this;
			$(".noticeBtn").click(function(){
				obj.initToastr();
			    obj.loadAnnouncementPage();
			    obj.loadUserIndexData();
			    obj.userLoginOut();
			     
			    obj.clickNavbar();
			    obj.showAllNotice();
			    obj.backNotice();
			})
		}
			
		

};

window.onload = function(){
	var ac = new Manager.Announcement();
	ac.init();	
};
$.ajaxSetup({complete:function(xhr,status){
	//若HEADER中含有REDIRECT说明后端想重定向
	if("REDIRECT" == xhr.getResponseHeader("REDIRECT")){ 
        //将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
        	window.location.href = "/";
        }
}});
 
	
 