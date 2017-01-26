package http.requests;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
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
	String method;

	public GetRequest(String url) 
	{
		this(null, url);
	}
	public GetRequest(String method, String url) 
	{
		method(method);
		headers = new ArrayList<Header>(0);
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicHeader(name, value));
	}

	// only DELETE changes the method
	public void method(String delete) {
		if (delete != null && delete.equalsIgnoreCase(delete)) {
			this.method = delete;
		}
	}

	public void send() 
	{
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpRequestBase httpRequest;
			if (method != null && method.equalsIgnoreCase("DELETE")) {
				httpRequest = new HttpDelete(url);
			} else {
				httpRequest = new HttpGet(url);
			}

			// add the headers to the request
			if (!headers.isEmpty()) {
				for (Header header : headers) {
					httpRequest.addHeader(header);
				}
			}

			response = httpClient.execute( httpRequest );
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
