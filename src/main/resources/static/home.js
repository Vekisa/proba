/*
    vehicleC - vozilo u korpi
    vehicleStartC - pocetak rezervacije za auto
    vehicleEndC - kraj rezervacije za auto
    
    roomC - soba u korpi
    roomStartC - pocetak rezervacije za sobu
    roomEndC - kraj rezervacije za sobu
    extraOptionsC - extra optioni za room
    
    flightC - let
    seatsC - lista sedista
   
*/
var currentData;
var currentUser;
var airlineRate;
var flightRate;
var rentACarRate;
var vehicleRate;
var hotelRate;
var roomRate;
var points = 0;

function clearShoppingCart(){
    localStorage.setItem("vehicleC", null);
    localStorage.setItem("vehicleStartC", null);
    localStorage.setItem("vehicleEndC", null);
    localStorage.setItem("roomC", null);
    localStorage.setItem("roomStartC", null);
    localStorage.setItem("roomEndC", null);
    localStorage.setItem("seatsC", null);
    localStorage.setItem("flightC", null);
    localStorage.setItem("extraOptionsC",null);
    
    $('#suggestedItems').hide();
    $('#quickResVehicle').hide();
    $('#quickResRoom').hide();
}


function load(){
		$.ajax({
			type: "GET",
			url: "users/registered/currentUser",
			beforeSend: function(xhr) {
		          if(localStorage.token) {
		            xhr.setRequestHeader('X-Auth-Token', localStorage.token);	            
		          }
		    },
			success: function(data){
				if(data!=null){
					$('#userName').text(data.username);
					currentUser = data;
				}
			}
		});
    
	$('#profile').hide();
	$("#myMap").hide();
	$('#profdiv').hide();
    
    if(localStorage.getItem('flightC') == null || localStorage.getItem('flightC') == "null" || localStorage.getItem('flightC') == ""){
        $('#suggestedItems').hide();
        $('#quickResVehicle').hide();
        $('#quickResRoom').hide();
    }
}

function setMapLocation(long, lat){
    var lonLat = new OpenLayers.LonLat(long, lat)
          .transform(
            new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
            map.getProjectionObject() // to Spherical Mercator Projection
          );
          
    var zoom=12;

    var markers = new OpenLayers.Layer.Markers( "Markers" );
    map.addLayer(markers);
    
    markers.addMarker(new OpenLayers.Marker(lonLat));
    
    map.setCenter(lonLat, zoom);
}

$(document).on('click','#userName',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").hide();
    $('#profdiv').hide();
	
	$('#usernameP').text(currentUser.username);
	$('#firstNameP').text(currentUser.firstName);
	$('#lastNameP').text(currentUser.lastName);
	$('#emailP').text(currentUser.email);
	$('#cityP').text(currentUser.city);
	$('#phoneNumberP').text(currentUser.phoneNumber);
	
	$("#profile").show();
	$('#friendsBtn').val(currentUser._links.friends.href);
	$('#friendsReqBtn').val(currentUser._links.friend_requests.href);
	$('#reservationHisBtn').val(currentUser._links.reservations_history.href);
	$('#activeResBtn').val(currentUser._links.active_reservations.href);
	
});

$(document).ready(function(){
    $(document).on('change','.reservationDates',function(e){
        let startDate = $('#startDate').val();
        let endDate = $('#endDate').val();
          
        if(startDate != "" && endDate != ""){
            let s = new Date(startDate);
            let e = new Date(endDate);
            var path = "";
            if($("#addDate").val() == "room"){
                path = "rooms";
            }else{
                path = "vehicles";
            }
            $.ajax({
                url: "/"+ path +"/" + $('#cancelBooking').val() + "/is-free?begin=" + s.getTime() + "&end=" + e.getTime(),
                type:"GET",
                async: false,
                success: function(data){
                    if(data == true){
                        $("#okIndicator").show();
                        $("#errorIndicator").hide();
                        $("#addDate").prop( "disabled", false );
                    }else{
                        $("#addDate").prop( "disabled", true );
                        $("#okIndicator").hide();
                        $("#errorIndicator").show(); 
                    }
                        
		        }
	       });
        }
    });
    
    $(document).on('change','#luggageSelect',function(e){
        //alert("promenio se");
        //alert($(this).val());
        //alert($(this).attr("class"));
        
    });
    
    $(document).on('change','#seatSelect',function(e){
        //alert("promenio se 2");
        //alert($(this).val());
        //alert($(this).attr("class"));
        $.ajax({
		  url: "/seats/" + $(this).val() + "/luggageInfo",
		  type:"GET",
		  success: function(data){
              if(data == null){
                  //alert("nemaaaaa");
                  $('#luggageSelect').val(-1);
              }else{
                  //alert("imaaaaa " + data.id);
                $('#luggageSelect').val(data.id);
              }           
		  }
	   });
    });
});

$(document).on('click','#addLuggageInfo',function(e){
    //alert("/seats/" + $('#seatSelect').val() + "/luggageInfo?luggageId="+ $('#luggageSelect').val());
    $.ajax({
		  url: "/seats/" + $('#seatSelect').val() + "/luggageInfo?luggageId="+ $('#luggageSelect').val(),
		  type:"PUT",
		  success: function(data){   
              //alert("postavio");
		  }
	});
});


$(document).on('click','#addDate',function(e){
    e.preventDefault();
    if($('#endDate').val() == "" ||  $('#startDate').val() == ""){
        $("#error").html("Choose dates!");
        $('#myModal').modal("show");
    }else{
        if($('#addDate').val() == "room"){
            $.ajax({
                url: "/rooms/"+$('#cancelBooking').val(),
                type:"GET",
                async: false,
                success: function(data){
                    localStorage.setItem('roomC', data.id);
                }
            });
            
            var selects = document.getElementsByClassName("cbExtraOptions");
            var extOpt = new Array();
            $.each(selects, function(index, select){
                    if(select.checked == true)
                        extOpt.push(select.value);
            });
            
            localStorage.setItem("extraOptionsC",JSON.stringify(extOpt));  
            localStorage.setItem('roomStartC', $('#startDate').val());
            localStorage.setItem('roomEndC', $('#endDate').val());
            $('#dateModal').modal('toggle');
        }else if($('#addDate').val() == "vehicle"){         
            $.ajax({
                url: "/vehicles/"+$('#cancelBooking').val(),
                type:"GET",
                async: false,
                success: function(data){
                    localStorage.setItem('vehicleC', data.id);
                }
            });

            localStorage.setItem('vehicleStartC', $('#startDate').val());
            localStorage.setItem('vehicleEndC', $('#endDate').val());
            $('#dateModal').modal('toggle');
        }
    }
});

$(document).on('click','#allUsers',function(e){
	e.preventDefault();
	printAllUsers("firstName");
});

function printAllUsers(sort){
     $('#tablediv').show();
    $('#profdiv').hide();
    $('#profile').hide();
    
	var allUsers;
	var friends;
    var sentRequests;
    var receivedRequests;
    
	$.ajax({
		url: "/users/registered",
		type:"GET",
		async: false,
		success: function(data){
            allUsers = data;
		}
	});
    
    var indexPom;
    
    $.each(allUsers, function(index, user) {
        if(user.username == currentUser.username)
            indexPom = index;
    });
    
	allUsers.splice(indexPom, 1);
    
    if(sort == "lastName"){
        allUsers.sort(SortByLastName);
    }else{
        allUsers.sort(SortByFirstName);
    }
    
	$.ajax({
		url: currentUser._links.friends.href,
		type:"GET",
		async: false,
		success: function(data){
            friends = data;
		}
	});
    
    $.ajax({
		url: currentUser._links.sent_friend_request.href,
		type:"GET",
		async: false,
		success: function(data){
            sentRequests = data;
		}
	});
    
    $.ajax({
		url: currentUser._links.friend_requests.href,
		type:"GET",
		async: false,
		success: function(data){
            receivedRequests = data;
		}
	});
	
	printUsers(allUsers,friends,sentRequests, receivedRequests);
}

function userInList(userN,data){
    var a = 0;
	$.each(data, function(index, user) {
		if(user.username == userN){
            a = 1;
        }
	});
    
    if(a == 1){
        return true;
    }else{
        return false;
    }
}

function userInListFriendRequests(userN,data){
    var a = 0;
    $.each(data, function(index, fr) {
        if(fr.sender.username == userN){
            a = 1;
        }
    });
    
    if(a == 1){
        return true;
    }else{
        return false;
    }
}

$(document).on('click','.addFriend',function(e){
    e.preventDefault();
    $(this).prop("disabled",true);
	$.ajax({
		url: $(this).val(),
		type:"POST",
		success: function(data){
            //alert("poslao");      
		}
	});
});

$(document).on('click','.unFriend',function(e){
    e.preventDefault();
    $(this).prop("disabled",true);
	$.ajax({
		url: $(this).val(),
		type:"DELETE",
		success: function(data){
            //alert("Izbacio iz prijatelja");      
		}
	});
});

$(document).on('click','.cancelFr',function(e){
     e.preventDefault();
    $(this).prop("disabled",true);
	$.ajax({
		url: $(this).val(),
		type:"DELETE",
		success: function(data){
            //alert("Odustao od zahteva");      
		}
	});
});

function printUsers(allUsers,friends,sentRequests, receivedRequests){
    var users = "";
    var button = "";
    var valueBtn ="";
    var link = "";
    var pomButton = "";
    var pomValueBtn = "";
    var pomLink = "";
    
	if(allUsers!=null){
       
		$.each(allUsers, function(index, user) {
            pomButton = "";
            if(friends != null && userInList(user.username,friends)){
                link = currentUser._links.remove_friend.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-danger unFriend\">UnFriend</button>"; 
            }else if(sentRequests != null && userInList(user.username,sentRequests)){
                link = currentUser._links.cancel_friend_request.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-warning cancelFr\">Cancel</button>";    
            }else if(receivedRequests != null && userInListFriendRequests(user.username,receivedRequests)){
            	//alert("tu sam");
                link = currentUser._links.accept_request.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-success btnAcceptReq\">Accept</button>";  
                
                pomLink = currentUser._links.decline_request.href;
                pomValueBtn = pomLink.substring(0, pomLink.length-1) + user.id;
                pomButton = "<button value = \"" + pomValueBtn +"\" type=\"button\" class=\"btn btn-danger btnDeclineReq\">Decline</button>"; 
            }else{
                link = currentUser._links.send_request.href;
                valueBtn = link.substring(0, link.length-1) + user.id;
                button = "<button value = \"" + valueBtn +"\" type=\"button\" class=\"btn btn-primary addFriend\">Add Friend</button>";
            }
			users += "<tr>" + "<td scope=\"col\">" + user.username + "</td>" + "<td scope=\"col\">"+ user.firstName + "</td>" + "<td scope=\"col\">" + user.lastName + "</td>" +
			"<td scope=\"col\">" + user.email + "</td>" + "<td scope=\"col\">" + user.city + "</td>" + "<td scope=\"col\">" + button + pomButton + "</td>" + "</tr>";
		});
	}
	
	document.getElementById("tablediv").innerHTML = 
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Username</th>" +
	    "<th scope=\"col\" id =\"firstNameSort\">First Name</th>" +
	    "<th scope=\"col\"  id =\"lastNameSort\">Last Name</th>" +
	    "<th scope=\"col\">Email</th>" +
        "<th scope=\"col\">City</th>" +
		"</tr>" +
		"</thead><tbody>" + users + "</tbody></table>";
}

$(document).on('click','#firstNameSort',function(e){
    e.preventDefault();
    printAllUsers("firstName");
});

