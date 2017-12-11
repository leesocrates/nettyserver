package com.lee.server.retrofit.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	static int length = 0;

	public static void saveImageFile(String filePath, byte[] imageBytes) {
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File imageFile = new File("test.jpg");
		// 创建输出流
		FileOutputStream outStream = null;
		length += imageBytes.length;
		System.out.println("has written length : " + length);
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

	public static byte[] getFileContent(String filePath) {
		byte[] bytes = null;
		FileInputStream in = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				System.out.println("file not exist");
			}
			in = new FileInputStream(filePath);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.out.println("bytes available:" + in.available());

			byte[] temp = new byte[1024];

			int size = 0;

			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}

			bytes = out.toByteArray();
			System.out.println("bytes size got is:" + bytes.length);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	public static byte[] getContentFromStream(InputStream in){
		if(in ==null){
			return new byte[0];
		}
		byte[] bytes = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			System.out.println("bytes available:" + in.available());

			byte[] temp = new byte[1024];

			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			bytes = out.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
}
