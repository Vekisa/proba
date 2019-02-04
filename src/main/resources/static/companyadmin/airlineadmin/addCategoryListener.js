$(document).on('submit', '#flightseatcategoryform', function(e) {
    e.preventDefault();
    let name = $("#nameCategory").val();
    let price = $("#priceCategory").val();
    let newCategory = new Object();
    newCategory.name = name;
    newCategory.price = price;
	let jsonBody = JSON.stringify(newCategory);
	let url = "/airlines/" + companyId + "/categories";
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

$(document).on('click', '#addCategory', function(e) {
	createSeatCategoryForm();
})

function createSeatCategoryForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"flightseatcategoryform\" method=\"POST\"></form><br>";
	let nameHTML = "Name: <input id=\"nameCategory\" type=\"text\"><br><br>";
	let priceHTML = "Price: <input id=\"priceCategory\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#flightseatcategoryform").append(nameHTML);
	$("#flightseatcategoryform").append(priceHTML);
	$("#flightseatcategoryform").append(submitHTML);
	$("#collection").show();	
}