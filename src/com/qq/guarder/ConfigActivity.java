package com.qq.guarder;

import com.qq.guarder.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends Activity {

	private Button config,ret;
	private EditText et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		config=(Button)findViewById(R.id.config);
		ret=(Button)findViewById(R.id.ret);
		et = (EditText)findViewById(R.id.et1);
		SharedPreferences sp=getSharedPreferences("phone",MODE_PRIVATE);
		et.setText(sp.getString("PhoneNumber", ""));
		config.setOnClickListener(new Button.OnClickListener(){ @Override
			
			public void onClick(View v) {
				SharedPreferences sp=getSharedPreferences("phone",MODE_PRIVATE);
				Editor editor=sp.edit();
				editor.putString("PhoneNumber", et.getText().toString());
				editor.commit();
				finish();
				
			}
	
		});
		

		ret.setOnClickListener(new Button.OnClickListener(){ @Override
			public void onClick(View v) {
				finish();
			}
	
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

}
