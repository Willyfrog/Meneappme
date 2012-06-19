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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
	
	private Feed[] feeds;	//contiene los feeds a los que se puede navegar 

	private ListView listaTitulares; //contiene la lista de titulares + metainfo
	
	private ActionBar abar;	//handle donde dejar la ActionBar y poder trastear con ella desde diversos sitios
	
	/**
	 * Ejecuta la tarea de traer los nuevos titulares sacados de la url proporcionada
	 * @param url Direccion url de algun feed de titulares de meneame
	 */
	private void fetchFeed(String url){
		new FetchFeedTask().execute(url);
	}
	
	// lee de layout/menu.xml y forma los items del menu de la actionbar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			Boolean res = super.onCreateOptionsMenu(menu);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.layout.menu, menu);
			return res;
	}
	
	//si se seleeciona un item de la Actionbar, aqui se hace algo
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//se deberia comprobar el id del menu item, pero mientras solo haya una accion posible
		fetchFeed(feeds[abar.getSelectedNavigationIndex()].getUrl());
		return super.onOptionsItemSelected(item);
	}
	
	//Opciones contextuales del item
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
        
        //Establecemos la ActionBar como la queremos
        abar = getActionBar();
        abar.setNavigationMode(abar.NAVIGATION_MODE_LIST);
        abar.setDisplayShowTitleEnabled(false);
        
        //Generamos la lista de Feeds y los a�adimos a la actionbar
        feeds = new Feed[] {
    			new Feed("Portada", this.getString(R.string.feedPortada) + "?rows=30"),	// TODO: parametrizar numero de items
    			new Feed("Pendientes", this.getString(R.string.feedPendientes))}; 	// uri: http://www.meneame.net/rss2.php?status=queued
        SpinnerAdapter feedAdapter = new ArrayAdapter<Feed> (this, android.R.layout.simple_spinner_dropdown_item, feeds);
        abar.setListNavigationCallbacks(feedAdapter, new OnNavigationListener() {
			
        	//Si pulsas sobre el item del menu, trae el feed apropiado
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				
				fetchFeed(feeds[itemPosition].getUrl());
				return true;
			}
		} 
        		);

        setContentView(R.layout.main); //Establece la vista
        
        //Hacemos la primera carga de titulares
        listaTitulares = (ListView) findViewById(R.id.titularesList);
        registerForContextMenu(listaTitulares);
        listaTitulares.setOnItemClickListener(new OnItemClickListener() {

        	//Si pulsamos sobre un item, lo abrimos en una activity nueva
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
        
        listaTitulares.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adaptador, View view,
					int pos, long id) {
				Log.d("ListView", "Long live the click!");
				Titular t = (Titular) listaTitulares.getItemAtPosition(pos);
				Bundle b = new Bundle();
				b.putString("titulo", t.getTitulo());
				b.putString("autor", t.getAutor());
				b.putString("descripcion", t.getDescripcion());
				b.putString("feed", t.getFeedComentarios().toString());
				b.putString("url", t.getUrl().toString());
				b.putString("urlComentarios", t.getUrlComentarios());
				Intent intent = new Intent(Meneappme.this, CommentView.class);
				intent.putExtras(b);
				startActivity(intent);
				return true;
			}
        	
		});

        datos = new ArrayList<Titular>();
		fetchFeed(this.getString(R.string.feedPortada) + "&rows=20"); //TODO: parametrizar
        
    }
    
    /**
     * Clase para traer feeds de meneame como tarea en background
     * @author gvaya
     *
     */
    protected class FetchFeedTask extends AsyncTask<String, Integer, List<Titular>> {
    	
    	ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		Log.d("Progress", "actualizado con valor: " + values[0]);
    		progressBar.setProgress(values[0]);
    		super.onProgressUpdate(values);
    	}
    	
    	//lanzamos la tarea, se llama con execute
    	@Override
    	protected List<Titular> doInBackground(String... params) {
    		URL feedUrl = null;
    		InputStream feed = null;
    		
    		publishProgress(0);
    		try
    		{
    			 feedUrl = new URL(params[0]);
    			 publishProgress(15);
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
    				 publishProgress(25);
    				 feed = feedUrl.openConnection().getInputStream();
    			}
    			catch (IOException e) {
    				Log.e("Parser", "error al intentar coger el feed: " + e.getMessage());
    				e.printStackTrace();
    			}
    			if (feed!=null){
    				return new RssDomParser(feed).parse();
    			}
    		}
    		
    		return null;
    	}

    	//cuando la tarea vuelve, qu� hacemos?
    	@Override
    	protected void onPostExecute(List<Titular> result) {
    		super.onPostExecute(result); 
    		Log.i("Task","volviendo de traer los feeds");
    		if (result!=null){
    			Log.i("Task","recuperados " + result.size() + " feeds");
        		//setProgress(50);
        		datos = result;
        		listaTitulares.setAdapter(new TitularAdapter(Meneappme.this, datos));
        		publishProgress(100);
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