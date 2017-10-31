var ws = null;
	
	var target = 'ws://' + window.location.host + '/Group13/Pop3';
	
	if('WebSocket' in window) {
		window.alert(target);
		ws = new WebSocket(target);
	}
	else if('MozWebSocket' in window) {
		window.alert(target);
		ws = new MozWebSocket(target);
	}
	else {
		window.alert('Web Socket is not supported by this browser!');
	}
	
	ws.onopen = function() {
		console.log("Connection has been established.");
	};

	
	$(document).ready(function() {
		document.getElementById('divi2').setAttribute('style','visibility:hidden');
		//Initially, hide the lower part of the web-page.
		
		
		$("#retr").keypress(function (e) {
		     //if the letter is not digit then display error and don't type anything
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        $("#errmsg1").html("Digits Only").show().fadeOut(2000);
		        return false;
		     }
		    });
		$("#delete").keypress(function (e) {
		     //if the letter is not digit then display error and don't type anything
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        $("#errmsg2").html("Digits Only").show().fadeOut(2000);
		        return false;
		     }
		    });
		$("#list").keypress(function (e) {
		     //if the letter is not digit then display error and don't type anything
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        $("#errmsg3").html("Digits Only").show().fadeOut(2000);
		        return false;
		     }
		    });	
		$("#top").keypress(function (e) {
		     //if the letter is not digit then display error and don't type anything
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        $("#errmsg4").html("Digits Only").show().fadeOut(2000);
		        return false;
		     }
		    });	
		$("#top1").keypress(function (e) {
		     //if the letter is not digit then display error and don't type anything
		     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		        //display error message
		        $("#errmsg5").html("Digits Only").show().fadeOut(2000);
		        return false;
		     }
		    });	

	
	//receive messages fromServer
	ws.onmessage = function(event) {
		
		console.log(event.data);
		var str = event.data;
		if(str=="+OK login successful"){
			document.getElementById('myHeader1').innerHTML = 'Transaction State';
			//change the state to transaction
			document.getElementById('divi2').setAttribute('style','visibility:visible');
			//make the lower part of the web-page visible if login is successful
			document.getElementById('divi1').setAttribute('style','visibility:hidden');
			//hide the authorization section
			}
		else if (str=="+OK server signing off (maildrop empty)"){
			//else keep it invisible
			document.getElementById('divi2').setAttribute('style','visibility:hidden');
			document.getElementById('myHeader1').innerHTML = 'Update State';
			}
	
		var n = str.indexOf("+OK");
		var m = str.indexOf("-ERR");
		//print the response on console based on +OK or -ERR message received from server.
		if(n >= 0) {
			$("#flow").val($("#flow").val() + "\nServer: "+ str +"\n");
		}
		
		if(m >= 0) {
			$("#flow").val($("#flow").val() + "\nServer: "+str+"\n");
		}
		
	}
	
	//close web socket connection
	ws.onclose = function(event) {
		console.log("Connection has been closed.")
	}

	
	//handler on clock (trigger!)

	$("#USER").on("click", function(event) //on USER button press
	{
		var value = document.getElementById('username').value;
		document.getElementById('divi2').setAttribute('style','visibility:hidden');
		if ($('#username').val() == ""){
            alert('Please complete the Username field');
        }
		else{
			
		if(ws.readyState == 1) {
			ws.send("USER "+value);// to send USER keyword along with username 
			console.log("USER "+value);
			$("#flow").val("Client: USER "+value+"\n");
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		}
	});
	
	$("#PASS").on("click", function(event) //on PASS button press
	{
		var value1 = document.getElementById('password').value;
		document.getElementById('divi2').setAttribute('style','visibility:hidden');
		if ($('#password').val() == ""){
            alert('Please complete the Passport field');
        }
		else{
		if(ws.readyState == 1) {
			ws.send("PASS "+value1);// to send PASS keyword along with password 
			console.log("PASS "+value1);
			$("#flow").val("Client: PASS "+value1+"\n");
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		}
	});

	$("#QUIT").on("click", function(event) //on QUIT button press in authorization section
	{	
		if(ws.readyState == 1) {
			
			ws.send("QUIT"); // to send QUIT keyword 
			console.log("QUIT");
			$("#flow").val("Client: QUIT\n");
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		
	});
	
	$("#QUIT1").on("click", function(event) //on QUIT button press in Transaction section
	{	
		if(ws.readyState == 1) {
			ws.send("QUIT"); // to send QUIT keyword
			console.log("QUIT");
			$("#flow").val("Client: QUIT\n");
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		
	});
	
	$("#RSET").on("click", function(event) //on RSET button press in Transaction section
	{
		if(ws.readyState == 1) {
			ws.send("RSET");// to send RSET keyword
			console.log("RSET");
			$("#flow").val("Client: RSET\n");
			}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		
	});

	$("#NOOP").on("click", function(event) //on NOOP button press in Transaction section
	{
		if(ws.readyState == 1) {
			ws.send("NOOP");// to send NOOP keyword
			console.log("NOOP");
			$("#flow").val("Client: NOOP\n");
			}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		
	});
	
	$("#STAT").on("click", function(event) {
		if(ws.readyState == 1) {
			ws.send("STAT");
			console.log("STAT");
			$("#flow").val("Client: STAT\n");	
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		
	});
	
	$("#LIST").on("click", function(event) {
		var value2 = document.getElementById('list').value;
		
		if(ws.readyState == 1) {
			ws.send("LIST "+value2);
			console.log("LIST "+value2);
			$("#flow").val("Client: LIST "+value2+"\n");
			}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
	});
	
	$("#RETR").on("click", function(event) {
		var value3 = document.getElementById('retr').value;
		if ($('#retr').val() == ""){
            alert('Please complete the Retrieve field');
        }
		else{
		if(ws.readyState == 1) {
			ws.send("RETR "+value3);
			console.log("RETR "+value3);
			$("#flow").val("Client: RETR "+value3+"\n");
			}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		}
	});

	$("#DELE").on("click", function(event) {
		var value4 = document.getElementById('delete').value;
		if ($('#delete').val() == ""){
            alert('Please complete the Delete field');
        }
		else{
		if(ws.readyState == 1) {
			ws.send("DELE "+value4);
			console.log("DELE "+value4);
			$("#flow").val("Client: DELE "+value4+"\n");	
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		}	
	});
	
	$("#TOP").on("click", function(event) {
		var value5 = document.getElementById('top').value;
		var value6 = document.getElementById('top1').value;
		if (($('#top').val() == "")||($('#top1').val() == "")){
            alert('Please complete both E-mail Nr. and Line Nr. fields');
        }
		else{
		if(ws.readyState == 1) {
			ws.send("TOP "+value5+"$"+value6);
			console.log("TOP "+value5+" "+value6);
			$("#flow").val("Client: TOP "+value5+" "+value6+"\n");
		}
		else {
	        alert("THE SESSION IS CLOSED.\n\nPLEASE REFRESH YOUR BROWSER TO RE-ESTABLISH THE SESSION");
			}
		}
	});
	
});