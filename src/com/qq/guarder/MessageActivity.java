package com.qq.guarder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.List;

import com.qq.guarder.R;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MessageActivity extends Activity {
/** Called when the activity is first created. */
@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		// 根据ID获取按钮
		Button button = (Button) this.findViewById(R.id.send);
		// 注册按钮被单击事件
		button.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// 根据ID获取手机号码编辑框
				EditText mobileText = (EditText) findViewById(R.id.mobile);
				// 获取手机号码
				String mobile = mobileText.getText().toString();
				// 根据ID获取信息内容编辑框
				EditText messageText = (EditText) findViewById(R.id.message);
				// 获取信息内容
				String message = messageText.getText().toString();
				// 移动运营商允许每次发送的字节数据有限，我们可以使用Android给我们提供 的短信工具。
				if (message != null) {
					SmsManager sms = SmsManager.getDefault();
					// 如果短信没有超过限制长度，则返回一个长度的List。
					List<String> texts = sms.divideMessage(message);
					for (String text : texts) {
						sms.sendTextMessage(mobile, null, text, null, null);
					}
				}
			}
		});
	}
}