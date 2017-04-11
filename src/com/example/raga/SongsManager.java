package com.example.raga;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.string;


public class SongsManager {
	final String MEDIA_PATH=new String("/sdcard/");
	private ArrayList<HashMap<String,String>> songList= new ArrayList<HashMap<String,String>>();
	public SongsManager()
	{
		
	}
	
	public ArrayList<HashMap<String, String>> getPlayList(){
		File home=new File(MEDIA_PATH);
		if(home.listFiles(new FileExtensionFilter()).length>0){
			for(File file:home.listFiles(new FileExtensionFilter())){
				HashMap<String, String> song= new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0,(file.getName().length()-4)));
				song.put("songPath", file.getPath());
				
				songList.add(song);
			}
		}
		
		
		return songList;
		
		
	}
	
	class FileExtensionFilter implements FilenameFilter{

		@Override
		public boolean accept(File dir, String name) {
			// TODO Auto-generated method stub
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
		
	}
	
}
