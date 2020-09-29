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
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Vibrator;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
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

public class ShopActivity extends AppCompatActivity {
	
	
	private HashMap<String, Object> tmp = new HashMap<>();
	private boolean isfav = false;
	private boolean ismic = false;
	private boolean setfav = false;
	private String search = "";
	
	private ArrayList<HashMap<String, Object>> ori = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> favo = new ArrayList<>();
	
	private LinearLayout bar;
	private LinearLayout div;
	private ImageView back;
	private TextView textview1;
	private ImageView sync;
	private ImageView clear;
	private LinearLayout linear1;
	private LinearLayout addbox;
	private LinearLayout sbox;
	private ListView ulist;
	private ListView flist;
	private TextView mylist;
	private TextView myfav;
	private LinearLayout addiv;
	private LinearLayout butdiv;
	private TextView addph;
	private EditText add;
	private ImageView mic;
	private ImageView favthis;
	private ImageView addthis;
	private TextView sph;
	private EditText sq;
	
	private Intent intent = new Intent();
	private AlertDialog.Builder dbl;
	private SharedPreferences data;
	private Vibrator vib;
	private SpeechRecognizer ttt;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.shop);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
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
		
		bar = (LinearLayout) findViewById(R.id.bar);
		div = (LinearLayout) findViewById(R.id.div);
		back = (ImageView) findViewById(R.id.back);
		textview1 = (TextView) findViewById(R.id.textview1);
		sync = (ImageView) findViewById(R.id.sync);
		clear = (ImageView) findViewById(R.id.clear);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		addbox = (LinearLayout) findViewById(R.id.addbox);
		sbox = (LinearLayout) findViewById(R.id.sbox);
		ulist = (ListView) findViewById(R.id.ulist);
		flist = (ListView) findViewById(R.id.flist);
		mylist = (TextView) findViewById(R.id.mylist);
		myfav = (TextView) findViewById(R.id.myfav);
		addiv = (LinearLayout) findViewById(R.id.addiv);
		butdiv = (LinearLayout) findViewById(R.id.butdiv);
		addph = (TextView) findViewById(R.id.addph);
		add = (EditText) findViewById(R.id.add);
		mic = (ImageView) findViewById(R.id.mic);
		favthis = (ImageView) findViewById(R.id.favthis);
		addthis = (ImageView) findViewById(R.id.addthis);
		sph = (TextView) findViewById(R.id.sph);
		sq = (EditText) findViewById(R.id.sq);
		dbl = new AlertDialog.Builder(this);
		data = getSharedPreferences("color", Activity.MODE_PRIVATE);
		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		ttt = SpeechRecognizer.createSpeechRecognizer(this);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		sync.setOnClickListener(new View.OnClickListener() {
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
				if (isfav) {
					dbl.setTitle("Celar");
					dbl.setMessage("Are you sire want to clear FAV list?");
					dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							favo.clear();
							flist.setAdapter(new FlistAdapter(favo));
							((BaseAdapter)flist.getAdapter()).notifyDataSetChanged();
						}
					});
					dbl.setNegativeButton("Hell No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					dbl.create().show();
				}
				else {
					dbl.setTitle("Clear");
					dbl.setMessage("Are you sure want to clear this list?");
					dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							ori.clear();
							ulist.setAdapter(new UlistAdapter(ori));
							((BaseAdapter)ulist.getAdapter()).notifyDataSetChanged();
						}
					});
					dbl.setNegativeButton("Hell No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					dbl.create().show();
				}
			}
		});
		
		ulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (ori.get((int)_position).get("isdone").toString().equals("true")) {
					ori.get((int)_position).put("isdone", "false");
				}
				else {
					ori.get((int)_position).put("isdone", "true");
				}
				ulist.setAdapter(new UlistAdapter(ori));
				((BaseAdapter)ulist.getAdapter()).notifyDataSetChanged();
			}
		});
		
		ulist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				vib.vibrate((long)(100));
				dbl.setTitle("Delete");
				dbl.setMessage("Are u sure want to delete this?");
				dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						ori.remove((int)(_position));
						ulist.setAdapter(new UlistAdapter(ori));
						((BaseAdapter)ulist.getAdapter()).notifyDataSetChanged();
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
		
		flist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				dbl.setTitle("Add to list?");
				dbl.setMessage("Want to add this to list?");
				dbl.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						tmp.clear();
						tmp = favo.get((int)_position);
						ori.add(tmp);
						arsybaiUtil.showMessage(getApplicationContext(), "Added to list");
						ulist.setAdapter(new UlistAdapter(ori));
						((BaseAdapter)ulist.getAdapter()).notifyDataSetChanged();
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
		
		flist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				vib.vibrate((long)(100));
				dbl.setTitle("Delete");
				dbl.setMessage("Are you sure want to delete this?");
				dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						favo.remove((int)(_position));
						flist.setAdapter(new FlistAdapter(favo));
						((BaseAdapter)flist.getAdapter()).notifyDataSetChanged();
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
		
		mylist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), ShopActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		myfav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				sbox.setVisibility(View.VISIBLE);
				butdiv.setVisibility(View.GONE);
				add.setVisibility(View.GONE);
				addbox.setVisibility(View.GONE);
				ulist.setVisibility(View.GONE);
				flist.setVisibility(View.VISIBLE);
				mylist.setTextColor(0xFF000000);
				myfav.setTextColor(0xFF00897B);
				sph.setVisibility(View.VISIBLE);
				sq.setVisibility(View.GONE);
				isfav = true;
				flist.setAdapter(new FlistAdapter(favo));
				((BaseAdapter)flist.getAdapter()).notifyDataSetChanged();
			}
		});
		
		addph.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				addph.setVisibility(View.GONE);
				add.setVisibility(View.VISIBLE);
				butdiv.setVisibility(View.VISIBLE);
			}
		});
		
		mic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (ismic) {
					ttt.stopListening();
					mic.setImageResource(R.drawable.ic_mic_none_black);
				}
				else {
					arsybaiUtil.showMessage(getApplicationContext(), "Speak Now");
					mic.setImageResource(R.drawable.ic_mic_black);
					vib.vibrate((long)(100));
					Intent _intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
					_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
					ttt.startListening(_intent);
				}
			}
		});
		
		favthis.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (setfav) {
					setfav = false;
					favthis.setImageResource(R.drawable.ic_star_outline_black);
				}
				else {
					setfav = true;
					favthis.setImageResource(R.drawable.ic_star_black);
				}
			}
		});
		
		addthis.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				tmp = new HashMap<>();
				tmp.put("product", add.getText().toString());
				tmp.put("isdone", "false");
				if (setfav) {
					tmp.put("fav", "true");
					favo.add(tmp);
				}
				else {
					tmp.put("fav", "false");
				}
				ori.add(tmp);
				add.setText("");
				add.setVisibility(View.GONE);
				addph.setVisibility(View.VISIBLE);
				butdiv.setVisibility(View.GONE);
				ulist.setAdapter(new UlistAdapter(ori));
				((BaseAdapter)ulist.getAdapter()).notifyDataSetChanged();
				setfav = false;
				favthis.setImageResource(R.drawable.ic_star_outline_black);
			}
		});
		
		sph.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				sph.setVisibility(View.GONE);
				sq.setVisibility(View.VISIBLE);
			}
		});
		
		sq.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				search = _charSeq;
				flist.setAdapter(new FlistAdapter(favo));
				((BaseAdapter)flist.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		ttt.setRecognitionListener(new RecognitionListener() {
			@Override
			public void onReadyForSpeech(Bundle _param1) {
			}
			@Override
			public void onBeginningOfSpeech() {
			}
			@Override
			public void onRmsChanged(float _param1) {
			}
			@Override
			public void onBufferReceived(byte[] _param1) {
			}
			@Override
			public void onEndOfSpeech() {
			}
			@Override
			public void onPartialResults(Bundle _param1) {
			}
			@Override
			public void onEvent(int _param1, Bundle _param2) {
			}
			@Override
			public void onResults(Bundle _param1) {
				final ArrayList<String> _results = _param1.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
				final String _result = _results.get(0);
				ismic = false;
				mic.setImageResource(R.drawable.ic_mic_none_black);
				add.setText(_result);
			}
			
			@Override
			public void onError(int _param1) {
				final String _errorMessage;
				switch (_param1) {
					case SpeechRecognizer.ERROR_AUDIO:
					_errorMessage = "audio error";
					break;
					case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
					_errorMessage = "speech timeout";
					break;
					case SpeechRecognizer.ERROR_NO_MATCH:
					_errorMessage = "speech no match";
					break;
					case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
					_errorMessage = "recognizer busy";
					break;
					case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
					_errorMessage = "recognizer insufficient permissions";
					break;
					default:
					_errorMessage = "recognizer other error";
					break;
				}
				ismic = false;
				mic.setImageResource(R.drawable.ic_mic_none_black);
				arsybaiUtil.showMessage(getApplicationContext(), "Speech error : ".concat(_errorMessage));
			}
		});
	}
	private void initializeLogic() {
		isfav = false;
		ismic = false;
		setfav = false;
		search = "!n";
		butdiv.setVisibility(View.GONE);
		add.setVisibility(View.GONE);
		addph.setVisibility(View.VISIBLE);
		flist.setVisibility(View.GONE);
		sbox.setVisibility(View.GONE);
		sync.setVisibility(View.INVISIBLE);
		_gradient(addbox, "#e0e0e0", 10);
		_gradient(sbox, "#e0e0e0", 10);
		if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/shopfav.aio"))) {
			
		}
		else {
			FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/shopfav.aio"), new Gson().toJson(favo));
		}
		favo = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/shopfav.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		ori = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/shop.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		ulist.setAdapter(new UlistAdapter(ori));
		((BaseAdapter)ulist.getAdapter()).notifyDataSetChanged();
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
		FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/shop.aio"), new Gson().toJson(ori));
		FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/shopfav.aio"), new Gson().toJson(favo));
	}
	private void _gradient (final View _view, final String _color, final double _radius) {
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor(_color)); s.setCornerRadius((float)_radius); _view.setBackground(s);
	}
	
	
	public class UlistAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public UlistAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_v = _inflater.inflate(R.layout.store, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final ImageView isdone = (ImageView) _v.findViewById(R.id.isdone);
			final TextView item = (TextView) _v.findViewById(R.id.item);
			final ImageView fav = (ImageView) _v.findViewById(R.id.fav);
			
			item.setText(ori.get((int)_position).get("product").toString());
			if (ori.get((int)_position).get("isdone").toString().equals("true")) {
				item.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				isdone.setVisibility(View.VISIBLE);
				_gradient(box, data.getString("shop2", ""), 10);
			}
			else {
				isdone.setVisibility(View.GONE);
				_gradient(box, data.getString("shop1", ""), 10);
			}
			if (ori.get((int)_position).containsKey("fav") && ori.get((int)_position).get("fav").toString().equals("true")) {
				fav.setVisibility(View.VISIBLE);
			}
			else {
				fav.setVisibility(View.GONE);
			}
			
			return _v;
		}
	}
	
	public class FlistAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public FlistAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_v = _inflater.inflate(R.layout.store, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final ImageView isdone = (ImageView) _v.findViewById(R.id.isdone);
			final TextView item = (TextView) _v.findViewById(R.id.item);
			final ImageView fav = (ImageView) _v.findViewById(R.id.fav);
			
			item.setText(favo.get((int)_position).get("product").toString());
			isdone.setVisibility(View.GONE);
			_gradient(box, data.getString("shop1", ""), 10);
			if (!search.equals("!n")) {
				if (favo.get((int)_position).get("product").toString().toLowerCase().contains(search.toLowerCase())) {
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
