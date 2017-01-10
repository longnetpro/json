package info.longnetpro.json.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import info.longnetpro.json.JsonFormat;
import info.longnetpro.json.JsonParserException;
import info.longnetpro.json.JsonType;
import info.longnetpro.json.JsonUtils;

public class Examples {

	public Examples() {
	}

	private static void test()
			throws FileNotFoundException, JsonParserException, UnsupportedEncodingException, IOException {
		File input = new File("C:\\temp\\test.json");
		File output = new File("C:\\temp\\testResult.json"); 

		JsonFormat format = new JsonFormat();
		//format.setCompactFormat(true);
		//format.setKeyRightAligned(true);
		format.setValueAlign(true);
		//format.setHexAscii(true);
		//format.setHexNonAscii(true);
		//format.setHexUpperCase(true);
		//format.setHexAllCharacters(true);
		
		
		JsonType value = null;

		value = JsonUtils.parseJsonFile(input);

		JsonUtils.print(value);
		JsonUtils.outputFile(value, output, format);

		String s = "0.123  ";
		value = JsonUtils.parseJsonString(s, false);
		JsonUtils.print(value, format);
	}

	private static void test1() {
		for (int i = 0; i <= 32; i++) {
			boolean b = Character.isISOControl(i);
			System.out.println(i + ": " + b);
		}
	}

	private static void test2() {
		int i = Integer.parseInt("xxxx", 16);
		System.out.println(i);
	}

	public static void main(String[] args)
			throws FileNotFoundException, JsonParserException, UnsupportedEncodingException, IOException {
		test();
	}
}
