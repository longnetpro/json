package info.longnetpro.json;

import java.io.Serializable;

public class JsonPointer implements Serializable {
	private static final long serialVersionUID = 1L;

	private String pointer = null;
	private JsonType referencedObject = null;
	private boolean jsonForamt = true;

	public JsonPointer() {
		super();
	}

	public static JsonPointer create() {
		return new JsonPointer();
	}

	public static JsonType resolve(JsonType jsonObj, String reference, boolean isJsonFormat)
			throws JsonPointerException {
		if (jsonObj == null || jsonObj.getType() != JsonType.JSON_ARRAY && jsonObj.getType() != JsonType.JSON_OBJECT) {
			throw new JsonPointerException("Invalid JSON value.");
		}
		if (reference == null || reference.equals("")) {
			return jsonObj;
		}
		String[] keys = evaluteReference(reference, isJsonFormat);
		JsonType obj = jsonObj;
		for (String key : keys) {
			if (obj.getType() == JsonType.JSON_OBJECT) {
				obj = obj.cast(JsonObject.class).getProperty(key);
				if (obj == null)
					break;
			} else if (obj.getType() == JsonType.JSON_ARRAY) {
				obj = obj.cast(JsonArray.class).get(Integer.parseInt(key));
				if (obj == null)
					break;
			} else {
				String msg = String.format("Invalid reference: %s.", key);
				throw new JsonPointerException(msg);
			}
		}
		return obj;
	}

	public static JsonType resolve(JsonType jsonObj, String reference) throws JsonPointerException {
		return resolve(jsonObj, reference, true);
	}

	public static String[] evaluteReference(String reference, boolean isJsonFormat) throws JsonPointerException {
		if (reference == null || !reference.startsWith("/")) {
			String msg = String.format("Invalid reference: %s.", reference);
			throw new JsonPointerException(msg);
		}

		String ref = reference;
		if (isJsonFormat) {
			try {
				ref = JsonUtils.parseJsonString('"' + reference + '"', false).toString();
			} catch (JsonParserException e) {
				throw new JsonPointerException(e);
			}
		}

		String[] keys = ref.substring(1).split("\\/", -1);
		for (int i = 0; i < keys.length; i++) {
			String s = keys[i];
			s = s.replaceAll("~1", "/");
			s = s.replaceAll("~0", "~");
			keys[i] = s;
		}
		return keys;
	}

	public static String[] evaluteReference(String reference) throws JsonPointerException {
		return evaluteReference(reference, false);
	}
	
	public static String createReference(String value) {
		String ref = value;
		ref = ref.replaceAll("~", "~0");
		ref = ref.replaceAll("/", "~1");
		ref = JsonString.create(ref).toJsonString();
		ref = ref.substring(1, ref.length() - 1);
		return ref;
	}

	public JsonType resolve() throws JsonPointerException {
		return resolve(referencedObject, pointer, jsonForamt);
	}

	public JsonPointer setPointer(String pointer) {
		this.pointer = pointer;
		return this;
	}

	public String getPointer() {
		return pointer;
	}

	public JsonPointer setReferencedObject(JsonType referencedObject) {
		this.referencedObject = referencedObject;
		return this;
	}

	public JsonType getReferencedObject() {
		return referencedObject;
	}

	public JsonPointer setJsonForamt(boolean jsonForamt) {
		this.jsonForamt = jsonForamt;
		return this;
	}

	public boolean isJsonForamt() {
		return jsonForamt;
	}
}
