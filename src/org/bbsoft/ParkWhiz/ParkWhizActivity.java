package org.bbsoft.ParkWhiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ParkWhizActivity extends Activity {
    
	public static final String TAG = "ParkWhizActivity"; 
	
	private EditText mSearchEditText;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupView();
        
    }
    
    private void setupView()
    {
    	mSearchEditText = (EditText)findViewById(R.id.SearchTextView);
    }

    public void onSearchParkingClick (View sender)
    {
    	String where = mSearchEditText.getText().toString();
    	
    	if (where != null && where.length() > 0)
    	{
    		Log.i(TAG, "Begin search for location with name " + where);
    		searchParking(where);
    	}
    	
    }
    
    private void searchParking(String where)
    {
    	try
    	{
	    	HttpClient client = new DefaultHttpClient(); 
	    	HttpGet get = new HttpGet();
	    	String url = buildUrl(Constants.API_SEARCH_METHOD, new String[]{"destination=",
	    																	"&start=",
	    																	"&end=",
	    																	"&key="},
	    													   new String[]{URLEncoder.encode(where, "UTF-8"),
	    																	 "",
	    																	 "",
	    																	 Constants.API_KEY});
	    	Log.i(TAG, url);
	    	
			get.setURI(new URI(url));
			HttpResponse response =  client.execute(get);
			
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				String data = stream2String(entity.getContent());
				Log.i(TAG, "Search result : " + data);
			}
			
    	}
    	catch(Exception ex)
    	{
    		Log.e(TAG, "searchParking exception", ex);
    	}
    	
    }
    
    private String stream2String(InputStream is)
    {
    	StringBuilder sb = new StringBuilder();
    	BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    	String line = "";
    	try 
    	{
			while((line = rd.readLine()) != null)
				sb.append(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return sb.toString();
    }
    
    private String buildUrl(String methodName, String[] args, String[] argsVal)
    {
    	String ret = null;
    	ret = Constants.API_BASEURL + methodName;
    	
    	for (int i = 0; i < args.length; i++)
    		ret += args[i] + argsVal[i]; 
    	
    	return ret;
    }
    
    
}