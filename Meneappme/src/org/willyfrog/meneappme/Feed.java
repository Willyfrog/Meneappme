package org.willyfrog.meneappme;

import android.net.Uri;

public class Feed {
	private String categoria;
	private String url;
	
	public Feed(String categoria, String url) {
		this.categoria = categoria;
		this.url = url;
	}
	
	public String getCategoria() {
		return categoria;
	}

	public String getUrl() {
		return url;
	}
	
	public String toString() {
		return categoria;
	}
	
}
