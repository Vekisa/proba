$(document).on('submit', '#luggageinfoform', function(e) {
    e.preventDefault();
    let minWeight = $("#minWeightLuggage").val();
    let maxWeight = $("#maxWeightLuggage").val();
    let price = $("#priceLuggage").val();
    let newLuggageInfo = new Object();
    newLuggageInfo.minWeight = minWeight;
    newLuggageInfo.maxWeight = maxWeight;
    newLuggageInfo.price = price;
	let jsonBody = JSON.stringify(newLuggageInfo);
	let url = "/airlines/" + companyId + "/luggageInfos";
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

$(document).on('click', '#addLuggage', function(e) {
	createLuggageInfoForm();
})

function createLuggageInfoForm() {
	document.getElementById("collection").innerHTML = "";
	document.getElementById("collection").innerHTML = "<br><form id=\"luggageinfoform\" method=\"POST\"></form><br>";
	let minWeightHTML = "Minimal weight: <input id=\"minWeightLuggage\" type=\"text\"><br><br>";
	let maxWeightHTML = "Maximal weight: <input id=\"maxWeightLuggage\" type=\"text\"><br><br>";
	let priceHTML = "Price: <input id=\"priceLuggage\" type=\"text\"><br><br>";
	let submitHTML = "<input type=\"submit\" class=\"btn btn-info btn-block custombutton\" value=\"Add\">";
	$("#luggageinfoform").append(minWeightHTML);
	$("#luggageinfoform").append(maxWeightHTML);
	$("#luggageinfoform").append(priceHTML);
	$("#luggageinfoform").append(submitHTML);
	$("#collection").show();	
}