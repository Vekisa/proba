$(document).on('submit','.form-signup',function(e){
	alert("Saljem serveru");
	e.preventDefault();
	$.ajax({
		url: "/users/registered",
		type:"POST",
		async: false,
		data: formToJSON(),
		contentType:"application/json",
		dataType:"json",
		success:function(data){
			if(data!=null){
				//sessionStorage.setItem("user",JSON.stringify(data));
				alert("Success");
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
		"firstName":$('#inputFirstName').val(),
		"email":$('#inputEmail').val(),
		"lastName":$('#inputLastName').val(),
		"password":$('#inputPassword').val(),
		"city":$('#inputCity').val(),
		"phoneNumber":$('#inputPhoneNumber').val()
		
	});
}