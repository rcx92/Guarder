package com.qq.guarder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Timer;
import java.util.TimerTask;

import com.qq.guarder.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button config;
	SensorManager sm;
	FileWriter out;
	TextView numberView;

	String number=""; 
	
	private void readNumber(){
		try{
			SharedPreferences sp=getSharedPreferences("phone",MODE_PRIVATE);
			number=sp.getString("PhoneNumber", "");
			numberView.setText(number);
			numberView.setTextColor(Color.argb(115, 255, 255, 255));
		}catch(Exception e){
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	//取得资源
		config=(Button)findViewById(R.id.config);
		//Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + "15026564009"));
		//startActivity(phoneIntent);
	//增加事件响应
        
		config.setOnClickListener(new Button.OnClickListener(){ @Override
			
			public void onClick(View v) {
				startActivityForResult(new Intent(MainActivity.this,ConfigActivity.class), 2);
			}
	
		});
		
		numberView=(TextView)findViewById(R.id.number);
		readNumber();
		if(number.equals("")){
			startActivityForResult(new Intent(MainActivity.this,ConfigActivity.class), 2);
		}

		boolean happened=getIntent().getBooleanExtra("happened", false);
		if(happened){
			Message.sendMessage(this, number);
    		startActivityForResult(new Intent(MainActivity.this,CountDownActivity.class), 1);
		}else {
	    	Intent serviceIntent = new Intent(this, MyService.class);
			stopService(serviceIntent);
	    	startService(serviceIntent);
		}
	}
	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode==1){
			if(resultCode==CountDownActivity.RESULT_OK){
				boolean result=data.getBooleanExtra("DownResult", false);
				if(result){
					Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:" + number));
					startActivity(phoneIntent);
				}
			}

	    	Intent serviceIntent = new Intent(this, MyService.class);
	    	stopService(serviceIntent);
	    	startService(serviceIntent);
	    	finish();
			
		}else if(requestCode==2){
			readNumber();
		}
		/*now=0;
		for(int i=0;i<20;++i)
			acc[i]=0;
		flag=true;*/
	}
	
}

