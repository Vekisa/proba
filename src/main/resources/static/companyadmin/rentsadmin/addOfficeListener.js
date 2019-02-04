$(document).on('submit', '#branchofficeform', function(e) {
    e.preventDefault();
    let address = $("#branchOfficeAddress").val();
    let locationId = $("#branchOfficeLocation option:selected").val();
    let newBranchOffice = new Object();
    newBranchOffice.address = address;
	let jsonBody = JSON.stringify(newBranchOffice);
	let url = "/rent-a-cars/" + companyId + "/branch_offices?location=" + locationId;
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

$(document).on('click', '#addOffice', function(e) {
	createBranchOfficeForm();
})

function createBranchOfficeForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"branchofficeform\" method=\"POST\"></form><br>";
	let addressHTML = "Address: <input id=\"branchOfficeAddress\" type=\"text\"><br><br>";
	let locationHTML = "Location: <select id=\"branchOfficeLocation\">";
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
					locationHTML += "<option value=\"" + location.id + "\">" + location.name + "</option>";
				});				
  			}
  		}
	});
	locationHTML += "</select><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#branchofficeform").append(addressHTML);
	$("#branchofficeform").append(locationHTML);
	$("#branchofficeform").append(submitHTML);	
}