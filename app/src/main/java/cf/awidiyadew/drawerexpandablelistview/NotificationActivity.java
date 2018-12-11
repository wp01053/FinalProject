package cf.awidiyadew.drawerexpandablelistview;

import android.content.Intent;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.Toast;


public class NotificationActivity extends AppCompatActivity {

    private Button btnStart, btnEnd;
    static boolean isRunning = true;
    static boolean isAlive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnEnd = (Button) findViewById(R.id.btnEnd);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Service 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NotificationActivity.this, MyService.class);
                Log.d("123123","222222222222");

                startService(intent);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Service 끝", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NotificationActivity.this, MyService.class);
                Log.d("123123","333333333333333");

                stopService(intent);
            }
        });
    }
    @Override
    protected void onPause() {
        isAlive = false;
        super.onPause();
    }
    @Override
   protected void onResume() {
       isAlive = true;
        super.onResume();
    }

   public void restart() {
        Intent intent = this.getIntent();
        this.finish();
        this.startActivity(intent);
   }
}

