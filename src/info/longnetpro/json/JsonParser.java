package info.longnetpro.json;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import info.longnetpro.json.util.StringUtils;

public class JsonParser {
	private static final byte NONE = 0;
	// private static final byte ANY_VALUE = 7;
	private static final byte OBJECT = 2;
	private static final byte ARRAY = 3;
	private static final byte STRING = 4;
	private static final byte NUMBER = 5;
	private static final byte CONST = 6;
	private static final byte PAIR = 8;
	private static final byte LIST = 9;
	private static final byte CLOSE_OBJECT = 17;
	private static final byte CLOSE_ARRAY = 18;
	private static final byte END_OF_INPUT = 19;

	private static final String INVALID_CHARACTER_ERROR = "Invalid character.";
	private static final String INVALID_ESCAPE_ERROR = "Invalid escape.";
	private static final String INVALID_CONST_ERROR = "Invalid const value.";
	private static final String STRING_NOT_ENCLOSED_ERROR = "String is not enclosed.";
	private static final String INVALID_UNICODE = "Invalid unicode.";
	private static final String INVALID_NUMBER = "Invalid number format.";
	private static final String EXPECT_ERROR_MESSAGE = "%s is expected, but '%s' is found.";
	private static final String INVALID_VALUE_TYPE = "Invalid value type.";

	private JsonReader jsonReader = null;
	private JsonType parsedJsonValue = null; // parsed Json value
	private boolean parseStructureOnly = true;

	public boolean isParseStructureOnly() {
		return parseStructureOnly;
	}

	public void setParseStructureOnly(boolean parseStructureOnly) {
		this.parseStructureOnly = parseStructureOnly;
	}

	private int columnIndex = 0;
	private int lineIndex = 1;
	private boolean lookBack = false;

	public JsonParser() {
		super();
	}

	public JsonType getParsedJsonValue() {
		return parsedJsonValue;
	}

	protected static String translateTokenType(byte type) {
		switch (type) {
		case NONE:
			return "NONE";
		case OBJECT:
			return "OBJECT";
		case ARRAY:
			return "ARRAY";
		case STRING:
			return "STRING";
		case NUMBER:
			return "NUMBER";
		case CONST:
			return "CONST";
		case PAIR:
			return "PAIR";
		case LIST:
			return "LIST";
		case CLOSE_OBJECT:
			return "CLOSE_OBJECT";
		case CLOSE_ARRAY:
			return "CLOSE_ARRAY";
		case END_OF_INPUT:
			return "END_OF_FILE";
		default:
			return "N/A";
		}
	}

	public void parse(Reader reader) throws JsonParserException {
		if (reader == null) {
			throw new JsonParserException("No Json reader specified.");
		}
		setReader(reader);
		jsonReader.resetReader();
		byte tokenType = getNextTokenType();
		if (parseStructureOnly) {
			expectTokenType(tokenType, OBJECT, ARRAY);
		} else {
			expectTokenType(tokenType, OBJECT, ARRAY, STRING, NUMBER, CONST);
		}
		parsedJsonValue = parseAnyValue(tokenType);
		tokenType = getNextTokenType();
		expectTokenType(tokenType, END_OF_INPUT);
	}

	public void closeReader() {
		jsonReader.closeReader();
	}

	private void back() {
		lookBack = true;
	}

	private void noBack() {
		lookBack = false;
	}

	private JsonType parseAnyValue(byte tokenType) throws JsonParserException {
		JsonType obj = null;
		switch (tokenType) {
		case OBJECT:
			obj = parseObject();
			break;
		case ARRAY:
			obj = parseArray();
			break;
		case STRING:
			obj = parseString();
			break;
		case NUMBER:
			obj = parseNumber();
			break;
		case CONST:
			obj = parseConst();
			break;
		default:
			throw new JsonParserException(INVALID_VALUE_TYPE, lineIndex, columnIndex);
		}
		return obj;
	}

	private void processNewLine() {

		/*
		 * final String format = "Process New Line. Line: %d, Column: %d";
		 * System.out.println(String.format(format, lineIndex, columnIndex));
		 * System.out.println();
		 */

		char ch = jsonReader.getCurrentChar();
		char c = 0;
		if (ch == '\r') {
			c = jsonReader.readChar();
			if (jsonReader.isEndOfFile() || c != '\n') {
				lineIndex++;
				columnIndex = 1;
				return;
			}
		}

		if (ch == '\n' || c == '\n') {
			jsonReader.readChar();
			lineIndex++;
			columnIndex = 1;
		}
	}

	private JsonType parseConst() throws JsonParserException {
		StringBuilder sb = new StringBuilder("");
		while (!jsonReader.isEndOfFile()) {
			char ch = jsonReader.getCurrentChar();
			if (isTokenDelimiter(ch))
				break;
			sb.append(ch);
			jsonReader.readChar();
		}
		String token = sb.toString();
		JsonType obj = null;
		if (token.equals("true") || token.equals("false")) {
			obj = JsonBoolean.forName(token);
			columnIndex += token.length();
			back();
		} else if (token.equals("null")) {
			obj = JsonNull.NULL;
			columnIndex += token.length();
			back();
		} else
			throw new JsonParserException(INVALID_CONST_ERROR, lineIndex, columnIndex);
		return obj;
	}

