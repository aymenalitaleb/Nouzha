package esi.siw.nouzha.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import esi.siw.nouzha.ActivityDetails;
import esi.siw.nouzha.R;
import esi.siw.nouzha.common.Common;

public class ListenNotification extends Service implements ChildEventListener{

    FirebaseDatabase db;
    DatabaseReference notifications, notification ;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        notifications = db.getReference("userNotification");
        Log.e("phone: ", Common.currentUser.getPhone());
        notification = notifications.child(Common.currentUser.getPhone());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notification.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenNotification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // Trigger here
        String activityId = dataSnapshot.getKey();
        Log.e("activityId2: ", activityId);
        if (!activityId.isEmpty()) {
            showNotification(activityId);
        }

    }

    private void showNotification(String activityId) {
        Intent intent = new Intent(this, ActivityDetails.class);
        intent.putExtra("activityId",activityId);

//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xffffff);

        PendingIntent notificationIntent;
        notificationIntent = PendingIntent.getActivity(getBaseContext(),uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        Log.e("activityId2: ", activityId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setTicker("NOUZHA")
                .setContentInfo("New activity")
                .setContentText("New activity may interest you")
                .setContentIntent(notificationIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // If you want to many notification show you need to give unique Id for each Notification
        int randomInt = new Random().nextInt(9999-1)+1;
        manager.notify(randomInt, builder.build());

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
