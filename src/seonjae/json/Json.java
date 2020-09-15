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
