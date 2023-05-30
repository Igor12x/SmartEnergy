package Models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "Que tal conferir o quanto você já consumiu de energia hoje?";
        NotificationHelper.showNotification(context, message);
    }
}
