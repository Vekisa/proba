$(document).on('click','#addRoom',function(e) {
	createRoomForm();
})

$(document).on('click','#income',function(e){
	document.getElementById("collection").innerHTML = "";
})

$(document).on('click','#graph',function(e){
	document.getElementById("collection").innerHTML = "";
})

$(document).on('click', '.update', function(e) {
	let url = "/rooms/" + e.toElement.id;
	createRoomForm();
	let floorNumber = "";
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
				floorNumber = data.number;
  			}
  		}
	});
	let roomTypeName = "";
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
				roomTypeName = data.name;
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
				$("#selectedFloor").val(floorNumber).prop('selected', true);
				$("#selectedType").val(roomTypeName).prop('selected', true);
				$("#numberOfBeds").val(data.numberOfBeds);
  			}
  		}
	});
})

$(document).on('click', '.remove', function(e) {
	let url = e.toElement.id.substring(6);

})

function createRoomForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<form id=\"roomform\"></form>";
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
	let submitHTML = "<input type=\"submit\" value=\"Add\">";
	$("#roomform").append(floorSelectHTML);
	$("#roomform").append(roomTypeSelectHTML);
	$("#roomform").append(numberOfBedsHTML);
	$("#roomform").append(submitHTML);	
}