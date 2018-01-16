package ru.toster.uggu;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    NotificationManager nm;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotif();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    void sendNotif() {
        // 1-я часть
//        Notification.Builder notif = new Notification.Builder();
//
//        // 3-я часть
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        // 2-я часть
//        notif.setLatestEventInfo(this, "Notification's title", "Notification's text", pIntent);
//
//        // ставим флаг, чтобы уведомление пропало после нажатия
//        notif.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // отправляем
//        nm.notify(1, notif);

    }
}
