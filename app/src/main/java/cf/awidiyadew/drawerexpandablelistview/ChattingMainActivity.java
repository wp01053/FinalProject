package cf.awidiyadew.drawerexpandablelistview;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cf.awidiyadew.drawerexpandablelistview.dto.ChatDTO;

public class ChattingMainActivity extends AppCompatActivity {

    private String CHAT_NAME;
    private String USER_NAME;

    private ListView chat_view;
    private EditText chat_edit;
    private TextView class_name;
    private Button chat_send;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    ChattingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_main);

        // 위젯 ID 참조
        chat_view = (ListView) findViewById(R.id.chat_view);
        chat_edit = (EditText) findViewById(R.id.chat_edit);
        chat_send = (Button) findViewById(R.id.chat_sent);
        class_name = findViewById(R.id.class_name);

        SharedPreferences setting = getSharedPreferences( "setting", MODE_PRIVATE );
        final String ACADEMY_NAME = setting.getString("ACADEMY_NAME", "");
        final String userName = setting.getString("USER_NAME", "");
        final String CLASS_NAME = setting.getString("CLASS_NAME", "");

        class_name.setText(ACADEMY_NAME+" : "+CLASS_NAME);
        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        CHAT_NAME = ACADEMY_NAME+CLASS_NAME;
        USER_NAME = userName;
        adapter = new ChattingAdapter(getApplicationContext());

        Log.d("123123",ACADEMY_NAME+CLASS_NAME);
        Log.d("123123",userName);

        // 채팅 방 입장
        openChat(CHAT_NAME);
        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
                chat_edit.setText(""); //입력창 초기화
            }

        });
    }

    private void addMessage(DataSnapshot dataSnapshot, ChattingAdapter adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.addItem(chatDTO.getUserName(),chatDTO.getMessage());
        adapter.notifyDataSetChanged();

    }

    private void removeMessage(DataSnapshot dataSnapshot, ChattingAdapter adapter) {
        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
        adapter.removeItem(chatDTO.getUserName(),chatDTO.getMessage());
        adapter.notifyDataSetChanged();
    }

    private void openChat(String chatName) {
        Log.d("123123", "대화방 입장 ;");
        // 리스트 어댑터 생성 및 세팅
//        final ArrayAdapter<String> adapter
//
//                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        adapter.notifyDataSetChanged();
        chat_view.setAdapter(adapter);
        Log.d("123123", "adapter 로 이동 ;");

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
