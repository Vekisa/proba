var currentData;
var currentUser;

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
					$('#userName').text(data.username);
					currentUser = data;
				}
			}
		});
    
	$('#profile').hide();
	$("#myMap").hide();
	$('#profdiv').hide();
}

function setMapLocation(long, lat){
    var lonLat = new OpenLayers.LonLat(long, lat)
          .transform(
            new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
            map.getProjectionObject() // to Spherical Mercator Projection
          );
          
    var zoom=12;

    var markers = new OpenLayers.Layer.Markers( "Markers" );
    map.addLayer(markers);
    
    markers.addMarker(new OpenLayers.Marker(lonLat));
    
    map.setCenter(lonLat, zoom);
}

$(document).on('click','#userName',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").hide();
	
	$('#usernameP').text(currentUser.username);
	$('#firstNameP').text(currentUser.firstName);
	$('#lastNameP').text(currentUser.lastName);
	$('#emailP').text(currentUser.email);
	$('#cityP').text(currentUser.city);
	$('#phoneNumberP').text(currentUser.phoneNumber);
	
	$("#profile").show();
	$('#friendsBtn').val(currentUser._links.friends.href);
	$('#friendsReqBtn').val(currentUser._links.friend_requests.href);
	$('#reservationHisBtn').val(currentUser._links.reservations_history.href);
	$('#activeResBtn').val(currentUser._links.active_reservations.href);
});

$(document).on('click','#allUsers',function(e){
	e.preventDefault();
    
    $('#tablediv').show();
    $('#profdiv').hide();
    $('#profile').hide();
    
	var allUsers;
	var friends;
    var sentRequests;
    
	$.ajax({
		url: "/users/registered",
		type:"GET",
		async: false,
		success: function(data){
            allUsers = data;
		}
	});
    
    var indexPom;
    $.each(allUsers, function(index, user) {
        if(user.username == currentUser.username)
            indexPom = index;
    });
	allUsers.splice(indexPom, 1);
    
    
	$.ajax({
		url: currentUser._links.friends.href,
		type:"GET",
		async: false,
		success: function(data){
            friends = data;
		}
	});
    
    $.ajax({
		url: currentUser._links.sent_friend_request.href,
		type:"GET",
		async: false,
		success: function(data){
            sentRequests = data;
		}
	});
	
	printUsers(allUsers,friends,sentRequests);
	
});

function userInList(userN,data){
    var a = 0;
	$.each(data, function(index, user) {
		if(user.username == userN){
            a = 1;
        }
	});
    
    if(a == 1){
        return true;
    }else{
        return false;
    }
}

$(document).on('click','.addFriend',function(e){
    e.preventDefault();
    $(this).prop("disabled",true);
	$.ajax({
		url: $(this).val(),
		type:"POST",
		success: function(data){
            alert("poslao");      
		}
	});
});

$(document).on('click','.unFriend',function(e){
    e.preventDefault();
    $(this).prop("disabled",true);
	$.ajax({
		url: $(this).val(),
		type:"DELETE",
		success: function(data){
            alert("Izbacio iz prijatelja");      
		}
	});
});

$(document).on('click','.cancelFr',function(e){
     e.preventDefault();
    $(this).prop("disabled",true);
	$.ajax({
		url: $(this).val(),
		type:"DELETE",
		success: function(data){
            alert("Odustao od zahteva");      
		}
	});
});

