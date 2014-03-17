package com.newbee.kristian.KOS.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
	public Context context;
	
	public Connection(Context context){
		this.context = context;
	}
	
	public boolean isConnected(){
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager)
			this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
	    		.getState() == NetworkInfo.State.CONNECTED) {
	        //we are connected to a network
	        connected = true;
	    }
	    else
	        connected = false;
		return connected;
	}
	
	public InputStream doGetConnect(String url) throws ClientProtocolException, IOException{
		HttpGet httpPost = new HttpGet(url);
		
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		BasicHttpResponse httpResponse = (BasicHttpResponse)  httpClient.execute(httpPost);

		HttpEntity entity = httpResponse.getEntity();
		return entity.getContent();
	}
}
