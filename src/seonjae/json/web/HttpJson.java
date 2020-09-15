package seonjae.json.web;

import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import seonjae.json.Json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HttpJson {
	
	@Getter
	protected String url;
	
	@Getter
	protected Json result;

	public HttpJson() {
	}
	public HttpJson(String url) {
		this.url = url;
		parse();
	}
	
	@SneakyThrows(Exception.class)
	private void parse() {
		URL url = new URL(this.url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		@Cleanup InputStreamReader isr = new InputStreamReader(connection.getInputStream());
		@Cleanup BufferedReader br = new BufferedReader(isr);
		
		StringWriter writer = new StringWriter();
		
		String line;
		while ((line = br.readLine()) != null) writer.write(line);
		

		Object load = Json.parse(writer.toString());
		
		if (!(load instanceof Json)) throw new Exception("http content is not json");
		
		result = (Json) load;
	}
	

	public Object get(String key) {
		String[] keys = key.split("\\.");
		Json json = result;
		for (int i = 0; i < keys.length - 1; i++) {
			Object o = json.get(keys[i]);
			if (o instanceof Json) json = (Json) o;
			else return null;
		}
		return json.get(keys[keys.length - 1]);
	}
	public Json getJson(String key) {
		Object o = get(key);
		return o != null ? (Json) o : null;
	}
	public String getString(String key) {
		Object o = get(key);
		return o != null ? get(key).toString() : null;
	}
	public Boolean getBoolean(String key) {
		Object o = get(key);
		return o != null ? Boolean.parseBoolean(getString(key)) : null;
	}
	public Long getLong(String key) {
		try {
			String value = getString(key);
			return value != null ? Long.parseLong(getString(key)) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	public Integer getInt(String key) {
		try {
			String value = getString(key);
			return value != null ? Integer.parseInt(getString(key)) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	public Double getDouble(String key) {
		try {
			String value = getString(key);
			return value != null ? Double.parseDouble(getString(key)) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	public Float getFloat(String key) {
		try {
			String value = getString(key);
			return value != null ? Float.parseFloat(getString(key)) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	public Short getShort(String key) {
		try {
			String value = getString(key);
			return value != null ? Short.parseShort(getString(key)) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getList(String key) {
		try {
			Object o = get(key);
			if (o == null) return null;
			return (List<Object>) o;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key) {
		try {
			Object o = get(key);
			if (o == null) return null;
			if (o instanceof List<?>) return (List<String>) get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Integer> getIntegerList(String key) {
		try {
			Object o = get(key);
			if (o == null) return null;
			if (o instanceof List<?>) return (List<Integer>) get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Long> getLongList(String key) {
		try {
			Object o = get(key);
			if (o == null) return null;
			if (o instanceof List<?>) return (List<Long>) get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Double> getDoubleList(String key) {
		try {
			Object o = get(key);
			if (o == null) return null;
			if (o instanceof List<?>) return (List<Double>) get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Float> getFloatList(String key) {
		try {
			Object o = get(key);
			if (o == null) return null;
			if (o instanceof List<?>) return (List<Float>) get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