$(document).on('click','#lastNameSort',function(e){
    e.preventDefault();
    printAllUsers("lastName");
});

//Booking
$(document).on('click','.bookvehicle',function(e){
    $("#cancelBooking").val($(this).val());
    $("#errorIndicator").hide();
    $("#okIndicator").hide();
    $('#addDate').val("vehicle");
    $('#startDate').val("");
    $('#endDate').val("");
    $('#dropDown').hide();
    $("#dateModal").modal();
    $("#addDate").prop( "disabled", false );
});

$(document).on('click','.bookflight',function(e){
    document.getElementById("seats").innerHTML = "";
    loadSeatsChartFor($(this).val());
    $("#cancelBookingSeats").val($(this).val());
	$("#flightModal").modal();
});

$(document).on('click','.bookRoom',function(e){
    $("#cancelBooking").val($(this).val());
    $("#errorIndicator").hide();
    $("#okIndicator").hide();
    $('#addDate').val("room");
    $('#startDate').val("");
    $('#endDate').val("");
    
    let extraOptions = "";
    $.ajax({
		url: currentData._links.hotel_extra_options.href,
		type:"GET",
        async: false,
		success: function(data){
            $.each(data,function(index,extraOption){
              extraOptions += "<input type=\"checkbox\" class=\"cbExtraOptions\" name=\"" + index + "\" value=\"" + extraOption.id + "\"" + index + "\">" + extraOption.description + "  " + extraOption.pricePerDay +"&euro;" + "<br>"; 
            });     
		}
	});
    
    document.getElementById('extraOptionsList').innerHTML = extraOptions;
    
    $('#dropDown').show();
	$("#dateModal").modal();
    $("#addDate").prop( "disabled", false );
});

//-----------

$(document).on('click','#clearCart',function(e){
    clearShoppingCart();
    document.getElementById("tablediv").innerHTML = "<button type=\"button\" class=\"btn btn-danger\" id=\"clearCart\">Clear</button>" + "<h1><strong>Shopping Cart</strong></h1>";
    $('#suggestedItems').hide();
    $('#quickResVehicle').hide();
    $('#quickResRoom').hide();
})

function printSC(){
    
    $("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	$("#myMap").hide();
    
    var vehicle = localStorage.getItem("vehicleC");
    var vehicleStartDate = localStorage.getItem("vehicleStartC");
    var vehicleEndDate = localStorage.getItem("vehicleEndC");
    var room = localStorage.getItem("roomC");
    var roomStartDate = localStorage.getItem("roomStartC");
    var roomEndDate = localStorage.getItem("roomEndC");
    var flight = localStorage.getItem("flightC");
    var seats = JSON.parse(localStorage.getItem("seatsC"));
    var a = 0;
    var b = 0;
    var buttonBook = "<hr>" + "<button type=\"button\" class=\"btn btn-success\" id=\"bookNow\">Book Now</button>" ;
    var airline;
    
    var vehicleHTML = "";
    var select = "";
    if(vehicle != null && vehicle != "null" && vehicle != undefined){   
        $.ajax({
            type: "GET",
            url: "/vehicles/" + vehicle,
            dataType: "json",
            async: false,
            success: function(data){
                 vehicleHTML = "<hr>" + "<strong>Vehicle</strong>" + "<hr>" + "<table class=\"table\" id=\"tablescvehicle\"><thead>" +
                    "<tr>" + 
                    "<th scope=\"col\">Brand</th>" +
                    "<th scope=\"col\">Model</th>" +
                    "<th scope=\"col\">Type</th>" +
                    "<th scope=\"col\">Seats Number</th>" +
                    "<th scope=\"col\">Price Per Day</th>" +
                    "<th scope=\"col\">Begin Date</th>" +
                    "<th scope=\"col\">End Date</th>" +
                    "</tr>" +
                "</thead><tbody>" +
                     "<tr>" + "<td scope=\"col\">"+ data.brand + "</td>" 
                    + "<td scope=\"col\">" + data.model + "</td>"
                    + "<td scope=\"col\">" + data.type + "</td>"
                    + "<td scope=\"col\">" + data.seatsNumber + "</td>"
                    + "<td scope=\"col\">" + data.pricePerDay + "</td>"
                    + "<td scope=\"col\">" + vehicleStartDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + vehicleEndDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + "<button type=\"button\" class=\"btn btn-danger\" id=\"removeVehicleFromSC\">Remove</button>" + "</td>"
                    + "</tr>"     
                + "</tbody></table>";
            }
        });
    }
    
    var roomHTML = "";
    var extraOptionsHTML = "<hr>" + "<strong>Extra Options</strong>" + "<hr>" + "<table class=\"table\" id=\"tablescextraoption\"><thead>" +
                    "<tr>" + 
                    "<th scope=\"col\">Description</th>" +
                    "<th scope=\"col\">Price Per Day</th>" +
                    "</tr>" +
                "</thead><tbody>"     
                
    if(room != null && room != "null" && room != undefined){
        var extOptions = JSON.parse(localStorage.getItem("extraOptionsC"))
        
        $.each(extOptions,function(index,extOptId){
            $.ajax({
                url: "/extra_options/" + extOptId,
                type:"GET",
                async: false,
                success: function(data){
                        b = 1;
                        extraOptionsHTML += "<tr>" + "<td scope=\"col\">" + data.description + "</td>" + "<td scope=\"col\">" + data.pricePerDay + "</td>" + "</tr>";
                        
                }
            }); 
        });
        
            
        extraOptionsHTML += "</tbody></table>";
        
         $.ajax({
            type: "GET",
            url: "/rooms/" + room,
            dataType: "json",
            async: false,
            success: function(data){
                 roomHTML = "<hr>" + "<strong>Room</strong>" + "<hr>" + "<table class=\"table\" id=\"tablescroom\"><thead>" +
                    "<tr>" + 
                    "<th scope=\"col\">Room Type</th>" +
                    "<th scope=\"col\">Floor</th>" +
                    "<th scope=\"col\">Number Of Beds</th>" +
                    "<th scope=\"col\">Price Per Day</th>" +
                    "<th scope=\"col\">Begin Date</th>" +
                    "<th scope=\"col\">End Date</th>" +
                    "</tr>" +
                "</thead><tbody>" +
                     "<tr>" + "<td scope=\"col\">"+ data.roomType.name + "</td>" 
                    + "<td scope=\"col\">" + data.floor.number + "</td>"
                    + "<td scope=\"col\">" + data.numberOfBeds + "</td>"
                    + "<td scope=\"col\">" + data.roomType.pricePerNight + "</td>"
                    + "<td scope=\"col\">" + roomStartDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + roomEndDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + "<button type=\"button\" class=\"btn btn-danger\" id=\"removeRoomFromSC\">Remove</button>" + "</td>"
                    + "</tr>"     
                + "</tbody></table>";
            }
        });
    }
    
    var flightHTML = "";
    var seatsStr = "";
    if(flight != null && flight != "null" && flight != undefined){
        a = 1;
        
        $.each(seats,function(index,seat){
           seatsStr += seat + " "; 
        });
        
        $.ajax({
            type: "GET",
            url: "/flights/" + flight,
            dataType: "json",
            async: false,
            success: function(data){
                 flightHTML = "<hr>" + "<strong>Flight</strong>" + "<hr>" + "<table class=\"table\" id=\"tablescflight\"><thead>" +
                    "<tr>" + 
                    "<th scope=\"col\">Start Destination</th>" +
                    "<th scope=\"col\">Finish Destination</th>" +
                    "<th scope=\"col\">Transfers</th>" +
                    "<th scope=\"col\">Departure Time</th>" +
                    "<th scope=\"col\">Arrival Time </th>" + 
                    "<th scope=\"col\">Flight Length</th>" +
                    "<th scope=\"col\">Base Price</th>" +
                    "<th scope=\"col\">Seats</th>" +
                    "</tr>" +
                "</thead><tbody>" +
                     "<tr>" + "<td scope=\"col\">"+ data.startDestination.name + "</td>" 
                    + "<td scope=\"col\">" + data.finishDestination.name + "</td>"
                    + "<td scope=\"col\">" + data.transfers + "</td>"
                    + "<td scope=\"col\">" + data.departureTime.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + data.arrivalTime.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + data.flightLength + "</td>"
                    + "<td scope=\"col\">" + data.basePrice + "</td>"
                    + "<td scope=\"col\">" + seatsStr + "</td>"
                    + "<td scope=\"col\">" + "<button type=\"button\" class=\"btn btn-danger\" id=\"removeFlightFromSC\">Remove</button>" + "</td>"
                    + "</tr>"     
                + "</tbody></table>";
                airline = data.airline.id;
            }
        });
        
        var luggageSelect = "<option value=\"-1\"></option>";
        
        $.ajax({
            type: "GET",
            url: "/airlines/" + airline + "/luggageInfos",
            async: false,
            success: function(data){
                //alert("dobio luggage");
                $.each(data,function(index, lugg){
                   luggageSelect += "<option value=\"" + lugg.id + "\">" + lugg.minWeight + "kg - " + lugg.maxWeight + "kg  " + lugg.price + "&euro;&nbsp;&nbsp;</option>"
                });
            }
        });
        
        var seatsSelect = "";
        
        $.each(seats,function(index,seat){
            seatsSelect += "<option value=\"" + seat + "\">" + seat +"</option>";
        });
    
        select = "<label>Seat:&nbsp;</label><select style=\"width: 150px\" id=\"luggageSelect\" class=\"" + 10 + "\"> " + luggageSelect  + "</select><br><label>Luggage:&nbsp;</label><select id=\"seatSelect\" style=\"width: 50px\" class=\"" + 20 + "\">" + seatsSelect + 
            "</select><br>"
            + "<button type=\"button\" class=\"btn btn-primary\" id=\"addLuggageInfo\">Set</button>";
        
    }
    
    var inputPoints="";
    
    if(a != 0){
         $.ajax({
            type: "GET",
            url: "/users/registered/" + currentUser.id + "/bonus_points",
            async: false,
            success: function(data){
                points = data;
                //alert("poeni" + data);
                inputPoints = "<label>Bonus Points:</label><br><input type=\"number\" id =\"pointsInput\" name=\"points\" min=\"0\" max=\"" + data  +"\" step=\"1\" value=\"0\">";
            }
        });
    }
    
    
    if(a == 0){
        if(b == 0){
	       document.getElementById("tablediv").innerHTML = "<button type=\"button\" class=\"btn btn-danger\" id=\"clearCart\">Clear</button>" + "<h1><strong>Shopping Cart</strong></h1>" + flightHTML +  vehicleHTML + roomHTML;
        }else{
             document.getElementById("tablediv").innerHTML = "<button type=\"button\" class=\"btn btn-danger\" id=\"clearCart\">Clear</button>" + "<h1><strong>Shopping Cart</strong></h1>" + flightHTML +  vehicleHTML + roomHTML + extraOptionsHTML;
        }
    }else{
        if(b == 0){
            document.getElementById("tablediv").innerHTML = "<button type=\"button\" class=\"btn btn-danger\" id=\"clearCart\">Clear</button>" + "<h1><strong>Shopping Cart</strong></h1>" + flightHTML +  vehicleHTML + roomHTML + "<hr><br>" + select + "<hr>" + inputPoints + buttonBook + "<hr>";
        }else{
            document.getElementById("tablediv").innerHTML = "<button type=\"button\" class=\"btn btn-danger\" id=\"clearCart\">Clear</button>" + "<h1><strong>Shopping Cart</strong></h1>" + flightHTML +  vehicleHTML + roomHTML + extraOptionsHTML + "<hr>" + select + "<hr>" + inputPoints + buttonBook  + "<hr>";
        }
    }
    if(flight != null && flight != "null" && flight != undefined){
        $.ajax({
              url: "/seats/" + $('#seatSelect').val() + "/luggageInfo",
              type:"GET",
              success: function(data){
                  if(data == null){
                      //alert("nemaaaaa");
                      $('#luggageSelect').val(-1);
                  }else{
                      //alert("imaaaaa " + data.id);
                    $('#luggageSelect').val(data.id);
                  }           
              }
           });
    }
}

$(document).on('click','#shoppingcart',function(e){
	e.preventDefault();
    printSC();
	
});

$(document).on('click','#removeVehicleFromSC', function(e) {
    e.preventDefault();
    localStorage.setItem("vehicleC", null);
    localStorage.setItem("vehicleStartC", null);
    localStorage.setItem("vehicleEndC", null);
    printSC();
});

$(document).on('click','#removeRoomFromSC', function(e) {
    e.preventDefault();
    localStorage.setItem("roomC", null);
    localStorage.setItem("roomStartC", null);
    localStorage.setItem("roomEndC", null);
    localStorage.setItem("extraOptionsC",null);
    printSC();
});

$(document).on('click','#removeFlightFromSC', function(e) {
    e.preventDefault();
    localStorage.setItem("seatsC", null);
    localStorage.setItem("flightC", null);
    $('#suggestedItems').hide();
    $('#quickResVehicle').hide();
    $('#quickResRoom').hide();
    printSC();
});


$(document).on('click','#bookNow', function() {
    var seats = localStorage.getItem("seatsC");
    var vehicle = localStorage.getItem("vehicleC");
    var vehicleStartDate = localStorage.getItem("vehicleStartC");
    var vehicleEndDate = localStorage.getItem("vehicleEndC");
    var room = localStorage.getItem("roomC");
    var roomStartDate = localStorage.getItem("roomStartC");
    var roomEndDate = localStorage.getItem("roomEndC");
    var seats = JSON.parse(localStorage.getItem("seatsC"));
    var extraOptions = JSON.parse(localStorage.getItem("extraOptionsC"));
    var reservation;
    var roomReservation;
    var vehicleReservation;
    
    //kreira kartu i rezervaciju i vraca je
    $.ajax({
		type: "POST",
		url: "/tickets",
		contentType: "application/json",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
		success: function(data){
            reservation = data;
            //alert("dobio nazad rezervaciju " + reservation.id)
        }
    });
    
    //dodaje sedista u  rezervaciju
    //alert("tickets/" + reservation.ticket.id + "/seats");
    //alert(seats);
    $.ajax({
		type: "POST",
		url: "tickets/" + reservation.ticket.id + "/seats",
        data: JSON.stringify(seats),
		contentType: "application/json",
        dataType: "json",
        async: false,
		success: function(data){
            //alert("dodao sedista");
        }
    });
    
    //dodaje vozilo u rezervaciju
    if(vehicle !== "null" && vehicle !== undefined){
         let s = new Date(vehicleStartDate);
         let e = new Date(vehicleEndDate);
         $.ajax({
            type: "POST",
            url: "vehicle-reservations/create?vehicleId=" + vehicle + "&beginDate=" + s.getTime() + "&endDate=" + e.getTime(),
            contentType: "application/json",
            async: false,
            success: function(data){
                vehicleReservation = data;
                //alert("napravio vehicle reservation");
                $.ajax({
                    type: "POST",
                    url: "/reservations/" + reservation.id + "/set-vehicle-reservation/" + vehicleReservation.id,
                    contentType: "application/json",
                    async: false,
                    success: function(data){
                        //alert("povezao vozilo");
                    }
                });
            }
            });
        
            
    }
    
    //pravi rezervaciju sobe i dodaje je u glavnu
    if(room !== "null" && room !== undefined){
        let s = new Date(roomStartDate);
        let e = new Date(roomEndDate);
        //alert("usao u room");
        $.ajax({
            type: "POST",
            url: "room_reservations/create-with-room/" + room + "?begin=" + s.getTime() + "&end=" + e.getTime(),
            contentType: "application/json",
            async: false,
            success: function(data){
                roomReservation = data;
                //alert("napravio room reservation");
                //alert(reservation.id + " " + roomReservation.id);
                 $.ajax({
                    type: "POST",
                    url: "/reservations/" + reservation.id + "/set-room-reservation/" + roomReservation.id, 
                    dataType: "json", 
                    async: false,
                    success: function(data){
                        //alert("povezao sobu"); 
                    }
                });
            }
        });
        
        //alert(extraOptions);
        if(extraOptions != null && extraOptions != "null" && extraOptions != ""){
        //alert("postavljam ih");
            $.ajax({
                type: "POST",
                url: "/room_reservations/" + roomReservation.id + "/multiple_extra_options", 
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(extraOptions),
                async: false,
                success: function(data){
                    //alert("ubacio extra optione"); 
                }
            });
        }
        
       
    }
    
    
    var pointsUser = 0;
    
    if($('#pointsInput').val() > points){
        pointsUser = points;
    }else{
        pointsUser = $('#pointsInput').val();
    }
    
    
    $.ajax({
        type: "GET",
        url: "users/registered/currentUser",
        beforeSend: function(xhr) {
            if (localStorage.token) {
              xhr.setRequestHeader('X-Auth-Token', localStorage.token);	            
            }
        },
        success: function(data){
            if(data!=null){
                //alert("vratio usera");
                $.ajax({
                    type: "POST",
                    url: "/reservations/" + reservation.id + "/user?user=" + data.id + "&points=" + pointsUser, 
                    contentType: "application/json",
                    async: false,
                    success: function(data2){
                        //alert("postavio usera"); 
                    }
                });
            }
        }
    });
    
    clearShoppingCart();
    printSC();
    
});

$(document).on('click','.tableRoomType tr', function() {
    var searchLink = $(this).attr('id');
    
   $.ajax({
		type: "GET",
		url: searchLink,
		dataType: "json",
		success: function(data){
            printRooms(data);
        }
   });
});

function printRooms(data){
    printCompany(currentData,"hotel");
    var rooms = "";
    $.each(data, function(index, room) {
		rooms += "<tr>" + "<td scope=\"col\">"+ room.numberOfBeds + "</td>" 
		+ "<td scope=\"col\">" + room.floor.number + "</td>"
        + "<td scope=\"col\">" + createRating(room.rating) + "</td>"
        + "<td scope=\"col\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-success bookRoom\" value = \"" + room.id + "\">Add To Cart</button></td>"
		+ "</tr>";
	});
	
	var tabela = "<table class=\"table\" id=\"tablerooms\"><thead>" +
				"<tr>" + 
			    "<th scope=\"col\">Number Of Beds</th>" +
			    "<th scope=\"col\">Floor</th>" +
                "<th scope=\"col\">Rating</th>" +
				"</tr>" +
			"</thead><tbody>" + rooms + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML +=  "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary cataloguebtn\">Catalogue</button></div></div>" + 
		"<hr>" + "<strong>Rooms</strong>" + "<hr>" + tabela;
}

$(document).ajaxError(function( event, jqxhr, settings, thrownError ) {
	  
    switch(jqxhr.status) {
	    case 404:
	    	 $("#error").html("Not found");
             $('#myModal').modal("show");
	      break;
	    case 204:
	    	$("#error").html("No content");
            $('#myModal').modal("show");
	      break;
	    case 400:
	    	$("#error").html("Bad request");
            $('#myModal').modal("show");
	      break;
	    case 500:
	    	$("#error").html("Something went wrong");
            $('#myModal').modal("show");
    }
    
});

$.ajaxSetup({
        cache:false,
        beforeSend: function (xhr){ 
            if (localStorage.token) {
		            xhr.setRequestHeader('X-Auth-Token', localStorage.token);
            } 
        }
});

$(document).on('click','#logout',function(e){
	e.preventDefault();
	$.ajax({
		url: "/users/registered/logout",
		type:"POST",
		async: false,
		beforeSend: function (xhr){ 
            if (localStorage.token) {
		            xhr.setRequestHeader('X-Auth-Token', localStorage.token);	            
            } 
        },
		success: function(data){
			localStorage.token = null;
            clearShoppingCart();
			window.location.href = 'index.html';		
		}
	});
});

$(document).on('click','#home',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	document.getElementById("tablediv").innerHTML = "<img height=\"700\" width=\"700\" src=\"home.png\" alt=\"Travel\">";
});

