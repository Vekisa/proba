function load(){
	$('#loading').hide();
}

$(document).on('submit','.form-signup',function(e){
	e.preventDefault();
	$('#loading').show();
	$.ajax({
		url: "/users/registered",
		type:"POST",
		data: formToJSON(),
		contentType:"application/json",
		dataType:"json",
		success:function(data){
			if(data!=null){
				window.location.href = 'signin.html';		
			}
		},
			
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