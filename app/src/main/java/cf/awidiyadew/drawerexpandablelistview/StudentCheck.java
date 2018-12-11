package cf.awidiyadew.drawerexpandablelistview;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cf.awidiyadew.drawerexpandablelistview.dto.StaticVariable;
import cf.awidiyadew.drawerexpandablelistview.task.AttendCheckTask;

public class StudentCheck extends AppCompatActivity {

    private static final String TAG = "lecture";
    private BeaconManager beaconManager;
    private Region region;
    private TextView SCheck;
    private Button button1;
    private boolean isConnected;
    final int DIALOG_PROGRESS1 = 1;
    ProgressDialog pb;

    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdfTime = new SimpleDateFormat("HHmm");

    // 변수에 날짜 값 저장한다.
    String checkDate = sdfDate.format(date);
    String checkTime = sdfTime.format(date);

    TextView TCheck;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //블루투스 어댑터 객체 얻기


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "123123123");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "444444444");
        setContentView(R.layout.activity_student_check);
        Log.d(TAG, "555555555");

        SCheck = findViewById(R.id.SCheck);
        beaconManager = new BeaconManager(this);
        isConnected = false;
        TCheck = (TextView) findViewById(R.id.TCheck);


        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            boolean boolflag = false;

            @Override
            public void onClick(View v) {
                showDialog(DIALOG_PROGRESS1);
                Log.d(TAG, "출석체크 버튼 클릭");
                Log.d(TAG, boolflag + "처음 booflag 확인값");

                //        add this below:
                beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                    @Override
                    public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                        isConnected = true;
                        Beacon nearestBeacon = list.get(0);
                        Log.d("Airport", "비콘 거리: " + nearestBeacon.getRssi());
                        // nearestBeacon.getRssi() : 비콘의 수신 강도
                        SCheck.setText("현재 비콘과의 거리"+nearestBeacon.getRssi());
//                     수신강도가 -70 이상일때 알림창을 띄운다.
                        if (isConnected == true) {
                            Log.d(TAG, boolflag + "중간 booflag 확인값");


                            AlertDialog.Builder dialog = new AlertDialog.Builder(StudentCheck.this);
                            dialog.setTitle("알림")
                                    .setMessage("출석이 완료되었습니다.")

                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .create().show();
                            pb.dismiss();
                            //출석 시간 확인란

                            TCheck.setText("출석 시간 : " + checkDate+" "+checkTime);

                            String result = attendCheck(checkDate, checkTime);

                            if(result.equals("attendFail")){
                                Toast.makeText(StudentCheck.this, "출석체크에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            boolflag = true;
                            Log.d(TAG, boolflag + "출석체크 버튼 클릭 끝");
                            Log.d(TAG, isConnected + "비콘 체크 중");

                        } else if (isConnected && nearestBeacon.getRssi() < -70) {
                            Toast.makeText(StudentCheck.this, "연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                            isConnected = false;
                        }
                        Log.d(TAG, isConnected + "비콘 체크 중222");
                        isConnected = false;
                        if (isConnected == false) {
                            onPause();

                        }

                    }

                });
                bluetoothOff();

            }


        });
        //                boolflag = true;

        region = new Region("ranged region",
                UUID.fromString("FDA50693-A4E2-4FB1-AFCF-C6EB07647825"), 10004, 54480);
//        본인이 연결할 Beacon의 ID와 Major / Minor Code를 알아야 한다.
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PROGRESS1:
                pb = new ProgressDialog(StudentCheck.this);

                pb.setProgress(30);
                pb.setTitle("출석체크 중입니다.");
                pb.setMessage("잠시만 기다려주세요");
                pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pb.setCanceledOnTouchOutside(false);
                return pb;
        }
        return super.onCreateDialog(id);
    }

    public void bluetoothOff() {
        mBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        // 블루투스 권한 및 활성화 코드드

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }


    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);
        super.onPause();
    }

    public String attendCheck(String paramDate, String paramTime){
        Map<String, String> params = new HashMap<String, String>();


        // 학생 조회를 위한 데이터

        params.put("ACADEMY_NAME", StaticVariable.academy_name);
        params.put("CLASS_NAME", StaticVariable.class_name);
        params.put("STUDENT_NAME", StaticVariable.userName);

        // 출석 일자, 시간
        params.put("ATTEND_DATE",  paramDate);
        params.put("ATTEND_TIME", paramTime);

        String result = null;
        try{
            AttendCheckTask attendCheckTask = new AttendCheckTask();
            result = attendCheckTask.execute( params ).get();
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


}