$(document).on('click','.backToProf',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").hide();
	$("#profdiv").hide();
	
	$('#usernameP').text(currentUser.username);
	$('#firstNameP').text(currentUser.firstName);
	$('#lastNameP').text(currentUser.lastName);
	$('#emailP').text(currentUser.email);
	$('#cityP').text(currentUser.city);
	$('#phoneNumberP').text(currentUser.phoneNumber);
	
	$("#profile").show();
	$('#friendsBtn').val(currentUser._links.friends.href);
	$('#friendsReqBtn').val(currentUser._links.friend_requests.href);
	$('#reservationHisBtn').val(currentUser._links.reservations_history.href);
	$('#activeResBtn').val(currentUser._links.active_reservations.href);
});

$(document).on('click','#airline',function(e){
	e.preventDefault();
	$("#tablediv").show();
	$("#myMap").hide();
	$("#profile").hide();
	$('#profdiv').hide();
	$.ajax({
		type: "GET",
		url: "airlines?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#airbtn',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$.ajax({
		type: "GET",
		url: "airlines?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#rentacar',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	$.ajax({
		type: "GET",
		url: "rent-a-cars?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#rentbtn',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$.ajax({
		type: "GET",
		url: "rent-a-cars?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#hotel',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
	$.ajax({
		type: "GET",
		url: "hotels?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#hotbtn',function(e){
	e.preventDefault();
	$("#myMap").hide();
	$.ajax({
		type: "GET",
		url: "hotels?page=0&size=10",
		dataType: "json",
		success: function(data){
			if(data!=null){
				printListOfCompanies(data);
			}
		}
	});
});

$(document).on('click','#bos',function(e){
	$("#myMap").hide();
	e.preventDefault();
	printCompany(currentData,"rentacar");
	printBranchOffices(currentData);
});

function printListOfCompanies(data){
	var s = "";
	$.each(data, function(index, company) {
		s += "<tr id = \"" + company.links[0].href + "\">" + "<th scope=\"row\">"+ index +"</th>" + "<td>" + company.name + "</td>" + "<td>"+ company.address
		+ "</td>" + "<td>" + company.description + "</td><td>" +  createRating(company.rating) + "</td></tr>";
	});
	document.getElementById("tablediv").innerHTML = "<table class=\"table table-hover\" id = \"table\"><thead>" +
	    		"<tr>" + 
	      		  "<th scope=\"col\">#</th>" +
			      "<th scope=\"col\">Name</th>" +
			      "<th scope=\"col\">Address</th>" +
			      "<th scope=\"col\">Description</th>" +
                  "<th scope=\"col\">Rating</th>" +
	    		"</tr>" +
	  		"</thead><tbody>" + s + "</tbody></table>";
}

$(document).on('click','#friendsBtn',function(e){
	e.preventDefault();
	var pomDataFriends;
	 $.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomDataFriends = data;
			}
	 });
	 
	 printFriends(pomDataFriends);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	  
});

$(document).on('click','#friendsReqBtn',function(e){
	e.preventDefault();
	var pomData;
	 $.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			} 
	 });
	 
	 printFriendRequests(pomData);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	 
}); 

