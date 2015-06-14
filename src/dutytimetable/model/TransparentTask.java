package dutytimetable.model;

import javafx.concurrent.Task;

/**
 * Created by beenotung on 6/14/15.
 */
public abstract class TransparentTask<T> extends Task<T> {
    @Override
    public void updateMessage(String message) {
        super.updateMessage(message);
    }

    @Override
    public void updateProgress(long workDone, long max) {
        super.updateProgress(workDone, max);
    }

    public void updateProgress(double percentage) {
        super.updateProgress(percentage, 1);
    }

    @Override
    public TransparentTask<T> clone() {
        TransparentTask<T> parent = this;
        try {
            return (TransparentTask<T>) super.clone();
        } catch (Exception e) {
            return new TransparentTask<T>() {
                @Override
                public T call() throws Exception {
                    return parent.call();
                }
            };
        }
    }
}
