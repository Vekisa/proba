$(document).on('click', '#addConfiguration', function(e) {
	let url = "/airlines/" + companyId + "/configurations";
	$.ajax({
		type: "POST",
		url: url,
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		}
	});
})