$(document).on('click','#reservationHisBtn',function(e){
	e.preventDefault();
	var pomData;
	$.ajax({
			type: "GET",
			url: $(this).val(),
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			}
	 });
	 
	 printReservationHistory(pomData);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
	 
});

$(document).on('click','#activeResBtn',function(e){
	e.preventDefault();
    printActRes();
});

function printActRes(){
    var pomData;
    
    $.ajax({
			type: "GET",
			url: "/users/registered/" + currentUser.id + "/activeReservations",
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			}
	 });
	 
	 printActiveReservations(pomData);
	 
	 $('#profdiv').show();
	 $('#profile').hide();
}

function printFriends(data){
	var friends = "";
    var link = currentUser._links.remove_friend.href;
    var valueBtn = link.substring(0, link.length-1);
    
	if(data!=null){
		$.each(data, function(index, friend) {
			friends += "<tr>" + "<td scope=\"col\">" + friend.username + "</td>" + "<td scope=\"col\">"+ friend.firstName + "</td>" + "<td scope=\"col\">" + friend.lastName + "</td>" +
			"<td scope=\"col\">" + friend.email + "</td>" + "<td scope=\"col\">"  + "<button value = \"" + valueBtn + friend.id + "\" type=\"button\" class=\"btn btn-danger unFriend\">UnFriend</button>"  +"</td>" + "</tr>";
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Username</th>" +
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Email</th>" +
		"</tr>" +
		"</thead><tbody>" + friends + "</tbody></table>";
}

$(document).on('click','.btnAcceptReq',function(e){
    e.preventDefault();
    $(this).prop("disabled",true);
    $(this).next().prop("disabled",true);
    $.ajax({
		type: "POST",
		url: $(this).val(),
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				//alert("prihvatio");
                
			}
		}
	});
});

$(document).on('click','.btnDeclineReq',function(e){
    e.preventDefault();
    $(this).prop("disabled",true);
    $(this).prev().prop("disabled",true);
    $.ajax({
		type: "DELETE",
		url: $(this).val(),
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				//alert("odustao");
			}
		}
	});
});

function printFriendRequests(data){
	var friendRequests = "";
    var linkAccept = "";
    var linkDecline = "";
    var valueBtnAccept = "";
    var valueBtnDecline = ""; 
    
	if(data!=null){
		$.each(data, function(index, friendRequest) {
            linkAccept = currentUser._links.accept_request.href;
            valueBtnAccept = linkAccept.substring(0, linkAccept.length-1) + friendRequest.sender.id;
            linkDecline = currentUser._links.decline_request.href;
            valueBtnDecline = linkDecline.substring(0, linkDecline.length-1) + friendRequest.sender.id;
            
			friendRequests += "<tr>" + "<td scope=\"col\">" + friendRequest.sender.firstName + "</td>" + "<td scope=\"col\">" + friendRequest.sender.lastName + "</td>" +
			"<td scope=\"col\">" + friendRequest.requestTime.substring(0, 19).replace('T', '<br>') + "</td>"
            + "<td scope=\"col\">" + "<button value=\"" + valueBtnAccept + "\" type=\"button\" class=\"btn btn-success btnAcceptReq\">Accept</button>"
            + "<button value=\"" + valueBtnDecline + "\" type=\"button\" class=\"btn btn-danger btnDeclineReq\">Decline</button>" + "</td>"
            + "</tr>";
		});
	}
    
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Request Time</th>" +
		"</tr>" +
		"</thead><tbody>" + friendRequests + "</tbody></table>";
}

