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
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class WritenoteActivity extends AppCompatActivity {
	
	
	private HashMap<String, Object> temp = new HashMap<>();
	private HashMap<String, Object> ret = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> listy = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear1;
	private LinearLayout menubox;
	private LinearLayout editbox;
	private TextView cancel;
	private TextView export;
	private ImageView delete;
	private ImageView save;
	private TextView date;
	private EditText title;
	private EditText content;
	
	private Intent intent = new Intent();
	private Calendar datetime = Calendar.getInstance();
	private AlertDialog.Builder dbl;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.writenote);
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
		
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		menubox = (LinearLayout) findViewById(R.id.menubox);
		editbox = (LinearLayout) findViewById(R.id.editbox);
		cancel = (TextView) findViewById(R.id.cancel);
		export = (TextView) findViewById(R.id.export);
		delete = (ImageView) findViewById(R.id.delete);
		save = (ImageView) findViewById(R.id.save);
		date = (TextView) findViewById(R.id.date);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);
		dbl = new AlertDialog.Builder(this);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dbl.setTitle("Cancel");
				dbl.setMessage("Are you sure want to cancel?\nyour changes may not be saved");
				dbl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						intent.putExtra("s", "false");
						intent.setClass(getApplicationContext(), NoteActivity.class);
						startActivity(intent);
						finish();
					}
				});
				dbl.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dbl.create().show();
			}
		});
		
		export.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (title.getText().toString().equals("")) {
					arsybaiUtil.showMessage(getApplicationContext(), "Title can not be empty!");
				}
				else {
					FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/aio/notes/".concat(title.getText().toString().replace(" ", "_").concat(".txt"))), content.getText().toString());
					arsybaiUtil.showMessage(getApplicationContext(), "file saved to ".concat(FileUtil.getExternalStorageDir().concat("/aio/notes/".concat(title.getText().toString().replace(" ", "_").concat(".txt")))));
				}
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dbl.setTitle("Confirmation");
				dbl.setMessage("Clear all data?");
				dbl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						title.setText("");
						content.setText("");
					}
				});
				dbl.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dbl.create().show();
			}
		});
		
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				arsybaiUtil.showMessage(getApplicationContext(), "Saving ...");
				listy = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				if (getIntent().getStringExtra("t").equals("write")) {
					temp.put("title", title.getText().toString());
					temp.put("date", date.getText().toString());
					temp.put("content", content.getText().toString());
					temp.put("ispin", "false");
					listy.add(temp);
					FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(listy));
					arsybaiUtil.showMessage(getApplicationContext(), "Saved");
					intent.setClass(getApplicationContext(), NoteActivity.class);
					startActivity(intent);
					finish();
				}
				else {
					listy.get((int)Double.parseDouble(temp.get("position").toString())).put("title", title.getText().toString());
					listy.get((int)Double.parseDouble(temp.get("position").toString())).put("content", content.getText().toString());
					listy.get((int)Double.parseDouble(temp.get("position").toString())).put("date", date.getText().toString());
					FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(listy));
					arsybaiUtil.showMessage(getApplicationContext(), "Saved");
					intent.setClass(getApplicationContext(), NoteActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}
	private void initializeLogic() {
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor("#ffffff")); s.setCornerRadius(10); menubox.setBackground(s);
		editbox.setBackground(s);
		if (getIntent().getStringExtra("t").equals("write")) {
			
		}
		else {
			temp = new Gson().fromJson(getIntent().getStringExtra("data"), new TypeToken<HashMap<String, Object>>(){}.getType());
			title.setText(temp.get("title").toString());
			content.setText(temp.get("content").toString());
		}
		datetime = Calendar.getInstance();
		date.setText(new SimpleDateFormat("E, dd MMMM yyyy").format(datetime.getTime()));
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
		dbl.setTitle("Exit");
		dbl.setMessage("Are you sure want to exit?\nyou change may not be saved");
		dbl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				intent.setClass(getApplicationContext(), NoteActivity.class);
				startActivity(intent);
				finish();
			}
		});
		dbl.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		dbl.create().show();
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
