package com.lee.server.retrofit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

	public static void saveImageFile(String filePath, byte[] imageBytes) {
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File imageFile = new File("test.jpg");
		// 创建输出流
		FileOutputStream outStream = null;
		// 写入数据
		try {
			outStream = new FileOutputStream(imageFile, true);
			outStream.write(imageBytes);
			System.out.println("save image success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("save image fail");
		} finally {
			// 关闭输出流
			try {
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
