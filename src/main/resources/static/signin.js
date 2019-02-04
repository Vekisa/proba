function load(){
}

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
			if(data!=null) {
				localStorage.token = data.token;
				$.ajax({
				type: "GET",
				url: "/users/registered/currentUser",
				async: false,			
				beforeSend: function(request) {
	    			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
	  			},
	  			success: function(data) {
	  				if(data.authorities[0].authority == "HOTEL_ADMIN" ||
	  				   data.authorities[0].authority == "AIRLINE_ADMIN" ||
	  				   data.authorities[0].authority == "RENT_A_CAR_ADMIN")
	  				   window.location.href = "companyadmin.html";
	  				else
	  			       window.location.href = "home.html";
	  			}
				});	
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