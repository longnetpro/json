package info.longnetpro.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import info.longnetpro.json.util.StringUtils;

public class JsonSerializer {
	private Writer out = null;
	private JsonFormat format;
	private int depth = 0;

	public JsonSerializer(JsonFormat format) {
		super();
		this.format = format;
	}

	public JsonSerializer() {
		super();
		this.format = new JsonFormat();
	}

	public static JsonSerializer createDefault() {
		return new JsonSerializer();
	}

	public void serialize(JsonType jsonObj, Writer out) throws IOException {
		if (out == null) {
			throw new IOException("No writer specified.");
		}
		this.out = out;
		outAny(jsonObj);
		outNewLine();
		try {
			this.out.close();
		} catch (Exception e) {
			;
		}
	}

	public void serialize(JsonType jsonObj) throws IOException {
		serialize(jsonObj, new StringWriter());
	}

	private void outNewLine() throws IOException {
		if (!format.isCompactFormat())
			out.write(format.getNewLine());
	}

	private void outIndent() throws IOException {
		if (!format.isIndenting() || format.isCompactFormat())
			return;
		for (int i = 0; i < format.getIndent(); i++) {
			outSpace();
		}
	}

	private void indent() throws IOException {
		if (!format.isIndenting() || format.isCompactFormat())
			return;
		for (int i = 0; i < depth; i++) {
			outIndent();
		}
	}

	private void outSpace() throws IOException {
		if (!format.isCompactFormat())
			out.write(' ');
	}

	private void outObjectOpen() throws IOException {
		out.write('{');
		outNewLine();
	}

	private void outObjectClose() throws IOException {
		outNewLine();
		indent();
		out.write('}');
	}

	private void outArrayOpen() throws IOException {
		out.write('[');
		outNewLine();
	}

	private void outArrayClose() throws IOException {
		outNewLine();
		indent();
		out.write(']');
	}

	private void outPair() throws IOException {
		outSpace();
		out.write(':');
		outSpace();
	}

	private void outList() throws IOException {
		out.write(',');
		outNewLine();
	}

	private void outKey(String key, int maxLength) throws IOException {
		String s = key;
		if (!format.isCompactFormat()) {
			if (format.isKeyRightAligned()) {
				s = StringUtils.leftPad(s, maxLength);
			} else if (format.isValueAlign()) {
				s = StringUtils.rightPad(s, maxLength);
			}
		}
		out.write(s);
	}

	private void outString(String string) throws IOException {
		String s = toJsonString(string);
		out.write(s);
	}

	public static String hexAllCharacters(String string, boolean hexUpperCase) {
		StringBuilder sb = new StringBuilder("");
		sb.append('"');
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			sb.append("\\u");
			sb.append(StringUtils.toHexString(ch, hexUpperCase));
		}
		sb.append('"');
		return sb.toString();
	}

	public static String toJsonString(String string, boolean hexUpperCase, boolean hexSpecialChar, boolean hexNonAscii,
			boolean hexAscii, boolean escapeSlash) {
		StringBuilder sb = new StringBuilder("");
		sb.append('"');
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			String s = "" + ch;
			if (JsonParser.isSpecialCharacter(ch)) {
				// special
				s = hexSpecialChar ? "\\u" + StringUtils.toHexString(ch, hexUpperCase)
						: escapeSpecialChar(ch, escapeSlash);
			} else if (JsonParser.isControlCharacter(ch)) {
				// control
				s = "\\u" + StringUtils.toHexString(ch, hexUpperCase);
			} else if (ch > 127) {
				// non ASCII
				if (hexNonAscii) {
					s = "\\u" + StringUtils.toHexString(ch, hexUpperCase);
				}
			} else {
				// ASCII
				if (hexAscii) {
					s = "\\u" + StringUtils.toHexString(ch, hexUpperCase);
				}
			}
			sb.append(s);
		}
		sb.append('"');
		return sb.toString();
	}

	public static String escapeSpecialChar(char ch, boolean escapeSlash) {
		final String s1 = "\"\\/\b\f\n\r\t";
		final String s2 = "\"\\/bfnrt";
		String s = "" + ch;
		int pos = s1.indexOf(ch);
		if (ch == '/' && escapeSlash || ch != '/' && pos >= 0) {
			s = "\\" + s2.charAt(pos);
		}
		return s;
	}

	private String toJsonString(String string) {
		String s = "";
		if (format.isHexAllCharacters()) {
			s = hexAllCharacters(string, format.isHexUpperCase());
		} else {
			s = toJsonString(string, format.isHexUpperCase(), format.isHexSpecialChar(), format.isHexNonAscii(),
					format.isHexAscii(), format.isEscapeSlash());
		}
		return s;
	}

	private void outPrimitive(JsonType obj) throws IOException {
		// Number, Boolean, Null
		String s = obj.toJsonString();
		out.write(s);
	}

	private void outEmptyArray() throws IOException {
		out.write("[");
		outSpace();
		out.write("]");
	}

	private void outArray(JsonArray array) throws IOException {
		if (array.getArraySize() == 0) {
			outEmptyArray();
			return;
		}

		outArrayOpen();
		depth++;
		for (int i = 0; i < array.getArraySize(); i++) {
			JsonType obj = array.get(i);
			indent();
			outAny(obj);
			if (i != array.getArraySize() - 1) {
				outList();
			}
		}
		depth--;
		outArrayClose();
	}

	private int getMaxKeyLength(String[] list) {
		int length = 0;
		for (String s : list) {
			if (length < s.length())
				length = s.length();
		}
		return length;
	}

	private void outEmptyObject() throws IOException {
		out.write("{");
		outSpace();
		out.write("}");
	}

	private void outObject(JsonObject object) throws IOException {
		if (object.getPropertyCount() == 0) {
			outEmptyObject();
			return;
		}

		outObjectOpen();
		depth++;
		String[] list = new String[object.getPropertyCount()];

		for (int i = 0; i < object.getPropertyCount(); i++) {
			String key = object.getKey(i);
			list[i] = toJsonString(key);
		}
		int maxLength = getMaxKeyLength(list);
		for (int i = 0; i < object.getPropertyCount(); i++) {
			String key = object.getKey(i);
			JsonType obj = object.getProperty(key);
			indent();
			outKey(list[i], maxLength);
			outPair();
			outAny(obj);
			if (i != object.getPropertyCount() - 1) {
				outList();
			}
		}
		depth--;
		outObjectClose();
	}

	private void outAny(JsonType obj) throws IOException {
		int type = obj.getType();
		switch (type) {
		case JsonType.JSON_OBJECT:
			outObject(obj.cast(JsonObject.class));
			break;
		case JsonType.JSON_ARRAY:
			outArray(obj.cast(JsonArray.class));
			break;
		case JsonType.JSON_STRING:
			outString(obj.toString());
			break;
		case JsonType.JSON_NUMBER:
			outPrimitive(obj);
			break;
		case JsonType.JSON_BOOLEAN:
			outPrimitive(obj);
			break;
		case JsonType.JSON_NULL:
			outPrimitive(obj);
			break;
		}
	}

	public Writer getOut() {
		return out;
	}

	public void setFormat(JsonFormat format) {
		this.format = format;
	}

	public JsonFormat getFormat() {
		return format;
	}
}
