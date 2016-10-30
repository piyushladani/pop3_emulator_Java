var ws = null;

$(document).ready(function() {
	console.log("this worked fine!");
	$("#flow").val("");
	
	var target = 'ws://' + window.location.host + '/MobCom2016_2017-1/ProtocolServer';
	if('WebSocket' in window) {
		ws = new WebSocket(target);
	}
	else if('MozWebSocket' in window) {
		ws = new MozWebSocket(target);
	}
	else {
		alert('Web Socket is not supported by this browser!');
	}
	ws.onopen = function() {
		console.log("Connection has been established.");
	};
	//receive messages from server
	ws.onmessage = function(event) {
		console.log(event.data);
		if(event.data == "200OK") {
			$("#flow").val($("#flow").val() + "\nClient <---- 200 ---- Server");
		}
		
		ws.send("Ack request");
		$("#flow").val($("#flow").val() + "\nClient ---- ACK ----> Server");
	}
	//close web socket connection
	ws.onclose = function(event) {
		console.log("Connection has been closed.")
	}
	
	$("#SendInvite").on("click", function(event) {
		ws.send("Invite request");
		$("#flow").val("Client ---- INV ----> Server");
	});
	
	
});