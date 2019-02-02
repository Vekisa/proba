function load(){
	$.ajax({
		type: "GET",
		url: "users/registered/currentUser",
		beforeSend: function(xhr) {
	          if (localStorage.token) {
	            xhr.setRequestHeader('X-Auth-Token', localStorage.token);
	            
	          }
	    },
		success: function(data){
			if(data!=null){
				$('#user').val(data.username);
			}
		}
	});
	
	$('#profile').hide();
}

$(document).on('click','#user',function(e){
	e.preventDefault();
	$("#").hide();
	$("#profile").show();
	$("#tablediv").hide();
	
});

$(document).ajaxError(function( event, jqxhr, settings, thrownError ) {
	  
    switch(jqxhr.status) {
	    case 404:
	    	 $("#error").html("Not found");
	      break;
	    case 204:
	    	$("#error").html("Not found");
	      break;
	    case 400:
	    	$("#error").html("Bad request");
	      break;
	    default:
	    	$("#error").html("Something went wrong");
    }
    $('#myModal').modal("show");
});

$(document).on('click','#logout',function(e){
	e.preventDefault();
	$.ajax({
		url: "/users/registered/logout",
		type:"POST",
		async: false,
		success: function(data){
			localStorage.token = null;
			window.location.href = 'index.html';		
		},
		error : function(xhr, status, error) {
			$("#error").html(JSON.parse(xhr.responseText).message);
		    $('#myModal').modal("show");
		}
	});
});
