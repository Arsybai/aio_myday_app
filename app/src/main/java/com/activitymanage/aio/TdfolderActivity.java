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
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class TdfolderActivity extends AppCompatActivity {
	
	
	private FloatingActionButton _fab;
	private String sr = "";
	private HashMap<String, Object> tmp = new HashMap<>();
	private String path = "";
	
	private ArrayList<HashMap<String, Object>> folders = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> tmptd = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private ImageView back;
	private TextView textview1;
	private LinearLayout addbox;
	private LinearLayout sbox;
	private ListView listview1;
	private EditText query;
	private ImageView add;
	private TextView sph;
	private EditText sq;
	
	private Intent intent = new Intent();
	private AlertDialog.Builder dbl;
	private Calendar cale = Calendar.getInstance();
	private SharedPreferences data;
	private Vibrator vib;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.tdfolder);
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
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		back = (ImageView) findViewById(R.id.back);
		textview1 = (TextView) findViewById(R.id.textview1);
		addbox = (LinearLayout) findViewById(R.id.addbox);
		sbox = (LinearLayout) findViewById(R.id.sbox);
		listview1 = (ListView) findViewById(R.id.listview1);
		query = (EditText) findViewById(R.id.query);
		add = (ImageView) findViewById(R.id.add);
		sph = (TextView) findViewById(R.id.sph);
		sq = (EditText) findViewById(R.id.sq);
		dbl = new AlertDialog.Builder(this);
		data = getSharedPreferences("color", Activity.MODE_PRIVATE);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.putExtra("t", "ee");
				intent.setClass(getApplicationContext(), TodoActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				intent.putExtra("t", "f");
				intent.putExtra("path", folders.get((int)_position).get("path").toString());
				intent.setClass(getApplicationContext(), TodoActivity.class);
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
				dbl.setMessage("Are you sure want to delete this folder?");
				dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						FileUtil.deleteFile(folders.get((int)_position).get("path").toString());
						folders.remove((int)(_position));
						arsybaiUtil.showMessage(getApplicationContext(), "Folder Deleted");
						listview1.setAdapter(new Listview1Adapter(folders));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
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
		
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				cale = Calendar.getInstance();
				if (query.getText().toString().equals("")) {
					arsybaiUtil.showMessage(getApplicationContext(), "Blank Name Discarded");
				}
				else {
					path = query.getText().toString().replace(" ", "_").concat(new SimpleDateFormat("ddMMyyyymmss").format(cale.getTime()).concat(".aio"));
					FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todofolder/".concat(path)), new Gson().toJson(tmptd));
					tmp.put("title", query.getText().toString());
					tmp.put("path", FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todofolder/".concat(path)));
					folders.add(tmp);
					listview1.setAdapter(new Listview1Adapter(folders));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
				addbox.setVisibility(View.GONE);
				query.setText("");
				sbox.setVisibility(View.VISIBLE);
				sq.setVisibility(View.GONE);
				sph.setVisibility(View.VISIBLE);
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
				sr = _charSeq;
				listview1.setAdapter(new Listview1Adapter(folders));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
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
				addbox.setVisibility(View.VISIBLE);
				sbox.setVisibility(View.GONE);
			}
		});
	}
	private void initializeLogic() {
		sr = "!n";
		addbox.setVisibility(View.GONE);
		sph.setVisibility(View.VISIBLE);
		sq.setVisibility(View.GONE);
		_gradient(sbox, "#E0E0E0", 10);
		_gradient(addbox, "#E0E0E0", 10);
		addbox.setVisibility(View.GONE);
		if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todofolder/path.aio"))) {
			folders = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todofolder/path.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		else {
			FileUtil.writeFile(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todofolder/path.aio")), new Gson().toJson(folders));
		}
		listview1.setAdapter(new Listview1Adapter(folders));
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
		intent.putExtra("t", "ababnaahna");
		intent.setClass(getApplicationContext(), TodoActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/todofolder/path.aio"), new Gson().toJson(folders));
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
				_v = _inflater.inflate(R.layout.todofol, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
			final TextView title = (TextView) _v.findViewById(R.id.title);
			
			title.setText(folders.get((int)_position).get("title").toString());
			if (sr.equals("!n")) {
				
			}
			else {
				if (folders.get((int)_position).get("title").toString().toLowerCase().contains(sr.toLowerCase())) {
					box.setVisibility(View.VISIBLE);
				}
				else {
					box.setVisibility(View.GONE);
				}
			}
			_gradient(box, "#e2bee7", 10);
			
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
