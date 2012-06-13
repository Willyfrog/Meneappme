package org.willyfrog.meneappme;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

public class RssParser {
	private InputStream feedUrl;
	private Titular titular;
	
	public RssParser(){
		this.feedUrl = null;
	}
	
	public RssParser(InputStream url){
			this.feedUrl = url;
		
	}
	
	public void setFeedUrl(InputStream feedUrl) {
		this.feedUrl = feedUrl;
	}

	public List<Titular> parse(){
		final List<Titular> titulares = new ArrayList<Titular>();
		
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild("item");
		item.setStartElementListener(new StartElementListener() {
			
			public void start(Attributes attributes) {
				titular = new Titular();
				
			}
		});
		
		item.setEndElementListener(new EndElementListener() {
			
			public void end() {
				titulares.add(titular);
				titular = null;
			}
		});
		
		item.getChild("title").setEndTextElementListener(new EndTextElementListener() {
			
			public void end(String body) {
				titular.setTitulo(body);
				
			}
		});
		
		item.getChild("link").setEndTextElementListener(new EndTextElementListener() {
			
			public void end(String body) {
				titular.setUrl(body);
			}
		});
		
		
		item.getChild("meneame:user").setEndTextElementListener(new EndTextElementListener() {
			
			public void end(String body) {
				titular.setAutor(body);
			}
		});
		
		item.getChild("votes").setEndTextElementListener(new EndTextElementListener() {
			
			public void end(String body) {
				titular.setPositivos(Integer.parseInt(body)); //TODO: catch exceptions
			}
		});

		//TODO: completar el resto de campos
		
		try{
			//InputStream input = getInputStream();
			if (feedUrl!=null)
				Xml.parse(feedUrl, Xml.Encoding.UTF_8, root.getContentHandler());
			else{
				return titulares;
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e); //TODO: convertir a error legible por el usuario
		}
		
		
		return titulares;
	}

	/*private InputStream getInputStream() {
		InputStream stre = null;
		try
		{
			 stre = feedUrl.openConnection().getInputStream();
		}
		catch (IOException e) {
			Log.e("Parser", "error al intentar coger el feed: " + e.getMessage());
			//throw new RuntimeException(e); //TODO: convertir a error legible por el usuario
			e.printStackTrace();
		}
		return stre;
	}*/
}
