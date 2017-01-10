package info.longnetpro.json;

public class JsonString extends JsonType {
	private static final long serialVersionUID = 1L;

	private String value = "";

	public JsonString(String value) {
		super();
		this.value = (value == null) ? "" : value;
	}

	public static JsonString create(String value) {
		return new JsonString(value);
	}

	public int getType() {
		return JsonType.JSON_STRING;
	}

	public String toJsonString() {
		String s = JsonSerializer.toJsonString(value, false, false, true, false, false);
		return s;
	}

	public String toString() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return value.equals(obj);
		}

		if (obj instanceof JsonString) {
			return value.equals(obj.toString());
		}
		return super.equals(obj);
	}

	public String getValue() {
		return value;
	}
}
