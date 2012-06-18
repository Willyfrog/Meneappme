package org.willyfrog.meneappme;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ShareActionProvider;

public class VerWeb extends Activity {
	
	private WebView wv;
	private ActionBar abar;
	private String url;
	
	private ShareActionProvider compartidor;
	private ShareActionProvider navegador;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			Boolean res = super.onCreateOptionsMenu(menu);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.layout.webmenu, menu);
			MenuItem item = menu.findItem(R.id.compartirAction);
			compartidor = (ShareActionProvider) item.getActionProvider();
			
			//generamos el intent de compartir
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, url);
			intent.setType("text/plain");
			setShareIntent(intent);
			
			//generamos el intent de navegar
			MenuItem navItem = menu.findItem(R.id.abrirNavegador);
			navegador = (ShareActionProvider) navItem.getActionProvider();
			Intent navIntent = new Intent();
			navIntent.setAction(Intent.ACTION_VIEW);
			navIntent.setData(Uri.parse(url));
			setNavegadorIntent(navIntent);
			return res;
	}
	
	private void setShareIntent(Intent shareIntent) {
		if (compartidor != null){
			compartidor.setShareIntent(shareIntent);
		}

	}

	private void setNavegadorIntent(Intent shareIntent) {
		if (navegador != null){
			navegador.setShareIntent(shareIntent);
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.compartirAction ){
			startActivity(item.getIntent());
		}
		else if (item.getItemId()==R.id.abrirNavegador) {
			startActivity(item.getIntent());
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//cargamos la url de la que viene
		Bundle b = getIntent().getExtras();
		url = b.getString("url");
		Log.d("verweb","arrancando navegador");
		setContentView(R.layout.webnoticia);
		abar = getActionBar();
		abar.setDisplayShowTitleEnabled(false);
		wv = (WebView) findViewById(R.id.webview);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(url);
		
	}

}
