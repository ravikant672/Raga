package com.example.raga;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;







import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HomeActivity extends Activity implements SensorEventListener {
	
	CheckBox cb_sensor;
	ListView lv_songs;
	TextView timer1,timer2;
	SeekBar sb_playing;
	Button bt_exit;
	Button bt_prev,bt_play,bt_next,bt_stop,bt_pause;
	MediaPlayer mp;
	static String value;
	static String path;
	static int vol=7;
	 String songs[];
	
	SensorManager sm;
	//SensorManager sm2;
	Sensor s;
	//Sensor s2;
	AudioManager am;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		lv_songs=(ListView) findViewById(R.id.songsList);
		sb_playing=(SeekBar) findViewById(R.id.seekBar1);
		bt_exit=(Button) findViewById(R.id.exitButton);
		bt_prev=(Button) findViewById(R.id.prevButton);
		bt_play=(Button) findViewById(R.id.playButton);
		bt_pause=(Button) findViewById(R.id.pauseButton);
		bt_next=(Button) findViewById(R.id.nextButton);
		bt_stop=(Button) findViewById(R.id.stopButton);
		cb_sensor=(CheckBox) findViewById(R.id.usesensor);
		timer1=(TextView) findViewById(R.id.timer1);
		timer2 = (TextView) findViewById(R.id.timer2);
		
		am=(AudioManager) getSystemService(AUDIO_SERVICE);
		
		sm= (SensorManager) getSystemService(SENSOR_SERVICE);
		//sm2= (SensorManager) getSystemService(SENSOR_SERVICE);
		s= sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
		//s2=sm2.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		
		
		
		
		
		try{
			
			
			
			
			File f=Environment.getExternalStoragePublicDirectory("Music/");
			//File f=new File("/sdcard/Music");
			//File f=Environment.getExternalStorageDirectory();
			songs=f.list();
			path= "sdcard/Music/"+songs[0].toString();
			value=songs[0].toString();
			
			
			
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,songs);
			lv_songs.setAdapter(adapter);
			registerForContextMenu(lv_songs);
			lv_songs.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					value = arg0.getItemAtPosition(position).toString();
					path= "sdcard/Music/"+value;
					if(mp==null){
						mp=MediaPlayer.create(HomeActivity.this, Uri.parse(path));
					}
					else{
						mp.stop();
						mp=MediaPlayer.create(HomeActivity.this, Uri.parse(path));
					}
					mp.start();
					sb_playing.setMax(mp.getDuration());
					bt_play.setVisibility(View.GONE);
					bt_pause.setVisibility(View.VISIBLE);
					if(mp.getCurrentPosition()== mp.getDuration()){
						nextMusic(bt_next);
					}
					
				}
			});
			
			
			
			
			sb_playing.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					// TODO Auto-generated method stub
					if(fromUser){
						if(mp!=null) mp.seekTo(sb_playing.getProgress());
						else Toast.makeText(HomeActivity.this, "Choose Song First", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			
			
		}
		catch(Exception e){
			
		}
		
		Thread t=new Thread(){
			public void run(){
				while(true){
				if(mp==null) 
					sb_playing.setProgress(0);
				else 
					sb_playing.setProgress(mp.getCurrentPosition());
				runOnUiThread(new Runnable() {
					public void run() {
						//tv.setText(Integer.toString(sb.getProgress()/1000));
						int min=sb_playing.getProgress()/60000;
						int val=sb_playing.getProgress()/1000;
						int sec=val%60;
						if(sec>9)
							timer1.setText("0"+min+":"+sec);
						else
							timer1.setText("0"+min+":0"+sec);
						if(mp!=null){
							int m=mp.getDuration()/60000;
							int v=mp.getDuration()/1000;
							int se=v%60;
							if(se>9)
								timer2.setText("0"+m+":"+se);
							else
								timer2.setText("0"+m+":0"+se);
						}
					}
				});
				}
			}
		};
		t.start();
		
		
		
	}

	
	
	
	
	public void playMusic(View v){
		
		if(mp==null){
			//Toast.makeText(this, path1, Toast.LENGTH_LONG).show();
			mp=MediaPlayer.create(HomeActivity.this, Uri.parse(path));
			mp.start();
			sb_playing.setMax(mp.getDuration());
			bt_play.setVisibility(View.GONE);
			bt_pause.setVisibility(View.VISIBLE);
			if(mp.getCurrentPosition()== mp.getDuration()){
				nextMusic(bt_next);
			}
		}
		else{
			if(mp.isPlaying()){
			
			}
			else{
				mp.start();
				sb_playing.setMax(mp.getDuration());
				bt_play.setVisibility(View.GONE);
				bt_pause.setVisibility(View.VISIBLE);
				if(mp.getCurrentPosition()== mp.getDuration()){
					nextMusic(bt_next);
				}
			}
	
		}
		
	}
	
	public void pauseMusic(View v){
		Toast.makeText(this, "paused", Toast.LENGTH_LONG).show();
			mp.pause();
			bt_play.setVisibility(View.VISIBLE);
			bt_pause.setVisibility(View.GONE);
		
	}
	public void prevMusic(View v){
		if(mp==null){
			Toast.makeText(this, "choose song first ", Toast.LENGTH_LONG).show();
		}
		
		else{
			int index=0;
			for (int i = 0; i < songs.length; i++) {
			    if(songs[i].toString().equals(value)){
			    	index= i;
			    	
			    	break;
			    }
			}
			
			index--;
			path= "sdcard/Music/"+songs[index].toString();
			mp.stop();
			mp=MediaPlayer.create(HomeActivity.this, Uri.parse(path));
			mp.start();
			value=songs[index].toString();
			sb_playing.setMax(mp.getDuration());
			bt_play.setVisibility(View.GONE);
			bt_pause.setVisibility(View.VISIBLE);
			if(mp.getCurrentPosition()== mp.getDuration()){
				nextMusic(bt_next);
			}
		}
	}
	public void nextMusic(View v){
		if(mp==null){
			Toast.makeText(this, "choose song first ", Toast.LENGTH_LONG).show();
		}
		//Toast.makeText(this, songs[1].toString(), Toast.LENGTH_LONG).show();
		else{
			int index=0;
			for (int i = 0; i < songs.length; i++) {
			    if(songs[i].toString().equals(value)){
			    	index= i;
			    	
			    	break;
			    }
			}
			
			index++;
			path= "sdcard/Music/"+songs[index].toString();
			mp.stop();
			mp=MediaPlayer.create(HomeActivity.this, Uri.parse(path));
			mp.start();
			value=songs[index].toString();
			sb_playing.setMax(mp.getDuration());
			bt_play.setVisibility(View.GONE);
			bt_pause.setVisibility(View.VISIBLE);
			if(mp.getCurrentPosition()== mp.getDuration()){
				nextMusic(bt_next);
			}
			
		}
	}
	
	public void stopMusic(View v){
		if(mp!=null) 
			mp.stop();
		mp=null;
		sb_playing.setProgress(0);
		timer1.setText("00:00");
		timer2.setText("00:00");
		bt_play.setVisibility(View.VISIBLE);
		bt_pause.setVisibility(View.GONE);
	}
	public void exit(View v){
		finish();
        System.exit(0);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		sm.registerListener(this, s,SensorManager.SENSOR_DELAY_NORMAL);
		//sm2.registerListener(this, s2,SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		sm.unregisterListener(this);
		//sm2.unregisterListener(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}





	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(cb_sensor.isChecked()==true){
		
		float v[]=event.values;
		//tv.setText(Float.toString(v[0])+"**"+Float.toString(v[1])+"**"+Float.toString(v[2]));
		if(v[0]>=7){
			prevMusic(bt_prev);
		}
		if(v[0]<=-7){
			nextMusic(bt_next);
		}
		
		if(v[1]<=-7){
			vol++;
			//volume increase	
			am.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
		}
		if(v[1]>=7){
		//volume decrease	
			vol--;
			am.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
		}
		if(v[2]<=-7){
			mp.pause();
		}
		else
			mp.start();
			
		
		
		/*if (s2.getType() == Sensor.TYPE_PROXIMITY){
			 float v[]=event.values;
			 if(v[0]==0){
				mp.pause(); 
			 }
			 else{
				 mp.start();
			 }
		 }
		*/
		
		
		
		
		
		}
	}
	
	public void authorInfo(View v){
		Intent i = new Intent(this , AuthorActivity.class);
		startActivity(i);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.second, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//long press on layout than show menu
		
		if(item.getTitle().toString().equals("play"))
		{	//rl.setBackgroundColor(Color.RED);
			playMusic(bt_play);
		}	
		else if(item.getTitle().toString().equals("pause"))
		{//rl.setBackgroundColor(Color.GREEN);
			pauseMusic(bt_pause);
		}
		else
			stopMusic(bt_stop);
			//rl.setBackgroundColor(Color.BLUE);
		return super.onContextItemSelected(item);
	}

	
	
	
	
}


