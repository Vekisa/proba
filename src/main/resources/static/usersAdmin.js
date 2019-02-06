var currentData;
var selected_search_item;
var addAdminPressed = false;
var currentCompany;
var companyId;

$(document).ajaxError(function( event, jqxhr, settings, thrownError ) {
  
    switch(jqxhr.status) {
	    case 404:
	    	 $("#error").html("Not found");
	      break;
	    case 204:
	    	$("#error").html("No content");
	      break;
	    case 400:
	    	$("#error").html("Bad request");
	      break;
	    default:
	    	$("#error").html("Something went wrong");
    }
    $('#myModal').modal("show");
});


$(document).on('click','#airline',function(e){
	e.preventDefault();
	currentCompany = "AIRLINE_ADMIN";
	$("#profile").hide();
	$.ajax({
		type: "GET",
		url: "airlines?page=0&size=500",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#airbtn',function(e){
	e.preventDefault();
	$("#profile").hide();
	$.ajax({
		type: "GET",
		url: "airlines?page=0&size=500",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#rentacar',function(e){
	e.preventDefault();
	currentCompany = "RENT_A_CAR_ADMIN";
	$("#profile").hide();
	$.ajax({
		type: "GET",
		url: "rent-a-cars?page=0&size=500",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#rentbtn',function(e){
	e.preventDefault();
	$.ajax({
		type: "GET",
		url: "rent-a-cars?page=0&size=500",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#hotel',function(e){
	e.preventDefault();
	currentCompany = "HOTEL_ADMIN";
	$.ajax({
		type: "GET",
		url: "hotels?page=0&size=500",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#hotbtn',function(e){
	e.preventDefault();
	$.ajax({
		type: "GET",
		url: "hotels?page=0&size=500",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#bos',function(e){
		e.preventDefault();
		printCompany(currentData,"rentacar");
		printBranchOffices(currentData);
});

function printListOfCompanies(data){
	var s = "";
	$.each(data, function(index, company) {
		s += "<tr id = \"" + company.links[0].href + "\">" + "<th scope=\"row\">"+ index +"</th>" + "<td>" + company.name + "</td>" + "<td>"+ company.address
		+ "</td>" + "<td>" + company.description + "</td><td><button class=\"addAdminBtn\" id=\"" + company.id + "\" style=\"font-size: 14px;\" class=\"btn btn-lg btn-primary\">add admin</button></td></tr>";
	});
	document.getElementById("tablediv").innerHTML = "<button class=\"btn btn-lg btn-primary btn-block\" type=\"button\" id=\"addBtn\">Add new company</button>" +
		"<table class=\"table table-hover\" id = \"table\"><thead>" +
	    		"<tr>" + 
	      		  "<th scope=\"col\">#</th>" +
			      "<th scope=\"col\">Name</th>" +
			      "<th scope=\"col\">Address</th>" +
			      "<th scope=\"col\">Description</th>" +
	    		"</tr>" +
	  		"</thead><tbody>" + s + "</tbody></table>";
	$('#tablediv').show();
    $("#profile").hide();
}

$(document).on('click', '.addAdminBtn', function(e){
	e.preventDefault();
	addAdminPressed = true;
	$('#addAdminModal').modal('show');
	companyId = $(this).attr('id');
	loadAdminModal();
})

function loadAdminModal(){
	var allUsers = [];
    
	$.ajax({
		url: "/users/companyAdmins",
		type:"GET",
		async: false,
		success: function(data){
			for(let d of data){
				if(d.authority == currentCompany){
					if(d.airline == null && d.hotel == null && d.rentacar == null){
						allUsers.push(d);
					}else if(d.airline != null){
						if(d.airline.id != companyId){
							allUsers.push(d);
						}
					}else if(d.hotel != null){
						if(d.hotel.id != companyId){
							allUsers.push(d);
						}
					}else if(d.rentacar != null){
						if(d.rentacar.id != companyId){
							allUsers.push(d);
						}
					}
				}
			}
		}
	});
	printUsersModal(allUsers);
}

function printUsers(allUsers){
    var users = "";
    
	if(allUsers!=null){
       
		$.each(allUsers, function(index, user) {
			users += "<tr>" + "<td scope=\"col\">" + user.username + "</td>" + "<td scope=\"col\">"+ user.firstName + "</td>" + "<td scope=\"col\">" + user.lastName + "</td>" +
			"<td scope=\"col\">" + user.email + "</td>" + "<td scope=\"col\">" + user.city + "</td>"+ "</tr>";
		});
	}
	
	document.getElementById("tablediv").innerHTML = "<button class=\"btn btn-lg btn-primary btn-block\" type=\"button\" id=\"addUserBtn\">Add new admin</button>" +
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Username</th>" +
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Email</th>" +
        "<th scope=\"col\">City</th>" +
		"</tr>" +
		"</thead><tbody>" + users + "</tbody></table>";
}

$(document).on('click', '#addUserBtn', function(e){
	e.preventDefault();
	$('#addNewAdminModal').modal('show');
	$('#tablediv').show();
})

$(document).on('click','#addBtn',function(e){
	e.preventDefault();
	printCompanyForm(currentCompany);
	$('#collectionModal').modal('show');
})

function printCompanyForm(currComp) {
	document.getElementById("collection1").innerHTML = "";
	document.getElementById("collection1").innerHTML = "<br><form id=\"companyForm\" method=\"POST\"></form><br>";
	let nameHTML = "<tr><td>Name:</td><td><input class=\"form-control\" id=\"name1\" type=\"text\"></td></tr>";
	let addressHTML = "<tr><td>Address: </td><td><input class=\"form-control\" id=\"address1\" type=\"text\"></td></tr>";
	let descriptionHTML = "<tr><td>Description: </td><td><input class=\"form-control\" id=\"description1\" type=\"text\"></td></tr>";
	let locationHTML = "<tr><td>Location: </td><td><select class=\"form-control\" id=\"location1\">";
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
	locationHTML += "</select></td></tr>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$('#companyForm').append("<table></table>");
	$("#companyForm").append(nameHTML);
	$("#companyForm").append(addressHTML);
	$("#companyForm").append(descriptionHTML);
	if(currentCompany != "RENT_A_CAR_ADMIN"){
		$('#companyForm').append(locationHTML);
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

function printUsersModal(allUsers){
    var users = "";
    
	if(allUsers!=null){
       
		$.each(allUsers, function(index, user) {
			users += "<tr>" + "<td scope=\"col\">" + user.username + "</td>" + "<td scope=\"col\">"+ user.firstName + "</td>" + "<td scope=\"col\">" + user.lastName + "</td>" +
			"<td scope=\"col\">" + user.email + "</td>" + "<td scope=\"col\">" + user.city + "</td>"+ "<td><button id=\"" + user.id + "\" class=\"addCompanyAdmin\" style=\"font-size: 14px;\" class=\"btn btn-lg btn-primary\">add</button></td>" + "</tr>";
		});
	}
	
	document.getElementById("adminsModalBody").innerHTML = 
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Username</th>" +
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Email</th>" +
        "<th scope=\"col\">City</th>" +
		"</tr>" +
		"</thead><tbody>" + users + "</tbody></table>";
}

$(document).on('click', '.addCompanyAdmin', function(e){
	e.preventDefault();
	$.ajax({
		url: "/users/companyAdmins/" + $(this).attr('id') + "/company?company=" + companyId,
		type: "PUT",
		async: false,
		success: function(){
			alert('Admin je dodat');
		}
	})
})

$(document).on('click','#admins',function(e){
	e.preventDefault();
    
    $('#tablediv').show();
    $("#profile").hide();
    
	var allUsers;
    
	$.ajax({
		url: "/users/companyAdmins",
		type:"GET",
		async: false,
		success: function(data){
            allUsers = data;
		}
	});
	
	$.ajax({
		url: "/users/usersAdmins",
		type:"GET",
		async: false,
		success: function(data){
            for(let d of data){
            	allUsers.push(d);
            }
		}
	});
	
	printUsers(allUsers);
	
});

$(document).on('click','#destbtn',function(e){
	e.preventDefault();
	printCompany(currentData,"airline");
	printDestinations(currentData);
});

$(document).on('click','#flightsbtn',function(e){
	e.preventDefault();
	printCompany(currentData,"airline");
	printFlights(currentData);
});

$(document).on('click','#cataloguebtn',function(e){
	e.preventDefault();
	printCompany(currentData,"hotel");
	printHotelCatalogue(currentData);
});

$(document).on('click','#extraopt',function(e){
	e.preventDefault();
	printCompany(currentData,"hotel");
	printExtraOptions(currentData);
});

function printExtraOptions(data){
	 $.ajax({
			type: "GET",
			url:  data._links.hotel_extra_options.href,
			dataType: "json",
			async: false,
			success: function(data){
					pomDataExtraOptions = data;
			}
	 });
	 
	 var extraOptions = "";
	 if(pomDataExtraOptions != null){
		 $.each(pomDataExtraOptions, function(index, extraOption) {
				extraOptions += "<tr><td scope=\"col\">" + extraOption.description + "</td>" + "<td>" + extraOption.pricePerDay + "</td></tr>";
		});
	 }
	 
	 var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Description</th>" +
	    "<th scope=\"col\">Price per day(&euro;)</th>" +
		"</tr>" +
	"</thead><tbody>" + extraOptions + "</tbody></table>";
	 
	 document.getElementById("tablediv").innerHTML += "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"cataloguebtn\">Catalogue</button></div></div>" +
	 "<hr>" + 
		"<strong>Extra Options</strong>" +
		"<hr>" + tabela;
}

function printDestinations(data){
	var pomDataDestinations;
	 $.ajax({
			type: "GET",
			url:  data._links.destinations.href,
			dataType: "json",
			async: false,
			success: function(data){
					pomDataDestinations = data;
			}
	});
	 
	 let destinations = "";
	 if(pomDataDestinations != null){
		 $.each(pomDataDestinations, function(index, destiantion) {
				destinations += "<tr><td scope=\"col\">" + destination.name + "</td></tr>";
		});
	 }
	 
	 var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Name</th>" +
	    "<th scope=\"col\">Brand</th>" +
	    "<th scope=\"col\">Model</th>" +
	    "<th scope=\"col\">Seats number</th>" +
	    "<th scope=\"col\">Type</th>" +
		"</tr>" +
	"</thead><tbody>" + destinations + "</tbody></table>";
	 
	 document.getElementById("tablediv").innerHTML += "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"flightsbtn\">Flights</button></div></div>" +
	 "<hr>" + 
		"<strong>Destinations</strong>" +
		"<hr>" + tabela;
	 
	 
}

$(document).on('click','#tablebo tr', function() {
	var branchOfficeLink = $(this).attr('id');
	var pomData;
	var pomDataVehicles;
	$.ajax({
		type: "GET",
		url: branchOfficeLink,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomData = data;
			}
		}
	});
	
	$.ajax({
		type: "GET",
		url: pomData._links.vehicles.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomDataVehicles = data;
			}
		}
	});
	
	var vehicles = "";
	
	$.each(pomDataVehicles, function(index, vehicle) {
		vehicles += "<td scope=\"col\">"+ vehicle.brand 
		+ "<td scope=\"col\">" + vehicle.model + "</td>"
		+ "<td scope=\"col\">"+ vehicle.seatsNumber + "</td>" 
		+ "<td scope=\"col\">"+ vehicle.type + "</td>"
		+ "</td></tr>";
	});
	
	var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
				"<tr>" + 
			    "<th scope=\"col\">Brand</th>" +
			    "<th scope=\"col\">Model</th>" +
			    "<th scope=\"col\">Seats number</th>" +
			    "<th scope=\"col\">Type</th>" +
			    "<th scope=\"col\"></th>" +
				"</tr>" +
			"</thead><tbody>" + vehicles + "</tbody></table>";
	
	printCompany(currentData,"rentacar");
	document.getElementById("tablediv").innerHTML +=  "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"bos\">Branch offices</button></div></div>" + 
		"<hr>" + "<strong>Vehicles of branch office on address \"" + pomData.address + "\"</strong>" + "<hr>" + tabela;
});

$(document).on('click','#table tr', function() {
	var companyLink = $(this).attr('id');
	let url2 = "";
	let authority;
	if(companyLink.includes("airlines")){
		url2 = companyLink + "/flights";
		authority = "AIRLINE_ADMIN";
	}else if(companyLink.includes("rent-a-cars")){
		url2 = companyLink + "/vehicles";
		authority = "RETN_A_CAR_ADMIN";
	}else if(companyLink.includes("hotels")){
		url2 = companyLink + "/rooms";
		authority = "HOTEL_ADMIN";
	}
if(addAdminPressed == false){
	$('#tablediv').hide();
	$.ajax({
		type: "GET",
		url: companyLink,
		dataType: "json",
		async: false,
		beforeSend: function(request) {
			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		},
		success: function(data){
			if(data!=null){
				currentData = data;
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
addAdminPressed = false;
});

function printCompany(data, company){
	let pomData;
	
	if(company!="rentacar"){
		$.ajax({
			type: "GET",
			url: data._links.location.href,
			dataType: "json",
			async: false,
			success: function(data){
				if(data!=null){
					pomData = data;
				}
			}
		});
	}
	
	let header = "";	
	let image = "";
	let buttonTitle = "";
	let buttonTitle2= "";
	let buttonId = "";
	
	if(company == "airline"){
		image = "air-plane-cartoon.png";
		header = "Flights";
		buttonTitle = "Airlines";
		buttonTitle2 = "Destinations";
		buttonId = "airbtn";
		buttonId2 = "destbtn";
	}else if(company == "rentacar"){
		image = "rent-a-car-cartoon.png";
		header = "Branch offices";
		buttonTitle = "Rent a cars"
		buttonTitle2 = "Nesto";
		buttonId = "rentbtn";
		buttonId2 = "nesto";
	}else if(company == "hotel"){
		image = "hotel-cartoon.png";
		header = "Catalogue";
		buttonTitle = "Hotels"
		buttonTitle2 = "Nesto";
		buttonId = "hotbtn";
		buttonId2 = "nesto";
	}
	
	let code = "<div class=\"container\">" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<img height=\"170\" width =\"200\" src=\"" + image +"\" class = \"profimg\" alt=\"airplane\">" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<strong style=\"font-size:40px;\">" + data.name +"</strong>" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<p>" + data.address +"</p>" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<p>" + data.description +"</p>" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star\"></span>" + 
						"</div>" +
					"</div>" +
					"<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"" + buttonId + "\">" + buttonTitle + "</button></div></div>";
	
	
						
	document.getElementById("tablediv").innerHTML = code ;
}

function printHotelCatalogue(data){
	var pomData;
	let link = "";
	$.ajax({
		type: "GET",
		url: data._links.hotelcatalogue.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				link = data._links.catalogue_room_types.href;
			}
		}
	});
	
	$.ajax({
		type: "GET",
		url: link,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomData = data;
			}
		}
	});
	
	let catalogue = "";
	$.each(pomData, function(index, roomType) {
		catalogue += "<tr><td scope=\"col\">" + roomType.name + "</td>" + "<td scope=\"col\">"+ roomType.description 
		+ "</td>" + "<td scope=\"col\">" + roomType.pricePerNight + "</td></tr>";
	});
	
	document.getElementById("tablediv").innerHTML += "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"extraopt\">Extra Options</button></div></div>" + "<div>" + 
	"<hr>" + 
	"<strong>Catalogue</strong>" +
	"<hr>" + "<table class=\"table table-hover\"><thead><tr><th scope=\"col\">Type</th><th scope=\"col\">Description</th><th scope=\"col\">Price per night(&euro;)</th></tr></thead><tbody>" + catalogue + "</tbody></table>" ;
}

function printFlights(data){
	var pomDataFlights;
	$.ajax({
		type: "GET",
		url: data._links.airline_flights.href,
		dataType: "json",
		async: false,
		success: function(data){			
				pomDataFlights = data;
		}
	});
	
	let flights = "";
	let s = "";
	if(pomDataFlights!=null){
		$.each(pomDataFlights, function(index, flight) {
			s += "<tr>" + "<td>" + flight.startDestination + "</td>" + "<td>"+ flight.finishDestination 
			+ "</td>" + "<td>" + flight.transfers + "</td><td>" + flight.departureTime + "</td><td>" + flight.arrivalTime + "</td><td>"
			+ flight.flightLength + "</td><td>" + flight.basePrice + "</td>" + "</tr>";
		});
	}
	
	flights ="<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"destbtn\">Destinations</button></div></div>" + "<div>" + 
	"<hr>" + 
	"<strong>Flights</strong>" +
	"<hr>" +  "<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Start Destination</th>" +
		"<th scope=\"col\">Finish Destination</th>" +
		"<th scope=\"col\">Transfers</th>" +
		"<th scope=\"col\">Departure Time</th>" +
		"<th scope=\"col\">Arrival Time</th>" +
		"<th scope=\"col\">Flight Length</th>" +
		"<th scope=\"col\">Base Price</th>" +
		"</tr>" +
		"</thead><tbody>" + s + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML += flights;
}

function printFlights1(data){
	
	let flights = "";
	let s = "";
	if(data!=null){
		$.each(data, function(index, flight) {
			s += "<tr>" + "<td>" + flight.startDestination + "</td>" + "<td>"+ flight.finishDestination 
			+ "</td>" + "<td>" + flight.transfers + "</td><td>" + flight.departureTime + "</td><td>" + flight.arrivalTime + "</td><td>"
			+ flight.flightLength + "</td><td>" + flight.basePrice + "</td>" + "</tr>";
		});
	}
	
	flights = 
	"<hr>" + 
	"<strong>Flights</strong>" +
	"<hr>" +  "<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Start Destination</th>" +
		"<th scope=\"col\">Finish Destination</th>" +
		"<th scope=\"col\">Transfers</th>" +
		"<th scope=\"col\">Departure Time</th>" +
		"<th scope=\"col\">Arrival Time</th>" +
		"<th scope=\"col\">Flight Length [h]</th>" +
		"<th scope=\"col\">Base Price</th>" +
		"</tr>" +
		"</thead><tbody>" + s + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML = flights;
}

function printBranchOffices(data){	
	var pomDataBranchOffices;
	$.ajax({
		type: "GET",
		url: data._links.branch_offices.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomDataBranchOffices = data;
			}
		}
	});
	
	let branchOffices = "";
	let s = "";
	$.each(pomDataBranchOffices, function(index, branchOffice) {
		s += "<tr id = \"" +branchOffice.links[0].href + "\">" + "<td>" + branchOffice.address + "</td>" + "<td>" + branchOffice.location.name + "</td>"+ "</tr>";
	});
	
	branchOffices ="</div>" + 
	"<hr>" + 
	"<strong>Branch officess</strong>" +
	"<hr>" + "<table class=\"table table-hover\" id = \"tablebo\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Address</th>" +
		"<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + s + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML += branchOffices;
	
	
}

$(document).on('click', '#rentacar-search', function(event){
	event.preventDefault();
	selected_search_item = "rentacar";
	$('#locationLabel1').text('Location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Company name');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Rent-a-car services');
	$('#airline-controls').css("display", "none");
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#hotel-search', function(event){
	event.preventDefault();
	selected_search_item = "hotel";
	$('#locationLabel1').text('Location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Company name');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Hotels');
	$('#airline-controls').css("display", "none");
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#airline-search', function(event){
	event.preventDefault();
	selected_search_item = "airline";
	$('#locationLabel1').text('Start location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Target location');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Airlines');
	$('#airline-controls').css("display", "block");
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#goSearch', function(e){
	e.preventDefault();
	let startDate = new Date($('#startDateInput').val());
	let endDate = new Date($('#endDateInput').val());
	if($('#startDateInput').val() != "" && $('#endDateInput').val() != ""){
		if(startDate < endDate){
			search_rentacars();
		}
		else{
			alert('Neispravno unet vremenski period!');
		}
	}
	search(selected_search_item);
})

function search(company){
	let parameters = [{"key": "name", "value": $('#nameInput').val()},
		{"key": "locationName", "value": $('#locationInput1').val()}]
	if($('#startDateInput').val() != ""){
		parameters[2] = {"key": "startDate", "value": new Date($('#startDateInput').val()).toISOString()};
	}
	if($('#endDateInput').val() != ""){
		parameters[3] = {"key": "endDate", "value": new Date($('#endDateInput').val()).toISOString()};
	}
	let url;
	for(i = 0; i < parameters.length; i++){
		if(parameters[i].value != "" && parameters[i].value != undefined){
			url += '&' + parameters[i].key + '=' + parameters[i].value;
		}
	}
	if(company == "rentacar"){
		$.ajax({
			method: 'GET',
			url: '/rent-a-cars/search?' + url,
			contentType: 'application/json',
			success: function(data){
				printListOfCompanies(data);
			}
		})
	}
	else if(company == "hotel"){
		$.ajax({
			method: 'GET',
			url: '/hotels/search?' + url,
			contentType: 'application/json',
			success: function(data){
				printListOfCompanies(data);
			}
		})
	}
	else if(company == "airline"){
		$.ajax({
			method: 'GET',
			url: '/flights/search?' + url,
			contentType: 'application/json',
			success: function(data){
				printFlights1(data);
			}
		})
	}
}

$(document).on('click','#user',function(e){
	load();
	$("#tablediv").hide();
})

$(document).on('click','#updateProfile',function(e){
	loadProfileForm();
})

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
	let usernameHTML = "<tr><td>Username: </td> <td><input class=\"form-control\" id=\"usernameInForm\" type=\"text\"></td></tr>";
	let oldPasswordHTML = "<tr><td>Old password: </td><td> <input class=\"form-control\" id=\"oldpasswordInForm\" type=\"password\"></td></tr>";
	let newPasswordHTML1 = "<tr><td>New password:</td><td> <input class=\"form-control\" id=\"newpassword1InForm\" type=\"password\"></td></tr>";
	let newPasswordHTML2 = "<tr><td>Repeat new password:</td><td><input class=\"form-control\" id=\"newpassword2InForm\" type=\"password\"></td></tr>";
	let emailHTML = "<tr><td>E-Mail:</td><td> <input class=\"form-control\" id=\"emailInForm\" type=\"text\"></td></tr>";
	let firstNameHTML = "<tr><td>First name:</td><td> <input class=\"form-control\" id=\"firstNameInForm\" type=\"text\"></td></tr>";
    let lastNameHTML = "<tr><td>Last name:</td><td> <input class=\"form-control\" id=\"lastNameInForm\" type=\"text\"></td></tr>";
    let cityHTML = "<tr><td>City: </td><td><input class=\"form-control\" id=\"cityInForm\" type=\"text\"></td></tr>";
    let phoneNumberHTML = "<tr><td>Phone number:</td><td> <input class=\"form-control\" id=\"phoneNumberInForm\" type=\"text\"></td></tr>";
    let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Update\">";
    $("#profileForm").append("<table></table>");
    $("#profileForm").append("<tbody></tbody>");
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

function load(){
	
	$('#loading').hide();
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
				adminId = data.id;
			}
		},
		error: function() {
			alert("Greska prilikom pribavljanja korisnika");
		}
	});
}

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
		  			alert('uspeh');
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

$(document).on('submit','.form-signup',function(e){
	e.preventDefault();
	$('#loading').show();
	let path;
	if($('#selectAuth').val() == "USERS_ADMIN"){
		path = "/usersAdmins";
	}
	else{
		path = "/companyAdmins";
	}
	$.ajax({
		url: "/users" + path,
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

$(document).on('submit','#companyForm',function(e){
	e.preventDefault();
	let path;
	let path1;
	if(currentCompany == "AIRLINE_ADMIN"){
		path = "/airlines?destination=" + $('#location1 option:selected').attr("value");
		path1 = "/airlines";
	}
	else if(currentCompany == "HOTEL_ADMIN"){
		path = "/hotels?location=" + $('#location1 option:selected').attr("value");
		path1 = "/hotels";
	}
	else{
		path = "/rent-a-cars";
		path1 = "/rent-a-cars";
	}
	
	$.ajax({
		url: path,
		type:"POST",
		data: companyFormToJSON(),
		contentType:"application/json",
		async: false,
		success:function(){
			$.ajax({
				type: "GET",
				url: path1 + "?page=0&size=500",
				dataType: "json",
				success: function(data){
					if(data!=null){
						$('#collectionModal').modal('hide');
						printListOfCompanies(data);
					}
				}
			});
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
		"phoneNumber":$('#inputPhoneNumber').val(),
		"authority": $('#selectAuth').val(),
		"state": "ACTIVE"
	});
}

function companyFormToJSON(currComp){
	return data = JSON.stringify({
			"name": $('#name1').val(),
			"address": $('#address1').val(),
			"description": $('#description1').val()
		}) 
}

$(document).on('click','#company',function(e){
	loadCollection();
});