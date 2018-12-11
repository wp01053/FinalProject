package cf.awidiyadew.drawerexpandablelistview;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.http.HEAD;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cf.awidiyadew.drawerexpandablelistview.data.BaseItem;
import cf.awidiyadew.drawerexpandablelistview.data.CustomDataProvider;
import cf.awidiyadew.drawerexpandablelistview.data.Item;
import cf.awidiyadew.drawerexpandablelistview.task.NotificationCheckTask;
import cf.awidiyadew.drawerexpandablelistview.task.TeacherTelTask;
import cf.awidiyadew.drawerexpandablelistview.dto.StaticVariable;

import cf.awidiyadew.drawerexpandablelistview.views.LevelBeamView;
import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    private MultiLevelListView multiLevelListView;
    SharedPreferences.Editor editor;
    Button button1, button2, button3, button4;
    private Geocoder geocoder ;


    public static String teachernum;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       geocoder = new Geocoder(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON ||
                mBluetoothAdapter.getState() == mBluetoothAdapter.STATE_ON) {
            mBluetoothAdapter.disable();
        } else {
            mBluetoothAdapter.enable();
        }
        confMenu();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        StudentCheck.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ChattingActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);

                params.put("ACADEMY_NAME", setting.getString("ACADEMY_NAME", ""));
                params.put("CLASS_NAME", setting.getString("CLASS_NAME", ""));


                String result = null;
                try {
                    TeacherTelTask teacherTelTask = new TeacherTelTask();
                    result = teacherTelTask.execute(params).get();
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

                teachernum = resultObj.get("TEACHER_TEL").getAsString();
                Log.d("123123", teachernum);
                Messenger messenger = new Messenger(getApplicationContext());
                messenger.sendMessageTo(teachernum, "저 오늘 지각입니다.");

            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        StudentCheck.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confMenu() {
        multiLevelListView = (MultiLevelListView) findViewById(R.id.multiLevelMenu);

        // custom ListAdapter
        ListAdapter listAdapter = new ListAdapter();

        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener(mOnItemClickListener);

        listAdapter.setDataItems(CustomDataProvider.getInitialItems());

        TextView navHeaderName = findViewById(R.id.navHeaderName);

        SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
        String studentName = setting.getString("USER_NAME", "");


        navHeaderName.setText(studentName + "님 환영합니다");
    }

    private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {

            StringBuilder builder = new StringBuilder("\"");

            builder.append(((BaseItem) object).getName());
            if ((((BaseItem) object).getName()).equals("공지사항")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Main2Activity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            } else if ((((BaseItem) object).getName()).equals("자료공유")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        databoardActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            } else if ((((BaseItem) object).getName()).equals("건의 게시판")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        suggestboardActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            } else if ((((BaseItem) object).getName()).equals("오시는 길")) {

                        // 지오코딩(GeoCoding) : 주소,지명 => 위도,경도 좌표로 변환
                        //     위치정보를 얻기위한 권한을 획득, AndroidManifest.xml
                        //    ACCESS_FINE_LOCATION : 현재 나의 위치를 얻기 위해서 필요함
                        //    INTERNET : 구글서버에 접근하기위해서 필요함

//                        Button b2 = (Button)findViewById(R.id.button2);
//                        final EditText et3 = (EditText)findViewById(R.id.editText3);
//
                        List<Address> list = null;

                        String str = StaticVariable.academy_name;


//                String str = null;
                        try {
                            list = geocoder.getFromLocationName(
                                    str, // 지역 이름
                                    10); // 읽을 개수
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                        }

                        if (list != null) {
                            if (list.size() == 0) {
//                        tv.setText("해당되는 주소 정보는 없습니다");
                            } else {
//                        tv.setText(list.get(0).toString());
//                                  list.get(0).getCountryName();  // 국가명
//                                  list.get(0).getLatitude();        // 위도
//                                  list.get(0).getLongitude();    // 경도
                                Log.d("123123", "경도"+String.valueOf(list.get(0).getLongitude()));
                                Log.d("123123", "위도"+String.valueOf(list.get(0).getLatitude()));
                            }
                        }
                                String url ="daummaps://look?p="+String.valueOf(list.get(0).getLatitude()+","+String.valueOf(list.get(0).getLongitude()));
                                Log.d("123123","uri" +url);

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                // Handle the camera action
                                startActivity(intent);



            } else if ((((BaseItem) object).getName()).equals("출석 체크")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        StudentCheck.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            } else if ((((BaseItem) object).getName()).equals("출석 현황")) {

                if(StaticVariable.loginType.equals( "TEACHER" )){
                     Intent intent = new Intent(
                            getApplicationContext(), // 현재 화면의 제어권자
                            Teacherweb.class); // 다음 넘어갈 클래스 지정
                    startActivity(intent); // 다음 화면으로 넘어간다

                }else{
                    Intent intent = new Intent(
                            getApplicationContext(), // 현재 화면의 제어권자
                            CalendarActivity.class); // 다음 넘어갈 클래스 지정
                    startActivity(intent); // 다음 화면으로 넘어간다
                }
            }
            else if ((((BaseItem) object).getName()).equals("CHILD OF GX-A")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        NotificationActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어s간다.


            }
            else if ((((BaseItem) object).getName()).equals("교육 일정")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ManagementActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다.

            }
            else if ((((BaseItem) object).getName()).equals("학원 소개")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ManagementActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다.

            }

            else if ((((BaseItem) object).getName()).equals("선생님용 출석체크")) {

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        TeacherActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            } else if ((((BaseItem) object).getName()).equals("반 채팅")) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ChattingActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다.


            } else if ((((BaseItem) object).getName()).equals("메세지 보내기")) {

                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);

                params.put("ACADEMY_NAME", setting.getString("ACADEMY_NAME", ""));
                params.put("CLASS_NAME", setting.getString("CLASS_NAME", ""));


                String result = null;
                try {
                    TeacherTelTask teacherTelTask = new TeacherTelTask();
                    result = teacherTelTask.execute(params).get();
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

                teachernum = resultObj.get("TEACHER_TEL").getAsString();
                Log.d("123123", teachernum);
                Messenger messenger = new Messenger(getApplicationContext());
                messenger.sendMessageTo(teachernum, "저 오늘 지각입니다.");
            } else if ((((BaseItem) object).getName()).equals("로그아웃")) {
                SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);

                editor = setting.edit();
                editor.clear();
//                editor.remove("setting");
                editor.commit();

                StaticVariable.staticVariableLogout();

                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        IntroActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                Toast.makeText(MainActivity.this, "로그아웃 완료", Toast.LENGTH_SHORT).show();
            }
