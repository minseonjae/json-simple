package seonjae.json.web;

import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import seonjae.json.Json;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

public class HttpsJson extends HttpJson {
	
	@Getter
	protected String url;
	
	@Getter
	protected Json result;
	
	public HttpsJson(String url) {
		this.url = url;
		parse();
	}
	
	@SneakyThrows(Exception.class)
	private void parse() {
		URL url = new URL(this.url);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, null, null);
		connection.setSSLSocketFactory(context.getSocketFactory());
		connection.setInstanceFollowRedirects(true);
		
		@Cleanup InputStreamReader isr = new InputStreamReader(connection.getInputStream());
		@Cleanup BufferedReader br = new BufferedReader(isr);
		
		StringWriter writer = new StringWriter();
		
		String line;
		while ((line = br.readLine()) != null) writer.write(line);
		
		Object load = Json.parse(writer.toString());
		
		if (!(load instanceof Json)) throw new Exception("http content is not json");
		
		result = (Json) load;
	}
}
