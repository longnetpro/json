package info.longnetpro.json;

public class JsonFormat {
	private boolean indenting = true;
	private int indent = 4;
	private String encoding = "UTF-8";
	private boolean valueAlign = false;
	private boolean keyRightAligned = false;
	private String newLine = "\r\n";
	private boolean compactFormat = false;
	private boolean hexAllCharacters = false;
	private boolean hexSpecialChar = false;
	private boolean hexAscii = false;
	private boolean hexNonAscii = false;
	private boolean hexUpperCase = false;
	private boolean escapeSlash = false;

	public JsonFormat() {
		super();
	}

	public static JsonFormat create() {
		return new JsonFormat();
	}

	public JsonFormat setIndenting(boolean indenting) {
		this.indenting = indenting;
		return this;
	}

	public boolean isIndenting() {
		return indenting;
	}

	public JsonFormat setIndent(int indent) {
		this.indent = indent;
		return this;
	}

	public int getIndent() {
		return indent;
	}

	public JsonFormat setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public String getEncoding() {
		return encoding;
	}

	public JsonFormat setValueAlign(boolean valueAlign) {
		this.valueAlign = valueAlign;
		return this;
	}

	public boolean isValueAlign() {
		return valueAlign;
	}

	public JsonFormat setKeyRightAligned(boolean keyRightAligned) {
		this.keyRightAligned = keyRightAligned;
		return this;
	}

	public boolean isKeyRightAligned() {
		return keyRightAligned;
	}

	public JsonFormat setHexAllCharacters(boolean hexAllCharacters) {
		this.hexAllCharacters = hexAllCharacters;
		return this;
	}

	public boolean isHexAllCharacters() {
		return hexAllCharacters;
	}

	public JsonFormat setHexUpperCase(boolean hexUpperCase) {
		this.hexUpperCase = hexUpperCase;
		return this;
	}

	public boolean isHexUpperCase() {
		return hexUpperCase;
	}

	public JsonFormat setNewLine(String newLine) {
		this.newLine = newLine;
		return this;
	}

	public String getNewLine() {
		return newLine;
	}

	public JsonFormat setHexNonAscii(boolean hexNonAscii) {
		this.hexNonAscii = hexNonAscii;
		return this;
	}

	public boolean isHexNonAscii() {
		return hexNonAscii;
	}

	public JsonFormat setCompactFormat(boolean compactFormat) {
		this.compactFormat = compactFormat;
		return this;
	}

	public boolean isCompactFormat() {
		return compactFormat;
	}

	public JsonFormat setHexSpecialChar(boolean hexSpecialChar) {
		this.hexSpecialChar = hexSpecialChar;
		return this;
	}

	public boolean isHexSpecialChar() {
		return hexSpecialChar;
	}

	public JsonFormat setHexAscii(boolean hexAscii) {
		this.hexAscii = hexAscii;
		return this;
	}

	public boolean isHexAscii() {
		return hexAscii;
	}

	public JsonFormat setEscapeSlash(boolean escapeSlash) {
		this.escapeSlash = escapeSlash;
		return this;
	}

	public boolean isEscapeSlash() {
		return escapeSlash;
	}
}
