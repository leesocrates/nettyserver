var stream;
//checks if the browser supports WebRTC 
function hasUserMedia() { 
   navigator.getUserMedia =  navigator.mediaDevices.getUserMedia
   return !!navigator.getUserMedia; 
}
 
if (hasUserMedia()) { 
   var handleStream = function (s) { 
   	  stream = s;
   	  console.log(s)
      var video = document.querySelector('video'); 
		
      //insert stream into the video tag 
      video.src = window.URL.createObjectURL(s); 
      
   }
		
   //get both video and audio streams from user's camera 
   navigator.mediaDevices.getUserMedia({ video: true, audio: true }).then(handleStream); 
}else {
   alert("Error. WebRTC is not supported!"); 
}

btnGetAudioTracks.addEventListener("click", function(){ 
   console.log("getAudioTracks"); 
   console.log(stream.getAudioTracks()); 
});
  
btnGetTrackById.addEventListener("click", function(){ 
   console.log("getTrackById"); 
   console.log(stream.getTrackById(stream.getAudioTracks()[0].id)); 
});
  
btnGetTracks.addEventListener("click", function(){ 
   console.log("getTracks()"); 
   console.log(stream.getTracks()); 
});
 
btnGetVideoTracks.addEventListener("click", function(){ 
   console.log("getVideoTracks()"); 
   console.log(stream.getVideoTracks()); 
});

btnRemoveAudioTrack.addEventListener("click", function(){ 
   console.log("removeAudioTrack()"); 
   stream.removeTrack(stream.getAudioTracks()[0]); 
});
  
btnRemoveVideoTrack.addEventListener("click", function(){ 
   console.log("removeVideoTrack()"); 
   stream.removeTrack(stream.getVideoTracks()[0]); 
});