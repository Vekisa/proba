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
			+ "<td><button class=\"update btn btn-info btn-block\" id=\"" + room.id + "\"><span class=\"fa fa-user\"></span>Update</button></td>"
			+ "<td><button class=\"remove btn btn-info btn-block\" id=\"" + room.id + "\"><span class=\"fa fa-user\"></span>Remove</button></td>"
			+ "</tr>";
	});
	tableHTML =  "<table class=\"table table-hover\" id = \"rooms\"><thead>" 
	+ "<tr>" 
	+ "<th scope=\"col\">Room type</th>" 
	+ "<th scope=\"col\">Number of beds</th>"
	+ "<th scope=\"col\">Rating</th>" 
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
			+ "<td>" + flight.departureTime + "</td>" 
			+ "<td>" + flight.arrivalTime + "</td>" 
			+ "<td>" + flight.transfers + "</td>" 
			+ "<td>" + startName + "</td>" 
			+ "<td>" + endName + "</td>" 
			+ "<td><button class=\"update btn btn-info btn-block\" id=\"" + flight.id + "\">Update</button></td>"
			+ "<td><button class=\"remove btn btn-info btn-block\" id=\"" + flight.id + "\">Remove</button></td>"
			+ "</tr>";
	});
	tableHTML =  "<table class=\"table table-hover\" id = \"flights\"><thead>" 
	+ "<tr>" 
	+ "<th scope=\"col\">Departure time</th>" 
	+ "<th scope=\"col\">Arrival time</th>"
	+ "<th scope=\"col\">Transfers</th>" 
	+ "<th scope=\"col\">From</th>" 
	+ "<th scope=\"col\">To</th>" 
	+ "<td></td>"
	+ "<td></td>"
	+"</tr>"
	+ "</thead><tbody>" + tableBodyHTML + "</tbody></table>";
	document.getElementById("collection").innerHTML = tableHTML;	
}

function printCars(data) {
}