var flight;
var seat;

$(document).on('click','#readjustFlight',function(e){
	createSeatsChart();
})

function createSeatsChart() {
	document.getElementById("collection").innerHTML = "";
	let flightHTML = "<br>Flight: <select id=\"selectedFlight\">";
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId + "/flights",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, flight) {
					flightHTML += "<option value=\"" + flight.id + "\">" + flight.finishDestination.name + " (" + flight.basePrice + ")" + "</option>";
				});				
  			}
  		}
	});
	flightHTML += "</select><br><br><div id=\"flightSeatsChart\"></div>";
	document.getElementById("collection").innerHTML = flightHTML;
	$("#collection").show();
}
 
 $(document).on('change','#selectedFlight',function(e) {
	let flightId = $("#selectedFlight option:selected").val();
	flight = flightId;
    loadSeatsChartFor(flightId);
})
 
function loadSeatsChartFor(id) {
	flightId = id;
	$.ajax({
		type: "GET",
		url: "/flights/" + id + "/seats",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
  				let tableHTML = "<table id=\"seatsChart\"><tr>";
  				let currentRow = 1;
  				let seatClass = "";
			 	$.each(data, function(index, seat) {
			 		if(seat.state == "TAKEN")
			 			seatClass = "btn-danger";
			 		else
			 			seatClass = "btn-primary";
					if(seat.row == currentRow) {
						tableHTML += "<td><button id=\"" + seat.id + "\" class=\"btn flight-seat " + seatClass + "\" align=\"center\">" + (index + 1) + "</button></td>";
					} else {
						tableHTML += "</tr><tr><td><button id=\"" + seat.id + "\" class=\"btn flight-seat " + seatClass + "\" align=\"center\">" + (index + 1) + "</button></td>";
						currentRow = seat.row;
					}
				});	
  				tableHTML += "</tr></table><br><br>";
  				document.getElementById("flightSeatsChart").innerHTML = tableHTML;
  			}
  		}
	});
	$("#collection").show();
}

$(document).on('click','.btn-primary',function(e) {
	document.getElementById("collection").innerHTML = "";
	let seatInfoHTML = "<div id=\"flightSeatInfo\"></div>"
	document.getElementById("collection").innerHTML = seatInfoHTML;
	createFlightSeatForm(e.target.id);
})

function createFlightSeatForm(id) {
	seat = id;
	let luggageInfoHTML = "<br><form>Luggage info: <select id=\"seatLuggageInfo\">";
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId + "/luggageInfos",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, luggage) {
					luggageInfoHTML += "<option value=\"" + luggage.id + "\">" + luggage.minWeight + " - " + luggage.maxWeight + " ( " + luggage.price + " ) " + "</option>";
				});				
  			}
  		}
	});
	luggageInfoHTML += "</select><br><br>";
	let categoryHTML = "Seat category: <select id=\"seatCategory\">";
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId + "/categories",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, category) {
					categoryHTML += "<option value=\"" + category.id + "\">" + category.name + "</option>";
				});				
  			}
  		}
	});
	categoryHTML += "</select><br><br>";
	let typeHTML = "Seat type: <select id=\"seatType\">";
	typeHTML += "<option value=\"REGULAR_SEAT\">REGULAR_SEAT</option>";
	typeHTML += "<option value=\"QUICK_RESERVATION\">QUICK_RESERVATION</option>";
	typeHTML += "</select><br><br>";
	let priceHTML = "Price: <input id=\"seatPrice\" type=\"text\"></form><br><br>";
	let updateSeatHTML = "<button class=\"btn btn-info btn-block custombutton\" id=\"updateSeat\"> Update </button>";
	let removeSeatHTML = "<button class=\"btn btn-info btn-block custombutton\" id=\"removeSeat\"> Remove </button><br>";
	$("#flightSeatInfo").append(luggageInfoHTML);
	$("#flightSeatInfo").append(categoryHTML);
	$("#flightSeatInfo").append(priceHTML);
	$("#flightSeatInfo").append(typeHTML);
	$("#flightSeatInfo").append(updateSeatHTML);
	$("#flightSeatInfo").append(removeSeatHTML);
	$.ajax({
		type: "GET",
		url: "/seats/" + id,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$("#seatCategory").val(data.category.id).prop("selected", true);
				if(data.luggageInfo != undefined)
					$("#seatLuggageInfo").val(data.luggageInfo.id).prop("selected", true);
				$("#seatPrice").val(data.price);				
  			}
  		}	
	});
}

$(document).on('click','#updateSeat',function(e) {
	let categoryId = $("#seatCategory option:selected").val();
	$.ajax({
		type: "PUT",
		async: false,
		url: "/seats/" + seat + "/category?category=" + categoryId,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}		
	});
	let luggageId = $("#seatLuggageInfo option:selected").val();
	$.ajax({
		type: "PUT",
		async: false,
		url: "/seats/" + seat + "/luggageInfo?luggageId=" + luggageId,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}		
	});
	let price = $("#seatPrice").val();
	let type = $("#seatType option:selected").val();
	let newSeat = new Object();
	newSeat.price = price;
	newSeat.type = type;
	let jsonBody = JSON.stringify(newSeat);
	$.ajax({
		type: "PUT",
		url: "/seats/" + seat,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}	
	});
	createSeatsChart();
	loadSeatsChartFor(flight);
})

$(document).on('click','#removeSeat',function(e) {
	$.ajax({
		type: "DELETE",
		url: "/seats/" + seat,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}		
	});
	createSeatsChart();
	loadSeatsChartFor(flight);
})

function getMaxColValue(data) {
	let maxValue = 0;
	$.each(data, function(index, seat) {
		if(seat.column > maxValue)
			maxValue = seat.column;
	});	
	return maxValue;
}