package http.requests;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.List;

public class GetRequest
{
	String url;
	String content;
	HttpResponse response;
	List<Header> headers;

	public GetRequest(String url) 
	{
		this.url = url;
		headers = new ArrayList<Header>(0);
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicHeader(name, value));
	}

	public void send() 
	{
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet(url);

			// add the headers to the request
			if (!headers.isEmpty()) {
				for (Header header : headers) {
					httpGet.addHeader(header);
				}
			}

			response = httpClient.execute( httpGet );
			HttpEntity entity = response.getEntity();
			this.content = EntityUtils.toString(response.getEntity());
			
			if( entity != null ) EntityUtils.consume(entity);
			httpClient.getConnectionManager().shutdown();
			
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
