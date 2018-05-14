package esi.siw.nouzha.models;

import java.sql.Timestamp;

/**
 * Created by Creator on 23/03/2018.
 */

public class Notification {

    private Timestamp date;
    private Activity activity;
    private Boolean seen;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
