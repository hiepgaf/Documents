package com.svlcthesis.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svlcthesis.R;
import com.svlc.hieptran.reciever.IOLib;
import com.svlc.hieptran.reciever.findcorner;

public class RecoverData extends ActionBarActivity {

	TextView mtv;
	ArrayList<Mat> list_Mat_camera = new ArrayList<Mat>();
	int height, width;
	Mat anhgoc, anhtrain;
	private double starttime;
	File sdCardDirectory;
	Point tl, tr, bl, br;
	StringBuilder stringrecover = new StringBuilder();
	ArrayList<String> strings = new ArrayList<String>();
	String pathroot;
	Mat scene_corners;
	protected StringBuilder builder = new StringBuilder();
	Button savefile;
	String ketquafinal;
	private File queryFile, objectFile;
	static {
		try {
			System.loadLibrary("opencv_java");
			System.loadLibrary("nonfree");
			System.loadLibrary("nonfree_jni");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
		}
	}

	private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				try {
					Bitmap bm = BitmapFactory.decodeResource(getResources(),
							R.drawable.startframe);

					anhgoc = new Mat(bm.getWidth(), bm.getHeight(),
							CvType.CV_8UC3);
					Utils.bitmapToMat(bm, anhgoc);
					Mat m = list_Mat_camera.get(0);
					int heighyuv = m.height();
					int widthyuv = m.width();
					int heightrgb = (int) (heighyuv / (1.5));
					Mat rgba = new Mat(widthyuv, heightrgb, CvType.CV_8UC4);
					Imgproc.cvtColor(m, rgba, Imgproc.COLOR_YUV420sp2RGBA, 4);
					anhtrain = rgba.clone();
				} catch (Exception e) {
				}
				scene_corners = new Mat(4, 1, CvType.CV_32FC2);
				new improc().execute();
			}

				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};
	private Dialog dialog;
	private LinkedList<DMatch> good_matches;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encode_data);
		dialog = new Dialog(RecoverData.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogview);
		list_Mat_camera = ReceiverActivity.listdata1.getListCamera();
		mtv = (TextView) findViewById(R.id.textview);
		starttime = System.currentTimeMillis();
		sdCardDirectory = new File(Environment.getExternalStorageDirectory()
				+ File.separator
				+ "ABSVLC"
				+ File.separator
				+ "Test_"
				+ IOLib.convertDate(String.valueOf(System.currentTimeMillis()),
						"dd-MM-yy hh-mm-ss a"));
		sdCardDirectory.mkdirs();
		builder.append(ReceiverActivity.time + "\n");
		savefile = (Button) findViewById(R.id.buttonsave);
		final EditText ed = (EditText) findViewById(R.id.filesavename);
		savefile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IOLib.writeTexttoFile(sdCardDirectory.getAbsolutePath(), ed.getText().toString(), ketquafinal);
				Toast.makeText(RecoverData.this, "Dữ liệu đã được lưu: "+sdCardDirectory.getAbsolutePath()+"/"+ed.getText().toString(), Toast.LENGTH_LONG).show();
				finish();
				Intent i = new Intent(RecoverData.this, MainActivity.class);
				startActivity(i);
			}
		});
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this,
				mOpenCVCallBack);
	}

	public String output2() {

		pathroot = sdCardDirectory.getAbsolutePath();
		long t1 = System.currentTimeMillis();

		Mat outpp1 = findcorner.getRGBfromYUVper(list_Mat_camera.get(0),
				scene_corners);
		IOLib.writeMattofile(pathroot, "Aframe_1", outpp1);
		Mat outpp2 = findcorner.getRGBfromYUVper(
				list_Mat_camera.get(list_Mat_camera.size() - 1), scene_corners);
		IOLib.writeMattofile(pathroot, "Aframe_end", outpp2);
		Mat dat1 = findcorner.getRGBfromYUVper(list_Mat_camera.get(1),
				scene_corners);
		Mat d2 = dat1.clone();
		//Imgproc.cvtColor(dat1, dat1, Imgproc.COLOR_RGB2GRAY);
		findcorner find = new findcorner(dat1);
		find.findcorners(dat1);
		IOLib.writeMattofile(pathroot, "AReference", find.drawcorner(d2));
		for (int i = 2; i < list_Mat_camera.size() - 1; i++) {
			Mat rgb = new Mat();
//			IOLib.writeMattofile(pathroot, "khunghinh" + i,
//					list_Mat_camera.get(i));
			rgb = findcorner.getRGBfromYUVper(list_Mat_camera.get(i),
					scene_corners);
//			IOLib.writeMattofile(pathroot, "khunghinhsaucat" + i,
//					rgb);
			//IOLib.writeMattofile(MainActivity.PATHROOT, "Bredata_" + i, rgb);
//			IOLib.writeMattofile(pathroot, "Cdetectcorrner" + i,
//					find.drawcorner(rgb));
			//
			Mat m1 = find.getperspective(rgb);
//			IOLib.writeMattofile(pathroot, "Ddata_" + i, m1);
			Imgproc.threshold(m1, m1, 180, 255, Imgproc.THRESH_BINARY);
			for (int ik = 0; ik < m1.rows(); ik += 11)
				for (int j = 0; j < m1.cols(); j += 11) {
					recoverydata(ik, j, m1);
				}

		}
		builder.append("Thoi gian khoi phuc : "
				+ String.valueOf((float) (System.currentTimeMillis() - t1) / 1000)
				+ " s\n");
		return stringrecover.toString();
	}

	private void detect_conner_obj(Mat camera, Mat object) {
		long startime = System.currentTimeMillis();
		MatOfPoint2f obj;
		MatOfPoint2f scene;

		Mat descriptors_camera;
		Mat descriptors_object;
		DescriptorExtractor DesExtractor;
		FeatureDetector detector;
		DescriptorMatcher DesMatcher;

		MatOfKeyPoint camerakey, objectkey;
		MatOfDMatch startmatches;

		detector = FeatureDetector.create(FeatureDetector.ORB);
		DesExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		DesMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
		camerakey = new MatOfKeyPoint();
		detector.detect(camera, camerakey);
		descriptors_camera = new Mat();
		DesExtractor.compute(camera, camerakey, descriptors_camera);
		// initstart
		objectkey = new MatOfKeyPoint();
		detector.detect(object, objectkey);
		descriptors_object = new Mat();
		DesExtractor.compute(object, objectkey, descriptors_object);

		double max_dist = 0;
		double min_dist = 99;
		startmatches = new MatOfDMatch();
		// matching descriptors
		DesMatcher.match(descriptors_object, descriptors_camera, startmatches);

		// New method of finding best matches
		List<DMatch> matchesList = startmatches.toList();

		// -- Quick calculation of max and min distances between keypoints
		for (int i = 0; i < descriptors_object.rows(); i++) {
			double dist = matchesList.get(i).distance;
			if (dist < min_dist)
				min_dist = dist;
			if (dist > max_dist)
				max_dist = dist;
		}
		good_matches = new LinkedList<DMatch>();

		for (int i = 0; i < matchesList.size(); i++) {
			if (matchesList.get(i).distance <= min_dist * 3) {
				good_matches.addLast(matchesList.get(i));
			}
		}
		builder.append("Object " + objectkey.toList().size() + "\n Scene"
				+ camerakey.toList().size());
		builder.append("\n Total time comapre "
				+ (float) (System.currentTimeMillis() - startime) / 1000);
		Log.d("TAG",
				"-" + good_matches.size() + " - mindist = " + min_dist
						+ "- matches = " + matchesList.size()
						+ "\n Total time comapre "
						+ (float) (System.currentTimeMillis() - startime)
						/ 1000);

		// put keypoints mats into lists
		List<KeyPoint> keypoints1_List = objectkey.toList();
		List<KeyPoint> keypoints2_List = camerakey.toList();

		// put keypoints into point2f mats so calib3d can use them to find
		// homography
		LinkedList<Point> objList = new LinkedList<Point>();
		LinkedList<Point> sceneList = new LinkedList<Point>();
		for (int i = 0; i < good_matches.size(); i++) {
			objList.addLast(keypoints1_List.get(good_matches.get(i).queryIdx).pt);
			sceneList
					.addLast(keypoints2_List.get(good_matches.get(i).trainIdx).pt);
		}
		obj = new MatOfPoint2f();
		scene = new MatOfPoint2f();
		obj.fromList(objList);
		scene.fromList(sceneList);
		Mat hg = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);

		Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);

		obj_corners.put(0, 0, new double[] { 0, 0 });
		obj_corners.put(1, 0, new double[] { object.cols(), 0 });
		obj_corners.put(2, 0, new double[] { object.cols(), object.rows() });
		obj_corners.put(3, 0, new double[] { 0, object.rows() });

		// obj_corners:input
		Core.perspectiveTransform(obj_corners, scene_corners, hg);
		Point p1 = new Point(scene_corners.get(0, 0));
		Point p2 = new Point(scene_corners.get(1, 0));
		Point p3 = new Point(scene_corners.get(2, 0));
		Point p4 = new Point(scene_corners.get(3, 0));
		Core.circle(camera, p1, 4, new Scalar(0, 255, 0), 3);
		Core.circle(camera, p2, 4, new Scalar(0, 255, 0), 3);
		Core.circle(camera, p3, 4, new Scalar(0, 255, 0), 3);
		Core.circle(camera, p4, 4, new Scalar(0, 255, 0), 3);

	
	}

	public void recoverydata(int x, int y, Mat m) {

		int count0 = 0, count1 = 0;
		for (int i = x; i < x + 10; i++)
			// row
			for (int j = y; j < y + 10; j++) {// col
				double db[] = m.get(i, j);
				if (db != null) {
					if (db[0] > 0)
						count1++;
					else
						count0++;
				}
			}
		if (count0 > count1)
			stringrecover.append("0");
		else
			stringrecover.append("1");
		count0 = 0;
		count1 = 0;
	}

	class improc extends AsyncTask<Void, String, Void> {

		long tcp0 = 0;
		int keyquery = 0, keytrain = 0, matches = 0;

		@Override
		protected Void doInBackground(Void... params) {
			// THAYDOI _ HIEP
			getCompare(anhgoc.getNativeObjAddr(), anhtrain.getNativeObjAddr(),
					scene_corners.getNativeObjAddr());
			// detect_conner_obj(anhtrain, anhgoc);
			String timecp = String
					.valueOf((float) (System.currentTimeMillis() - tcp0) / 1000);
			output2();
			builder.append("Thời gian so sánh : " + timecp + " s \n");

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			tcp0 = System.currentTimeMillis();
			dialog.show();
			Log.d("TAG","On Pre");
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			showToast("Khôi phục xong !");
			dialog.dismiss();
			IOLib.writeMattofile(Environment.getExternalStorageDirectory()
					+ File.separator + "ABSVLC", "rgbgoc", anhgoc);
			IOLib.writeMattofile(Environment.getExternalStorageDirectory()
					+ File.separator + "ABSVLC", "rgbtrain", anhtrain);
			// IOLib.writeMattofile(
			// Environment.getExternalStorageDirectory()
			// + File.separator + "ABSVLC", "anhcatrgbtrain",
			// findcorner.getRGBfromYUVper(anhtrain,scene_corners));
			// builder.append("Tổng thời gian : "
			// + String.valueOf((float) (System.currentTimeMillis() - starttime)
			// / 1000 + " s \n"));
			builder.append("Số keypoints khung phát hiện : " + getquerykey()
					+ "\n");
			builder.append("Số keypoints khung camera : " + gettrainkey()
					+ "\n");
			builder.append("Số điểm trùng khớp : " + getmatches() + "\n");

			mtv.setText(
					"Kết quả khôi phục \n"+ unescapeJava(decodetext(stringrecover.toString())));
			ketquafinal = unescapeJava(decodetext(stringrecover.toString()));
			String datare = "Sau khi giải mã :\n\t "
					+ unescapeJava(decodetext(stringrecover.toString()))
					+ "\nTrước khi giải mã unicode : \n\t"
					+ decodetext(stringrecover.toString());
			String bitstream = "\n\nDữ liệu bit : \n\t"
					+ stringrecover.toString();
			String thongtin = builder.toString()
					+ "Số kí tự khôi phục được : "
					+ unescapeJava(decodetext(stringrecover.toString()))
							.length();
			String out = thongtin + "\n" + datare + bitstream;
			IOLib.writeTexttoFile(pathroot, "AAKết quả", out);

			Log.d("TAG", "Ket cmn qua : "
					+ unescapeJava(decodetext(stringrecover.toString())));
		}

	}

	void showToast(String s) {
		Toast.makeText(RecoverData.this, s, Toast.LENGTH_SHORT).show();
	}

	String decodetext(String input) {
		String s2 = "";
		char nextChar;

		for (int i = 0; i <= input.length() - 8; i += 8) {
			String tmp = input.substring(i, i + 8);

			nextChar = (char) Integer.parseInt(tmp, 2);
			s2 += nextChar;
		}
		return s2;
	}

	public static String unescapeJava(String escaped) {
		if (escaped.indexOf("\\u") == -1)
			return escaped;

		String processed = "";

		int position = escaped.indexOf("\\u");
		while (position != -1) {
			if (position != 0)
				processed += escaped.substring(0, position);
			String token = escaped.substring(position + 2, position + 6);
			try {
				processed += (char) Integer.parseInt(token, 16);
			} catch (Exception e) {
				Log.d("TAG", token);
			}
			escaped = escaped.substring(position + 6);
			position = escaped.indexOf("\\u");
		}
		processed += escaped;

		return processed;
	}

	public static native void getSIFT(String addrImage, long addrKeypoint,
			long addrDescriptors, int minhessian);

	public static native int gettrainkey();

	public static native int getquerykey();

	public static native int getmatches();

	public static native void getCompare(long addrQuery, long addTrain,
			long addrCorrner);
}
