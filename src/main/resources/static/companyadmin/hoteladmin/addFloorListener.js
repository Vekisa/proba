$(document).on('submit', '#floorform', function(e) {
    e.preventDefault();
    let number = $("#floorNumber").val();
    let newFloor = new Object();
    newFloor.number = number;
	let jsonBody = JSON.stringify(newFloor);
	let url = "/hotels/" + companyId + "/floors";
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

$(document).on('click', '#addFloor', function(e) {
	createFloorForm();
})

function createFloorForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"floorform\" method=\"POST\"></form><br>";
	let numberHTML = "Number: <input id=\"floorNumber\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#floorform").append(numberHTML);
	$("#floorform").append(submitHTML);	
}