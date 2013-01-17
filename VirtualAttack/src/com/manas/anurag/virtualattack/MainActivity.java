package com.manas.anurag.virtualattack;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;

public class MainActivity extends Activity implements SensorEventListener, OnLoadCompleteListener{

	private SensorManager mySensorManager;
	private Sensor myAccelerometer;
	private static SoundPool sp;
	static String audio = "audio/whip.mp3";
	private ImageView imview;
	private int soundId;
	float volume;
	boolean loaded = true;
	private float[] last_acc = new float[]{5,5,5};
	private long last_loaded_time = System.currentTimeMillis();
	private Gallery galleryView;
	private GalleryAdapter gAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		galleryView = (Gallery)findViewById(R.id.galleryview);
		gAdapter = new GalleryAdapter(this);
		galleryView.setAdapter(gAdapter);
		
		galleryView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(arg2==0){
					audio = "audio/whip.mp3";
				}
				else{
					audio = "audio/sword.mp3";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mySensorManager.registerListener(this, myAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
		sp.setOnLoadCompleteListener(this);
	}
	
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
	}

	public final void onSensorChanged(SensorEvent event) {
		float[] acc = event.values;
		if(isJerk(acc) && loaded){
			loaded = false;;
			try {
				AssetFileDescriptor descriptor = getAssets().openFd(audio);
				soundId = sp.load(descriptor, 1);
				descriptor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isJerk(float[] acc){
		float magnitude = 0;
		for(int i=0;i<3;i++){
			magnitude += (Math.abs(acc[i]) - last_acc[i]) * (Math.abs(acc[i]) - last_acc[i]);
			last_acc[i] = Math.abs(acc[i]);
		}
		long current_time = System.currentTimeMillis();
		if(magnitude>100 && (current_time-last_loaded_time) > 170){
			Log.d("data",Float.toString(magnitude));
			last_loaded_time = current_time;
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mySensorManager.registerListener(this, myAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mySensorManager.unregisterListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.icon_full:	
			startActivity(new Intent(this,Icon.class));
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		sp.play(soundId, volume, volume, 1, 0, 1f);
		loaded = true;
	}

}
