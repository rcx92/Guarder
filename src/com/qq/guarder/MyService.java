package com.qq.guarder;

import java.util.Timer;
import java.util.TimerTask;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.textservice.SentenceSuggestionsInfo;
import android.widget.Button;

public class MyService extends Service {
	private KeyguardManager km;
	private KeyguardLock kl;
	private PowerManager pm;
	final static float lowbound = (float) 1.0;
	final static float upbound = (float) 6.0;
	private static final float eps = (float) 1e-6;
	SensorManager sm;
	float[] acc;
	Vector[] vcc;
	double[] lu;
	float totx=0, toty=0, totz=0;
	double lux;
	double lulu;
	long last=0,lastLight=0;
	int now=0;
	boolean flag;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	private void update(){
		if (! flag) return;
		
    	if(last>0){
    		totx/=last; toty/=last; totz/=last;
    	}
    	float tacc = (float) Math.sqrt(totx*totx+toty*toty+totz*totz);
    	if(lastLight>0)lulu = lux/lastLight;
    	lastLight=0;lux=0;
    	//acc[now] = (float) (tacc-9.7);
    	acc[now]=tacc;
    	vcc[now]=new Vector(totx,toty,totz);
    	lu[now]=lulu;
    	//Log.i("save", "acc = "+acc[now]);
    	++now;
    	if (now==20) now=0;
    	//if (checkfall() && vcc[now].isCross(vcc[(now+4)%5])){
    	if(checkfall2()){
    		Log.i("hehe","haha");
    		pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag").acquire();
    		Intent intent=new Intent(this,MainActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		intent.putExtra("happened", true);
    		startActivity(intent);
    		flag=false;
    		stopSelf();
    	}
		last=0; totx=toty=totz=0;
	}

	private boolean checkfall2(){
		double[] tmp=new double[20];
		for(int i=0;i<20;++i)tmp[i]=acc[(now+i)%20];
		return Predict.predict(tmp);
	}
	private boolean checkfall() {
		int state=0;
		double light=0;
		//Log.i("Light",lu[now]+"");
		for (int i=0; i<5; ++i){
			light+=lu[(now+i)%5];
			state = changeState(state, nowstate(acc[(now+i)%5]));
		}
		return state==3 ;//&& light/5<10;
	}

	private int changeState(int state, int nowstate) {
		if (nowstate==0) return state;
		if ((state==0 || state==1) && nowstate==-1) return state+1;
		if (state==2 && nowstate==1) return 3;
		return state;
	}

	private int nowstate(float f) {
		if (Math.abs(f)+eps>lowbound && Math.abs(f)<upbound+eps){
			if (f<-eps) return -1;
			return 1;
		}
		return 0;
	}

	@Override
	public void onDestroy() {
	// TODO Auto-generated method stub
		super.onDestroy();
		kl.reenableKeyguard();
		sm.unregisterListener(mySensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		sm.unregisterListener(myLightEventListener, sm.getDefaultSensor(Sensor.TYPE_LIGHT));
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent,int flags, int startId) {
	// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		acc = new float[20];
		vcc= new Vector[20];
		lu = new double[20];
	    for (int i=0; i<20; ++i) acc[i]=0;
	    now=0;
	    Log.i("start", "service");
	    sm=(SensorManager) getSystemService(Context.SENSOR_SERVICE);//传感器管理器
	    
		
		flag=true;
		sm.registerListener(mySensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
		sm.registerListener(myLightEventListener, sm.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_FASTEST);
	    Timer updateTimer = new Timer("gForceUpdate");
	    updateTimer.scheduleAtFixedRate(new TimerTask() {  
	        public void run() {  
	        	update();  
	        }  
	    }, 0, 50);
		km=((KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE));
		Log.i("key",km.toString());
		kl=km.newKeyguardLock("");
	    kl.disableKeyguard();
	    pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
	    return START_STICKY;
	}

    final SensorEventListener mySensorEventListener = new SensorEventListener() { //重力感应器事件监听
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				totx = totx+event.values[SensorManager.DATA_X];
				toty = toty+event.values[SensorManager.DATA_Y];
				totz = totz+event.values[SensorManager.DATA_Z];
				++last;
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	final SensorEventListener myLightEventListener = new SensorEventListener() { //重力感应器事件监听
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType()==Sensor.TYPE_LIGHT){
				lux+=event.values[0];
				++lastLight;
			}
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
}
