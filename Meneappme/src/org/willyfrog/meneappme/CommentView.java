package org.willyfrog.meneappme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.willyfrog.meneappme.Meneappme.FetchFeedTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class CommentView extends Activity {

	ActionBar abar;
	String feed; //feed RSS
	String url;  
	String urlComments; //url comentarios meneame
	String autor;
	String titulo;
	String descripcion;
	ShareActionProvider compartidor;
	List<Comentario> datos;
	ListView listaComentarios;
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			Boolean res = super.onCreateOptionsMenu(menu);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.layout.webmenu, menu);
			MenuItem item = menu.findItem(R.id.compartirAction);
			compartidor = (ShareActionProvider) item.getActionProvider();
			compartidor.setShareHistoryFileName(null);
			//generamos el intent de compartir
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, urlComments);
			intent.setType("text/plain");
			setShareIntent(intent);
			listaComentarios = (ListView) findViewById(R.id.listaComentarios);
			
			return res;
	}
	
	private void setShareIntent(Intent shareIntent) {
		if (compartidor != null){
			compartidor.setShareIntent(shareIntent);
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.compartirAction ){
			startActivity(item.getIntent());
		}
		else if (item.getItemId()==R.id.abrirNavegador) {
			Intent navIntent = new Intent(); //no pudeo tener 2 shareactionprovider?
			navIntent.setAction(Intent.ACTION_VIEW);
			navIntent.setData(Uri.parse(urlComments));
			startActivity(navIntent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Ejecuta la tarea de traer los nuevos comentarios sacados de la url proporcionada
	 * @param url Direccion url de algun feed de comentarios de meneame
	 */
	private void fetchFeed(String url){
		new FetchFeedTask().execute(url);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		feed = b.getString("feed");
		url = b.getString("url");
		urlComments = b.getString("urlComentarios");
		autor = b.getString("autor");
		titulo = b.getString("titulo");
		descripcion = b.getString("descripcion");
		abar = getActionBar();
		abar.setTitle(titulo);
		setContentView(R.layout.comments);
		TextView title = (TextView) findViewById(R.id.tituloComment);
		title.setText(titulo);
		TextView author = (TextView) findViewById(R.id.autorComment);
		author.setText(autor);
		TextView urlText = (TextView) findViewById(R.id.urlComment);
		urlText.setText(url);
		TextView desc = (TextView) findViewById(R.id.cuerpoComment);
		desc.setText(descripcion);
		fetchFeed(feed);
		
	}

	
	
protected class FetchFeedTask extends AsyncTask<String, Integer, List<Comentario>> {
    	
    	//ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
    	
    	/*@Override
    	protected void onProgressUpdate(Integer... values) {
    		//Log.d("Progress", "actualizado con valor: " + values[0]);
    		progressBar.setProgress(values[0]);
    		super.onProgressUpdate(values);
    	}*/
    	
    	//lanzamos la tarea, se llama con execute
    	@Override
    	protected List<Comentario> doInBackground(String... params) {
    		URL feedUrl = null;
    		InputStream feed = null;
    		
    		//setProgress(0);
    		try
    		{
    			 feedUrl = new URL(params[0]);
    			 //publishProgress(15);
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
    				 //publishProgress(25);
    				 feed = feedUrl.openConnection().getInputStream();
    			}
    			catch (IOException e) {
    				Log.e("Parser", "error al intentar coger el feed: " + e.getMessage());
    				e.printStackTrace();
    			}
    			if (feed!=null){
    				return new RssCommentParser(feed).parse();
    			}
    		}
    		
    		return null;
    	}

    	//cuando la tarea vuelve, qu� hacemos?
    	@Override
    	protected void onPostExecute(List<Comentario> result) {
    		super.onPostExecute(result); 
    		Log.i("Task","volviendo de traer los feeds");
    		if (result!=null){
    			Log.i("Task","recuperados " + result.size() + " feeds");
        		//setProgress(50);
        		datos = result;
        		listaComentarios.setAdapter(new CommentAdapter(CommentView.this, datos));
        		//publishProgress(100);
    		}
    		else{
    			AlertDialog.Builder builder = new AlertDialog.Builder(CommentView.this);
				builder.setMessage("Error al intentar recuperar el feed");
				AlertDialog alerta = builder.show();
    			Log.i("Task", "no se recupero ningun feed");
    		}

    	}
    	
    }
    
}
