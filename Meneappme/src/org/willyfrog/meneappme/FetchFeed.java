package org.willyfrog.meneappme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class FetchFeed extends AsyncTask<String, ProgressBar, InputStream> {
	private Exception exception;
	
	@Override
	protected InputStream doInBackground(String... params) {
		URL feedUrl = null;
		try
		{
			 feedUrl = new URL(params[0]);
		}
		catch (MalformedURLException e) {
			Log.e("Parser", "error al formar la URL: " + e.getMessage());
		}
		if (feedUrl!=null)
		{	
			try{
				 return feedUrl.openConnection().getInputStream();
			}
			catch (IOException e) {
				Log.e("Parser", "error al intentar coger el feed: " + e.getMessage());
				//throw new RuntimeException(e); //TODO: convertir a error legible por el usuario
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(InputStream result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	
	
}
