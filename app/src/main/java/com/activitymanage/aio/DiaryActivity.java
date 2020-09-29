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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.os.Vibrator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class DiaryActivity extends AppCompatActivity {
	
	
	private String search = "";
	
	private ArrayList<HashMap<String, Object>> diary = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout botbar;
	private ImageView back;
	private TextView textview1;
	private ImageView add;
	private LinearLayout sbox;
	private ListView listview1;
	private TextView sph;
	private EditText sq;
	private TextView mydiary;
	private TextView stories;
	
	private Intent intent = new Intent();
	private Vibrator vib;
	private AlertDialog.Builder dbl;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.diary);
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
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		botbar = (LinearLayout) findViewById(R.id.botbar);
		back = (ImageView) findViewById(R.id.back);
		textview1 = (TextView) findViewById(R.id.textview1);
		add = (ImageView) findViewById(R.id.add);
		sbox = (LinearLayout) findViewById(R.id.sbox);
		listview1 = (ListView) findViewById(R.id.listview1);
		sph = (TextView) findViewById(R.id.sph);
		sq = (EditText) findViewById(R.id.sq);
		mydiary = (TextView) findViewById(R.id.mydiary);
		stories = (TextView) findViewById(R.id.stories);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		dbl = new AlertDialog.Builder(this);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.putExtra("t", "a");
				intent.setClass(getApplicationContext(), DiarysetActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				intent.putExtra("t", "e");
				intent.putExtra("position", String.valueOf((long)(_position)));
				intent.setClass(getApplicationContext(), DiarysetActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				vib.vibrate((long)(100));
				dbl.setTitle("Delete");
				dbl.setMessage("Are you sure want to delete this?");
				dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						if (diary.get((int)(diary.size() - 1) - _position).get("img").toString().equals("none") || diary.get((int)(diary.size() - 1) - _position).get("img").toString().equals("ex")) {
							
						}
						else {
							FileUtil.deleteFile(diary.get((int)(diary.size() - 1) - _position).get("img").toString());
						}
						diary.remove((int)((diary.size() - 1) - _position));
						listview1.setAdapter(new Listview1Adapter(diary));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
				});
				dbl.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dbl.create().show();
				return true;
			}
		});
		
		sph.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				sq.setVisibility(View.VISIBLE);
				sph.setVisibility(View.GONE);
			}
		});
		
		sq.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				search = _charSeq;
				listview1.setAdapter(new Listview1Adapter(diary));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
	}
	private void initializeLogic() {
		search = "!n";
		sph.setVisibility(View.VISIBLE);
		sq.setVisibility(View.GONE);
		botbar.setVisibility(View.GONE);
		_gradient(sbox, "#e0e0e0", 10);
		_gradient(mydiary, "#e0e0e0", 100);
		diary = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/diary.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		listview1.setAdapter(new Listview1Adapter(diary));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
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
		intent.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/diary.aio"), new Gson().toJson(diary));
	}
	private void _gradient (final View _view, final String _color, final double _radius) {
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor(_color)); s.setCornerRadius((float)_radius); _view.setBackground(s);
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.diarylist, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
			final TextView title = (TextView) _v.findViewById(R.id.title);
			final TextView content = (TextView) _v.findViewById(R.id.content);
			final TextView from = (TextView) _v.findViewById(R.id.from);
			final TextView date = (TextView) _v.findViewById(R.id.date);
			final TextView mood = (TextView) _v.findViewById(R.id.mood);
			
			date.setText(diary.get((int)(diary.size() - 1) - _position).get("date").toString());
			mood.setText(diary.get((int)(diary.size() - 1) - _position).get("mood").toString());
			title.setText(diary.get((int)(diary.size() - 1) - _position).get("title").toString());
			content.setText(diary.get((int)(diary.size() - 1) - _position).get("content").toString());
			_gradient(mood, "#e0e0e0", 100);
			if (diary.get((int)(diary.size() - 1) - _position).containsKey("color")) {
				if (diary.get((int)(diary.size() - 1) - _position).get("color").toString().equals("#ffffff")) {
					_gradient(box, "#bbdefb", 10);
				}
				else {
					_gradient(box, diary.get((int)(diary.size() - 1) - _position).get("color").toString(), 10);
				}
			}
			else {
				_gradient(box, "#bbdefb", 10);
			}
			if (diary.get((int)(diary.size() - 1) - _position).get("img").toString().contains("none")) {
				imageview1.setVisibility(View.GONE);
			}
			else {
				if (diary.get((int)(diary.size() - 1) - _position).get("img").toString().contains("ex")) {
					imageview1.setVisibility(View.VISIBLE);
					imageview1.setImageResource(R.drawable.main);
				}
				else {
					imageview1.setVisibility(View.VISIBLE);
					imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(diary.get((int)(diary.size() - 1) - _position).get("img").toString(), 1024, 1024));
				}
			}
			if (diary.get((int)(diary.size() - 1) - _position).containsKey("from")) {
				from.setText(diary.get((int)(diary.size() - 1) - _position).get("from").toString());
				from.setVisibility(View.VISIBLE);
			}
			else {
				from.setVisibility(View.GONE);
			}
			_gradient(from, "#e0e0e0", 100);
			if (!search.equals("!n")) {
				if (diary.get((int)(diary.size() - 1) - _position).get("title").toString().toLowerCase().contains(search.toLowerCase())) {
					box.setVisibility(View.VISIBLE);
				}
				else {
					box.setVisibility(View.GONE);
				}
			}
			else {
				
			}
			
			return _v;
		}
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
