$(document).on('submit', '#flightsegmentform', function(e) {
    e.preventDefault();
    let minWeight = $("#minWeightLuggage").val();
    let maxWeight = $("#maxWeightLuggage").val();
    let price = $("#priceLuggage").val();
    let newLuggageInfo = new Object();
    newLuggageInfo.minWeight = minWeight;
    newLuggageInfo.maxWeight = maxWeight;
    newLuggageInfo.price = price;
	let jsonBody = JSON.stringify(newLuggageInfo);
	let url = "/airlines/" + companyId + "/luggageInfos";
	$.ajax({
		type: "POST",
		url: url,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}
	});
	loadCollection();
})

$(document).on('click', '#addSegment', function(e) {
	createFlightSegmentForm();
})

function createFlightSegmentForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"flightsegmentform\" method=\"POST\"></form><br>";
	let startRowHTML = "First row: <input id=\"firstRowSegment\" type=\"text\"><br><br>";
	let endRowHTML = "Last row: <input id=\"lastRowSegment\" type=\"text\"><br><br>";
	let columnsHTML = "Number of columns: <input id=\"columnsSegment\" type=\"text\"><br><br>";
	let configurationSelectHTML = "Add to configuration: <select id=\"selectedCategory\">";
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId +"/configurations",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, configuration) {
					configurationSelectHTML += "<option value=\"" + configuration.id + "\">" + configuration.id + "</option>";
				});				
  			}
  		}
	});
	configurationSelectHTML += "</select><br><br>";
	let categorySelectHTML = "With seats category: <select id=\"selectedOffice\">";
	$.ajax({
		type: "GET",
		url: "/airlines/" + companyId +"/categories",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, category) {
					categorySelectHTML += "<option value=\"" + category.id + "\">" + category.name + "</option>";
				});				
  			}
  		}
	});
	categorySelectHTML += "</select><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#flightsegmentform").append(startRowHTML);
	$("#flightsegmentform").append(endRowHTML);
	$("#flightsegmentform").append(columnsHTML);
	$("#flightsegmentform").append(configurationSelectHTML);
	$("#flightsegmentform").append(categorySelectHTML);
	$("#flightsegmentform").append(submitHTML);
	$("#collection").show();	
}