//            else if((((BaseItem) object).getName()).equals("MyPage")){
//
//                Intent intent = new Intent(
//                        getApplicationContext(), // 현재 화면의 제어권자
//                        MapActivity.class); // 다음 넘어갈 클래스 지정
//                startActivity(intent); // 다음 화면으로 넘어간다
//
//            }
            builder.append("\" clicked!\n");
            builder.append(getItemInfoDsc(itemInfo));

            Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };


    private class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView;
            LevelBeamView levelBeamView;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            // DIEKSEKUSI SAAT KLIK PADA GROUP-ITEM
            return CustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return CustomDataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.data_item, null);
                //viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            //viewHolder.infoView.setText(getItemInfoDsc(itemInfo));

            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.ic_expand_less : R.drawable.ic_expand_more);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            return convertView;
        }
    }

    class Messenger {
        private Context mContext;

        public Messenger(Context mContext) {
            this.mContext = mContext;
        }

        public void sendMessageTo(String phoneNum, String message) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNum, null, message, null, null);

            Toast.makeText(mContext, "Message completed", Toast.LENGTH_SHORT).show();
        }
    }

    private String getItemInfoDsc(ItemInfo itemInfo) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("level[%d], idx in level[%d/%d]",
                itemInfo.getLevel() + 1, /*Indexing starts from 0*/
                itemInfo.getIdxInLevel() + 1 /*Indexing starts from 0*/,
                itemInfo.getLevelSize()));

        if (itemInfo.isExpandable()) {
            builder.append(String.format(", expanded[%b]", itemInfo.isExpanded()));
        }
        return builder.toString();
    }
}
