package com.svlc.hieptran.reciever;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class ListData {
	public static ArrayList<Mat> ListCamera;

	
	public static ArrayList<Mat> getListCamera() {
		return ListCamera;
	}

	public static void setListCamera(ArrayList<Mat> listCamera) {
		ListCamera = listCamera;
	}
}
