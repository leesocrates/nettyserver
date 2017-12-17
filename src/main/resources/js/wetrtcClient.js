//checks if the browser supports WebRTC 

function hasUserMedia() { 
   navigator.getUserMedia =  navigator.mediaDevices.getUserMedia
   return !!navigator.getUserMedia; 
}
 
if (hasUserMedia()) { 
   var handleStream = function (stream) { 
      var video = document.querySelector('video'); 
		
      //insert stream into the video tag 
      video.src = window.URL.createObjectURL(stream); 
   }
		
   //get both video and audio streams from user's camera 
   navigator.mediaDevices.getUserMedia({ video: true, audio: true }).then(handleStream); 
}else {
   alert("Error. WebRTC is not supported!"); 
}