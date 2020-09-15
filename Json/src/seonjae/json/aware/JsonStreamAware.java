package seonjae.json.aware;

import java.io.Writer;

public interface JsonStreamAware {
	public void writeJsonString(Writer out);
	public void writeJsonString(Writer out, int count);
}
