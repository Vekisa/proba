$(document).on('submit', '#extraoptionform', function(e) {
    e.preventDefault();
    let description = $("#extraOptionDescription").val();
    let price = $("#extraOptionPrice").val();
    let discount = $("#extraOptionDiscount").val();
    let newExtraOption = new Object();
    newExtraOption.description = description;
    newExtraOption.pricePerDay = price;
    newExtraOption.discount = discount;
	let jsonBody = JSON.stringify(newExtraOption);
	let url = "/hotels/" + companyId + "/extra-options";
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

$(document).on('click', '#addOption', function(e) {
	createExtraOptionForm();
})

function createExtraOptionForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"extraoptionform\" method=\"POST\"></form><br>";
	let descriptionHTML = "Description: <input id=\"extraOptionDescription\" type=\"text\"><br><br>";
	let priceHTML = "Price per day: <input id=\"extraOptionPrice\" type=\"text\"><br><br>";
	let discountHTML = "Discount: <input id=\"extraOptionDiscount\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#extraoptionform").append(descriptionHTML);
	$("#extraoptionform").append(priceHTML);
	$("#extraoptionform").append(discountHTML);
	$("#extraoptionform").append(submitHTML);	
}