function printUsers(allUsers,friends,sentRequests){
    var users = "";
    var button = "";
    var valueBtn ="";
    var link = "";
	if(allUsers!=null){
		$.each(allUsers, function(index, user) {
            if(friends != null && userInList(user.username,friends)){
                link = currentUser._links.remove_friend.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-danger unFriend\">UnFriend</button>"; 
            }else if(sentRequests != null && userInList(user.username,sentRequests)){
                link = currentUser._links.cancel_friend_request.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-warning cancelFr\">Cancel</button>";    
            }else{
                link = currentUser._links.send_request.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-primary addFriend\">Add Friend</button>";
            }
			users += "<tr>" + "<td scope=\"col\">" + user.username + "</td>" + "<td scope=\"col\">"+ user.firstName + "</td>" + "<td scope=\"col\">" + user.lastName + "</td>" +
			"<td scope=\"col\">" + user.email + "</td>" + "<td scope=\"col\">" + user.city + "</td>" + "<td scope=\"col\">" + button + "</td>" + "</tr>";
		});
	}
	
	document.getElementById("tablediv").innerHTML = 
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

//Booking
$(document).on('click','.bookvehicle',function(e){
	alert("bukiram auto");
});

$(document).on('click','.bookflight',function(e){
	alert("bukiram let");
});

//-----------

$(document).on('click','#shoppingcart',function(e){
	e.preventDefault();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	$("#myMap").hide();
	document.getElementById("tablediv").innerHTML = "<h1><strong>Shopping Cart</strong></h1>";
});

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

$(document).on('click','#logout',function(e){
	e.preventDefault();
	$.ajax({
		url: "/users/registered/logout",
		type:"POST",
		async: false,
		success: function(data){
			localStorage.token = null;
			window.location.href = 'index.html';		
		}
	});
});

$(document).on('click','#home',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	document.getElementById("tablediv").innerHTML = "<img height=\"700\" width=\"700\" src=\"home.png\" alt=\"Travel\">";
});

$(document).on('click','.backToProf',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").hide();
	$("#profdiv").hide();
	
	$('#usernameP').text(currentUser.username);
	$('#firstNameP').text(currentUser.firstName);
	$('#lastNameP').text(currentUser.lastName);
	$('#emailP').text(currentUser.email);
	$('#cityP').text(currentUser.city);
	$('#phoneNumberP').text(currentUser.phoneNumber);
	
	$("#profile").show();
	$('#friendsBtn').val(currentUser._links.friends.href);
	$('#friendsReqBtn').val(currentUser._links.friend_requests.href);
	$('#reservationHisBtn').val(currentUser._links.reservations_history.href);
	$('#activeResBtn').val(currentUser._links.active_reservations.href);
});

$(document).on('click','#airline',function(e){
	e.preventDefault();
	$("#tablediv").show();
	$("#myMap").hide();
	$("#profile").hide();
	$('#profdiv').hide();
	$.ajax({
		type: "GET",
		url: "airlines?page=0&size=10",
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
	$("#myMap").hide();
	$.ajax({
		type: "GET",
		url: "airlines?page=0&size=10",
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
	$("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	$.ajax({
		type: "GET",
		url: "rent-a-cars?page=0&size=10",
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
	$("#myMap").hide();
	$.ajax({
		type: "GET",
		url: "rent-a-cars?page=0&size=10",
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
	$("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	$.ajax({
		type: "GET",
		url: "hotels?page=0&size=10",
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
	$("#myMap").hide();
	$.ajax({
		type: "GET",
		url: "hotels?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#bos',function(e){
	$("#myMap").hide();
	e.preventDefault();
	printCompany(currentData,"rentacar");
	printBranchOffices(currentData);
});

function printListOfCompanies(data){
	var s = "";
	$.each(data, function(index, company) {
		s += "<tr id = \"" + company.links[0].href + "\">" + "<th scope=\"row\">"+ index +"</th>" + "<td>" + company.name + "</td>" + "<td>"+ company.address
		+ "</td>" + "<td>" + company.description + "</td></tr>";
	});
	document.getElementById("tablediv").innerHTML = "<table class=\"table table-hover\" id = \"table\"><thead>" +
	    		"<tr>" + 
	      		  "<th scope=\"col\">#</th>" +
			      "<th scope=\"col\">Name</th>" +
			      "<th scope=\"col\">Address</th>" +
			      "<th scope=\"col\">Description</th>" +
	    		"</tr>" +
	  		"</thead><tbody>" + s + "</tbody></table>";
}

$(document).on('click','#friendsBtn',function(e){
	e.preventDefault();
	var pomDataFriends;
	 $.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomDataFriends = data;
			}
	 });
	 
	 printFriends(pomDataFriends);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	 
});

$(document).on('click','#friendsReqBtn',function(e){
	e.preventDefault();
	var pomData;
	 $.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			}
	 });
	 
	 printFriendRequests(pomData);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	 
});

$(document).on('click','#reservationHisBtn',function(e){
	e.preventDefault();
	var pomData;
	 $.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			}
	 });
	 
	 printReservationHistory(pomData);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	 
});

$(document).on('click','#activeResBtn',function(e){
	e.preventDefault();
	var pomData;
	 $.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			}
	 });
	 
	 printReservationHistory(pomData);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	 
});

