package dutytimetable.deprecated.launcher;


import dutytimetable.deprecated.gui.CoreJFrame;
import myutils.debug.Debug;

/**
 * Created by beenotung on 5/28/15.
 */
public class Launcher_Debug {
    public static void main(String[] args) {
        Debug.MODE = Debug.MODE_DEBUG;
        CoreJFrame coreJFrame = new CoreJFrame() ;
    }
}
