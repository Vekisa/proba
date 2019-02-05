var vehicleId;

$(document).on('click','#addVehicle',function(e) {
	createVehicleForm();
})

$(document).on('click','#incomeRent',function(e){
	document.getElementById("collection").innerHTML = "";
	let formHTML = "<br><label><strong>Income by date:</strong></label><form id=\"incomeFormRent\" method=\"PUT\"></form>";
	let beginDateHTML = "<br><input type=\"date\" id=\"begin\">-";
	let endDateHTML = "<input type=\"date\" id=\"end\"><br>";
	document.getElementById("collection").innerHTML = formHTML;
	$("#incomeFormRent").append(beginDateHTML);
	$("#incomeFormRent").append(endDateHTML);
	let submitHTML = "<br><input type=\"submit\" value=\"Calculate\"  class=\"btn btn-info btn-block custombutton\"><br>";
	$("#incomeFormRent").append(submitHTML);
})

$(document).on('click','#graphRent',function(e){
	document.getElementById("collection").innerHTML = "";
	let formHTML = "<br><label><strong>Statistic by date:</strong></label><form id=\"statisticFormRent\" method=\"PUT\"></form>";
	let beginDateHTML = "<br><input type=\"date\" id=\"begin\">-";
	let endDateHTML = "<input type=\"date\" id=\"end\"><br>";
	document.getElementById("collection").innerHTML = formHTML;
	$("#statisticFormRent").append(beginDateHTML);
	$("#statisticFormRent").append(endDateHTML);
	let submitHTML = "<br><input type=\"submit\" value=\"Calculate\"  class=\"btn btn-info btn-block custombutton\"><br>";
	$("#statisticFormRent").append(submitHTML);
})

$(document).on('submit', '#incomeFormRent', function(e) {
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
		url: "/rent-a-cars/" + companyId + "/income?begin=" + beginTime.getTime() + "&end=" + endTime.getTime(),
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

$(document).on('submit', '#statisticFormRent', function(e) {
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
		url: "/rent-a-cars/" + companyId + "/statistic?begin=" + beginTime.getTime() + "&end=" + endTime.getTime(),
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

$(document).on('click', '.updateVehicle', function(e) {
	vehicleId = e.toElement.id;
	let url = "/vehicles/" + e.toElement.id;
	createVehicleForm();
	$("#vehicleform").prop("id", "vehicleformupdate");
	let officeId = "";
	$.ajax({
		type: "GET",
		url: url + "/office",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				officeId = data.id;
  			}
  		}
	});
	$.ajax({
		type: "GET",
		url: url,
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$("#brand").val(data.brand);
				$("#model").val(data.model);
				$("#year").val(data.productionYear);
				$("#type").val(data.type);
				$("#capacity").val(data.seatsNumber);
				$("#price").val(data.pricePerDay);
				$("#selectedOffice").val(officeId).prop("selected", true);
  			}
  		}
	});
})

$(document).on('click', '.removeVehicle', function(e) {
	let url = "/vehicles/" + e.toElement.id;
	$.ajax({
		type: "DELETE",
		url: url,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		error: function(data) {
  			alert("Doslo je do greske prilikom brisanja vozila");
  		}
	});
	loadCollection();
})

$(document).on('submit', '#vehicleformupdate', function(e) {
    e.preventDefault();
    let officeId = $("#selectedOffice option:selected").val();
	$.ajax({
		type: "PUT",
		url: "/vehicles/" + vehicleId + "/office?office=" + officeId,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}	
	});
	let newVehicle = new Object();
	newVehicle.pricePerDay = $("#price").val();
	newVehicle.brand = $("#brand").val();
	newVehicle.model = $("#model").val();
	newVehicle.productionYear = $("#year").val();
	newVehicle.type = $("#type").val();
	newVehicle.seatsNumber = $("#capacity").val();
	newVehicle.pricePerDay = $("#price").val();
	let jsonBody = JSON.stringify(newVehicle);
	$.ajax({
		type: "PUT",
		url: "/vehicles/" + vehicleId,
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

$(document).on('submit', '#vehicleform', function(e) {
    e.preventDefault();
    let newVehicle = new Object();
	newVehicle.pricePerDay = $("#price").val();
	newVehicle.brand = $("#brand").val();
	newVehicle.model = $("#model").val();
	newVehicle.productionYear = $("#year").val();
	newVehicle.type = $("#type").val();
	newVehicle.seatsNumber = $("#capacity").val();
	newVehicle.pricePerDay = $("#price").val();
	let jsonBody = JSON.stringify(newVehicle);
    let officeId = $("#selectedOffice option:selected").val();
	$.ajax({
		type: "POST",
		url: "/branch_offices/" + officeId + "/vehicles",
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null)
  				vehicleId = data.id;
  		}
	});
	loadCollection();
})

function createVehicleForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"vehicleform\" method=\"POST\"></form><br>";
	let brandHTML = "Brand: <input id=\"brand\" type=\"text\"><br><br>";
	let modelHTML = "Model: <input id=\"model\" type=\"text\"><br><br>";
	let yearHTML = "Year: <input id=\"year\" type=\"text\"><br><br>";
	let typeHTML = "Type: <input id=\"type\" type=\"text\"><br><br>";
	let capacityHTML = "Capacity: <input id=\"capacity\" type=\"text\"><br><br>";
	let priceHTML = "Price per day: <input id=\"price\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	let officeSelectHTML = "Branch office: <select id=\"selectedOffice\">";
	$.ajax({
		type: "GET",
		url: "/rent-a-cars/" + companyId +"/branch_offices",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, office) {
					officeSelectHTML += "<option value=\"" + office.id + "\">" + office.address + "</option>";
				});				
  			}
  		}
	});
	officeSelectHTML += "</select><br><br>";
	$("#vehicleform").append(brandHTML);
	$("#vehicleform").append(modelHTML);
	$("#vehicleform").append(yearHTML);
	$("#vehicleform").append(typeHTML);
	$("#vehicleform").append(capacityHTML);
	$("#vehicleform").append(priceHTML);
	$("#vehicleform").append(officeSelectHTML);
	$("#vehicleform").append(submitHTML);	
	$("#collection").show();
}