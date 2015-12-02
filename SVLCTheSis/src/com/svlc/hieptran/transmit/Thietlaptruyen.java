package com.svlc.hieptran.transmit;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.svlcthesis.R;
import com.example.svlcthesis.R.id;
import com.example.svlcthesis.R.layout;
import com.svlc.hieptran.reciever.IOLib;
import com.svlcthesis.activity.TransmitActivity;

public class Thietlaptruyen extends Activity {
	Button bt, done;
	TextView tenfile, duongdan, sokitu, soanh,sokitued,soanhed;
	LinearLayout layoutchon, layoutnhap;
	RadioGroup rdogr;
	int chon;
	EditText textinput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thietlaptruyen);
		bt = (Button) findViewById(R.id.chonfiletruyen);
		done = (Button) findViewById(R.id.xongxuoi);
		tenfile = (TextView) findViewById(R.id.tenfile);
		duongdan = (TextView) findViewById(R.id.duongdan);
		sokitu = (TextView) findViewById(R.id.sokitu);
		soanh = (TextView) findViewById(R.id.soluonganh);
		sokitued = (TextView) findViewById(R.id.sokitued);
		soanhed = (TextView) findViewById(R.id.soluonganhed);
		layoutchon = (LinearLayout) findViewById(R.id.layoutchon);
		layoutnhap = (LinearLayout) findViewById(R.id.layoutnhap);
		textinput = (EditText) findViewById(R.id.nhapdulieu);
		layoutchon.setVisibility(View.GONE);
		layoutnhap.setVisibility(View.GONE);
		rdogr = (RadioGroup) findViewById(R.id.radioGroup1);
		textinput.addTextChangedListener(new TextWatcher(){
		    public void afterTextChanged(Editable s) {
		        sokitued.setText(s.length() + " kí tự ");
		        soanhed.setText(computenum(s.toString())+" ảnh");
		    }
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		    public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 
		rdogr.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio0:
					chon = 0;
					layoutchon.setVisibility(View.VISIBLE);
					layoutnhap.setVisibility(View.GONE);
					break;
				case R.id.radio1:
					chon = 1;
					layoutchon.setVisibility(View.GONE);
					layoutnhap.setVisibility(View.VISIBLE);
					break;
				default:
					chon = -1;
					layoutchon.setVisibility(View.GONE);
					layoutnhap.setVisibility(View.GONE);
					break;
				}

			}
		});

		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
				mediaIntent.setType("text/plain");
				startActivityForResult(mediaIntent, 1);
			}
		});
		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Thietlaptruyen.this, TransmitActivity.class);
				if (chon == 0)
					i.putExtra("FULLPATH", fullp);
				else if (chon == 1)
					i.putExtra("FULLPATH", textinput.getText().toString());
				startActivity(i);

			}
		});
	}

	String fullp = "";

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			String uripath = uri.getPath();
			Log.d("", "Video URI= " + uripath);

			String dataread = IOLib.ReadTextFromtxtFile(uripath);
			Log.d("TAG", dataread);
			tenfile.setText(uri.getLastPathSegment());
			
			duongdan.setText(uripath);
			sokitu.setText(dataread.length() + " kí tự");
			soanh.setText(computenum(EncodeData.toJAVA(dataread)) + " ảnh");
			//fullp = Improc.toJAVA(dataread);
			fullp = dataread;
		}
	}

	private int computenum(String input) {
		int length = input.length();
		if (length % 735 == 0)
			return (int) (length / 735);
		else
			return (int) (length / 735) + 1;
	}

	public static String getFileNameByUri(Context context, Uri uri) {
		String fileName = "unknown";// default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = context.getContentResolver().query(uri, null, null,
					null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);// Instead
																				// of
																				// "MediaStore.Images.Media.DATA"
																				// can
																				// be
																				// used
																				// "_data"
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		} else if (uri.getScheme().compareTo("file") == 0) {
			fileName = filePathUri.getLastPathSegment().toString();
		} else {
			fileName = fileName + "_" + filePathUri.getLastPathSegment();
		}
		return fileName;
	}
}
