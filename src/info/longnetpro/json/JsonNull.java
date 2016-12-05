package info.longnetpro.json;

public class JsonNull extends JsonType {
	private static final long serialVersionUID = 1L;

	public static final JsonNull NULL = new JsonNull();

	private JsonNull() {
		super();
	}

	public int getType() {
		return JsonType.JSON_NULL;
	}

	public JsonNull getJsonNull() {
		return this;
	}

	public String toJsonString() {
		return "null";
	}

	public String toString() {
		return toJsonString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return true;
		if (obj instanceof JsonNull)
			return true;
		return super.equals(obj);
	}

	public Object getValue() {
		return null;
	}
}
