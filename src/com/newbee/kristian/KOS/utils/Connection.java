package com.newbee.kristian.KOS.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
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
	    		.getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
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
		int timeoutConnection = (int) TimeUnit.SECONDS.toMillis(20);
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = (int) TimeUnit.SECONDS.toMillis(20);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		BasicHttpResponse httpResponse = (BasicHttpResponse)  httpClient.execute(httpPost);

		HttpEntity entity = httpResponse.getEntity();
		return entity.getContent();
	}
	
	public InputStream doPostConnect(String url, List<String[]> data) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = null;
		if (data.size() > 0){
			nameValuePairs = new ArrayList<NameValuePair>();
			for (int i = 0; i < data.size(); i++) {
		        nameValuePairs.add(new BasicNameValuePair(data.get(i)[0], data.get(i)[1].toString()));
			}
		}
		if (nameValuePairs != null)
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = (int) TimeUnit.MINUTES.toMillis(1);
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = (int) TimeUnit.MINUTES.toMillis(1);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		BasicHttpResponse httpResponse = (BasicHttpResponse)  httpClient.execute(httpPost);

		HttpEntity entity = httpResponse.getEntity();
		if (httpResponse.getStatusLine().getStatusCode() == 200)
			return entity.getContent();
		else 
			return null;
	}
}
