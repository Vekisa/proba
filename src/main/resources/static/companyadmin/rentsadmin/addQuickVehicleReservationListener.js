$(document).on('submit', '#quickVehicleRoom', function(e) {
    e.preventDefault();
    let beginTimeString = $("#begin1").val();
	let endTimeString = $("#end1").val();
	let beginTime = new Date(beginTimeString);
	let endTime = new Date(endTimeString);
    let newVehicleReservation = new Object();
    newVehicleReservation.beginDate = beginTime;
    newVehicleReservation.endDate = endTime;
	let jsonBody = JSON.stringify(newVehicleReservation);
	let vehicleId = $("#quickVehicleRoom option:selected").val();
	let url = "/vehicle-reservations/quicks?vehicle=" + vehicleId;
	$.ajax({
		type: "POST",
		url: url,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function() {
  			loadCollection();
  		}
	});
})

$(document).on('click', '#addQuickVehicle', function(e) {
	createQuickVehicleForm();
})

function createQuickVehicleForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"quickVehicleRoom\" method=\"POST\"></form><br>";
	let beginDateHTML = "<label>Start: </label><input type=\"datetime-local\" id=\"begin1\"><br><br>";
	let endDateHTML = "<label>End: </label><input type=\"datetime-local\" id=\"end1\"><br><br>";
	let selectVehicleHTML = "Vehicle: <select id=\"quickVehicleSelect\">";
	$.ajax({
		type: "GET",
		url: "/rent-a-cars/" + companyId + "/vehicles",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, vehicle) {
					selectVehicleHTML += "<option value=\"" + vehicle.id + "\">" + vehicle.id + "</option>";
				});				
  			}
  		}
	});
	selectVehicleHTML += "</select><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#quickVehicleRoom").append(beginDateHTML);
	$("#quickVehicleRoom").append(endDateHTML);
	$("#quickVehicleRoom").append(selectVehicleHTML);
	$("#quickVehicleRoom").append(submitHTML);	
	$("collection").show();
}