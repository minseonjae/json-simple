package seonjae.json.file;

import lombok.Getter;
import seonjae.json.Json;

import java.io.File;
import java.util.List;
import java.util.Set;

public class JsonConfig extends JsonFile {
	
	@Getter
	protected Json defaults = new Json();
	
	public JsonConfig(File file) {
		this(file, true);
		values = new Json();
	}
	public JsonConfig(File file, boolean space) {
		super(file, space);
	}
	
	public void clearDefaults() {
		defaults.clear();
	}
	public void clearValues() {
		values.clear();
	}
	public void clearAll() {
		clearDefaults();
		clearValues();
	}
	
	public JsonConfig saveDefaults() {
		save(file, defaults, true);
		return this;
	}
	public JsonConfig save() {
		if (!file.exists()) save(file, defaults, true);
		else {
			load(file);
			save(file, saveToJson(), true);
		}
		return this;
	}
	public JsonConfig load() {
		load(file);
		return this;
	}
	
	public String saveToString() {
		Json json = saveToJson();
		return space ? json.toSpaceJsonString() : json.toJsonString();
	}
	public Json saveToJson() {
		Json json = new Json();
		
		defaults.forEach((key, value) -> json.put(key, values.containsKey(key) ? values.get(key) : values));
		values.forEach((key, value) -> {
			if (!json.containsKey(key)) json.put(key, value);
		});
		return json;
	}
	
	public void addDefault(String key, Object value) {
		String[] keys = key.split("\\.");
		Json json = defaults;
		for (int i = 0; i < keys.length -1; i++) {
			Object o = json.get(keys[i]);
			if (o instanceof Json) json = (Json) o;
			else json.put(keys[i], json = new Json());
		}
		json.put(keys[keys.length - 1], value);
	}
	public void set(String key, Object value) {
		String[] keys = key.split("\\.");
		Json json = values;
		for (int i = 0; i < keys.length -1; i++) {
			Object o = json.get(keys[i]);
			if (o instanceof Json) json = (Json) o;
			else json.put(keys[i], json = new Json());
		}
		json.put(keys[keys.length - 1], value);
	}
	public Set<String> getKeys(String key) {
		String[] keys = key.split("\\.");
		Json json = values;
		for (int i = 0; i < keys.length && keys[0].length() > 0; i++) {
			Object o = json.get(keys[i]);
			if (o instanceof Json) json = (Json) o;
			else return null;
		}
		return json.keySet();
	}
	
	public Object get(String key) {
		String[] keys = key.split("\\.");
		Json json = values;
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
