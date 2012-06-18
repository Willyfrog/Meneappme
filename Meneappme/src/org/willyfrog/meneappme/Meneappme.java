package org.willyfrog.meneappme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;



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
		new FetchFeedTask().execute(url);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			Boolean res = super.onCreateOptionsMenu(menu);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.layout.menu, menu);
			return res;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		fetchFeed(feeds[abar.getSelectedNavigationIndex()].getUrl());
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//Opciones contextuales
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "Arrancando");
        abar = getActionBar();
        abar.setNavigationMode(abar.NAVIGATION_MODE_LIST);
        abar.setDisplayShowTitleEnabled(false);
        
        //TODO: a�adir opciones a la barra
        feeds = new Feed[] {
    			new Feed("Portada", this.getString(R.string.feedPortada) + "?rows=30"),	// TODO: parametrizar numero de items
    			new Feed("Pendientes", this.getString(R.string.feedPendientes))}; 	// uri: http://www.meneame.net/rss2.php?status=queued
        SpinnerAdapter feedAdapter = new ArrayAdapter<Feed> (this, android.R.layout.simple_spinner_dropdown_item, feeds);
        abar.setListNavigationCallbacks(feedAdapter, new OnNavigationListener() {
			
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				
				fetchFeed(feeds[itemPosition].getUrl());
				return true;
			}
		} 
        		);
        //ArrayAdapter<Feed> feedAdapter = new ArrayAdapter<Feed>(this, android.R.layout.simple_spinner_dropdown_item, feeds);
        setContentView(R.layout.main);
        listaTitulares = (ListView) findViewById(R.id.titularesList);
        registerForContextMenu(listaTitulares);
        listaTitulares.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adaptador, View view, int position,
					long id) {
				Titular t = (Titular) listaTitulares.getItemAtPosition(position);
				//TODO: pasar la url a la actividad
				Bundle b = new Bundle();
				b.putString("url", t.getUrl().toString());
				Intent intent = new Intent(Meneappme.this, VerWeb.class);
				intent.putExtras(b);
				startActivity(intent);
			}
        	
		});
        
        //final Spinner listaFeeds = (Spinner) findViewById(R.id.feedSpin);

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
    		setProgress(0);
    		try
    		{
    			 feedUrl = new URL(params[0]);
    			 setProgress(15);
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
    				 setProgress(25);
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
        		listaTitulares.setAdapter(new TitularAdapter(Meneappme.this, datos));
        		setProgress(100);
        		/*TitularAdapter ta = (TitularAdapter) listaTitulares.getAdapter();
        		if (ta!=null){
        			Log.i("Task", "Actualizando datos");
        			ta.setDatos(datos);
        			
        			setProgress(100);
        		}
        		else{
        			Log.w("Task", "no se pudo recuperar un adaptador de titulares, creamos uno nuevo");
        			listaTitulares.setAdapter(new TitularAdapter(Meneappme.this, datos));
        			setProgress(100);
        		}*/
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