function printReservationHistory(data){
	var history = "";
	var name = "";
	if(data!=null){
		$.each(data, function(index, reservation) {
			 $.ajax({
					type: "GET",
					url:  reservation.links[1].href,
					dataType: "json",
					async: false,
					success: function(data){
						name = data.name;
					}
			 });
			 
			history += "<tr>" + "<td scope=\"col\">" + reservation.beginDate.substring(0, 19).replace('T', '<br>') + "</td>" + "<td scope=\"col\">"+ reservation.endDate.substring(0, 19).replace('T', '<br>') + "</td>" +
			"<td scope=\"col\">" + name + "</td>" + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-warning viewReservationHis\">View</button>" + "</td>" + "</tr>";
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Begin Date</th>" +
	    "<th scope=\"col\">End Date</th>" +
	    "<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + history + "</tbody></table>";
}

function printActiveReservations(data){
	var history = "";
	var name = "";
    var vehicle = "";
    var button = "";
    //alert("tu sam");
    
	if(data!=null){
        //alert("imam sta da prikazem");
		$.each(data, function(index, reservation) {
			 $.ajax({
					type: "GET",
					url:  reservation.links[1].href,
					dataType: "json",
					async: false,
					success: function(data){
						name = data.name;
					}
			 });
            
            if(reservation.ticket.numberOfSeats > 1){
                button = "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-primary inviteReservation\">Invite</button>";
            }else{
                button = "";
            }
			 
			history += "<tr>" + "<td scope=\"col\">" + reservation.beginDate.substring(0, 19).replace('T', '<br>') + "</td>" + "<td scope=\"col\">"+ reservation.endDate.substring(0, 19).replace('T', '<br>') + "</td>" +
			"<td scope=\"col\">" + name + "</td>"
            + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-warning viewReservation\">View</button>" + "</td>"
            + "<td scope=\"col\">" + button + "</td>"
            + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-info passengers\">Passengers</button>" + "</td>"
            + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-danger cancelReservation\">Cancel</button>" + "</td>"
            + "</tr>";
            
		});
	}
	
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Begin Date</th>" +
	    "<th scope=\"col\">End Date</th>" +
	    "<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + history + "</tbody></table>";
}


$(document).on('click','.passengers',function(e){
    e.preventDefault();
    var id = $(this).val();
    
    printPass(id);
    
    $('#smallAddBtn').val(id);
    $('#modalPassengers').modal();
});

function printPass(id){
    
    $('#addPassFields').show();
    
    $.ajax({
        type: "GET",
        url:  "/reservations/" + id + "/free-seats",
        dataType: "json",
        async: false,
        success: function(data){
            if(data == ""){  
                $('#addPassFields').hide();
            }
        }
	 }); 
    
    $.ajax({
        type: "GET",
        url:  "/reservations/" + id + "/passengers",
        dataType: "json",
        async: false,
        success: function(data){
            $("#closePassengers").val(id);
            printPassengers(data);
        }
	 }); 
}

function printPassengers(data){
    var passengers = "";
    
    $.each(data,function(index, passenger){
        if(passenger.user == null || passenger.user == "null"){
            passengers += "<tr>" + "<td scope=\"col\">" + passenger.firstName + "</td>" + "<td scope=\"col\">" + passenger.lastName + "</td>" + "<td scope=\"col\">" + passenger.passportNumber + "</td>" 
                + "<td scope=\"col\">" + "<button value=\"" + passenger.id  + "\" style=\"margin: 10px\" type=\"button\" class=\"btn btn-danger removePassenger\">Remove</button>" + "</td>"
                + "</tr>";
        }else{
            passengers += "<tr>" + "<td scope=\"col\">" + passenger.firstName + "</td>" + "<td scope=\"col\">" + passenger.lastName + "</td>" + "<td scope=\"col\">" + passenger.passportNumber + "</td>" + "</tr>";  
        }
    });
    
    document.getElementById('passengers').innerHTML = "<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
	    "<th scope=\"col\">Passport Number</th>" +
		"</tr>" +
		"</thead><tbody>" + passengers + "</tbody></table>";
}

$(document).on('click','.removePassenger',function(e){
    var reservationId = $("#closePassengers").val();
    var passengerId = $(this).val();
    //alert("Brisem iz rezervacije: " + reservationId + " passengera: " + passengerId);
     $.ajax({
        type: "POST",
        url:  "/reservations/" + reservationId + "/delete_passenger/" + passengerId,
        dataType: "json",
        async: false,
        success: function(data){
            printPass(reservationId);
        }
	 }); 
});

$(document).on('click','#smallAddBtn',function(e){
    e.preventDefault();
    var reservationId = $(this).val();
    //alert("/seats/" + reservationId + "/passenger_values?firstName=" + $('#smallFN').val() + "&lastName=" + $('#smallLN').val() +"&passport=" + $('#smallN').val());
    $.ajax({
        type: "PUT",
        url:  "/seats/" + reservationId + "/passenger_values?firstName=" + $('#smallFN').val() + "&lastName=" + $('#smallLN').val() +"&passport=" + $('#smallN').val(),
        dataType: "json",
        async: false,
        success: function(data){
            printPass(reservationId);
        }
	 }); 
});

$(document).on('click','.viewReservationHis',function(e){
     var id = $(this).val();
    var reservation;
    var flight;
    $.ajax({
        type: "GET",
        url:  "/reservations/" + id,
        dataType: "json",
        async: false,
        success: function(data){
            reservation = data;
        }
	 }); 
    
    var htmlCode = ""
    
    if(reservation.ticket != null && reservation.ticket != "null"){
        
        $.ajax({
            type: "GET",
            url:  "/tickets/" + reservation.ticket.id + "/flight",
            dataType: "json",
            async: false,
            success: function(data){
                flight = data;
            }
	   }); 
        flightRate = flight.id;
        airlineRate = flight.airline.id;
        htmlCode += "<hr>" + 
            "<strong>Flight</strong>" +
            "<hr>" + "<pre><strong>From:</strong> " + flight.startDestination.name + "</pre>" + 
                "<pre><strong>Destination:</strong> " + flight.finishDestination.name + "</pre>" +
                "<pre><strong>Number Of Seats:</strong> " + reservation.ticket.numberOfSeats + "</pre>" +
                "<pre><strong>Departure Time:</strong> " + flight.departureTime.substring(0, 19).replace('T', ' ') + "</pre>" +
                "<pre><strong>Arrival Time:</strong> " + flight.arrivalTime.substring(0, 19).replace('T', ' ') + "</pre>" +
                "<pre><strong>Price:</strong> " + reservation.ticket.price + "&euro;</pre>" +
                "<pre><strong>Rate:</strong> " +
                    "<input type=\"radio\" name=\"flight\" value=\"1\">1&nbsp;" +
                    "<input type=\"radio\" name=\"flight\" value=\"2\">2&nbsp;" +
                    "<input type=\"radio\" name=\"flight\" value=\"3\" checked>3&nbsp;" +
                    "<input type=\"radio\" name=\"flight\" value=\"4\">4&nbsp;" +
                    "<input type=\"radio\" name=\"flight\" value=\"5\">5&nbsp;" +
                    "<button value =\"" + flight.airline.id + "\" style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id=\"rateFlightBtn\">Rate!</button><br>" +
                    "<input type=\"checkbox\" name=\"cbRateAirline\" value=\"Bike\" id =\"cbRateAirline\"> Airline<br>" + 
                    "<input type=\"checkbox\" name=\"cbRateFlight\" value=\"Bike\" id =\"cbRateFlight\"> Flight<br>"
                + "</pre>" ;
    }
    
    if(reservation.vehicleReservation != null && reservation.vehicleReservation != "null"){
        rentACarRate = reservation.vehicleReservation.vehicle.branchOffice.rentACar.id;
        vehicleRate = reservation.vehicleReservation.vehicle.id;
        htmlCode += "<hr>" + 
            "<strong>Vehicle</strong>" +
            "<hr>" + "<pre><strong>Brand:</strong> " + reservation.vehicleReservation.vehicle.brand + "</pre>" +
                "<pre><strong>Model:</strong> " + reservation.vehicleReservation.vehicle.model + "</pre>" +
                "<pre><strong>Type:</strong> " + reservation.vehicleReservation.vehicle.type + "</pre>" +
                "<pre><strong>Begin Date:</strong> " + reservation.vehicleReservation.beginDate.substring(0, 19).replace('T', ' ') + "</pre>" + 
                "<pre><strong>End Date:</strong> " + reservation.vehicleReservation.endDate.substring(0, 19).replace('T', ' ') + "</pre>" +
                "<pre><strong>Price:</strong> " + reservation.vehicleReservation.price + "&euro;</pre>" +
                "<pre><strong>Rate:</strong> " +
                    "<input type=\"radio\" name=\"vehicle\" value=\"1\">1&nbsp;" +
                    "<input type=\"radio\" name=\"vehicle\" value=\"2\">2&nbsp;" +
                    "<input type=\"radio\" name=\"vehicle\" value=\"3\" checked>3&nbsp;" +
                    "<input type=\"radio\" name=\"vehicle\" value=\"4\">4&nbsp;" +
                    "<input type=\"radio\" name=\"vehicle\" value=\"5\">5&nbsp;" +
                    "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id=\"rateVehicleBtn\">Rate!</button><br>" +
                    "<input type=\"checkbox\" name=\"cbRateRent\" value=\"Bike\" id=\"cbRateRent\"> Rent A Car<br>" + 
                    "<input type=\"checkbox\" name=\"cbRateCar\" value=\"Bike\" id=\"cbRateCar\"> Vehicle<br>"
                + "</pre>";
    }
    
    if(reservation.roomReservation != null && reservation.roomReservation != "null"){
        hotelRate = reservation.roomReservation.room.floor.hotel.id;
        roomRate = reservation.roomReservation.room.id;
        htmlCode += "<hr>" + 
            "<strong>Room</strong>" +
            "<hr>" + "<pre><strong>Hotel:</strong> " + reservation.roomReservation.room.floor.hotel.name + "</pre>" +
                    "<pre><strong>Room Type:</strong> " + reservation.roomReservation.room.roomType.description + "</pre>" + 
                    "<pre><strong>Begin Date:</strong> " + reservation.roomReservation.beginDate.substring(0, 19).replace('T', ' ') + "</pre>" + 
                    "<pre><strong>End Date:</strong> " + reservation.roomReservation.endDate.substring(0, 19).replace('T', ' ') + "</pre>" +
                    "<pre><strong>Price:</strong> " + reservation.roomReservation.price + "&euro;</pre>" + 
                    "<pre><strong>Rate:</strong> " +
                    "<input type=\"radio\" name=\"room\" value=\"1\">1&nbsp;" +
                    "<input type=\"radio\" name=\"room\" value=\"2\">2&nbsp;" +
                    "<input type=\"radio\" name=\"room\" value=\"3\" checked>3&nbsp;" +
                    "<input type=\"radio\" name=\"room\" value=\"4\">4&nbsp;" +
                    "<input type=\"radio\" name=\"room\" value=\"5\">5&nbsp;" +
                    "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id=\"rateRoomBtn\">Rate!</button><br>" +
                    "<input type=\"checkbox\" name=\"cbRateHotel\" value=\"Bike\" id=\"cbRateHotel\"> Hotel<br>" + 
                    "<input type=\"checkbox\" name=\"cbRateRoom\" value=\"Bike\" id=\"cbRateRoom\"> Room<br>"
                + "</pre>";
;
    }
    
    document.getElementById("reservation").innerHTML = htmlCode;
    
    $('#reservationModal').modal();
    
})

$(document).on('click','#rateRoomBtn',function(e){
    var rating = $('[name=room]:checked').val();
    
    //alert("/users/registered/" + currentUser.id + "/rate-room?room=" + roomRate + "&rating=" + rating);
    
    if($('#cbRateHotel').is(':checked')){
        $.ajax({
            type: "POST",
            url:  "/users/registered/" + currentUser.id + "/rate-hotel?hotel=" + hotelRate + "&rating=" + rating,
            async: false,
            success: function(data){
                //alert("hotel ocenjen");
            }
	   }); 
    }
    
    if($('#cbRateRoom').is(':checked')){
        $.ajax({
            type: "POST",
            url:  "/users/registered/" + currentUser.id + "/rate-room?room=" + roomRate + "&rating=" + rating,
            async: false,
            success: function(data){
                //alert("room ocenjen");
            }
	   }); 
    }
    
});

$(document).on('click','#rateVehicleBtn',function(e){
    var rating = $('[name=vehicle]:checked').val();
    
    //alert("/users/registered/" + currentUser.id + "/rate-vehicle?vehicle=" + vehicleRate + "&rating=" + rating);
    
    if($('#cbRateRent').is(':checked')){
        //alert("ocenjujem vehicle")
        $.ajax({
            type: "POST",
            url:  "/users/registered/" + currentUser.id + "/rate-rent-a-car?rentACar=" + rentACarRate + "&rating=" + rating,
            async: false,
            success: function(data){
                //alert("rent ocenjen");
            }
	   }); 
    }
    
    if($('#cbRateCar').is(':checked')){
        $.ajax({
            type: "POST",
            url:  "/users/registered/" + currentUser.id + "/rate-vehicle?hotel=" + vehicleRate + "&rating=" + rating,
            async: false,
            success: function(data){
                //alert("vehicle ocenjen");
            }
	   }); 
    }
    
});

$(document).on('click','#rateFlightBtn',function(e){
    var rating = $('[name=flight]:checked').val();
    //alert("/users/registered/" + currentUser.id + "/rate-airline?airline=" + $(this).val() + "&rating=" + rating);
    
    if($('#cbRateAirline').is(':checked')){
        $.ajax({
            type: "POST",
            url:  "/users/registered/" + currentUser.id + "/rate-airline?airline=" + airlineRate + "&rating=" + rating,
            async: false,
            success: function(data){
                //alert("airline ocenjen");
            }
	   }); 
    }
    
    if($('#cbRateFlight').is(':checked')){
        $.ajax({
            type: "POST",
            url:  "/users/registered/" + currentUser.id + "/rate-flight?flight=" + flightRate + "&rating=" + rating,
            async: false,
            success: function(data){
                //alert("flight ocenjen");
            }
	   }); 
    }
});

$(document).on('click','.viewReservation',function(e){
    var id = $(this).val();
    var reservation;
    var flight;
    $.ajax({
        type: "GET",
        url:  "/reservations/" + id,
        dataType: "json",
        async: false,
        success: function(data){
            reservation = data;
        }
	 }); 
    
    var htmlCode = ""
    
    if(reservation.ticket != null && reservation.ticket != "null"){
        
        $.ajax({
            type: "GET",
            url:  "/tickets/" + reservation.ticket.id + "/flight",
            dataType: "json",
            async: false,
            success: function(data){
                flight = data;
            }
	   }); 
        
        htmlCode += "<hr>" + 
            "<strong>Flight</strong>" +
            "<hr>" + "<pre><strong>From:</strong> " + flight.startDestination.name + "</pre>" + 
                "<pre><strong>Destination:</strong> " + flight.finishDestination.name + "</pre>" +
                "<pre><strong>Number Of Seats:</strong> " + reservation.ticket.numberOfSeats + "</pre>" +
                "<pre><strong>Departure Time:</strong> " + flight.departureTime.substring(0, 19).replace('T', ' ') + "</pre>" +
                "<pre><strong>Arrival Time:</strong> " + flight.arrivalTime.substring(0, 19).replace('T', ' ') + "</pre>" +
                "<pre><strong>Price:</strong> " + reservation.ticket.price + "&euro;</pre>";
    }
    
    if(reservation.vehicleReservation != null && reservation.vehicleReservation != "null"){
        htmlCode += "<hr>" + 
            "<strong>Vehicle</strong>" +
            "<hr>" + "<pre><strong>Brand:</strong> " + reservation.vehicleReservation.vehicle.brand + "</pre>" +
                "<pre><strong>Model:</strong> " + reservation.vehicleReservation.vehicle.model + "</pre>" +
                "<pre><strong>Type:</strong> " + reservation.vehicleReservation.vehicle.type + "</pre>" +
                "<pre><strong>Begin Date:</strong> " + reservation.vehicleReservation.beginDate.substring(0, 19).replace('T', ' ') + "</pre>" + 
                "<pre><strong>End Date:</strong> " + reservation.vehicleReservation.endDate.substring(0, 19).replace('T', ' ') + "</pre>" +
                "<pre><strong>Price:</strong> " + reservation.vehicleReservation.price + "&euro;</pre>";
    }
    
    if(reservation.roomReservation != null && reservation.roomReservation != "null"){
        htmlCode += "<hr>" + 
            "<strong>Room</strong>" +
            "<hr>" + "<pre><strong>Hotel:</strong> " + reservation.roomReservation.room.floor.hotel.name + "</pre>" +
                    "<pre><strong>Room Type:</strong> " + reservation.roomReservation.room.roomType.description + "</pre>" + 
                    "<pre><strong>Begin Date:</strong> " + reservation.roomReservation.beginDate.substring(0, 19).replace('T', ' ') + "</pre>" + 
                    "<pre><strong>End Date:</strong> " + reservation.roomReservation.endDate.substring(0, 19).replace('T', ' ') + "</pre>" +
                    "<pre><strong>Price:</strong> " + reservation.roomReservation.price + "&euro;</pre>";
;
    }
    
    document.getElementById("reservation").innerHTML = htmlCode;
    
    $('#reservationModal').modal();
});

$(document).on('click','.inviteReservation',function(e){
    //e.preventDefault();
    var id = $(this).val();
    $('#friendsModal').modal();
    
    $.ajax({
        type: "GET",
        url:  "/users/registered/" + currentUser.id + "/friends",
        dataType: "json",
        async: false,
        success: function(data){
            printFriendsToInvite(data);
        }
	 }); 
});

$(document).on('click','.cancelReservation',function(e){
    e.preventDefault();
    var reservationId = $(this).val();
    //alert("odustajem");
    
     $.ajax({
        type: "POST",
        url:  "/reservations/" + reservationId +"/cancel?user=" + currentUser.id,
        dataType: "json",
        async: false,
        success: function(data){
            //alert("odustao");
            printActRes();   
        }
	 }); 
});

function printFriendsToInvite(data){
    
    var friendsHTML = "";
    
    $.each(data,function(index,friend){
        friendsHTML += "<tr>" + "<td scope=\"col\">" + friend.firstName + "</td>" + "<td scope=\"col\">" + friend.lastName + "</td>"
            + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + friend.id +"\" type=\"button\" class=\"btn btn-primary inviteFriend\">Invite</button>" + "</td>"
            + "</tr>";
    });
    
    
    var code = "<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">First Name</th>" +
	    "<th scope=\"col\">Last Name</th>" +
		"</tr>" +
		"</thead><tbody>" + friendsHTML + "</tbody></table>";
    
     document.getElementById("friends").innerHTML = code;
}

$(document).on('click','.inviteFriend',function(e){
    e.preventDefault();
    var friendId = $(this).val();
    var friendList = new Array();
    friendList.push(friendId);
    
    //alert("/reservations/" + currentUser.id + "/invite");
    //alert(friendList);
    
    $.ajax({
        type: "POST",
        url:  "/reservations/" + currentUser.id + "/invite",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(friendList),
        async: false,
        success: function(data){
            //alert("poslao invite");
        }
	 });
    
});

$(document).on('click','#destbtn',function(e){
	$("#myMap").hide();
	e.preventDefault();
	printCompany(currentData,"airline");
	printDestinations(currentData);
});

$(document).on('click','#flightsbtn',function(e){
	$("#myMap").hide();
	e.preventDefault();
	printCompany(currentData,"airline");
	printFlights(currentData);
});

$(document).on('click','.cataloguebtn',function(e){
	e.preventDefault();
	printCompany(currentData,"hotel");
	printHotelCatalogue(currentData);
});

$(document).on('click','#extraopt',function(e){
	$("#myMap").hide();
	e.preventDefault();
	printCompany(currentData,"hotel");
	printExtraOptions(currentData);
});

function printExtraOptions(data){
	 $.ajax({
			type: "GET",
			url:  data._links.hotel_extra_options.href,
			dataType: "json",
			async: false,
			success: function(data){
					pomDataExtraOptions = data;
			}
	 });
	 
	 var extraOptions = "";
	 if(pomDataExtraOptions != null){
		 $.each(pomDataExtraOptions, function(index, extraOption) {
				extraOptions += "<tr><td scope=\"col\">" + extraOption.description + "</td>" + "<td>" + extraOption.pricePerDay + "</td></tr>";
		});
	 }
	 
	 var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Description</th>" +
	    "<th scope=\"col\">Price per day(&euro;)</th>" +
		"</tr>" +
	"</thead><tbody>" + extraOptions + "</tbody></table>";
	 
	 document.getElementById("tablediv").innerHTML += "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary cataloguebtn\">Catalogue</button></div></div>" +
	 "<hr>" + 
		"<strong>Extra Options</strong>" +
		"<hr>" + tabela;
}

function printDestinations(data){
	var pomDataDestinations;
	 $.ajax({
			type: "GET",
			url:  data._links.destinations.href,
			dataType: "json",
			async: false,
			success: function(data){
					pomDataDestinations = data;
			}
	});
	 
	 var destinations = "";
    
	 if(pomDataDestinations != null){
		 $.each(pomDataDestinations, function(index, destination) {
				destinations += "<tr><td scope=\"col\">" + destination.name + "</td></tr>";
		});
	 }
	 
	 var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Name</th>" +
	    "<th scope=\"col\">Brand</th>" +
	    "<th scope=\"col\">Model</th>" +
	    "<th scope=\"col\">Seats number</th>" +
	    "<th scope=\"col\">Type</th>" +
		"</tr>" +
	"</thead><tbody>" + destinations + "</tbody></table>";
	 
	 document.getElementById("tablediv").innerHTML += "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"flightsbtn\">Flights</button></div></div>" +
	 "<hr>" + 
		"<strong>Destinations</strong>" +
		"<hr>" + tabela;
	 
	 
}

$(document).on('click','#tablebo tr', function() {
	var branchOfficeLink = $(this).attr('id');
	var pomData;
	var pomDataVehicles;
	$.ajax({
		type: "GET",
		url: branchOfficeLink,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomData = data;
			}
		}
	});
	
	$.ajax({
		type: "GET",
		url: pomData._links.location.href,
		dataType: "json",
		success: function(data){
			if(data!=null){
				$.ajax({
					type: "GET",
					url: data._links.long_lat.href,
					dataType: "json",
					success: function(data2){
						if(data2!=null){
							setMapLocation(data2.lon, data2.lat);
						}
					}
				});
			}
		}
	});
	
	$.ajax({
		type: "GET",
		url: pomData._links.vehicles.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomDataVehicles = data;
			}
		}
	});
	
	$("#myMap").show();
	
	var vehicles = "";
	
	$.each(pomDataVehicles, function(index, vehicle) {
		vehicles += "<tr>" + "<td scope=\"col\">"+ vehicle.brand 
		+ "<td scope=\"col\">" + vehicle.model + "</td>"
		+ "<td scope=\"col\">"+ vehicle.seatsNumber + "</td>" 
		+ "<td scope=\"col\">"+ vehicle.type + "</td>"
		+ "<td scope=\"col\">"+ vehicle.pricePerDay + "</td>"
        + "<td scope=\"col\">"+ createRating(vehicle.rating) + "</td>"
		+ "<td scope=\"col\"><button id = \"" + vehicle.links[1].href +"\" type=\"button\" class=\"btn btn-success bookvehicle\" value = \"" + vehicle.id +"\">Add To Cart</button></td>" 
		+ "</td></tr>";
	});
	
	var tabela = "<table class=\"table table-hover\" id = \"tableveh\"><thead>" +
				"<tr>" + 
			    "<th scope=\"col\">Brand</th>" +
			    "<th scope=\"col\">Model</th>" +
			    "<th scope=\"col\">Seats number</th>" +
			    "<th scope=\"col\">Type</th>" +
			    "<th scope=\"col\">Price per day(&euro;)</th>" +
                "<th scope=\"col\">Rating</th>" +
			    "<th scope=\"col\"></th>" +
				"</tr>" +
			"</thead><tbody>" + vehicles + "</tbody></table>";
	
	printCompany(currentData,"rentacar");
	document.getElementById("tablediv").innerHTML +=  "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"bos\">Branch offices</button></div></div>" + 
		"<hr>" + "<strong>Vehicles of branch office on address \"" + pomData.address + "\"</strong>" + "<hr>" + tabela;
});

