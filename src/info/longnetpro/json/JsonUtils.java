package info.longnetpro.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class JsonUtils {
	private static final String DEFAULT_FILE_ENCODING = "UTF-8";

	public static JsonType parse(Reader reader, boolean parseStructureOnly) throws JsonParserException {
		JsonParser parser = new JsonParser();
		parser.setParseStructureOnly(parseStructureOnly);
		parser.parse(reader);
		JsonType value = parser.getParsedJsonValue();
		parser.closeReader();
		return value;
	}

	public static JsonType parse(Reader reader) throws JsonParserException {
		return parse(reader, true);
	}

	public static JsonType parseJsonString(String source, boolean parseStructureOnly) throws JsonParserException {
		Reader reader = new StringReader(source);
		return parse(reader, parseStructureOnly);
	}

	public static JsonType parseJsonString(String source) throws JsonParserException {
		return parseJsonString(source, true);
	}

	public static JsonType parseJsonFile(File file, String encoding, boolean parseStructureOnly)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
		return parse(reader, parseStructureOnly);
	}

	public static JsonType parseJsonFile(File file, String encoding)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		return parseJsonFile(file, encoding, true);
	}

	public static JsonType parseJsonFile(String filename, String encoding, boolean parseStructureOnly)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), encoding));
		return parse(reader, parseStructureOnly);
	}

	public static JsonType parseJsonFile(String filename, String encoding)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		return parseJsonFile(filename, encoding, true);
	}

	public static JsonType parseJsonFile(File file, boolean parseStructureOnly)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), DEFAULT_FILE_ENCODING));
		return parse(reader, parseStructureOnly);
	}

	public static JsonType parseJsonFile(File file)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		return parseJsonFile(file, true);
	}

	public static JsonType parseJsonFile(String filename, boolean parseStructureOnly)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), DEFAULT_FILE_ENCODING));
		return parse(reader, parseStructureOnly);
	}

	public static JsonType parseJsonFile(String filename)
			throws JsonParserException, UnsupportedEncodingException, FileNotFoundException {
		return parseJsonFile(filename, true);
	}

	public static void output(JsonType jsonObj, Writer out, JsonFormat format) throws IOException {
		JsonSerializer js = new JsonSerializer(format);
		js.serialize(jsonObj, out);
	}

	public static void output(JsonType jsonObj, Writer out) throws IOException {
		JsonSerializer js = new JsonSerializer();
		js.serialize(jsonObj, out);
	}

	public static String outputString(JsonType jsonObj, JsonFormat format) throws IOException {
		JsonSerializer js = new JsonSerializer(format);
		js.serialize(jsonObj);
		return js.getOut().toString();
	}

	public static String outputString(JsonType jsonObj) throws IOException {
		JsonSerializer js = new JsonSerializer();
		js.serialize(jsonObj);
		return js.getOut().toString();
	}

	public static void print(JsonType jsonObj, JsonFormat format) throws IOException {
		String outString = outputString(jsonObj, format);
		System.out.println(outString);
	}

	public static void print(JsonType jsonObj) throws IOException {
		String outString = outputString(jsonObj);
		System.out.println(outString);
	}

	public static void outputFile(JsonType jsonObj, File file, JsonFormat format) throws IOException {
		Writer out = new FileWriter(file);
		output(jsonObj, out, format);
	}

	public static void outputFile(JsonType jsonObj, File file) throws IOException {
		Writer out = new FileWriter(file);
		output(jsonObj, out);
	}

	public static void outputFile(JsonType jsonObj, String filename, JsonFormat format) throws IOException {
		Writer out = new FileWriter(filename);
		output(jsonObj, out, format);
	}

	public static void outputFile(JsonType jsonObj, String filename) throws IOException {
		Writer out = new FileWriter(filename);
		output(jsonObj, out);
	}

}
