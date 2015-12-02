package com.svlcthesis.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.svlcthesis.R;
import com.svlc.hieptran.reciever.ListData;
import com.svlc.hieptran.reciever.ORBMatching;
import com.svlc.hieptran.reciever.ORBMatching.myasyncend;

@SuppressWarnings("deprecation")
public class ReceiverActivity extends ActionBarActivity implements
		SurfaceHolder.Callback, Camera.PreviewCallback {
	SurfaceView surface;

	ImageView matframe, srcimg, copmare, result;
	TextView tv;
	ArrayList<String> list;
	ArrayAdapter<String> adap;
	ListView listview;
	public static ListData listdata1;

	private SurfaceHolder msurhold;
	public static Camera mcamera;
	public static Parameters mparas;
	private double starttime = 0, nowtime = 0;
	Bitmap bmp1, bmp2;
	Mat matYUV;
	Mat mGray, startframe, endframe;
	Mat src, cpr;
	Mat output;
	ArrayList<Mat> listframe = new ArrayList<Mat>();
	ArrayAdapter<String> madap;
	ORBMatching myorb;
	int hyub, wyub;
	boolean isfocus = false;
	public static boolean isflash = false;
	public static final String TAG = "HIEPGA";
	public static String time;
	private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {

				matYUV = new Mat();
				mGray = new Mat();
				output = new Mat();

				bmp1 = BitmapFactory.decodeResource(getResources(),
						R.drawable.startframe);
				startframe = new Mat(bmp1.getHeight(), bmp1.getWidth(),
						CvType.CV_8UC4);
				Utils.bitmapToMat(bmp1, startframe);

				bmp2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.dsc);
				endframe = new Mat(bmp2.getHeight(), bmp2.getWidth(),
						CvType.CV_8UC4);
				Utils.bitmapToMat(bmp2, endframe);
				myorb = new ORBMatching(ReceiverActivity.this, startframe,
						endframe);
				matYUV = new Mat(hyub, wyub, CvType.CV_8UC1);
			}

				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reciver);
		Log.d(TAG, "Oncreate");
		getSupportActionBar().hide();
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		surface = (SurfaceView) findViewById(R.id.mainsurfaceviewq);
		msurhold = surface.getHolder();
		msurhold.addCallback(this);

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this,
				mOpenCVCallBack);
	}
long t1;
	public void surfaceCreated(SurfaceHolder holder) {

		mcamera = Camera.open();
		mparas = mcamera.getParameters();
		mparas.setFocusMode(Parameters.FOCUS_MODE_AUTO);
		List<Size> list = mparas.getSupportedPreviewSizes();
		for (int i = 0; i < list.size(); i++) {
			Log.d("DMM", list.get(0).width + "-" + list.get(0).height);
		}
		hyub = (int) (list.get(0).height * 1.5);
		wyub = list.get(0).width;
		mparas.setPreviewSize(list.get(0).width, list.get(0).height);
		mparas.setPictureSize(list.get(0).width, list.get(0).height);
		mcamera.setParameters(mparas);

		try {
			mcamera.setPreviewDisplay(holder);
		} catch (IOException e) {
		}
		mcamera.setPreviewCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("TAG", "surfaceChanged");
		mcamera.startPreview();
		t1=System.currentTimeMillis();
		
		Log.d("TAG","batdau");
	}

	private ArrayList<Mat> listdata = new ArrayList<>();
	String t;

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("TAGD", "surfacedes");
		mcamera.setPreviewCallback(null);
		mcamera.stopPreview();
		mcamera.release();
		mcamera = null;
	}

	ArrayList<Mat> Camera_data = new ArrayList<>();
	boolean isligh = false;

	int test = 0;
	double ttt;
	boolean thuxong = true;
	boolean isstart = false;

	@SuppressWarnings("static-access")
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (starttime == 0) {
			starttime = System.currentTimeMillis();
		}

		if (!isstart && nowtime - starttime > 2000) {

			mcamera.autoFocus(myauto);
			starttime = 0;

			matYUV.put(0, 0, data);

		}
		if (isstart && myorb.isCheckend()) {

			flashligh();
			
				Log.d("TAG", " flash cua start ");
				if (!myorb.isEnd()) {
					matYUV.put(0, 0, data);
					Mat m = matYUV.clone();
					listdata.add(m);

				} else if (myorb.isEnd()) {
					Log.d("TAG", " thuxong ");
					listdata1.setListCamera(listdata);
					time = "Th�?i gian nhận ảnh : "+ String.valueOf((float)(System.currentTimeMillis()-t1)/1000);
					Intent i = new Intent(ReceiverActivity.this,
							RecoverData.class);
					startActivity(i);
					finish();

				}
				myorb.new myasyncend(matYUV).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

			}
		
		nowtime = System.currentTimeMillis();

	}

	AutoFocusCallback myauto = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			Log.d("TAG", "goi on auto");
			try {
				Thread.sleep(100);

			} catch (InterruptedException e) {

			}
			isstart = true;

		}
	};
	private void flashligh() {
		mparas = mcamera.getParameters();
		mparas.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mcamera.setParameters(mparas);

		try {
			Thread.sleep(100);

		} catch (InterruptedException e) {

		}
		mparas.setFlashMode(Parameters.FLASH_MODE_OFF);
		mcamera.setParameters(mparas);
	}

}
