package info.longnetpro.json;

import java.io.IOException;
import java.io.Reader;

public class JsonReader {
	private Reader reader = null;

	private boolean endOfFile = false;

	private int cursor = 0;
	private char currentChar = 0;
	private StringBuilder buffer = new StringBuilder("");

	public JsonReader(Reader reader) {
		super();
		this.reader = reader;
		try {
			reader.mark(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.endOfFile = false;
	}

	public void resetReader() {
		if (reader != null) {
			try {
				reader.reset();
				endOfFile = false;
				cursor = 0;
				currentChar = 0;
				clearBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected int read() {
		try {
			int i = reader.read();
			if (i == -1) {
				endOfFile = true;
			}
			if (!endOfFile || cursor != -1)
				cursor++;
			return i;
		} catch (IOException e) {
			e.printStackTrace();
			endOfFile = true;
			return -1;
		}
	}

	public char readChar() {
		int i = read();
		if (i == -1)
			currentChar = 0;
		else
			currentChar = (char) i;
		return currentChar;
	}

	public String preReadChars(int count) {
		for (int i = 0; i < count; i++) {
			readChar();
			if (!isEndOfFile()) {
				buffer.append(currentChar);
			} else {
				break;
			}
		}
		return this.buffer.toString();
	}

	public String preReadChar() {
		return preReadChars(1);
	}

	public void clearBuffer() {
		this.buffer = new StringBuilder("");
	}

	public boolean isEndOfFile() {
		return endOfFile;
	}

	public int getCursor() {
		return cursor;
	}

	public char getCurrentChar() {
		return currentChar;
	}

	public String getBufferedText() {
		return this.buffer.toString();
	}

	public void bufferChar(char ch) {
		buffer.append(ch);
	}

	public void closeReader() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				;
			}
		}
	}
}
