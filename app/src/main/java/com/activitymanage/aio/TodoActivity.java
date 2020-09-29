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
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Vibrator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class TodoActivity extends AppCompatActivity {
	
	
	private HashMap<String, Object> tmp = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> todos = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private ImageView back;
	private TextView textview1;
	private ImageView share;
	private ImageView clear;
	private ImageView folder;
	private LinearLayout addbox;
	private ListView listview1;
	private TextView pholder;
	private EditText ttodo;
	private ImageView add;
	
	private Intent intent = new Intent();
	private AlertDialog.Builder dbl;
	private Vibrator vib;
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.todo);
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
		back = (ImageView) findViewById(R.id.back);
		textview1 = (TextView) findViewById(R.id.textview1);
		share = (ImageView) findViewById(R.id.share);
		clear = (ImageView) findViewById(R.id.clear);
		folder = (ImageView) findViewById(R.id.folder);
		addbox = (LinearLayout) findViewById(R.id.addbox);
		listview1 = (ListView) findViewById(R.id.listview1);
		pholder = (TextView) findViewById(R.id.pholder);
		ttodo = (EditText) findViewById(R.id.ttodo);
		add = (ImageView) findViewById(R.id.add);
		dbl = new AlertDialog.Builder(this);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		data = getSharedPreferences("color", Activity.MODE_PRIVATE);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (getIntent().getStringExtra("t").equals("f")) {
					intent.setClass(getApplicationContext(), TdfolderActivity.class);
				}
				else {
					intent.setClass(getApplicationContext(), HomeActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
		
		share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (data.getString("account", "").equals("true")) {
					
				}
				else {
					arsybaiUtil.showMessage(getApplicationContext(), "You need to have an account to use this");
				}
			}
		});
		
		clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dbl.setTitle("Clear All");
				dbl.setMessage("Are you sure want to clear all lists?");
				dbl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						todos.clear();
						listview1.setAdapter(new Listview1Adapter(todos));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
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
		
		folder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), TdfolderActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (todos.get((int)(todos.size() - 1) - _position).get("isdone").toString().equals("true")) {
					todos.get((int)(todos.size() - 1) - _position).put("isdone", "false");
					listview1.setAdapter(new Listview1Adapter(todos));
				}
				else {
					todos.get((int)(todos.size() - 1) - _position).put("isdone", "true");
					listview1.setAdapter(new Listview1Adapter(todos));
				}
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
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
						todos.remove((int)((todos.size() - 1) - _position));
						listview1.setAdapter(new Listview1Adapter(todos));
					}
				});
				dbl.setNegativeButton("Hell No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dbl.create().show();
				return true;
			}
		});
		
		pholder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				pholder.setVisibility(View.GONE);
				ttodo.setVisibility(View.VISIBLE);
			}
		});
		
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (ttodo.getText().toString().equals("")) {
					arsybaiUtil.showMessage(getApplicationContext(), "Blank data discarded");
				}
				else {
					tmp = new HashMap<>();
					tmp.put("title", ttodo.getText().toString());
					tmp.put("isdone", "false");
					todos.add(tmp);
					listview1.setAdapter(new Listview1Adapter(todos));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					ttodo.setText("");
				}
				pholder.setVisibility(View.VISIBLE);
				ttodo.setVisibility(View.GONE);
			}
		});
	}
	private void initializeLogic() {
		pholder.setVisibility(View.VISIBLE);
		ttodo.setVisibility(View.GONE);
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor("#e0e0e0")); s.setCornerRadius(10); addbox.setBackground(s);
		if (getIntent().getStringExtra("t").equals("f")) {
			todos = new Gson().fromJson(FileUtil.readFile(getIntent().getStringExtra("path")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			listview1.setAdapter(new Listview1Adapter(todos));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			share.setVisibility(View.GONE);
			folder.setVisibility(View.GONE);
		}
		else {
			todos = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todo.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			listview1.setAdapter(new Listview1Adapter(todos));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			share.setVisibility(View.GONE);
			folder.setVisibility(View.VISIBLE);
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
	public void onPause() {
		super.onPause();
		if (getIntent().getStringExtra("t").equals("f")) {
			FileUtil.writeFile(getIntent().getStringExtra("path"), new Gson().toJson(todos));
		}
		else {
			FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todo.aio"), new Gson().toJson(todos));
		}
	}
	
	@Override
	public void onBackPressed() {
		if (getIntent().getStringExtra("t").equals("f")) {
			intent.setClass(getApplicationContext(), TdfolderActivity.class);
		}
		else {
			intent.setClass(getApplicationContext(), HomeActivity.class);
		}
		startActivity(intent);
		finish();
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
				_v = _inflater.inflate(R.layout.todolist, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final ImageView check = (ImageView) _v.findViewById(R.id.check);
			final TextView item = (TextView) _v.findViewById(R.id.item);
			
			item.setText(todos.get((int)(todos.size() - 1) - _position).get("title").toString());
			if (todos.get((int)(todos.size() - 1) - _position).get("isdone").toString().equals("true")) {
				item.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				check.setVisibility(View.VISIBLE);
				_gradient(box, data.getString("todo2", ""), 10);
			}
			else {
				_gradient(box, data.getString("todo1", ""), 10);
				check.setVisibility(View.GONE);
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
