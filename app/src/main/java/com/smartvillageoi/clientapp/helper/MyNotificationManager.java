package com.smartvillageoi.clientapp.helper;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.smartvillageoi.clientapp.R;
import com.smartvillageoi.clientapp.activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class MyNotificationManager {
    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;

    final Context mCtx;
    final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void showBigNotification(String title, String message, String url, Intent intent) {

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, ID_BIG_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(Html.fromHtml(title).toString());
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo_login).setTicker(title)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setColor(ContextCompat.getColor(MainActivity.activity, R.color.colorPrimary))
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setContentIntent(resultPendingIntent)
                .setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message).toString())
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.drawable.logo_login)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo_login))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSound(defaultSoundUri)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    public void showSmallNotification(String title, String message, Intent intent) {
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent resultPendingIntent = PendingIntent.getActivity(mCtx, ID_SMALL_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "notification");
        Notification notification;
        notification = mBuilder.setSmallIcon(R.drawable.logo_login).setTicker(title)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setColor(ContextCompat.getColor(MainActivity.activity, R.color.colorPrimary))
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setContentIntent(resultPendingIntent)
                .setContentTitle(Html.fromHtml(title).toString())
                .setContentText(Html.fromHtml(message).toString())
                .setSmallIcon(R.drawable.logo_login)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.logo_login))
                .setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(message).toString()))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSound(defaultSoundUri)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
        }
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }


    //The method will return Bitmap from an image URL
    Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {

            return null;
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    void createChannel(NotificationManager notificationManager) {
        String name = "notification";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel mChannel = new NotificationChannel("notification", name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        mChannel.setShowBadge(true);
        mChannel.setLightColor(ContextCompat.getColor(MainActivity.activity, R.color.colorPrimary));
        mChannel.setSound(defaultSoundUri, new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build());
        notificationManager.createNotificationChannel(mChannel);
    }


}