$(document).on('click','#table tr', function() {
	var companyLink = $(this).attr('id');
	$.ajax({
		type: "GET",
		url: companyLink,
		dataType: "json",
		success: function(data){
			if(data!=null){
				currentData = data;
				if(companyLink.includes("airlines")){
					printCompany(data,"airline");
					printFlights(data);
				}else if(companyLink.includes("rent-a-cars")){
					printCompany(data,"rentacar");
					printBranchOffices(data);
				}else if(companyLink.includes("hotels")){
					printCompany(data,"hotel");
					printHotelCatalogue(data);
				}
			}
		}
	});
});

function printCompany(data, company){
	var pomData;
	
	if(company!="rentacar"){
		$.ajax({
			type: "GET",
			url: data._links.location.href,
			dataType: "json",
			async: false,
			success: function(data){
				if(data!=null){
					pomData = data;
				}
			}
		});
		
		$.ajax({
			type: "GET",
			url: pomData._links.long_lat.href,
			dataType: "json",
			success: function(data){
				if(data!=null){
					setMapLocation(data.lon,data.lat);
				}
			}
		});
		$("#myMap").show();
	}
	
	var header = "";	
	var image = "";
	var buttonTitle = "";
	var buttonTitle2= "";
	var buttonId = "";
	
	if(company == "airline"){
		image = "air-plane-cartoon.png";
		header = "Flights";
		buttonTitle = "Airlines";
		buttonTitle2 = "Destinations";
		buttonId = "airbtn";
		buttonId2 = "destbtn";
	}else if(company == "rentacar"){
		image = "rent-a-car-cartoon.png";
		header = "Branch offices";
		buttonTitle = "Rent a cars"
		buttonTitle2 = "Nesto";
		buttonId = "rentbtn";
		buttonId2 = "nesto";
	}else if(company == "hotel"){
		image = "hotel-cartoon.png";
		header = "Catalogue";
		buttonTitle = "Hotels"
		buttonTitle2 = "Nesto";
		buttonId = "hotbtn";
		buttonId2 = "nesto";
	}
	
	var code = "<div class=\"container\">" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<img height=\"170\" width =\"200\" src=\"" + image +"\" class = \"profimg\" alt=\"airplane\">" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<strong style=\"font-size:40px;\">" + data.name +"</strong>" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<p>" + data.address +"</p>" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
							"<p>" + data.description +"</p>" +
						"</div>" +
					"</div>" +
					"<div class=\"row\">" + 
						"<div class=\"col-sm\">" +
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star checked\"></span>" + 
						"<span class=\"fa fa-star\"></span>" + 
						"</div>" +
					"</div>" +
					"<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"" + buttonId + "\">" + buttonTitle + "</button></div></div>";
	
	
						
	document.getElementById("tablediv").innerHTML = code ;
}

function printHotelCatalogue(data){
	var pomData;
	var link = "";
	$.ajax({
		type: "GET",
		url: data._links.hotelcatalogue.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				link = data._links.catalogue_room_types.href;
			}
		}
	});
	
	$.ajax({
		type: "GET",
		url: link,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomData = data;
			}
		}
	});
	
	var catalogue = "";
    var linkId = "";
	$.each(pomData, function(index, roomType) {
        linkId = currentData._links.search.href;
        linkId = linkId.replace("{roomTypeId}",roomType.id);
		catalogue += "<tr id = \"" + linkId +"\"><td scope=\"col\">" + roomType.name + "</td>" + "<td scope=\"col\">"+ roomType.description 
		+ "</td>" + "<td scope=\"col\">" + roomType.pricePerNight + "</td></tr>";
	});
	
	document.getElementById("tablediv").innerHTML += "<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"extraopt\">Extra Options</button></div></div>" + "<div>" + 
	"<hr>" + 
	"<strong>Catalogue</strong>" +
	"<hr>" + "<table class=\"table table-hover tableRoomType\"><thead><tr><th scope=\"col\">Type</th><th scope=\"col\">Description</th><th scope=\"col\">Price per night(&euro;)</th></tr></thead><tbody>" + catalogue + "</tbody></table>" ;
}

function printFlights(data){
	var pomDataFlights;
	$.ajax({
		type: "GET",
		url: data._links.airline_flights.href,
		dataType: "json",
		async: false,
		success: function(data){			
				pomDataFlights = data;
		}
	});
	
	var flights = "";
	var s = "";
	if(pomDataFlights!=null){
		$.each(pomDataFlights, function(index, flight) {
			s += "<tr>" + "<td>" + flight.startDestination.name + "</td>" + "<td>"+ flight.finishDestination.name 
			+ "</td>" + "<td>" + flight.transfers + "</td><td>" + flight.departureTime.substring(0, 19).replace('T', '<br>') + "</td><td>" + flight.arrivalTime.substring(0, 19).replace('T', '<br>') + "</td><td>"
			+ flight.flightLength + "</td><td>" + flight.basePrice + "</td><td>" + flight.tripType +"</td><td>" + createRating(flight.rating) + "</td>"
			+ "<td scope=\"col\"><button id = \"" + flight.links[0].href +"\" type=\"button\" class=\"btn btn-success bookflight\" value = \"" + flight.id +"\">Add to cart</button></td>"
			+"</tr>";
		});
	}
	
	flights ="<div class=\"row\"><div class=\"col-sm\"><button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary\" id =\"destbtn\">Destinations</button></div></div>" + "<div>" + 
	"<hr>" + 
	"<strong>Flights</strong>" +
	"<hr>" +  "<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Start Destination</th>" +
		"<th scope=\"col\">Finish Destination</th>" +
		"<th scope=\"col\">Transfers</th>" +
		"<th scope=\"col\">Departure Time</th>" +
		"<th scope=\"col\">Arrival Time</th>" +
		"<th scope=\"col\">Flight Length</th>" +
		"<th scope=\"col\">Base Price</th>" +
        "<th scope=\"col\">Trip Type</th>" +
        "<th scope=\"col\">Rating</th>" +
		"</tr>" +
		"</thead><tbody>" + s + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML += flights;
}

