package cf.awidiyadew.drawerexpandablelistview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("123123", "11111111111");
        String action = intent.getAction();
        Log.d("123123", "123123");

        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                Log.d("123123", "앱 설치 다시했을 때");
                Intent i = new Intent(context, MyService.class);
                context.startService(i);
        }
    }
}
