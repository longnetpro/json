package info.longnetpro.json;

public class JsonPointerException extends Exception {
    private static final long serialVersionUID = 1L;

    public JsonPointerException(Throwable throwable) {
        super(throwable);
    }

    public JsonPointerException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public JsonPointerException(String string) {
        super(string);
    }

    public JsonPointerException() {
        super();
    }
}
