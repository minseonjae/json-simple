package seonjae.json.file;

import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import seonjae.json.Json;

import java.io.*;

public class JsonFile {
	
	@Getter
	protected File file;
	
	@Getter
	protected Json values;
	
	@Getter
	protected boolean space;
	
	public JsonFile(File file) {
		this(file, null);
	}
	public JsonFile(File file, Json json) {
		this(file, json, false);
	}
	public JsonFile(File file, boolean space) {
		this(file, null, space);
	}
	public JsonFile(@NonNull File file, Json json, boolean space) {
		this.file = file;
		values = json;
		this.space = space;
	}
	
	public JsonFile save() {
		save(file, values, space);
		return this;
	}
	public JsonFile load() {
		load(file);
		return this;
	}
	
	@SneakyThrows(Exception.class)
	private static void createFile(File file) {
		if (file.exists()) return;
		
		int length = file.getPath().length() - file.getName().length();
		if (length < 1) new File(file.getPath().substring(0, length)).mkdirs();
		
		file.createNewFile();
	}
	@SneakyThrows(Exception.class)
	public final static void save(File file, Json json, boolean space) {
		if (!file.exists()) createFile(file);
		
		@Cleanup FileOutputStream fos = new FileOutputStream(file);
		@Cleanup OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		@Cleanup BufferedWriter bw = new BufferedWriter(osw);
		
		bw.write(space ? json.toSpaceJsonString() : json.toJsonString());
	}
	@SneakyThrows(Exception.class)
	public final static Json load(File file) {
		if (!file.exists()) throw new Exception("where is json file?");

		@Cleanup FileInputStream fis = new FileInputStream(file);
		@Cleanup InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		@Cleanup BufferedReader br = new BufferedReader(isr);
		
		StringWriter writer = new StringWriter();
		
		String line;
		while ((line = br.readLine()) != null) writer.write(line);
		
		Object load = Json.parse(writer.toString());
		
		if (!(load instanceof Json)) throw new Exception("file content is not json");
		
		return (Json) load;
	}
}
