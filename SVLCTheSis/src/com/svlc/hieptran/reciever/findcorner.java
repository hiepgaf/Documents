package com.svlc.hieptran.reciever;

import java.util.ArrayList;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.svlc.hieptran.transmit.EncodeData;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.widget.ImageView;

public class findcorner {
	public static Point tl;
	public static Point tr;
	public static Point bl;
	public static Point br;
	private StringBuilder builder;
	public static final Scalar GREEN = new Scalar(0, 255, 0);
	public static final Scalar RED = new Scalar(255, 0, 0);
	public static final Scalar BLUE = new Scalar(0, 0, 255);
	public static final Scalar YELLOW = new Scalar(255, 255, 0);
	Mat mm;

	public findcorner(Mat m) {
		mm = m;
		builder = new StringBuilder();

	}

	public String getinfo() {
		return builder.toString();
	}

	public void findcorners(Mat m) {
		// Imgproc.threshold(m, m, 180, 255, Imgproc.THRESH_BINARY);
		findtopleft(m);
		findbotleft(m);
		findbotrught(m);
		findtopright(m);
	}

	void findtopleft(Mat m2) {
		Mat m1 = m2.clone();

		// Imgproc.cvtColor(m1, m1, Imgproc.COLOR_RGB2GRAY);

		int cc = 0;
		ArrayList<Double> dat = new ArrayList<Double>();
		for (int r = 0; r < 20; r++) {
			for (int c = 0; c < 20; c++) {
				double dd[] = m1.get(r, c);
				builder.append(dd[0] + " ");
				dat.add(dd[0]);
			}
			builder.append("\t");

		}
		for (int i = 21; i < dat.size(); i++) {

			if (Math.abs(dat.get(i) - dat.get(i - 20)) > 40
					&& Math.abs(dat.get(i) - dat.get(i - 1)) > 25) {

				cc = i;
				break;
			}
		}

		int cs = cc % 20;
		int rs = cc / 20;
		builder.append("ket qua : " + "cot " + cs + "- hang : " + rs);
		tl = new Point(cs, rs);

	}

	void findtopright(Mat m2) {
		Mat m1 = m2.clone();

		// Imgproc.cvtColor(m1, m1, Imgproc.COLOR_RGB2GRAY);

		int cc = 0;
		ArrayList<Double> dat = new ArrayList<Double>();
		for (int r = 0; r < 20; r++) {
			for (int c = m1.cols() - 1; c > m1.cols() - 21; c--) {
				double dd[] = m1.get(r, c);
				dat.add(dd[0]);
			}

		}
		for (int i = 21; i < dat.size(); i++) {

			if (Math.abs(dat.get(i) - dat.get(i - 20)) > 20
					&& Math.abs(dat.get(i) - dat.get(i - 1)) > 20) {

				cc = i;
				break;
			}
		}

		int cs = m1.cols() - cc % 20;
		int rs = cc / 20;
		tr = new Point(cs, rs);
	}

	void findbotleft(Mat m2) {
		Mat m1 = m2.clone();

		// Imgproc.cvtColor(m1, m1, Imgproc.COLOR_RGB2GRAY);

		int cc = 0;
		ArrayList<Double> dat = new ArrayList<Double>();
		for (int r = m1.rows() - 1; r > m1.rows() - 21; r--) {
			for (int c = 0; c < 20; c++) {
				double dd[] = m1.get(r, c);
				dat.add(dd[0]);
			}

		}
		for (int i = 21; i < dat.size(); i++) {

			if (Math.abs(dat.get(i) - dat.get(i - 20)) > 30
					&& Math.abs(dat.get(i) - dat.get(i - 1)) > 25) {

				cc = i;
				break;
			}
		}

		int cs = cc % 20;
		int rs = m1.rows() - cc / 20;

		bl = new Point(cs, rs);
	}

