package cf.awidiyadew.drawerexpandablelistview;

import android.app.NotificationChannel;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cf.awidiyadew.drawerexpandablelistview.item.TeacherAttendCheckItem;
import cf.awidiyadew.drawerexpandablelistview.task.NotificationCheckTask;
import cf.awidiyadew.drawerexpandablelistview.task.TeacherAttendCheckTask;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class MyService extends Service {
    NotificationManager notifiManager;
    ServiceThread thread;
    Notification Notifi;
    public static String classstart;
    public static String classend;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("123123","service start");


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //서비스가 종료될 때 할 작업
    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Toast.makeText(MyService.this, "Service 처음", Toast.LENGTH_SHORT).show();
        Log.d("123123", "service 하러 옴 처음");



        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);

        params.put("ACADEMY_NAME", setting.getString("ACADEMY_NAME", ""));
        params.put("CLASS_NAME", setting.getString("CLASS_NAME", ""));


        String result = null;
        try {
            NotificationCheckTask notificationCheckTask = new NotificationCheckTask();
            result = notificationCheckTask.execute(params).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("123123", result + " : result 값");
//        if (result.equals("serverFail")) {
//            Toast.makeText(this, "서버 접속에 실패 하였습니다", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
 //       Object obj = parser.parse(result);

        JsonObject resultObj = gson.fromJson(result, JsonObject.class);

        classstart = resultObj.get("CLASS_START").getAsString();
        classend = resultObj.get("CLASS_END").getAsString();



        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;

    }



    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            Date dt = new Date();

            SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss a");

            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            Log.d("123123", "서비스 실행 시작 run111");
            Log.d("123123","현재시간 초단위 "+full_sdf.format(dt));
            Log.d("123123","현재시간 : "+ sdf.format(dt));
            String date1 =  sdf.format(dt) ;
            Log.d("123123", "시작시간 : " +classstart);
            Log.d("123123", "퇴근시간 : " +classend);

            //출근 알람
            if (date1.equals(classstart)) {
                String channelId = "channel";
                String channelName = "Channel Name";

                Log.d("123123", "myServiceHandler 동작 시작 !");
                NotificationManager notifManager
                        = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, importance);
                    notifManager.createNotificationChannel(mChannel);
                }
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext(), channelId);
                //알람이 뜨고 그 알람을 클릭했을 시에 들어가는 액티비티 지금은 main
                Intent notificationIntent = new Intent(getApplicationContext()
                        , MainActivity.class);
                if(mBluetoothAdapter == null){
                    Log.d("123123","블루투스 지원 x");
                }else{
                    if(!mBluetoothAdapter.isEnabled()){
                        mBluetoothAdapter.enable();
                        Log.d("123123","블루투스 활성화");
                    }else{
                        mBluetoothAdapter.disable();
                        Log.d("123123","블루투스 비활성화");
                        mBluetoothAdapter.enable();
                    }
                }
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                int requestID = (int) System.currentTimeMillis();
                PendingIntent pendingIntent
                        = PendingIntent.getActivity(getApplicationContext()
                        , requestID
                        , notificationIntent
                        , PendingIntent.FLAG_UPDATE_CURRENT);
                //알람 바에 뜨는 내용들 변경 가능 오레오버전부터 notification 쓰는 방식이 바뀜
                builder.setContentTitle("출근") // required
                        .setContentText("5분전 입니다.")  // required
                        .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                        .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                        .setSound(RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSmallIcon(android.R.drawable.btn_star)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources()
                                , R.drawable.check))
                        .setBadgeIconType(R.drawable.account)
                        .setContentIntent(pendingIntent);
                notifManager.notify(0, builder.build());

            }

            //퇴근 알람
            if (date1.equals(classend)) {
                String channelId = "channel";
                String channelName = "Channel Name";

                Log.d("123123", "myServiceHandler 동작 시작 !");
                NotificationManager notifManager
                        = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, importance);
                    notifManager.createNotificationChannel(mChannel);
                }

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext(), channelId);
                //알람이 뜨고 그 알람을 클릭했을 시에 들어가는 액티비티 지금은 main
                Intent notificationIntent = new Intent(getApplicationContext()
                        , MainActivity.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                int requestID = (int) System.currentTimeMillis();
                PendingIntent pendingIntent
                        = PendingIntent.getActivity(getApplicationContext()
                        , requestID
                        , notificationIntent
                        , PendingIntent.FLAG_UPDATE_CURRENT);
                //알람 바에 뜨는 내용들 변경 가능 오레오버전부터 notification 쓰는 방식이 바뀜
                builder.setContentTitle("퇴근") // required
                        .setContentText("5분전 입니다.")  // required
                        .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                        .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                        .setSound(RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSmallIcon(android.R.drawable.btn_star)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources()
                                , R.drawable.check))
                        .setBadgeIconType(R.drawable.account)
                        .setContentIntent(pendingIntent);
                notifManager.notify(0, builder.build());
            }
            else if (!sdf.format(dt).equals("1745")) {
                Log.d("123123", "두시오분이아님");
            }
//
//            Intent intent = new Intent(MyService.this, NotificationActivity.class);
//            if (!NotificationActivity.isRunning) {
//                intent = new Intent();
//                if (!NotificationActivity.isAlive) {
//                    Toast.makeText(MyService.this, "Service 중간", Toast.LENGTH_SHORT).show();
//
//                    new NotificationActivity().restart();
//                }
//            }

//        }
        }

    }
}
