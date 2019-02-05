function printCollection(data, authority) {
	if(authority == "HOTEL_ADMIN") {
		printRooms(data);
	}
	if(authority == "AIRLINE_ADMIN") {
		printFlights(data);
	}
	if(authority == "RENT_A_CAR_ADMIN") {
		printCars(data);
	}
	$("#collection").show();
}

function printRooms(data) {
	let tableBodyHTML = "";
	$.each(data, function(index, room) {
		let typeName = "";
		$.ajax({
			type: "GET",
			url: "/rooms/" + room.id + "/room-type",
			dataType: "json",
			async: false,			
			beforeSend: function(request) {
    			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  			},
  			success: function(data) {
  				if(data != null)
  					typeName = data.name;
  				else
  					typeName = "<strong>Not assigned</strong>";
  			}
		});
		tableBodyHTML += "<tr id=\"" + room.id + "\">" 
			+ "<td>" + typeName + "</td>" 
			+ "<td>" + room.numberOfBeds + "</td>" 
			+ "<td>" + room.rating + "</td>"
			+ "<td><button class=\"updateRoom btn btn-info btn-block\" id=\"" + room.id + "\"><span class=\"fa fa-user\"></span>Update</button></td>"
			+ "<td><button class=\"removeRoom btn btn-info btn-block\" id=\"" + room.id + "\"><span class=\"fa fa-user\"></span>Remove</button></td>"
			+ "</tr>";
	});
	tableHTML =  "<br><table class=\"table table-hover\" id = \"rooms\"><thead>" 
	+ "<tr>" 
	+ "<th scope=\"col\" onclick=\"sortTable(0, rooms)\" class=\"hoverable\">Room type</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(1, rooms)\" class=\"hoverable\">Number of beds</th>"
	+ "<th scope=\"col\" onclick=\"sortTable(2, rooms)\" class=\"hoverable\">Rating</th>" 
	+ "<td></td>"
	+ "<td></td>"
	+"</tr>"
	+ "</thead><tbody>" + tableBodyHTML + "</tbody></table>";
	document.getElementById("collection").innerHTML = tableHTML;
}

function printFlights(data) {
	let tableBodyHTML = "";
	$.each(data, function(index, flight) {
		let startName = "";
		$.ajax({
			type: "GET",
			url: "/flights/" + flight.id + "/start",
			async: false,			
			beforeSend: function(request) {
    			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  			},
  			success: function(data) {
  				if(data != null)
  					startName = data.name;
  				else
  					startName = "<strong>Not assigned</strong>";
  			}
		});
		let endName = "";
		$.ajax({
			type: "GET",
			url: "/flights/" + flight.id + "/finish",
			async: false,			
			beforeSend: function(request) {
    			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  			},
  			success: function(data) {
  				if(data != null)
  					endName = data.name;
  				else
  					endName = "<strong>Not assigned</strong>";
  			}
		});
		tableBodyHTML += "<tr id=\"" + flight.id + "\">" 
			+ "<td>" + flight.departureTime.substring(0, 18).replace('T', '<br>') + "</td>" 
			+ "<td>" + flight.arrivalTime.substring(0, 18).replace('T', '<br>') + "</td>" 
			+ "<td>" + flight.basePrice + "</td>" 
			+ "<td>" + flight.transfers + "</td>" 
			+ "<td>" + startName + "</td>" 
			+ "<td>" + endName + "</td>" 
			+ "<td><button class=\"updateFlight btn btn-info btn-block\" id=\"" + flight.id + "\">Update</button></td>"
			+ "<td><button class=\"removeFlight btn btn-info btn-block\" id=\"" + flight.id + "\">Remove</button></td>"
			+ "</tr>";
	});
	tableHTML =  "<br><table class=\"table table-hover\" id = \"flights\"><thead>" 
	+ "<tr>" 
	+ "<th scope=\"col\" onclick=\"sortTable(0, flights)\" class=\"hoverable\">Departure time</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(1, flights)\" class=\"hoverable\">Arrival time</th>"
	+ "<th scope=\"col\" onclick=\"sortTable(2, flights)\" class=\"hoverable\">Price</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(3, flights)\" class=\"hoverable\">Transfers</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(4, flights)\" class=\"hoverable\">From</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(5, flights)\" class=\"hoverable\">To</th>" 
	+ "<td></td>"
	+ "<td></td>"
	+"</tr>"
	+ "</thead><tbody>" + tableBodyHTML + "</tbody></table>";
	document.getElementById("collection").innerHTML = tableHTML;	
}

function printCars(data) {
	let tableBodyHTML = "";
	$.each(data, function(index, vehicle) {
		tableBodyHTML += "<tr id=\"" + vehicle.id + "\">" 
			+ "<td>" + vehicle.brand + " " + vehicle.model + " " + vehicle.productionYear + "</td>" 
			+ "<td>" + vehicle.seatsNumber + "</td>" 
			+ "<td>" + vehicle.pricePerDay + "</td>" 
			+ "<td>" + vehicle.type + "</td>" 
			+ "<td><button class=\"updateVehicle btn btn-info btn-block\" id=\"" + vehicle.id + "\">Update</button></td>"
			+ "<td><button class=\"removeVehicle btn btn-info btn-block\" id=\"" + vehicle.id + "\">Remove</button></td>"
			+ "</tr>";
	});
	tableHTML =  "<br><table class=\"table table-hover\" id = \"vehicles\"><thead>" 
	+ "<tr>" 
	+ "<th scope=\"col\" onclick=\"sortTable(0, vehicles)\" class=\"hoverable\">Info</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(1, vehicles)\" class=\"hoverable\">Capacity</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(2, vehicles)\" class=\"hoverable\">Price per day</th>" 
	+ "<th scope=\"col\" onclick=\"sortTable(3, vehicles)\" class=\"hoverable\">Type</th>" 
	+ "<td></td>"
	+ "<td></td>"
	+"</tr>"
	+ "</thead><tbody>" + tableBodyHTML + "</tbody></table>";
	document.getElementById("collection").innerHTML = tableHTML;	
}