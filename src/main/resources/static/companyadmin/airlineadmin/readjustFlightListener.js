$(document).on('click','#readjustFlight',function(e){
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
})
 
 $(document).on('change','#selectedFlight',function(e) {
	let flightId = $("#selectedFlight option:selected").val();
    loadSeatsChartFor(flightId);
})
 
function loadSeatsChartFor(id) {
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
  				tableHTML += "</tr></table><br><button id=\"removeSeatsButton\" class=\"btn btn-info btn-block custombutton\"> Remove </button><br>";
  				document.getElementById("flightSeatsChart").innerHTML = tableHTML;
  			}
  		}
	});
}

$(document).on('click','#removeSeatsButton',function(e) {

})

$(document).on('click','.btn-primary',function(e) {
	$(this).removeClass('btn-primary').addClass('btn-success');
})

$(document).on('click','.btn-success',function(e) {
	$(this).removeClass('btn-success').addClass('btn-primary');
})

$(document).on({
    mouseenter: function () {
        
    },
    mouseleave: function () { }
}, ".flight-seat");

function getMaxColValue(data) {
	let maxValue = 0;
	$.each(data, function(index, seat) {
		if(seat.column > maxValue)
			maxValue = seat.column;
	});	
	return maxValue;
}