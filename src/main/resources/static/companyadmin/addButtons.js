function addButtons(authority) {
	if(authority == "HOTEL_ADMIN")
		addButtonsForHotel();
	if(authority == "AIRLINE_ADMIN")
		addButtonsForAirline();
	if(authority == "RENT_A_CAR_ADMIN")
		addButtonsForRentACar();
}

function addButtonsForHotel() {
	let buttonsHTML = "";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addRoom\"add> Add </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"incomeHotel\"add> Income </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"graphHotel\"add> Graph </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"updateCompany\"add> Update </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addRoomType\"add> Add type </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addFloor\"add> Add floor </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addOption\"add> Add option </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addQuickRoom\"add> Add quick reservation </button><br>";
	document.getElementById("buttons").innerHTML = buttonsHTML;
}

function addButtonsForAirline() {
	let buttonsHTML = "";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addFlight\"add> Add </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"incomeAirline\"add> Income </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"graphAirline\"add> Graph </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"updateCompany\"add> Update </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addLuggage\"add> Add luggage </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addConfiguration\"add> Add configuration </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addSegment\"add> Add segment </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addCategory\"add> Add category </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"readjustFlight\"add> Readjust flight </button><br>";
	buttonsHTML +=  "<input type=\"number\" id = \"flightv\">" +
						"<input type=\"number\" id = \"confv\"><br>" +
							"<button class=\"btn btn-info btn-block custombutton\" id=\"addConfig\">Add Configuration</button><br>";
	document.getElementById("buttons").innerHTML = buttonsHTML;
}

function addButtonsForRentACar() {
	let buttonsHTML = "";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addVehicle\"add> Add </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"incomeRent\"add> Income </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"graphRent\"add> Graph </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"updateCompany\"add> Update </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addOffice\"add> Add office </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addQuickVehicle\"add> Add quick reservation </button><br>";
	document.getElementById("buttons").innerHTML = buttonsHTML;
}

$(document).on('click','#addConfig',function(e){
	e.preventDefault();
	var flightId = $('#flightv').val();
	var confId = $('#confv').val();
	
	$.ajax({
		type: "POST",
		url: "/flights/" + flightId + "/configuration?configId=" + confId,
		async: false,			
		beforeSend: function(request) {
			request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
		},
		success: function(data) {
			alert("postavoio");
		}
	});
})