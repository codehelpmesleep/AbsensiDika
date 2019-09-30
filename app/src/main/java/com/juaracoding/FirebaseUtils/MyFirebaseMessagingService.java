package com.juaracoding.FirebaseUtils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.juaracoding.absensidika.Permission.activity.PermissionApproval;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;


/**
 * Created by prabh on 25-10-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private Bitmap bitmap;
    private Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            sendUserNotification(remoteMessage.getData().get("TITLE"), remoteMessage.getData().get("BODY"),Integer.parseInt(remoteMessage.getData().get("ID")));
        }

    //    Intent intent = new Intent(MyFirebaseMessagingService.this,MapsNotification.class);
   //     intent.putExtra("panicid",Integer.parseInt(remoteMessage.getData().get("ID")));

     //   startActivity(intent);

    }


    private void sendUserNotification(String title, String mess,int idpanic) {
        int notifyID = 1;
        Intent intent;
        NotificationChannel mChannel;
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.panic2);

        intent = new Intent(context, PermissionApproval.class);
        intent.putExtra("panicid",idpanic);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = context.getPackageName();// The id of the channel.
        CharSequence name = "Sample one";// The user-visible name of the channel.
        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setSound(soundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mess));
        notificationBuilder.setContentText(mess);
        //notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder));


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_INSISTENT;



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setSound(soundUri,audioAttributes);
            notificationManager.createNotificationChannel(mChannel);
        }
        if (notificationManager != null) {
            notificationManager.notify(notifyID /* ID of notification */, notification);
        }


    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x036085;
            notificationBuilder.setColor(color);
            return R.mipmap.ic_launcher;

        } else {
            return R.mipmap.ic_launcher;
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        AppUtil.setSetting(context,"firebaseId",s);
    }

}
