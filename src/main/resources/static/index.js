function load(){
	alert(localStorage.token);
	if(localStorage.token.equals("undefined")){
		alert("upao sam u if");
		window.location.href = 'home.html';
	}
}

$(document).on('click','#home',function(e){
	alert("dsadsadsdsa");
});