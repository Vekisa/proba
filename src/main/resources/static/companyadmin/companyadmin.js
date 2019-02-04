var authority;
var adminId;
var companyId;

function load(){
	$("#companyProfile").hide();
	$("#collection").hide();
	$("#profile").show();
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
				document.getElementById("user").innerHTML = data.username;
				authority = data.authorities[0].authority;
				if(authority == "HOTEL_ADMIN") {
					document.getElementById("company").innerHTML = "Hotel";
					$(document).attr("title", "Hotel admin page");
				}
				if(authority == "AIRLINE_ADMIN") {
				    document.getElementById("company").innerHTML = "Airline";
				    $(document).attr("title", "Airline admin page");
				}
				if(authority == "RENT_A_CAR_ADMIN") {
				    document.getElementById("company").innerHTML = "Rent-a-Car";
				    $(document).attr("title", "Rent-a-Car admin page");
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

$(document).on('click','#updateCompany',function(e){
	loadCompanyForm();
})

$(document).on('click','#updateProfile',function(e){
	loadProfileForm();
})

$(document).on('click','#user',function(e){
	load();
})

$(document).on('submit', '#companyForm', function(e) {
    e.preventDefault();
	let name = $("#name").val();
	let address = $("#address").val();
	let description = $("#description").val();
	let locationId = $("#location option:selected").val();
	let company = new Object();
	company.name = name;
	company.address = address;
	company.description = description;
	let jsonBody = JSON.stringify(company);
	let url;
	if(authority == "HOTEL_ADMIN") {
		url = "/hotels/" + companyId;
	}
	if(authority == "AIRLINE_ADMIN") {
		url = "/airlines/" + companyId;
	}
	if(authority == "RENT_A_CAR_ADMIN") {
		url = "/rent-a-cars/" + companyId;
	}
	if(locationId != undefined)
		$.ajax({
			type: "PUT",
			url: url + "/location?destination=" + locationId,
			dataType: "json",
			data: jsonBody,
			contentType: "application/json",
			async: false,
			beforeSend: function(request) {
	    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
	  		}
		});
	$.ajax({
		type: "PUT",
		url: url,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null)
  				loadCollection();
  		}
	});
})

$(document).on('submit', '#profileForm', function(e) {
    e.preventDefault();
    let newPassword1 = $("#newpassword1InForm").val();
    let newPassword2 = $("#newpassword2InForm").val();
    if(newPassword1 == newPassword2) {
	    let oldPassword = $("#oldpasswordInForm").val();
	    let password;
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
					password = data.password;
				}
			}
		});
		password = oldPassword;
		if(oldPassword == password) {
			let user = new Object();
			let username = $("#usernameInForm").val();
			let email = $("#emailInForm").val();
			let firstName = $("#firstNameInForm").val();
			let lastName = $("#lastNameInForm").val();
			let city = $("#cityInForm").val();
			let phoneNumber = $("#phoneNumberInForm").val();
			user.username = username;
			user.email = email;
			user.firstName = firstName;
			user.lastName = lastName;
			user.city = city;
			user.phoneNumber = phoneNumber;
			user.password = newPassword1;
			let jsonBody = JSON.stringify(user);
			$.ajax({
				type: "PUT",
				url: "/users/companyAdmins/" + adminId,
				dataType: "json",
				data: jsonBody,
				contentType: "application/json",
				async: false,
				beforeSend: function(request) {
		    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		  		},
		  		success: function(data) {
		  			if(data != null)
		  				loadCollection();
		  		}
			});		
		} else {
			$("#error").html("Stara sifra je neispravna!");
			$('#myModal').modal("show");		
		}
	} else {
		$("#error").html("Sifre se ne poklapaju!");
		$('#myModal').modal("show");
	}
})

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
		url1 = "/rent-a-cars/" + companyId;
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
				$("#company_description").append("<strong>Description:</strong> " + data.description);
				$("#company_address").empty();
				$("#company_address").append("<strong>Address:</strong> " + data.address);
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
			} else {
				$("#collection").hide();
			}
		}		
	});	
}

