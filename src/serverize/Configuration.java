package serverize;

public class Configuration {

    public static int loadInt(String name, int i) {
        String value = System.getenv(name);
        if (value == null) return i;
        return Integer.valueOf(value);
    }

    public static long loadLong(String name, long i) {
        String value = System.getenv(name);
        if (value == null) return i;
        return Long.valueOf(value);
    }

    public static String loadString(String name, String s) {
        String value = System.getenv(name);
        if (value == null) return s;
        return value;
    }
}
