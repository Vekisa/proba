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
	buttonsHTML += 	"<button class=\"btn btn-info btn-block\" id=\"addRoom\"add><span class=\"fa fa-user\"></span> Add </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block\" id=\"incomeHotel\"add><span class=\"fa fa-user\"></span> Income </button>";
	buttonsHTML += 	"<button class=\"btn btn-info btn-block\" id=\"graphHotel\"add><span class=\"fa fa-user\"></span> Graph </button>";
	document.getElementById("buttons").innerHTML = buttonsHTML;
}

function addButtonsForAirline() {
}

function addButtonsForRentACar() {
}