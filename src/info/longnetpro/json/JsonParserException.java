package info.longnetpro.json;

public class JsonParserException extends Exception {
	private static final long serialVersionUID = 4973542894512694433L;

	private int line = 1;
	private int column = 1;

	public JsonParserException(String message, int line, int column) {
		super(message);
		this.line = line;
		this.column = column;
	}

	public JsonParserException(int line, int column) {
		super(JsonParserException.class.toString());
		this.line = line;
		this.column = column;
	}

	public JsonParserException(Throwable throwable) {
		super(throwable);
	}

	public JsonParserException(String string, Throwable throwable) {
		super(string, throwable);
	}

	public JsonParserException(String string) {
		super(string);
	}

	public JsonParserException() {
		super();
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		final String format = "Error occured at Line %d, Column %d. Message: %s";
		String s = String.format(format, line, column, message);
		return s;
	}
}