function printFriends(data){
	var friends = "";
	if(data!=null){
		$.each(data, function(index, friend) {
			friends += "<tr>" + "<td scope=\"col\">" + friend.username + "</td>" + "<td scope=\"col\">"+ friend.firstName + "</td>" + "<td scope=\"col\">" + friend.lastName + "</td>" +
			"<td scope=\"col\">" + friend.email + "</td>" + "</tr>";
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Username</th>" +
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Email</th>" +
		"</tr>" +
		"</thead><tbody>" + friends + "</tbody></table>";
}

$(document).on('click','#btnAcceptReq',function(e){
    e.preventDefault();
    $('#btnAcceptReq').prop("disabled",true);
    $('#btnDeclineReq').prop("disabled",true);
    $.ajax({
		type: "POST",
		url: $(this).val(),
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				alert("prihvatio");
			}
		}
	});
});

$(document).on('click','#btnDeclineReq',function(e){
    e.preventDefault();
    $('#btnAcceptReq').prop("disabled",true);
    $('#btnDeclineReq').prop("disabled",true);
    $.ajax({
		type: "DELETE",
		url: $(this).val(),
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				alert("odustao");
			}
		}
	});
});

function printFriendRequests(data){
	var friendRequests = "";
    var linkAccept = "";
    var linkDecline = "";
    var valueBtnAccept = "";
    var valueBtnDecline = ""; 
    
	if(data!=null){
		$.each(data, function(index, friendRequest) {
            linkAccept = currentUser._links.accept_request.href;
            valueBtnAccept = linkAccept.substring(0, linkAccept.length-1) + friendRequest.sender.id;
            linkDecline = currentUser._links.decline_request.href;
            valueBtnDecline = linkDecline.substring(0, linkDecline.length-1) + friendRequest.sender.id;
            
			friendRequests += "<tr>" + "<td scope=\"col\">" + friendRequest.sender.firstName + "</td>" + "<td scope=\"col\">" + friendRequest.sender.lastName + "</td>" +
			"<td scope=\"col\">" + friendRequest.requestTime + "</td>"
            + "<td scope=\"col\">" + "<button value=\"" + valueBtnAccept + "\" type=\"button\" class=\"btn btn-success\" id =\"btnAcceptReq\">Accept</button>" + "</td>"
            + "<td scope=\"col\">" + "<button value=\"" + valueBtnDecline + "\" type=\"button\" class=\"btn btn-danger\" id =\"btnDeclineReq\">Decline</button>" + "</td>"
                + "</tr>";
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Request Time</th>" +
		"</tr>" +
		"</thead><tbody>" + friendRequests + "</tbody></table>";
}

function printReservationHistory(data){
	var history = "";
	var name = "";
	if(data!=null){
		$.each(data, function(index, reservation) {
			 $.ajax({
					type: "GET",
					url:  reservation._links.location.href,
					dataType: "json",
					async: false,
					success: function(data){
						name = data.name;
					}
			 });
			 
			history += "<tr>" + "<td scope=\"col\">" + reservation.beginDate + "</td>" + "<td scope=\"col\">"+ reservation.endDate + "</td>" +
			"<td scope=\"col\">" + name + "</td>" + "</tr>";
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Begin Date</th>" +
	    "<th scope=\"col\">End Date</th>" +
	    "<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + history + "</tbody></table>";
}

function printActiveReservations(data){
	var history = "";
	var name = "";
	if(data!=null){
		$.each(data, function(index, reservation) {
			 $.ajax({
					type: "GET",
					url:  reservation._links.location.href,
					dataType: "json",
					async: false,
					success: function(data){
						name = data.name;
					}
			 });
			 
			history += "<tr>" + "<td scope=\"col\">" + reservation.beginDate + "</td>" + "<td scope=\"col\">"+ reservation.endDate + "</td>" +
			"<td scope=\"col\">" + name + "</td>" + "</tr>";
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Begin Date</th>" +
	    "<th scope=\"col\">End Date</th>" +
	    "<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + history + "</tbody></table>";
}

$(document).on('click','#destbtn',function(e){
	$("#myMap").hide();
	e.preventDefault();
	printCompany(currentData,"airline");
	printDestinations(currentData);
});

$(document).on('click','#flightsbtn',function(e){
	$("#myMap").hide();
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
	$("#myMap").hide();
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
	 
	 var destinations = "";
    
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
		url: pomData._links.location.href,
		dataType: "json",
		success: function(data){
			if(data!=null){
				$.ajax({
					type: "GET",
					url: data._links.long_lat.href,
					dataType: "json",
					success: function(data2){
						if(data2!=null){
							setMapLocation(data2.lon, data2.lat);
						}
					}
				});
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
	
	$("#myMap").show();
	
	var vehicles = "";
	
	$.each(pomDataVehicles, function(index, vehicle) {
		vehicles += "<tr><td scope=\"col\">" + vehicle.name + "</td>" + "<td scope=\"col\">"+ vehicle.brand 
		+ "<td scope=\"col\">" + vehicle.model + "</td>"
		+ "<td scope=\"col\">"+ vehicle.seatsNumber + "</td>" 
		+ "<td scope=\"col\">"+ vehicle.type + "</td>"
		+ "<td scope=\"col\">"+ vehicle.pricePerDay + "</td>"
		+ "<td scope=\"col\"><button id = \"" + vehicle.links[1].href +"\" type=\"button\" class=\"btn btn-success bookvehicle\">Add to cart</button></td>" 
		+ "</td></tr>";
	});
	
	var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
				"<tr>" + 
			    "<th scope=\"col\">Name</th>" +
			    "<th scope=\"col\">Brand</th>" +
			    "<th scope=\"col\">Model</th>" +
			    "<th scope=\"col\">Seats number</th>" +
			    "<th scope=\"col\">Type</th>" +
			    "<th scope=\"col\">Price per day(&euro;)</th>" +
			    "<th scope=\"col\"></th>" +
				"</tr>" +
			"</thead><tbody>" + vehicles + "</tbody></table>";
	
	printCompany(currentData,"rentacar");
	document.getElementById("tablediv").innerHTML +=  "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"bos\">Branch offices</button></div></div>" + 
		"<hr>" + "<strong>Vehicles of branch office on address \"" + pomData.address + "\"</strong>" + "<hr>" + tabela;
});

$(document).on('click','#table tr', function() {
	var companyLink = $(this).attr('id');
	$.ajax({
		type: "GET",
		url: companyLink,
		dataType: "json",
		success: function(data){
			if(data!=null){
				currentData = data;
				if(companyLink.includes("airlines")){
					printCompany(data,"airline");
					printFlights(data);
				}else if(companyLink.includes("rent-a-cars")){
					printCompany(data,"rentacar");
					printBranchOffices(data);
				}else if(companyLink.includes("hotels")){
					printCompany(data,"hotel");
					printHotelCatalogue(data);
				}
			}
		}
	});
});

function printCompany(data, company){
	var pomData;
	
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
		
		$.ajax({
			type: "GET",
			url: pomData._links.long_lat.href,
			dataType: "json",
			success: function(data){
				if(data!=null){
					setMapLocation(data.lon,data.lat);
				}
			}
		});
		$("#myMap").show();
	}
	
	var header = "";	
	var image = "";
	var buttonTitle = "";
	var buttonTitle2= "";
	var buttonId = "";
	
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
	
	var code = "<div class=\"container\">" +
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
	var link = "";
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
	
	var catalogue = "";
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
	
	var flights = "";
	var s = "";
	if(pomDataFlights!=null){
		$.each(pomDataFlights, function(index, flight) {
			s += "<tr>" + "<td>" + flight.startDestination + "</td>" + "<td>"+ flight.finishDestination 
			+ "</td>" + "<td>" + flight.transfers + "</td><td>" + flight.departureTime + "</td><td>" + flight.arrivalTime + "</td><td>"
			+ flight.flightLength + "</td><td>" + flight.basePrice + "</td>"
			+ "<td scope=\"col\"><button id = \"" + flight.links[0].href +"\" type=\"button\" class=\"btn btn-success bookflight\">Add to cart</button></td>"
			+"</tr>";
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
	
	var branchOffices = "";
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
	$('#locationLabel1').text('Location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Company name');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Rent-a-car services');
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#hotel-search', function(event){
	event.preventDefault();
	$('#locationLabel1').text('Location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Company name');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Hotels');
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#airline-search', function(event){
	event.preventDefault();
	$('#locationLabel1').text('Start location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Target location');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Airlines');
	$('#airline-controls').css("display", "block");
	$('#search-panel').css("display", "block");
})
