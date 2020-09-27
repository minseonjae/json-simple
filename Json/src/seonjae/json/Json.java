package seonjae.json;

import lombok.SneakyThrows;
import seonjae.json.aware.JsonAware;
import seonjae.json.aware.JsonStreamAware;
import seonjae.json.parser.JsonParser;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Json extends LinkedHashMap<String, Object> implements Map<String, Object>, JsonAware, JsonStreamAware {

	private static final long serialVersionUID = 1957669340610102787L;

	public Json() {
		super();
	}
	public Json(Map<String, Object> map) {
		super(map);
	}
	
	@SneakyThrows(Exception.class)
	public static Object parse(Reader reader) {
		return new JsonParser().parse(reader);
	}
	@SneakyThrows(Exception.class)
	public static Object parse(String string) {
		return new JsonParser().parse(string);
	}
	
	public void writeJsonString(Writer writer) {
		writeJsonString(this, writer);
	}
	public void writeJsonString(Writer writer, int count) {
		writeJsonString(this, writer, count);
	}

	@SneakyThrows(Exception.class)
	public static void writeJsonString(Map<?, ?> map, Writer writer) {
		if(map == null) writer.write("null");
		else {
			boolean first = true;
			Iterator<?> iterator = map.entrySet().iterator();
			
			writer.write('{');
			while(iterator.hasNext()){
	            if (first) first = false;
	            else writer.write(',');
				Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
				writer.write('\"' + JsonValue.escape(String.valueOf(entry.getKey())) + "\":");
				JsonValue.writeJsonString(entry.getValue(), writer);
			}
			writer.write('}');
		}
	}
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Map<?, ?> map, Writer writer, int count) {
		if(map == null) writer.write("null");
		else {

			boolean first = true;
			Iterator<?> iterator = map.entrySet().iterator();

			StringWriter space = new StringWriter();

			for (int i = 0; i < count; i++) space.write('\t');

			writer.write("{\r\n");
			while(iterator.hasNext()){
	            if (first) first = false;
	            else writer.write(",\r\n");
				Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
				writer.write(space.toString() + "\t\"" + JsonValue.escape(String.valueOf(entry.getKey())) + "\":");
				JsonValue.writeJsonString(entry.getValue(), writer, count + 1);
			}
			writer.write("\r\n" + space.toString() + "}");
		}
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

	public String toJsonString() {
		return toJsonString(this);
	}
	public String toSpaceJsonString() {
		return toSpaceJsonString(this);
	}
	@SneakyThrows(Exception.class)
	public static String toJsonString(Map<?, ?> map) {
		StringWriter writer = new StringWriter();
		writeJsonString(map, writer);
		return writer.toString();
	}
	@SneakyThrows(Exception.class)
	public static String toSpaceJsonString(Map<?, ?> map) {
		StringWriter writer = new StringWriter();
		writeJsonString(map, writer, 0);
		return writer.toString();
	}
	
	public String toString() {
		return toJsonString();
	}
}
