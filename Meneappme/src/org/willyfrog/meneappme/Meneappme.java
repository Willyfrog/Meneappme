package org.willyfrog.meneappme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;



public class Meneappme extends Activity {
	
	/*private Titular[] datos = new Titular[] {
			new Titular(),
			new Titular(),
			new Titular(),
			new Titular()
	};*/
	static final String LOGTAG = "Meneapp";
	private List<Titular> datos;
	
	private Feed[] feeds; 

	private ListView listaTitulares;
	
	private ActionBar abar;
	
	private void fetchFeed(String url){
		//ProgressDialog progreso = ProgressDialog.show(this, "De camino a buscar los titulares", "Por favor, espere...", true);
		new FetchFeedTask().execute(url);

		//progreso.hide();
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "Arrancando");
        setContentView(R.layout.main);

        feeds = new Feed[] {
    			new Feed("Portada", this.getString(R.string.feedPortada) + "?rows=30"),	// TODO: parametrizar numero de items
    			new Feed("Pendientes", this.getString(R.string.feedPendientes))}; 	// uri: http://www.meneame.net/rss2.php?status=queued
        
        ArrayAdapter<Feed> feedAdapter = new ArrayAdapter<Feed>(this, android.R.layout.simple_spinner_dropdown_item, feeds);
        listaTitulares = (ListView) findViewById(R.id.titularesList);
        final Spinner listaFeeds = (Spinner) findViewById(R.id.feedSpin);
        ImageButton refreshBtn = (ImageButton) findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Feed f = (Feed) listaFeeds.getSelectedItem();
				Log.i(LOGTAG, "Recuperando feed " + f.getCategoria());
				fetchFeed(f.getUrl());
			}
		});
        datos = new ArrayList<Titular>();
		fetchFeed(this.getString(R.string.feedPortada) + "&rows=20"); //TODO: parametrizar
		/*if (datos == null){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("No hay conexion a internet");
			AlertDialog alerta = builder.show();
			
		}
		else{	
			listaTitulares.setAdapter(new TitularAdapter(this, datos));
		}
        listaFeeds.setAdapter(feedAdapter);*/
        
    }
    
    protected class FetchFeedTask extends AsyncTask<String, ProgressBar, List<Titular>> {
    	
    	@Override
    	protected List<Titular> doInBackground(String... params) {
    		URL feedUrl = null;
    		InputStream feed = null;
    		//setProgress(0);
    		try
    		{
    			 feedUrl = new URL(params[0]);
    			 //setProgress(15);
    		}
    		catch (MalformedURLException e) {
    			/*AlertDialog.Builder builder = new AlertDialog.Builder(Meneappme.this);
    			builder.setMessage("Esto es raro... no se que hacer con esta url");
    			AlertDialog alerta = builder.create();*/
    			Log.e("Parser", "error al formar la URL: " + e.getMessage());
    		}
    		if (feedUrl!=null)
    		{	
    			try{
    				 feed = feedUrl.openConnection().getInputStream();
    				 //setProgress(25);
    			}
    			catch (IOException e) {
    				/*AlertDialog.Builder builder = new AlertDialog.Builder(Meneappme.this);
    				builder.setMessage("Error al intentar recuperar el feed");
    				AlertDialog alerta = builder.create();*/
    				Log.e("Parser", "error al intentar coger el feed: " + e.getMessage());
    				//throw new RuntimeException(e); //TODO: convertir a error legible por el usuario
    				e.printStackTrace();
    			}
    			if (feed!=null){
    				return new RssDomParser(feed).parse();
    			}
    		}
    		
    		return null;
    	}

    	@Override
    	protected void onPostExecute(List<Titular> result) {
    		super.onPostExecute(result); 
    		Log.i("Task","volviendo de traer los feeds");
    		if (result!=null){
    			Log.i("Task","recuperados " + result.size() + " feeds");
        		//setProgress(50);
        		datos = result;
        		TitularAdapter ta = (TitularAdapter) listaTitulares.getAdapter();
        		if (ta!=null){
        			Log.i("Task", "Actualizando datos");
        			ta.setDatos(datos);
        			//setProgress(100);
        		}
        		else{
        			Log.w("Task", "no se pudo recuperar un adaptador de titulares, creamos uno nuevo");
        			listaTitulares.setAdapter(new TitularAdapter(Meneappme.this, datos));
        			//setProgress(100);
        		}
    		}
    		else{
    			AlertDialog.Builder builder = new AlertDialog.Builder(Meneappme.this);
				builder.setMessage("Error al intentar recuperar el feed");
				AlertDialog alerta = builder.show();
    			Log.i("Task", "no se recupero ningun feed");
    		}

    	}
    	
    }
    
    
}