package com.svlc.hieptran.reciever;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;

import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ORBMatching {

	private Mat mdetect, mend, descriptors_start, descriptors_end;
	private DescriptorExtractor DesExtractor;
	private FeatureDetector detector;
	private DescriptorMatcher DesMatcher;
	//static Rect rec;
	private MatOfKeyPoint startkey, endkey, keyofcamgoc;
	//private MatOfDMatch startmatches, startfinal_matches;
	private MatOfDMatch endmatches, endfinal_matches;
	
	private Context mcon;
	
	static boolean misStart, isEnd;
	LinkedList<DMatch> good_matches;
	
	// public static Mat scene_corners;
	public static boolean checkend;

	public static boolean isCheckend() {
		return checkend;
	}

	public static void setCheckend(boolean checkend) {
		ORBMatching.checkend = checkend;
	}

	public ORBMatching(Context con, Mat detect, Mat end) {
		mcon = con;
		mdetect = detect;
		mend = end;
		
		// init
		detector = FeatureDetector.create(FeatureDetector.ORB);

		DesExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		DesMatcher = DescriptorMatcher
				.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		endkey = new MatOfKeyPoint();
		detector.detect(mend, endkey);
		descriptors_end = new Mat();
		DesExtractor.compute(mend, endkey, descriptors_end);
		// initstart
		// startkey = new MatOfKeyPoint();
		// detector.detect(mdetect, startkey);
		setCheckend(true);
		// descriptors_start = new Mat();
		// DesExtractor.compute(mdetect, startkey, descriptors_start);
		// scene_corners = new Mat(4, 1, CvType.CV_32FC2);
	}

	public static boolean isEnd() {
		return isEnd;
	}

	public static void setEnd(boolean isEnd) {
		ORBMatching.isEnd = isEnd;
	}

	double tttttt;

	public class myasyncend extends AsyncTask<Mat, Void, Void> {
		Mat cameraend;
		Mat descriptors_cam_end;

		public myasyncend(Mat camera) {
			setCheckend(false);
			Log.d("TAG", "Bat dau end");
			tttttt = System.currentTimeMillis();
			cameraend = camera;
			MatOfKeyPoint camerakey_end = new MatOfKeyPoint();
			detector.detect(cameraend, camerakey_end);
			descriptors_cam_end = new Mat();
			DesExtractor.compute(camera, camerakey_end, descriptors_cam_end);
		}

		@Override
		protected Void doInBackground(Mat... params) {
			compareend(descriptors_cam_end);
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			List<DMatch> finalMatchesList = endfinal_matches.toList();
			final int matchesFound = finalMatchesList.size();

			if (matchesFound > 10 && matchesFound < 400) {
				
				setEnd(true);

			} else {
				setEnd(false);

			}
			setCheckend(true);
			Log.d("TAG",
					"ket thuc end /; "
							+ String.valueOf((float)(System.currentTimeMillis()
									- tttttt)/1000));
		}
	}

	private void compareend(Mat disendcam) {
		double max_dist = 0;
		double min_dist = 99;
		endmatches = new MatOfDMatch();
		// matching descriptors
		DesMatcher.match(descriptors_end, disendcam, endmatches);
		Log.d("TAG", "Matches Size end " + endmatches.size());
		// New method of finding best matches
		List<DMatch> matchesList = endmatches.toList();
		for (int i = 0; i < descriptors_end.rows(); i++) {
			double dist = matchesList.get(i).distance;
			if (dist < min_dist)
				min_dist = dist;
			if (dist > max_dist)
				max_dist = dist;
		}

		List<DMatch> matches_final = new ArrayList<DMatch>();
		for (int i = 0; i < matchesList.size(); i++) {
			if (matchesList.get(i).distance < min_dist * 3) {
				matches_final.add(endmatches.toList().get(i));
			}
		}
		endfinal_matches = new MatOfDMatch();
		endfinal_matches.fromList(matches_final);
	}
	// public class myasyncstart extends AsyncTask<Void, Void, Void> {
	// // private ImageView mresult;
	// private Mat camerast;
	// private Mat descriptors_cam_start;
	// private MatOfKeyPoint camerakeystart;
	//
	// public myasyncstart(Mat camera) {
	// camerast = camera;
	// // mresult = mres;
	// camerakeystart = new MatOfKeyPoint();
	// detector.detect(camerast, camerakeystart);
	// descriptors_cam_start = new Mat();
	// DesExtractor.compute(camerast, camerakeystart,
	// descriptors_cam_start);
	// keyofcamgoc = camerakeystart;
	// }
	//
	// @Override
	// protected Void doInBackground(Void... params) {
	// comparestart(descriptors_cam_start);
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// super.onPostExecute(result);
	// List<DMatch> finalMatchesList = startfinal_matches.toList();
	// final int matchesFound = finalMatchesList.size();
	// Log.d("TAG", "size - start : " + matchesFound);
	// if (matchesFound > 3 && matchesFound < 400) {
	//
	// // detect_conner_obj(camerast);
	// Toast.makeText(mcon, "Phát hiện dữ liệu!", Toast.LENGTH_SHORT)
	// .show();
	// setisStart(true);
	//
	// } else {
	// setisStart(false);
	// }
	// }
	// }

	// private void comparestart(Mat desccam) {
	// double max_dist = 0;
	// double min_dist = 99;
	// startmatches = new MatOfDMatch();
	// DesMatcher.match(descriptors_start, desccam, startmatches);
	// List<DMatch> matchesList = startmatches.toList();
	// for (int i = 0; i < descriptors_start.rows(); i++) {
	// double dist = matchesList.get(i).distance;
	// if (dist < min_dist)
	// min_dist = dist;
	// if (dist > max_dist)
	// max_dist = dist;
	// }
	// good_matches = new LinkedList<DMatch>();
	// List<DMatch> matches_final = new ArrayList<DMatch>();
	// for (int i = 0; i < matchesList.size(); i++) {
	// if (matchesList.get(i).distance <= min_dist * 3) {
	// matches_final.add(startmatches.toList().get(i));
	// good_matches.addLast(matchesList.get(i));
	// }
	// }
	//
	// startfinal_matches = new MatOfDMatch();
	// startfinal_matches.fromList(matches_final);
	// }

	// public void detect_conner_obj(Mat camera) {
	//
	// MatOfPoint2f obj;
	// MatOfPoint2f scene;
	// List<KeyPoint> keypoints1_List = startkey.toList();
	// List<KeyPoint> keypoints2_List = keyofcamgoc.toList();
	// LinkedList<Point> objList = new LinkedList<Point>();
	// LinkedList<Point> sceneList = new LinkedList<Point>();
	// for (int i = 0; i < good_matches.size(); i++) {
	// objList.addLast(keypoints1_List.get(good_matches.get(i).queryIdx).pt);
	// sceneList
	// .addLast(keypoints2_List.get(good_matches.get(i).trainIdx).pt);
	// }
	// obj = new MatOfPoint2f();
	// scene = new MatOfPoint2f();
	// obj.fromList(objList);
	// scene.fromList(sceneList);
	// Mat hg = Calib3d.findHomography(obj, scene, Calib3d.RANSAC, 5);
	// Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
	// obj_corners.put(0, 0, new double[] { 0, 0 });
	// obj_corners.put(1, 0, new double[] { mdetect.cols(), 0 });
	// obj_corners.put(2, 0, new double[] { mdetect.cols(), mdetect.rows() });
	// obj_corners.put(3, 0, new double[] { 0, mdetect.rows() });
	// Core.perspectiveTransform(obj_corners, scene_corners, hg);
	//
	// }

	// public static void setisStart(boolean is) {
	// misStart = is;
	// }
	//
	// public static boolean getisStart() {
	// return misStart;
	// }
}
