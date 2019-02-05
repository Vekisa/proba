var flightId;

$(document).on('click','#incomeAirline',function(e){
	document.getElementById("collection").innerHTML = "";
	let formHTML = "<label><strong>Income by date:</strong></label><form id=\"incomeFormAirline\" method=\"PUT\"></form>";
	let beginDateHTML = "<br><input type=\"date\" id=\"begin\">-";
	let endDateHTML = "<input type=\"date\" id=\"end\"><br>";
	document.getElementById("collection").innerHTML = formHTML;
	$("#incomeFormAirline").append(beginDateHTML);
	$("#incomeFormAirline").append(endDateHTML);
	let submitHTML = "<br><input type=\"submit\" value=\"Calculate\"  class=\"btn btn-info btn-block custombutton\"><br>";
	$("#incomeFormAirline").append(submitHTML);
})

$(document).on('click','#graphAirline',function(e){
	document.getElementById("collection").innerHTML = "";
	let formHTML = "<br><label><strong>Statistic by date:</strong></label><form id=\"statisticFormAirline\" method=\"PUT\"></form>";
	let beginDateHTML = "<br><input type=\"date\" id=\"begin\">-";
	let endDateHTML = "<input type=\"date\" id=\"end\"><br>";
	document.getElementById("collection").innerHTML = formHTML;
	$("#statisticFormAirline").append(beginDateHTML);
	$("#statisticFormAirline").append(endDateHTML);
	let submitHTML = "<br><input type=\"submit\" value=\"Calculate\"  class=\"btn btn-info btn-block custombutton\"><br>";
	$("#statisticFormAirline").append(submitHTML);
})

$(document).on('submit', '#incomeFormAirline', function(e) {
    e.preventDefault();
    if(document.getElementById("chartContainer") == undefined) {
    	let chartHTML = "<br><div id=\"chartContainer\" style=\"height: 380px; width: 100%;\"></div>"
		$("#collection").append(chartHTML);
	} else {
		$("#chartContainer").show();
	}
	let beginTimeString = $("#begin").val();
	let endTimeString = $("#end").val();
	let beginTime = new Date(beginTimeString);
	let endTime = new Date(endTimeString);
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId + "/income?begin=" + beginTime.getTime() + "&end=" + endTime.getTime(),
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null)
  				createChart(data, "Income", "$", 10000);
  		}
	});
})

$(document).on('submit', '#statisticFormAirline', function(e) {
    e.preventDefault();
    if(document.getElementById("chartContainer") == undefined) {
    	let chartHTML = "<br><div id=\"chartContainer\" style=\"height: 380px; width: 100%;\"></div>"
		$("#collection").append(chartHTML);
	} else {
		$("#chartContainer").show();
	}
	let beginTimeString = $("#begin").val();
	let endTimeString = $("#end").val();
	let beginTime = new Date(beginTimeString);
	let endTime = new Date(endTimeString);
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId + "/statistic?begin=" + beginTime.getTime() + "&end=" + endTime.getTime(),
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null)
  				createChart(data, "Statistic", "", 50);
  		}
	});
})

$(document).on('click', '.updateFlight', function(e) {
	flightId = e.toElement.id;
	let url = "/flights/" + e.toElement.id;
	createFlightForm();
	$("#flightform").prop("id", "flightformupdate");
})

$(document).on('click', '.removeFlight', function(e) {
	let url = "/flights/" + e.toElement.id;
	$.ajax({
		type: "DELETE",
		url: url,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}
	});
	loadCollection();
})

$(document).on('submit', '#flightformupdate', function(e) {
	e.preventDefault();
	let url = "/flights/" + flightId;
	let basePrice = $("#basePrice").val();
	let transfers = $("#transfers").val();
	let numberOfTransfers = $("#numberOfTransfers").val();
	let begin1 = $("#begin1").val();
	let end1 = $("#end1").val();
	let begin2 = $("#begin2").val();
	let end2 = $("#end2").val();
	let flight = new Object();
	flight.basePrice = basePrice;
	flight.transfers = transfers;
	flight.numberOfTransfers = numberOfTransfers;
	flight.departureTime = begin1;
	flight.arrivalTime = end1;
	let jsonBody = JSON.stringify(flight);
	$.ajax({
		type: "PUT",
		url: url,
		async: false,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}
	});
    let destinationId = $("#finishDestination option:selected").val();
    	$.ajax({
			type: "PUT",
			url: url + "/?destinationId=" + destinationId,
			async: false,
			beforeSend: function(request) {
	    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
	  		}
	});
	loadCollection();
})

$(document).on('submit', '#flightform', function(e) {
	e.preventDefault();
	let url = "/flights";
	let basePrice = $("#basePrice").val();
	let transfers = $("#transfers").val();
	let numberOfTransfers = $("#numberOfTransfers").val();
	let begin1 = $("#begin1").val();
	let end1 = $("#end1").val();
	let begin2 = $("#begin2").val();
	let end2 = $("#end2").val();
	let flight = new Object();
	flight.basePrice = basePrice;
	flight.transfers = transfers;
	flight.numberOfTransfers = numberOfTransfers;
	flight.departureTime = begin1;
	flight.arrivalTime = end1;
	let jsonBody = JSON.stringify(flight);
	let destinationId = $("#finishDestination option:selected").val();
	$.ajax({
		type: "POST",
		url: url + "/?airline=" + companyId + "&destination=" + destinationId,
		async: false,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}
	});
	loadCollection();
})

$(document).on('click','#addFlight',function(e) {
	createFlightForm();
})

function createFlightForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"flightform\" method=\"POST\"></form>";
	let destinationHTML = "Finish destination: <select id=\"finishDestination\">";
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
					destinationHTML += "<option value=\"" + location.id + "\">" + location.name + "</option>";
				});				
  			}
  		}
	});
	destinationHTML += "</select><br><br>";
	let priceHTML = "Base price: <input id=\"basePrice\" type=\"text\"><br><br>";
	let transfersHTML = "Transfers: <input id=\"transfers\" type=\"text\"><br><br>";
	let numberOfTransfersHTML = "Number of transfers: <input id=\"numberOfTransfers\" type=\"text\"><br><br>";
	let beginDateHTML = "<label>Departure time: </label><input type=\"datetime-local\" id=\"begin1\"><br><br>";
	let endDateHTML = "<label>Arrival time: </label><input type=\"datetime-local\" id=\"end1\"><br><br>";
	let returnBeginDateHTML = "<label>Return departure time: </label><input type=\"datetime-local\" id=\"begin2\"><br><br>";
	let returnEndDateHTML = "<label>Return arrival time: </label><input type=\"datetime-local\" id=\"end2\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#flightform").append(destinationHTML);
	$("#flightform").append(priceHTML);
	$("#flightform").append(transfersHTML);
	$("#flightform").append(numberOfTransfersHTML);
	$("#flightform").append(beginDateHTML);
	$("#flightform").append(endDateHTML);
	$("#flightform").append(returnBeginDateHTML);
	$("#flightform").append(returnEndDateHTML);
	$("#flightform").append(submitHTML);	
	$("#collection").show();
}