# nettyserver
test Server for android client 

这个服务程序提供了一些简单的接口

所有的提供的接口都定义在RetrofitServer这个类
```
private static final Router router = new Router().POST("/register", RegisterHandler.class)
			.POST("/login", LoginHandler.class).POST("addAccountRecord", AddAccountRecordHandler.class)
			.GET("accountRecordList", AccountRecordListHandler.class)
			.POST("/upload/image", UploadImageHandler.class)
			.GET("/getAccount", GetAccountInfoHandler.class)
			.GET("/getHtml/:path", GetHtmlHandler.class).GET("/getFile/:path", GetFileHandle.class)
			.GET("/getJson/:path", GetJsonHandler.class).GET("/getFileDir/:path", FileDownloadHandler.class)
			.GET("/getImage/:path", GetImageHandler.class);
	Handler handler = new Handler(router);
```

使用方法：<br>
运行RetrofitServer这个类，启动服务<br>
服务启动后，就可以调用接口获取信息了<br>
例如：<br>
在本机浏览器中输入：<br>
http://localhost:8080/getHtml/test1.html <br>
就可以打开一个html的测试页面
