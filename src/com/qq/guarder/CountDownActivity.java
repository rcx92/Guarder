package com.qq.guarder;

import com.qq.guarder.R;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CountDownActivity extends Activity {
	private Button yes,no;
	private Intent data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count_down);

		yes = (Button)findViewById(R.id.yesCountDown);
	//	yes.setVisibility(View.INVISIBLE);
		no = (Button)findViewById(R.id.noCountDown);
	//	no.setVisibility(View.INVISIBLE);
		data=new Intent(this,MainActivity.class);
		MyCount mc=new MyCount(30000,1000);
		mc.start();
		
	}
	
	class MyCount extends CountDownTimer {
		private boolean result;
		TipHelper tipHelper;
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			result=true;
			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result=true;
					data.putExtra("DownResult", result);
					setResult(RESULT_OK,data);
					tipHelper.stopSound();
					finish();
					cancel();
					
				}
			});

			no.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result=false;
					data.putExtra("DownResult", result);
					setResult(RESULT_OK,data);
					tipHelper.stopSound();
					finish();
					cancel();
				}
			});
			tipHelper=new TipHelper();
			tipHelper.playSound(CountDownActivity.this);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			result=true;
			data.putExtra("DownResult", result);
			setResult(RESULT_OK,data);
			tipHelper.stopSound();
			finish();

		}
		boolean getResult(){
			return result;
		}
		@Override
		public void onTick(long arg0) {
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.count_down, menu);
		return true;
	}

}
