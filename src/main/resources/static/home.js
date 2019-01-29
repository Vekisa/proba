$(document).on('click','#logout',function(e){
	e.preventDefault();
	$.ajax({
		url: "/users/registered/logout",
		type:"POST",
		async: false,
		contentType:"application/json",
		dataType:"json",
		complete: function(data){
			localStorage.token = null;
			window.location.href = 'index.html';		
		},
		fail : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("Error");
		}
	});
});
