var stream;
//checks if the browser supports WebRTC 
function hasUserMedia() { 
   navigator.getUserMedia =  navigator.mediaDevices.getUserMedia
   return !!navigator.getUserMedia; 
}
 
if (hasUserMedia()) { 
   var handleStream = function (s) { 
   	  stream = s;
   	  var allTracts = s.getTracks();
   	  var t = (s.getVideoTracks())[0];
   	  var constraint = t.getConstraints();
      var video = document.querySelector('video');

      //insert stream into the video tag
      video.srcObject = s; 
      
   }
   
	var videoConstraints = {
			"mandatory":{"aspectRatio":1.3333333},   
			"facingMode":["user", "environment"],
			"optional":[{"width":{"min":640}},
				{"height":{"max":400}}]
		   }	
   //get both video and audio streams from user's camera 
   navigator.mediaDevices.getUserMedia({ video: videoConstraints, audio: false })
   .then(handleStream)
   .catch(function(error){console.log("getUserMedia error : "+error)});
}else {
   alert("Error. WebRTC is not supported!"); 
}

