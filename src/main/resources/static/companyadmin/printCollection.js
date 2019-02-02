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
}

function printRooms(data) {
	let tableBodyHTML = "";
	$.each(data, function(index, room) {
		tableBodyHTML += "<tr id=\"" + room.id + "\">" 
			+ "<td>" + room.roomType + "</td>" 
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
}

function printCars(data) {
}