function printFlights1(data){
	
	var flights = "";
	var s = "";
	if(data!=null){
		$.each(data, function(index, flight) {
			s += "<tr>" + "<td>" + flight.startDestination.name + "</td>" + "<td>"+ flight.finishDestination.name 
			+ "</td>" + "<td>" + flight.transfers + "</td><td>" + flight.departureTime.substring(0, 19).replace('T', '<br>') + "</td><td>" + flight.arrivalTime.substring(0, 19).replace('T', '<br>') + "</td><td>"
			+ flight.flightLength + "</td><td>" + flight.basePrice + "</td>"
			+ "<td scope=\"col\">" + createRating(flight.rating) + "</td>"
			+ "<td scope=\"col\"><button id = \"" + flight.links[0].href +"\" type=\"button\" class=\"btn btn-success bookflight\" value = \"" + flight.id +"\">Add to cart</button></td>"
			+"</tr>";
		});
	}
	
	flights = "<div>" + 
	"<hr>" + 
	"<strong>Flights</strong>" +
	"<hr>" +  "<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Start Destination</th>" +
		"<th scope=\"col\">Finish Destination</th>" +
		"<th scope=\"col\">Transfers</th>" +
		"<th scope=\"col\">Departure Time</th>" +
		"<th scope=\"col\">Arrival Time</th>" +
		"<th scope=\"col\">Flight Length</th>" +
		"<th scope=\"col\">Base Price</th>" +
		"<th scope=\"col\">Rating</th>" +
		"</tr>" +
		"</thead><tbody>" + s + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML = flights;
}

function printBranchOffices(data){	
	var pomDataBranchOffices;
	$.ajax({
		type: "GET",
		url: data._links.branch_offices.href,
		dataType: "json",
		async: false,
		success: function(data){
			if(data!=null){
				pomDataBranchOffices = data;
			}
		}
	});
	
	var branchOffices = "";
	let s = "";
	$.each(pomDataBranchOffices, function(index, branchOffice) {
		s += "<tr id = \"" +branchOffice.links[0].href + "\">" + "<td>" + branchOffice.address + "</td>" + "<td>" + branchOffice.location.name + "</td>"+ "</tr>";
	});
	
	branchOffices ="</div>" + 
	"<hr>" + 
	"<strong>Branch officess</strong>" +
	"<hr>" + "<table class=\"table table-hover\" id = \"tablebo\"><thead>" +
		"<tr>" + 
		"<th scope=\"col\">Address</th>" +
		"<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + s + "</tbody></table>";
	
	document.getElementById("tablediv").innerHTML += branchOffices;
}

$(document).on('click', '#rentacar-search', function(event){
	event.preventDefault();
	selected_search_item = "rentacar";
	$('#locationLabel1').text('Location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Company name');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Rent-a-car services');
	$('#airline-controls').css("display", "none");
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#x', function(){
	$('#search-panel').css("display", "none");
})

$(document).on('click', '#hotel-search', function(event){
	event.preventDefault();
	selected_search_item = "hotel";
	$('#locationLabel1').text('Location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Company name');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Hotels');
	$('#airline-controls').css("display", "none");
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#airline-search', function(event){
	event.preventDefault();
	selected_search_item = "airline";
	$('#locationLabel1').text('Start location');
	$('#locationInput1').css("display", "block");
	$('#nameLabel').text('Target location');
	$('#nameInput').css("display", "block");
	$('#search-title').text('Airlines');
	$('#airline-controls').css("display", "block");
	$('#search-panel').css("display", "block");
})

$(document).on('click', '#goSearch', function(e){
	e.preventDefault();
    $("#tablediv").show();
	$("#myMap").hide();
	$("#profile").hide();
	$('#profdiv').hide();
	let startDate = new Date($('#startDateInput').val());
	let endDate = new Date($('#endDateInput').val());
	if($('#startDateInput').val() != "" && $('#endDateInput').val() != ""){
		if(startDate < endDate){
			search(selected_search_item);
		}
		else{
			alert('Neispravno unet vremenski period!');
		}
	}
	search(selected_search_item);
})

function search(company){
	let parameters = [{"key": "name", "value": $('#nameInput').val()},
		{"key": "locationName", "value": $('#locationInput1').val()}]
	if($('#startDateInput').val() != ""){
		parameters[2] = {"key": "startDate", "value": new Date($('#startDateInput').val()).getTime()};
	}
	if($('#endDateInput').val() != ""){
		parameters[3] = {"key": "endDate", "value": new Date($('#endDateInput').val()).getTime()};
	}
	parameters[4] = {"key": "tripType", "value": $('#tripTypeSelect').find('option:selected').attr('value')};
	parameters[5] = {"key": "category", "value": $('#flighClassInput').val()};
	parameters[6] = {"key": "weight", "value": $('#LuggageWeightInput').val()};
	parameters[7] = {"key": "personNum", "value": $('#personsNumberInput').val()};
	parameters[8] = {"key": "airline", "value": $('#airlineIdFilter').val()};
	parameters[9] = {"key": "priceBegin", "value": $('#priceBegin').val()};
	parameters[10] = {"key": "priceEnd", "value": $('#priceEnd').val()};
	parameters[11] = {"key": "durationBegin", "value": $('#durationBegin').val()};
	parameters[12] = {"key": "durationEnd", "value": $('#durationEnd').val()};
	let url;
	for(i = 0; i < parameters.length; i++){
		if(parameters[i] != undefined && parameters[i].value != "" && parameters[i].value != undefined){
			url += '&' + parameters[i].key + '=' + parameters[i].value;
		}
	}
    
    //alert(url);
	if(company == "rentacar"){
		$.ajax({
			method: 'GET',
			url: '/rent-a-cars/search?' + url,
			contentType: 'application/json',
			success: function(data){
				printListOfCompanies(data);
			}
		})
	}
	else if(company == "hotel"){
		$.ajax({
			method: 'GET',
			url: '/hotels/search?' + url,
			contentType: 'application/json',
			success: function(data){
				printListOfCompanies(data);
			}
		})
	}
	else if(company == "airline"){
		$.ajax({
			method: 'GET',
			url: '/flights/search?' + url,
			contentType: 'application/json',
			success: function(data){
				printFlights1(data);
			}
		})
	}
}
$(document).on('click','#addTicket',function(e){
    var x = document.getElementsByClassName("taken");
    var seats = new Array();
    $.each(x, function(index, seat) {
    	seats.push(seat.id);
    });
    
    localStorage.setItem("flightC",$('#cancelBookingSeats').val());
    localStorage.setItem("seatsC",JSON.stringify(seats));
    $('#flightModal').modal('toggle');
    
    $('#suggestedItems').show();
    $('#quickResVehicle').show();
    $('#quickResRoom').show();
    
});

