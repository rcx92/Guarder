package com.qq.guarder;

import java.util.Random;  
import android.app.Notification;  
import android.app.NotificationManager;  
import android.app.Service;  
import android.content.Context;  
import android.media.AudioManager;  
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
  
public class TipHelper {  
	MediaPlayer mMediaPlayer;
	Vibrator vibrator;
	public TipHelper(){
		mMediaPlayer = new MediaPlayer();
	}
    // ≤•∑≈ƒ¨»œ¡Â…˘  
    // ∑µªÿNotification id  
    public void playSound(final Context context) {  
    	   try {
               Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
               
               mMediaPlayer.setDataSource(context, alert);
               //final AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
               mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
               mMediaPlayer.setLooping(true);
               mMediaPlayer.prepare();
               mMediaPlayer.start();
           } catch (Exception e) {
               e.printStackTrace();
           }
    	   

          /* try {
               vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);   
               long[] pattern = {800, 150, 400, 130}; // OFF/ON/OFF/ON...   
               vibrator.vibrate(pattern, 2);
           } catch (Exception e) {
               e.printStackTrace();
           }*/
    }  
    public void stopSound(){
    	 try {
             if(this.mMediaPlayer != null) {
                 this.mMediaPlayer.stop();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    	 

         /*try {
             if (null != vibrator) {
                 vibrator.cancel();
                 vibrator = null;
             }
         } catch (Exception e) {
             e.printStackTrace();
         }*/

    }
}  