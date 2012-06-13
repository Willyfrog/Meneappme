package org.willyfrog.meneappme;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class feedsAdapter extends ArrayAdapter {
	
	private Feed[] feeds;
	private Activity contexto;
	
	public feedsAdapter(Activity context, Feed[] feeds){
		super(context, android.R.layout.simple_spinner_dropdown_item, feeds);
		this.feeds = feeds;
		this.contexto = context;
	}

}