$(document).on('click','#suggestedRent',function(e){
    e.preventDefault();
    $("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
    printSuggestedRents();
});

$(document).on('click','#suggestedHotel',function(e){
    e.preventDefault();
    $("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
    printSuggestedHotels();
});

$(document).on('click','#quickResRoom',function(e){
    e.preventDefault();
    $("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
    
    printQuickRoomReservations();
});

$(document).on('click','#quickResFlight',function(e){
    e.preventDefault();
    $("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
    
    printQuickFlightReservations();
});

$(document).on('click','#quickResVehicle',function(e){
    e.preventDefault();
    $("#myMap").hide();
	$("#tablediv").show();
	$("#profile").hide();
	$('#profdiv').hide();
    
    printQuickVehicleReservations();
});

function printQuickVehicleReservations(){
    var flightId = localStorage.getItem("flightC");
    var vehicleQuicks = new Array(); 
    $.ajax({
        method: 'GET',
        url: '/flights/' + flightId,
        async: false,
        success: function(data){
            //alert("dobio let");
             $.ajax({
                method: 'GET',
                url: '/destinations/' + data.finishDestination.id + '/vehicle-quicks',
                async: false, 
                success: function(data2){    
                    //alert("dobio quickove vozila");
                    printQVR(data2);
                }   
            });
        }   
    });
}

function printQVR(data){
    var vehicleReservations = "";
    
    if(data!=null && data!= "null" && data!=undefined && data!="undefined"){
        $.each(data,function(index, vehicleReservation){
            vehicleReservations += "<tr>" + "<td scope=\"col\">" + vehicleReservation.vehicle.branchOffice.rentACar.name + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.vehicle.brand + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.vehicle.model + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.vehicle.type + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.vehicle.seatsNumber + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.beginDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.endDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.price + "</td>"
                    + "<td scope=\"col\">" + vehicleReservation.vehicle.pricePerDay + "</td>"
                    + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + vehicleReservation.id + "\" type=\"button\" class=\"btn btn-primary bookQuickReservationVehicleBtn\">Book</button>" + "</td>"
                    + "</tr>";
        });
    }
    
    document.getElementById("tablediv").innerHTML =
            "<table class=\"table table-hover\"><thead>" +
            "<tr>" + 
                "<th scope=\"col\">Rent A Car</th>" +
                "<th scope=\"col\">Brand</th>" +
                "<th scope=\"col\">Model</th>" +
                "<th scope=\"col\">Type</th>" +
                "<th scope=\"col\">Seats Number</th>" +
                "<th scope=\"col\">Begin Date</th>" +
                "<th scope=\"col\">End Date</th>" +
                "<th scope=\"col\">Old Price(&euro;)</th>" +
                "<th scope=\"col\">Discounted Price(&euro;)</th>" +
            "</tr>" +
            "</thead><tbody>" + vehicleReservations + "</tbody></table>";
    
}
$(document).on('click','.bookQuickReservationRoomBtn',function(e){
    var roomReservationId = $(this).val();
    var seats = JSON.parse(localStorage.getItem("seatsC"));
    var reservation;
    
     $.ajax({
		type: "POST",
		url: "/tickets",
		contentType: "application/json",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
		success: function(data){
            reservation = data;
            //alert("dobio nazad rezervaciju " + reservation.id)
        }
    });
    
    $.ajax({
		type: "POST",
		url: "tickets/" + reservation.ticket.id + "/seats",
        data: JSON.stringify(seats),
		contentType: "application/json",
        dataType: "json",
        async: false,
		success: function(data){
            //alert("dodao sedista");
        }
    });
    
    $.ajax({
        type: "POST",
        url: "/reservations/" + reservation.id + "/set-room-reservation/" + roomReservationId, 
        dataType: "json", 
        async: false,
        success: function(data){
            //alert("povezao sobu"); 
        }
    });
    
     $.ajax({
        type: "POST",
        url: "/reservations/" + reservation.id + "/user?user=" + currentUser.id, 
        contentType: "application/json",
        async: false,
        success: function(data2){
            //alert("postavio usera"); 
        }
    });
    
    printQuickRoomReservations();
    clearShoppingCart(); 
    
});

$(document).on('click','.bookQuickReservationVehicleBtn',function(e){
   e.preventDefault();
   var vehicleReservationId = $(this).val();
    //alert(vehicleReservationId);
   var seats = JSON.parse(localStorage.getItem("seatsC"));
    var reservation;
    
    $.ajax({
		type: "POST",
		url: "/tickets",
		contentType: "application/json",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
		success: function(data){
            reservation = data;
            //alert("dobio nazad rezervaciju " + reservation.id)
        }
    });
    
    $.ajax({
		type: "POST",
		url: "tickets/" + reservation.ticket.id + "/seats",
        data: JSON.stringify(seats),
		contentType: "application/json",
        dataType: "json",
        async: false,
		success: function(data){
            //alert("dodao sedista");
        }
    });
    
     $.ajax({
        type: "POST",
        url: "/reservations/" + reservation.id + "/set-vehicle-reservation/" + vehicleReservationId,
        contentType: "application/json",
        async: false,
        success: function(data){
            //alert("povezao vozilo");
        }
     });
    
    $.ajax({
        type: "POST",
        url: "/reservations/" + reservation.id + "/user?user=" + currentUser.id, 
        contentType: "application/json",
        async: false,
        success: function(data2){
            //alert("postavio usera"); 
        }
    });
    
    printQuickVehicleReservations();
    clearShoppingCart();
});

function printQuickRoomReservations(){  
    var flightId = localStorage.getItem("flightC");
    var roomQuicks = new Array(); 
    $.ajax({
        method: 'GET',
        url: '/flights/' + flightId,
        async: false,
        success: function(data){
            //alert("dobio let");
             $.ajax({
                method: 'GET',
                url: '/destinations/' + data.finishDestination.id + '/room-quicks',
                async: false, 
                success: function(data2){    
                    //alert("dobio quickove soba");
                    printQRR(data2);
                }   
            });
        }   
    });
}

function printQRR(data){
    var roomReservations = "";
    if(data != null && data != "null" && data != "undefined" && data != undefined){
        $.each(data,function(index, roomReservation){
            roomReservations += "<tr>" + "<td scope=\"col\">" + roomReservation.room.floor.hotel.name + "</td>" 
                    + "<td scope=\"col\">" + roomReservation.beginDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + roomReservation.endDate.substring(0, 19).replace('T', '<br>') + "</td>"
                    + "<td scope=\"col\">" + roomReservation.price + "</td>"
                    + "<td scope=\"col\">" + roomReservation.room.roomType.pricePerNight + "</td>"
                    + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + roomReservation.id + "\" type=\"button\" class=\"btn btn-primary bookQuickReservationRoomBtn\">Book</button>" + "</td>"
                    + "</tr>";
        });
    }
    
    document.getElementById("tablediv").innerHTML =
            "<table class=\"table table-hover\"><thead>" +
            "<tr>" + 
                "<th scope=\"col\">Hotel</th>" +
                "<th scope=\"col\">Begin Date</th>" +
                "<th scope=\"col\">End Date</th>" +
                "<th scope=\"col\">Old Price(&euro;)</th>" +
                "<th scope=\"col\">Discounted Price(&euro;)</th>" +
            "</tr>" +
            "</thead><tbody>" + roomReservations + "</tbody></table>";
}

function printQuickFlightReservations(){
    
    $.ajax({
        method: 'GET',
        url: '/airlines/all-quicks',
        async: false, 
        success: function(data){    
            printQFR(data);
        }   
    });
}

function printQFR(data){
    var flightSeats = "";
    
    if(data!=null && data!= "null" && data!=undefined && data!="undefined"){
        $.each(data,function(index,flightSeat){
            flightSeats += "<tr>" + "<td scope=\"col\">" + flightSeat.flight.startDestination.name + "</td>" 
                + "<td scope=\"col\">" + flightSeat.flight.finishDestination.name + "</td>"
                + "<td scope=\"col\">" + flightSeat.flight.departureTime.substring(0, 19).replace('T', '<br>') + "</td>"
                + "<td scope=\"col\">" + flightSeat.flight.arrivalTime.substring(0, 19).replace('T', '<br>') + "</td>"
                + "<td scope=\"col\">" + flightSeat.flight.flightLength + "</td>"
                + "<td scope=\"col\">" + flightSeat.flight.basePrice + "</td>"
                + "<td scope=\"col\">" + flightSeat.price + "</td>"
                + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + flightSeat.id +"\" type=\"button\" class=\"btn btn-primary bookQuickReservationFlightBtn\">Book</button>" + "</td>"
                + "</tr>";
        });
    }
        
        document.getElementById("tablediv").innerHTML =
            "<table class=\"table table-hover\"><thead>" +
            "<tr>" + 
            "<th scope=\"col\">Start Destination</th>" +
            "<th scope=\"col\">Finish Destination</th>" +
            "<th scope=\"col\">Departure Time</th>" +
            "<th scope=\"col\">Arrival Time</th>" +
            "<th scope=\"col\">Flight Length(km)</th>" +
            "<th scope=\"col\">Old Price(&euro;)</th>" +
            "<th scope=\"col\">Discounted Price(&euro;)</th>" +
            "</tr>" +
            "</thead><tbody>" + flightSeats + "</tbody></table>";
}


$(document).on('click','.bookQuickReservationFlightBtn',function(e) {
    var flightSeatId = $(this).val();
    var reservation;
    var seat = new Array();
    seat.push(flightSeatId);
    
    $.ajax({
		type: "POST",
		url: "/tickets",
		contentType: "application/json",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
		success: function(data){
            //alert("napravio rezervaciju");
            reservation = data;
        }
    });

    $.ajax({
		type: "POST",
		url: "tickets/" + reservation.ticket.id + "/seats",
        data: JSON.stringify(seat),
		contentType: "application/json",
        dataType: "json",
        async: false,
		success: function(data){
            //alert("dodao sediste sa akcijom");
        }
    });
    
     $.ajax({
        type: "POST",
        url: "/reservations/" + reservation.id + "/user?user=" + currentUser.id, 
        contentType: "application/json",
        async: false,
        success: function(data2){
                //alert("postavio usera"); 
        }
    });
    
    $(this).prop( "disabled", true );
    printQuickFlightReservations();
    
});

function printSuggestedRents(){
     var flightId = localStorage.getItem("flightC"); 
     var rents; 
     $.ajax({
			method: 'GET',
			url: '/flights/'+ flightId + "/finish",
			contentType: 'application/json',
            dataType: "json",
			success: function(data){
                $.ajax({
                    method: 'GET',
                    url: '/rent-a-cars/search?locationName=' + data.name,
                    contentType: 'application/json',
                    dataType: "json",
                    success: function(data2){    
                        printListOfCompanies(data2);
                    }   
                });
            }           
    });  
}

function printSuggestedHotels(){
    var flightId = localStorage.getItem("flightC"); 
     var rents; 
     $.ajax({
			method: 'GET',
			url: '/flights/'+ flightId + "/finish",
			contentType: 'application/json',
            dataType: "json",
			success: function(data){
                $.ajax({
                    method: 'GET',
                    url: '/hotels/search?locationName=' + data.name,
                    contentType: 'application/json',
                    dataType: "json",
                    success: function(data2){    
                        printListOfCompanies(data2);
                    }   
                });
            }           
    }); 
}

function loadSeatsChartFor(id) {
	flightId = id;
	$.ajax({
		type: "GET",
		url: "/flights/" + id + "/seats",
		async: false,
		beforeSend: function(request) {
    		request.setRequestHeader("X-Auth-Token", localStorage.getItem("token"));
  		},
  		success: function(data) {
  			if(data != null) {
  				let tableHTML = "<table id=\"seatsChart\"><tr>";
  				let currentRow = 1;
  				let seatClass = "";
			 	$.each(data, function(index, seat) {
			 		if(seat.state == "TAKEN")
			 			seatClass = "btn-danger";
			 		else
			 			seatClass = "btn-primary";
					if(seat.row == currentRow) {
						tableHTML += "<td><button id=\"" + seat.id + "\" class=\"btn flight-seat " + seatClass + "\" align=\"center\">" + (index + 1) + "</button></td>";
					} else {
						tableHTML += "</tr><tr><td><button id=\"" + seat.id + "\" class=\"btn flight-seat " + seatClass + "\" align=\"center\">" + (index + 1) + "</button></td>";
						currentRow = seat.row;
					}
				});	
  				tableHTML += "</tr></table><br><br>";
  				document.getElementById("seats").innerHTML = tableHTML;
  			}
  		}
	});
}

$(document).on('click','.btn-primary',function(e) {
    e.preventDefault();
    if($(this).hasClass('flight-seat')){
        $(this).removeClass('btn-primary').addClass('btn-success');
        $(this).addClass('taken');
    }
})

$(document).on('click','.btn-success',function(e) {
    e.preventDefault();
    if($(this).hasClass('flight-seat')){
        $(this).removeClass('btn-success').addClass('btn-primary');
        $(this).removeClass('taken');
    }
})

$(document).on('click','.btn-danger',function(e) {
    e.preventDefault();
})

function SortByFirstName(a, b){
  var aName = a.firstName.toLowerCase();
  var bName = b.firstName.toLowerCase(); 
  return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}

function SortByLastName(a, b){
  var aName = a.lastName.toLowerCase();
  var bName = b.lastName.toLowerCase(); 
  return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}

function createRating(value){
    if(value < 1.5){
        return "<img src=\"star.png\">";
    }else if(value >= 1.5 &&  value < 2.5){
        return "<img src=\"star.png\"><img src=\"star.png\">";
    }else if(value >= 2.5 &&  value < 3.5){
        return "<img src=\"star.png\"><img src=\"star.png\"><img src=\"star.png\">";
    }else if(value >= 3.5 && value < 4.5){
        return "<img src=\"star.png\"><img src=\"star.png\"><img src=\"star.png\"><img src=\"star.png\">";
    }else if(value > 4.5){
        return "<img src=\"star.png\"><img src=\"star.png\"><img src=\"star.png\"><img src=\"star.png\"><img src=\"star.png\">";
    }
}

$(document).on('click', '#pendingResBtn', function(e) {
	e.preventDefault();
    loadPendingReservations();
})

function loadPendingReservations() {
    var pomData;
    $.ajax({
			type: "GET",
			url: "/users/registered/" + currentUser.id + "/pendingReservations",
			dataType: "json",
			async: false,
			success: function(data){
					pomData = data;
			}
	 });
	 printPendingReservations(pomData);
	 $('#profdiv').show();
	 $('#profile').hide();
}

function printPendingReservations(data) {
	var pendingHTML = "";
	var name = "";
    var vehicle = "";
	if(data!=null){
		$.each(data, function(index, reservation) {
			 $.ajax({
					type: "GET",
					url:  reservation.links[1].href,
					dataType: "json",
					async: false,
					success: function(data){
						name = data.name;
					}
			 });
			pendingHTML += "<tr>" + "<td scope=\"col\">" + reservation.beginDate.substring(0, 19).replace('T', '<br>') + "</td>" + "<td scope=\"col\">"+ reservation.endDate.substring(0, 19).replace('T', '<br>') + "</td>" +
			"<td scope=\"col\">" + name + "</td>"
            + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-primary acceptPendingReservation\">Accept</button>" + "</td>"
            + "<td scope=\"col\">" + "<button style=\"margin: 10px\" value =\"" + reservation.id +"\" type=\"button\" class=\"btn btn-danger cancelPendingReservation\">Cancel</button>" + "</td>"
            + "</tr>";
            
		});
	}
	document.getElementById("profdiv").innerHTML = "<button style=\"margin: 10px\" type=\"button\" class=\"btn btn-primary backToProf\">Profile</button>" +
		"<table class=\"table table-hover\"><thead>" +
		"<tr>" + 
	    "<th scope=\"col\">Begin Date</th>" +
	    "<th scope=\"col\">End Date</th>" +
	    "<th scope=\"col\">Location</th>" +
		"</tr>" +
		"</thead><tbody>" + pendingHTML + "</tbody></table>";
}

$(document).on('click', '.acceptPendingReservation', function(e) {
	e.preventDefault();
    var reservationId = $(this).val();
     $.ajax({
        type: "POST",
        url:  "/reservations/" + reservationId +"/accept?user=" + currentUser.id,
        dataType: "json",
        async: false,
        success: function(data){
            loadPendingReservations();   
        }
	 }); 
})

$(document).on('click', '.cancelPendingReservation', function(e) {
	e.preventDefault();
    var reservationId = $(this).val();
     $.ajax({
        type: "POST",
        url:  "/reservations/" + reservationId +"/decline?user=" + currentUser.id,
        dataType: "json",
        async: false,
        success: function(data){
            loadPendingReservations();   
        }
	 });
})
