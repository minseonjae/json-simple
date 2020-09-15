package seonjae.json;

import lombok.SneakyThrows;
import seonjae.json.aware.JsonAware;
import seonjae.json.aware.JsonStreamAware;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends ArrayList<Object> implements JsonAware, JsonStreamAware {
	
	private static final long serialVersionUID = 3957988303675231981L;
	
	public JsonArray(){
		super();
	}
	public JsonArray(Collection<?> c) {
		super(c);
	}
	
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Object[] array, Writer out) {
		if(array == null) out.write("null");
		else if(array.length == 0) out.write("[]");
		else {
			out.write("[");
			JsonValue.writeJsonString(array[0], out);

			for (int i = 1; i < array.length; i++) {
				out.write(",");
				JsonValue.writeJsonString(array[i], out);
			}

			out.write("]");
		}
	}
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Object[] array, Writer writer, int count) {
		if (array == null) writer.write("null");
		else if (array.length == 0) writer.write("[]");
		else {
			StringWriter space = new StringWriter();
			for (int i = 0; i < count; i++) space.write("\t");

			writer.write("[\r\n\t" + space.toString());
			for (int i = 0; i < array.length; i++) {
	            if (i != 0) writer.write(",\r\n\t" + space.toString());

				JsonValue.writeJsonString(array[0], writer, count + 1);
			}
			writer.write("\r\n" + space.toString() + "]");
		}
	}
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Collection<?> collection, Writer writer) {
		if (collection == null) writer.write("null");
		else if (collection.size() == 0) writer.write("[]");
		else {
			boolean first = true;
			Iterator<?> iterator = collection.iterator();

			writer.write('[');
			while (iterator.hasNext()) {
	            if (first) first = false;
	            else writer.write(',');

				Object value = iterator.next();
				if(value == null) {
					writer.write("null");
					continue;
				}
				JsonValue.writeJsonString(value, writer);
			}
			writer.write(']');
		}
	}
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Collection<?> collection, Writer writer, int count) {
		if (collection == null) writer.write("null");
		else if (collection.size() == 0) writer.write("[]");
		else {
			boolean first = true;
			Iterator<?> iterator = collection.iterator();
			
			StringWriter space = new StringWriter();
			for (int i = 0; i < count; i++) space.write("\t");
			
			writer.write("[\r\n\t" + space.toString());
			while (iterator.hasNext()) {
	            if (first) first = false;
	            else writer.write(",\r\n\t" + space.toString());
	            
				Object value = iterator.next();
				JsonValue.writeJsonString(value, writer, count + 1);
			}
			writer.write("\r\n" + space.toString() + "]");
		}
	}
	public void writeJsonString(Writer writer) {
		writeJsonString(this, writer);
	}
	public void writeJsonString(Writer writer, int count) {
		writeJsonString(this, writer, count);
	}
	
	@SneakyThrows(Exception.class)
	public static String toJsonString(List<?> list) {
		StringWriter writer = new StringWriter();
		writeJsonString(list, writer);
		return writer.toString();
	}
	@SneakyThrows(Exception.class)
	public static String toSpaceJsonString(List<?> list, int count) {
		StringWriter writer = new StringWriter();
		writeJsonString(list, writer, count);
		return writer.toString();
	}
	@SneakyThrows(Exception.class)
	public static String toJsonString(Object[] array) {
		StringWriter writer = new StringWriter();
		writeJsonString(array, writer);
		return writer.toString();
	}
	@SneakyThrows(Exception.class)
	public static String toSpaceJsonString(Object[] array, int count) {
		StringWriter writer = new StringWriter();
		writeJsonString(array, writer, count);
		return writer.toString();
	}
	public static String toJsonString(Collection<?> collection) {
		StringWriter writer = new StringWriter();
		writeJsonString(collection, writer);
		return writer.toString();
	}
	public static String toSpaceJsonString(Collection<?> collection, int count) {
		StringWriter writer = new StringWriter();
		writeJsonString(collection, writer, count);
		return writer.toString();
	}
	
	public String toJsonString() {
		return toJsonString(this);
	}
	public String toSpaceJsonString() {
		return toSpaceJsonString(this, 0);
	}
	
	public String toString() {
		return toJsonString();
	}
}
