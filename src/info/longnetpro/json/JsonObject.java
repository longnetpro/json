package info.longnetpro.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonObject extends JsonType {
	private static final long serialVersionUID = 1L;

	private final Map<String, JsonType> properties = new HashMap<String, JsonType>();
	private final List<String> keyList = new ArrayList<String>();

	public JsonObject() {
		super();
	}

	public static JsonObject create() {
		return new JsonObject();
	}

	public int getType() {
		return JsonType.JSON_OBJECT;
	}

	public String toJsonString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			JsonString js = JsonString.create(key);
			sb.append(js.toJsonString());
			sb.append(':');
			JsonType obj = properties.get(key);
			sb.append(obj.toJsonString());
			if (i != keyList.size() - 1) {
				sb.append(',');
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public JsonObject setProperty(String key, JsonType value) {
		if (!keyList.contains(key))
			keyList.add(key);
		properties.put(key, value);
		return this;
	}

	public JsonObject setProperty(String key, String value) {
		setProperty(key, JsonString.create(value));
		return this;
	}

	public JsonObject setProperty(String key, Number number) {
		setProperty(key, JsonNumber.create(number));
		return this;
	}

	public JsonObject setProperty(String key, Boolean value) {
		JsonBoolean jb = JsonBoolean.forBoolean(value);
		setProperty(key, jb);
		return this;
	}

	public JsonObject setNullProperty(String key) {
		setProperty(key, JsonNull.NULL);
		return this;
	}

	public JsonObject setArrayProperty(String key) {
		setProperty(key, JsonArray.create());
		return this;
	}

	public JsonObject setObjectProperty(String key) {
		setProperty(key, JsonObject.create());
		return this;
	}

	public JsonType getProperty(String key) {
		JsonType obj = properties.get(key);
		if (obj == null) {
			obj = JsonNull.NULL;
		}
		return obj;
	}

	public String getPropertyAsString(String key) {
		JsonType obj = getProperty(key);
		if (obj instanceof JsonString) {
			return obj.cast(JsonString.class).getValue();
		} else if (obj instanceof JsonNull) {
			return null;
		} else {
			return obj.toJsonString();
		}
	}

	public Number getPropertyAsNumber(String key) {
		JsonType obj = getProperty(key);
		if (obj instanceof JsonNumber) {
			return obj.cast(JsonNumber.class).getValue();
		} else if (obj instanceof JsonBoolean) {
			return obj.cast(JsonBoolean.class).booleanValue() ? 1 : 0;
		} else {
			return 0;
		}
	}

	public Boolean getPropertyAsBoolean(String key, Boolean defaultValue) {
		JsonType obj = getProperty(key);
		if (obj instanceof JsonBoolean) {
			return obj.cast(JsonBoolean.class).getValue();
		} else if (obj instanceof JsonNumber) {
			JsonNumber n = (JsonNumber) obj;
			return (n.isInteger() && n.intValue() == 0) ? Boolean.FALSE : Boolean.TRUE;
		} else {
			return defaultValue;
		}
	}

	public Boolean getPropertyAsBoolean(String key) {
		return getPropertyAsBoolean(key, Boolean.FALSE);
	}

	public int getPropertyCount() {
		return keyList.size();
	}

	public boolean isEmpty() {
		return keyList.size() == 0;
	}

	public String getKey(int index) {
		return keyList.get(index);
	}

	public JsonObject clear() {
		properties.clear();
		keyList.clear();
		return this;
	}

	public JsonObject removeProperty(String key) {
		properties.remove(key);
		List<String> list = new ArrayList<String>();
		list.add(key);
		keyList.removeAll(list);
		return this;
	}

	public Map<String, JsonType> getProperties() {
		return properties;
	}

	public Map<String, JsonType> getValue() {
		return properties;
	}
}
