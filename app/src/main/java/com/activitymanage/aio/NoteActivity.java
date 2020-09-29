package com.activitymanage.aio;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.view.View;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class NoteActivity extends AppCompatActivity {
	
	
	private FloatingActionButton _fab;
	private HashMap<String, Object> temp = new HashMap<>();
	private double num = 0;
	private boolean bookmark = false;
	
	private ArrayList<HashMap<String, Object>> notes = new ArrayList<>();
	
	private LinearLayout linear1;
	private ListView listview1;
	private CheckBox checkbox1;
	private TextView len;
	
	private Intent intent = new Intent();
	private AlertDialog.Builder tpl;
	private AlertDialog.Builder dbl;
	private SharedPreferences data;
	private AlertDialog.Builder sgl;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.note);
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
		
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		listview1 = (ListView) findViewById(R.id.listview1);
		checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
		len = (TextView) findViewById(R.id.len);
		tpl = new AlertDialog.Builder(this);
		dbl = new AlertDialog.Builder(this);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		sgl = new AlertDialog.Builder(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				temp.clear();
				temp.put("position", String.valueOf((long)((notes.size() - 1) - _position)));
				temp.put("title", notes.get((int)(notes.size() - 1) - _position).get("title").toString());
				temp.put("content", notes.get((int)(notes.size() - 1) - _position).get("content").toString());
				intent.putExtra("t", "edit");
				intent.putExtra("data", new Gson().toJson(temp));
				intent.setClass(getApplicationContext(), WritenoteActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				tpl.setTitle("Menu");
				tpl.setMessage("Message");
				if (notes.get((int)(notes.size() - 1) - _position).get("ispin").toString().equals("true")) {
					tpl.setPositiveButton("Unpin", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							notes.get((int)(notes.size() - 1) - _position).put("ispin", "false");
							FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(notes));
							listview1.setAdapter(new Listview1Adapter(notes));
							((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
						}
					});
				}
				else {
					tpl.setPositiveButton("Pin", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							notes.get((int)(notes.size() - 1) - _position).put("ispin", "true");
							FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(notes));
							listview1.setAdapter(new Listview1Adapter(notes));
							((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
						}
					});
				}
				tpl.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						dbl.setTitle("Delete");
						dbl.setMessage("Are you sure want to delete?\nthis process can not be undo");
						dbl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								notes.remove((int)((notes.size() - 1) - _position));
								FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(notes));
								listview1.setAdapter(new Listview1Adapter(notes));
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
				tpl.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				tpl.create().show();
				return true;
			}
		});
		
		checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					bookmark = true;
					listview1.setAdapter(new Listview1Adapter(notes));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
				else {
					bookmark = false;
					listview1.setAdapter(new Listview1Adapter(notes));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.putExtra("t", "write");
				intent.setClass(getApplicationContext(), WritenoteActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void initializeLogic() {
		notes = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		listview1.setAdapter(new Listview1Adapter(notes));
		if (data.getString("tour", "").equals("true")) {
			
		}
		else {
			sgl.setTitle("Guide");
			sgl.setMessage("Click to edit or Long click to see menu\n(✿ ♡‿♡)");
			sgl.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface _dialog, int _which) {
					data.edit().putString("tour", "true").commit();
				}
			});
			sgl.create().show();
		}
		len.setText(String.valueOf((long)(notes.size())).concat(" NOTES"));
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
				_v = _inflater.inflate(R.layout.notelist, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView content = (TextView) _v.findViewById(R.id.content);
			final TextView date = (TextView) _v.findViewById(R.id.date);
			final ImageView pin = (ImageView) _v.findViewById(R.id.pin);
			final TextView title = (TextView) _v.findViewById(R.id.title);
			
			android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor("#1de9b6")); s.setCornerRadius(10); box.setBackground(s);
			date.setText(notes.get((int)(notes.size() - 1) - _position).get("date").toString());
			title.setText(notes.get((int)(notes.size() - 1) - _position).get("title").toString());
			content.setText(notes.get((int)(notes.size() - 1) - _position).get("content").toString());
			if (notes.get((int)(notes.size() - 1) - _position).get("ispin").toString().equals("true")) {
				pin.setVisibility(View.VISIBLE);
			}
			else {
				pin.setVisibility(View.GONE);
			}
			if (bookmark) {
				if (notes.get((int)(notes.size() - 1) - _position).get("ispin").toString().equals("true")) {
					box.setVisibility(View.VISIBLE);
				}
				else {
					box.setVisibility(View.GONE);
				}
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
