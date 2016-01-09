package http.requests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PostRequest
{
	String url;
	ArrayList<BasicNameValuePair> nameValuePairs;
	HashMap<String,File> nameFilePairs;
	List<Header> headers;

	String content;
	String encoding;
	HttpResponse response;
	String json;

	public PostRequest(String url)
	{
	  this(url, "ISO-8859-1");
	}
	
	public PostRequest(String url, String encoding) 
	{
		this.url = url;
		this.encoding = encoding;
		nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameFilePairs = new HashMap<String,File>();
		headers = new ArrayList<Header>();
	}

	public void addData(String key, String value) 
	{
		BasicNameValuePair nvp = new BasicNameValuePair(key,value);
		nameValuePairs.add(nvp);
	}

	public void addJson(String json) {
		this.json = json;
	}

	public void addFile(String name, File f) {
		nameFilePairs.put(name,f);
	}

	public void addFile(String name, String path) {
		File f = new File(path);
		nameFilePairs.put(name,f);
	}
	
	public void addHeader(String name, String value) {
		headers.add(new BasicHeader(name, value));
	}

	public void send() 
	{
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			if (!nameValuePairs.isEmpty()) {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, encoding));
			}
			// add json
			else if (json != null) {
				StringEntity params =new StringEntity(json);
				httpPost.addHeader("content-type", "application/json");
				httpPost.setEntity(params);
			} 
			// file handling
			else if (!nameFilePairs.isEmpty()) {
				MultipartEntity mentity = new MultipartEntity();	
				Iterator<Entry<String,File>> it = nameFilePairs.entrySet().iterator();
			    while (it.hasNext()) {
			        Entry<String, File> pair =  it.next();
			        String name = (String) pair.getKey();
			        File f = (File) pair.getValue();
					mentity.addPart(name, new FileBody(f));
			    }				
				for (NameValuePair nvp : nameValuePairs) {
					mentity.addPart(nvp.getName(), new StringBody(nvp.getValue()));
				}
				httpPost.setEntity(mentity);
			}

			// add the headers to the request
			if (!headers.isEmpty()) {
				for (Header header : headers) {
					httpPost.addHeader(header);
				}
			}

			response = httpClient.execute( httpPost );
			HttpEntity   entity   = response.getEntity();
			this.content = EntityUtils.toString(response.getEntity());

			if( entity != null ) EntityUtils.consume(entity);

			httpClient.getConnectionManager().shutdown();

			// Clear it out for the next time
			nameValuePairs.clear();
			nameFilePairs.clear();

		} catch( Exception e ) { 
			e.printStackTrace(); 
		}
	}

	/* Getters
	_____________________________________________________________ */

	public String getContent()
	{
		return this.content;
	}

	public String getHeader(String name)
	{
		Header header = response.getFirstHeader(name);
		if(header == null)
		{
			return "";
		}
		else
		{
			return header.getValue();
		}
	}
}