	void findbotrught(Mat m2) {
		Mat m1 = m2.clone();

		// Imgproc.cvtColor(m1, m1, Imgproc.COLOR_RGB2GRAY);

		int cc = 0;
		ArrayList<Double> dat = new ArrayList<Double>();
		for (int r = m2.rows() - 1; r > m1.rows() - 21; r--) {
			for (int c = m2.cols() - 1; c > m1.cols() - 21; c--) {
				double dd[] = m1.get(r, c);
				dat.add(dd[0]);
			}

		}
		for (int i = 21; i < dat.size(); i++) {

			if (Math.abs(dat.get(i) - dat.get(i - 20)) > 30
					&& Math.abs(dat.get(i) - dat.get(i - 1)) > 30) {

				cc = i;
				break;
			}
		}

		int cs = m1.cols() - cc % 20;
		int rs = m1.rows() - cc / 20;

		br = new Point(cs, rs);
	}

	public static Mat getRGBfromYUVper(Mat m, Mat scenecorner)

	{
		int heighyuv = m.height();
		int widthyuv = m.width();
		int heightrgb = (int) (heighyuv / (1.5));
		Mat rgba = new Mat(widthyuv, heightrgb, CvType.CV_8UC4);
		Imgproc.cvtColor(m, rgba, Imgproc.COLOR_YUV420sp2RGBA, 4);
		Mat outpp = new Mat(1154, 615, CvType.CV_8UC3);

		Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);

		obj_corners.put(0, 0, new double[] { 0, 0 });
		obj_corners.put(1, 0, new double[] { outpp.cols(), 0 });
		obj_corners.put(2, 0, new double[] { outpp.cols(), outpp.rows() });
		obj_corners.put(3, 0, new double[] { 0, outpp.rows() });
		Mat trasmiz = Imgproc.getPerspectiveTransform(scenecorner, obj_corners);
		Imgproc.warpPerspective(rgba, outpp, trasmiz, outpp.size());
		return outpp;
	}

	public static Mat getRGBfromYUV(Mat m)

	{
		int heighyuv = m.height();
		int widthyuv = m.width();
		int heightrgb = (int) (heighyuv / (1.5));
		Mat rgba = new Mat(widthyuv, heightrgb, CvType.CV_8UC4);
		Imgproc.cvtColor(m, rgba, Imgproc.COLOR_YUV420sp2RGBA, 4);
		Mat outpp = new Mat(1154, 615, CvType.CV_8UC3);
		return outpp;
	}

	public Mat getperspective(Mat m2) {
		Imgproc.cvtColor(m2, m2, Imgproc.COLOR_RGB2GRAY);
		Mat outpp = new Mat(1154, 615, CvType.CV_8UC4);
		Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
		obj_corners.put(0, 0, new double[] { 0, 0 });
		obj_corners.put(1, 0, new double[] { outpp.cols(), 0 });
		obj_corners.put(2, 0, new double[] { outpp.cols(), outpp.rows() });
		obj_corners.put(3, 0, new double[] { 0, outpp.rows() });
		Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);
		scene_corners.put(0, 0, new double[] { tl.x, tl.y });
		scene_corners.put(1, 0, new double[] { tr.x, tr.y });
		scene_corners.put(2, 0, new double[] { br.x, br.y });
		scene_corners.put(3, 0, new double[] { bl.x, bl.y });
		Mat trasmiz = Imgproc.getPerspectiveTransform(scene_corners,
				obj_corners);
		Imgproc.warpPerspective(m2, outpp, trasmiz, outpp.size());
		return outpp;
	}

	public Mat drawcorner(Mat m3) {
		Mat m2 = m3.clone();
		Core.circle(m2, tr, 3, RED, 3);
		Core.circle(m2, br, 3, GREEN, 3);
		Core.circle(m2, bl, 3, YELLOW, 3);
		Core.circle(m2, tl, 3, BLUE, 3);
		return m2;

	}

	public void setimg(Mat m, ImageView img) {
		Bitmap bmp = Bitmap.createBitmap(m.cols(), m.rows(), Config.ARGB_8888);
		Utils.matToBitmap(m, bmp);
		img.setImageBitmap(bmp);
	}
}
