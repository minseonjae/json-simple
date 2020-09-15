package seonjae.json;

import lombok.SneakyThrows;
import seonjae.json.aware.JsonAware;
import seonjae.json.aware.JsonStreamAware;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class JsonValue {
	
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Object value, Writer writer) {
		if(value == null) writer.write("null");
		else {
			if(value instanceof String) writer.write('\"' + escape((String) value) + '\"');
			else if(value instanceof Double) {
				if(((Double)value).isInfinite() || ((Double)value).isNaN()) writer.write("null");
				else writer.write(value.toString());
			} else if(value instanceof Float) {
				if(((Float)value).isInfinite() || ((Float)value).isNaN()) writer.write("null");
				else writer.write(value.toString());
			} else if(value instanceof Number) writer.write(value.toString());
			else if(value instanceof Boolean) writer.write(value.toString());
			else if((value instanceof JsonStreamAware)) ((JsonStreamAware) value).writeJsonString(writer);
			else if((value instanceof JsonAware)) writer.write(((JsonAware) value).toJsonString());
			else if(value instanceof Map) Json.writeJsonString((Map<?, ?>) value, writer);
			else if(value instanceof Collection) JsonArray.writeJsonString((Collection<?>) value, writer);
			else if(value instanceof Byte[]) JsonArray.writeJsonString((Byte[]) value, writer);
			else if(value instanceof Short[]) JsonArray.writeJsonString((Short[]) value, writer);
			else if(value instanceof Integer[]) JsonArray.writeJsonString((Integer[]) value, writer);
			else if(value instanceof Long[]) JsonArray.writeJsonString((Long[]) value, writer);
			else if(value instanceof Float[]) JsonArray.writeJsonString((Float[]) value, writer);
			else if(value instanceof Double[]) JsonArray.writeJsonString((Double[]) value, writer);
			else if(value instanceof Boolean[]) JsonArray.writeJsonString((Boolean[]) value, writer);
			else if(value instanceof Character[]) JsonArray.writeJsonString((Character[]) value, writer);
			else if(value instanceof Object[]) JsonArray.writeJsonString((Object[]) value, writer);
			else writer.write(value.toString());
		}
	}
	@SneakyThrows(Exception.class)
	public static void writeJsonString(Object value, Writer writer, int count) {
		if(value == null) writer.write("null");
		else {
			if(value instanceof String) writer.write('\"' + escape((String) value) + '\"');
			else if(value instanceof Double) {
				if(((Double)value).isInfinite() || ((Double)value).isNaN()) writer.write("null");
				else writer.write(value.toString());
			} else if(value instanceof Float) {
				if(((Float)value).isInfinite() || ((Float)value).isNaN()) writer.write("null");
				else writer.write(value.toString());
			} else if(value instanceof Number) writer.write(value.toString());
			else if(value instanceof Boolean) writer.write(value.toString());
			else if((value instanceof JsonStreamAware)) ((JsonStreamAware) value).writeJsonString(writer, count);
			else if((value instanceof JsonAware)) writer.write(((JsonAware) value).toJsonString());
			else if(value instanceof Map) Json.writeJsonString((Map<?, ?>) value, writer, count);
			else if(value instanceof Collection) JsonArray.writeJsonString((Collection<?>) value, writer, count);
			else if(value instanceof Byte[]) JsonArray.writeJsonString((Byte[]) value, writer, count);
			else if(value instanceof Short[]) JsonArray.writeJsonString((Short[]) value, writer, count);
			else if(value instanceof Integer[]) JsonArray.writeJsonString((Integer[]) value, writer, count);
			else if(value instanceof Long[]) JsonArray.writeJsonString((Long[]) value, writer, count);
			else if(value instanceof Float[]) JsonArray.writeJsonString((Float[]) value, writer, count);
			else if(value instanceof Double[]) JsonArray.writeJsonString((Double[]) value, writer, count);
			else if(value instanceof Boolean[]) JsonArray.writeJsonString((Boolean[]) value, writer, count);
			else if(value instanceof Character[]) JsonArray.writeJsonString((Character[]) value, writer, count);
			else if(value instanceof Object[]) JsonArray.writeJsonString((Object[]) value, writer, count);
			else writer.write(value.toString());
		}
	}
	@SneakyThrows(Exception.class)
	public static String toJsonString(Object value) {
		StringWriter writer = new StringWriter();
		writeJsonString(value, writer);
		return writer.toString();
	}
	@SneakyThrows(Exception.class)
	public static String toJsonString(Object value, int count) {
		StringWriter writer = new StringWriter();
		writeJsonString(value, writer, count);
		return writer.toString();
	}
	
	public static String escape(String string){
		if(string == null) return null;
        StringBuffer sb = new StringBuffer();
        escape(string, sb);
        return sb.toString();
    }

    static void escape(String string, StringBuffer sb) {
    	final int len = string.length();
		for (int i = 0; i<len; i++) {
			char ch = string.charAt(i);
			switch(ch){
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) sb.append('0');
					sb.append(ss.toUpperCase());
				} else sb.append(ch);
			}
		}
	}
}
