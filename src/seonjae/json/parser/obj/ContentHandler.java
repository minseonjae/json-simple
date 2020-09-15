package seonjae.json.parser.obj;

import seonjae.json.parser.exception.ParseException;

import java.io.IOException;

public interface ContentHandler {
	public void startJSON() throws ParseException, IOException;
	public void endJSON() throws ParseException, IOException;
	public boolean startObject() throws ParseException, IOException;
	public boolean endObject() throws ParseException, IOException;
	public boolean startObjectEntry(String key) throws ParseException, IOException;
	public boolean endObjectEntry() throws ParseException, IOException;
	public boolean startArray() throws ParseException, IOException;
	public boolean endArray() throws ParseException, IOException;
	public boolean primitive(Object value) throws ParseException, IOException;
}
