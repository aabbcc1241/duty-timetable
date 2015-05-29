package dutytimetable.debug;

/**
 * Created by hkpu on 15/5/2015.
 */
public class Debug {
    private static final String BASE = "Error Code: ";
    public static final String ERROR_CODE_FILE_READ = gen(1);
    public static final String ERROR_MESSAGE_FILE_READ = "Failed to read from the file, please retry, or refer to the user manual " + ERROR_CODE_FILE_READ;
    public static final String ERROR_CODE_FILE_WRITE = gen(2);
    public static final String ERROR_MESSAGE_FILE_WRITE = "Failed to save to the file, please retry, or refer to the user manual " + ERROR_CODE_FILE_READ;

    public static String gen(int code) {
        String CODE = String.valueOf(code);
        if (CODE.length() < 2)
            CODE = "0" + CODE;
        return "(" + BASE + CODE + ")";
    }

    public static String MODE_DEBUG = "Debug Mode";
    public static String MODE_RELEASE = "Release Mode";
    public static String MODE = MODE_DEBUG;

    public static void println(String msg) {
        if (MODE == MODE_DEBUG)
            System.out.println(msg);
    }
}
