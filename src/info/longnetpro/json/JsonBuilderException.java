package info.longnetpro.json;

public class JsonBuilderException extends Exception {
    private static final long serialVersionUID = 1L;

    public JsonBuilderException(Throwable throwable) {
        super(throwable);
    }

    public JsonBuilderException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public JsonBuilderException(String string) {
        super(string);
    }

    public JsonBuilderException() {
        super();
    }
}
