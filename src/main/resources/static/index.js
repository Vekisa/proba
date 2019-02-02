function load(){
	$("#myMap").hide();
	map = new OpenLayers.Map("mapdiv");
    map.addLayer(new OpenLayers.Layer.OSM());
    var markers = new OpenLayers.Layer.Markers( "Markers" );
    map.addLayer(markers);
    markers.addMarker(new OpenLayers.Marker(lonLat));
    setMapLocation(15,15);
}

function setMapLocation(long, lat){
    var lonLat = new OpenLayers.LonLat(long, lat)
          .transform(
            new OpenLayers.Projection("EPSG:4326"),
            map.getProjectionObject()
          ); 
    var zoom=12;   
    map.setCenter (lonLat, zoom);
}

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

$(document).on('click','#tablebo tr', function() {
	var branchOfficeLink = $(this).attr('id');
	
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
});

$(document).on('click','#table tr', function() {
	var companyLink = $(this).attr('id');
	$.ajax({
		type: "GET",
		url: companyLink,
		dataType: "json",
		success: function(data){
			if(data!=null){
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
	
	if(company == "airline"){
		image = "air-plane-cartoon.png";
		header = "Flights";
	}else if(company == "rentacar"){
		image = "rent-a-car-cartoon.png";
		header = "Branch offices";
	}else if(company == "hotel"){
		image = "hotel-cartoon.png";
		header = "Catalogue";
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
				"<hr>" + 
				"<strong>" + header + "</strong>" +
				"<hr>";
	
	
						
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
	
	document.getElementById("tablediv").innerHTML += "<table class=\"table table-hover\"><thead><tr><th scope=\"col\">Type</th><th scope=\"col\">Description</th><th scope=\"col\">Price per night(&euro;)</th></tr></thead><tbody>" + catalogue + "</tbody></table>" ;
}

function printFlights(data){
	var pomDataFlights;
	$.ajax({
		type: "GET",
		url: data._links.airline_flights.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomDataFlights = data;
			}
		}
	});
	
	let flights = "";
	let s = "";
	$.each(pomDataFlights, function(index, flight) {
		s += "<tr>" + "<td>" + flight.startDestination + "</td>" + "<td>"+ flight.finishDestination 
		+ "</td>" + "<td>" + flight.transfers + "</td><td>" + flight.departureTime + "</td><td>" + flight.arrivalTime + "</td><td>"
		+ flight.flightLength + "</td><td>" + flight.basePrice + "</td>" + "</tr>";
	});
	
	flights =  "<table class=\"table table-hover\"><thead>" +
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
	
	let branchOffices = "";
	let s = "";
	$.each(pomDataBranchOffices, function(index, branchOffice) {
		s += "<tr id = \"" +branchOffice.links[0].href + "\">" + "<td>" + branchOffice.address + "</td>" + "</tr>";
	});
	
	branchOffices =  "<table class=\"table table-hover\" id = \"tablebo\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Address</th>" +
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
	$('#search-panel').css("display", "block");
})