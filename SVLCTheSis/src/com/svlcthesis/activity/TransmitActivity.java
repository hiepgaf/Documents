package com.svlcthesis.activity;

import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.svlcthesis.R;
import com.svlc.hieptran.transmit.EncodeData;

public class TransmitActivity extends ActionBarActivity {

	private EncodeData pc;
	private ImageView imv;
	private TextView tv;
	Bitmap bmp;
	double start, end;
	int[] black = { 0, 0, 0 };
	int[] white = { 255, 255, 255 };
	ArrayList<Integer> list = new ArrayList<Integer>();
	int count = 1;
	Thread th1;
	Handler mHandler = new Handler();
	SensorManager sensorManager;
	Sensor lightSensor;
	// public static boolean lightdetect = false;
	int k = 0;
	public static ArrayList<Bitmap> list_encoded = new ArrayList<Bitmap>();
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.d("tag", "OpenCV loaded successfully");
				pc = new EncodeData(1154, 615, TransmitActivity.this, path);
				pc.execute();

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
	public void onResume() {
		super.onResume();

	}

	int height, width;
	LinearLayout layoutmain;
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encode);
		getSupportActionBar().hide();

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		lp.screenBrightness = 1;
		Display disttt = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		disttt.getSize(size);
		width = size.x;
		height = size.y;
		getWindow().setAttributes(lp);
		// pc = new processCreat();
		path = getIntent().getStringExtra("FULLPATH");
		imv = (ImageView) findViewById(R.id.imv);

		// height = getWindowManager().getDefaultDisplay().getHeight();
		// width = getWindowManager().getDefaultDisplay().getWidth();
		Log.d("TAG", "h = " + height + "-w = " + width);
		// LayoutParams pra = new LayoutParams(width, height);
		layoutmain = (LinearLayout) findViewById(R.id.layoutmain);
		layoutmain.setPadding(42, 44, 42, 44);
		//THAYDOI
		layoutmain.setBackgroundColor(Color.GRAY);
		// layoutmain.setPadding(40, 40, 40, 40);
		// imv.setLayoutParams(pra);
		imv.setImageResource(R.drawable.startframe);
		list.add(0);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		if (lightSensor == null) {
			Toast.makeText(TransmitActivity.this, "No Light Sensor!",
					Toast.LENGTH_LONG).show();
		} else {
			float max = lightSensor.getMaximumRange();
			Log.v("Bright", "Bright Max: " + String.valueOf(max));
			sensorManager.registerListener(lightSensorEventListener,
					lightSensor, SensorManager.SENSOR_DELAY_UI);
		}

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this,
				mLoaderCallback);
	}

	int coutttss = 0;
	SensorEventListener lightSensorEventListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
				float currentReading = event.values[0];
				list.add((int) currentReading);
				count++;
				if (count > 3
						&& (list.get(count - 1) - list.get(count - 2)) > 25) {
					
					
					layoutmain.setPadding(52, 54, 52, 54);
				//	layoutmain.setBackgroundColor(Color.GRAY);//THAYDOI
				//	sensorManager.unregisterListener(lightSensorEventListener);
					
						if (k < list_encoded.size()) {
//							sensorManager.registerListener(
//									lightSensorEventListener, lightSensor,
//									SensorManager.SENSOR_DELAY_FASTEST);
							if(k==0)
							{
								imv.setBackgroundColor(Color.BLACK);
								imv.setImageBitmap(null);
								
							}
						
							else
							{
								
							imv.setImageBitmap(list_encoded.get(k));
							}
							k++;
							

						}

					
				}

				Log.d("TAG",
						"Độ sáng hiện tại là : "
								+ String.valueOf(currentReading)+" lux");
			}
		}
	};

}
