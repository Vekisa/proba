$(document).on('submit', '#roomtypeform', function(e) {
    e.preventDefault();
    let name = $("#roomTypeName").val();
    let description = $("#roomTypeDescription").val();
    let price = $("#roomTypePrice").val();
    let newRoomType = new Object();
    newRoomType.name = name;
    newRoomType.description = description;
    newRoomType.pricePerNight = price;
	let jsonBody = JSON.stringify(newRoomType);
	let url = "/catalogues/";
	$.ajax({
		type: "GET",
		url: "/hotels/" + companyId + "/catalogue",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null)
  				url += data.id + "/room-types";
  		}
	});
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

$(document).on('click', '#addRoomType', function(e) {
	createRoomTypeForm();
})

function createRoomTypeForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"roomtypeform\" method=\"POST\"></form><br>";
	let nameHTML = "Name: <input id=\"roomTypeName\" type=\"text\"><br><br>";
	let descriptionHTML = "Description: <input id=\"roomTypeDescription\" type=\"text\"><br><br>";
	let priceHTML = "Price per night: <input id=\"roomTypePrice\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#roomtypeform").append(nameHTML);
	$("#roomtypeform").append(descriptionHTML);
	$("#roomtypeform").append(priceHTML);
	$("#roomtypeform").append(submitHTML);	
}