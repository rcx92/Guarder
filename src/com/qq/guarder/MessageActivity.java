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
		// ����ID��ȡ��ť
		Button button = (Button) this.findViewById(R.id.send);
		// ע�ᰴť�������¼�
		button.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// ����ID��ȡ�ֻ�����༭��
				EditText mobileText = (EditText) findViewById(R.id.mobile);
				// ��ȡ�ֻ�����
				String mobile = mobileText.getText().toString();
				// ����ID��ȡ��Ϣ���ݱ༭��
				EditText messageText = (EditText) findViewById(R.id.message);
				// ��ȡ��Ϣ����
				String message = messageText.getText().toString();
				// �ƶ���Ӫ������ÿ�η��͵��ֽ��������ޣ����ǿ���ʹ��Android�������ṩ �Ķ��Ź��ߡ�
				if (message != null) {
					SmsManager sms = SmsManager.getDefault();
					// �������û�г������Ƴ��ȣ��򷵻�һ�����ȵ�List��
					List<String> texts = sms.divideMessage(message);
					for (String text : texts) {
						sms.sendTextMessage(mobile, null, text, null, null);
					}
				}
			}
		});
	}
}