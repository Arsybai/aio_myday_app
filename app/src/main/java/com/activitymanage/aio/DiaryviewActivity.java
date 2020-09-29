package com.activitymanage.aio;

import androidx.appcompat.app.AppCompatActivity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class DiaryviewActivity extends AppCompatActivity {
	
	
	private String temp = "";
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private ScrollView vscroll1;
	private TextView back;
	private TextView export;
	private LinearLayout box;
	private TextView date;
	private ImageView imageview1;
	private TextView title;
	private TextView content;
	
	private Intent intent = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.diaryview);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		back = (TextView) findViewById(R.id.back);
		export = (TextView) findViewById(R.id.export);
		box = (LinearLayout) findViewById(R.id.box);
		date = (TextView) findViewById(R.id.date);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), DiaryActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		export.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				temp = title.getText().toString().concat("\n".concat(date.getText().toString().concat("\n\n\n".concat(content.getText().toString()))));
				FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/aio/diary/".concat(title.getText().toString().replace(" ", "_")).concat(".txt")), temp);
				arsybaiUtil.showMessage(getApplicationContext(), "Export to ".concat(FileUtil.getExternalStorageDir().concat("/aio/diary/".concat(title.getText().toString().replace(" ", "_")).concat(".txt"))));
			}
		});
	}
	private void initializeLogic() {
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor("#ffffff")); s.setCornerRadius(10); box.setBackground(s);
		date.setText(getIntent().getStringExtra("date"));
		title.setText(getIntent().getStringExtra("title"));
		content.setText(getIntent().getStringExtra("content"));
		if (!getIntent().getStringExtra("img").equals("none")) {
			imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(getIntent().getStringExtra("img"), 1024, 1024));
		}
		else {
			if (getIntent().getStringExtra("img").equals("ex")) {
				imageview1.setImageResource(R.drawable.main);
			}
			else {
				imageview1.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		intent.setClass(getApplicationContext(), DiaryActivity.class);
		startActivity(intent);
		finish();
	}
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
