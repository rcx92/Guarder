package com.qq.guarder;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class Message {
	private static Location location;
	private static String getAddress(Activity ac){
		LocationManager mgr = (LocationManager) ac.getSystemService(Context.LOCATION_SERVICE);Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = mgr.getBestProvider(crit, false);
		location=null;
		int tot=0;
		//while(location  == null )
		{
		  mgr.requestLocationUpdates(provider, 0, 0, new LocationListener(){
			@Override
			public void onLocationChanged(Location loc){
				if(loc!=null)location=loc;
			}
			
			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
		  });
		}
		
        if(location==null)return "";
        String lat=location.getLatitude()+"";
        String lng=location.getLongitude()+"";
		
		
		
		
		ConnectivityManager connMgr = (ConnectivityManager)ac.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()){
			String uri=
		"http://api.map.baidu.com/geocoder/v2/?output=json";
			uri+="&ak=96808b51fd67ced729f200ae0cb06b77";
			uri+="&location="+lat+","+lng;
			uri+="&coordtype=wgs84ll";
			Log.i("uri",uri);
			HttpGet httpRequest = new HttpGet(uri);
			try{
				HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200){
					String strResult = EntityUtils.toString(httpResponse.getEntity());
					JSONObject jsonObject=new JSONObject(strResult);
					int status=jsonObject.getInt("status");
					if(status!=0){
						Log.i("status",status+"");
						Log.i("msg",jsonObject.getString("msg"));
						return "";
					}
					String address=jsonObject.getJSONObject("result").getString("formatted_address");
					return address;
				}else return "";
			}catch(Exception e){
				e.printStackTrace();
			}
			return "";	
		}else {
			return "";
		}
	}
	public static void sendMessage(Activity ac,String phoneNumber){
		String add=getAddress(ac);
		if(add=="")add="未知";
		Log.i("message",add);
		String message="Guarder: 您的家人可能摔倒了。摔倒地点："+add;
		SmsManager sms=SmsManager.getDefault();
		List<String> texts = sms.divideMessage(message);
		for (String text : texts) {
			//sms.sendTextMessage(phoneNumber, null, text, null, null);
		}
		
	}
}
