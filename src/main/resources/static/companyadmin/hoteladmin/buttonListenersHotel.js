var roomId;

$(document).on('click','#addRoom',function(e) {
	createRoomForm();
})

$(document).on('click','#incomeHotel',function(e){
	document.getElementById("collection").innerHTML = "";
	let formHTML = "<br><label><strong>Income by date:</strong></label><form id=\"incomeFormHotel\" method=\"PUT\"></form>";
	let beginDateHTML = "<br><input type=\"date\" id=\"begin\">-";
	let endDateHTML = "<input type=\"date\" id=\"end\"><br>";
	document.getElementById("collection").innerHTML = formHTML;
	$("#incomeFormHotel").append(beginDateHTML);
	$("#incomeFormHotel").append(endDateHTML);
	let submitHTML = "<br><input type=\"submit\" value=\"Calculate\"  class=\"btn btn-info btn-block custombutton\"><br>";
	$("#incomeFormHotel").append(submitHTML);
})

$(document).on('click','#graphHotel',function(e){
	document.getElementById("collection").innerHTML = "";
	let formHTML = "<br><label><strong>Statistic by date:</strong></label><form id=\"statisticFormHotel\" method=\"PUT\"></form>";
	let beginDateHTML = "<br><input type=\"date\" id=\"begin\">-";
	let endDateHTML = "<input type=\"date\" id=\"end\"><br>";
	document.getElementById("collection").innerHTML = formHTML;
	$("#statisticFormHotel").append(beginDateHTML);
	$("#statisticFormHotel").append(endDateHTML);
	let submitHTML = "<br><input type=\"submit\" value=\"Calculate\"  class=\"btn btn-info btn-block custombutton\"><br>";
	$("#statisticFormHotel").append(submitHTML);
})

$(document).on('click', '.updateRoom', function(e) {
	roomId = e.toElement.id;
	let url = "/rooms/" + e.toElement.id;
	createRoomForm();
	$("#roomform").prop("id", "roomformupdate");
	let floorId = "";
	$.ajax({
		type: "GET",
		url: url + "/floor",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				floorId = data.id;
  			}
  		}
	});
	let roomTypeId = "";
	$.ajax({
		type: "GET",
		url: url + "/room-type",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				roomTypeId = data.id;
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
				$("#selectedFloor").val(floorId).prop("selected", true);
				$("#selectedType").val(roomTypeId).prop("selected", true);
				$("#numberOfBeds").val(data.numberOfBeds);
  			}
  		}
	});
})

$(document).on('click', '.removeRoom', function(e) {
	let url = "/rooms/" + e.toElement.id;
	$.ajax({
		type: "DELETE",
		url: url,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		error: function(data) {
  			alert("Doslo je do greske prilikom brisanja sobe");
  		}
	});
	loadCollection();
})

$(document).on('submit', '#incomeFormHotel', function(e) {
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
		url: "/hotels/" + companyId + "/income?begin=" + beginTime.getTime() + "&end=" + endTime.getTime(),
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

$(document).on('submit', '#statisticFormHotel', function(e) {
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
		url: "/hotels/" + companyId + "/statistic?begin=" + beginTime.getTime() + "&end=" + endTime.getTime(),
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

$(document).on('submit', '#roomformupdate', function(e) {
    e.preventDefault();
    let typeId = $("#selectedType option:selected").val();
    let gotError = false;
 	$.ajax({
		type: "PUT",
		url: "/rooms/" + roomId + "/type?roomtype=" + typeId,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}
	});
	let floorId = $("#selectedFloor option:selected").val();
	$.ajax({
		type: "PUT",
		url: "/rooms/" + roomId + "/floor?floor=" + floorId,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}	
	});
	let newRoom = new Object();
	newRoom.numberOfBeds = $("#numberOfBeds").val();
	let jsonBody = JSON.stringify(newRoom);
	$.ajax({
		type: "PUT",
		url: "/rooms/" + roomId,
		dataType: "json",
		data: jsonBody,
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}	
	});
	if(gotError) alert("Neuspesna promena sobe!");
	loadCollection();
})

$(document).on('submit', '#roomform', function(e) {
    e.preventDefault();
	let floorId = $("#selectedFloor option:selected").val();
	if(floorId != undefined) {
	    let newRoom = new Object();
		newRoom.numberOfBeds = $("#numberOfBeds").val();
		let jsonBody = JSON.stringify(newRoom);
		$.ajax({
			type: "POST",
			url: "/floors/" + floorId + "/rooms",
			dataType: "json",
			data: jsonBody,
			contentType: "application/json",
			async: false,
			beforeSend: function(request) {
	    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
	  		},
			success: function(data) {
				if(data != null)
					roomId = data.id;
			}
		});
	    let typeId = $("#selectedType option:selected").val();
	    if(typeId != undefined)
		 	$.ajax({
				type: "PUT",
				url: "/rooms/" + roomId + "/type?roomtype=" + typeId,
				async: false,
				beforeSend: function(request) {
		    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		  		}
			});
	}
	loadCollection();
})

function createRoomForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"roomform\" method=\"POST\"></form><br>";
	let floorSelectHTML = "Floor number: <select id=\"selectedFloor\">";
	$.ajax({
		type: "GET",
		url: "/hotels/" + companyId +"/floors",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, floor) {
					floorSelectHTML += "<option value=\"" + floor.id + "\">" + floor.number + "</option>";
				});				
  			}
  		}
	});
	floorSelectHTML += "</select><br><br>";
	let roomTypeSelectHTML = "Room type: <select id=\"selectedType\">";
		$.ajax({
		type: "GET",
		url: "/hotels/" + companyId +"/roomtypes",
		dataType: "json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, roomType) {
					roomTypeSelectHTML += "<option value=\"" + roomType.id + "\">" + roomType.name + "</option>";
				});				
  			}
  		}
	});
	roomTypeSelectHTML += "</select><br><br>";
	let numberOfBedsHTML = "Number of beds: <input id=\"numberOfBeds\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#roomform").append(floorSelectHTML);
	$("#roomform").append(roomTypeSelectHTML);
	$("#roomform").append(numberOfBedsHTML);
	$("#roomform").append(submitHTML);	
	$("#collection").show();
}