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
import android.widget.ScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.content.Intent;
import android.net.Uri;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class NotesetActivity extends AppCompatActivity {
	
	
	private FloatingActionButton _fab;
	private double position = 0;
	private boolean stt = false;
	private HashMap<String, Object> tmp = new HashMap<>();
	private boolean ddel = false;
	
	private ArrayList<HashMap<String, Object>> notes = new ArrayList<>();
	
	private LinearLayout bar;
	private ScrollView vscroll1;
	private ImageView back;
	private LinearLayout linear2;
	private ImageView pin;
	private ImageView del;
	private ImageView share;
	private LinearLayout linear3;
	private TextView title;
	private EditText etitle;
	private TextView content;
	private LinearLayout vbox;
	private EditText econtent;
	private ImageView sttc;
	private ProgressBar progressbar1;
	
	private Intent intent = new Intent();
	private Calendar cale = Calendar.getInstance();
	private SpeechRecognizer stts;
	private AlertDialog.Builder dbl;
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.noteset);
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
		
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		bar = (LinearLayout) findViewById(R.id.bar);
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		back = (ImageView) findViewById(R.id.back);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		pin = (ImageView) findViewById(R.id.pin);
		del = (ImageView) findViewById(R.id.del);
		share = (ImageView) findViewById(R.id.share);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		title = (TextView) findViewById(R.id.title);
		etitle = (EditText) findViewById(R.id.etitle);
		content = (TextView) findViewById(R.id.content);
		vbox = (LinearLayout) findViewById(R.id.vbox);
		econtent = (EditText) findViewById(R.id.econtent);
		sttc = (ImageView) findViewById(R.id.sttc);
		progressbar1 = (ProgressBar) findViewById(R.id.progressbar1);
		stts = SpeechRecognizer.createSpeechRecognizer(this);
		dbl = new AlertDialog.Builder(this);
		data = getSharedPreferences("color", Activity.MODE_PRIVATE);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), NotesActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		pin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (notes.get((int)position).get("ispin").toString().equals("true")) {
					notes.get((int)position).put("ispin", "false");
					pin.setImageResource(R.drawable.ic_star_outline_black);
				}
				else {
					pin.setImageResource(R.drawable.ic_star_black);
					notes.get((int)position).put("ispin", "true");
				}
			}
		});
		
		del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dbl.setTitle("Delete");
				dbl.setMessage("Are you sure want to delete this?");
				dbl.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						ddel = true;
						notes.remove((int)(position));
						intent.setClass(getApplicationContext(), NotesActivity.class);
						startActivity(intent);
						startActivity(intent);
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
		
		title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				title.setVisibility(View.GONE);
				etitle.setVisibility(View.VISIBLE);
			}
		});
		
		content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				econtent.setVisibility(View.VISIBLE);
				sttc.setVisibility(View.VISIBLE);
				content.setVisibility(View.GONE);
				vbox.setVisibility(View.VISIBLE);
			}
		});
		
		sttc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (stt) {
					progressbar1.setVisibility(View.GONE);
					stts.stopListening();
					stt = false;
				}
				else {
					arsybaiUtil.showMessage(getApplicationContext(), "Speak Now");
					progressbar1.setVisibility(View.VISIBLE);
					Intent _intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
					_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
					stts.startListening(_intent);
					stt = true;
				}
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				intent.setClass(getApplicationContext(), NotesActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		stts.setRecognitionListener(new RecognitionListener() {
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
					econtent.setText(econtent.getText().toString().concat("\n".concat(_result)));
				}
				stt = false;
				progressbar1.setVisibility(View.GONE);
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
				stt = false;
				progressbar1.setVisibility(View.GONE);
			}
		});
	}
	private void initializeLogic() {
		del.setVisibility(View.VISIBLE);
		progressbar1.setVisibility(View.GONE);
		share.setVisibility(View.GONE);
		stt = false;
		notes = new Gson().fromJson(getIntent().getStringExtra("notes"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		position = Double.parseDouble(getIntent().getStringExtra("position"));
		if (getIntent().getStringExtra("t").equals("v")) {
			title.setText(notes.get((int)position).get("title").toString());
			content.setText(notes.get((int)position).get("content").toString());
			etitle.setText(notes.get((int)position).get("title").toString());
			econtent.setText(notes.get((int)position).get("content").toString());
			etitle.setVisibility(View.GONE);
			econtent.setVisibility(View.GONE);
			vbox.setVisibility(View.INVISIBLE);
			title.setVisibility(View.VISIBLE);
			content.setVisibility(View.VISIBLE);
			if (notes.get((int)position).get("ispin").toString().equals("true")) {
				pin.setImageResource(R.drawable.ic_star_black);
			}
			else {
				pin.setImageResource(R.drawable.ic_star_outline_black);
			}
		}
		else {
			title.setVisibility(View.GONE);
			content.setVisibility(View.GONE);
			vbox.setVisibility(View.VISIBLE);
			etitle.setVisibility(View.VISIBLE);
			econtent.setVisibility(View.VISIBLE);
			pin.setVisibility(View.GONE);
			del.setVisibility(View.GONE);
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
		intent.setClass(getApplicationContext(), NotesActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		cale = Calendar.getInstance();
		if (getIntent().getStringExtra("t").contains("v")) {
			if (ddel) {
				FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(notes));
			}
			else {
				notes.get((int)position).put("title", etitle.getText().toString());
				notes.get((int)position).put("content", econtent.getText().toString());
				notes.get((int)position).put("date", new SimpleDateFormat("dd MMMM yyyy").format(cale.getTime()));
				FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(notes));
			}
		}
		else {
			if (econtent.getText().toString().equals("") && etitle.getText().toString().equals("")) {
				arsybaiUtil.showMessage(getApplicationContext(), "Blank note discarded");
			}
			else {
				tmp.put("title", etitle.getText().toString());
				tmp.put("content", econtent.getText().toString());
				tmp.put("ispin", "false");
				tmp.put("date", new SimpleDateFormat("dd MMMM yyyy").format(cale.getTime()));
				notes.add(tmp);
				FileUtil.writeFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/.database/note.aio"), new Gson().toJson(notes));
			}
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
