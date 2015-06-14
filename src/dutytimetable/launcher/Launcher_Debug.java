package dutytimetable.launcher;


import dutytimetable.view.MainViewLauncher;
import javafx.application.Application;
import myutils.debug.Debug;

/**
 * Created by beenotung on 6/8/15.
 */
public class Launcher_Debug {
    public static void main(String [] args){
        Debug.MODE=Debug.MODE_DEBUG;
        Application.launch(MainViewLauncher.class,args);
    }
}
