package dutytimetable.launcher;

import dutytimetable.debug.Debug;
import dutytimetable.gui.CoreJFrame;

/**
 * Created by beenotung on 5/28/15.
 */
public class Launcher_Debug {
    public static void main(String[] args) {
        Debug.MODE = Debug.MODE_DEBUG;
        CoreJFrame coreJFrame = new CoreJFrame() ;
    }
}