	private JsonObject parseObject() throws JsonParserException {
		JsonObject object = new JsonObject();
		byte tokenType = getNextTokenType();
		expectTokenType(tokenType, STRING, CLOSE_OBJECT);
		while (tokenType != CLOSE_OBJECT) {
			JsonString jstring = parseString();
			String key = jstring.toString();
			tokenType = getNextTokenType();
			expectTokenType(tokenType, PAIR);
			tokenType = getNextTokenType();
			expectTokenType(tokenType, OBJECT, ARRAY, STRING, NUMBER, CONST);
			JsonType obj = parseAnyValue(tokenType);
			tokenType = getNextTokenType();
			expectTokenType(tokenType, LIST, CLOSE_OBJECT);
			if (tokenType == LIST) {
				object.setProperty(key, obj);
				tokenType = getNextTokenType();
				expectTokenType(tokenType, STRING);
			} else {
				object.setProperty(key, obj);
			}
		}
		return object;
	}

	private JsonArray parseArray() throws JsonParserException {
		JsonArray array = new JsonArray();
		byte tokenType = getNextTokenType();
		expectTokenType(tokenType, OBJECT, ARRAY, STRING, NUMBER, CONST, CLOSE_ARRAY);
		while (tokenType != CLOSE_ARRAY) {
			JsonType obj = parseAnyValue(tokenType);
			tokenType = getNextTokenType();
			expectTokenType(tokenType, LIST, CLOSE_ARRAY);
			if (tokenType == LIST) {
				array.add(obj);
				tokenType = getNextTokenType();
				expectTokenType(tokenType, OBJECT, ARRAY, STRING, NUMBER, CONST);
			} else {
				array.add(obj);
			}
		}
		return array;
	}

	private JsonString parseString() throws JsonParserException {
		jsonReader.readChar();
		StringBuilder sb = new StringBuilder("");
		while (!jsonReader.isEndOfFile()) {
			columnIndex++;
			char ch = jsonReader.getCurrentChar();
			if (ch == '"') {
				return JsonString.create(sb.toString());
			}
			String message = null;
			if (isDisallowedInString(ch)) {
				message = INVALID_CHARACTER_ERROR;
				if (ch == '\r' || ch == '\n') {
					message = STRING_NOT_ENCLOSED_ERROR;
				}
				throw new JsonParserException(message, lineIndex, columnIndex);
			}

			if (ch == '\\') {
				jsonReader.readChar();
				columnIndex++;
				ch = jsonReader.getCurrentChar();
				switch (ch) {
				case '"':
					ch = '"';
					break;
				case '\\':
					ch = '\\';
					break;
				case '/':
					ch = '/';
					break;
				case 'b':
					ch = '\b';
					break;
				case 'f':
					ch = '\f';
					break;
				case 'n':
					ch = '\n';
					break;
				case 'r':
					ch = '\r';
					break;
				case 't':
					ch = '\t';
					break;
				case 'u':
					columnIndex++;
					ch = parseUnicodeChar();
					columnIndex += 3;
					break;
				default:
					throw new JsonParserException(INVALID_ESCAPE_ERROR, lineIndex, columnIndex);
				}
			}
			sb.append(ch);
			jsonReader.readChar();
		}

		if (jsonReader.isEndOfFile()) {
			columnIndex++;
			throw new JsonParserException(STRING_NOT_ENCLOSED_ERROR, lineIndex, columnIndex);
		}

		return null;
	}

	private char parseUnicodeChar() throws JsonParserException {
		jsonReader.clearBuffer();
		String result = jsonReader.preReadChars(4);
		if (result.length() != 4)
			throw new JsonParserException(INVALID_UNICODE, lineIndex, columnIndex);
		try {
			int i = Integer.parseInt(result, 16);
			return (char) i;
		} catch (NumberFormatException e) {
			throw new JsonParserException(INVALID_UNICODE, lineIndex, columnIndex);
		}
	}

	private JsonNumber parseNumber() throws JsonParserException {
		StringBuilder sb = new StringBuilder("");
		while (!jsonReader.isEndOfFile()) {
			char ch = jsonReader.getCurrentChar();
			if (isTokenDelimiter(ch))
				break;
			sb.append(ch);
			jsonReader.readChar();
		}
		String token = sb.toString();

		boolean isNumber = true;

		try {
			final String regex = "^[-]?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?$";
			isNumber = token.matches(regex);
		} catch (Exception e) {
			isNumber = false;
		}

		JsonNumber obj = null;

		if (isNumber) {
			obj = JsonNumber.create(token);
			columnIndex += token.length();
			back();
		} else
			throw new JsonParserException(INVALID_NUMBER, lineIndex, columnIndex);
		return obj;
	}

