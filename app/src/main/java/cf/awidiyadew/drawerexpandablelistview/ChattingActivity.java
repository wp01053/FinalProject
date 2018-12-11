package cf.awidiyadew.drawerexpandablelistview;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.w3c.dom.Text;

public class ChattingActivity extends AppCompatActivity {


    private Button user_next;
    private ListView chat_list;
    private TextView user_chat, user_edit;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        user_chat = (TextView) findViewById(R.id.user_chat);
        user_edit = (TextView) findViewById(R.id.user_edit);
        user_next = (Button) findViewById(R.id.user_next);
//        chat_list = (ListView) findViewById(R.id.chat_list);
        SharedPreferences setting = getSharedPreferences("setting", Context.MODE_MULTI_PROCESS);
        final String ACADEMY_NAME = setting.getString("ACADEMY_NAME", "");
        final String userName = setting.getString("USER_NAME", "");
        final String CLASS_NAME = setting.getString("CLASS_NAME", "");
        user_chat.setText(ACADEMY_NAME + CLASS_NAME);
        user_edit.setText(userName);
        Log.d("sssdfasdf", ACADEMY_NAME);
        Log.d("sssdfasdf", userName);
        Log.d("sssdfasdf", CLASS_NAME);

        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChattingActivity.this, Main3Activity.class);
                intent.putExtra("chatName", ACADEMY_NAME + CLASS_NAME);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }

        });

//        showChatList();

    }
}

//    private void showChatList() {
//
//        // 리스트 어댑터 생성 및 세팅
//        final ArrayAdapter<String> adapter
//
//                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//
//        chat_list.setAdapter(adapter);
//
//        chat_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                final String list = adapter.getItem(position);
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChattingActivity.this);
//                builder.setMessage("방 삭제 하시겠습니까?")
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setCancelable(true)
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id){
//                                dialog.cancel();
//                            }
//                        })
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                String key = databaseReference.child("chat").child(list).getKey();
//                                databaseReference.child("chat").child(list).removeValue();
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.show();
//
//                return true;
//            }
//        });
//
//
//        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String list = adapter.getItem(position);
//                SharedPreferences setting = getSharedPreferences( "setting", MODE_PRIVATE );
//                final String ACADEMY_NAME = setting.getString("ACADEMY_NAME", "");
//                final String userName = setting.getString("USER_NAME", "");
//                final String CLASS_NAME = setting.getString("CLASS_NAME", "");
//
//                Log.d("sssdfasdf", ACADEMY_NAME);
//                Log.d("sssdfasdf", userName);
//                Log.d("sssdfasdf", CLASS_NAME);
//                Intent intent = new Intent(ChattingActivity.this,ChattingMainActivity.class);
//                intent.putExtra("chatName",ACADEMY_NAME+CLASS_NAME);
//                intent.putExtra("userName",userName);
//                startActivity(intent);
//            }
//        });


//        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
//        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
//                adapter.add(dataSnapshot.getKey());
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                adapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                adapter.remove(dataSnapshot.getKey());
//
//                adapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                adapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

