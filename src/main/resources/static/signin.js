$(document).on('submit','.form-signin',function(e){
	//alert("Saljem serveru");
	//alert($('#inputUsername').val() + " " + $('#inputPassword').val());
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
				sessionStorage.setItem("userToken",data);
				localStorage.token = data.token;
				alert(data.token);
				//alert("Success");
				window.location.href = 'home.html';		
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("Error");
		}
	});
});

function formToJSON() {
	return JSON.stringify({
		"username":$('#inputUsername').val(),
		"password":$('#inputPassword').val()
	});
}