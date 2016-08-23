package info.longnetpro.json;

public class JsonBoolean extends JsonType {
    private static final long serialVersionUID = 1L;

    public static final JsonBoolean TRUE = new JsonBoolean(true);
    public static final JsonBoolean FALSE = new JsonBoolean(false);

    private boolean value = false;

    private JsonBoolean(boolean value) {
        super();
        this.value = value;
    }

    public static JsonBoolean forName(String token) {
        if (token.equals("true"))
            return TRUE;
        else
            return FALSE;
    }

    public static JsonBoolean forBoolean(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static JsonBoolean forBoolean(Boolean value) {
        return forBoolean(value.booleanValue());
    }

    public int getType() {
        return JsonType.JSON_BOOLEAN;
    }

    public String toJsonString() {
        return Boolean.toString(value);
    }

    public String toString() {
        return toJsonString();
    }

    public boolean booleanValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Boolean) {
            Boolean b = value;
            return b.equals(obj);
        }

        if (obj instanceof JsonBoolean) {
            return (obj != null) && obj.equals(value);
        }
        return super.equals(obj);
    }

    public Boolean getValue() {
        return value;
    }
}
