var currentData;
var selected_search_item;

function load(){
		$("#myMap").hide();
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

$(document).on('click','#home',function(e){
	e.preventDefault();
	$("#myMap").hide();
	document.getElementById("tablediv").innerHTML = "<img height=\"700\" width=\"700\" src=\"home.png\" alt=\"Travel\">";
});

$(document).on('click','#about',function(e){
	e.preventDefault();
	$("#myMap").hide();
	document.getElementById("tablediv").innerHTML = "Ovde bi kao trebalo da pise nesto pametno o nasoj stranici";
});

$(document).on('click','#airline',function(e){
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

$(document).on('click','#about',function(e){
	e.preventDefault();
	$("#myMap").hide();
	document.getElementById("tablediv").innerHTML ="Ovo je nas predivni about. Samo cu reci - Napusite mi se kurca svi sa softa."
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