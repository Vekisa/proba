$(document).on('submit','.form-signin',function(e){
	e.preventDefault();
	$.ajax({
		url: "/users/registered/auth",
		type:"POST",
		async: false,
		data: formToJSON(),
		contentType:"application/json",
		dataType:"json",
		success:function(data){
			if(data!=null){
				localStorage.token = data.token;
				alert(data.token);
				window.location.href = 'home.html';		
			}
		},
		error : function(xhr, status, error) {
			alert(JSON.parse(xhr.responseText));
			$("#error").html(JSON.parse(xhr.responseText).message);
		    $('#myModal').modal("show");
		}
	});
});

function formToJSON() {
	return JSON.stringify({
		"username":$('#inputUsername').val(),
		"password":$('#inputPassword').val()
	});
}