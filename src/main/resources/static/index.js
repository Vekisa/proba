function load(){
	alert(localStorage.token);
	if(localStorage.token != undefined){
		window.location.href = 'home.html';
	}
}

$(document).on('click','#home',function(e){
	alert("dsadsadsdsa");
});