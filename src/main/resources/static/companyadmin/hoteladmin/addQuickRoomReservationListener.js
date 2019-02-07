$(document).on('submit', '#quickRoomForm', function(e) {
    e.preventDefault();
    let beginTimeString = $("#begin1").val();
	let endTimeString = $("#end1").val();
	let beginTime = new Date(beginTimeString);
	let endTime = new Date(endTimeString);
    let newRoomReservation = new Object();
    newRoomReservation.beginDate = beginTime;
    newRoomReservation.endDate = endTime;
	let jsonBody = JSON.stringify(newRoomReservation);
	let roomId = $("#quickRoomSelect option:selected").val();
	let url = "/room_reservations/quicks?room=" + roomId;
	let reservationId;
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
  		success: function(data) {
  			reservationId = data.id;
  		}
	});
	var selects = document.getElementsByClassName("cbExtraOptions");
    var extOpt = new Array();
    $.each(selects, function(index, select){
            if(select.checked == true)
                extOpt.push(select.value);
    });
    $.ajax({
    	type: "POST",
    	url: "/room_reservations/" + reservationId + "/multiple_extra_options",
		dataType: "json",
		data: JSON.stringify(extOpt),
		contentType: "application/json",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			loadCollection();
  		}
    });
})

$(document).on('click', '#addQuickRoom', function(e) {
	createQuickRoomForm();
})

function createQuickRoomForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"quickRoomForm\" method=\"POST\"></form><br>";
	let beginDateHTML = "<label>Start: </label><input type=\"datetime-local\" id=\"begin1\"><br><br>";
	let endDateHTML = "<label>End: </label><input type=\"datetime-local\" id=\"end1\"><br><br>";
	let selectRoomHTML = "Room: <select id=\"quickRoomSelect\">";
	$.ajax({
		type: "GET",
		url: "/hotels/" + companyId + "/rooms",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
				$.each(data, function(index, room) {
					selectRoomHTML += "<option value=\"" + room.id + "\">" + room.id + "</option>";
				});				
  			}
  		}
	});
	selectRoomHTML += "</select><br><br>";
	let extraOptions = "<div aria-labelledby=\"dropdownMenuLink\" id=\"extraOptionsList\"></div><br>";
	let selectExtraOptionHTML = "";
    $.ajax({
		url: "/hotels/" + companyId + "/extra-options",
		type:"GET",
        async: false,
		success: function(data){
            $.each(data,function(index,extraOption){
              selectExtraOptionHTML += "<input type=\"checkbox\" class=\"cbExtraOptions\" name=\"" + index + "\" value=\"" + extraOption.id + "\"" + index + "\">" + extraOption.description + "  " + extraOption.pricePerDay +"&euro;" + "<br>"; 
            });     
		}
	});
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#quickRoomForm").append(beginDateHTML);
	$("#quickRoomForm").append(endDateHTML);
	$("#quickRoomForm").append(selectRoomHTML);
	$("#quickRoomForm").append(extraOptions);
	$("#quickRoomForm").append(submitHTML);	
	$("#collection").show();
	document.getElementById('extraOptionsList').innerHTML = selectExtraOptionHTML;
}