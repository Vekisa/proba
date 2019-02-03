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
	document.getElementById("buttons").innerHTML = buttonsHTML;
}

function addButtonsForAirline() {
	let buttonsHTML = "";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"addFlight\"add> Add </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"incomeAirline\"add> Income </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block custombutton\" id=\"graphAirline\"add> Graph </button>";
	document.getElementById("buttons").innerHTML = buttonsHTML;
}

function addButtonsForRentACar() {
}