	private byte getTokenType(char ch) throws JsonParserException {
		// System.out.println(String.format("Line: %d, Column: %d", lineIndex,
		// columnIndex));
		if (ch == 0 && jsonReader.isEndOfFile()) {
			return END_OF_INPUT;
		}
		byte tokenType = NONE;
		switch (ch) {
		case '{':
			tokenType = OBJECT;
			break;
		case '[':
			tokenType = ARRAY;
			break;
		case '"':
			tokenType = STRING;
			break;
		case 't':
		case 'f':
		case 'n':
			tokenType = CONST;
			break;
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			tokenType = NUMBER;
			break;
		case ':':
			tokenType = PAIR;
			break;
		case ',':
			tokenType = LIST;
			break;
		case '}':
			tokenType = CLOSE_OBJECT;
			break;
		case ']':
			tokenType = CLOSE_ARRAY;
			break;
		default:
			throw new JsonParserException(INVALID_CHARACTER_ERROR, lineIndex, columnIndex);
		}
		return tokenType;
	}

	private void consumeWhiteSpaces(boolean readFirst) throws JsonParserException {
		// return next available char
		char ch = jsonReader.getCurrentChar();
		if (readFirst) {
			ch = jsonReader.readChar();
			columnIndex++;
		}
		while (!jsonReader.isEndOfFile() && isWhiteSpace(ch)) {
			if (ch == '\n' || ch == '\r') {
				processNewLine();
				ch = jsonReader.getCurrentChar();
				continue;
			}
			columnIndex++;
			ch = jsonReader.readChar();
		}

		if (!(jsonReader.isEndOfFile() && ch == 0) && !isAllowedTokenStart(ch)) {
			throw new JsonParserException(INVALID_CHARACTER_ERROR, lineIndex, columnIndex);
		}
	}

	private void consumeWhiteSpaces() throws JsonParserException {
		consumeWhiteSpaces(true);
	}

	private byte getNextTokenType() throws JsonParserException {
		if (lookBack) {
			noBack();
			char ch = jsonReader.getCurrentChar();
			if (isWhiteSpace(ch)) {
				if (ch == '\r' || ch == '\n') {
					processNewLine();
					consumeWhiteSpaces(false);
				} else {
					consumeWhiteSpaces();
				}
			}
		} else {
			consumeWhiteSpaces();
		}
		char ch = jsonReader.getCurrentChar();
		byte tokenType = getTokenType(ch);
		return tokenType;
	}

	public static boolean isAllowedTokenStart(char ch) {
		return isStructuralTokenStart(ch) || "tfn-0123456789\"".indexOf(ch) >= 0;
	}

	public static boolean isDisallowedInString(char ch) {
		return isControlCharacter(ch) || isWhiteSpace(ch) && ch != ' ';
	}

	public static boolean isSpecialCharacter(char ch) {
		return "\"\\/\b\f\n\r\t".indexOf(ch) >= 0;
	}

	public static boolean isWhiteSpace(char ch) {
		return ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ';
	}

	public static boolean isStructuralTokenStart(char ch) {
		return "{[}]:,".indexOf(ch) >= 0;
	}

	public static boolean isTokenDelimiter(char ch) {
		return isWhiteSpace(ch) || isStructuralTokenStart(ch);
	}

	public static boolean isControlCharacter(char ch) {
		return ch >= 0 && ch <= 31;
	}

	private void expectTokenType(byte nextTokenType, byte... expectedTokenTypes) throws JsonParserException {
		boolean isExpected = false;
		for (int i = 0; i < expectedTokenTypes.length; i++) {
			isExpected = isExpected || nextTokenType == expectedTokenTypes[i];
		}

		final Map<Byte, String> map = new HashMap<Byte, String>();
		map.put(OBJECT, "left brace ({)");
		map.put(ARRAY, "left bracket ([)");
		map.put(STRING, "quote (\")");
		map.put(NUMBER, "a number");
		map.put(CONST, "true or false or null");
		map.put(PAIR, "colon (:)");
		map.put(LIST, "comma (,)");
		map.put(CLOSE_OBJECT, "right brace (})");
		map.put(CLOSE_ARRAY, "right bracket (])");
		map.put(END_OF_INPUT, "end of input");

		if (!isExpected) {
			String[] msgs = new String[expectedTokenTypes.length];
			for (int i = 0; i < expectedTokenTypes.length; i++) {
				msgs[i] = map.get(expectedTokenTypes[i]);
			}
			String str = StringUtils.join(msgs, " or ");
			String errorMsg = "";
			if (nextTokenType != END_OF_INPUT) {
				char ch = jsonReader.getCurrentChar();
				errorMsg = String.format(EXPECT_ERROR_MESSAGE, str, ch);
			} else {
				String s = "end of input";
				errorMsg = String.format(EXPECT_ERROR_MESSAGE, str, s);
			}
			throw new JsonParserException(errorMsg, lineIndex, columnIndex);
		}
	}

	public void setReader(Reader reader) {
		this.jsonReader = new JsonReader(reader);
	}
}
