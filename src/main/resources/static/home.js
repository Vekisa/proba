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
		error : function(xhr, status, error) {
			$("#error").html(JSON.parse(xhr.responseText).message);
		    $('#myModal').modal("show");
		}
	});
});
