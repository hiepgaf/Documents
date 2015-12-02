package com.svlc.hieptran.reciever;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.text.format.DateFormat;
import android.util.Log;

public class IOLib {
	public static void writeMattofile(String path, String name, Mat m) {
		try {

			Bitmap out = Bitmap.createBitmap(m.cols(), m.rows(),
					Config.ARGB_8888);
			Utils.matToBitmap(m, out);
			File image = new File(path, name + ".jpg");
			FileOutputStream outStream;

			outStream = new FileOutputStream(image);
			out.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			/* 100 to keep full quality of the image */
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * Phương thức đọc dữ liệu văn bản từ File
	 * @author Mr.G
	 * @param fullpath Đường dẫn đến file
	 * @return {@value text} Kết quả trả về
	 */
	public static String ReadTextFromtxtFile(String fullpath) {
		
		StringBuilder text = new StringBuilder();
		try {
		     
		     File file = new File(fullpath);

		     BufferedReader br = new BufferedReader(new FileReader(file));  
		     String line;   
		     while ((line = br.readLine()) != null) {
		                text.append(line);
		                text.append('\n');
		     }
		     br.close() ;
		 }catch (IOException e) {
		    e.printStackTrace();           
		 }
		Log.d("TAG", text.toString());
		return text.toString();
	}
	

	public static String convertDate(String dateInMilliseconds,
			String dateFormat) {
		return DateFormat
				.format(dateFormat, Long.parseLong(dateInMilliseconds))
				.toString();
	}
	
 

	public static void writeTexttoFile(String path, String name, String context) {
		try {

			File file = new File(path, name + ".txt");

			// If file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(context);
			bw.close();

			Log.d("TAG", "Sucess");

		} catch (IOException e) {
			Log.d("TAG", e.getMessage());
			e.printStackTrace();

		}
	}
}