function loadCompanyForm() {
	let url1 = "";
	if(authority == "HOTEL_ADMIN") {
		url1 = "/hotels/" + companyId;
	}
	if(authority == "AIRLINE_ADMIN") {
		url1 = "/airlines/" + companyId;
	}
	if(authority == "RENT_A_CAR_ADMIN") {
		url1 = "/rent-a-cars/" + companyId;
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
				printCompanyForm(data, url1);
			}
		}
	});
}

function loadProfileForm() {
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
				printProfileForm(data);
			}
		}
	});	
}

function printProfileForm(user) {
	$("#collection").show();
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"profileForm\" method=\"POST\"></form><br>";
	let usernameHTML = "Username: <input id=\"usernameInForm\" type=\"text\"><br><br>";
	let oldPasswordHTML = "Old password: <input id=\"oldpasswordInForm\" type=\"password\"><br><br>";
	let newPasswordHTML1 = "New password: <input id=\"newpassword1InForm\" type=\"password\"><br><br>";
	let newPasswordHTML2 = "Repeat new password: <input id=\"newpassword2InForm\" type=\"password\"><br><br>";
	let emailHTML = "E-Mail: <input id=\"emailInForm\" type=\"text\"><br><br>";
	let firstNameHTML = "First name: <input id=\"firstNameInForm\" type=\"text\"><br><br>";
    let lastNameHTML = "Last name: <input id=\"lastNameInForm\" type=\"text\"><br><br>";
    let cityHTML = "City: <input id=\"cityInForm\" type=\"text\"><br><br>";
    let phoneNumberHTML = "Phone number: <input id=\"phoneNumberInForm\" type=\"text\"><br><br>";
    let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Update\">";
	$("#profileForm").append(usernameHTML);
	$("#profileForm").append(oldPasswordHTML);
	$("#profileForm").append(newPasswordHTML1);
	$("#profileForm").append(newPasswordHTML2);
	$("#profileForm").append(emailHTML);
	$("#profileForm").append(firstNameHTML);
	$("#profileForm").append(lastNameHTML);
	$("#profileForm").append(cityHTML);
	$("#profileForm").append(phoneNumberHTML);
	$("#profileForm").append(submitHTML);
	$("#usernameInForm").val(user.username);
	$("#emailInForm").val(user.email);
	$("#firstNameInForm").val(user.firstName);
	$("#lastNameInForm").val(user.lastName);
	$("#cityInForm").val(user.city);
	$("#phoneNumberInForm").val(user.phoneNumber);
}

function printCompanyForm(data, url) {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"companyForm\" method=\"POST\"></form><br>";
	let nameHTML = "Name: <input id=\"name\" type=\"text\"><br><br>";
	let addressHTML = "Address: <input id=\"address\" type=\"text\"><br><br>";
	let descriptionHTML = "Description: <input id=\"description\" type=\"text\"><br><br>";
	let locationHTML = "Location: <select id=\"location\">";
	$.ajax({
		type: "GET",
		url: "/destinations/all",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, location) {
					locationHTML += "<option value=\"" + location.id + "\">" + location.name + "</option>";
				});				
  			}
  		}
	});
	locationHTML += "</select><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Update\">";
	$("#companyForm").append(nameHTML);
	$("#companyForm").append(addressHTML);
	$("#companyForm").append(descriptionHTML);
	$("#name").val(data.name);
	$("#address").val(data.address);
	$("#description").val(data.description);
	if(authority != "RENT_A_CAR_ADMIN") {
		$("#companyForm").append(locationHTML);
		$.ajax({
			type: "GET",
			url: url + "/location",
			beforeSend: function(request) {
				request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
			},
			success: function(location) {
				$("#location").val(location.id).prop("selected", true);
			}		
		});
	}
	$("#companyForm").append(submitHTML);
}

$(document).on('click', '#logout', function(e) {
	$.ajax({
		type: "POST",
		url: "/users/registered/logout",
		beforeSend: function(request) {
			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		},
		success: function() {
			localStorage.removeItem("token");
			window.location.href = "index.html";
		}
	});
});