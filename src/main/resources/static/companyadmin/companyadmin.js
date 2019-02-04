var authority;
var adminId;
var companyId;

function load(){
	$("#companyProfile").hide();
	$("#collection").hide();
	$.ajax({
		type: "GET",
		url: "/users/registered/currentUser",
		dataType: "json",
		async: false,
	  	beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
		success: function(data) {
			if(data != null) {
				$("#first_name").empty();
				$("#first_name").append("<strong>First name:</strong> " + data.firstName);
				$("#last_name").empty();
				$("#last_name").append("<strong>Last name:</strong> " + data.lastName);
				$("#email").empty();
				$("#email").append("<strong>E-mail:</strong> " + data.email);
				$("#city").empty();
				$("#city").append("<strong>City:</strong> " + data.city);
				$("#phone").empty();
				$("#phone").append("<strong>Phone number:</strong> " + data.phoneNumber);
				$("#username").text("Profile of user: " + data.username);
				authority = data.authorities[0].authority;
				if(authority == "HOTEL_ADMIN") {
					document.getElementById("company").innerHTML = "Hotel";
				}
				if(authority == "AIRLINE_ADMIN") {
				    document.getElementById("company").innerHTML = "Airline";
				}
				if(authority == "RENT_A_CAR_ADMIN") {
				    document.getElementById("company").innerHTML = "Rent-a-Car";
				}				
				adminId = data.id;
			}
		},
		error: function() {
			alert("Greska prilikom pribavljanja korisnika");
		}
	});
	$.ajax({
		type: "GET",
		url: "/users/companyAdmins/" + adminId,
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
  				if(authority == "HOTEL_ADMIN")
  					companyId = data.hotel.id;
  				if(authority == "AIRLINE_ADMIN")
  					companyId = data.airline.id;
  				if(authority == "RENT_A_CAR_ADMIN")
  					companyId = data.rentACar.id;
  			}
  		}
	});
}

$(document).on('click','#company',function(e){
	loadCollection();
});

function loadCollection() {
	let url1 = "";
	let url2 = "";
	if(authority == "HOTEL_ADMIN") {
		$("#company").val("Hotel");
		url1 = "/hotels/" + companyId;
		url2 = url1 + "/rooms";
	}
	if(authority == "AIRLINE_ADMIN") {
	    $("#company").val("Airline");
		url1 = "/airlines/" + companyId;
		url2 = url1 + "/flights";
	}
	if(authority == "RENT_A_CAR_ADMIN") {
	    $("#company").val("Rent-a-car");
		url1 = "/rent-a-cars" + companyId;
		url2 = url1 + "/vehicles";
	}
	$.ajax({
		type: "GET",
		url: url1,
		dataType: "json",
		async: false,
		beforeSend: function(request) {
			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		},
		success: function(data) {
			if(data != null) {
				$("#company_name").empty();
				$("#company_name").append("<strong>" + data.name + "</strong> ");
				$("#company_description").empty();
				$("#company_description").append("<strong>Description:</strong> " + data.address);
				$("#company_address").empty();
				$("#company_address").append("<strong>Address:</strong> " + data.description);
				$("#companyProfile").show();
				addButtons(authority);
				$("#profile").hide();
			}
		}
	});
	$.ajax({
		type: "GET",
		url: url2,
		async: false,
		beforeSend: function(request) {
			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		},
		success: function(data) {
			if(data != null) {
				printCollection(data, authority);
			}
		}		
	});	
}

$(document).on('click', '#logout', function(e) {
	$.ajax({
		type: "POST",
		url: "/users/registered/logout",
		beforeSend: function(request) {
			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		},
		success: function() {
			window.location.href = "index.html";
		}
	});
});