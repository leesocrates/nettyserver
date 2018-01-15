var connection = new WebSocket('ws://47.52.98.195:9090');
//var connection = new WebSocket('ws://localhost:9090'); 
var name = "";
var receiveChannel;

var loginInput = document.querySelector('#loginInput'); 
var loginBtn = document.querySelector('#loginBtn'); 

var otherUsernameInput = document.querySelector('#otherUsernameInput'); 
var connectToOtherUsernameBtn = document.querySelector('#connectToOtherUsernameBtn'); 
var msgInput = document.querySelector('#msgInput'); 
var sendMsgBtn = document.querySelector('#sendMsgBtn'); 
var connectedUser, myConnection, dataChannel;
  
//when a user clicks the login button 
loginBtn.addEventListener("click", function(event) { 
   name = loginInput.value; 
	
   if(name.length > 0) { 
      send({ 
         type: "login", 
         name: name 
      }); 
   } 
}); 
 
//handle messages from the server 
connection.onmessage = function (message) { 
   console.log("connection Got message", message.data); 
   var data = JSON.parse(message.data); 
	
   switch(data.type) { 
      case "login": 
         onLogin(data.success); 
         break; 
      case "offer": 
         onOffer(data.offer, data.name); 
         break; 
      case "answer":
         onAnswer(data.answer); 
         break; 
      case "candidate": 
         onCandidate(data.candidate); 
         break; 
      default: 
         break; 
   } 
}; 
 
//when a user logs in 
function onLogin(success) { 

   if (success === false) { 
      alert("oops...try a different username"); 
   } else { 
      //creating our RTCPeerConnection object 
      var configuration = { 
         "iceServers": [{ "url": "stun:stun.1.google.com:19302" }] 
      }; 
		
      console.log("RTCPeerConnection object start create"); 
//      myConnection = new webkitRTCPeerConnection(configuration, { 
//         optional: [{RtpDataChannels: true}] 
//      }); 
      var servers = null;
      pcConstraint = null;
      myConnection =  new RTCPeerConnection(configuration,  pcConstraint);
		
      console.log("RTCPeerConnection object was created"); 
      console.log(myConnection); 
  
      myConnection.ondatachannel = receiveChannelCallback;
      //setup ice handling 
      //when the browser finds an ice candidate we send it to another peer 
      myConnection.onicecandidate = function (event) { 
		
         if (event.candidate) { 
        	 console.log("send candidate : "+event.candidate)
            send({ 
               type: "candidate", 
               candidate: event.candidate 
            });
         } 
      }; 
      openDataChannel();	
      	
   } 
};
  
function receiveChannelCallback(event) {
	console.log('Receive Channel Callback'+ event.channel.label);
	  receiveChannel = event.channel;
	  receiveChannel.onmessage = onReceiveMessageCallback;

	}


function onReceiveMessageCallback(event) {
	console.log('Received Message ：' + event.data + " , on channel label : "+receiveChannel.label);
	}


connection.onopen = function () { 
   console.log("connection Connected"); 
}; 
 
connection.onerror = function (err) { 
   console.log("connection Got error", err); 
};
  
// Alias for sending messages in JSON format 
function send(message) { 
   if (connectedUser) { 
      message.name = connectedUser; 
   }
	
   connection.send(JSON.stringify(message)); 
};





//setup a peer connection with another user 
connectToOtherUsernameBtn.addEventListener("click", function () {
  
   var otherUsername = otherUsernameInput.value;
   connectedUser = otherUsername;
	
   if (otherUsername.length > 0) { 
      //make an offer 
      myConnection.createOffer(function (offer) { 
         console.log("offer created : "+offer); 
			
         send({ 
            type: "offer", 
            offer: offer 
         }); 
			
         myConnection.setLocalDescription(offer); 
      }, function (error) { 
         alert("An error has occurred on createOffer()."+ error); 
      }); 
      
   } 
});
  
//when somebody wants to call us 
function onOffer(offer, name) { 
   connectedUser = name; 
   myConnection.setRemoteDescription(new RTCSessionDescription(offer));
	
   myConnection.createAnswer(function (answer) { 
      myConnection.setLocalDescription(answer); 
		
      send({ 
         type: "answer", 
         answer: answer 
      }); 
		
   }, function (error) { 
      alert("oops... createAnswer error"); 
   }); 
}

//when another user answers to our offer 
function onAnswer(answer) { 
   myConnection.setRemoteDescription(new RTCSessionDescription(answer)); 
}
  
//when we got ice candidate from another user 
function onCandidate(candidate) { 
	console.log("onCandidate() add IceCandidate")
   myConnection.addIceCandidate(new RTCIceCandidate(candidate)); 
}




//creating data channel 
function openDataChannel() { 

   var dataChannelOptions = { 
      reliable:true 
   }; 
   
   dataConstraint = null;
	
   dataChannel = myConnection.createDataChannel("myDataChannel"+Math.random(), dataConstraint);
	
   dataChannel.onerror = function (error) { 
      console.log("dataChannel Error:", error); 
   };
	
   dataChannel.onmessage = function (event) { 
      console.log("dataChannel Got message : ", event.data, " ; on chanel lable : "+dataChannel.label); 
   };  
   
   dataChannel.onopen = function () { 
	      console.log("dataChannel onopen():"+ dataChannel.label); 
	   };  
	   
   dataChannel.onclose = function () { 
		 console.log("dataChannel onclose:"); 
   };  	   
}
  
//when a user clicks the send message button 
sendMsgBtn.addEventListener("click", function (event) { 
   var val = msgInput.value; 
   console.log("dataChannel send message: "+val);
   if(receiveChannel){
	   receiveChannel.send(val); 
   } else {
	   dataChannel.send(val); 
   }
});

