//var https=require('https');
//var ws=require('ws');
//var fs=require('fs');
//var keypath=process.cwd()+'/server.key';//我把秘钥文件放在运行命令的目录下测试
//var certpath=process.cwd()+'/server.crt';//console.log(keypath);
////console.log(certpath);
// 
//var options = {
// key: fs.readFileSync(keypath),
// cert: fs.readFileSync(certpath),
//
//};
// 
//var server=https.createServer(options, function (req, res) {//要是单纯的https连接的话就会返回这个东西
// res.writeHead(403);//403即可
// res.end("This is a WebSockets server!");
//}).listen(9090);
// 
// 
//var wss = new ws.Server( { server: server } );//把创建好的https服务器丢进websocket的创建函数里，ws会用这个服务器来创建wss服务
////同样，如果丢进去的是个http服务的话那么创建出来的还是无加密的ws服务
////wss.on( 'connection', function ( wsConnect ) {
//// wsConnect.on( 'message', function ( message ) {
////  console.log( message );
//// });
////});




//require our websocket library 
var WebSocketServer = require('ws').Server; 

//creating a websocket server at port 9090 
var wss = new WebSocketServer({port: 9090}); 
 
//all connected to the server users
var users = {};

//when a user connects to our sever 
wss.on('connection', function(connection) { 
   console.log("user connected");
   
   connection.on("error", function(error){
	   console.log("connection to user "+connection.name +" has a error :"+error);
   });
	
   //when server gets a message from a connected user 
   connection.on('message', function(message){
	   var data; 
		
	   //accepting only JSON messages 
	   try { 
	      data = JSON.parse(message); 
	   } catch (e) { 
	      console.log("Invalid JSON"); 
	      data = {}; 
	   }
		
	   //switching type of the user message 
	   switch (data.type) { 
	      //when a user tries to login 
	      case "login": 
	         console.log("User logged:", data.name); 
				
	         //if anyone is logged in with this username then refuse 
	         if(users[data.name]) { 
	            sendTo(connection, { 
	               type: "login", 
	               success: false 
	            }); 
	         } else { 
	            //save user connection on the server 
	            users[data.name] = connection; 
	            connection.name = data.name; 
					
	            sendTo(connection, { 
	               type: "login", 
	               success: true 
	            });
					
	         } 
				
	         break;
	      case "offer": 
	    	   //for ex. UserA wants to call UserB 
	    	   console.log("Sending offer to: ", data.name); 
	    		
	    	   //if UserB exists then send him offer details 
	    	   var conn = users[data.name]; 
	    		
	    	   if(conn != null){ 
	    	      //setting that UserA connected with UserB 
	    	      connection.otherName = data.name; 
	    			
	    	      sendTo(conn, { 
	    	         type: "offer", 
	    	         offer: data.offer, 
	    	         name: connection.name 
	    	      }); 
	    	   }
	    		
	    	   break;
	      case "answer": 
	    	   console.log("Sending answer to: ", data.name); 
	    		
	    	   //for ex. UserB answers UserA 
	    	   var conn = users[data.name]; 
	    		
	    	   if(conn != null) { 
	    	      connection.otherName = data.name; 
	    	      sendTo(conn, { 
	    	         type: "answer", 
	    	         answer: data.answer 
	    	      }); 
	    	   }
	    		
	    	   break;	
	      case "candidate": 
	    	   console.log("Sending candidate to:",data.name); 
	    	   var conn = users[data.name]; 
	    		
	    	   if(conn != null) {
	    	      sendTo(conn, { 
	    	         type: "candidate", 
	    	         candidate: data.candidate 
	    	      }); 
	    	   }
	    		
	    	   break;
	      case "leave": 
	    	   console.log("Disconnecting from", data.name); 
	    	   var conn = users[data.name]; 
	    	   conn.otherName = null; 
	    		
	    	   //notify the other user so he can disconnect his peer connection 
	    	   if(conn != null) { 
	    	      sendTo(conn, { 
	    	         type: "leave",
	    	         name:connection.name
	    	      }); 
	    	   } 
	    		
	    	   break;
	      default: 
	         sendTo(connection, { 
	            type: "error", 
	            message: "Command no found: " + data.type 
	         }); 
				
	         break; 
	   } 
	   
	   connection.on("close", function() { 

		   if(connection.name) { 
		      delete users[connection.name]; 
				
		      if(connection.otherName) { 
		         console.log("Disconnecting from ", connection.otherName); 
		         var conn = users[connection.otherName]; 
		         if(conn.otherName){
		        	 //如果一方发送了offer，另一方没有发送answer，这里otherName字段是undefined，赋值null会报错
		        	 conn.otherName = null;
						
			         if(conn != null) { 
			            sendTo(conn, { 
			               type: "leave" 
			            }); 
			         }  
		         }
		         
		      } 
		   } 
		});
	   
	  
	});
	
}); 

function sendTo(connection, message) { 
    connection.send(JSON.stringify(message)); 
}
	