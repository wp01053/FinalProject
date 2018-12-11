package cf.awidiyadew.drawerexpandablelistview;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;
    Date dt = new Date();

    SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm:ss a");

    SimpleDateFormat sdf = new SimpleDateFormat("hhmm");

    public ServiceThread(Handler handler){
        this.handler = handler;
        Log.d("123123",dt.toString());
        Log.d("123123",full_sdf.format(dt).toString());
        Log.d("123123",sdf.format(dt).toString());
    }

    public void stopForever(){
        synchronized (this) {
            Log.d("123123", "서비스 종료");
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            handler.sendEmptyMessage(0);
          //쓰레드에 있는 핸들러에게 메세지를 보냄
            Log.d("123123", "서비스 실행 시작 쓰레드 부분");
            try { //30초씩 쉰다.
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


