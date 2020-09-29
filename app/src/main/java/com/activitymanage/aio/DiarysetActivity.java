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
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ProgressBar;
import android.content.Intent;
import android.net.Uri;
import android.content.ClipData;
import android.app.Activity;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class DiarysetActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICK = 101;
	
	private HashMap<String, Object> tmp = new HashMap<>();
	private boolean editmode = false;
	private boolean isview = false;
	private String path = "";
	private double position = 0;
	private String whatcolor = "";
	private String whatmood = "";
	private String picked = "";
	private boolean ispick = false;
	private String whatcontent = "";
	private String whattitle = "";
	private boolean falsePause = false;
	private boolean isListening = false;
	
	private ArrayList<String> moodd = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> colour = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> diary = new ArrayList<>();
	
	private LinearLayout linear1;
	private ListView clr;
	private ScrollView vscroll1;
	private ImageView back;
	private TextView act;
	private ImageView share;
	private ImageView image;
	private ImageView color;
	private ImageView edit;
	private ImageView add;
	private LinearLayout div;
	private LinearLayout linear3;
	private ImageView imageview4;
	private TextView title;
	private EditText etitle;
	private TextView content;
	private EditText econtent;
	private LinearLayout speakbox;
	private TextView date;
	private TextView mood;
	private Spinner emood;
	private ImageView mic;
	private ProgressBar speakbar;
	
	private Intent intent = new Intent();
	private Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
	private SharedPreferences data;
	private Calendar cale = Calendar.getInstance();
	private AlertDialog.Builder dbl;
	private SpeechRecognizer dikte;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.diaryset);
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
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		clr = (ListView) findViewById(R.id.clr);
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		back = (ImageView) findViewById(R.id.back);
		act = (TextView) findViewById(R.id.act);
		share = (ImageView) findViewById(R.id.share);
		image = (ImageView) findViewById(R.id.image);
		color = (ImageView) findViewById(R.id.color);
		edit = (ImageView) findViewById(R.id.edit);
		add = (ImageView) findViewById(R.id.add);
		div = (LinearLayout) findViewById(R.id.div);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		imageview4 = (ImageView) findViewById(R.id.imageview4);
		title = (TextView) findViewById(R.id.title);
		etitle = (EditText) findViewById(R.id.etitle);
		content = (TextView) findViewById(R.id.content);
		econtent = (EditText) findViewById(R.id.econtent);
		speakbox = (LinearLayout) findViewById(R.id.speakbox);
		date = (TextView) findViewById(R.id.date);
		mood = (TextView) findViewById(R.id.mood);
		emood = (Spinner) findViewById(R.id.emood);
		mic = (ImageView) findViewById(R.id.mic);
		speakbar = (ProgressBar) findViewById(R.id.speakbar);
		pick.setType("image/*");
		pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		data = getSharedPreferences("color", Activity.MODE_PRIVATE);
		dbl = new AlertDialog.Builder(this);
		dikte = SpeechRecognizer.createSpeechRecognizer(this);
		
		clr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				whatcolor = colour.get((int)_position).get("data").toString();
				_gradient(div, colour.get((int)_position).get("data").toString(), 0);
				clr.setVisibility(View.GONE);
				vscroll1.setVisibility(View.VISIBLE);
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_save();
				intent.setClass(getApplicationContext(), DiaryActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				falsePause = true;
				startActivityForResult(pick, REQ_CD_PICK);
			}
		});
		
		color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				clr.setVisibility(View.VISIBLE);
				vscroll1.setVisibility(View.GONE);
			}
		});
		
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				edit.setVisibility(View.GONE);
				add.setVisibility(View.VISIBLE);
				color.setVisibility(View.VISIBLE);
				image.setVisibility(View.VISIBLE);
				
			}
		});
		
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_save();
				intent.setClass(getApplicationContext(), DiaryActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(pick, REQ_CD_PICK);
			}
		});
		
		title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				title.setVisibility(View.GONE);
				etitle.setVisibility(View.VISIBLE);
				
				color.setVisibility(View.VISIBLE);
				edit.setVisibility(View.GONE);
				add.setVisibility(View.VISIBLE);
				image.setVisibility(View.VISIBLE);
			}
		});
		
		etitle.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				content.setVisibility(View.GONE);
				econtent.setVisibility(View.VISIBLE);
				speakbox.setVisibility(View.VISIBLE);
				color.setVisibility(View.VISIBLE);
				edit.setVisibility(View.GONE);
				add.setVisibility(View.VISIBLE);
				image.setVisibility(View.VISIBLE);
				speakbar.setVisibility(View.GONE);
			}
		});
		
		econtent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				whatcontent = _charSeq;
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		emood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				whatmood = moodd.get((int)(_position));
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		mic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (isListening) {
					speakbar.setVisibility(View.GONE);
					mic.setImageResource(R.drawable.ic_mic_none_black);
					isListening = false;
					dikte.stopListening();
				}
				else {
					speakbar.setVisibility(View.VISIBLE);
					mic.setImageResource(R.drawable.ic_mic_black);
					isListening = true;
					Intent _intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
					_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
					dikte.startListening(_intent);
					arsybaiUtil.showMessage(getApplicationContext(), "Speak Now");
				}
			}
		});
		
		dikte.setRecognitionListener(new RecognitionListener() {
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
				if (econtent.getText().toString().equals("")) {
					econtent.setText(_result);
				}
				else {
					econtent.setText(econtent.getText().toString().concat(" ".concat(_result)));
				}
				speakbar.setVisibility(View.GONE);
				mic.setImageResource(R.drawable.ic_mic_none_black);
				isListening = false;
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
				arsybaiUtil.showMessage(getApplicationContext(), _errorMessage);
				speakbar.setVisibility(View.GONE);
				mic.setImageResource(R.drawable.ic_mic_none_black);
				isListening = false;
			}
		});
	}
	private void initializeLogic() {
		cale = Calendar.getInstance();
		date.setText(new SimpleDateFormat("dd MMMM yyyy").format(cale.getTime()));
		moodd.add("neutral 😐");
		moodd.add("happy 😊");
		moodd.add("sad ☹️");
		moodd.add("angry 😡");
		moodd.add("laugh 🤣");
		moodd.add("shocked 😱");
		moodd.add("devil 😈");
		moodd.add("food 🥗");
		emood.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, moodd));
		((ArrayAdapter)emood.getAdapter()).notifyDataSetChanged();
		colour = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/color.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		diary = new Gson().fromJson(FileUtil.readFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/diary.aio")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		clr.setAdapter(new ClrAdapter(colour));
		((BaseAdapter)clr.getAdapter()).notifyDataSetChanged();
		if (getIntent().getStringExtra("t").equals("a")) {
			isview = false;
			whatcolor = "#ffffff";
			clr.setVisibility(View.GONE);
			etitle.setVisibility(View.GONE);
			econtent.setVisibility(View.GONE);
			mood.setVisibility(View.GONE);
			imageview4.setVisibility(View.GONE);
			edit.setVisibility(View.GONE);
			
		}
		else {
			isview = true;
			position = (diary.size() - 1) - Double.parseDouble(getIntent().getStringExtra("position"));
			clr.setVisibility(View.GONE);
			etitle.setVisibility(View.GONE);
			econtent.setVisibility(View.GONE);
			mood.setVisibility(View.VISIBLE);
			imageview4.setVisibility(View.GONE);
			edit.setVisibility(View.VISIBLE);
			emood.setVisibility(View.GONE);
			add.setVisibility(View.GONE);
			image.setVisibility(View.GONE);
			color.setVisibility(View.GONE);
			_gradient(mood, "#e0e0e0", 100);
			mood.setText(diary.get((int)position).get("mood").toString());
			title.setText(diary.get((int)position).get("title").toString());
			etitle.setText(diary.get((int)position).get("title").toString());
			content.setText(diary.get((int)position).get("content").toString());
			econtent.setText(diary.get((int)position).get("content").toString());
			if (diary.get((int)position).get("img").toString().equals("none")) {
				
			}
			else {
				imageview4.setVisibility(View.VISIBLE);
				if (diary.get((int)position).get("img").toString().equals("ex")) {
					imageview4.setImageResource(R.drawable.main);
				}
				else {
					imageview4.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(diary.get((int)position).get("img").toString(), 1024, 1024));
				}
			}
			if (diary.get((int)position).containsKey("color")) {
				_gradient(div, diary.get((int)position).get("color").toString(), 0);
			}
			else {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("color", "#ffffff");
					diary.add((int)position, _item);
				}
				
			}
		}
		speakbox.setVisibility(View.GONE);
		share.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICK:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				picked = _filePath.get((int)(0));
				ispick = true;
				imageview4.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_filePath.get((int)(0)), 1024, 1024));
				imageview4.setVisibility(View.VISIBLE);
			}
			else {
				ispick = false;
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		_save();
		intent.setClass(getApplicationContext(), DiaryActivity.class);
		startActivity(intent);
		finish();
	}
	private void _gradient (final View _view, final String _color, final double _radius) {
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable(); s.setColor(Color.parseColor(_color)); s.setCornerRadius((float)_radius); _view.setBackground(s);
	}
	
	
	private void _save () {
		if (!(whattitle.equals("") && whatcontent.equals(""))) {
			if (ispick) {
				path = FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/images/").concat(etitle.getText().toString().replace(" ", "_").concat(new SimpleDateFormat("ddMMyyyhhmmss").format(cale.getTime()).concat(".png")));
			}
			else {
				path = "none";
			}
			whattitle = etitle.getText().toString();
			if (getIntent().getStringExtra("t").equals("a")) {
				tmp.clear();
				tmp.put("title", whattitle);
				tmp.put("date", date.getText().toString());
				tmp.put("mood", whatmood);
				tmp.put("content", whatcontent);
				tmp.put("color", whatcolor);
				if (ispick) {
					FileUtil.copyFile(picked, path);
					tmp.put("img", path);
				}
				else {
					tmp.put("img", "none");
				}
				diary.add(tmp);
			}
			else {
				diary.get((int)position).put("title", etitle.getText().toString());
				diary.get((int)position).put("content", econtent.getText().toString());
				if (whatcolor.equals("")) {
					
				}
				else {
					diary.get((int)position).put("color", whatcolor);
				}
				if (ispick) {
					if (diary.get((int)position).get("img").toString().equals("none") || diary.get((int)position).get("img").toString().equals("ex")) {
						FileUtil.copyFile(picked, path);
						diary.get((int)position).put("img", path);
					}
					else {
						FileUtil.copyFile(picked, diary.get((int)position).get("img").toString());
					}
				}
				else {
					
				}
			}
			FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/diary.aio"), new Gson().toJson(diary));
		}
		else {
			arsybaiUtil.showMessage(getApplicationContext(), "Blank diary discarded");
		}
	}
	
	
	public class ClrAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public ClrAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
				_v = _inflater.inflate(R.layout.colour, null);
			}
			
			final LinearLayout box = (LinearLayout) _v.findViewById(R.id.box);
			final TextView textview1 = (TextView) _v.findViewById(R.id.textview1);
			
			textview1.setText(colour.get((int)_position).get("name").toString());
			_gradient(box, colour.get((int)_position).get("data").toString(), 10);
			
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
