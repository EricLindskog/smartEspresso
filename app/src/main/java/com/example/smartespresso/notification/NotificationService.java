package com.example.smartespresso.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.smartespresso.MainActivity;
import com.example.smartespresso.R;

public class NotificationService {
    private Context ctx;
    public NotificationService(Context ctx){
        this.ctx = ctx;
    }
    public void postNotification(){
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "temp")
                .setSmallIcon(R.drawable.smartespresso_logo)
                .setContentTitle(ctx.getString(R.string.temp_notification_title))
                .setContentText(ctx.getString(R.string.temp_notification_body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{500})
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
    public void createChannel(){
        CharSequence name = "temperature";
        String description = "temperature information";//getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
        channel.setDescription(description);
        channel.setVibrationPattern(new long[]{500});
        NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
