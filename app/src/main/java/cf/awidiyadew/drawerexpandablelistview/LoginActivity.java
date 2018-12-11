package cf.awidiyadew.drawerexpandablelistview;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import cf.awidiyadew.drawerexpandablelistview.dto.StaticVariable;
import cf.awidiyadew.drawerexpandablelistview.dto.StudentDto;
import cf.awidiyadew.drawerexpandablelistview.task.LoginDataTask;

import static cf.awidiyadew.drawerexpandablelistview.MainActivity.*;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    EditText input_ID, input_PW;
    CheckBox Auto_LogIn;
    Button btn_login;

    String userId, userPw;

   SharedPreferences setting;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_ID = (EditText) findViewById(R.id.input_ID);
        input_PW = (EditText) findViewById(R.id.input_PW);
        Auto_LogIn = (CheckBox) findViewById(R.id.Auto_LogIn);
        btn_login = (Button) findViewById(R.id.btn_login);

        setting = getSharedPreferences("setting", 0);

        editor = setting.edit();


        btn_login.setOnClickListener(new View.OnClickListener() {
            //이번 버전부터 MAC 주소 구하는 부분이 변경됨 이게 로컬 MAC 구하는 부분ㅇ
            private String getBluetoothMacAddress() {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                String bluetoothMacAddress = "";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                    try {
                        Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                        mServiceField.setAccessible(true);

                        Object btManagerService = mServiceField.get(bluetoothAdapter);

                        if (btManagerService != null) {
                            bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                        }
                    } catch (NoSuchFieldException e) {

                    } catch (NoSuchMethodException e) {

                    } catch (IllegalAccessException e) {

                    } catch (InvocationTargetException e) {

                    }

                } else {
                    bluetoothMacAddress = bluetoothAdapter.getAddress();
                }
                Log.d(TAG, bluetoothMacAddress);

                return bluetoothMacAddress;

            }

            @Override
            public void onClick(View view) {
                Log.d( TAG, getBluetoothMacAddress() + "이게 로컬 MAC 주소" );


                if (input_ID.getText().length() == 0 || input_PW.getText().length() == 0) {
                    Toast.makeText( LoginActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT ).show();
                    return;
                }

                userId = input_ID.getText().toString();
                userPw = input_PW.getText().toString();

                String result = accountCheck( userId, userPw );

                if (result.equals( "serverFail" )) {
                    Toast.makeText( LoginActivity.this, "서버 접속에 실패 하였습니다", Toast.LENGTH_SHORT ).show();
                    return;
                }

                if (result.equals( "loginFail" )) {
                    loginFail();
                    return;
                }

                if (result.equals( "loginSuccess" )) {
                    Toast.makeText( LoginActivity.this, userId + "님 로그인 성공", Toast.LENGTH_SHORT ).show();
                }
            }
        });


        Auto_LogIn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    String ID = input_ID.getText().toString();
                    String PW = input_PW.getText().toString();

                    editor.putString("USER_NAME", ID);
                    editor.putString("USER_PW", PW);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                } else {
                    /**
                     * remove로 지우는것은 부분삭제
                     * clear로 지우는것은 전체 삭제 입니다
                     */
//					editor.remove("ID");
//					editor.remove("PW");
//					editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (setting.getBoolean("Auto_Login_enabled", false)) {

            userId = setting.getString("USER_NAME", "");
            userPw = setting.getString("USER_PW", "");

            Auto_LogIn.setChecked(true);

            String result = accountCheck( userId, userPw );

            if(result.equals( "serverFail" )){
                Toast.makeText(LoginActivity.this, "서버 접속에 실패 하였습니다", Toast.LENGTH_SHORT).show();
                return;
            }

            if(result.equals( "loginFail" )){
                autoLoginFail();
                StaticVariable.staticVariableLogout();
                return;
            }

            if (result.equals( "loginSuccess" )) {
                Toast.makeText( this, userId + "님 자동 로그인 성공", Toast.LENGTH_SHORT ).show();
            }
        }
    }



    public String accountCheck(String userId, String userPw){

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("userPw", userPw);

        String result = null;
        try {
            LoginDataTask loginDataTask = new LoginDataTask();
            result = loginDataTask.execute(params).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result.equals("serverFail")) {
            return "serverFail";
        }

        if(result == null){
            return "loginFail";
        }

        Gson gson = new Gson();
        StudentDto userData = gson.fromJson(result, StudentDto.class);

        String loginResult = loginCheck( userData );

        return loginResult;

    }



    public String loginCheck(StudentDto userData) {

        setting = getSharedPreferences("setting", MODE_PRIVATE);

        if (userPw.equals(userData.getUSER_PW())) {
            loginSuccess(userData, setting);
            return "loginSuccess";
        }
        return "loginFail";
    }


    protected void loginSuccess(StudentDto userData, SharedPreferences setting) {

        editor = setting.edit();

        editor.putString("ACADEMY_NAME", userData.getACADEMY_NAME());
        editor.putString("CLASS_NAME", userData.getCLASS_NAME());
        editor.putString("USER_NAME", userData.getUSER_NAME());
        editor.putString("USER_GENDER", userData.getUSER_GENDER());
        editor.putString("USER_TEL", userData.getUSER_TEL());
        editor.putString("USER_PW", userData.getUSER_PW());
        editor.putString("USER_BIRTH", userData.getUSER_BIRTH());
        editor.putString("USER_MAC",userData.getUSER_MAC());
        editor.putString("GUARDIAN_NAME", userData.getGUARDIAN_NAME());
        editor.putString("GUARDIAN_TEL", userData.getGUARDIAN_TEL());

        editor.putString("LOGIN_TYPE", userData.getLOGIN_TYPE());
        editor.commit();

        StaticVariable sv = new StaticVariable();
        sv.academy_name = setting.getString( "ACADEMY_NAME", "" );
        sv.class_name = setting.getString( "CLASS_NAME", "" );
        sv.userName = setting.getString( "USER_NAME", "" );
        sv.userGender = setting.getString( "USER_GENDER", "" );
        sv.userTel = setting.getString( "USER_TEL", "" );
        sv.userPw = setting.getString( "USER_PW", "" );
        sv.userBirth = setting.getString( "USER_BIRTH", "" );
        sv.userMAC = setting.getString( "USER_MAC", "" );
        sv.guardianName = setting.getString( "GUARDIAN_NAME", "" );
        sv.guardianTel = setting.getString( "GUARDIAN_TEL", "" );
        sv.loginType = setting.getString( "LOGIN_TYPE", "" );

        Intent intent = new Intent(
                this, // 현재 화면의 제어권자
                MainActivity.class); // 다음 넘어갈 클래스 지정
        startActivity(intent); // 다음 화면으로 넘어간다
    }

    public void loginFail() {
        Toast.makeText(this, "아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
    }

    public void autoLoginFail() {
        Toast.makeText(this, "자동 로그인에 실패 하였습니다", Toast.LENGTH_SHORT).show();
    }
}
