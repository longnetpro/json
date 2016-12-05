package info.longnetpro.json;

import java.io.Serializable;

abstract public class JsonType implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final byte JSON_OTHER = 0;
	public static final byte JSON_NULL = 1;
	public static final byte JSON_BOOLEAN = 2;
	public static final byte JSON_STRING = 3;
	public static final byte JSON_NUMBER = 4;
	public static final byte JSON_OBJECT = 5;
	public static final byte JSON_ARRAY = 6;

	public JsonType() {
		super();
	}

	abstract public int getType();

	abstract public String toJsonString();

	abstract public Object getValue();

	@SuppressWarnings("unchecked")
	public <T> T cast(T obj) {
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public <T> T cast(Class<T> clazz) {
		return (T) this;
	}
}
