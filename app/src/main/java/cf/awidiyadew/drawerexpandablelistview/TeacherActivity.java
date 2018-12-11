package cf.awidiyadew.drawerexpandablelistview;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cf.awidiyadew.drawerexpandablelistview.adapter.TeacherAttendCheckAdapter;
import cf.awidiyadew.drawerexpandablelistview.dto.StudentDto;
import cf.awidiyadew.drawerexpandablelistview.item.TeacherAttendCheckItem;
import cf.awidiyadew.drawerexpandablelistview.task.LoginDataTask;
import cf.awidiyadew.drawerexpandablelistview.task.TeacherAttendCheckTask;

public class TeacherActivity extends AppCompatActivity {
    private static final String TAG = "lecture";
    //BluetoothAdapter
    BluetoothAdapter mBluetoothAdapter;

    //블루투스 요청 액티비티 코드
    final static int BLUETOOTH_REQUEST_CODE = 100;

    //UI
//    TextView txtState;
    Button btnSearch;
    ListView studentList;

    //Adapter
    SimpleAdapter adapterDevice;
    TeacherAttendCheckAdapter adapter;

    //Adapter에 담을 객체 생성
    TeacherAttendCheckItem item = null;

    //list - Device 목록 저장
    List<Map<String,String>> dataDevice;

    // DB에서 가져온 맥주소 리스트에 저장후 블루투스 검색시 비교시 사용
    ArrayList<String> studentMacList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        //UI
//        txtState = (TextView)findViewById(R.id.txtState);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        studentList = (ListView)findViewById(R.id.studentList);


        //Adapter
        dataDevice = new ArrayList<>();
//        adapterDevice = new SimpleAdapter(this, dataDevice, android.R.layout.simple_list_item_2, new String[]{"name","address"}, new int[]{android.R.id.text1, android.R.id.text2});
//        studentList.setAdapter(adapterDevice);

        adapter = new TeacherAttendCheckAdapter(this);

        Map<String, String> params = new HashMap<String, String>();
        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);

        params.put("ACADEMY_NAME", setting.getString("ACADEMY_NAME", ""));
        params.put("CLASS_NAME", setting.getString("CLASS_NAME", ""));


        String result = null;
        try {
            TeacherAttendCheckTask teacherAttendCheckTask = new TeacherAttendCheckTask();
            result = teacherAttendCheckTask.execute(params).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.equals("serverFail")) {
            Toast.makeText(this, "서버 접속에 실패 하였습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        JSONObject resultObj = null;
        try {
            JSONArray jArray = new JSONArray( result );

            for(int i=0; i < jArray.length(); i++){
                resultObj = jArray.getJSONObject( i );
                String studentName = resultObj.getString( "STUDENT_NAME" ) ;
                String studentMac = resultObj.getString( "STUDENT_MAC" );
                studentMacList.add( studentMac );
                item = new TeacherAttendCheckItem( studentName, studentMac, R.drawable.delete );

                adapter.addItem( item );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        studentList.setAdapter( adapter );

        //블루투스 지원 유무 확인
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //블루투스를 지원하지 않으면 null을 리턴한다
        if(mBluetoothAdapter == null){
            Toast.makeText(this, "블루투스를 지원하지 않는 단말기 입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //블루투스 브로드캐스트 리시버 등록
        //리시버1
        IntentFilter stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        registerReceiver(mBluetoothStateReceiver, stateFilter);
        //리시버2
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        registerReceiver(mBluetoothSearchReceiver, searchFilter);


        //1. 블루투스가 꺼져있으면 활성화
//        if(!mBluetoothAdapter.isEnabled()){
//            mBluetoothAdapter.enable(); //강제 활성화
//        }

        //2. 블루투스가 꺼져있으면 사용자에게 활성화 요청하기
        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }
    }


    //블루투스 상태변화 BroadcastReceiver
    BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //BluetoothAdapter.EXTRA_STATE : 블루투스의 현재상태 변화
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

            //블루투스 활성화
            if(state == BluetoothAdapter.STATE_ON){
//                txtState.setText("블루투스 활성화");
            }
            //블루투스 활성화 중
            else if(state == BluetoothAdapter.STATE_TURNING_ON){
//                txtState.setText("블루투스 활성화 중...");
            }
            //블루투스 비활성화
            else if(state == BluetoothAdapter.STATE_OFF){
//                txtState.setText("블루투스 비활성화");
            }
            //블루투스 비활성화 중
            else if(state == BluetoothAdapter.STATE_TURNING_OFF){
//                txtState.setText("블루투스 비활성화 중...");
            }
        }
    };

    //블루투스 검색결과 BroadcastReceiver
    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch(action){
                //블루투스 디바이스 검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    dataDevice.clear();
                    Toast.makeText(TeacherActivity.this, "블루투스 검색 시작", Toast.LENGTH_SHORT).show();
                    break;
                //블루투스 디바이스 찾음
                case BluetoothDevice.ACTION_FOUND:
                    //검색한 블루투스 디바이스의 객체를 구한다
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    //데이터 저장
//                    Map map = new HashMap();
//                    map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
//                    map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소

//                    dataDevice.add(map);
//                    //리스트 목록갱신
//                    adapterDevice.notifyDataSetChanged();

                    Log.d("테스트123123",device.getAddress());

                    for(int i=0; i < studentMacList.size(); i++){
                        if(device.getAddress().equals( studentMacList.get( i ) )){
                            adapter.checkImg( i );
                            adapter.notifyDataSetChanged();

                            StudentCheck studentCheck = new StudentCheck();

                            //현재 시간 구하기
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
                            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmm");
                            String checkDate = sdfDate.format(date);
                            String checkTime = sdfTime.format(date);

                            studentCheck.attendCheck(checkDate, checkTime );
                        }
                    }
                    break;
                //블루투스 디바이스 검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(TeacherActivity.this, "블루투스 검색 종료", Toast.LENGTH_SHORT).show();
                    btnSearch.setEnabled(true);
                    break;
            }
        }
    };


    //블루투스 검색 버튼 클릭
    public void mOnBluetoothSearch(View v){
        //검색버튼 비활성화
        btnSearch.setEnabled(false);
        //mBluetoothAdapter.isDiscovering() : 블루투스 검색중인지 여부 확인
        //mBluetoothAdapter.cancelDiscovery() : 블루투스 검색 취소
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        //mBluetoothAdapter.startDiscovery() : 블루투스 검색 시작
        mBluetoothAdapter.startDiscovery();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case BLUETOOTH_REQUEST_CODE:
                //블루투스 활성화 승인
                if(resultCode == Activity.RESULT_OK){

                }
                //블루투스 활성화 거절
                else{
                    Toast.makeText(this, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBluetoothStateReceiver);
        unregisterReceiver(mBluetoothSearchReceiver);
        super.onDestroy();
    }
}


