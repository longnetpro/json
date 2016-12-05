package info.longnetpro.json;

import java.util.ArrayDeque;
import java.util.Deque;

public class JsonBuilder {
	private static final String CANNOT_RECREATE_ERROR = "Cannot re-create the JSON object or array. Reset the builder and then create it.";

	private JsonType object = null;
	private JsonType current = null;
	private JsonType holder = null;
	private final Deque<JsonType> holderStack = new ArrayDeque<JsonType>();

	public JsonBuilder() {
		super();
	}

	public static JsonBuilder createBuilder() {
		return new JsonBuilder();
	}

	private void checkObjectExistence() throws JsonBuilderException {
		if (object != null) {
			throw new JsonBuilderException(CANNOT_RECREATE_ERROR);
		}
	}

	public JsonBuilder createObject() throws JsonBuilderException {
		checkObjectExistence();
		object = JsonObject.create();
		current = object;
		holder = null;
		return this;
	}

	public JsonBuilder createArray() throws JsonBuilderException {
		checkObjectExistence();
		object = JsonArray.create();
		current = object;
		holder = null;
		return this;
	}

	public JsonType build() {
		return object;
	}

	public JsonBuilder reset() {
		object = null;
		current = null;
		holder = null;
		holderStack.clear();
		return this;
	}

	public JsonBuilder beginObject() throws JsonBuilderException {
		if (current == null || current.getType() != JsonType.JSON_OBJECT) {
			throw new JsonBuilderException("The current value is not an Object.");
		}
		holder = current;
		current = null;
		holderStack.push(holder);
		return this;
	}

	public JsonBuilder beginArray() throws JsonBuilderException {
		if (current == null || current.getType() != JsonType.JSON_ARRAY) {
			throw new JsonBuilderException("The current value is not an Array.");
		}
		holder = current;
		current = null;
		holderStack.push(holder);
		return this;
	}

	public JsonBuilder endObject() throws JsonBuilderException {
		JsonType obj = holderStack.peek();
		if (holder != obj) {
			throw new JsonBuilderException("Unmatched end object.");
		}
		if (obj == null || obj.getType() != JsonType.JSON_OBJECT) {
			throw new JsonBuilderException("Cannot construct proper object value.");
		}
		holderStack.pop();
		holder = holderStack.peek();
		current = obj;
		return this;
	}

	public JsonBuilder endArray() throws JsonBuilderException {
		JsonType obj = holderStack.peek();
		if (holder != obj) {
			throw new JsonBuilderException("Unmatched end array.");
		}
		if (obj == null || obj.getType() != JsonType.JSON_ARRAY) {
			throw new JsonBuilderException("Cannot construct proper array value.");
		}
		holderStack.pop();
		holder = holderStack.peek();
		current = obj;
		return this;
	}

	private JsonObject checkObjectHolder(String key) throws JsonBuilderException {
		if (holder == null || holder.getType() != JsonType.JSON_OBJECT) {
			throw new JsonBuilderException("Expect an Object holder.");
		} else if (key == null) {
			throw new JsonBuilderException("The holder is an Object and a key is required.");
		}
		return holder.cast(JsonObject.class);
	}

	public JsonBuilder setProperty(String key, JsonType obj) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		holder.setProperty(key, obj);
		if (obj == null || obj.getType() != JsonType.JSON_OBJECT && obj.getType() != JsonType.JSON_ARRAY) {
			current = null;
		} else {
			current = obj;
		}
		return this;
	}

	public JsonBuilder setProperty(String key, String obj) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		holder.setProperty(key, obj);
		current = null;
		return this;
	}

	public JsonBuilder setProperty(String key, Number obj) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		holder.setProperty(key, obj);
		current = null;
		return this;
	}

	public JsonBuilder setProperty(String key, Boolean obj) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		holder.setProperty(key, obj);
		current = null;
		return this;
	}

	public JsonBuilder setNullProperty(String key) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		holder.setNullProperty(key);
		current = null;
		return this;
	}

	public JsonBuilder setObjectProperty(String key) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		current = JsonObject.create();
		holder.setProperty(key, current);
		return this;
	}

	public JsonBuilder setArrayProperty(String key) throws JsonBuilderException {
		JsonObject holder = checkObjectHolder(key);
		current = JsonArray.create();
		holder.setProperty(key, current);
		return this;
	}

	private JsonArray checkArrayHolder() throws JsonBuilderException {
		if (holder == null || holder.getType() != JsonType.JSON_ARRAY) {
			throw new JsonBuilderException("Expect an Array holder.");
		}
		return holder.cast(JsonArray.class);
	}

	public JsonBuilder add(JsonType obj) throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		holder.add(obj);
		if (obj == null || obj.getType() != JsonType.JSON_OBJECT && obj.getType() != JsonType.JSON_ARRAY) {
			current = null;
		} else {
			current = obj;
		}
		return this;
	}

	public JsonBuilder add(String obj) throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		holder.add(obj);
		current = null;
		return this;
	}

	public JsonBuilder add(Number obj) throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		holder.add(obj);
		current = null;
		return this;
	}

	public JsonBuilder add(Boolean obj) throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		holder.add(obj);
		current = null;
		return this;
	}

	public JsonBuilder addNull() throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		holder.addNull();
		current = null;
		return this;
	}

	public JsonBuilder addArray() throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		current = JsonArray.create();
		holder.add(current);
		return this;
	}

	public JsonBuilder addObject() throws JsonBuilderException {
		JsonArray holder = checkArrayHolder();
		current = JsonObject.create();
		holder.add(current);
		return this;
	}
}
