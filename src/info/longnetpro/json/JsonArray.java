package info.longnetpro.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray extends JsonType {
	private static final long serialVersionUID = 1L;

	private final List<JsonType> array = new ArrayList<JsonType>();

	public JsonArray() {
		super();
	}

	public static JsonArray create() {
		return new JsonArray();
	}

	public int getType() {
		return JsonType.JSON_ARRAY;
	}

	public String toJsonString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.size(); i++) {
			JsonType obj = array.get(i);
			sb.append(obj.toJsonString());
			if (i != array.size() - 1) {
				sb.append(',');
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public JsonType get(int index) {
		JsonType obj = array.get(index);
		if (obj == null) {
			obj = JsonNull.NULL;
		}
		return obj;
	}

	public String getAsString(int index) {
		JsonType obj = get(index);
		if (obj instanceof JsonString) {
			return obj.cast(JsonString.class).getValue();
		} else if (obj instanceof JsonNull) {
			return null;
		} else {
			return obj.toJsonString();
		}
	}

	public Number getAsNumber(int index) {
		JsonType obj = get(index);
		if (obj instanceof JsonNumber) {
			return obj.cast(JsonNumber.class).getValue();
		} else if (obj instanceof JsonBoolean) {
			return obj.cast(JsonBoolean.class).booleanValue() ? 1 : 0;
		} else {
			return 0;
		}
	}

	public Boolean getAsBoolean(int index, Boolean defaultValue) {
		JsonType obj = get(index);
		if (obj instanceof JsonBoolean) {
			return obj.cast(JsonBoolean.class).getValue();
		} else if (obj instanceof JsonNumber) {
			JsonNumber n = (JsonNumber) obj;
			return (n.isInteger() && n.intValue() == 0) ? Boolean.FALSE : Boolean.TRUE;
		} else {
			return defaultValue;
		}
	}

	public Boolean getAsBoolean(int index) {
		return getAsBoolean(index, Boolean.FALSE);
	}

	public JsonArray add(JsonType element) {
		array.add(element);
		return this;
	}

	public int getArraySize() {
		return array.size();
	}

	public boolean isEmpty() {
		return array.size() == 0;
	}

	public JsonArray clear() {
		array.clear();
		return this;
	}

	public JsonArray remove(int index) {
		array.remove(index);
		return this;
	}

	public JsonArray removeAll(JsonType obj) {
		List<JsonType> list = new ArrayList<JsonType>();
		list.add(obj);
		array.removeAll(list);
		return this;
	}

	public JsonArray remove(JsonType obj) {
		array.remove(obj);
		return this;
	}

	public JsonArray add(String string) {
		add(JsonString.create(string));
		return this;
	}

	public JsonArray add(Boolean value) {
		add(JsonBoolean.forBoolean(value));
		return this;
	}

	public JsonArray add(Number number) {
		add(JsonNumber.create(number));
		return this;
	}

	public JsonArray addNull() {
		add(JsonNull.NULL);
		return this;
	}

	public JsonArray addArray() {
		add(JsonArray.create());
		return this;
	}

	public JsonArray addObject() {
		add(JsonObject.create());
		return this;
	}

	public List<JsonType> getArray() {
		return array;
	}

	public List<JsonType> getValue() {
		return array;
	}
}
