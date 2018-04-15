package br.ufpe.cin.if1001.rss.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.ufpe.cin.if1001.rss.ui.MainActivity;

public class UpdateReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 767;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context,0, nIntent,0);

        Notification notification = new Notification
                .Builder(context)
                .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                .setContentTitle("RSS")
                .setContentText("Atualização de feed disponível")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        nManager.notify(NOTIFICATION_ID, notification);
    }
}
