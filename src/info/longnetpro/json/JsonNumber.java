package info.longnetpro.json;

public class JsonNumber extends JsonType {
    private static final long serialVersionUID = 1L;

    private Number number = new Integer(0);
    private boolean isInteger = true;
    private String token = "";

    public JsonNumber(String token) {
        super();
        this.token = token;
        parseValue();
    }

    public JsonNumber(Number number) {
        super();
        this.number = number;
        this.token = number.toString();
    }

    private void parseValue() {
        try {
            if (token.indexOf('.') > 0 || token.indexOf('e') > 0 || token.indexOf('E') > 0) {
                isInteger = false;
                number = Double.parseDouble(token);
            } else {
                isInteger = true;
                number = Integer.parseInt(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean isInteger() {
        return this.isInteger;
    }

    public static JsonNumber create(String token) {
        return new JsonNumber(token);
    }

    public static JsonNumber create(Number number) {
        return new JsonNumber(number);
    }

    public int getType() {
        return JsonType.JSON_NUMBER;
    }

    public String toJsonString() {
        return this.token;
    }

    public String toString() {
        return number.toString();
    }

    public Number getNumber() {
        return number;
    }

    public int intValue() {
        return number.intValue();
    }

    public double doubleValue() {
        return number.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Number) {
            return number.equals(obj);
        }

        if (obj instanceof JsonNumber) {
            JsonNumber jn = (JsonNumber)obj;
            return number.equals(jn.getNumber());
        }

        return super.equals(obj);
    }

    public Number getValue() {
        return number;
    }
}
