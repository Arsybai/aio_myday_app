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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Vibrator;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class NotesActivity extends AppCompatActivity {
	
	
	private FloatingActionButton _fab;
	private double nn = 0;
	private HashMap<String, Object> tmp = new HashMap<>();
	private String tt = "";
	private String q = "";
	private String cclor = "";
	
	private ArrayList<HashMap<String, Object>> notes = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> finals = new ArrayList<>();
	
	private LinearLayout bar;
	private LinearLayout lbg;
	private ImageView back;
	private TextView titl;
	private ImageView collab;
	private ImageView sbtn;
	private ProgressBar progressbar2;
	private LinearLayout sbox;
	private CheckBox fonly;
	private ListView ntlist;
	private EditText squery;
	
	private AlertDialog.Builder dbl;
	private Vibrator vib;
	private AlertDialog.Builder tpl;
	private Intent intent = new Intent();
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.notes);
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
		
		bar = (LinearLayout) findViewById(R.id.bar);
		lbg = (LinearLayout) findViewById(R.id.lbg);
		back = (ImageView) findViewById(R.id.back);
		titl = (TextView) findViewById(R.id.titl);
		collab = (ImageView) findViewById(R.id.collab);
		sbtn = (ImageView) findViewById(R.id.sbtn);
		progressbar2 = (ProgressBar) findViewById(R.id.progressbar2);
		sbox = (LinearLayout) findViewById(R.id.sbox);
		fonly = (CheckBox) findViewById(R.id.fonly);
		ntlist = (ListView) findViewById(R.id.ntlist);
		squery = (EditText) findViewById(R.id.squery);
		dbl = new AlertDialog.Builder(this);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		tpl = new AlertDialog.Builder(this);
		data = getSharedPreferences("color", Activity.MODE_PRIVATE);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		collab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (data.getString("account", "").equals("true")) {
					
				}
				else {
					arsybaiUtil.showMessage(getApplicationContext(), "You need to have an account to use this");
				}
			}
		});
		
		sbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				sbox.setVisibility(View.VISIBLE);
			}
		});
		
		fonly.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		fonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					tt = "f";
				}
				else {
					tt = "tt";
				}
				ntlist.setAdapter(new NtlistAdapter(finals));
			}
		});
		
		ntlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				intent.putExtra("position", String.valueOf((long)(_position)));
				intent.putExtra("notes", new Gson().toJson(finals));
				intent.putExtra("t", "v");
				intent.setClass(getApplicationContext(), NotesetActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		ntlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				vib.vibrate((long)(100));
				tpl.setTitle(finals.get((int)_position).get("title").toString());
				tpl.setMessage(" ");
				if (finals.get((int)_position).get("ispin").toString().equals("true")) {
					tpl.setPositiveButton("UNFAV", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							finals.get((int)_position).put("ispin", "false");
							FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(finals));
							intent.setClass(getApplicationContext(), NotesActivity.class);
							startActivity(intent);
							finish();
						}
					});
				}
				else {
					tpl.setPositiveButton("FAV", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							finals.get((int)_position).put("ispin", "true");
							FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(finals));
							intent.setClass(getApplicationContext(), NotesActivity.class);
							startActivity(intent);
							finish();
						}
					});
				}
				tpl.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						dbl.setTitle("Delete ?");
						dbl.setMessage("Are u sure want to delete this?");
						dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								finals.remove((int)(_position));
								FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(finals));
								ntlist.setAdapter(new NtlistAdapter(finals));
								((BaseAdapter)ntlist.getAdapter()).notifyDataSetChanged();
							}
						});
						dbl.setNegativeButton("Hell No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						dbl.create().show();
					}
				});
				tpl.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				tpl.create().show();
				return true;
			}
		});
		
		squery.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				tt = "n";
				q = _charSeq;
				ntlist.setAdapter(new NtlistAdapter(finals));
				((BaseAdapter)ntlist.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.putExtra("t", "e");
				intent.putExtra("position", "0");
				intent.putExtra("notes", new Gson().toJson(finals));
				intent.setClass(getApplicationContext(), NotesetActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void initializeLogic() {
		sbox.setVisibility(View.GONE);
		tt = "tt";
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor("#E0E0E0")); s.setCornerRadius(10); sbox.setBackground(s);
		finals = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		ntlist.setAdapter(new NtlistAdapter(finals));
		((BaseAdapter)ntlist.getAdapter()).notifyDataSetChanged();
		progressbar2.setVisibility(View.GONE);
		collab.setVisibility(View.INVISIBLE);
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
	private void _gradient (final View _view, final String _color, final String _stroke_c, final double _radius) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		
		gd.setColor(Color.parseColor(_color));
		gd.setCornerRadius((float)_radius);
		//gd.setStroke(2,Color.parseColor(_stroke_c));
		
		_view.setBackground(gd);
	}
	
	
	public class NtlistAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public NtlistAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
			
			if (finals.get((int)_position).get("ispin").toString().equals("true")) {
				pin.setVisibility(View.VISIBLE);
			}
			else {
				pin.setVisibility(View.GONE);
			}
			title.setText(finals.get((int)_position).get("title").toString());
			content.setText(finals.get((int)_position).get("content").toString());
			date.setText(finals.get((int)_position).get("date").toString());
			if (tt.equals("n")) {
				if (finals.get((int)_position).get("title").toString().toLowerCase().contains(q.toLowerCase())) {
					box.setVisibility(View.VISIBLE);
				}
				else {
					box.setVisibility(View.GONE);
				}
			}
			else {
				
			}
			if (tt.equals("f")) {
				if (finals.get((int)_position).get("ispin").toString().toLowerCase().contains("true")) {
					box.setVisibility(View.VISIBLE);
				}
				else {
					box.setVisibility(View.GONE);
				}
			}
			else {
				
			}
			_gradient(box, data.getString("note", ""), "#03a9f4", 10);
			
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
