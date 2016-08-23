package info.longnetpro.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Examples {
    public Examples() {
        super();
    }

    @SuppressWarnings("unused")
	private static void test() throws FileNotFoundException, JsonParserException, UnsupportedEncodingException, IOException {
        String filename = "C:\\temp\\test.json";
        filename = "C:\\JDeveloper000\\bentall\\configuration\\framework\\defaultMenu.json";
        File f = new File(filename);
        JsonParser parser = new JsonParser(f);
        parser.parse();

        JsonFormat format = new JsonFormat();
        //format.setIndenting(false);
        //format.setCompactFormat(true);
        //format.setHexSpecialChar(true);
        format.setHexNonAscii(true);
        format.setHexUpperCase(true);
        //format.setHexAllCharacters(true);
        //format.setEscapeSlash(true);
        format.setValueAlign(true);
        //format.setKeyRightAligned(true);

        Writer out = new StringWriter();
        parser.output(out, format);

        //String s = "{\"firstName\\t/\\t6\":false,\"middleName\":null,\"lastName\":\"Smith\",\"age\":25,\"address\":{\"streetAddress\":\"21 2nd Street\",\"city\":\"New York\",\"state\":\"NY\",\"postalCode\":10021.1e+2},\"phoneNumbers\":[{\"type\":\"home\",\"number\":\"212 555-1234\"},{\"type\":\"fax\",\"number\":\"646 555-4567\",\"test\":\"\u4f60\u4eecabc\u597d\"}]}";
        String s = "[true , null,{},1234567 ]";
        //String s = out.toString();

        parser = new JsonParser(s);
        parser.parse();
        out = new StringWriter();
        parser.output(out, format);
        System.out.println(parser.getParsedJsonValue().toJsonString());
    }

    @SuppressWarnings("unused")
	private static void test1() {
        for (int i = 0; i <= 32; i++) {
            boolean b = Character.isISOControl(i);
            System.out.println(i + ": " + b);
        }
    }

    @SuppressWarnings("unused")
	private static void test2() {
        int i = Integer.parseInt("ff", 16);
        System.out.println(i);
    }

    @SuppressWarnings("unused")
	private static void test3() throws IOException, JsonParserException {
        JsonObject obj = new JsonObject();
        obj.setArrayProperty("test").setProperty("olahelp",
                                                 "http://bentallkennedy.local/ola.jspx").getProperty("test").cast(JsonArray.class).add("haha").add(StringUtils.convertString("你好",
                                                                                                                                                                          "UTF-8"));


        JsonFormat format = new JsonFormat();
        Writer out = new StringWriter();
        JsonSerializer.serialize(obj, out, format);
        String s = out.toString();
        System.out.println(s);

        //format.setIndenting(false);
        //format.setCompactFormat(true);
        //format.setHexAllCharacters(true);
        format.setHexNonAscii(true);
        //format.setHexAscii(true);
        format.setHexUpperCase(true);
        //format.setHexSpecialChar(true);
        //format.setEscapeSlash(true);
        //format.setKeyRightAligned(true);
        //s = "true";
        JsonParser jp = new JsonParser(s);
        jp.parse();
        jp.print(format);
    }

    @SuppressWarnings("unused")
	private static void test4() throws JsonBuilderException, IOException, JsonPointerException {
        JsonBuilder jb = JsonBuilder.createBuilder();
        JsonType array =
            jb.createArray().beginArray().add("test").add(12345).add("nul\t\nl").addNull().addObject().beginObject().setProperty("key1", "haha").setProperty("key2",
                                                                                                                                                             false).setNullProperty("key3").setArrayProperty("array").beginArray().add("hello world!").addObject().beginObject().setProperty("why",
                                                                                                                                                                                                                                                                                             "end").endObject().add("address").endArray().setProperty("key4",
                                                                                                                                                                                                                                                                                                                                                      12345).endObject().endArray().build();
        JsonSerializer.print(array);

        JsonType object =
        //jb.reset().createObject().beginObject().setProperty("haha", "test").setProperty("key", 12345).setArrayProperty("array").setArrayProperty("arr/a\\y1").beginArray().add(123).add("array in object").endArray().endObject().build();
        jb.reset().createObject().beginObject().setArrayProperty("array").endObject().build();
        JsonSerializer.print(object);

        System.out.println("--------------------------");

        String r = JsonString.create("arr/a\\y1").toJsonPointerReference();

        String pointer = "/" + r + "/1";
        JsonType obj = JsonPointer.create().setReferencedObject(object).setPointer(pointer).resolve();
        JsonSerializer.print(obj);
    }

    public static void main(String[] args) throws FileNotFoundException, JsonParserException, UnsupportedEncodingException, IOException, JsonBuilderException,
                                                  JsonPointerException {
        test3();
        String s = "\uD834\uDD1E";
        int i = s.codePointAt(0);
        System.out.println(s.length());
        System.out.println((char)i);
        i = s.charAt(1);
        System.out.println(s.length());
        System.out.println((char)i);
        System.out.println("Test1");
    }
}
